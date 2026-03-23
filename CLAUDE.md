# Pushpaka — CLAUDE.md

## Repo Mission (Rebaselined)

Pushpaka is a **working-group publication repo** for drone regulation and UTM standards in India. Its primary outputs are:

- Published work items and specifications (docs-first)
- OpenAPI / interoperability specs as first-class technical artifacts
- Meeting records and working-group minutes
- Limited reference code only where it directly supports a published spec

It is **not** a broad implementation monorepo, and it is **not** a place to vendor full external platform codebases without strong justification.

---

## Repository Structure

```
pushpaka/
├── docs/                    # PRIMARY OUTPUT — MkDocs knowledge base
│   ├── work-items/          # Work item specs I01–I08
│   ├── openapi/             # SOURCE-OF-TRUTH OpenAPI specs
│   │   ├── registry.yaml              (58K, v1.0.17)
│   │   └── flight-authorisation.yaml  (16K)
│   ├── minutes/             # Meeting minutes (Aug 2021–May 2022)
│   ├── ref/                 # Reference documents
│   ├── index.md             # Microsite homepage
│   ├── bibliography.md
│   └── nomenclature.md
├── reference-implementation/ # LIMITED SCOPE — Java/Spring Boot services
│   ├── src/                 # 89 Java files; registry + flight-auth only
│   ├── docker/              # Keycloak, SpiceDB, PostgreSQL configs
│   ├── docker-compose.yaml  # Full service stack
│   ├── openapi.yaml         # COPY (generated from docs/openapi/)
│   └── pom.xml              # Maven build (Java 8, Spring Boot 2.7.6)
├── ardupilot/               # Git submodule — 232MB, pure upstream, no patches
├── qgroundcontrol/          # Git submodule — 123MB, pure upstream, no patches
├── mkdocs.yml               # Docs site config (theme: pulse, minimal nav)
├── requirements.txt         # mkdocs, mkdocs-bootswatch
├── Vagrantfile              # ArduPilot dev VM (Ubuntu 22.04); needs audit
└── .github/workflows/main.yml  # Docs-only CI: auto-deploy to GH Pages on dev push
```

---

## Work Item Status

| ID | Title | Status | Notes |
|----|-------|--------|-------|
| I01 | Concept of Operations V1 | **Published** | Foundational; risk categories A/B/C/D |
| I02 | Commentary on Draft Rules 2021 | **Published** | External (iSpirt) |
| I03 | Draft Rules Change Requests | **Aborted** | No file present |
| I04 | Commentary on Drone Rules 2021 | **Aborted** | No file present |
| I05 | UTM Concept of Operations | **Published** (Working Draft) | 10-layer UTM architecture; timelines need refresh |
| I06 | UTM Technical Specification | **Skeletal** | ~20 lines, authors only; needs work or archiving |
| I07 | Feedback Sessions 1–2: ConOps V1 | **Draft** | Session docs, videos, flyers |
| I08 | NIDSP Specification | **In Progress** | Active on branch `i08`; should explicitly frame DPG-light/spec-first options |
| I09 | Pushpaka Ecosystem Presentation | — | External (Google Slides) |

---

## OpenAPI — Source of Truth

**Authoritative location: `docs/openapi/`**

- `docs/openapi/registry.yaml` — UAS type/manufacturer/pilot registry (OpenAPI 3.0.2, v1.0.17)
- `docs/openapi/flight-authorisation.yaml` — Flight permits and airspace tokens

These are **hand-maintained spec-first**. `reference-implementation/openapi.yaml` is a downstream copy used for code generation. Do not edit the copy directly.

---

## Reference Implementation — Scope

Role: **limited / illustrative** — supports I05 (registry + flight authorisation) only.

- Do **not** expand scope beyond registry and flight authorisation without explicit decision.
- Broader Java refactoring is **not** a current priority.
- Whether to keep it runnable, demo-only, or archival is an open decision (see rebaseline plan).

### Running the stack

```bash
cd reference-implementation
docker-compose up -d        # PostgreSQL, Keycloak (8080), SpiceDB (8081/9090/50051)

mvn test                    # Run all tests
mvn test -Dtest="EntityTests"
mvn prettier:write          # Format code

# Registry Service (port 8082)
SPRING_PROFILES_ACTIVE=registry mvn compile exec:java \
  -Dexec.mainClass="in.ispirt.pushpaka.registry.RegistryService"

# Flight Authorization Service (port 8083)
SPRING_PROFILES_ACTIVE=flightauthorisation mvn compile exec:java \
  -Dexec.mainClass="in.ispirt.pushpaka.flightauthorisation.FlightAuthorisationService"
```

Swagger UI: http://localhost:8082/swagger-ui.html

---

## Documentation Site

```bash
pip install -r requirements.txt
mkdocs serve        # Local preview
mkdocs gh-deploy    # Manual deploy (or push to dev for auto-deploy)
```

CI auto-deploys to GitHub Pages on push to `dev` branch via `.github/workflows/main.yml`.

**Known nav gap:** `mkdocs.yml` only exposes 4 items. `openapi/`, `ref/`, and `minutes/` are built but not in nav.

---

## External Submodules

`ardupilot/` (232MB) and `qgroundcontrol/` (123MB) are **pure upstream**, no Pushpaka patches. Their future in the repo is under review — likely to be replaced with links and notes.

```bash
git submodule update --init --recursive   # Populate after clone
```

---

## Rebaseline Plan — Phase Summary

The repo is undergoing a strategic rebaseline (see `tmp/plan.md`). Five areas:

| # | Area | Key Decisions Needed |
|---|------|----------------------|
| 1 | Docs, minutes, work items | Classify all items; add status page; clean nav |
| 2 | OpenAPI & specs | Confirm spec-first ownership; publish as first-class output |
| 3 | Reference implementation | Lock scope to I05; decide runnable vs illustrative |
| 4 | External integrations | Remove or replace ardupilot/qgroundcontrol with links |
| 5 | Repo governance | Choose one task tool; audit Vagrantfile; define review workflow |

---

## Open Decisions (require human input before proceeding)

- [ ] **OPS-01** — Task management: GitHub Issues or Asana?
- [ ] **JAVA-01** — Reference impl role: active-limited / maintenance / illustrative / paused?
- [ ] **EXT-01** — Submodules: keep / replace with links / remove?
- [ ] **API-01** — OpenAPI ownership already spec-first; confirm no Java generation needed
- [ ] **OPS-02** — Vagrantfile: valid / outdated / retire?
- [ ] **I05** — Timeline refresh: when is the redraft discussion happening?
- [ ] **I08** — Explicitly add DPG-light / decentralized framing into the spec

---

## Branch & Deploy Conventions

| Branch | Purpose |
|--------|---------|
| `master` | Main — target for PRs |
| `dev` | Auto-deploys docs to GitHub Pages on push |
| `i08`, `i06`, … | Feature branches per work item |

---

## Governance Gaps (to be filled)

- No `CONTRIBUTING.md` in root
- No `CODE_OF_CONDUCT.md` in root
- No GitHub issue or PR templates
- No CI for Java build/test (docs-only CI currently)
