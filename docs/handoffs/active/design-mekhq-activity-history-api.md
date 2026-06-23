# Agent Handoff

## Issue

- GitHub issue: `#58`
- Roadmap entry: `Epic: Investigate richer MekHQ activity history API`
- Priority: `Medium`

## Goal

Design the read-only local MekHQ activity-history API shape after the source audit is complete.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_ACTIVITY_HISTORY_API_TRACKING.md`
- The audit note produced by issue `#57`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`

## Expected Output

- Add a design note under `docs/current/`.
- Specify endpoint shape, query parameters, response envelope, default limits, date windows, category/type filters, target filters, sanitization, privacy defaults, unsupported entries, and fixture/test expectations.
- Update follow-up implementation issues if the audit changes the expected slices.

## Files And Areas

Likely files to read:

- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporter.java`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`
- `docs/templates/mekhq-live-campaign-*.fixture.json`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "sections=|unsupported|sanitize|report" external/src/mekhq/MekHQ/src/mekhq/service -g "*.java"
```

## Constraints

- Do not implement before the source audit exists.
- Preserve the disabled-by-default, loopback-only, read-only API posture.
- Keep default responses bounded.
- Do not expose medical/patient logs broadly without explicit privacy handling.

## Acceptance Criteria

- The design distinguishes campaign ledger history from application/debug logs.
- The default response is bounded and safe for dashboard use.
- Per-person medical and patient logs have explicit privacy/opt-in or target-filter rules.
- Historical daily reports, person logs, finance transactions, scenario reports, and maintenance/logistics sources have clear inclusion or unsupported decisions.
- Follow-up implementation issues are updated if needed.

## Open Questions

- Should the API use `GET /campaign/state?sections=history`, a dedicated `GET /campaign/history`, or both?
- Should high-volume domains use pagination or a simple bounded `limit` plus date window first?
- What is the minimum useful default window for MEK-RPG: 7 days, 30 days, or current campaign date only?
