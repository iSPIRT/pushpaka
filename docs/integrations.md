# External Integrations

This page documents Pushpaka's relationship with external drone software platforms.
These codebases are no longer vendored in this repo — see upstream links below.

---

## ArduPilot

**What it is:** Open-source autopilot firmware for drones, planes, rovers, and submarines.
Used to fly the physical drone hardware that Pushpaka's UTM services need to interact with.

**Upstream repo:** https://github.com/ArduPilot/ardupilot

**Relationship to Pushpaka:**
- Was included as a git submodule for local development reference
- No Pushpaka-specific patches were applied
- Removed from this repo in Phase 2 of the rebaseline (2026); no local changes were lost

**Relevant integration point:**
ArduPilot drones communicate via MAVLink. Pushpaka's planned MAVLink bridge (Phase 4b)
will sit between ArduPilot SITL and the registry/flight-authorisation REST APIs,
allowing firmware to remain unmodified.

**Development environment:**
A `.devcontainer/` setup provides the full dev stack. The `Vagrantfile` is retained for reference but superseded by the devcontainer.

---

## QGroundControl

**What it is:** Open-source ground control station (GCS) for MAVLink-based drones.
Provides the operator-facing UI for flight planning, monitoring, and control.

**Upstream repo:** https://github.com/mavlink/qgroundcontrol

**Relationship to Pushpaka:**
Pushpaka extends QGroundControl using its official **Custom Build** mechanism — a
`custom/` directory that is overlaid at build time without forking or patching the
QGC source tree. The plugin is maintained in this repo under `qgc-plugin/`.

### Architecture: Custom Build plugin

The `qgc-plugin/custom/` directory is a self-contained QGC custom build that adds:

| Component | Purpose |
|-----------|---------|
| `PushpakaPlugin` (`QGCCorePlugin` subclass) | Top-level plugin; wires all components together; exposes properties to QML |
| `UserAuthentication` | OAuth2 Authorization Code Flow via Keycloak; exposes `isAuthenticated` and `authorise()` to QML |
| `RegistryClient` | HTTP client for the Pushpaka Registry (port 8082); fetches pilot profile and UAS list after login |
| `FlightAuthorisationClient` | HTTP client for the Flight Authorisation service (port 8083); submits flight plans and retrieves signed AUTs |
| `PushpakaStatusIndicator.qml` | Toolbar status indicator (injected via `toolBarIndicators()` override); shows auth/AUT state; opens login or flight plan panel on click |
| `FlightPlanPanel.qml` | Popup panel for submitting flight plans (UAS selection, start/end time, submit button, busy indicator) |

**Key implementation details:**
- No fork of QGC — upstream is a git submodule at `qgc-plugin/qgroundcontrol`; `custom/` is symlinked/placed at `qgc-plugin/qgroundcontrol/custom`
- OAuth2: Keycloak realm `pushpaka`, client `utm-client`, redirect handler on `http://localhost:8000`
- Registry URL: env var `REGISTRY_URL`, default `http://localhost:8082`
- Flight authorisation URL: env var `FLIGHT_AUTH_URL`, default `http://localhost:8083`
- Toolbar indicator injected via `QGCCorePlugin::toolBarIndicators()` override
- Flight plan submission is a two-call chain: `POST /api/v1/flightPlan` → `GET /api/v1/airspace-usage-tokens/by-flight-plan/{fpId}` → signed JWT returned and stored

**Status:** Active — login, pilot/UAS fetch, flight plan submission, and AUT receipt are implemented (issues [#67](https://github.com/iSPIRT/pushpaka/issues/67), [#74](https://github.com/iSPIRT/pushpaka/issues/74), [#75](https://github.com/iSPIRT/pushpaka/issues/75)).

**Manual test instructions:** See [QGC Integration Testing](ref/qgc-testing.md).

**To build:**
```bash
cd qgc-plugin
# Symlink the custom build into the QGC source tree
ln -s $(pwd)/custom qgroundcontrol/custom

cd qgroundcontrol
cmake -B build -DCMAKE_BUILD_TYPE=Debug
cmake --build build --target QGroundControl -j$(nproc)
```
