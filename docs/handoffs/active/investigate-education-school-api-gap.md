# Agent Handoff

## Issue

- GitHub issue: `#109`
- Roadmap entry: `Epic: Close MEK-RPG live-play MekHQ API gaps`
- Priority: `High` / P1 user-blocking for MEK-RPG play

## Goal

Investigate MekHQ's education, school, training, graduation, and personnel skill/trait data model, then either implement the local-control API support MEK-RPG needs or produce a source-backed producer design/ticket with exact blockers and endpoint shape.

The immediate user need is roster management for `Sharpe's Strikers`: MEK-RPG can identify people marked `Student`, `Recruit`, `Dependent`, or `Background Character`, but cannot tell where students are enrolled, what they are studying, when they graduate, whether they already graduated, or which skills/traits/options make them useful for assignment.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_PLAY_API_GAP_PRODUCER_PLAN.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_PERSONNEL_DETAIL_API.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\handoffs\active\megamek-education-api-gap-handoff.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_PLAYTEST_API_GAP_REPORT.md`
  - Start with the top open finding: `2026-08-20 - Priority 1 education enrollment and graduation fields unavailable`.
- `C:\Users\waltr\Documents\mek-rpg\campaigns\sharpes-strikers\education-tracker.md`
- `C:\Users\waltr\Documents\mek-rpg\campaigns\sharpes-strikers\education-tracker-candidates.csv`
- `C:\Users\waltr\Documents\mek-rpg\scripts\report-mekhq-education-tracker.py`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\RICH_CHARACTER_MEKHQ_API_NEEDS.md`

## Expected Output

- Source-backed map of MekHQ classes/methods that own education, school, training, graduation, personnel skills, traits/options, abilities, awards, and XP.
- Implementation, tests, fixtures, and docs if the source model is safe enough to expose.
- If implementation is not safe yet, a producer-side design or follow-up issue with exact source owners, missing source services, blockers, and recommended endpoint shape.
- Updated workspace docs with durable findings and verification results.

## Files And Areas

Likely files to read or edit:

- `external/src/mekhq/MekHQ/src/mekhq/campaign/personnel/`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/personnel/skills/`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/personnel/enums/`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/universe/`
- `external/src/mekhq/MekHQ/src/mekhq/gui/dialog/`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporter*`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalPersonnelDetailExporter*`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_PERSONNEL_DETAIL_API.md`
- `docs/templates/mekhq-live-campaign-*.fixture.json`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "education|school|academy|student|graduate|graduation|training|course|program|enroll|enrolled|ability|option|award|experience|xp|Skill" external/src/mekhq/MekHQ/src
rg -n "personnel/detail|LocalCampaignStateExporter|LocalPersonnelDetailExporter|skills|awards|options|special abilities" external/src/mekhq/MekHQ/src/mekhq/service
```

Source verification should use the commands in `docs/current/KNOWN_COMMANDS.md` and `docs/current/SOURCE_CHANGE_WORKFLOW.md`. At minimum, run the targeted MekHQ service tests touched by the change plus compile/checkstyle, or record the exact blocker.

## Constraints

- Do not parse or edit active MekHQ saves as the API solution.
- Keep sensitive logs, medical details, patient information, hidden GM data, and long raw personnel history out by default.
- Prefer compact whole-roster education summaries plus richer person-detail data.
- Use explicit `Unknown`, empty, or not-applicable fields instead of silent omission.
- Keep whole-roster reads bounded for campaigns with 1000+ personnel.
- Do not include unrelated user changes.

## Acceptance Criteria

- Current students can expose school/program/time remaining when MekHQ source owns the data, or explicit `Unknown`/unsupported markers when it does not.
- Historical graduates or assignment-review candidates remain discoverable without relying on stale chat memory or active-save parsing.
- Person detail exposes relevant skills, traits/options, abilities, awards, and XP summary useful for job assignment, or records exact unsupported boundaries.
- Whole-roster personnel state stays compact and avoids sensitive logs by default.
- Fixtures or design examples cover students, graduates, dependents/background characters, unknown education state, and skill/trait summaries.
- Verification is run or exact blockers are recorded.
- Workspace roadmap/task docs and GitHub issue `#109` are updated before close-out.

## Open Questions

- Is `Student` status always backed by a structured school/training record, or can it be manually assigned without source-owned education data?
- Does MekHQ store multiple concurrent or historical education records per person?
- Does graduation automatically change status, rank, primary role, skills, or assignment, or does it only produce a prompt/report/manual follow-up?
- What source object owns expected graduation date and time remaining?
- Are traits/options stored in all personnel records or only exposed through selected-person detail?
