# Agent Handoff

## Issue

- GitHub issue: `#44`
- Roadmap entry: `Epic: Guarded live MekHQ command API for MEK-RPG`
- Priority: `High`

## Goal

Coordinate the write-side MEK-RPG/MekHQ API workstream. This epic exists to split high-level RPG intent into narrow MekHQ-owned command endpoints that mutate the already-loaded campaign through source-owned logic.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/MEK_RPG_MEKHQ_BRIDGE_PRIMITIVES.md`

## Expected Output

- Keep child issues sequenced and scoped.
- Preserve the disabled-by-default, loopback-only, loaded-GUI-campaign posture while prototyping.
- Ensure each command family has source-backed selectors, guard fields, prompt policy, and disposable-campaign verification before implementation.
- Coordinate focused child epic `#70` for pilot assignment and TO&E edits before declaring epic `#44` PR-ready.

## Files And Areas

Likely files to read or edit:

- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/handoffs/active/`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporter.java`

## Constraints

- Do not implement the epic directly.
- Do not create a generic patch-any-campaign-state endpoint.
- Do not edit campaign saves directly.
- Do not treat display rows as stable selectors.

## Acceptance Criteria

- Child issues cover command envelope/readiness, campaign notes, unit purchase selectors, personnel death/status, and medical/prosthetic handling.
- New child epic `#70` covers pilot assignment and TO&E mutation stories `#71` through `#77`.
- Roadmap and task board point to the child issue sequence.
- The epic can close only after enough child work has produced a coherent guarded command API slice or a documented stop decision.

## Open Questions

- Which command should be implemented first after discovery?
- Should command selectors be durable across save/reload or live-session only?
- What save policy should MEK-RPG use once commands are trusted?
- For pilot assignment and TO&E edits, can existing MekHQ UI workflows be reused safely, or does the API need source-owned non-dialog services first?
