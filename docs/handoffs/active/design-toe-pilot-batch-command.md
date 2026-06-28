# Agent Handoff

## Issue

- GitHub issue: `#76`
- Roadmap entry: `Epic: Add guarded TO&E and pilot assignment commands for MEK-RPG`
- Priority: `Medium`

## Goal

Design the optional atomic batch command for combined lance setup operations after individual pilot and TO&E command semantics are known.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- Design note from issue `#72`
- Implementation notes from issues `#74` and `#75` if available
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEGAMEK_TOE_PILOT_ASSIGNMENT_API_HANDOFF.md`

## Expected Output

- A design note or design-note section for `toe.batch_update`.
- A recommendation: implement now, defer until after individual commands are smoke-tested, or reject batch for V1.

## Files And Areas

Likely files to read or edit:

- command design note from issue `#72`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "batch|toe\\.batch_update|atomic" docs/current external/src/mekhq/MekHQ/src
```

## Constraints

- Batch is optional; do not force it into V1 if individual commands are not stable.
- Do not allow partial apply unless a future opt-in policy is explicitly designed.
- Reuse individual command selectors, guards, prompt policy, idempotency, and save policy.

## Acceptance Criteria

- Operation schema supports create force, move unit, and assign pilot with temporary client refs where needed.
- Atomic dry-run/apply semantics are specified.
- Refusal aggregation identifies every blocking operation without mutation.
- V1/deferred recommendation is explicit.

## Open Questions

- Is batch needed for first MEK-RPG use, or can the client sequence individual dry-run/apply calls safely?
- If one operation changes state revision, how should later operations in the same batch validate guards?
