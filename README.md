# Pushpaka

Working-group publication repo for drone regulation and UTM standards in India.

Published site: **https://ispirt.github.io/pushpaka/**

## What's here

- `docs/` — work items, meeting minutes, reference documents, OpenAPI specs
- `reference-implementation/` — illustrative Java/Spring Boot services (registry + flight authorisation)
- `.devcontainer/` — cross-platform dev environment (VS Code / GitHub Codespaces)

## Dev environment (devcontainer)

The fastest way to get a working environment — no local Java, Maven, or Docker knowledge required.

### Requirements
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- [VS Code](https://code.visualstudio.com/) + [Dev Containers extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers)

### First-time setup
```bash
cp .devcontainer/.env.example .devcontainer/.env
# Edit .env if any default ports conflict with services already on your machine
```

### Start core stack (ref impl + Keycloak + SpiceDB + PostgreSQL)
```bash
docker compose -f .devcontainer/docker-compose.yml up
```

Or open in VS Code → **Reopen in Container** — everything starts automatically.

### Default service ports
| Service | Host port |
|---------|-----------|
| PostgreSQL | 15432 |
| Keycloak | 18080 |
| SpiceDB HTTP | 18081 |
| Registry service | 8082 |
| Flight authorisation | 8083 |

### Start with ArduPilot SITL
```bash
docker compose -f .devcontainer/docker-compose.yml --profile sitl up
# QGC connects from host on UDP:14550
```

### GitHub Codespaces
Open this repo in Codespaces — the devcontainer starts automatically, no local setup needed.

---

## Docs site

### Requirements

```
pip install -r requirements.txt
```

### Local preview

```
mkdocs serve
```

### Build (verify before deploying)

```
mkdocs build --strict
```

### Deploy

GitHub Actions auto-deploys the `docs` site on push to the `dev` branch. To deploy manually:

```
mkdocs gh-deploy
```

## Contributing

Issues and tasks are tracked in [GitHub Issues](https://github.com/iSPIRT/pushpaka/issues).
Branch naming follows work item IDs (e.g. `i08`) or rebaseline phase IDs (e.g. `rb-01-docs`).
See `CLAUDE.md` for full project context.
