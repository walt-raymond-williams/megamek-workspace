# Agent Handoff

## Issue

- GitHub issue: `#20`
- Roadmap entry: `Epic: Control MekHQ player and OPFOR mech rosters`
- Priority: `Medium`

## Goal

Decide whether custom RATs are worth using for physical-miniature OPFOR control, compared with manual regeneration and fixed OPFOR MUL substitution.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MECH_ROSTER_CONTROL_TRACKING.md`
- `docs/current/MECH_ROSTER_CONTROL_WORKFLOWS.md`
- `docs/current/MEGAMEK_PROJECT_PROFILE.md`
- `external/installs/MekHQ-0.51.00/docs/StratCon/Custom RATs.txt`
- `external/installs/MekHQ-0.51.00/docs/RAT and Force Generator Stuff/rat-generator.txt`

## Expected Output

- A recommendation under `docs/current/` comparing custom RATs, fixed MULs, manual substitution, workspace tooling, and MekHQ source changes.
- Child implementation issues only if a data/tool/source path is justified.

## Files And Areas

Likely files to read or edit:

- `docs/current/MECH_ROSTER_CONTROL_WORKFLOWS.md`
- `docs/current/MECH_ROSTER_CONTROL_TRACKING.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `external/installs/MekHQ-0.51.00/data/rat`
- `external/installs/MekHQ-0.51.00/data/universe/ratdata`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "custom random assignment|ratdata|RAT configuration|scaled BV" external/installs/MekHQ-0.51.00/docs
```

## Constraints

- Final custom-RAT decisions need the user's physical miniature list.
- Prefer data-only or UI workflows before proposing source changes.
- Commit completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- The recommendation explains when custom RATs are better than fixed OPFOR MULs.
- The recommendation identifies the minimum data needed from the user's miniature inventory.
- Any proposed implementation path has clear acceptance criteria and verification.

## Open Questions

- Should generated OPFOR be strictly limited to owned miniatures, or can the GM substitute close-BV physical units?
- Should custom RATs live in the installed data tree, a user data directory, or generated workspace output copied into place?
