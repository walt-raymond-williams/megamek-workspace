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

- A validation note under `docs/current/`, likely `MEK_RPG_MEKHQ_CHECKPOINT_VALIDATION.md`.
- Schema/current-doc updates if validation changes field names, provenance expectations, unsupported fields, or warnings.
- Clear blocker note if UI comparison or MEK-RPG helper execution cannot run.

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

- Identifies exact save/helper inputs used.
- Compares campaign identity/date/location/funds, personnel ids/names, unit ids/names/status, at least one market offer if present, reports if present, and unsupported fields.
- Distinguishes method-backed values from raw XML/helper inferred values.
- Records whether MekHQ UI comparison was completed, skipped, or blocked.
- Updates schema/current docs when validation changes the contract.

## Open Questions

- Which disposable save should be used first: bundled quickstart copy, shakedown autosave copy, or a newly created throwaway save?
- Can the current environment run the MEK-RPG helper directly, or should validation consume previously generated helper JSON?
