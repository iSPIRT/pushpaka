# Pushpaka Rebaseline Plan

## Summary

This rebaseline incorporates the updated project intent:

1. `docs/` is the main public artifact:
   - meeting minutes
   - published work items
   - microsite content at `https://ispirt.github.io/pushpaka/`

2. `reference-implementation/` should no longer be assumed to drive the repo:
   - OpenAPI publication should be treated as the primary technical output
   - implementation beyond registry and flight authorisation should likely be put on hold
   - broader Java refactoring should not be treated as the current default priority

3. `ardupilot/` and `qgroundcontrol/` are external codebases:
   - they should likely be linked to rather than vendored in full

4. task management should use one tool only:
   - either GitHub Issues or Asana

5. `I05` timelines are being redrafted separately and should be refreshed after discussion

6. the `Vagrantfile` may be outdated and must be audited

7. `I08` should explicitly consider a lighter, spec-first, decentralized or DPG-light end-state rather than assuming a heavy centralized implementation

## Rebaselined Repo Mission

The repo should be repositioned as:

- a working-group publication repo
- a source for published documents and meeting records
- a source of truth for technical/open specifications
- a home for limited supporting reference code only where necessary
- not a broad implementation monorepo
- not a place to vendor and maintain full external platform codebases unless strongly justified

## Main Areas

### Area 1. Docs, Minutes, And Published Work
Covers:
- `docs/`
- work items such as `I05`, `I06`, `I08`
- meeting minutes
- microsite structure and publishing

### Area 2. OpenAPI And Technical Specification
Covers:
- published OpenAPI specs
- source-of-truth decision for API artifacts
- relationship between spec and reference code

### Area 3. Reference Implementation
Covers:
- `reference-implementation/`
- registry
- flight authorisation
- whether Java remains active, limited, paused, or illustrative only

### Area 4. External Integrations
Covers:
- `ardupilot/`
- `qgroundcontrol/`
- whether these should remain in repo or be replaced by links and notes

### Area 5. Repo Governance And Working Model
Covers:
- task management tool choice
- dev environment and `Vagrantfile`
- lifecycle states for docs/code
- repo simplification and maintenance boundaries

---

## Phase 0: Strategic Rebaseline And Scope Lock

### Docs, Minutes, And Published Work

#### `RB-00-DOC-01` Write a top-level repo mission statement
Document clearly that the repo is primarily for published docs, specs, and limited supporting code.

#### `RB-00-DOC-02` Reclassify all work items by current state
Classify `I05`, `I06`, `I08`, older work items, and minutes-related TODOs into:
- active
- under revision
- published
- paused
- archived

#### `RB-00-DOC-03` Rebaseline `I05` as the current narrow implementation anchor
Make clear that only registry and flight authorisation are candidates for near-term implementation support.

#### `RB-00-DOC-04` Rebaseline `I08` as a strategic architecture document
Explicitly state that `I08` may lead to a spec-first / DPG-light / decentralized model, not necessarily a large central implementation.

### OpenAPI And Technical Specification

#### `RB-00-API-01` Decide whether OpenAPI is spec-first or Java-generated
Choose one clear source-of-truth model:
- hand-maintained spec files
- generated from Java
- mixed, with strict ownership rules

Recommended default:
- spec-first, with Java no longer driving publication by default

#### `RB-00-API-02` Define the publication model for OpenAPI
Document how OpenAPI will be authored, reviewed, versioned, and published on the microsite.

### Reference Implementation

#### `RB-00-JAVA-01` Decide the role of `reference-implementation/`
Choose one:
- active but limited to registry + flight-auth
- maintenance-only
- illustrative/demo only
- paused except for critical updates
- no longer central to repo purpose

#### `RB-00-JAVA-02` Freeze broader implementation scope beyond `I05`
Make explicit that implementation work beyond registry and flight authorisation is on hold unless reactivated later.

### External Integrations

#### `RB-00-EXT-01` Decide the future of vendored ArduPilot and QGroundControl code
Choose whether to:
- keep temporarily
- replace with links and notes
- remove entirely after preserving any useful local context

### Repo Governance And Working Model

#### `RB-00-OPS-01` Choose one task management tool
Pick one:
- GitHub Issues
- Asana

Recommended default:
- GitHub Issues, if the repo remains the collaboration center

#### `RB-00-OPS-02` Audit the `Vagrantfile`
Decide whether it is:
- valid and should be documented
- outdated and should be replaced
- obsolete and should be retired

---

## Phase 1: Public Artifact Cleanup And Repo Simplification

### Docs, Minutes, And Published Work

#### `RB-01-DOC-01` Reorganize microsite content around what should be public
Make the docs structure clearly reflect:
- published work items
- active drafts
- meeting archive
- OpenAPI/spec publication
- current status

#### `RB-01-DOC-02` Add a “Current Status” page to the microsite
Summarize:
- what is active
- what is paused
- what is published
- what is being reconsidered

#### `RB-01-DOC-03` Clean the microsite navigation
Ensure `mkdocs.yml` matches the intended public information architecture.

#### `RB-01-DOC-04` Refresh `I05` timeline framing
Replace stale timeline language with the redrafted version after discussion.

#### `RB-01-DOC-05` Update document lifecycle labels
Apply consistent status language such as:
- draft
- in review
- published
- paused
- archived

### OpenAPI And Technical Specification

#### `RB-01-API-01` Make OpenAPI publication a first-class output
Treat published API specs as a primary technical deliverable of the repo.

#### `RB-01-API-02` Decouple spec publication from Java where possible
Do not require the Java codebase to remain the publication driver if the spec can stand alone.

