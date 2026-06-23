# MEK-RPG Live MekHQ Contract Accept Command Design

Status: source design for GitHub issue `#52`, completed `2026-06-23`.

Purpose: define how MEK-RPG should ask the loaded MekHQ campaign to accept one selected contract-market offer without editing saves, selecting UI rows, or auto-answering Swing prompts.

## Recommendation

`Decision`: implement a narrow guarded endpoint:

```http
POST /campaign/command/contracts/accept
```

The command should accept exactly one current `Campaign#getContractMarket().getContracts()` offer, selected by the offer's source `Mission#getId()` value and protected by full guard fields. MekHQ should own the mutation. MEK-RPG should never accept by display name, table row, or raw save edit.

`Decision`: V1 should support only `promptPolicy=refuse_if_prompt`. It should refuse before mutation whenever the equivalent `ContractMarketDialog#acceptContract(...)` path would show any confirmation, faction-standing, StratCon start, travel/mothball, transit, or rental dialog.

`Decision`: V1 is safe enough to implement as a source command only if it extracts a source-owned non-dialog acceptance service or command helper. The helper may reuse the non-dialog side effects from `ContractMarketDialog#acceptContract(...)`, but it must not call the dialog method directly because that method can mutate the campaign before later dialogs are shown.

## Source Findings

`Confirmed from source`: `AbstractContractMarket` owns market offers in `contracts`, assigns market offer ids with `lastId++`, stores them in `contractIds`, serializes `lastId`, and writes each offer through `Contract#writeToXML(...)`. Source files: `AbstractContractMarket.java`, `AtbMonthlyContractMarket.java`, and `CamOpsContractMarket.java`.

`Confirmed from source`: `Mission#writeToXMLBegin(...)` writes the mission id both as a `mission` attribute and an `id` child. `Mission.generateInstanceFromXML(...)` restores the id from the child node. A saved contract-market offer id is therefore stable across save/reload while the offer remains in the market.

`Confirmed from source`: accepting an offer changes the id that MEK-RPG should use after acceptance. `Campaign#addMission(Mission)` assigns `lastMissionId + 1` and calls `mission.setId(...)`, so the market offer id is an acceptance selector, not the final active mission id. The response must return both `acceptedMarketContractId` and `newMissionId`.

`Confirmed from source`: `ContractMarketDialog#acceptContract(...)` performs these source-owned side effects:

- for `AtBContract`, creates an employer liaison before any acceptance confirmation and creates a clan opponent when needed
- optionally asks a contract challenge confirmation through `triggerConfirmationDialog()`
- overwrites the contract name from `ContractSummaryPanel`
- credits advance funds and transport reimbursement as `TransactionType.CONTRACT_PAYMENT`
- calls `campaign.addMission(selectedContract)`
- calls `selectedContract.acceptContract(campaign)` after mission insertion
- for `AtBContract`, `AtBContract#acceptContract(...)` initializes StratCon campaign state when StratCon is enabled
- optionally processes faction-standing contract-acceptance reports
- opens faction-standing or StratCon start informational dialogs
- calls `ContractAutomation.contractStartPrompt(...)`, which can ask about mothballing and travel and can start a jump transaction
- calls `FacilityRentals.offerContractRentalOpportunity(...)`, which can open rental and rental-confirmation dialogs and mutate rented facility counts
- removes the offer from the contract market

`Confirmed from source`: `ContractMarketDialog#acceptContract(...)` is unsafe as a command entry point because some prompts happen after finances, mission insertion, and contract acceptance. A command must pre-detect prompt-required cases before applying the first mutation.

## Selector Policy

Use `contract_id` from `GET /campaign/commands` selectors as the primary selector only for current market offers.

Required selector and guard fields:

- `contractId`: integer `Mission#getId()` from `selectors.contract_market_offers[]`
- `expectedStateRevision`: readiness state revision from `GET /campaign/commands`
- `expectedName`
- `expectedStatus`
- `expectedEmployer`
- `expectedEnemyCode` and `expectedEnemyName` for `AtBContract`
- `expectedContractType`
- `expectedStartDate`
- `expectedEndDate`
- `expectedSystemId` and `expectedSystemName`
- `expectedLengthMonths`
- `expectedAdvanceAmount`
- `expectedTransportAmount`
- `expectedMonthlyPayout`
- `expectedTotalAmount`
- `expectedEstimatedTotalProfit`
- `expectedTransportComp`
- `expectedSalvagePct`
- `expectedStraightSupport`
- `expectedBattleLossComp`
- `expectedCommandRights`
- `expectedCurrentCampaignDate`
- `expectedCurrentSystemId`
- `expectedCurrentLocation`
- `expectedCampaignBalance`
- `expectedMarketOfferCount`
- `expectedActiveMissionCount`

