# Pushpaka MAVLink Bridge

Connects to an ArduPilot SITL instance via MAVLink and bridges flight events
to the Pushpaka REST APIs.

## Current capability

| Phase | What |
|-------|------|
| 39a (this) | Connect to SITL, log HEARTBEAT and armed-state changes |
| 39b (next) | Intercept ARM commands; deny via `COMMAND_ACK` if no valid AUT |

## Requirements

- Python 3.10+
- ArduPilot SITL running (via the devcontainer SITL profile)
- Pushpaka Flight Authorisation service running (port 8083)

## Setup

```bash
cd sitl-bridge
pip install -r requirements.txt
```

## Run

```bash
# Default: SITL at localhost:5760, flight-auth at localhost:8083
python3 bridge.py

# Custom endpoints
python3 bridge.py --sitl-host localhost --sitl-port 5760 \
                  --flight-auth-url http://localhost:8083 \
                  --verbose
```

## Start SITL first

```bash
# From repo root
docker compose -f .devcontainer/docker-compose.yml --profile sitl up -d
```

SITL exposes:

| Endpoint | Protocol | Purpose |
|----------|----------|---------|
| `localhost:5760` | TCP | MAVLink — bridge connects here |
| `localhost:14550` | UDP | QGC connects here |

## Architecture

```
ArduPilot SITL
    │  MAVLink (TCP 5760)
    ▼
bridge.py  ──────────────────────────────►  Flight Auth REST API
    │  HEARTBEAT, ARM events              GET /airspace-usage-tokens/...
    │
    ▼
  stdout log
```

The bridge is a passive listener in 39a. In 39b it will actively respond to
ARM commands with `COMMAND_ACK MAV_RESULT_DENIED` when no AUT is present.
