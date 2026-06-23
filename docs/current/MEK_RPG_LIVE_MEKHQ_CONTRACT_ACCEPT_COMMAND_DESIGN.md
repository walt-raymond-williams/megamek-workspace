# MEK-RPG Live MekHQ Contract Accept Command Design

Status: source design for GitHub issue `#52`, completed `2026-06-23`.

Purpose: define how MEK-RPG should ask the loaded MekHQ campaign to accept one selected contract-market offer without editing saves or selecting UI rows, while still letting MekHQ own the contract acceptance business logic.

## Recommendation

`Decision`: implement a narrow guarded endpoint:

```http
POST /campaign/command/contracts/accept
```

The command should accept exactly one current `Campaign#getContractMarket().getContracts()` offer, selected by the offer's source `Mission#getId()` value and protected by full guard fields. MekHQ should own the mutation and should run the same business logic that the UI button ultimately relies on: finance credits, mission insertion, contract acceptance, StratCon initialization, reports, and market removal. MEK-RPG should never accept by display name, table row, or raw save edit.

`Decision`: V1 should not be limited to "prompt-free only" if the prompt is a known branch that can be represented explicitly in the request. The API should model known interactive choices as first-class policy fields. It should acknowledge or decline known informational/optional UI branches deliberately, and refuse only unknown or unsupported prompt branches.

`Decision`: V1 is safe enough to implement as a source command only if it extracts a source-owned non-dialog acceptance service or command helper, or otherwise refactors `ContractMarketDialog#acceptContract(...)` so the UI handler and API command share the same underlying acceptance workflow. The goal is not to reimplement contract acceptance outside MekHQ. The goal is to let MekHQ process the existing workflow while replacing ad hoc Swing prompts with explicit API-supplied choices.

`Decision`: API command failures must not crash MekHQ or stop the local control server. The endpoint should catch validation errors, source exceptions, and unexpected runtime failures; return a structured `failed` or `refused` response; log enough detail for debugging; clear any in-progress command/idempotency state; and leave the HTTP service available for later calls.

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

`Confirmed from source`: `ContractMarketDialog#acceptContract(...)` is risky as a direct API entry point because some prompts happen after finances, mission insertion, and contract acceptance. That does not mean those mutations are wrong; they are the desired MekHQ-owned effects. It means the API must make the interactive branch choices explicit before running the workflow, or refactor the workflow so each known prompt decision is supplied by request policy instead of blocking on Swing.

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

## Prompt And Choice Policy

V1 should model known UI branches explicitly instead of treating every prompt as a hard blocker. Recommended request policy fields:

- `confirmationPolicy`: `accept_known_contract_challenge` or `refuse_if_confirmation_required`.
- `informationalPromptPolicy`: `acknowledge_known_informational` or `refuse_if_prompt`.
- `travelPolicy`: `decline_travel_automation`, `start_travel_if_guarded`, or `refuse_if_travel_prompt`.
- `mothballPolicy`: `decline_mothballing`, `accept_automated_mothballing_if_guarded`, or `refuse_if_mothball_prompt`.
- `rentalPolicy`: `decline_all_rentals`, `specified_rental_counts`, or `refuse_if_rental_prompt`.
- `unknownPromptPolicy`: must be `refuse`.

V1 may start with a small allowed set:

- accept known contract challenge confirmations only when `confirmationPolicy=accept_known_contract_challenge`
- acknowledge faction-standing and StratCon start informational dialogs only when `informationalPromptPolicy=acknowledge_known_informational`
- decline optional travel automation, automated mothballing, and rentals by default when the request says so explicitly
- refuse all unknown, new, or unmodeled prompts

V1 should refuse before mutation with machine-readable reasons when a required choice is absent or unsupported:

- `contract_confirmation_choice_required`: `AtBContract` challenge confirmation would show and `confirmationPolicy` does not authorize accepting it.
- `faction_standing_ack_required`: faction-standing greeting would show and `informationalPromptPolicy` does not authorize acknowledging it.
- `stratcon_start_ack_required`: StratCon start briefing would show and `informationalPromptPolicy` does not authorize acknowledging it.
- `contract_travel_choice_required`: travel/mothball/transit prompt would show and `travelPolicy`/`mothballPolicy` do not provide a supported choice.
- `contract_rental_choice_required`: rental prompt would show and `rentalPolicy` does not provide a supported choice.
- `visible_dialogs_present`: any visible dialog is already present when the command begins.
- `unsupported_prompt_policy`: request asks for a prompt choice the implementation has not modeled.
- `unknown_prompt_required`: source detects a prompt category outside the known allowlist.

V1 should not auto-answer arbitrary Swing prompts. If it answers or bypasses a known prompt, that answer must be visible in the request, reported in the response, and implemented through source-owned logic rather than window scraping. Examples: an explicit travel policy that sets a jump path and debits transport through `TransportCostCalculations.performJumpTransaction(...)`, or a rental policy that supplies exact rental counts and confirmation guards.

