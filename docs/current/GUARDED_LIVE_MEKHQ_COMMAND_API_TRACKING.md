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
  - `#46`: Implement live MekHQ command readiness and selector discovery.
- Open:
  - `#43`: Discover first guarded live MekHQ command API easy wins for MEK-RPG.
  - `#47`: Design live MekHQ personnel death and status command API.
  - `#48`: Design live MekHQ medical treatment and prosthetic command API.
  - `#49`: Design live MekHQ unit-market purchase command API.
  - `#44`: Epic: Guarded live MekHQ command API for MEK-RPG.
- Blocked: none yet for this epic.

## Recommended Next Step

- Issue: `#43`
- Why next: refresh the easy-win ranking now that `GET /campaign/commands` exists, then proceed into the domain-specific designs for personnel status, medical/prosthetics, and unit-market purchase.
- Handoff: `docs/handoffs/active/discover-live-mekhq-command-api-easy-wins.md`

## Verification State

- Commands passed:
  - Documentation consistency review by source/doc inspection for issue `#45`.
  - `.\gradlew.bat :MekHQ:compileJava` from `external/src/mekhq` for issue `#46`.
  - `.\gradlew.bat :MekHQ:checkstyleMain` from `external/src/mekhq` for issue `#46`.
- Source commits:
  - `e19740b110` in `external/src/mekhq`: `Expose command readiness endpoint`.
- Manual checks:
  - Read issue `#45`, the active handoff, `MEKHQ_ADVANCE_DAY_CONTROL_API_PROTOTYPE.md`, and `LocalControlService.java`.
  - Read issue `#46`, `UnitMarketOffer.java`, `LocalCampaignStateExporter.java`, and source-confirmed selector methods before implementing `GET /campaign/commands`.
- Known blockers:
  - Source push for MekHQ itself remains blocked because `external/src/mekhq` points at upstream `MegaMek/mekhq` and GitHub returned `Permission to MegaMek/mekhq.git denied to walt-raymond-williams`.

## Related Docs

- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/handoffs/archive/design-live-mekhq-command-envelope.md`
- `docs/handoffs/archive/implement-live-mekhq-command-readiness-selectors.md`
