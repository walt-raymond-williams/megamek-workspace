# Agent Handoff

## Issue

- GitHub issue: `#49`
- Roadmap entry: `Design live MekHQ unit-market purchase command API`
- Priority: `High`
- Parent epic: `#44`
- Status: `Completed on 2026-06-23`

## Goal

Design a guarded unit-market purchase command for MEK-RPG actions such as buying a DropShip from the live MekHQ market.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/MEK_RPG_MEKHQ_BRIDGE_PRIMITIVES.md`
- `external/src/mekhq/MekHQ/src/mekhq/gui/panes/UnitMarketPane.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/market/unitMarket/UnitMarketOffer.java`

## Expected Output

- Source-backed selector design for unit-market offers.
- Proposed endpoint shape, likely `POST /campaign/command/markets/unit-offers/purchase`.
- Guard fields for offer selector, expected unit name/type, expected price, expected transit duration, expected balance, and delivery policy.
- Refusal rules for duplicate or ambiguous offers.
- Design note: `docs/current/MEK_RPG_LIVE_MEKHQ_UNIT_MARKET_PURCHASE_COMMAND_DESIGN.md`
- Follow-up implementation handoff: `docs/handoffs/active/implement-live-mekhq-unit-market-purchase-command.md`

## Files And Areas

Likely files to read or edit:

- `external/src/mekhq/MekHQ/src/mekhq/gui/panes/UnitMarketPane.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/market/unitMarket/UnitMarketOffer.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/market/unitMarket/AbstractUnitMarket.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/Campaign.java`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`

## Constraints

- Do not purchase by row index, display name, or partial market fields.
- Do not implement purchase as manual funds debit plus separate unit add.
- Preserve MekHQ's actual purchase path: price calculation, debit, unit add or delivery queue, market offer removal, reports/transactions.

## Acceptance Criteria

- The design proves how a DropShip offer can be selected safely or records why it remains blocked.
- The design identifies before/after verification facts: balance, transaction, new unit id, transport/cargo state, offer removal, transit/delivery state, and reports.
- Follow-up implementation issue is narrow and disposable-campaign testable.

## Open Questions

- Should unit-market offer selectors be generated live from object identity/state hash or added as durable source fields?
- How should command behavior differ for instant delivery versus transit delivery?
