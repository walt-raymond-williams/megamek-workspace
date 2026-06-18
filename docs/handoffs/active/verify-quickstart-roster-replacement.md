# Agent Handoff

## Issue

- GitHub issue: `#17`
- Roadmap entry: `Epic: Control MekHQ player and OPFOR mech rosters`
- Priority: `High`

## Goal

Verify the simplest no-source-change workflow for replacing the New Player Quickstart roster in a disposable campaign save.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MECH_ROSTER_CONTROL_TRACKING.md`
- `docs/current/MECH_ROSTER_CONTROL_WORKFLOWS.md`
- `docs/current/MEGAMEK_PROJECT_PROFILE.md`
- `docs/current/SAVE_FORMAT_NOTES.md`

## Expected Output

- A short verification note under `docs/current/` or an update to `MECH_ROSTER_CONTROL_WORKFLOWS.md`.
- Clear steps for replacing quickstart units using MekHQ UI/GM controls.
- Any blockers or UI uncertainty recorded for the live shakedown.

## Files And Areas

Likely files to read or edit:

- `docs/current/MECH_ROSTER_CONTROL_WORKFLOWS.md`
- `docs/current/MECH_ROSTER_CONTROL_TRACKING.md`
- `docs/current/TASKS.md`
- `docs/current/ROADMAP.md`
- `analysis/tmp/` for disposable save copies, if needed

## Commands

Useful commands or checks:

```powershell
git status --short --branch
Test-Path 'C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\MekHQ.exe'
```

## Constraints

- Do not edit or overwrite the bundled quickstart save.
- Use a copied/disposable save.
- Preserve uncertainty if the UI path cannot be fully exercised.
- Commit completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- A copied quickstart campaign has at least one original unit removed through GM controls.
- At least one replacement unit is added through GM controls.
- Personnel, TO&E, and transport follow-up needs are documented.
- The workflow is documented well enough for the user to repeat manually.

## Open Questions

- Which exact replacement units should be used for the verification run?
- Should verification use placeholder units if the user's final miniature list is not yet available?
