# Agent Handoff

## Issue

- GitHub issue: `#95`
- Roadmap entry: `Design bounded live-play gap API contract`
- Priority: `High`

## Goal

Turn the MEK-RPG playtest gap request into a source-backed producer API design that chooses endpoint shapes, query parameters, bounded defaults, unsupported markers, and implementation sequence.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_PLAY_API_GAP_PRODUCER_PLAN.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_ACTIVITY_HISTORY_SOURCE_AUDIT.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_RELIABILITY_TRACKING.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_PLAYTEST_API_GAP_CHANGE_REQUEST_2026_07_16.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_PLAYTEST_API_GAP_REPORT.md`

## Expected Output

- A design note under `docs/current/` that maps each requested gap to:
  - producer-supported endpoint or section
  - query/filter/limit behavior
  - source owner or source gap
  - `unknown`, `withheld`, or `unsupported` behavior
  - implementation child issue
- Roadmap and task-board updates if sequencing changes.

## Files And Areas

Likely files to read or edit:

- `docs/current/MEK_RPG_LIVE_PLAY_API_GAP_PRODUCER_PLAN.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_ACTIVITY_HISTORY_API_TRACKING.md`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporter.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCommandReadinessExporter.java`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "pending-deployments|scenario|bot_forces|battle value|reinforce|salvage|Transaction|getTransactions|personnelLog|assignmentLog|transport|cargo" external/src/mekhq/MekHQ/src
```

## Constraints

- Do not implement source changes in this design issue unless the user explicitly expands scope.
- Avoid accepting MEK-RPG endpoint names verbatim when an existing endpoint or bounded history design is better.
- Keep the read-only, loopback-only, disabled-by-default API posture.
- Preserve uncertainty and evidence labels.

## Acceptance Criteria

- The design explicitly addresses all five request areas plus the reinforcement-arrival gap.
- Unbounded history/ledger requests are replaced with bounded query contracts.
- Existing overlapping issues, especially activity-history issue `#58`, are reused or referenced instead of duplicated.
- The next implementation issue is obvious.

## Open Questions

- Whether scenario intel should be a dedicated endpoint or an enriched pending-deployments/state DTO.
- Whether finance/salvage/reputation/XP history should land as one endpoint family or separate bounded domain endpoints.
