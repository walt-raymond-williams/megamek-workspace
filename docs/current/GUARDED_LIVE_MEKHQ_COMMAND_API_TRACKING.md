# Guarded Live MekHQ Command API Tracking

## Purpose

GitHub Issues are the execution source of truth. This file is the compact local recovery snapshot for epic `#44`, branch state, issue state, next management step, and handoff paths.

## Integration Branch

- Branch: `codex/guarded-live-mekhq-command-api`
- Base: `master`
- Human review required before merge: `Yes`

## Issue Snapshot

- Last refreshed: `2026-06-23`
- Closed:
  - `#43`: Discover first guarded live MekHQ command API easy wins for MEK-RPG.
  - `#45`: Define guarded live MekHQ command envelope and prompt policy.
  - `#46`: Implement live MekHQ command readiness and selector discovery.
  - `#50`: Implement guarded live MekHQ campaign status-note command.
  - `#47`: Design live MekHQ personnel death and status command API.
  - `#51`: Implement guarded live MekHQ personnel status command.
  - `#48`: Design live MekHQ medical treatment and prosthetic command API.
  - `#53`: Implement guarded live MekHQ personnel fatigue command.
  - `#49`: Design live MekHQ unit-market purchase command API.
  - `#54`: Implement guarded live MekHQ unit-market purchase command.
  - `#52`: Design live MekHQ contract selection command API.
- Open:
  - `#55`: Implement guarded live MekHQ contract accept command.
  - `#44`: Epic: Guarded live MekHQ command API for MEK-RPG.
- Blocked: none yet for this epic.

## Recommended Next Step

- Issue: `#55`
- Why next: Contract selection design is complete; the next slice is implementing guarded contract accept using MekHQ-owned acceptance logic, source-owned finance, mission insertion, contract acceptance, market removal, explicit known prompt choices, failure isolation, and readiness selectors.
- Handoff: `docs/handoffs/active/implement-live-mekhq-contract-accept-command.md`

## Verification State

- Commands passed:
  - Documentation consistency review by source/doc inspection for issue `#45`.
  - `.\gradlew.bat :MekHQ:compileJava` from `external/src/mekhq` for issue `#46`.
  - `.\gradlew.bat :MekHQ:checkstyleMain` from `external/src/mekhq` for issue `#46`.
  - `.\gradlew.bat :MekHQ:compileJava` from `external/src/mekhq` for issue `#50`.
  - `.\gradlew.bat :MekHQ:checkstyleMain` from `external/src/mekhq` for issue `#50`.
  - `.\gradlew.bat :MekHQ:compileJava` from `external/src/mekhq` for issue `#51`.
  - `.\gradlew.bat :MekHQ:checkstyleMain` from `external/src/mekhq` for issue `#51`.
  - `.\gradlew.bat :MekHQ:compileJava` from `external/src/mekhq` for issue `#53`.
  - `.\gradlew.bat :MekHQ:checkstyleMain` from `external/src/mekhq` for issue `#53`.
  - `.\gradlew.bat :MekHQ:compileJava` from `external/src/mekhq` for issue `#54`.
  - `.\gradlew.bat :MekHQ:checkstyleMain` from `external/src/mekhq` for issue `#54`.
- Source commits:
  - `e19740b110` in `external/src/mekhq`: `Expose command readiness endpoint`.
  - `4429d99ea2` in `external/src/mekhq`: `Add guarded status note command`.
  - `32366b64a0` in `external/src/mekhq`: `Add guarded personnel status command`.
  - `ef6ef99ef9` in `external/src/mekhq`: `Add guarded personnel fatigue command`.
  - `78890ba458` in `external/src/mekhq`: `Add guarded unit market purchase command`.
