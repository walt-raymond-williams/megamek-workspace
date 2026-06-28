# Agent Handoff

## Issue

- GitHub issue: `#77`
- Roadmap entry: `Epic: Add guarded TO&E and pilot assignment commands for MEK-RPG`
- Priority: `High`

## Goal

Add regression fixtures, tests, and a live disposable-campaign smoke checklist for the pilot assignment and TO&E command family.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- Implementation results from issues `#73`, `#74`, and `#75`
- Batch design from issue `#76` if accepted

## Expected Output

- Source regression tests and fixtures where practical.
- A live smoke checklist under `docs/current/` or updates to the existing live API smoke checklist docs.
- Clear known blockers for any live GUI checks that cannot run.

## Files And Areas

Likely files to read or edit:

- `external/src/mekhq/MekHQ/test`
- `docs/templates/mekhq-live-campaign-*.fixture.json`
- `docs/current/`
- `docs/current/KNOWN_COMMANDS.md`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
.\gradlew.bat --no-daemon :MekHQ:test --tests mekhq.service.LocalCampaignStateExporterTest --tests mekhq.service.LocalCommandReadinessExporterTest --tests mekhq.service.LocalControlServiceHttpTest
.\gradlew.bat --no-daemon :MekHQ:compileJava :MekHQ:checkstyleMain :MekHQ:checkstyleTest
```

Run source commands from `external/src/mekhq`.

## Constraints

- Live smoke checks must use copied or disposable campaign saves only.
- Do not overwrite active campaign saves.
- Do not hide unrun live GUI checks; record them as blockers or manual checks.

## Acceptance Criteria

- Coverage includes valid assignment, invalid role assignment, stale guard refusal, duplicate assignment refusal, valid force move, invalid force deletion/move refusal, and post-command reread verification.
- At least one negative request is followed by `/status` or `GET /campaign/commands` to prove server availability.
- Smoke checklist includes setup, dry-run, apply, reread, save-policy, and rollback/disposable-data guidance.
- Roadmap, tracking doc, and task board are updated when the issue closes.

## Open Questions

- Which disposable campaign has the simplest valid mix of MekWarriors, non-MekWarriors, BattleMechs, and force tree rows for repeatable tests?
- Should smoke use Sharpe's Strikers only after the user confirms a copied save path?
