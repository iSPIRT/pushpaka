# Pushpaka API Specifications

This directory is the **source of truth** for all Pushpaka OpenAPI specifications. These are hand-maintained, spec-first YAML files. They are not generated from Java code.

## Published Specs

| Service | Spec | Rendered |
|---------|------|---------|
| Registry | [registry.yaml](./registry.yaml) | [registry.html](https://ispirt.github.io/pushpaka/openapi/registry.html) |
| Flight Authorisation | [flight-authorisation.yaml](./flight-authorisation.yaml) | [flight-authorisation.html](https://ispirt.github.io/pushpaka/openapi/flight-authorisation.html) |

## Ownership Model

- `docs/openapi/` — authoritative source; edit here
- `reference-implementation/openapi.yaml` — downstream copy used for Java code generation; do not edit directly

## Versioning

Specs are versioned via the `info.version` field in each YAML file. Increment the version when making breaking or significant changes.
