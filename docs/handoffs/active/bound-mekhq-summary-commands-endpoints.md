# Agent Handoff

## Issue

- GitHub issue: `#64`
- Roadmap entry: `Epic: Stabilize live MekHQ API reliability for MEK-RPG play`
- Priority: `High`

## Goal

Keep `GET /campaign/summary` and `GET /campaign/commands` fast and bounded so MEK-RPG can call them during live play without waiting on deep campaign-state traversal.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_RELIABILITY_TRACKING.md`
- `docs/handoffs/active/audit-live-mekhq-api-timeouts.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEGAMEK_API_RELIABILITY_HANDOFF_2026-06-26.md`

## Expected Output

- Source changes that bound summary and command readiness work.
- Explicit unavailable/limited readiness rows or warnings for any selector generation that is too expensive.
- Updated tests or fixtures for normal and fallback behavior.

## Files And Areas

Likely files to read or edit:

- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporter.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCommandReadinessExporter.java`
- Local control API tests and fixtures.

## Commands

Useful commands or checks:

```powershell
git status --short --branch
.\gradlew.bat :MekHQ:compileJava
.\gradlew.bat :MekHQ:checkstyleMain
.\gradlew.bat :MekHQ:test --tests mekhq.service.LocalCommandReadinessExporterTest --tests mekhq.service.LocalControlServiceHttpTest
```

## Constraints

- Summary must stay read-only and should not call full state export.
- Readiness should prefer explicit blockers over expensive guessing.
- Preserve command safety guards and selector semantics from epic `#44`.

## Acceptance Criteria

- `/campaign/summary` avoids unrelated full-state collectors.
- `/campaign/commands` returns bounded readiness or structured limited-readiness warnings.
- Tests cover fallback/limited behavior.
- Verification passes or blockers are documented.

## Open Questions

- Which readiness selectors are safe enough for every call, and which should become opt-in or bounded?
