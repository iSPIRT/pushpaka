# Pushpaka

Working-group publication repo for drone regulation and UTM standards in India.

Published site: **https://ispirt.github.io/pushpaka/**

---

## Repository Structure

```
pushpaka/
├── docs/                          # PRIMARY OUTPUT — MkDocs knowledge base
│   ├── work-items/                # Work item specs I01–I08
│   ├── openapi/                   # SOURCE-OF-TRUTH OpenAPI specs
│   │   ├── registry.yaml          #   UAS registry (v1.0.17)
│   │   └── flight-authorisation.yaml #   Flight permits and airspace tokens
│   ├── minutes/                   # Working-group meeting records
│   ├── ref/                       # Reference and background documents
│   └── index.md                   # Microsite homepage
├── reference-implementation/      # Illustrative Java/Spring Boot services (I05 scope only)
│   ├── src/main/java/             #   Registry + flight-auth service code
│   │   └── in/ispirt/pushpaka/
│   │       ├── dao/               #   Hibernate entities + DaoInstance singleton
│   │       │   ├── entities/      #     16 JPA entity classes
│   │       │   └── seeds/         #     Seeds.java (fixture constants) + SeedLoader.java
│   │       ├── registry/          #   Registry service (controllers, services, config)
│   │       ├── flightauthorisation/ # Flight authorisation service
│   │       └── authorisation/     #   SpiceDB AuthZ client
│   ├── src/test/java/
│   │   └── in/ispirt/pushpaka/
│   │       ├── unittests/         #   AuthZTest (30 SpiceDB tests), AutTest (AUT generation)
│   │       └── integration/       #   DemoScenario1–5 + TestUtils (HTTP end-to-end)
│   ├── src/test/resources/
│   │   └── fixtures/              #   16 JSON request-body templates ({{placeholder}} tokens)
│   ├── src/main/resources/        #   Hibernate config, Spring properties, SpiceDB schema
│   ├── docker/                    #   Keycloak, SpiceDB, PostgreSQL service configs
│   ├── docker-compose.yaml        #   Full dev stack
│   ├── openapi.yaml               #   DOWNSTREAM COPY — generated from docs/openapi/
│   └── pom.xml                    #   Maven build (Java 17, Spring Boot 2.7.6)
├── qgc-plugin/                    # QGroundControl UTM enforcement plugin (issue #67)
│   ├── qgroundcontrol/            #   Upstream QGC v5.0.8 submodule (unmodified)
│   ├── custom/                    #   Pushpaka plugin code (QGC Custom Build)
│   │   ├── src/                   #     PushpakaPlugin, UserAuthentication (Keycloak)
│   │   └── qml/                   #     AUT status indicator and panels
│   ├── .devcontainer/             #   Qt 6 + CMake build environment
│   └── setup.sh                   #   Wires custom/ into QGC source tree + configures build
├── mkdocs.yml                     # Docs site config (theme: pulse)
├── requirements.txt               # Python deps for MkDocs
├── Vagrantfile                    # ArduPilot dev VM (needs audit)
└── .github/workflows/
    ├── main.yml                   # Docs CI — strict build + auto-deploy to GH Pages
    └── java-ci.yml                # Java CI — runs tests on PRs touching reference-impl
```

---

## Working Group Docs

Work items, meeting minutes, OpenAPI specs, and reference documents live under `docs/` and are published as a static site via MkDocs.

### Preview locally

```bash
pip install -r requirements.txt
mkdocs serve
```

### Build (verify before deploying)

```bash
mkdocs build --strict
```

### Deploy

GitHub Actions auto-deploys to GitHub Pages on push to the `dev` branch. To deploy manually:

```bash
mkdocs gh-deploy
```

---

## Specifications

OpenAPI specifications are the **source of truth** for all API contracts.

| Spec | Location |
|------|----------|
| UAS Registry | `docs/openapi/registry.yaml` |
| Flight Authorisation | `docs/openapi/flight-authorisation.yaml` |

The file `reference-implementation/openapi.yaml` is a downstream copy used for code generation — do not edit it directly.

