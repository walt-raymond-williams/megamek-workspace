# Agent Handoff

## Issue

- GitHub issue: `#90`
- Roadmap entry: `Epic: Add Sarna-backed BattleTech immersion research workflow`
- Priority: `High`

## Goal

Design a practical workflow for using Sarna.net to enrich MEK-RPG campaign play with BattleTech lore and context while keeping local MekHQ data/source authoritative for mechanics and current state.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SARNA_IMMERSION_WORKFLOW_TRACKING.md`
- issue `#89` audit output

## Expected Output

- A design note under `docs/current/`, recommended path `docs/current/SARNA_BATTLETECH_IMMERSION_WORKFLOW.md`.
- Clear lookup triggers and output patterns for planets, factions, units, technology, historical events, manufacturers, mercenary commands, and character origins.
- Roadmap/tracking/task updates.

## Files And Areas

Likely files to read:

- `docs/current/HELP_FILE_WORKFLOW.md`
- `docs/current/CAMPAIGN_ANALYSIS_WORKFLOW.md`
- `docs/current/BATTLETECH_CONTEXT.md`
- issue `#89` audit output

## Commands

Useful commands or checks:

```powershell
git status --short --branch
```

## Constraints

- Do not include unrelated user changes.
- Keep the design practical and table-facing.
- Use Sarna for lore/context; use local campaign data and MegaMek/MekHQ source for current state and implementation behavior.
- Commit completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- Workflow defines when agents should search Sarna during live assist, scenario prep, campaign reports, and narrative/RPG work.
- Workflow distinguishes lore/context from local save/source facts without making the guidance feel legalistic.
- Examples include planet, faction, unit/equipment, historical event, and personnel-origin lookups.
- Roadmap, tracking doc, and task board are updated and changes are committed.

## Open Questions

- Should agents include Sarna context by default in every campaign report, or only when it adds meaningful texture?
- How should agents keep lore snippets concise during live tactical support?
