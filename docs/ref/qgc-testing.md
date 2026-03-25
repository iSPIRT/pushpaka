# QGC Integration Testing

Step-by-step instructions for manually testing the Pushpaka QGroundControl plugin
end-to-end: from stack startup through pilot login, flight plan submission, and
AUT receipt.

---

## Prerequisites

- Docker and Docker Compose installed
- QGroundControl built with the Pushpaka custom plugin (see [Integrations — QGroundControl](../integrations.md))
- Maven and Java 17 for the reference implementation services

---

## 1. Start the backing services

```bash
cd reference-implementation
docker compose up -d
```

This starts:

| Service | Port | Purpose |
|---------|------|---------|
| PostgreSQL | 15432 | Database |
| Keycloak | 18080 | Identity provider (realm: `pushpaka`) |
| SpiceDB | 50051 | Authorisation |

Wait ~15 seconds for Keycloak to fully initialise before starting the Java services.

---

## 2. Start the Registry service

```bash
cd reference-implementation
SPRING_PROFILES_ACTIVE=registry mvn compile exec:java \
  -Dexec.mainClass="in.ispirt.pushpaka.registry.RegistryService"
```

Registry listens on **port 8082**. Confirm it is ready when you see:

```
Started RegistryService in X seconds
```

---

## 3. Seed the database (first run only)

On first run the reference implementation seeds deterministic fixture data
(pilot, UAS, manufacturer, etc.) automatically via `SeedLoader` on startup.
No manual step is needed.

---

## 4. Start the Flight Authorisation service

```bash
cd reference-implementation
SPRING_PROFILES_ACTIVE=flightauthorisation mvn compile exec:java \
  -Dexec.mainClass="in.ispirt.pushpaka.flightauthorisation.FlightAuthorisationService"
```

Flight authorisation listens on **port 8083**.

---

## 5. Launch QGroundControl

```bash
cd qgc-plugin/qgroundcontrol
./build/QGroundControl
```

---

## 6. Login flow

1. The Pushpaka status indicator appears in the QGC toolbar (top-right area).
   Its initial state is **grey / unauthenticated**.

2. Click the indicator. A browser window opens at the Keycloak login page
   (`http://localhost:18080/realms/pushpaka/...`).

3. Log in with the seeded pilot account: `test.pilot.0@test.com` / `test`
   (from `docker/keycloak-realm-pushpaka-test.json`).

4. After successful login, the browser redirects to `http://localhost:8000`
   (the OAuth callback handler). The QGC window regains focus.

5. The plugin:
   - Calls `GET /api/v1/pilots/me` on the Registry to resolve the pilot UUID
   - Calls `GET /api/v1/uas` to fetch the available UAS list

6. The toolbar indicator turns **amber** (authenticated, no active AUT).

---

## 7. Submit a flight plan

1. Click the toolbar indicator again. The **Flight Plan Panel** popup opens.

2. Select a UAS from the dropdown (populated from the UAS list fetched above).

3. Enter a start time and end time in ISO 8601 format, e.g.:
   ```
   Start: 2026-06-01T10:00:00Z
   End:   2026-06-01T10:30:00Z
   ```

4. Click **Submit**.

5. The panel shows a busy indicator while the plugin:
   - Posts `POST /api/v1/flightPlan` to the Flight Authorisation service
   - Polls `GET /api/v1/airspace-usage-tokens/by-flight-plan/{fpId}` to retrieve the
     signed AUT JWT

6. On success the panel closes automatically.

7. The toolbar indicator turns **green** (authenticated, valid AUT).

---

## 8. Verify the AUT

To inspect the issued JWT directly, query the flight authorisation service:

```bash
# List all flight plans
curl http://localhost:8083/api/v1/flightPlan/find \
  -H "Authorization: Bearer <your-keycloak-access-token>"

# Get AUT for a specific flight plan
curl http://localhost:8083/api/v1/airspace-usage-tokens/by-flight-plan/<flight-plan-uuid> \
  -H "Authorization: Bearer <your-keycloak-access-token>"
```

The response includes a `signed_jwt` field containing the RSA-signed JWT issued
by the Pushpaka UTM authority.

---

## SITL scenario (optional — full ARM enforcement test)

This scenario extends the basic QGC test with an ArduPilot SITL vehicle to
verify that ARM is blocked without an AUT and allowed once one is issued.

### 9. Start SITL

```bash
# From repo root — starts the ArduPilot SITL container in addition to the core stack
docker compose -f .devcontainer/docker-compose.yml --profile sitl up -d
```

SITL exposes MAVLink on `localhost:5760` (TCP, bridge) and `localhost:14550` (UDP, QGC).

### 10. Start the MAVLink bridge

```bash
# Get a token first
TOKEN=$(curl -s -X POST http://localhost:18080/realms/pushpaka/protocol/openid-connect/token \
  -d "grant_type=password&client_id=backend&username=test.pilot.0@test.com&password=test" \
  | jq -r .access_token)

export PUSHPAKA_TOKEN=$TOKEN
python3 sitl-bridge/bridge.py --require-aut
```

The bridge logs HEARTBEAT state and enforces AUT on ARM commands.

### 11. Connect QGC to SITL

In QGC: **Application Settings → Comm Links → Add** → UDP, port 14550.
QGC auto-detects the ArduCopter vehicle on connection.

### 12. Attempt ARM without AUT

In QGC, attempt to arm the vehicle (Fly view → Arm slider or MAVLink console).

Expected:
- QGC plugin: indicator stays amber; reactive disarm fires immediately + dialog shown
- Bridge: logs `ARM BLOCKED — no active AUT; sending disarm`

### 13. Get AUT and ARM successfully

1. Click the UTM indicator → log in → submit a flight plan
2. Indicator turns green (AUT issued)
3. Attempt ARM again

Expected:
- QGC plugin: ARM proceeds (indicator is green, `hasValidAut = true`)
- Bridge: logs `ARM allowed — valid AUT found`
- Vehicle arms normally in SITL

### 14. Verify in bridge log

```
ARM command received — checking AUT …
ARM allowed — valid AUT found
Vehicle ARMED
```

---

## Troubleshooting

| Symptom | Likely cause | Fix |
|---------|-------------|-----|
| Toolbar indicator does not appear | Custom plugin not detected by QGC build | Confirm `custom/` is present in `qgc-plugin/qgroundcontrol/` and rebuild |
| Browser does not open on click | `QDesktopServices` issue | Run QGC from a desktop session (not SSH) |
| Login succeeds but indicator stays grey | Pilot not found in DB | Check `GET /api/v1/pilots/me` returns 200; ensure DB is seeded and pilot's Keycloak UUID matches the `user.id` in the DB |
| Flight plan submit fails immediately | Registry or flight-auth service not running | Confirm both services are up on ports 8082 / 8083 |
| AUT fetch returns 404 | AUT issuance failed silently | Check flight-auth service logs for `AUT issuance failed for FlightPlan` warning |
| `signed_jwt` field is null | RSA key not found | Confirm `AirspaceUsageTokenUtils.getDigitalSkyRsaKey()` can locate the key on the classpath |
