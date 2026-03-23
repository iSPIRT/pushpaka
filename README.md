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
│   │       ├── registry/          #   Registry service (controllers, services, config)
│   │       ├── flightauthorisation/ # Flight authorisation service
│   │       └── authorisation/     #   SpiceDB AuthZ client
│   ├── src/test/java/             #   Unit, entity, authZ, and integration tests
│   ├── src/main/resources/        #   Hibernate config, Spring properties, SpiceDB schema
│   ├── docker/                    #   Keycloak, SpiceDB, PostgreSQL service configs
│   ├── docker-compose.yaml        #   Full dev stack
│   ├── openapi.yaml               #   DOWNSTREAM COPY — generated from docs/openapi/
│   └── pom.xml                    #   Maven build (Java 17, Spring Boot 2.7.6)
├── ardupilot/                     # Submodule — pure upstream ArduPilot, no patches
├── qgroundcontrol/                # Submodule — pure upstream QGC, no patches
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

## Simulation (SITL + QGC)

ArduPilot Software-in-the-Loop simulation and QGroundControl integration for end-to-end UTM flight scenario testing.

### Start with ArduPilot SITL

```bash
docker compose -f .devcontainer/docker-compose.yml --profile sitl up
```

| Endpoint | Protocol | Purpose |
|----------|----------|---------|
| `localhost:5760` | TCP | MAVLink (autopilot stream) |
| `localhost:14550` | UDP | QGC connection |

### QGroundControl

Download QGC from [qgroundcontrol.com](https://qgroundcontrol.com). Connect to `localhost:14550` (UDP) — QGC auto-detects the SITL vehicle.

For Keycloak OAuth integration details see [`docs/integrations.md`](docs/integrations.md).

---

## Contributing

Issues and tasks are tracked in [GitHub Issues](https://github.com/iSPIRT/pushpaka/issues).
Branch naming follows work item IDs (e.g. `i08`) or rebaseline phase IDs (e.g. `rb-03-refimpl`).
See `CLAUDE.md` for full project context and open decisions.
