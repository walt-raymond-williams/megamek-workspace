# Mech Roster Control Tracking

## Purpose

GitHub Issues are the execution source of truth. This file is the compact local recovery snapshot for controlling MekHQ player and OPFOR rosters in a physical-miniatures campaign.

## Integration Branch

- Branch: `Not created`
- Base: `master`
- Human review required before merge: `Yes`, if a multi-issue implementation branch is created.

## Issue Snapshot

- Last refreshed: `2026-06-18`
- Closed: None.
- Open: `#14` Epic: Control MekHQ player and OPFOR mech rosters.
- Blocked: Final constrained roster work needs the user's physical miniature list.

## Recommended Next Step

- Issue: `#14`
- Why next: The epic needs discovery before useful child issues can be created. Start by confirming in-game and source-supported ways to alter the quickstart player roster, then confirm the exact AtB/StratCon OPFOR generation path and data hooks.
- Handoff: `docs/handoffs/active/mech-roster-control-epic.md`

## Verification State

- Commands passed: GitHub issue `#14` created with labels `epic` and `enhancement`.
- Manual checks: Initial source/docs inspection confirmed the New Player Quickstart loads `campaigns/The Learning Ropes.cpnx.gz`, custom AtB RAT metadata is documented under installed StratCon docs, and StratCon OPFOR is generally scaled-BV from RAT/random assignment sources.
- Known blockers: Local source build/test verification remains blocked by the Java 17 Gradle daemon/toolchain issue documented in `docs/current/TASKS.md`.

## Related Docs

- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/TABLETOP_RESULT_MUL_WORKFLOW.md`
- `docs/handoffs/active/mech-roster-control-epic.md`
