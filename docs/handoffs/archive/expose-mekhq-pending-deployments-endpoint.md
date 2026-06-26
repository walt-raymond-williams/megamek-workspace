# Agent Handoff

## Issue

- GitHub issue: `#66`
- Roadmap entry: `Epic: Stabilize live MekHQ API reliability for MEK-RPG play`
- Priority: `High`

## Goal

Expose a lightweight live-read path for pending operations and unit/personnel commitments so MEK-RPG can confirm table-critical deployment facts without broad campaign-state reads.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_RELIABILITY_TRACKING.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEGAMEK_API_RELIABILITY_HANDOFF_2026-06-26.md`

## Expected Output

- Source-backed endpoint or summary-subsection design for pending scenario names, ids, dates, force links, and relevant unit/personnel commitments.
- Implementation if the source path is clear and safe.
- Fixtures/tests and consumer-facing docs.

## Files And Areas

Likely files to inspect:

- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporter.java`
- Scenario, force, unit, and personnel source classes found by `rg`.
- Existing scenario export tests/fixtures.

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "getScenarios|Scenario|Force|playerForce|personnel|crew|assignment" external/src/mekhq/MekHQ/src -g "*.java"
.\gradlew.bat :MekHQ:compileJava
.\gradlew.bat :MekHQ:checkstyleMain
```

## Constraints

- Do not infer a personnel commitment as confirmed unless source ownership is clear.
- Keep the endpoint read-only, lightweight, and bounded.
- Preserve unsupported metadata for commitment facts that cannot be source-confirmed.

## Acceptance Criteria

- MEK-RPG has a small read-only call for pending operation facts.
- Double-M-style personnel commitment lookup is supported or explicitly unsupported with a reason.
- Pending operation response includes ids/names/dates where available.
- Verification passes or blockers are documented.

## Open Questions

- Should the endpoint be `/campaign/pending-deployments`, a summary subsection, or a narrowed state section?
