# Agent Handoff

## Issue

- GitHub issue: `#72`
- Roadmap entry: `Epic: Add guarded TO&E and pilot assignment commands for MEK-RPG`
- Priority: `High`

## Goal

Design the guarded local MekHQ API contract for pilot assignment and TO&E edits using the source audit from issue `#71`.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/GUARDED_LIVE_MEKHQ_COMMAND_API_TRACKING.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_PILOT_TOE_SOURCE_AUDIT.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEGAMEK_TOE_PILOT_ASSIGNMENT_API_HANDOFF.md`

## Expected Output

- A design note under `docs/current/` covering readiness rows, request/response schemas, selectors, guard facts, dry-run behavior, refusal codes, prompt policy, save policy, and post-command reread verification.
- Updates to roadmap/tracking docs and follow-up implementation scope if the source audit changes the split.

## Files And Areas

Likely files to read or edit:

- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_PILOT_TOE_SOURCE_AUDIT.md`
- new focused design note under `docs/current/`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/GUARDED_LIVE_MEKHQ_COMMAND_API_TRACKING.md`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "units\\.assign_pilot|toe\\.move_unit|campaign/commands" docs/current external/src/mekhq/MekHQ/src
```

## Constraints

- Use the shared command envelope from issue `#45`.
- Treat role and force-edit validation as a first-class design concern; issue `#71` found that much of it is currently embedded in Swing menus rather than a reusable non-dialog service.
- Do not design a generic save patch endpoint.
- Do not require MEK-RPG to infer eligibility from display names.
- Keep `toe.batch_update` optional unless the source audit makes it cheap and safe.

## Acceptance Criteria

- Design includes `units.assign_pilot`, `units.unassign_pilot`, `units.swap_pilots`, `toe.move_unit`, `toe.create_force`, `toe.rename_force`, and `toe.delete_empty_force`.
- Readiness rows and read selector needs are specified.
- Refusal cases include invalid role, unavailable personnel, duplicate assignment, stale guards, scenario/deployment locks, invalid force structure, non-empty force deletion, and prompt requirements.
- V1 versus deferred scope is explicit.

## Open Questions

- Should pilot replacement be part of `assign_pilot` policy or only `swap_pilots`?
- Should V1 allow assignment to mothballed units?
- Should force names enforce uniqueness, and if so at which tree level?
