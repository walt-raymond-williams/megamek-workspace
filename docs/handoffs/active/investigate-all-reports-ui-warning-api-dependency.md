# Agent Handoff

## Issue

- GitHub issue: `#69`
- Roadmap entry: `Investigate All Reports UI warning dependency in MekHQ API`
- Priority: `High`

## Goal

Determine whether the local MekHQ API uses the same UI-facing "All Reports" path that the user observed showing warnings over or near its buttons, and decide whether that creates inefficiency, timeout risk, or warning/UI leakage that needs a source change.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_ACTIVITY_HISTORY_API_TRACKING.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_ACTIVITY_HISTORY_SOURCE_AUDIT.md`

## Expected Output

- Add a source-backed note under `docs/current/` explaining whether any local MekHQ API endpoint relies on the All Reports UI/report path.
- If the API does rely on that path, identify affected endpoint(s), performance or timeout risk, visible warning/overlay risk, and safer alternatives.
- If the API does not rely on that path, record the evidence and update relevant API docs or handoffs so the concern is resolved.
- Create follow-up implementation issue(s) only if a source change is needed.

## Files And Areas

Likely files to read or inspect when executing the issue:

- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporter.java`
- Report/history UI classes discovered from searching for "All Reports" and report dialog labels.
- Current API docs under `docs/current/MEK_RPG_LIVE_MEKHQ_*.md`.

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "All Reports|all reports|report" external/src/mekhq/MekHQ/src -g "*.java"
rg -n "report|history|LocalCampaignStateExporter|LocalControlService" docs/current -g "*.md"
```

## Constraints

- The user explicitly asked not to inspect code while creating this issue; do not treat this handoff as evidence that the API actually uses All Reports.
- Separate `Confirmed by user` UI observations, `Confirmed from source` API behavior, and `Inferred` performance risk.
- Preserve the disabled-by-default, loopback-only, read-only posture of existing API reads.
- Do not modify MekHQ source unless the investigation proves a source change is needed and a follow-up implementation issue is created or clearly in scope.

## Acceptance Criteria

- The investigation states whether the API does or does not use the UI-facing All Reports path, with source references.
- Any inefficiency, warning overlay, or timeout risk is classified as confirmed or inferred.
- Relevant roadmap/task/API docs are updated.
- Verification is run, or the exact blocker is recorded.
- Repository documentation changes are committed and pushed without including unrelated user changes.

## Open Questions

- Is "All Reports" a UI-only aggregation, a shared report collector, or unrelated to the current API implementation?
- Are current report/history endpoints bounded enough for API use, or do they accidentally traverse a broad UI report path?
- Should issue `#58` explicitly avoid All Reports-style aggregation in the activity-history design?
