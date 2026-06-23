# Agent Handoff

## Issue

- GitHub issue: `#59`
- Roadmap entry: `Epic: Investigate richer MekHQ activity history API`
- Priority: `Medium`

## Goal

Implement the first safe activity-history export slice for historical daily campaign reports, if the audit and design confirm the source model is safe.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_ACTIVITY_HISTORY_API_TRACKING.md`
- Audit note from issue `#57`
- Design note from issue `#58`

## Expected Output

- MekHQ source changes that expose bounded, sanitized historical daily report activity through the chosen read-only API shape.
- Source tests and sanitized fixtures.
- Documentation updates recording verification and any unsupported cases.

## Files And Areas

Likely files to read or edit:

- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporter.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/Campaign.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/log/HistoricalLogEntry.java`
- `external/src/mekhq/MekHQ/src/mekhq/gui/dialog/HistoricalDailyReportDialog.java`
- Relevant MekHQ service tests under `external/src/mekhq/MekHQ/test/`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
.\gradlew.bat :MekHQ:compileJava
.\gradlew.bat :MekHQ:checkstyleMain
.\gradlew.bat :MekHQ:test --tests mekhq.service.LocalCampaignStateExporterTest
```

## Constraints

- Wait for issues `#57` and `#58`.
- Keep the endpoint read-only.
- Do not dump unlimited historical logs by default.
- Do not edit campaign saves.

## Acceptance Criteria

- Export is disabled or clearly unsupported when historical daily logs are unavailable or option-gated off.
- Response is bounded by date range and/or limit and never dumps unlimited campaign history by default.
- Report text is sanitized consistently with existing report export behavior.
- Tests cover enabled, disabled/unavailable, date-window, limit, and sanitization behavior.
- Documentation and fixtures are updated.

## Open Questions

- Which campaign option exactly gates historical daily log capture?
- Should historical daily log entries include original report category, or does the source only preserve text/date?
