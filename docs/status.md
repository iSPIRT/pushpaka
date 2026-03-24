# Current Status

This page summarises the current state of publication work items and API specifications.
Engineering tasks and code work are tracked in [GitHub Issues](https://github.com/iSPIRT/pushpaka/issues).

## Publication Work Items

| ID | Name | Status | Notes |
|----|------|--------|-------|
| [I01](./work-items/i01.md) | Concept of Operations V1 | Published | Stable. Foundational risk-category framework (A/B/C/D). |
| I02 | Commentary on Draft Rules 2021 | Published | Published on iSpirt blog. |
| I03 | Draft Rules Change Requests | Archived | Aborted. |
| I04 | Commentary on Drone Rules 2021 | Archived | Aborted. |
| [I05](./work-items/i05/index.md) | UTM Concept of Operations | Draft | Under active revision (2025–ongoing). Working version in Google Doc. Repo copy is a 2021–22 snapshot. |
| [I06](./work-items/i06.md) | UTM Technical Standards | Paused | Skeletal draft only. Awaiting direction. |
| [I07](./work-items/i07.md) | Feedback Sessions: ConOps V1 | Published | Session recordings and materials available. |
| [I08](./work-items/i08.md) | NIDSP Specification | Draft | Actively being written. DPG-light framing to be added. |

## API Specifications

| Spec | Status | Notes |
|------|--------|-------|
| [Registry](./openapi/README.md) | Published | v1.0.17. Source of truth in `docs/openapi/`. |
| [Flight Authorisation](./openapi/README.md) | Published | Source of truth in `docs/openapi/`. |

## Reference Implementation

Illustrative Java/Spring Boot implementation of the registry and flight authorisation services. Scoped to I05 — not a production system.

| Component | Status | Notes |
|-----------|--------|-------|
| Registry service (Java) | Active | Service layer, entity model, SpiceDB AuthZ wired. |
| Flight Authorisation service (Java) | Active | Flight plan and AUT endpoints available. |
| DevContainer | Done | Full dev stack via `.devcontainer/` (Postgres, Keycloak, SpiceDB). |
| QGC integration | In progress | Keycloak auth + registry + flight-auth from QGroundControl. |
| MAVLink bridge | Planned | ArduPilot SITL ↔ Pushpaka APIs. |
