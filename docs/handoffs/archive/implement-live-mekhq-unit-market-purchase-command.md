# Agent Handoff

## Issue

- GitHub issue: `#54`
- Roadmap entry: `Implement guarded live MekHQ unit-market purchase command`
- Priority: `High`
- Parent epic: `#44`
- Design issue: `#49`

## Goal

Implement source-generated live-session unit-market offer selectors and a guarded single-offer purchase command for non-black-market unit-market offers.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_UNIT_MARKET_PURCHASE_COMMAND_DESIGN.md`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCommandReadinessExporter.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/gui/panes/UnitMarketPane.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/market/unitMarket/UnitMarketOffer.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/market/unitMarket/AbstractUnitMarket.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/Campaign.java`

## Expected Output

- `GET /campaign/commands` exposes source-generated unit-market offer selector candidates.
- `POST /campaign/command/markets/unit-offers/purchase` supports dry-run and apply for one unique non-black-market offer.
- Duplicate exact offer fingerprints, stale selectors, stale guard fields, insufficient funds, unloadable entities, and black-market offers are refused.
- Purchase applies through MekHQ-owned logic: price calculation, `TransactionType.UNIT_PURCHASE` debit, `Campaign#addNewUnit(...)`, delivery facts, offer removal, reports, optional audit report, and command-envelope save handling.
- Compile and checkstyle pass from `external/src/mekhq`.

## Files And Areas

Likely source files:

- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCommandReadinessExporter.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/gui/panes/UnitMarketPane.java`
- a new source service/helper near the local command API or unit market package, if it keeps purchase logic reusable without JTable selection
- local API fixture files under `docs/templates/` if existing command fixtures are updated

Likely workspace docs:

- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/GUARDED_LIVE_MEKHQ_COMMAND_API_TRACKING.md`
- `docs/current/TASKS.md`
- this handoff, moved to `docs/handoffs/archive/` when done

## Constraints

- Do not purchase by row index, display name, localized label, or client-computed hash.
- Do not implement purchase as manual funds debit plus independent unit add.
- Do not support multi-offer purchases in V1.
- Do not support black-market purchases in V1 because the existing UI path has random swindle behavior.
- Do not auto-assign cargo, crew, or transport loads after a DropShip purchase.
- Preserve the shared command envelope: campaign/date guards, idempotency, dry-run, prompt policy, opt-in save, prompt facts, save facts, before/after facts, and process-local retry semantics.

## Acceptance Criteria

- A unique non-black-market DropShip offer can be selected safely by source-generated selector plus guard fields.
- Dry-run reports intended price, balance, delivery, unit, offer-removal, report, prompt, and save facts without mutation.
- Apply mode debits the expected balance, creates the unit through `Campaign#addNewUnit(...)`, removes exactly one matching offer, reports the new unit id and delivery state, and does not save unless requested.
- Duplicate exact offers return `duplicate_offer_ambiguous`.
- Stale selector or stale price/balance guards return a refusal without mutation.
- Black-market offers return `black_market_purchase_unsupported`.
- `.\gradlew.bat :MekHQ:compileJava` and `.\gradlew.bat :MekHQ:checkstyleMain` pass from `external/src/mekhq`.

## Open Questions

- Should selector tokens be stored only inside the readiness response, or cached in a process-local selector registry for clearer stale-token diagnostics?
- Should V1 require a successful matching dry-run before apply for high-price purchases, or are full guard fields sufficient?
