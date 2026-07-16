# Agent Handoff

## Issue

- GitHub issue: `#100`
- Roadmap entry: `Implement bounded salvage, finance, reputation, and XP ledgers`
- Priority: `High`

## Goal

Expose bounded, queryable source-owned history for finance transactions, salvage dispositions, contract performance/reputation, and personnel XP sources where MekHQ actually tracks those facts.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_PLAY_API_GAP_PRODUCER_PLAN.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_ACTIVITY_HISTORY_SOURCE_AUDIT.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_ACTIVITY_HISTORY_API_TRACKING.md`
- The bounded live-play gap API design note.
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_PLAYTEST_API_GAP_REPORT.md`

## Expected Output

- Source implementation for the supported first ledger/history families, or a narrower design if source audit shows the requested facts are not durable.
- Required date windows, limits, and filters for every history read.
- Explicit unsupported entries for untracked item-level sale provenance, projected reputation, faction-standing deltas, or XP source deltas.
- Docs, fixtures, and tests updated.

## Files And Areas

Likely files to read or edit:

- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporter.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/finances/`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/personnel/`
- Contract, scenario resolution, salvage, and reputation source classes found by `rg`.
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "class Transaction|getTransactions|reputation|standing|salvage|XP|experience|award|Contract|ResolveScenarioTracker" external/src/mekhq/MekHQ/src
.\gradlew.bat --no-daemon :MekHQ:compileJava :MekHQ:checkstyleMain :MekHQ:checkstyleTest
```

## Constraints

- Do not add unbounded `all history` endpoints.
- Coordinate with issue `#58` activity-history design; do not create a competing history contract.
- Treat reports as evidence only when the design says report-derived facts are acceptable.
- Preserve read-only behavior.

## Acceptance Criteria

- Finance transaction reads are queryable and bounded.
- Salvage, reputation, and XP facts are either source-backed or explicitly unsupported.
- MEK-RPG can answer supported "what changed and why?" questions without stale notes or save parsing.
- Tests prove limit/filter behavior.

## Open Questions

- Does MekHQ track item-level salvage sale disposition in a durable source object, or only aggregate contract values and finance/report text?
- Are reputation and faction-standing deltas persisted as deltas, or only current values/report effects?
