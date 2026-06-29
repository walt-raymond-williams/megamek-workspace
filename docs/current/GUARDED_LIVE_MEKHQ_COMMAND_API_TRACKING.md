# Guarded Live MekHQ Command API Tracking

## Purpose

GitHub Issues are the execution source of truth. This file is the compact local recovery snapshot for epic `#44`, branch state, issue state, next management step, and handoff paths.

## Integration Branch

- Branch: `codex/guarded-live-mekhq-command-api`
- Base: `master`
- Human review required before merge: `Yes`

## Issue Snapshot

- Last refreshed: `2026-06-29`
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
  - `#55`: Implement guarded live MekHQ contract accept command.
  - `#71`: Audit MekHQ pilot assignment and TO&E source owners.
  - `#72`: Design guarded pilot assignment and TO&E command API.
  - `#73`: Expose pilot assignment and TO&E read selectors.
- Open:
  - `#44`: Epic: Guarded live MekHQ command API for MEK-RPG.
  - `#70`: Epic: Add guarded TO&E and pilot assignment commands for MEK-RPG.
  - `#74`: Implement guarded MekHQ pilot assignment commands.
  - `#75`: Implement guarded MekHQ TO&E force commands.
  - `#76`: Design atomic TO&E and pilot assignment batch command.
  - `#77`: Add pilot assignment and TO&E command fixtures and smoke checklist.
  - `#79`: Smoke test contract accept prompts and MekHQ UI refresh.
- Blocked: none yet for this epic.

## Recommended Next Step

- Issue: `#79`
- Why next: Contract accept is already implemented, but live disposable-campaign smoke has not proven the prompt-choice chain or visible MekHQ UI refresh behavior. This should be checked before relying on API-driven contract acceptance during play. Issue `#74` remains the next implementation slice after this verification pass.
- Handoff: `docs/handoffs/active/smoke-test-contract-accept-ui-refresh.md`

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
  - `.\gradlew.bat :MekHQ:compileJava` from `external/src/mekhq` for issue `#55`.
  - `.\gradlew.bat :MekHQ:checkstyleMain` from `external/src/mekhq` for issue `#55`.
  - `.\gradlew.bat :MekHQ:test --tests mekhq.service.LocalCommandReadinessExporterTest --tests mekhq.service.LocalControlServiceHttpTest` from `external/src/mekhq` after adding local control API regression tests.
  - `.\gradlew.bat :MekHQ:test` from `external/src/mekhq` after adding local control API readiness regression tests.
  - `.\gradlew.bat --no-daemon :MekHQ:test --tests mekhq.service.LocalCampaignStateExporterTest --tests mekhq.service.LocalCommandReadinessExporterTest` from `external/src/mekhq` for issue `#73`.
  - `.\gradlew.bat --no-daemon :MekHQ:compileJava :MekHQ:checkstyleMain :MekHQ:checkstyleTest` from `external/src/mekhq` for issue `#73`.