- Manual checks:
  - Read issue `#45`, the active handoff, `MEKHQ_ADVANCE_DAY_CONTROL_API_PROTOTYPE.md`, and `LocalControlService.java`.
  - Read issue `#46`, `UnitMarketOffer.java`, `LocalCampaignStateExporter.java`, and source-confirmed selector methods before implementing `GET /campaign/commands`.
  - Read issue `#43`, bridge primitive notes, `Campaign#addReport(...)`, `Campaign#addFunds(...)`, `FinancesTab#addFundsActionPerformed()`, `PersonnelMarketDialog#hireActionListener(...)`, `ContractMarketDialog#acceptContract(...)`, and `UnitMarketPane#purchaseSelectedOffers()` before selecting issue `#50`.
  - Read issue `#50`, `LocalControlService.java`, `LocalCommandReadinessExporter.java`, `Campaign#addReport(...)`, and `DailyReportType` before implementing `POST /campaign/command/status-note`.
  - Read issue `#47`, `Person#changeStatus(...)`, `PersonnelStatus`, `Person#setPrisonerStatus(...)`, `PrisonerStatus`, `Unit#remove(...)`, `CampaignEventProcessor`, `ResolveScenarioTracker`, `CapturePrisoners`, and personnel-table status/prisoner UI commands before designing issue `#51`.
  - Read issue `#51`, `LocalControlService.java`, `LocalCommandReadinessExporter.java`, `Person#changeStatus(...)`, `PersonnelStatus`, and command-envelope docs before implementing `POST /campaign/command/personnel/status`.
  - Read issue `#48`, `MedicalController`, `InjuryUtil`, `AdvancedMedicalAlternateHealing`, `AdvancedReplacementLimbDialog`, `PersonnelTableMouseAdapter#replaceLimb(...)`, `Person` injury/fatigue APIs, `Injury`, `InjuryType`, `InjurySubType`, and `ProstheticType` before designing `personnel.fatigue`, `personnel.medical-treatment`, and `personnel.prosthetic-surgery`.
  - Read issue `#53`, `LocalControlService.java`, `LocalCommandReadinessExporter.java`, `Person#changeFatigue(...)`, `Person#getFatigueDirect()`, `Person#getAdjustedFatigue()`, `Person#getPermanentFatigue()`, and issue `#48` medical design before implementing `POST /campaign/command/personnel/fatigue`.
  - Read issue `#49`, `UnitMarketOffer.java`, `AbstractUnitMarket.java`, `UnitMarketPane.java`, `UnitMarketTableModel.java`, `UnitMarketType.java`, `Campaign#addNewUnit(...)`, `LocalCommandReadinessExporter.java`, and `LocalCampaignStateExporter.java` before designing `POST /campaign/command/markets/unit-offers/purchase`.
  - Read issue `#54`, `MEK_RPG_LIVE_MEKHQ_UNIT_MARKET_PURCHASE_COMMAND_DESIGN.md`, `LocalCommandReadinessExporter.java`, `LocalControlService.java`, `UnitMarketPane.java`, `UnitMarketOffer.java`, `AbstractUnitMarket.java`, `UnitMarketType.java`, and `Campaign#addNewUnit(...)` before implementing source-generated live-session unit-market offer selectors and guarded single-offer purchase.
  - Read issue `#52`, `ContractMarketDialog.java`, `AbstractContractMarket.java`, `AtbMonthlyContractMarket.java`, `CamOpsContractMarket.java`, `Mission.java`, `Campaign#addMission(...)`, `Contract.java`, `AtBContract.java`, `ContractAutomation.java`, `FacilityRentals.java`, `FactionStandingGreeting.java`, and `DialogContractStart.java` before designing `POST /campaign/command/contracts/accept`.
- Known blockers:
  - Source push for MekHQ itself remains blocked because `external/src/mekhq` points at upstream `MegaMek/mekhq` and GitHub returned `Permission to MegaMek/mekhq.git denied to walt-raymond-williams` when pushing source commits `ef6ef99ef9` and `78890ba458`.
  - Live status-note smoke testing remains not run; it needs a source-built MekHQ instance launched with `mekhq.controlApi.enabled=true` and a copied/disposable campaign loaded.
  - Live personnel.status smoke testing remains not run; it needs a source-built MekHQ instance launched with `mekhq.controlApi.enabled=true` and a copied/disposable campaign loaded.
  - Live personnel.fatigue smoke testing remains not run; it needs a source-built MekHQ instance launched with `mekhq.controlApi.enabled=true` and a copied/disposable campaign loaded.
  - Live unit-market purchase smoke testing remains not run; it needs a source-built MekHQ instance launched with `mekhq.controlApi.enabled=true` and a copied/disposable campaign with representative unit-market offers, ideally including a DropShip offer.

## Related Docs

- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_PERSONNEL_STATUS_COMMAND_DESIGN.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_MEDICAL_COMMAND_DESIGN.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_UNIT_MARKET_PURCHASE_COMMAND_DESIGN.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_CONTRACT_ACCEPT_COMMAND_DESIGN.md`
- `docs/handoffs/active/implement-live-mekhq-contract-accept-command.md`
- `docs/handoffs/archive/discover-live-mekhq-command-api-easy-wins.md`
- `docs/handoffs/archive/design-live-mekhq-command-envelope.md`
- `docs/handoffs/archive/implement-live-mekhq-command-readiness-selectors.md`
- `docs/handoffs/archive/implement-live-mekhq-status-note-command.md`
- `docs/handoffs/archive/design-live-mekhq-personnel-status-command.md`
- `docs/handoffs/archive/implement-live-mekhq-personnel-status-command.md`
- `docs/handoffs/archive/design-live-mekhq-medical-prosthetic-command.md`
- `docs/handoffs/archive/implement-live-mekhq-personnel-fatigue-command.md`
- `docs/handoffs/archive/design-live-mekhq-unit-market-purchase-command.md`
- `docs/handoffs/archive/implement-live-mekhq-unit-market-purchase-command.md`
- `docs/handoffs/archive/design-live-mekhq-contract-selection-command.md`
