# Guarded Live MekHQ Command API Tracking

## Purpose

GitHub Issues are the execution source of truth. This file is the compact local recovery snapshot for epic `#44`, branch state, issue state, next management step, and handoff paths.

## Integration Branch

- Branch: `codex/guarded-live-mekhq-command-api`
- Base: `master`
- Human review required before merge: `Yes`

## Issue Snapshot

- Last refreshed: `2026-06-22`
- Closed:
  - `#45`: Define guarded live MekHQ command envelope and prompt policy.
- Open:
  - `#43`: Discover first guarded live MekHQ command API easy wins for MEK-RPG.
  - `#46`: Implement live MekHQ command readiness and selector discovery.
  - `#47`: Design live MekHQ personnel death and status command API.
  - `#48`: Design live MekHQ medical treatment and prosthetic command API.
  - `#49`: Design live MekHQ unit-market purchase command API.
  - `#44`: Epic: Guarded live MekHQ command API for MEK-RPG.
- Blocked: none yet for this epic.

## Recommended Next Step

- Issue: `#46`
- Why next: `#45` defines the reusable command envelope; command readiness and selector discovery should now expose which commands are available or blocked before domain-specific write endpoints are implemented.
- Handoff: `docs/handoffs/active/implement-live-mekhq-command-readiness-selectors.md`

## Verification State

- Commands passed:
  - Documentation consistency review by source/doc inspection for issue `#45`.
- Manual checks:
  - Read issue `#45`, the active handoff, `MEKHQ_ADVANCE_DAY_CONTROL_API_PROTOTYPE.md`, and `LocalControlService.java`.
- Known blockers:
  - Source push for MekHQ itself remains separate from this workspace branch because `external/src/mekhq` points at upstream `MegaMek/mekhq`.

## Related Docs

- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/handoffs/archive/design-live-mekhq-command-envelope.md`
