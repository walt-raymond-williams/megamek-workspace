# Agent Handoff

## Issue

- GitHub issue: `#97`
- Roadmap entry: `Implement inventory, cargo, transport, and recovery capacity export`
- Priority: `High`

## Goal

Expose inventory mass/value and transport/recovery capacity facts through the live MekHQ local API where MekHQ source owns the data, while explicitly marking unsupported capacity math.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_PLAY_API_GAP_PRODUCER_PLAN.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`
- The bounded live-play gap API design note.
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_PLAYTEST_API_GAP_REPORT.md`

## Expected Output

- Source-backed inventory/cargo/transport DTOs or explicit unsupported entries where exact capacity is not tracked.
- Aggregate totals for inventory tons/value when source-owned.
- Capacity rows for transport/recovery units where source-owned.
- Tests, fixtures, and docs.

## Files And Areas

Likely files to read or edit:

- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporter.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/parts/`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/unit/`
- Transport, cargo, warehouse, parts, and recovery vehicle classes found by `rg`.
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "warehouse|inventory|part|cargo|transport|capacity|recovery|towing|tonnage|getWeight" external/src/mekhq/MekHQ/src
.\gradlew.bat --no-daemon :MekHQ:compileJava :MekHQ:checkstyleMain :MekHQ:checkstyleTest
```

## Constraints

- Do not derive exact cargo capacity from raw save fields or display strings if MekHQ has no source-owned calculation.
- Mark exact capacity `unknown` or `unsupported` when the source cannot back it.
- Keep current market/procurement action data display-only.

## Acceptance Criteria

- MEK-RPG can see supported inventory mass/value and transport/recovery facts from live API data.
- Unsupported capacity math is explicit rather than silently absent.
- Tests cover known capacity and unsupported/unknown cases.

## Open Questions

- Which MekHQ classes compute cargo and unit-transport capacity today?
- Does MekHQ distinguish salvage/recovery loading from ordinary cargo capacity in source?
