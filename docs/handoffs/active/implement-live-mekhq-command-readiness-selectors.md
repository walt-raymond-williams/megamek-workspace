# Agent Handoff

## Issue

- GitHub issue: `#46`
- Roadmap entry: `Implement live MekHQ command readiness and selector discovery`
- Priority: `High`
- Parent epic: `#44`

## Goal

Expose which guarded commands are currently available and why other commands are blocked, including safe selectors where MekHQ can provide them.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporter.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/market/unitMarket/UnitMarketOffer.java`

## Expected Output

- Source-backed decision on endpoint shape, likely `GET /campaign/commands`.
- Initial command readiness rows for status-note, funds adjustment, personnel status, medical treatment, contract decision, personnel hire, unit purchase, repair/procurement, and save.
- Selector policy for live-session selectors versus durable selectors.
- Implementation issue if endpoint is ready to build.

## Files And Areas

Likely files to read or edit:

- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporter.java`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/templates/mekhq-live-campaign-state.fixture.json`

## Constraints

- Do not expose selectors that cannot safely identify the intended campaign object.
- Do not enable unit-market purchases from display names or row indexes.

## Acceptance Criteria

- MEK-RPG can ask MekHQ which commands to display without inferring from read-only sections.
- Blocked commands include machine-readable reason codes.
- Unit-market purchase remains blocked unless safe selectors are designed.

## Open Questions

- Should selectors be signed/opaque tokens tied to the current state revision?
- Should selector discovery live under `/campaign/commands` or inside `bridge_metadata`?
