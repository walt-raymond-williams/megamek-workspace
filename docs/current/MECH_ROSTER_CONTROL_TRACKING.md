# Mech Roster Control Tracking

## Purpose

GitHub Issues are the execution source of truth. This file is the compact local recovery snapshot for controlling MekHQ player and OPFOR rosters in a physical-miniatures campaign.

## Integration Branch

- Branch: `Not created`
- Base: `master`
- Human review required before merge: `Yes`, if a multi-issue implementation branch is created.

## Issue Snapshot

- Last refreshed: `2026-06-18`
- Closed: `#18`, `#19`, and `#20`.
- Open: `#14` Epic: Control MekHQ player and OPFOR mech rosters; child issue `#17`.
- Blocked: Full completion of `#17` needs a live MekHQ UI pass; final constrained roster work needs the user's physical miniature list.

## Recommended Next Step

- Issue: `#17` when live UI is available; otherwise leave roster-control execution and return to the broader task queue.
- Why next: `#18`, `#19`, and `#20` are complete. The remaining roster-control child is blocked on user-operated MekHQ validation.
- Handoff: `docs/handoffs/active/verify-quickstart-roster-replacement.md`

## Verification State

- Commands passed: GitHub issue `#14` created with labels `epic` and `enhancement`; child issues `#17` through `#20` created with labels `agent-task` and `enhancement`; `docs/templates/PHYSICAL_MINIATURE_ROSTER.csv` parsed successfully with `Import-Csv`; issue `#18` generated a local placeholder fixed OPFOR setup MUL with installed jars and parsed it back with `MULParser` as three entities.
- Manual checks: Source/docs inspection confirmed the New Player Quickstart loads `campaigns/The Learning Ropes.cpnx.gz`, GM add/remove is safer than save surgery for player roster control, scenario edit can regenerate bot forces, bot formations can be edited or loaded from fixed setup MULs, installed docs describe custom AtB RAT metadata under `data/universe/ratdata`, and MekHQ's current unit generator path uses the modern `RATGeneratorConnector` over `data/forcegenerator`. Issue `#17` also confirmed the installed MekHQ executable and a disposable quickstart save copy.
- Known blockers: Local source build/test verification remains blocked by the Java 17 Gradle daemon/toolchain issue documented in `docs/current/TASKS.md`. Live UI automation for `#17` is blocked because the Windows Computer Use helper reported `Computer Use native pipe path is unavailable`.

## Related Docs

- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MECH_ROSTER_CONTROL_WORKFLOWS.md`
- `docs/current/QUICKSTART_ROSTER_REPLACEMENT_VERIFICATION.md`
- `docs/current/PHYSICAL_MINIATURE_ROSTER_MODEL.md`
- `docs/current/FIXED_OPFOR_MUL_POOL_WORKFLOW.md`
- `docs/current/CUSTOM_RAT_STRATEGY.md`
- `docs/current/TABLETOP_RESULT_MUL_WORKFLOW.md`
- `docs/handoffs/active/mech-roster-control-epic.md`
- `docs/handoffs/active/verify-quickstart-roster-replacement.md`
- `docs/handoffs/archive/define-physical-miniature-roster-model.md`
- `docs/handoffs/archive/prototype-fixed-opfor-mul-pools.md`
- `docs/handoffs/archive/decide-custom-rat-strategy.md`
