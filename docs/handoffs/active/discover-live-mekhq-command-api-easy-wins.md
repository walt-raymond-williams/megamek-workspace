# Agent Handoff

## Issue

- GitHub issue: `#43`
- Roadmap entry: `Discover first guarded live MekHQ command API easy wins for MEK-RPG`
- Priority: `High`

## Goal

Turn the MEK-RPG write-side strategy shift into a source-backed command API plan and identify the first one or two safe MekHQ-owned mutation endpoints to implement after read-only live state.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/MEK_RPG_MEKHQ_BRIDGE_PRIMITIVES.md`

## Expected Output

- A source-backed recommendation for the first safe command API slice.
- A command envelope draft covering guard fields, idempotency, dry-run behavior, save policy, prompt handling, and before/after response fields.
- A ranked easy-win list with explicit blockers for unsafe or selector-dependent workflows.
- Follow-up GitHub issues/handoffs for implementation if one or two endpoints are ready.

## Files And Areas

Likely files to read or edit:

- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporter.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/Campaign.java`
- `external/src/mekhq/MekHQ/src/mekhq/gui/FinancesTab.java`
- `external/src/mekhq/MekHQ/src/mekhq/gui/dialog/ContractMarketDialog.java`
- `external/src/mekhq/MekHQ/src/mekhq/gui/panes/UnitMarketPane.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/market/unitMarket/UnitMarketOffer.java`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "addFunds|recruitPerson|acceptContract|purchaseSelectedOffers|UnitMarketOffer|addReport" external/src/mekhq/MekHQ/src/mekhq -g "*.java"
```

If implementation begins, verify from `external/src/mekhq`:

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat :MekHQ:compileJava
.\gradlew.bat :MekHQ:checkstyleMain
```

## Constraints

- Do not include unrelated user changes.
- Do not edit campaign saves directly.
- Preserve the local-only, disabled-by-default control API posture while prototyping.
- Mutating commands must call MekHQ-owned logic and refuse when required selectors, prompts, or preconditions are unsafe.
- Do not treat unit-market display rows as durable selectors.
- Commit completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- The command API strategy is grounded in source references, not just desired MEK-RPG behavior.
- The first easy-win endpoint candidates are ranked by value, risk, selectors, prompt policy, and disposable-campaign verification path.
- Unsafe workflows such as DropShip purchase are decomposed into prerequisite selector/prompt work instead of hand-waved.
- Any ready implementation work is split into narrow child issues with handoffs.

## Open Questions

- What is the best MekHQ-owned place to store a MEK-RPG campaign status/note mutation: report entry, campaign note, custom metadata, or another existing structure?
- Should command selectors be live-session ephemeral ids or durable ids that survive save/reload?
- Which command should come first after discovery: command readiness/selector discovery, campaign status note, GM funds adjustment, contract decision, personnel hire, or unit purchase?