Recommended optional guards:

- `expectedAdvancePct`
- `expectedSigningBonusPct`
- `expectedMrbcFee`
- `expectedSharePct` when the share system is enabled
- `expectedFactionStandingEnabled`
- `expectedUseStratCon`
- `expectedRentalCosts`
- `expectedTravelDays`

Refuse if:

- no campaign is loaded
- `contractId` is not in `campaign.getContractMarket().getContracts()`
- more than one market offer with the same id is found
- the selected object is already in `campaign.getMissions()`
- any guard field differs
- the readiness `state_revision` differs
- the current contract market is disabled
- the contract has null or invalid destination system/date facts needed for guard checks

## Prompt Refusal Rules

V1 should refuse before mutation with machine-readable reasons:

- `contract_confirmation_prompt_required`: `AtBContract` challenge confirmation would show for garrison, retainer, guerrilla, unknown difficulty, very easy, hard, or very hard contracts.
- `faction_standing_prompt_required`: `campaign.getCampaignOptions().isTrackFactionStanding()` is true and the selected contract path would instantiate `FactionStandingGreeting`.
- `stratcon_start_prompt_required`: faction standing is off, the contract is `AtBContract`, and StratCon is enabled, which would instantiate `DialogContractStart`.
- `contract_travel_prompt_required`: the campaign is not already in the target system, which would make `ContractAutomation.contractStartPrompt(...)` show mothball/transit dialogs.
- `contract_rental_prompt_required`: hospital, kitchen, or holding-cell rental costs are nonzero, which would call `FacilityRentals.offerContractRentalOpportunity(...)`.
- `visible_dialogs_present`: any visible dialog is already present when the command begins.
- `unsupported_prompt_policy`: request uses anything other than `refuse_if_prompt` in V1.

V1 should not auto-answer or suppress these prompts. Later versions may add endpoint-specific policies only after source code exposes non-dialog services for the relevant choice. Examples: an explicit travel policy that sets a jump path and debits transport through `TransportCostCalculations.performJumpTransaction(...)`, or a rental policy that supplies exact rental counts and confirmation guards.

## Request Contract

```json
{
  "command": "contracts.accept",
  "commandVersion": 1,
  "idempotencyKey": "mek-rpg-contract-accept-30250408-001",
  "expectedCampaignId": "<campaign uuid>",
  "expectedCampaignName": "The Learning Ropes",
  "expectedDate": "3025-04-08",
  "expectedStateRevision": "<from GET /campaign/commands>",
  "dryRun": true,
  "saveAfterSuccess": false,
  "promptPolicy": "refuse_if_prompt",
  "contractId": 3,
  "contractGuards": {
    "expectedName": "Cadre Duty for Federated Suns",
    "expectedStatus": "ACTIVE",
    "expectedEmployer": "Federated Suns",
    "expectedEnemyCode": "DC",
    "expectedEnemyName": "Draconis Combine",
    "expectedContractType": "CADRE_DUTY",
    "expectedStartDate": "3025-05-01",
    "expectedEndDate": "3025-11-01",
    "expectedSystemId": "New Avalon",
    "expectedSystemName": "New Avalon",
    "expectedLengthMonths": 6,
    "expectedAdvanceAmount": "1000000",
    "expectedTransportAmount": "0",
    "expectedMonthlyPayout": "250000",
    "expectedTotalAmount": "2500000",
    "expectedEstimatedTotalProfit": "750000",
    "expectedTransportComp": 50,
    "expectedSalvagePct": 50,
    "expectedStraightSupport": 50,
    "expectedBattleLossComp": 50,
    "expectedCommandRights": "LIAISON",
    "expectedTravelDays": 0
  },
  "campaignGuards": {
    "expectedCurrentSystemId": "<current system id>",
    "expectedCampaignBalance": "5000000",
    "expectedMarketOfferCount": 1,
    "expectedActiveMissionCount": 0
  },
  "acceptanceOptions": {
    "contractName": "Cadre Duty for Federated Suns",
    "appendAuditReport": true
  },
  "clientContext": {
    "actor": "MEK-RPG",
    "sceneId": "contract-selection",
    "actionId": "accept-cadre-duty",
    "reason": "Player selected this contract in MEK-RPG"
  }
}
```

