#!/usr/bin/env python3
"""
bridge.py — Pushpaka MAVLink bridge

Connects to an ArduPilot SITL instance via MAVLink and bridges relevant
flight events to the Pushpaka REST APIs.

Capability:
  39a - Connect to SITL; log HEARTBEAT state and armed-state changes
  39b - Intercept ARM commands; send immediate DISARM if no valid AUT found

Usage:
  python3 bridge.py [--sitl-host HOST] [--sitl-port PORT]
                    [--flight-auth-url URL]
                    [--require-aut]
                    [--verbose]

Defaults:
  --sitl-host       localhost
  --sitl-port       5760   (TCP; matches .devcontainer docker-compose SITL profile)
  --flight-auth-url http://localhost:8083

ARM enforcement (39b):
  Pass --require-aut to enable.
  Set PUSHPAKA_TOKEN env var to a valid Keycloak bearer token so the bridge
  can call the authenticated flight-auth API to check for a current AUT.

  When the bridge detects an ARM and no CREATED/INUSE AUT is found, it
  immediately sends MAV_CMD_COMPONENT_ARM_DISARM (disarm) to SITL, reversing
  the arm. This is the same reactive approach used by the QGC plugin (#76).
"""

import argparse
import logging
import os
import sys
import time

try:
    from pymavlink import mavutil
except ImportError:
    print("ERROR: pymavlink not installed. Run: pip install -r requirements.txt")
    sys.exit(1)

import requests

# ── Logging ───────────────────────────────────────────────────────────────────

logging.basicConfig(
    format="%(asctime)s  %(levelname)-8s  %(message)s",
    datefmt="%H:%M:%S",
    level=logging.INFO,
)
log = logging.getLogger("bridge")

MAV_STATE_NAMES = {
    0: "UNINIT",
    1: "BOOT",
    2: "CALIBRATING",
    3: "STANDBY",
    4: "ACTIVE",
    5: "CRITICAL",
    6: "EMERGENCY",
    7: "POWEROFF",
    8: "FLIGHT_TERMINATION",
}

AUT_ACTIVE_STATES = {"CREATED", "INUSE"}


# ── Bridge ────────────────────────────────────────────────────────────────────

