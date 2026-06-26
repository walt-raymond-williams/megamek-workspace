# Agent Handoff

## Issue

- GitHub issue: `#65`
- Roadmap entry: `Epic: Stabilize live MekHQ API reliability for MEK-RPG play`
- Priority: `High`

## Goal

Make `GET /campaign/state?sections=...` collect only requested sections where practical, and return partial data with structured warnings when optional collectors are slow.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_RELIABILITY_TRACKING.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEGAMEK_API_RELIABILITY_HANDOFF_2026-06-26.md`

## Expected Output

- Source audit of current `sections=` behavior.
- Implementation or design update for lazy section collection.
- Partial-response warning shape for skipped or timed-out optional sections, if implemented in this slice.
- Updated docs, fixtures, and targeted tests.

## Files And Areas

Likely files to read or edit:

- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporter.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- Live API fixture JSON under `docs/templates/`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "sections|unsupported|bridge_metadata|reportsSection|unitsSection|personnelSection|scenariosSection" external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporter.java
.\gradlew.bat :MekHQ:compileJava
.\gradlew.bat :MekHQ:checkstyleMain
```

## Constraints

- Keep `bridge_metadata` cheap and available.
- Do not silently omit requested data; skipped sections need warnings or unsupported entries.
- Be careful with Swing/threading if adding timeouts.

## Acceptance Criteria

- Narrowed state requests do not traverse unrelated expensive collectors unless documented as unavoidable.
- Partial responses are structured and consumer-safe if added.
- Tests or fixtures cover at least one narrowed request and one warning path.
- Verification passes or blockers are documented.

## Open Questions

- Is Java-level per-section timeout handling safe inside the current local control service, or should the first pass only make collection lazy?
