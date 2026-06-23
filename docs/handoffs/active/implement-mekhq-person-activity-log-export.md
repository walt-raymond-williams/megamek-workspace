# Agent Handoff

## Issue

- GitHub issue: `#60`
- Roadmap entry: `Epic: Investigate richer MekHQ activity history API`
- Priority: `Medium`

## Goal

Implement per-person activity log export for the local read-only MekHQ API, if the audit and design confirm privacy and payload rules.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_ACTIVITY_HISTORY_API_TRACKING.md`
- Audit note from issue `#57`
- Design note from issue `#58`
- Implementation results from issue `#59`, if completed first

## Expected Output

- MekHQ source changes that expose bounded, sanitized, filterable per-person log activity through the chosen read-only API shape.
- Tests and fixtures with representative personal/service, assignment, scenario, performance, medical, and patient log rows.
- Documentation updates recording privacy/default behavior.

## Files And Areas

Likely files to read or edit:

- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporter.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/personnel/Person.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/log/LogEntry.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/log/LogEntryType.java`
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
- Prefer implementing after `#59` unless the design recommends otherwise.
- Require explicit target filters or conservative defaults for private medical/patient history.
- Do not edit campaign saves.

## Acceptance Criteria

- Export requires explicit person target filters or a conservative default that avoids dumping all private medical/patient history.
- Personal/service, assignment, scenario, performance, medical, and patient logs are distinguished by family and entry type.
- Legacy/personnel-log migration behavior is handled according to source findings.
- Response includes source/trust metadata and bounded limits.
- Tests cover filtering, date windows, multiple log families, missing/no-log people, and sanitization.

## Open Questions

- Which person selector should the endpoint use: UUID, current API person id, name guard, or a combination?
- Should medical and patient logs be separate opt-in families even when a person filter is provided?
