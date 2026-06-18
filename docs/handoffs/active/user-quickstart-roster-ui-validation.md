# User Task Checklist

## Issue

- GitHub issue: `#21`
- Related agent issue: `#17`
- Roadmap entry: `Epic: Control MekHQ player and OPFOR mech rosters`
- Owner: `User`

## Goal

Run the live MekHQ UI validation that Codex could not complete automatically: prove a disposable New Player Quickstart campaign can have one unit added and one original unit removed through GM controls.

## Safety Rules

- Do not overwrite the bundled quickstart save:

```text
external/installs/MekHQ-0.51.00/campaigns/The Learning Ropes.cpnx.gz
```

- Save under a disposable name/path before editing the roster.
- Placeholder units are acceptable for this workflow test.
- Record what happened; do not worry about making a perfect campaign roster.

## Checklist

1. Start MekHQ from:

```text
external/installs/MekHQ-0.51.00/MekHQ.exe
```

2. Open `New Player Quickstart` or load a disposable copy of `The Learning Ropes`.
3. Save immediately under a disposable name/path.
4. Turn on `GM Mode`.
5. Add one replacement unit through the Unit Market / Unit Selector using the GM add flow.
6. Open the Hangar.
7. Remove one original quickstart unit through `GM Mode > Remove Unit` or the equivalent GM remove action.
8. Note any prompts about pilots, assignments, formations, cargo, or transport.
9. Review or reassign pilots/crew, TO&E placement, and transport if MekHQ requires it.
10. Save the disposable campaign again.

## Report Back To Codex

Record these details in the thread or on GitHub issue `#21`:

- disposable save name/path
- exact UI path used to enable GM mode
- exact UI path used to add a unit
- unit added
- exact UI path used to remove a unit
- unit removed
- prompts, warnings, or errors shown by MekHQ
- whether pilots/crew, TO&E, or transport needed manual follow-up
- whether the final disposable save reopened or looked correct

## Acceptance Criteria

- The bundled quickstart save remains untouched.
- A disposable campaign save contains one added unit and one removed original quickstart unit.
- The UI add/remove paths are known well enough to update `QUICKSTART_ROSTER_REPLACEMENT_VERIFICATION.md`.
- Codex can close or unblock issue `#17` from the reported result.
