# Agent Handoff

## Issue

- GitHub issue: `#52`
- Roadmap entry: `Design live MekHQ contract selection command API`
- Priority: `High`

## Goal

Design a guarded contract-market selection command so MEK-RPG can ask MekHQ to accept a specific available contract from the loaded campaign, with MekHQ owning validation, side effects, reports, finances, contract state, prompt refusal, and save behavior.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/GUARDED_LIVE_MEKHQ_COMMAND_API_TRACKING.md`
- `docs/handoffs/active/guarded-live-mekhq-command-api-epic.md`

## Expected Output

- Source-backed design note for `POST /campaign/command/contracts/accept` or a better source-confirmed endpoint name.
- API request/response contract suitable for MEK-RPG integration work.
- Contract selector and guard-field policy, including expected contract id/name, employer, enemy, dates, location, payment, transport, salvage, and current campaign date.
- Prompt/dialog refusal rules for confirmation, faction standing, rental, travel, or other contract acceptance prompts.
- Readiness/selector update requirements for `GET /campaign/commands`.
- Draft memo or memo-ready section for the MEK-RPG team explaining how to consume the command once implemented.
- Narrowed follow-up implementation issue if source review shows the command is safe enough.

## Files And Areas

Likely files to read or edit:

- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`
- `docs/current/GUARDED_LIVE_MEKHQ_COMMAND_API_TRACKING.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `external/src/mekhq/MekHQ/src/mekhq/gui/dialog/ContractMarketDialog.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/market/contractMarket/AbstractContractMarket.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/mission/Contract.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCommandReadinessExporter.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "acceptContract|addMission|getContracts|contractMarket|ContractMarketDialog|accept contract" external/src/mekhq/MekHQ/src
rg -n "contracts.accept|contract acceptance|contract-market|Contract Market Decision" docs/current docs/handoffs
```

If source changes are made in a follow-up implementation issue, expected checks are likely:

```powershell
.\gradlew.bat :MekHQ:compileJava
.\gradlew.bat :MekHQ:checkstyleMain
```

## Constraints

- Do not accept a contract by display name, list row, or inferred market position.
- Do not bypass MekHQ contract acceptance logic with direct save edits or generic field patches.
- Do not auto-answer arbitrary Swing prompts.
- Keep `saveAfterSuccess` explicit and opt-in.
- Preserve dry-run behavior; dry-run must not add missions, funds, reports, travel state, rentals, or saves.
- Treat decline/reject, negotiation, market refresh, and contract generation as separate future commands unless source review proves they belong in the same design.
- Commit completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- The design identifies the source-owned acceptance path and every known side effect.
- The proposed API contract uses the shared guarded command envelope from issue `#45`.
- Selector policy is safe enough for MEK-RPG, or the design explicitly blocks implementation until selectors/readiness are improved.
- Prompt-policy behavior is explicit and conservative.
- MEK-RPG integration guidance is memo-ready.
- Roadmap, tracking, and task docs remain consistent.

## Open Questions

- Are contract ids stable enough across the loaded session and save/reload for acceptance, or should the command require live-session tokens plus guard fields?
- Which contract acceptance prompts can be source-detected before mutation, and which require refusal until a source-owned prompt policy exists?
- Does MekHQ acceptance always credit advances and transport funds through the same path, or are there contract-type or option-dependent branches?
- Should the first command accept only one contract and refuse if any active/deployed contract state would make acceptance ambiguous?
