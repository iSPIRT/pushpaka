# Pushpaka MAVLink Bridge

Connects to an ArduPilot SITL instance via MAVLink and bridges flight events
to the Pushpaka REST APIs.

## Capability

| Phase | What |
|-------|------|
| 39a | Connect to SITL, log HEARTBEAT and armed-state changes |
| 39b | Intercept ARM commands; send immediate DISARM if no active AUT found |

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
# 39a — connect and log only
python3 bridge.py

# 39b — enable ARM enforcement
# Get a Keycloak token first (see README root for curl snippet)
export PUSHPAKA_TOKEN=<keycloak-bearer-token>
python3 bridge.py --require-aut

# All options
python3 bridge.py --sitl-host localhost --sitl-port 5760 \
                  --flight-auth-url http://localhost:8083 \
                  --require-aut --verbose
```

### ARM enforcement behaviour

When `--require-aut` is set:

| Condition | Result |
|-----------|--------|
| Active AUT found (`CREATED` or `INUSE`) | ARM passes through |
| No active AUT | Bridge sends immediate DISARM to SITL + logs warning |
| `PUSHPAKA_TOKEN` not set | ARM always blocked (fail-safe) |
| Flight-auth service unreachable | ARM always blocked (fail-safe) |

This mirrors the QGC plugin's reactive disarm behaviour (issue #76).

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

The bridge listens passively by default. With `--require-aut` it reacts to
ARM commands by sending an immediate DISARM when no active AUT is present.
