# Agent Handoff: Define Tabletop Result Input Schema

## Issue

- GitHub issue: `#9`
- Roadmap entry: `Epic: Robust tabletop battle result MUL workflow`
- Priority: `High`

## Goal

Define the minimum practical tabletop result/input schema needed to get hand-played BattleTech results back into MekHQ. The schema should be useful whether the final workflow is manual entry through MekHQ's built-in UI, editing/importing a battle-record MUL, or generating a battle-record MUL with custom tooling.

Current posture: this is discovery, not a commitment to custom implementation. Keep the schema focused on the information the tabletop player/GM must capture and on how that information maps to MekHQ's built-in resolution workflow.

## Outcome

Completed on `2026-06-18`.

- Added `docs/current/TABLETOP_RESULT_INPUT_SCHEMA.md`.
- Separated minimum first-campaign manual capture fields from optional future generator fields.
- Included scenario fields, unit result buckets, damage fields, personnel/ejection fields, kill credit, examples, and issue `#10` validation recommendations.
- Updated roadmap, task board, and tabletop MUL workflow links.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEGAMEK_PROJECT_PROFILE.md`
- `docs/current/SOURCE_CODE_GUIDE.md`
- `docs/current/TABLETOP_RESULT_MUL_WORKFLOW.md`
- `docs/current/SALVAGE_RULES_NOTES.md`
- `docs/handoffs/active/robust-tabletop-battle-result-mul-workflow.md`

## Expected Output

- A durable schema/design note under `docs/current/`.
- Required and optional fields for units, pilots/crew, armor/internal state, critical damage when needed, ammo state if relevant, destroyed/devastated/salvage/retreated outcomes, kills, battlefield-control prompt guidance, and scenario outcome inputs.
- Example result records for at least one simple player unit and one enemy/salvage candidate.
- A short note identifying which fields are needed no matter what and which fields only matter if custom generation is later chosen.

## Files And Areas

- `docs/current/`
- `docs/current/SOURCE_CODE_GUIDE.md`
- `docs/current/SAVE_FORMAT_NOTES.md`
- `docs/handoffs/active/robust-tabletop-battle-result-mul-workflow.md`

## Commands

```powershell
git status --short --branch
```

## Constraints

- Do not edit campaign saves.
- Prefer source-confirmed fields over inferred MUL XML structure.
- Separate required fields from optional or future fields.

## Acceptance Criteria

- The schema is implementable without editing campaign saves directly.
- Required fields are separated from optional or future fields.
- The schema does not assume custom generation is mandatory; it supports built-in MekHQ result-entry/import workflows as a first-class possible outcome.
- Unresolved edge cases are listed, especially ejections, prisoner/casualty states, salvage ownership, off-board retreat, and ammunition/critical state fidelity.

## Open Questions

- What is the smallest schema that supports the user's likely first campaign battles?
- How much damage fidelity does MekHQ require for repair/salvage consequences?