- Source commits:
  - `e19740b110` in `external/src/mekhq`: `Expose command readiness endpoint`.
  - `4429d99ea2` in `external/src/mekhq`: `Add guarded status note command`.
  - `32366b64a0` in `external/src/mekhq`: `Add guarded personnel status command`.
  - `ef6ef99ef9` in `external/src/mekhq`: `Add guarded personnel fatigue command`.
  - `78890ba458` in `external/src/mekhq`: `Add guarded unit market purchase command`.
  - `0451eb53d4` in `external/src/mekhq`: `Add guarded contract accept command`.
  - `51dbfbe645` in `external/src/mekhq`: `Add local control API readiness tests`.
  - `53741cd082` in `external/src/mekhq`: `Expose pilot TOE read selectors`.
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
  - Read issue `#55`, `MEK_RPG_LIVE_MEKHQ_CONTRACT_ACCEPT_COMMAND_DESIGN.md`, `LocalControlService.java`, `LocalCommandReadinessExporter.java`, `ContractMarketDialog.java`, `AbstractContractMarket.java`, `ContractAutomation.java`, `FacilityRentals.java`, `Contract.java`, and `AtBContract.java` before implementing guarded contract accept selectors and endpoint.
  - Read issue `#71`, MEK-RPG TO&E/pilot handoff, `Unit.java`, `AssignPersonToUnitMenu.java`, `AssignUnitToPersonMenu.java`, `Campaign.java`, `Formation.java`, `TOETransferHandler.java`, `AssignUnitToForceMenu.java`, `TOEMouseAdapter.java`, `StaticChecks.java`, and related personnel role/source methods before recommending the issue `#72` design path.
  - Read issue `#72`, command envelope docs, issue `#71` audit, MEK-RPG TO&E/pilot handoff, and next implementation handoffs before adding design note `MEK_RPG_LIVE_MEKHQ_PILOT_TOE_COMMAND_DESIGN.md`.
  - Read issue `#73`, issue `#72` design, `LocalCampaignStateExporter.java`, `LocalCommandReadinessExporter.java`, `Unit.java`, `Person.java`, `Formation.java`, and local service tests before exposing pilot assignment and TO&E read selectors.
  - Created issue `#79` after reviewing `MEK_RPG_LIVE_MEKHQ_CONTRACT_ACCEPT_COMMAND_DESIGN.md`, `MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`, `MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`, `LocalControlService.java`, `LocalCommandReadinessExporter.java`, and `LocalContractMarketSelectors.java`; the gap is live proof of prompt handling and visible MekHQ UI refresh after the non-dialog API path.
- Known blockers:
  - Source push for MekHQ itself remains blocked because `external/src/mekhq` points at upstream `MegaMek/mekhq` and GitHub returned `Permission to MegaMek/mekhq.git denied to walt-raymond-williams` when pushing source commit `53741cd082`.
  - Live status-note smoke testing remains not run; it needs a source-built MekHQ instance launched with `mekhq.controlApi.enabled=true` and a copied/disposable campaign loaded.
  - Live personnel.status smoke testing remains not run; it needs a source-built MekHQ instance launched with `mekhq.controlApi.enabled=true` and a copied/disposable campaign loaded.
  - Live personnel.fatigue smoke testing remains not run; it needs a source-built MekHQ instance launched with `mekhq.controlApi.enabled=true` and a copied/disposable campaign loaded.
  - Live unit-market purchase smoke testing remains not run; it needs a source-built MekHQ instance launched with `mekhq.controlApi.enabled=true` and a copied/disposable campaign with representative unit-market offers, ideally including a DropShip offer.
  - Live contract accept smoke testing remains not run; issue `#79` now tracks this explicitly. It needs a source-built MekHQ instance launched with `mekhq.controlApi.enabled=true` and a copied/disposable campaign with at least one selectable contract-market offer. The smoke must also record whether the visible MekHQ contract market, briefing/active contract view, reports, and finances refresh automatically or need a source follow-up.
  - Pilot assignment and TO&E read selectors are available in source commit `53741cd082`, but mutating implementation remains blocked on shared/extracted validation. Role eligibility currently lives mostly in Swing menus, while model methods trust callers after basic location/registration checks.

## Related Docs

- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_PERSONNEL_STATUS_COMMAND_DESIGN.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_MEDICAL_COMMAND_DESIGN.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_UNIT_MARKET_PURCHASE_COMMAND_DESIGN.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_CONTRACT_ACCEPT_COMMAND_DESIGN.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_PILOT_TOE_SOURCE_AUDIT.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_PILOT_TOE_COMMAND_DESIGN.md`
- `docs/handoffs/active/toe-pilot-assignment-command-api-epic.md`
- `docs/handoffs/active/implement-guarded-pilot-assignment-commands.md`
- `docs/handoffs/active/implement-guarded-toe-force-commands.md`
- `docs/handoffs/active/design-toe-pilot-batch-command.md`
- `docs/handoffs/active/add-pilot-toe-command-fixtures-smoke.md`
- `docs/handoffs/active/smoke-test-contract-accept-ui-refresh.md`
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
- `docs/handoffs/archive/implement-live-mekhq-contract-accept-command.md`
- `docs/handoffs/archive/audit-mekhq-pilot-toe-source-owners.md`
- `docs/handoffs/archive/design-guarded-pilot-toe-command-api.md`
- `docs/handoffs/archive/implement-pilot-toe-read-selectors.md`
