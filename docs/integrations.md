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
The `Vagrantfile` previously provided an Ubuntu 22.04 VM for ArduPilot builds.
This will be replaced by a `.devcontainer/` setup in Phase 4a.

---

## QGroundControl

**What it is:** Open-source ground control station (GCS) for MAVLink-based drones.
Provides the operator-facing UI for flight planning, monitoring, and control.

**Upstream repo:** https://github.com/mavlink/qgroundcontrol

**Relationship to Pushpaka:**
QGroundControl was forked and extended with Keycloak-based user authentication,
allowing the GCS to authenticate operators against the Pushpaka identity provider
before flight operations.

### Pushpaka-specific work: Keycloak OAuth integration

Commit: `Add UserAuthentication via Keycloak using Qt OAuth` (2024-07-19, Sayandeep Purkayasth)

**Files added/modified:**

| File | Purpose |
|------|---------|
| `src/UserAuthentication.h` | Qt `QObject` exposing `isAuthenticated` property and `authorise()` slot |
| `src/UserAuthentication.cpp` | OAuth2 Authorization Code Flow via `QOAuth2AuthorizationCodeFlow`; connects to Keycloak at `http://localhost:8080/realms/digitalsky` |
| `src/ui/toolbar/UserAuthentication.qml` | QML element placeholder (stub, imports `UserAuthentication`) |
| `src/ui/toolbar/MainStatusIndicator.qml` | UI toolbar hook for auth status |
| `src/QGCApplication.h`, `src/main.cc`, `src/api/QGCCorePlugin.cc` | Integration points wiring `UserAuthentication` into the QGC application lifecycle |
| `src/CMakeLists.txt`, `qgroundcontrol.pro`, `qgroundcontrol.qrc` | Build system additions |

**Key implementation details:**
- Uses Qt's `QOAuth2AuthorizationCodeFlow` (Authorization Code Flow)
- Keycloak realm: `digitalsky`
- Client ID: `qgc`
- Redirect: local HTTP reply handler on port 8000
- `isAuthenticated` property bindable from QML for UI reactivity
- Browser-based auth flow via `QDesktopServices::openUrl`

**Status:** Proof-of-concept / early integration. The QML element is a stub.
Full integration is deferred to Phase 4a (DevContainer) when the full stack is wired together.

**To resume this work:**
1. Fork `https://github.com/mavlink/qgroundcontrol`
2. Re-apply the changes above against a current upstream commit
3. Wire into the devcontainer compose stack (Keycloak endpoint will need updating from `localhost:8080` to service name)