class PushpakaBridge:
    def __init__(
        self,
        sitl_host: str,
        sitl_port: int,
        flight_auth_url: str,
        require_aut: bool,
        token: str | None,
    ):
        self.sitl_addr = f"tcp:{sitl_host}:{sitl_port}"
        self.flight_auth_url = flight_auth_url.rstrip("/")
        self.require_aut = require_aut
        self.token = token
        self._conn = None
        self._last_system_status = None
        self._last_armed = None

    def connect(self) -> bool:
        log.info("Connecting to SITL at %s …", self.sitl_addr)
        try:
            self._conn = mavutil.mavlink_connection(self.sitl_addr)
            log.info("Waiting for HEARTBEAT …")
            self._conn.wait_heartbeat(timeout=30)
            log.info(
                "Connected — system %d component %d",
                self._conn.target_system,
                self._conn.target_component,
            )
            if self.require_aut:
                log.info("AUT enforcement enabled (--require-aut)")
                if not self.token:
                    log.warning(
                        "PUSHPAKA_TOKEN not set — AUT checks will always fail "
                        "and ARM will always be blocked"
                    )
            return True
        except Exception as e:
            log.error("Connection failed: %s", e)
            return False

    def run(self):
        if not self.connect():
            sys.exit(1)

        log.info("Bridge running. Ctrl-C to stop.")
        while True:
            try:
                msg = self._conn.recv_match(blocking=True, timeout=5)
                if msg is None:
                    continue
                self._handle(msg)
            except KeyboardInterrupt:
                log.info("Stopped.")
                break
            except Exception as e:
                log.warning("recv error: %s", e)
                time.sleep(1)

    def _handle(self, msg):
        msg_type = msg.get_type()

        if msg_type == "HEARTBEAT":
            self._on_heartbeat(msg)
        elif msg_type == "COMMAND_LONG":
            self._on_command_long(msg)

    # ── HEARTBEAT ─────────────────────────────────────────────────────────────

    def _on_heartbeat(self, msg):
        status = msg.system_status
        armed = bool(msg.base_mode & mavutil.mavlink.MAV_MODE_FLAG_SAFETY_ARMED)

        # Log system status transitions
        if status != self._last_system_status:
            name = MAV_STATE_NAMES.get(status, str(status))
            log.info(
                "HEARTBEAT  state=%-16s  armed=%s  type=%d  autopilot=%d",
                name, armed, msg.type, msg.autopilot,
            )
            self._last_system_status = status

        # Log armed state transitions
        if armed != self._last_armed:
            if armed:
                log.info("Vehicle ARMED")
            else:
                log.info("Vehicle DISARMED")
            self._last_armed = armed

    # ── ARM intercept (39b) ───────────────────────────────────────────────────

    def _on_command_long(self, msg):
        if msg.command != mavutil.mavlink.MAV_CMD_COMPONENT_ARM_DISARM:
            return

        arm_requested = (msg.param1 == 1.0)
        if not arm_requested:
            return  # disarm commands pass through

        if not self.require_aut:
            log.debug("ARM command received (AUT enforcement disabled — passing through)")
            return

        log.info("ARM command received — checking AUT …")

        if self._has_active_aut():
            log.info("ARM allowed — valid AUT found")
        else:
            log.warning("ARM BLOCKED — no active AUT; sending disarm")
            self._send_disarm()

    def _has_active_aut(self) -> bool:
        """Return True if the flight-auth service has at least one active AUT."""
        auts = self._flight_auth_get("/api/v1/airspaceUsageToken/find")
        if not isinstance(auts, list):
            log.warning("Could not retrieve AUT list — blocking ARM by default")
            return False

        for aut in auts:
            if aut.get("state") in AUT_ACTIVE_STATES:
                log.debug("Active AUT found: id=%s state=%s", aut.get("id"), aut.get("state"))
                return True

        log.info("No active AUT found (checked %d AUT(s))", len(auts))
        return False

    def _send_disarm(self):
        """Send an immediate disarm command to SITL."""
        self._conn.mav.command_long_send(
            self._conn.target_system,
            self._conn.target_component,
            mavutil.mavlink.MAV_CMD_COMPONENT_ARM_DISARM,
            0,       # confirmation
            0,       # param1: 0 = disarm
            21196,   # param2: magic number to force disarm (matches ArduPilot)
            0, 0, 0, 0, 0,
        )

    # ── Helpers ───────────────────────────────────────────────────────────────

    def _flight_auth_get(self, path: str):
        """GET from the flight authorisation service. Returns parsed JSON or None."""
        url = f"{self.flight_auth_url}{path}"
        headers = {}
        if self.token:
            headers["Authorization"] = f"Bearer {self.token}"
        try:
            resp = requests.get(url, headers=headers, timeout=5)
            if resp.status_code == 200:
                return resp.json()
            log.debug("GET %s → %d", path, resp.status_code)
            return None
        except requests.RequestException as e:
            log.warning("flight-auth request failed: %s", e)
            return None


# ── Entry point ───────────────────────────────────────────────────────────────

def main():
    parser = argparse.ArgumentParser(description="Pushpaka MAVLink bridge")
    parser.add_argument("--sitl-host", default="localhost")
    parser.add_argument("--sitl-port", type=int, default=5760)
    parser.add_argument("--flight-auth-url", default="http://localhost:8083")
    parser.add_argument(
        "--require-aut",
        action="store_true",
        help="Block ARM if no active AUT found in flight-auth service. "
             "Set PUSHPAKA_TOKEN env var with a valid bearer token.",
    )
    parser.add_argument("--verbose", action="store_true")
    args = parser.parse_args()

    if args.verbose:
        logging.getLogger().setLevel(logging.DEBUG)

    token = os.environ.get("PUSHPAKA_TOKEN")

    bridge = PushpakaBridge(
        sitl_host=args.sitl_host,
        sitl_port=args.sitl_port,
        flight_auth_url=args.flight_auth_url,
        require_aut=args.require_aut,
        token=token,
    )
    bridge.run()


if __name__ == "__main__":
    main()