## Failure Isolation

`Decision`: the local control server should be resilient around this and every future mutating command.

Implementation requirements:

- Wrap command execution in a top-level try/catch inside the local control endpoint.
- Convert validation failures to `refused` or `blocked`, not thrown exceptions.
- Convert unexpected source exceptions to `failed` with `statusReason=source_exception` or a narrower code.
- Log the exception server-side with command name, idempotency key, and sanitized target facts.
- Do not call `System.exit`, dispose the main frame, or shut down the HTTP server from a command failure path.
- Clear any per-command running flag in a `finally` block so one failed call does not permanently block later commands.
- Do not save after failed, refused, blocked, or dry-run responses.
- Include smoke tests that deliberately send bad/stale requests and then immediately call `/status` or `GET /campaign/commands` to prove the server remains alive.

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
  "promptPolicy": "explicit_known_choices",
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
    "appendAuditReport": true,
    "confirmationPolicy": "accept_known_contract_challenge",
    "informationalPromptPolicy": "acknowledge_known_informational",
    "travelPolicy": "decline_travel_automation",
    "mothballPolicy": "decline_mothballing",
    "rentalPolicy": "decline_all_rentals",
    "unknownPromptPolicy": "refuse"
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
    "policy": "explicit_known_choices",
    "promptsDetected": ["contract_challenge_confirmation"],
    "choicesApplied": {
      "confirmationPolicy": "accept_known_contract_challenge",
      "travelPolicy": "decline_travel_automation",
      "rentalPolicy": "decline_all_rentals"
    },
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

Refusal response should use `status=refused` and return the first hard blocker in `statusReason`, plus an array of all prompt or choice blockers found where practical. Unexpected exceptions should use `status=failed`, keep the server running, and report `serverStillAvailable=true` when the endpoint can confirm it.

## Readiness Updates

`GET /campaign/commands` should keep `contracts.accept` blocked until implementation. The implementation issue should update it to:

- `available` when at least one contract offer can be accepted with the implementation's supported explicit choice policies
- `blocked` with `no_selectable_contract_offers` when offers exist but all require unsupported choices or stale/invalid facts
- selector scope: `saved_market_contract_id_current_state_revision`
- required selectors: `contract_id`, `expected_state_revision`, `expected_name`, `expected_employer`, `expected_enemy`, `expected_dates`, `expected_location`, `expected_payment_terms`, `expected_transport_terms`, `expected_campaign_balance`
- metadata listing supported choice policies and refusal reasons matching the prompt/choice and guard codes above

Readiness contract selectors should add enough guard facts that MEK-RPG can render a contract picker and echo the exact source facts without recomputing terms client-side.

## MEK-RPG Integration Guidance

MEK-RPG should consume this command in three steps:

1. Fetch `GET /campaign/commands` and show `selectors.contract_market_offers[]` candidates with their supported acceptance choices.
2. When the player chooses a contract, send `POST /campaign/command/contracts/accept` with the selector, state revision, all guard fields copied from MekHQ readiness/state output, and explicit policies for known prompts. Use `dryRun=true` first for UI confirmation.
3. Apply with the same guard fields, `dryRun=false`, a fresh idempotency key for the final action, explicit known-choice policies, and `saveAfterSuccess=false` unless the user explicitly requested a save.

If MekHQ refuses with an unsupported-choice reason, MEK-RPG should tell the user the contract needs manual acceptance in MekHQ or a later command version that supports the missing choice. MEK-RPG should not try to simulate acceptance with separate funds, mission, travel, or report edits.

## Follow-Up Implementation Scope

Create a follow-up issue to implement `POST /campaign/command/contracts/accept` for one contract-market offer using MekHQ-owned acceptance logic and explicit known prompt choices. Scope:

- source-generated readiness metadata and selector guard facts
- guarded dry-run and apply endpoint using the shared command envelope
- process-local idempotency
- prompt/choice preflight before mutation
- source-owned acceptance helper that applies advance/transport credits, mission insertion, `Contract#acceptContract(...)`, market removal, optional audit report, and opt-in save
- top-level command exception handling that returns structured failures without crashing MekHQ or stopping the local control API
- negative smoke test proving a failed API request leaves `/status` or `GET /campaign/commands` usable
- compile and checkstyle verification

Out of scope for V1:

- decline/reject contract
- negotiation, MRBC fee toggles, advance/signing/share mutation
- retainer start/end commands
- unsupported travel automation choices
- unsupported automated mothballing choices
- unsupported rental selection choices
- arbitrary faction-standing dialog responses beyond known acknowledgement
- accepting contracts that require unknown or unmodeled prompts
- market refresh/generation
