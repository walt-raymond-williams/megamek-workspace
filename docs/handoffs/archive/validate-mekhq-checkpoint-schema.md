# Agent Handoff

## Issue

- GitHub issue: `#28`
- Roadmap entry: `Validate MekHQ checkpoint schema against disposable save`
- Priority: `Medium`

## Goal

Validate the draft MekHQ read-only checkpoint schema against a disposable MekHQ save, MekHQ UI facts where possible, and MEK-RPG's current XML summary helper output.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT_SCHEMA.md`
- `docs/current/SAVE_FORMAT_NOTES.md`
- `docs/current/SOURCE_CODE_GUIDE.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_READ_ONLY_CHECKPOINT_EXPORT_CONTRACT.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_SAVE_SUMMARY_HELPER.md`

## Expected Output

- Completed with validation note `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_VALIDATION.md`.
- Schema/export docs now link the validation note. No field rename was required.
- UI comparison was not completed; blocker is recorded in the validation note.

## Files And Areas

Likely files to read or edit:

- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT_SCHEMA.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT.md`
- `docs/current/SAVE_FORMAT_NOTES.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- MEK-RPG helper path, if available: `C:\Users\waltr\Documents\mek-rpg\scripts\summarize-mekhq-save.py`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
Test-Path 'C:\Users\waltr\Documents\mek-rpg\scripts\summarize-mekhq-save.py'
Test-Path 'C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\MekHQ.exe'
```

## Constraints

- Do not include unrelated user changes.
- Preserve uncertainty and evidence labels.
- Do not overwrite or edit campaign saves.
- Work only on copied/disposable save data when extracting.
- No write automation, headless day advancement, purchase/sale, repair, personnel mutation, or tactical result writeback.
- Commit and push completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- Done: identifies exact save/helper inputs used.
- Done: compares campaign identity/date/location/funds, personnel ids/names, unit ids/names/status, market offers, report support, and unsupported fields.
- Done: distinguishes method-backed values from raw XML/helper inferred values.
- Done: records MekHQ UI comparison as not completed, with blocker.
- Done: updates current docs to link validation; no contract-changing schema update was needed.

## Open Questions

- Resolved: first validation used copied shakedown autosave `analysis/tmp/issue-22/Autosave-1-The Learning Ropes-30250720.cpnx.gz`.
- Resolved: current environment ran the MEK-RPG helper directly.
