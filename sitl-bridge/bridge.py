#!/usr/bin/env python3
"""
bridge.py — Pushpaka MAVLink bridge

Connects to an ArduPilot SITL instance via MAVLink and bridges relevant
flight events to the Pushpaka REST APIs.

Current capability (39a):
  - Connect to SITL and log HEARTBEAT state changes

Planned (39b):
  - Intercept ARM commands; deny via COMMAND_ACK if no valid AUT

Usage:
  python3 bridge.py [--sitl-host HOST] [--sitl-port PORT]
                    [--flight-auth-url URL] [--verbose]

Defaults:
  --sitl-host       localhost
  --sitl-port       5760   (TCP; matches .devcontainer docker-compose SITL profile)
  --flight-auth-url http://localhost:8083
"""

import argparse
import logging
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

# MAVLink mode names for readable log output
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


# ── Bridge ────────────────────────────────────────────────────────────────────

class PushpakaBridge:
    def __init__(self, sitl_host: str, sitl_port: int, flight_auth_url: str):
        self.sitl_addr = f"tcp:{sitl_host}:{sitl_port}"
        self.flight_auth_url = flight_auth_url.rstrip("/")
        self._conn = None
        self._last_system_status = None

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

        # 39b: ARM intercept will be added here
        # elif msg_type == "COMMAND_LONG":
        #     self._on_command_long(msg)

    def _on_heartbeat(self, msg):
        status = msg.system_status
        if status != self._last_system_status:
            name = MAV_STATE_NAMES.get(status, str(status))
            armed = bool(msg.base_mode & mavutil.mavlink.MAV_MODE_FLAG_SAFETY_ARMED)
            log.info(
                "HEARTBEAT  state=%-16s  armed=%s  type=%d  autopilot=%d",
                name,
                armed,
                msg.type,
                msg.autopilot,
            )
            self._last_system_status = status

    # ── Helpers ───────────────────────────────────────────────────────────────

    def _flight_auth_get(self, path: str) -> dict | None:
        """GET from the flight authorisation service. Returns parsed JSON or None."""
        url = f"{self.flight_auth_url}{path}"
        try:
            resp = requests.get(url, timeout=5)
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
    parser.add_argument("--verbose", action="store_true")
    args = parser.parse_args()

    if args.verbose:
        logging.getLogger().setLevel(logging.DEBUG)

    bridge = PushpakaBridge(
        sitl_host=args.sitl_host,
        sitl_port=args.sitl_port,
        flight_auth_url=args.flight_auth_url,
    )
    bridge.run()


if __name__ == "__main__":
    main()
