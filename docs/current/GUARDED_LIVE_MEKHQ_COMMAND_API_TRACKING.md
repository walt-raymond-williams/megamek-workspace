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
  - `#43`: Discover first guarded live MekHQ command API easy wins for MEK-RPG.
  - `#45`: Define guarded live MekHQ command envelope and prompt policy.
  - `#46`: Implement live MekHQ command readiness and selector discovery.
  - `#50`: Implement guarded live MekHQ campaign status-note command.
  - `#47`: Design live MekHQ personnel death and status command API.
- Open:
  - `#51`: Implement guarded live MekHQ personnel status command.
  - `#48`: Design live MekHQ medical treatment and prosthetic command API.
  - `#49`: Design live MekHQ unit-market purchase command API.
  - `#44`: Epic: Guarded live MekHQ command API for MEK-RPG.
- Blocked: none yet for this epic.

## Recommended Next Step

- Issue: `#51`
- Why next: `#47` found a conservative source-backed V1 for MEK-RPG personnel status events. Implementing it would prove the next guarded command after status-note while preserving tactical-result, medical, prisoner, and payout boundaries.
- Handoff: `docs/handoffs/active/implement-live-mekhq-personnel-status-command.md`

## Verification State

- Commands passed:
  - Documentation consistency review by source/doc inspection for issue `#45`.
  - `.\gradlew.bat :MekHQ:compileJava` from `external/src/mekhq` for issue `#46`.
  - `.\gradlew.bat :MekHQ:checkstyleMain` from `external/src/mekhq` for issue `#46`.
  - `.\gradlew.bat :MekHQ:compileJava` from `external/src/mekhq` for issue `#50`.
  - `.\gradlew.bat :MekHQ:checkstyleMain` from `external/src/mekhq` for issue `#50`.
- Source commits:
  - `e19740b110` in `external/src/mekhq`: `Expose command readiness endpoint`.
  - `4429d99ea2` in `external/src/mekhq`: `Add guarded status note command`.
- Manual checks:
  - Read issue `#45`, the active handoff, `MEKHQ_ADVANCE_DAY_CONTROL_API_PROTOTYPE.md`, and `LocalControlService.java`.
  - Read issue `#46`, `UnitMarketOffer.java`, `LocalCampaignStateExporter.java`, and source-confirmed selector methods before implementing `GET /campaign/commands`.
  - Read issue `#43`, bridge primitive notes, `Campaign#addReport(...)`, `Campaign#addFunds(...)`, `FinancesTab#addFundsActionPerformed()`, `PersonnelMarketDialog#hireActionListener(...)`, `ContractMarketDialog#acceptContract(...)`, and `UnitMarketPane#purchaseSelectedOffers()` before selecting issue `#50`.
  - Read issue `#50`, `LocalControlService.java`, `LocalCommandReadinessExporter.java`, `Campaign#addReport(...)`, and `DailyReportType` before implementing `POST /campaign/command/status-note`.
  - Read issue `#47`, `Person#changeStatus(...)`, `PersonnelStatus`, `Person#setPrisonerStatus(...)`, `PrisonerStatus`, `Unit#remove(...)`, `CampaignEventProcessor`, `ResolveScenarioTracker`, `CapturePrisoners`, and personnel-table status/prisoner UI commands before designing issue `#51`.
- Known blockers:
  - Source push for MekHQ itself remains blocked because `external/src/mekhq` points at upstream `MegaMek/mekhq` and GitHub returned `Permission to MegaMek/mekhq.git denied to walt-raymond-williams`.
  - Live status-note smoke testing remains not run; it needs a source-built MekHQ instance launched with `mekhq.controlApi.enabled=true` and a copied/disposable campaign loaded.

## Related Docs

- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_PERSONNEL_STATUS_COMMAND_DESIGN.md`
- `docs/handoffs/archive/discover-live-mekhq-command-api-easy-wins.md`
- `docs/handoffs/archive/design-live-mekhq-command-envelope.md`
- `docs/handoffs/archive/implement-live-mekhq-command-readiness-selectors.md`
- `docs/handoffs/archive/implement-live-mekhq-status-note-command.md`
- `docs/handoffs/archive/design-live-mekhq-personnel-status-command.md`