`contractName` should default to the current contract name. If supplied, it must be plain text, short, and included in guard validation. V1 should not expose MRBC fee, advance percentage, signing bonus, or share percentage mutation in the accept endpoint; those are contract negotiation/terms commands.

## Response Contract

Dry-run response:

```json
{
  "status": "dry_run",
  "statusReason": "would_accept_contract",
  "command": "contracts.accept",
  "commandVersion": 1,
  "idempotencyKey": "mek-rpg-contract-accept-30250408-001",
  "before": {
    "campaignDate": "3025-04-08",
    "balance": "5000000",
    "marketOfferCount": 1,
    "activeMissionCount": 0,
    "contract": {}
  },
  "afterPreview": {
    "acceptedMarketContractId": 3,
    "newMissionId": "assigned_on_apply",
    "advanceCredit": "1000000",
    "transportCredit": "0",
    "marketOfferRemoved": true,
    "stratconStateWouldInitialize": false,
    "auditReportWouldAppend": true,
    "saveWouldRun": false
  },
  "promptFacts": {
    "policy": "refuse_if_prompt",
    "promptsDetected": [],
    "visibleDialogsAtStart": 0
  },
  "warnings": [],
  "unsupported": []
}
```

Apply response should use `status=applied` and include:

- `acceptedMarketContractId`
- `newMissionId`
- before/after balance
- created finance transaction summaries or at least transaction count delta and amounts
- before/after market offer count
- before/after active mission count
- contract before/after facts
- whether StratCon state was initialized
- report count deltas, including politics and audit reports
- prompt facts
- save facts
- echoed sanitized client audit context

Refusal response should use `status=refused` and return the first hard blocker in `statusReason`, plus an array of all prompt blockers found where practical.

## Readiness Updates

`GET /campaign/commands` should keep `contracts.accept` blocked until implementation. The implementation issue should update it to:

- `available` only when at least one contract offer is selectable under `promptPolicy=refuse_if_prompt`
- `blocked` with `no_selectable_contract_offers` when offers exist but all would require prompts or stale/invalid facts
- selector scope: `saved_market_contract_id_current_state_revision`
- required selectors: `contract_id`, `expected_state_revision`, `expected_name`, `expected_employer`, `expected_enemy`, `expected_dates`, `expected_location`, `expected_payment_terms`, `expected_transport_terms`, `expected_campaign_balance`
- metadata refusal reasons matching the prompt and guard codes above

Readiness contract selectors should add enough guard facts that MEK-RPG can render a contract picker and echo the exact source facts without recomputing terms client-side.

## MEK-RPG Integration Guidance

MEK-RPG should consume this command in three steps:

1. Fetch `GET /campaign/commands` and show only `selectors.contract_market_offers[]` candidates that MekHQ marks selectable for `contracts.accept`.
2. When the player chooses a contract, send `POST /campaign/command/contracts/accept` with the selector, state revision, and all guard fields copied from MekHQ readiness/state output. Use `dryRun=true` first for UI confirmation.
3. Apply with the same guard fields, `dryRun=false`, a fresh idempotency key for the final action, `promptPolicy=refuse_if_prompt`, and `saveAfterSuccess=false` unless the user explicitly requested a save.

If MekHQ refuses with a prompt-required reason, MEK-RPG should tell the user the contract must be accepted manually in MekHQ or wait for a later command version that supports the needed policy. MEK-RPG should not try to simulate acceptance with separate funds, mission, travel, or report edits.

## Follow-Up Implementation Scope

Create a follow-up issue to implement `POST /campaign/command/contracts/accept` for one prompt-free contract-market offer. Scope:

- source-generated readiness metadata and selector guard facts
- guarded dry-run and apply endpoint using the shared command envelope
- process-local idempotency
- prompt preflight refusal before mutation
- source-owned acceptance helper that applies advance/transport credits, mission insertion, `Contract#acceptContract(...)`, market removal, optional audit report, and opt-in save
- compile and checkstyle verification

Out of scope for V1:

- decline/reject contract
- negotiation, MRBC fee toggles, advance/signing/share mutation
- retainer start/end commands
- travel automation
- automated mothballing
- rental selection
- faction-standing dialog responses
- accepting contracts that require challenge confirmation
- market refresh/generation
