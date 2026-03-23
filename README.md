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

### Start core stack (ref impl + Keycloak + SpiceDB + PostgreSQL)
```
docker compose -f .devcontainer/docker-compose.yml up
```

Or open in VS Code and click **Reopen in Container** — everything starts automatically.

### Start with ArduPilot SITL
```
docker compose -f .devcontainer/docker-compose.yml --profile sitl up
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
