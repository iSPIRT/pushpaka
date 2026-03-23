# Pushpaka

Working-group publication repo for drone regulation and UTM standards in India.

Published site: **https://ispirt.github.io/pushpaka/**

## What's here

- `docs/` — work items, meeting minutes, reference documents, OpenAPI specs
- `reference-implementation/` — illustrative Java/Spring Boot services (registry + flight authorisation)
- `qgroundcontrol/` — QGroundControl fork with Keycloak authentication integration

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
