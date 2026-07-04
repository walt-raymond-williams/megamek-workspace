# Agent Handoff

## Issue

- GitHub issue: `#85`
- Roadmap entry: `Epic: Expose planetary information in the MekHQ API`
- Priority: `High`
- Status: `Done`

## Goal

Design a read-only local API endpoint that returns Navigation-tab-style planetary and parent-system facts for the loaded campaign date, with exact id/name selector behavior grounded in the issue `#84` source audit.

## Completed Output

- Design note: `docs/current/MEK_RPG_LIVE_MEKHQ_PLANETARY_API_DESIGN.md`
- Endpoint chosen: `GET /campaign/planetary/detail`
- Date policy: loaded campaign date only for V1.
- Selector policy: exact selectors only; `systemId` preferred, `systemName` exact/case-insensitive with ambiguity detection, `planetId`/`planetPosition`/`planetName` as selected-planet selectors or guards.
- Error policy: structured `400`, `404`, and `409` responses for missing selectors, invalid positions, conflicts, not-found cases, and ambiguous names.
- Follow-up issue: `#86` should implement the endpoint using the design note.

## Required Context

Read these first for follow-up implementation:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/PLANETARY_INFORMATION_API_TRACKING.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_PLANETARY_API_SOURCE_AUDIT.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_PLANETARY_API_DESIGN.md`

## Notes

- The design deliberately avoids using current Swing Navigation-tab selection state because no source-confirmed local API bridge exposes it.
- The design deliberately defers arbitrary `asOfDate` support because academies and campaign overlays depend on loaded campaign context.
- The active implementation handoff is `docs/handoffs/active/implement-planetary-information-endpoint.md`.
