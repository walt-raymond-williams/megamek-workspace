# MEK-RPG Live MekHQ Activity History API Tracking

## Purpose

GitHub Issues are the execution source of truth. This file is the compact recovery snapshot for epic `#56`, which tracks richer read-only MekHQ activity history export for MEK-RPG and campaign-assistant workflows.

## Workstream Shape

- Parent epic: `#56`, "Epic: Investigate richer MekHQ activity history API"
- Status: `Planned`
- Integration branch: use the active local MekHQ API branch unless a later source implementation slice needs a new branch.
- Human review required before merge to `master`: `Yes`, if source changes land.

## Source Anchors

- `Confirmed from source`: `Campaign#addReport(...)` can add current daily report text and, when campaign options permit, append `HistoricalLogEntry` rows for historical daily logs.
- `Confirmed from source`: `HistoricalDailyReportDialog` views bounded historical daily report windows and uses `MHQConstants.MAX_HISTORICAL_LOG_DAYS`.
- `Confirmed from source`: `Person` serializes and reads six log families: `personnelLog`/personal, `medicalLog`, `patientLog`, `scenarioLog`, `assignmentLog`, and `performanceLog`.
- `Confirmed from source`: `Finances` owns serialized `Transaction` rows through `Finances#getTransactions()` and `Transaction#writeToXML(...)`.
- `Inferred`: historical daily reports are likely the safest first export slice, while per-person medical and patient logs need explicit privacy and target-filter rules before implementation.

## Issue Snapshot

- Last refreshed: `2026-06-23`
- Open:
  - `#57`: Audit MekHQ activity-history source owners.
  - `#58`: Design read-only MekHQ activity-history API.
  - `#59`: Implement historical daily report activity export.
  - `#60`: Implement MekHQ per-person activity log export.
  - `#61`: Add MekHQ activity-history fixtures and tests.
- Blocked:
  - `#59`, `#60`, and `#61` should wait for `#57` and `#58`.

## Recommended Next Step

- Issue: `#57`
- Why next: the audit must classify source owners, serialization, UI consumers, date semantics, text/HTML behavior, privacy risk, and payload risk before API shape or implementation.
- Handoff: `docs/handoffs/active/audit-mekhq-activity-history-sources.md`

## Verification State

- Commands passed: none yet for this workstream.
- Manual checks: none yet.
- Known blockers: source implementation smoke tests will need a disposable campaign with representative historical daily logs and personnel logs.

## Related Docs

- `docs/current/ROADMAP.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`
- `docs/current/GUARDED_LIVE_MEKHQ_COMMAND_API_TRACKING.md`
- `docs/handoffs/active/audit-mekhq-activity-history-sources.md`
