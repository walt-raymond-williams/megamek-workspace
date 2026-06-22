# Agent Handoff

## Issue

- GitHub issue: `#40`
- Roadmap entry: `Deepen live API finance, personnel, and unit sections`
- Priority: `Medium`

## Goal

Expose richer method-backed finance, personnel, and unit context through the live read-only MekHQ campaign API.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEGAMEK_LIVE_API_CHANGE_REQUEST.md`

Prefer completing or reviewing issue `#39` first so this work reuses the settled trust envelope and unsupported-entry shape.

## Expected Output

- Source-backed mapping for finance, personnel, and unit fields requested by MEK-RPG.
- Live API DTO/output expansion for safe method-backed fields.
- Structured unsupported entries for unavailable, unsafe, or raw-save-only fields.
- Sanitized fixture updates with non-empty examples where possible.
- Documentation of provenance, limitations, and verification.

## Files And Areas

Likely files to read or edit:

- `external/src/mekhq`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`
- `docs/templates/mekhq-live-campaign-*.fixture.json`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg "class Campaign|class Finances|class Person|class Unit|salary|fatigue|injur|repair" external/src/mekhq
.\gradlew.bat :MekHQ:compileJava
.\gradlew.bat :MekHQ:checkstyleMain
```

Run Gradle commands from `external/src/mekhq`. Record exact blockers if source verification cannot run.

## Constraints

- Keep the endpoint read-only and live-context-only for loaded campaigns.
- Do not expose raw saves or require MEK-RPG to parse active saves.
- Do not invent field meanings; mark inferred or unsupported fields explicitly.
- Do not include unrelated user changes.
- Commit completed workspace changes before stopping unless explicitly told not to.

## Acceptance Criteria

- Finance/personnel/unit sections provide richer useful context or explicit unsupported entries.
- Method-backed values include source/provenance where practical.
- Fixtures/docs are updated for MEK-RPG adapter use.
- Verification commands or blockers are recorded.

## Open Questions

- Which personnel injury/fatigue/pay fields are safe and stable in source?
- Which unit crew, tech, damage, repair, transport, cargo, and scenario assignment fields are available without creating action semantics?
