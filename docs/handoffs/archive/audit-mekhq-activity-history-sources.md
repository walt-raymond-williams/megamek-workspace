# Agent Handoff

## Issue

- GitHub issue: `#57`
- Roadmap entry: `Epic: Investigate richer MekHQ activity history API`
- Priority: `Medium`

## Goal

Audit MekHQ campaign-history sources and classify what can safely feed a richer read-only local activity history API.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_ACTIVITY_HISTORY_API_TRACKING.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`
- `docs/current/GITHUB_ISSUE_WORKFLOW.md`

## Expected Output

- Add a source-backed audit note under `docs/current/`.
- Map each history-like source to owner class, save serialization, UI consumer, date semantics, text/HTML behavior, privacy risk, payload-size risk, and recommended API classification.
- Recommend the next child issue to run.

## Files And Areas

Likely files to read:

- `external/src/mekhq/MekHQ/src/mekhq/campaign/Campaign.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/log/`
- `external/src/mekhq/MekHQ/src/mekhq/gui/dialog/HistoricalDailyReportDialog.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/personnel/Person.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/finances/`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/scenario/`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporter.java`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "HistoricalLogEntry|historicalDaily|MAX_HISTORICAL_LOG_DAYS|addReport" external/src/mekhq/MekHQ/src/mekhq -g "*.java"
rg -n "personnelLog|medicalLog|patientLog|scenarioLog|assignmentLog|performanceLog|LogEntryType" external/src/mekhq/MekHQ/src/mekhq -g "*.java"
rg -n "class Transaction|class Finances|getTransactions|writeToXML" external/src/mekhq/MekHQ/src/mekhq/campaign/finances -g "*.java"
```

## Constraints

- Do not mutate MekHQ source for this audit.
- Do not edit campaign saves.
- Distinguish campaign ledger history from application/debug log files.
- Preserve uncertainty and evidence labels.
- Do not include unrelated user changes.

## Acceptance Criteria

- Daily reports/current report buffers and historical daily log behavior are distinguished.
- Per-person log families are mapped, including legacy/personnel-log migration behavior if relevant.
- Finance transactions, scenario reports, maintenance/logistics reports, market/procurement records, and application/debug logs are explicitly classified.
- The note identifies which sources are safe for `GET /campaign/state`, which likely need a dedicated endpoint, and which should remain unsupported.
- Parent epic `#56`, `ROADMAP.md`, and `TASKS.md` are updated or commented with the audit result and next recommended issue.

## Open Questions

- Should medical and patient logs require explicit person filters even for read-only local export?
- Are historical daily log entries present only when campaign options are enabled, and how should disabled history be represented?
- Should finance transactions be part of a unified activity feed or remain in the finance section with stronger filters?
