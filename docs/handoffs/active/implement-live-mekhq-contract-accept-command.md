# Agent Handoff

## Issue

- GitHub issue: `#55`
- Roadmap entry: `Implement guarded live MekHQ contract accept command`
- Priority: `High`

## Goal

Implement `POST /campaign/command/contracts/accept` so MEK-RPG can ask the loaded MekHQ campaign to accept one contract-market offer through MekHQ-owned logic, with conservative guards, explicit known prompt choices, and no direct save edits.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/GUARDED_LIVE_MEKHQ_COMMAND_API_TRACKING.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_CONTRACT_ACCEPT_COMMAND_DESIGN.md`
- `docs/handoffs/active/guarded-live-mekhq-command-api-epic.md`

## Expected Output

- `POST /campaign/command/contracts/accept` using the shared guarded command envelope.
- `GET /campaign/commands` readiness and selector metadata for contract accept candidates.
- Source-owned dry-run and apply paths for one current market offer selected by contract id plus guard facts.
- Explicit request policies for known UI choices such as contract confirmation, informational acknowledgements, travel/mothballing, and rentals.
- Prompt/choice preflight refusal before mutation for unknown or unsupported prompt paths.
- Response facts for finance credits, mission insertion, market removal, reports, prompts, saves, and audit context.
- Top-level exception handling so failed API calls return structured failures and do not crash MekHQ or stop the local control API.

## Files And Areas

Likely source files:

- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCommandReadinessExporter.java`
- possible new helper under `external/src/mekhq/MekHQ/src/mekhq/service/`
- `external/src/mekhq/MekHQ/src/mekhq/gui/dialog/ContractMarketDialog.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/market/contractMarket/AbstractContractMarket.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/market/contractMarket/ContractAutomation.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/mission/Contract.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/mission/AtBContract.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/mission/rentals/FacilityRentals.java`

Likely workspace docs to update:

- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/GUARDED_LIVE_MEKHQ_COMMAND_API_TRACKING.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`

## Commands

Useful source search:

```powershell
rg -n "contracts.accept|ContractMarketDialog|acceptContract|contractStartPrompt|offerContractRentalOpportunity|FactionStandingGreeting|DialogContractStart" MekHQ/src/mekhq
```

Expected checks after source edits:

```powershell
.\gradlew.bat :MekHQ:compileJava
.\gradlew.bat :MekHQ:checkstyleMain
```

## Constraints

- Do not bypass MekHQ contract business logic. The implementation should share or extract the source-owned acceptance workflow behind `ContractMarketDialog#acceptContract(...)`, not reimplement acceptance in MEK-RPG or raw save edits.
- Do not call the current Swing handler blindly if it can block on an unanswered modal dialog after partial mutation; make known prompt choices explicit first.
- Do not accept by display name, table row, or inferred market position.
- Do not change MRBC fee, advance percentage, signing bonus, shares, negotiation terms, retainers, travel, or rentals in V1.
- Do not auto-answer arbitrary Swing prompts in V1.
- Known prompts may be answered or acknowledged only when the request supplies an explicit supported policy, such as accepting a known contract challenge, acknowledging known informational prompts, declining travel automation, declining automated mothballing, or declining rentals.
- Refuse before mutation when an unknown prompt appears or when a required prompt choice is unsupported or missing.
- Catch validation/source/runtime exceptions and return `refused`, `blocked`, or `failed` responses instead of letting exceptions escape the HTTP handler or crash MekHQ.
- After a failed/refused API call, the local server must still answer `/status` or `GET /campaign/commands`.
- Keep `saveAfterSuccess` explicit and opt-in.
- Dry-run must not create finance transactions, missions, reports, market removals, travel state, rentals, or saves.

## Acceptance Criteria

- The command validates campaign id/name/date, state revision, contract id, expected terms, expected current campaign facts, `dryRun`, supported prompt/choice policies, idempotency key, save policy, and audit context.
- `GET /campaign/commands` marks `contracts.accept` available only when at least one contract offer is selectable with the implementation's supported explicit choice policies.
- Dry-run returns intended side effects without mutation.
- Apply mode credits advance and transport funds, calls `Campaign#addMission(...)`, invokes `Contract#acceptContract(...)`, removes the market offer, optionally appends a `GENERAL` MEK-RPG audit report, and returns the new mission id.
- Refusal and failure responses include machine-readable reason codes from the design note.
- Negative smoke tests send at least one bad/stale/unsupported request and then verify the local control server still responds.
- Compile and checkstyle pass, or exact blockers are recorded in docs.

## Open Questions

- Should the implementation use a new service class for contract acceptance to keep `LocalControlService` as endpoint glue and make the shared UI/API workflow easier to test?
- Should high-value contract acceptance require a matching dry-run in a later version?
- What live disposable campaign should be used for smoke testing once a source-built MekHQ instance is available?
