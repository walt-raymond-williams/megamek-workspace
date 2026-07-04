# Planetary Information API Tracking

## Purpose

GitHub Issues are the execution source of truth. This file is the compact recovery snapshot for the workstream that will expose Navigation-tab-style planetary data through the local MekHQ API.

## Integration Branch

- Branch: `codex/guarded-live-mekhq-command-api`
- Base: `master`
- Human review required before merge: `Yes`
- Notes: Use the existing local API branch unless a later agent intentionally creates a narrower child branch. Source changes belong in `external/src/mekhq`; workspace docs record design, verification, and handoffs.

## Issue Snapshot

- Last refreshed: `2026-07-04`
- Epic: `#83` Expose planetary information in the MekHQ API
- Completed:
  - `#84`: Audit MekHQ Navigation tab planetary data sources; audit note `docs/current/MEK_RPG_LIVE_MEKHQ_PLANETARY_API_SOURCE_AUDIT.md`.
  - `#85`: Design read-only MekHQ planetary information API; design note `docs/current/MEK_RPG_LIVE_MEKHQ_PLANETARY_API_DESIGN.md`.
- Open:
  - `#86`: Implement read-only MekHQ planetary information endpoint
  - `#87`: Add planetary API docs, fixtures, and live smoke checklist
- Blocked: None known before source audit.

## Recommended Next Step

- Issue: `#86`
- Why next: Issue `#85` chose `GET /campaign/planetary/detail`, exact selector and ambiguity rules, loaded-campaign-date behavior, response envelope, sourceable value shape, unsupported boundaries, and test expectations. The next step is to implement that endpoint in MekHQ source.
- Handoff: `docs/handoffs/active/implement-planetary-information-endpoint.md`
- Source input: `docs/current/MEK_RPG_LIVE_MEKHQ_PLANETARY_API_SOURCE_AUDIT.md` and `docs/current/MEK_RPG_LIVE_MEKHQ_PLANETARY_API_DESIGN.md`

## Verification State

- Commands passed: Documentation-only issue; no build required for `#85`.
- Manual checks: Source audit `#84` inspected `NavigationTab`, `MapTab`, `PlanetViewPanel`, `Planet`, `PlanetarySystem`, `Systems`, `SourceableValue`, academy filtering, disease helpers, and existing local API envelope conventions.
- Known blockers: Live smoke will require a source-built MekHQ GUI launched with `-Dmekhq.controlApi.enabled=true` and a safe loaded campaign.

## Related Docs

- `docs/current/ROADMAP.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_PLANETARY_API_SOURCE_AUDIT.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_PLANETARY_API_DESIGN.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`
- `docs/current/GITHUB_ISSUE_WORKFLOW.md`
- `docs/handoffs/archive/audit-navigation-planetary-data-sources.md`
- `docs/handoffs/archive/design-planetary-information-api.md`
- `docs/handoffs/active/implement-planetary-information-endpoint.md`
- `docs/handoffs/active/add-planetary-api-fixtures-smoke.md`
