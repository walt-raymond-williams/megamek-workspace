# Agent Handoff

## Issue

- GitHub issue: `#85`
- Roadmap entry: `Epic: Expose planetary information in the MekHQ API`
- Priority: `High`

## Goal

Design a read-only local API endpoint that accepts a planet name and returns Navigation-tab-style planetary and parent-system facts for the loaded campaign date.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/PLANETARY_INFORMATION_API_TRACKING.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_PLANETARY_API_SOURCE_AUDIT.md` after issue `#84` creates it

## Expected Output

- A design note under `docs/current/`, recommended path `docs/current/MEK_RPG_LIVE_MEKHQ_PLANETARY_API_DESIGN.md`.
- Endpoint path, query parameters, response envelope, field groups, ambiguity handling, error behavior, source/provenance policy, unsupported entries, and fixture/test expectations.
- Roadmap/tracking/task updates.

## Files And Areas

Likely files to read:

- `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporter.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlServiceHttpTest.java`
- Source files identified by issue `#84`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "campaign/personnel/detail|campaign/pending-deployments|campaign/state" external/src/mekhq/MekHQ/src/mekhq/service
```

## Constraints

- Do not include unrelated user changes.
- Preserve uncertainty and evidence labels.
- The endpoint must remain read-only and local-control-API-shaped.
- Commit completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- Endpoint path and query parameters are specified, including `planetName` and any optional exact/id/date controls.
- Response shape includes campaign/API metadata, resolved planet/system identity, current-date facts, warnings, unsupported entries, and timing.
- Not found, ambiguous name, duplicate planet/system names, and no-loaded-campaign behavior are specified.
- Expected tests/fixtures are listed for issue `#86`.
- Roadmap, tracking doc, and task board are updated and changes are committed.

## Open Questions

- Should the endpoint be `/campaign/planetary/detail`, `/campaign/planets/detail`, or another path?
- Should clients be able to request a date other than the loaded campaign date for historical/future planetary events?
- Should source/provenance metadata mirror `SourceableValue` directly or use a smaller consumer-facing shape?