To browse interactively, run the reference implementation (see below) and open:

```
http://localhost:8082/swagger-ui.html   # Registry
http://localhost:8083/swagger-ui.html   # Flight authorisation
```

---

## Reference Implementation

An **illustrative** Java/Spring Boot implementation of the registry and flight authorisation services. Scope is intentionally limited to demonstrating the I05 UTM ConOps — it is not a production system.

### Stack

| Service | Host port |
|---------|-----------|
| PostgreSQL | 15432 |
| Keycloak | 18080 |
| SpiceDB HTTP | 18081 |
| SpiceDB gRPC | 50051 |
| Registry service | 8082 |
| Flight authorisation | 8083 |

### Requirements

- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- [VS Code](https://code.visualstudio.com/) + [Dev Containers extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers)

No local Java or Maven installation needed — the devcontainer provides everything.

### First-time setup

```bash
cp .devcontainer/.env.example .devcontainer/.env
# Edit .env if any default ports conflict with services already on your machine
```

### Start (VS Code)

Open the repo in VS Code → **Reopen in Container**. All services start automatically.

### Start (CLI)

```bash
docker compose -f .devcontainer/docker-compose.yml up
```

### GitHub Codespaces

Open this repo in Codespaces — the devcontainer starts automatically, no local setup needed.

### Build and test (inside devcontainer)

```bash
cd /workspace/reference-implementation

mvn compile                        # Compile only
mvn test                           # All tests
mvn test -Dtest="EntityTests"      # Entity tests only
mvn prettier:write                 # Format code
```

### Testing

The test suite has two tiers — **unit/authZ tests** that run in CI, and **integration tests** that require the full stack.

#### AuthZTest (runs in CI)

`unittests/AuthZTest.java` — 30 ordered JUnit 5 tests covering the SpiceDB authorisation layer end-to-end:

| Group | What is tested |
|-------|---------------|
| Schema | SpiceDB schema write |
| Identities | Platform user, CAA, Manufacturer, Trader, DSSP, Repair Agency, Operator administrator creation |
| Membership | Pilot–Operator association (add, remove, negative cases) |
| Approvals | CAA approves Manufacturer, Operator, DSSP, Trader, Repair Agency |
| Lookups | UAS ownership, regulator lookup, bulk relationship export |

Tests are stateful and ordered; each builds on the prior state. Run with:

```bash
mvn test -Dtest="AuthZTest"
```

#### AutTest (runs in CI)

`unittests/AutTest.java` — smoke test for AUT (Airspace Usage Token) generation logic. Verifies the token can be created with valid parameters.

#### DemoScenario1–5 (manual only, requires full stack + Keycloak)

`integration/DemoScenario*.java` — HTTP-level end-to-end tests against live services. Not run in CI.

| Scenario | What it exercises |
|----------|------------------|
| DemoScenario1 | Full entity registration: CAA, Manufacturer, Operator, Pilot, DSSP, Repair Agency, Trader, UAS, UAS Sale |
| DemoScenario2 | AUT generation for BVLOS operation |
| DemoScenario3 | AUT generation for VLOS operation |
| DemoScenario4 | AUT generation for EVLOS operation |
| DemoScenario5 | Flight plan submission, conflict rejection, AUT + signing key verification, kill-switch, temporary flight restriction |

Run a specific scenario (full stack must be up):

```bash
mvn test -Dtest="DemoScenario1"
```

#### Fixtures

Request payloads are stored as JSON templates in `src/test/resources/fixtures/` — one file per entity type (e.g. `manufacturer.json`, `uas-type.json`). Placeholder tokens like `{{manufacturerId}}` are filled at runtime by `TestUtils.fill()`. This keeps test logic readable and the payloads auditable without parsing Java strings.

---

### Keycloak

Keycloak is used as the identity provider. It issues JWTs that the services validate via Spring OAuth2 resource server.

| Detail | Value |
|--------|-------|
| Admin console | `http://localhost:18080` (admin / admin) |
| Realm | `pushpaka` |
| Client ID | `backend` |
| Token endpoint | `http://localhost:18080/realms/pushpaka/protocol/openid-connect/token` |

The test realm is seeded from `docker/keycloak-realm-pushpaka-test.json`. Pre-configured test users (all with password `test`):

| Username | Role |
|----------|------|
| `test.platform.admin@test.com` | Platform administrator |
| `test.caa.admin@test.com` | Civil Aviation Authority admin |
| `test.manufacturer.0.admin@test.com` | Manufacturer admin |
| `test.operator.0.admin@test.com` | Operator admin |
| `test.pilot.0@test.com` | Pilot |
| `test.dssp.0.admin@test.com` | DSSP admin |
| `test.repair.agency.0.admin@test.com` | Repair Agency admin |
| `test.trader.0.admin@test.com` | Trader admin |
| `test.uas.0.owner@test.com` | UAS owner |

To get a JWT manually (e.g. for Swagger UI or `curl` testing):

```bash
curl -s -X POST http://localhost:18080/realms/pushpaka/protocol/openid-connect/token \
  -d "grant_type=password&client_id=backend" \
  -d "username=test.caa.admin@test.com&password=test" \
  | python3 -m json.tool | grep access_token
```

---

### Run services manually (inside devcontainer)

```bash
# Registry (port 8082)
SPRING_PROFILES_ACTIVE=registry mvn compile exec:java \
  -Dexec.mainClass="in.ispirt.pushpaka.registry.RegistryService"

# Flight authorisation (port 8083)
SPRING_PROFILES_ACTIVE=flightauthorisation mvn compile exec:java \
  -Dexec.mainClass="in.ispirt.pushpaka.flightauthorisation.FlightAuthorisationService"
```

### Environment variables

All connection details are read from environment. See `.devcontainer/.env.example` for the full list. Key variables:

| Variable | Default | Used by |
|----------|---------|---------|
| `DATABASE_URL` | `jdbc:postgresql://localhost:15432/pushpaka?sslmode=disable` | Hibernate |
| `DATABASE_USER` | `postgres` | Hibernate |
| `DATABASE_PASSWORD` | `secret` | Hibernate |
| `KEYCLOAK_URL` | `http://localhost:18080` | Spring OAuth2 |
| `SPICEDB_TARGET` | `localhost:50051` | SpiceDB gRPC client |
| `SPICEDB_GRPC_PRESHARED_KEY` | `somerandomkeyhere` | SpiceDB auth |

---

## Simulation (QGC + SITL)

End-to-end UTM simulation: boot the full stack, run API smoke checks, and launch QGroundControl for manual flight authorisation testing.

### Quick start

```bash
./reference-implementation/src/test/scripts/e2e-smoke.sh
```

This single script:
1. Starts the docker stack (Postgres, Keycloak, SpiceDB)
2. Starts the Registry (port 8082) and Flight Auth (port 8083) services
3. Runs API smoke checks (token fetch, `/pilots/me`, flight plan, AUT)
4. Launches the QGC binary
5. Prints the manual QGC checklist

Options:
```bash
./reference-implementation/src/test/scripts/e2e-smoke.sh --no-qgc   # backend only
./reference-implementation/src/test/scripts/e2e-smoke.sh --teardown # stop everything
```

For the full manual QGC walkthrough (login → flight plan → AUT → green indicator) see [`docs/ref/qgc-testing.md`](docs/ref/qgc-testing.md).

### ArduPilot SITL (optional)

To add a simulated vehicle:

```bash
docker compose -f .devcontainer/docker-compose.yml --profile sitl up
```

| Endpoint | Protocol | Purpose |
|----------|----------|---------|
| `localhost:5760` | TCP | MAVLink (autopilot stream) |
| `localhost:14550` | UDP | QGC connection |

QGC auto-detects the SITL vehicle on `localhost:14550` (UDP).

---

## Contributing

Issues and tasks are tracked in [GitHub Issues](https://github.com/iSPIRT/pushpaka/issues).
Branch naming follows work item IDs (e.g. `i08`) or rebaseline phase IDs (e.g. `rb-03-refimpl`).
See `CLAUDE.md` for full project context and open decisions.
