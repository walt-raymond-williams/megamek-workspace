# Agent Handoff

## Issue

- GitHub issue: `#98`
- Roadmap entry: `Implement personnel turnover and HR/admin pressure export`
- Priority: `Medium`

## Goal

Expose personnel turnover/departure history and HR/admin pressure facts where MekHQ source tracks them, and return explicit unsupported markers where it does not.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_PLAY_API_GAP_PRODUCER_PLAN.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_PERSONNEL_DETAIL_API.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_ACTIVITY_HISTORY_SOURCE_AUDIT.md`
- The bounded live-play gap API design note.
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_PLAYTEST_API_GAP_REPORT.md`

## Expected Output

- Source audit of departure/status/service log sources and any admin/HR capacity model.
- Implementation of bounded turnover/history export if source-owned data exists.
- Explicit unsupported entries if departure reason or HR pressure is not tracked.
- Docs, fixtures, and tests.

## Files And Areas

Likely files to read or edit:

- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporter.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/personnel/`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/personnel/log/`
- Admin, HR, role, salary, and personnel-removal source classes found by `rg`.
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "departure|retire|resign|fire|dismiss|turnover|admin|HR|human resources|ServiceLogger|changeStatus|PersonnelStatus" external/src/mekhq/MekHQ/src
.\gradlew.bat --no-daemon :MekHQ:compileJava :MekHQ:checkstyleMain :MekHQ:checkstyleTest
```

## Constraints

- Do not infer a departure reason from absence in the current roster unless marked as unknown.
- Treat medical/patient logs with the existing explicit opt-in posture.
- Keep roster-wide histories bounded.

## Acceptance Criteria

- MEK-RPG can distinguish confirmed departures from missing-current-roster unknowns.
- HR/admin pressure is exact, unknown, or unsupported with source evidence.
- Tests cover supported and unsupported cases.

## Open Questions

- Does MekHQ have a source-owned HR/admin capacity pressure model, or only personnel roles/current counts?
- Are departure reasons durable structured data or only log text?
