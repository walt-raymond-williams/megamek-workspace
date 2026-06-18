# Agent Handoff

## Issue

- GitHub issue: `#14`
- Roadmap entry: `Epic: Control MekHQ player and OPFOR mech rosters`
- Priority: `High`

## Goal

Explore and decompose the best way to control MekHQ campaign rosters for a physical-miniatures campaign: changing the player's starting mercenary company units and constraining or replacing generated opposition forces.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MECH_ROSTER_CONTROL_TRACKING.md`
- `docs/current/MEGAMEK_PROJECT_PROFILE.md`
- `docs/current/SAVE_FORMAT_NOTES.md`
- `docs/current/SOURCE_CODE_GUIDE.md`
- `docs/current/TABLETOP_RESULT_MUL_WORKFLOW.md`

Useful installed docs:

- `external/installs/MekHQ-0.51.00/docs/StratCon/Custom RATs.txt`
- `external/installs/MekHQ-0.51.00/docs/StratCon/stratcon-faq-2.6.md`
- `external/installs/MekHQ-0.51.00/docs/RAT and Force Generator Stuff/rat-generator.txt`
- `external/installs/MekHQ-0.51.00/docs/RAT and Force Generator Stuff/force-generator.txt`

## Expected Output

- Source- and UI-grounded discovery notes under `docs/current/`.
- A recommendation for the simplest reliable workflow to alter the player roster in a copied quickstart campaign.
- A recommendation for OPFOR control: manual substitution, scenario editing, custom RATs, generated data packs, a workspace tool, or MekHQ source changes.
- Child GitHub issues and handoffs only after discovery identifies useful bounded work.

## Files And Areas

Likely files to read or edit:

- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MECH_ROSTER_CONTROL_TRACKING.md`
- `docs/current/SAVE_FORMAT_NOTES.md`
- `docs/current/SOURCE_CODE_GUIDE.md`
- `external/src/mekhq/MekHQ/src/mekhq/gui`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/mission`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/stratCon`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/universe`
- `external/installs/MekHQ-0.51.00/data/rat`
- `external/installs/MekHQ-0.51.00/data/universe/ratdata`
- `external/installs/MekHQ-0.51.00/data/forcegenerator`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "NewPlayerQuickstart|LAUNCHER_NEW_PLAYER_QUICKSTART_PATH|The Learning Ropes" external/src/mekhq/MekHQ/src
rg -n "Regenerate Bot Forces|generateForce|BotForce|RATGenerator|RandomUnitGenerator" external/src/mekhq/MekHQ/src/mekhq
rg -n "custom random assignment|ratdata|RAT configuration|scaled BV" external/installs/MekHQ-0.51.00/docs
```

## Constraints

- Do not edit the bundled quickstart save directly; work on copies.
- Do not include unrelated user changes.
- Prefer MekHQ UI/GM workflows and data-only custom RATs before proposing source changes.
- Preserve uncertainty and evidence labels.
- Do not create child issues until the work can be bounded with acceptance criteria.
- Commit completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- The epic is represented in GitHub, `ROADMAP.md`, and `MECH_ROSTER_CONTROL_TRACKING.md`.
- Discovery distinguishes confirmed UI behavior, source-confirmed behavior, installed-doc behavior, and inference.
- The next agent can start discovery without rereading this conversation.
- Any proposed tool or source change explains why existing MekHQ UI, scenario editing, or custom RAT data is insufficient.

## Open Questions

- Which physical miniatures and variants should be legal for the player and OPFOR pools?
- Should OPFOR generation be strictly limited to owned miniatures, or is close-BV manual substitution acceptable?
- Should player roster changes preserve the quickstart's finances, personnel, parts inventory, and transport assignments exactly, or is a looser story-first conversion acceptable?
