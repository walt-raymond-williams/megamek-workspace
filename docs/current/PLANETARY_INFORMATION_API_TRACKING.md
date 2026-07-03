# Planetary Information API Tracking

## Purpose

GitHub Issues are the execution source of truth. This file is the compact recovery snapshot for the workstream that will expose Navigation-tab-style planetary data through the local MekHQ API.

## Integration Branch

- Branch: `codex/guarded-live-mekhq-command-api`
- Base: `master`
- Human review required before merge: `Yes`
- Notes: Use the existing local API branch unless a later agent intentionally creates a narrower child branch. Source changes belong in `external/src/mekhq`; workspace docs record design, verification, and handoffs.

## Issue Snapshot

- Last refreshed: `2026-07-03`
- Epic: `#83` Expose planetary information in the MekHQ API
- Open:
  - `#84`: Audit MekHQ Navigation tab planetary data sources
  - `#85`: Design read-only MekHQ planetary information API
  - `#86`: Implement read-only MekHQ planetary information endpoint
  - `#87`: Add planetary API docs, fixtures, and live smoke checklist
- Blocked: None known before source audit.

## Recommended Next Step

- Issue: `#84`
- Why next: The API should match MekHQ's Navigation side tab, and `PlanetViewPanel` has date-sensitive display logic that should be mapped before naming response fields.
- Handoff: `docs/handoffs/active/audit-navigation-planetary-data-sources.md`

## Verification State

- Commands passed: Not applicable yet; planning and issue creation only.
- Manual checks: Initial source orientation found likely owners in `NavigationTab`, `PlanetViewPanel`, `Planet`, `PlanetarySystem`, and `LocalControlService`.
- Known blockers: Live smoke will require a source-built MekHQ GUI launched with `-Dmekhq.controlApi.enabled=true` and a safe loaded campaign.

## Related Docs

- `docs/current/ROADMAP.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`
- `docs/current/GITHUB_ISSUE_WORKFLOW.md`
- `docs/handoffs/active/audit-navigation-planetary-data-sources.md`
- `docs/handoffs/active/design-planetary-information-api.md`
- `docs/handoffs/active/implement-planetary-information-endpoint.md`
- `docs/handoffs/active/add-planetary-api-fixtures-smoke.md`
