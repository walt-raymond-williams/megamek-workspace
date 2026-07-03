# Agent Handoff

## Issue

- GitHub issue: `#92`
- Roadmap entry: `Epic: Add Sarna-backed BattleTech immersion research workflow`
- Priority: `Medium`

## Goal

Test the new Sarna workflow against representative MEK-RPG/MekHQ campaign questions and record example output patterns agents can imitate.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SARNA_IMMERSION_WORKFLOW_TRACKING.md`
- `docs/current/SARNA_BATTLETECH_IMMERSION_WORKFLOW.md`
- docs changed by issue `#91`

## Expected Output

- A compact examples note or template examples showing how Sarna context improves contract, planet, faction, unit, and character-facing campaign answers.
- Roadmap/tracking/task updates.

## Files And Areas

Likely files to read or edit:

- `docs/current/SARNA_BATTLETECH_IMMERSION_WORKFLOW.md`
- `docs/current/BATTLETECH_CONTEXT.md`
- `docs/templates/`
- current campaign docs if examples use existing campaign context

## Commands

Useful commands or checks:

```powershell
git status --short --branch
```

## Constraints

- Do not include unrelated user changes.
- Do not overwrite campaign saves.
- Keep examples compact and campaign-facing.
- Commit completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- At least three representative examples are drafted using Sarna lookups and local campaign/source boundaries.
- Examples show concise immersive context without burying tactical recommendations.
- Any friction or follow-up improvements are recorded.
- Roadmap, tracking doc, and task board are updated and changes are committed.

## Open Questions

- Which live campaign facts should be used for the first example pass?
- Should examples be standalone docs or embedded in the workflow/template files?
