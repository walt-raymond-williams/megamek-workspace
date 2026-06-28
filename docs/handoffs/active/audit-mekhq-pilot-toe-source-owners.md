# Agent Handoff

## Issue

- GitHub issue: `#71`
- Roadmap entry: `Epic: Add guarded TO&E and pilot assignment commands for MEK-RPG`
- Priority: `High`

## Goal

Identify the MekHQ source owners for pilot/crew assignment, unassignment, swaps, force tree edits, and TO&E unit movement before any command API design or implementation proceeds.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/GUARDED_LIVE_MEKHQ_COMMAND_API_TRACKING.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEGAMEK_TOE_PILOT_ASSIGNMENT_API_HANDOFF.md`

## Expected Output

- A source-backed note under `docs/current/` mapping assignment and TO&E source owners.
- A recommendation for issue `#72`: direct design, source service extraction first, or explicit V1 blockers.
- Updates to `ROADMAP.md`, `TASKS.md`, and `GUARDED_LIVE_MEKHQ_COMMAND_API_TRACKING.md` when the issue closes.

## Files And Areas

Likely files to read or edit:

- `external/src/mekhq/MekHQ/src`
- `docs/current/`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/GUARDED_LIVE_MEKHQ_COMMAND_API_TRACKING.md`

Search starting points:

- assignment, crew, pilot, commander, entity, unit personnel links
- force tree, TO&E, force add/remove/rename/move UI actions
- scenario deployment or pending-deployment locks
- unit mothball, transport, repair, and in-transit status checks

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "assign|unassign|swap|crew|pilot|gunner|commander" external/src/mekhq/MekHQ/src
rg -n "Force|force tree|TO&E|toe|move.*unit|rename.*force|delete.*force" external/src/mekhq/MekHQ/src
```

## Constraints

- This is a source audit, not an implementation issue.
- Preserve uncertainty with evidence labels.
- Do not modify MekHQ source in this issue unless the user explicitly expands scope.
- Do not edit campaign saves.

## Acceptance Criteria

- The audit cites concrete source classes/files.
- Confirmed source facts, inferences, and unknowns are separated.
- Prompt/dialog dependencies are identified.
- Selector candidates and stale-guard needs are identified.
- V1 refusal cases and blockers are listed for pilot assignment and TO&E edits.

## Open Questions

- Does MekHQ already have reusable non-dialog assignment and force-edit services?
- Which unit/person/force ids survive save/reload and which should be live-session selectors?
- What exact source behavior prevents assignment or force edits during deployment, transit, repair, mothballing, or scenario locks?
