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
- Open: `#14` Epic: Control MekHQ player and OPFOR mech rosters; child issues `#17`, `#18`, `#19`, and `#20`.
- Blocked: Final constrained roster work needs the user's physical miniature list.

## Recommended Next Step

- Issue: `#17`
- Why next: Verify the no-source-change quickstart roster replacement workflow in a disposable save before building data models, fixed OPFOR pools, or custom RATs.
- Handoff: `docs/handoffs/active/verify-quickstart-roster-replacement.md`

## Verification State

- Commands passed: GitHub issue `#14` created with labels `epic` and `enhancement`; child issues `#17` through `#20` created with labels `agent-task` and `enhancement`.
- Manual checks: Source/docs inspection confirmed the New Player Quickstart loads `campaigns/The Learning Ropes.cpnx.gz`, GM add/remove is safer than save surgery for player roster control, scenario edit can regenerate bot forces, bot formations can be edited or loaded from fixed MULs, custom AtB RAT metadata is documented under installed StratCon docs, and StratCon OPFOR is generally scaled-BV from RAT/random assignment sources.
- Known blockers: Local source build/test verification remains blocked by the Java 17 Gradle daemon/toolchain issue documented in `docs/current/TASKS.md`.

## Related Docs

- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MECH_ROSTER_CONTROL_WORKFLOWS.md`
- `docs/current/TABLETOP_RESULT_MUL_WORKFLOW.md`
- `docs/handoffs/active/mech-roster-control-epic.md`
- `docs/handoffs/active/verify-quickstart-roster-replacement.md`
- `docs/handoffs/active/define-physical-miniature-roster-model.md`
- `docs/handoffs/active/prototype-fixed-opfor-mul-pools.md`
- `docs/handoffs/active/decide-custom-rat-strategy.md`
