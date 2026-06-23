# Agent Handoff

## Issue

- GitHub issue: `#55`
- Roadmap entry: `Implement guarded live MekHQ contract accept command`
- Priority: `High`

## Goal

Implement `POST /campaign/command/contracts/accept` so MEK-RPG can ask the loaded MekHQ campaign to accept one prompt-free contract-market offer through MekHQ-owned logic, with conservative guards and no direct save edits.

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
- Prompt preflight refusal before mutation for every known prompt-required path.
- Response facts for finance credits, mission insertion, market removal, reports, prompts, saves, and audit context.

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

- Do not call `ContractMarketDialog#acceptContract(...)` directly; it can mutate before later prompts.
- Do not accept by display name, table row, or inferred market position.
- Do not change MRBC fee, advance percentage, signing bonus, shares, negotiation terms, retainers, travel, or rentals in V1.
- Do not auto-answer or suppress Swing prompts in V1.
- Refuse before mutation when confirmation, faction-standing, StratCon start, travel/mothball, transit, or rental prompts would be required.
- Keep `saveAfterSuccess` explicit and opt-in.
- Dry-run must not create finance transactions, missions, reports, market removals, travel state, rentals, or saves.

## Acceptance Criteria

- The command validates campaign id/name/date, state revision, contract id, expected terms, expected current campaign facts, `dryRun`, `promptPolicy=refuse_if_prompt`, idempotency key, save policy, and audit context.
- `GET /campaign/commands` marks `contracts.accept` available only when at least one prompt-free contract offer is selectable.
- Dry-run returns intended side effects without mutation.
- Apply mode credits advance and transport funds, calls `Campaign#addMission(...)`, invokes `Contract#acceptContract(...)`, removes the market offer, optionally appends a `GENERAL` MEK-RPG audit report, and returns the new mission id.
- Refusal responses include machine-readable reason codes from the design note.
- Compile and checkstyle pass, or exact blockers are recorded in docs.

## Open Questions

- Should the implementation use a new service class for contract acceptance to avoid adding more command-specific logic to `LocalControlService`?
- Should high-value contract acceptance require a matching dry-run in a later version?
- What live disposable campaign should be used for smoke testing once a source-built MekHQ instance is available?
