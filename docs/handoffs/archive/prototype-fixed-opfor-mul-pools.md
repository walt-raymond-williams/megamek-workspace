# Agent Handoff

## Issue

- GitHub issue: `#18`
- Roadmap entry: `Epic: Control MekHQ player and OPFOR mech rosters`
- Priority: `Medium`

## Goal

Prototype fixed OPFOR MUL pools that can be loaded into MekHQ bot formations for physical-miniatures play.

## Outcome

Completed on `2026-06-18`.

- Added `docs/current/FIXED_OPFOR_MUL_POOL_WORKFLOW.md`.
- Added `docs/templates/OPFOR_MUL_POOL_MANIFEST.csv`.
- Verified a local placeholder setup MUL can be generated with installed MekHQ/MegaMek jars through `EntityListFile.saveTo(...)` and parsed back with `MULParser`.
- Did not commit the raw placeholder MUL because it is generated from full MegaMek unit definitions and does not represent confirmed user inventory.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MECH_ROSTER_CONTROL_TRACKING.md`
- `docs/current/MECH_ROSTER_CONTROL_WORKFLOWS.md`
- `docs/current/TABLETOP_RESULT_MUL_WORKFLOW.md`
- `docs/current/MEGAMEK_PROJECT_PROFILE.md`

## Expected Output

- A documented prototype path for creating fixed OPFOR MULs.
- One or more sample MUL pool files only if they can be made from explicit user-provided or clearly placeholder units.
- Verification notes for loading a fixed MUL through `CustomizeBotForceDialog`.

## Files And Areas

Likely files to read or edit:

- `docs/current/MECH_ROSTER_CONTROL_WORKFLOWS.md`
- `docs/current/MECH_ROSTER_CONTROL_TRACKING.md`
- `analysis/` for generated scratch/prototype outputs
- `campaigns/` only if the user explicitly provides or approves campaign inputs

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "CustomizeBotForceDialog|MULParser|EntityListFile" external/src/mekhq/MekHQ/src external/src/megamek/megamek/src
```

## Constraints

- Do not commit raw campaign saves unless explicitly asked.
- Do not present placeholder MULs as the user's real miniature inventory.
- Prefer MekHQ/MegaMek serialization where possible instead of hand-written entity XML.
- Commit completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- The workflow explains how a fixed OPFOR MUL is created and loaded into a bot formation.
- The note identifies what can be verified without the user's real miniature list.
- Any generated artifact is clearly marked placeholder or source-derived.

## Open Questions

- Which physical miniatures should be represented in the first OPFOR pools?
- Should pools be organized by BV band, weight class, faction, scenario role, or a mix?