#### `RB-01-API-03` Align microsite API pages with the new source-of-truth model
Update published OpenAPI pages to reflect the chosen ownership model.

### Reference Implementation

#### `RB-01-JAVA-01` Reposition the Java implementation in the repo docs
State clearly whether it is:
- active
- limited
- illustrative
- paused

#### `RB-01-JAVA-02` Document the exact in-scope surface if retained
If kept active, define scope as registry + flight authorisation only.

### External Integrations

#### `RB-01-EXT-01` Create integration notes for ArduPilot and QGroundControl
Explain:
- why they matter
- whether Pushpaka has touched them
- where canonical upstream repos live

### Repo Governance And Working Model

#### `RB-01-OPS-01` Document backlog workflow
Specify one working method for how tasks are created, tracked, and closed.

---

## Phase 2: Narrow-Scope Technical Cleanup

### OpenAPI And Technical Specification

#### `RB-02-API-01` Establish a clean canonical OpenAPI source
Consolidate to one authoritative location and one publication flow.

#### `RB-02-API-02` Ensure published OpenAPI artifacts are coherent
Make sure the published specs are versioned, linked, and internally consistent.

### Reference Implementation

#### `RB-02-JAVA-01` Keep Java only for essential support of the narrowed scope
If the repo still needs Java, limit work to what supports:
- registry
- flight authorisation
- spec alignment
- basic reproducibility if required

#### `RB-02-JAVA-02` Avoid broad refactors unless strictly necessary
Do not prioritize large architectural cleanup unless it directly supports the reduced mission.

#### `RB-02-JAVA-03` Decide whether Java should remain runnable
Choose one:
- runnable maintained reference
- partially maintained demo
- archival example

### Docs, Minutes, And Published Work

#### `RB-02-DOC-01` Link `I05`, `I06`, and OpenAPI consistently
Ensure these documents tell one coherent story:
- `I05` as operational anchor
- `I06` as technical bridge if retained
- OpenAPI as published interface artifact

#### `RB-02-DOC-02` Rework `I06` based on actual role
If `I06` is kept, make it support the narrower scope rather than implying a full implementation program.

---

## Phase 3: External Codebase Exit Or Containment Strategy

### External Integrations

#### `RB-03-EXT-01` Decide whether ArduPilot and QGroundControl should remain in-repo
Recommended default:
- remove and replace with references, unless there is active patching work that genuinely requires local co-location

#### `RB-03-EXT-02` Preserve any meaningful local knowledge before removal
If any local deltas matter:
- document them
- capture patch notes
- record upstream repo references and relevant commits

#### `RB-03-EXT-03` Replace embedded trees with documentation
Add docs that describe:
- integration relationship
- custom assumptions
- upstream ownership

### Repo Governance And Working Model

#### `RB-03-OPS-01` Simplify repo structure after external-code decision
Reduce maintenance burden by keeping only what is central to the repo mission.

---

## Phase 4: `I08` Strategic Direction And DPG-Light Framing

### Docs, Minutes, And Published Work

#### `RB-04-DOC-01` Add architecture-options framing into `I08`
Document alternatives such as:
- centralized implementation-heavy architecture
- spec-first interoperability architecture
- DPG-light model
- decentralized service landscape

#### `RB-04-DOC-02` Explicitly state what does not need centralized control
Capture the design principle that many services may not need to be centrally operated.

#### `RB-04-DOC-03` Separate normative requirements from reference implementation ideas
Prevent the document from implying that a heavy implementation program is mandatory.

#### `RB-04-DOC-04` Define the possible end-state for Pushpaka outputs
State the hypothesis that the main long-term output may be:
- standards
- interoperability specs
- trust frameworks
- minimal shared components only where necessary

---

## Phase 5: Ongoing Governance And Working Rhythm

### Repo Governance And Working Model

#### `RB-05-OPS-01` Define maintenance boundaries by repo area
Clarify which areas are:
- active
- maintenance-only
- paused
- external
- archived

#### `RB-05-OPS-02` Define review and publication workflow
Specify how:
- docs are reviewed
- specs are published
- code changes are accepted
- statuses are updated

#### `RB-05-OPS-03` Keep the repo aligned to mission over time
Periodically review whether the repo is still too broad for its intended role.

---

## Immediate Recommended Actions

1. Define the repo mission statement.
2. Decide whether OpenAPI becomes fully spec-first.
3. Decide the role of `reference-implementation/`.
4. Choose one task management tool.
5. Audit the `Vagrantfile`.
6. Rebaseline `I05` after the redraft discussion.
7. Add DPG-light / decentralized framing into `I08`.
8. Decide whether `ardupilot/` and `qgroundcontrol/` should remain in the repo.

## Important Defaults

- Default recommendation: make the repo docs-and-spec first.
- Default recommendation: keep Java only if needed for narrow `I05` support.
- Default recommendation: pause implementation ambitions beyond registry and flight authorisation.
- Default recommendation: remove or replace full vendored external codebases with links and notes unless there is an active need.
- Default recommendation: use one task-management system only.
- Default recommendation: let `I08` explicitly explore a lighter, decentralized, spec-led end-state.

## Acceptance Criteria For Rebaseline

A successful rebaseline should leave the repo in this condition:

- The repo purpose is obvious.
- It is clear what is active vs paused.
- OpenAPI publication has one clear source-of-truth.
- `reference-implementation/` has an explicitly limited or paused role.
- external codebases are either removed or clearly marked as external references.
- one task-management workflow is in use.
- `I05` and `I08` reflect current strategic direction rather than legacy implementation momentum.

