# Agent Handoff

## Issue

- GitHub issue: `#67`
- Roadmap entry: `Epic: Stabilize live MekHQ API reliability for MEK-RPG play`
- Priority: `Medium`

## Goal

Add regression coverage and a repeatable live smoke checklist proving the local MekHQ API stays responsive for the MEK-RPG play-critical reads.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_RELIABILITY_TRACKING.md`
- `docs/current/KNOWN_COMMANDS.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEGAMEK_API_RELIABILITY_HANDOFF_2026-06-26.md`

## Expected Output

- Targeted source tests for summary, commands, narrowed state sections, fallback warnings, and post-failure server availability.
- Fixture updates if endpoint shape changes.
- A current-doc smoke checklist with exact PowerShell commands and expected bounded behavior.

## Files And Areas

Likely files to read or edit:

- Local control API tests under `external/src/mekhq/MekHQ/test/`
- `docs/current/KNOWN_COMMANDS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_RELIABILITY_TRACKING.md`
- Live API fixtures under `docs/templates/`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
.\gradlew.bat :MekHQ:test --tests mekhq.service.LocalCommandReadinessExporterTest --tests mekhq.service.LocalControlServiceHttpTest
.\gradlew.bat :MekHQ:compileJava
.\gradlew.bat :MekHQ:checkstyleMain
```

## Constraints

- Do not require a raw campaign save to be committed.
- Live smoke commands must use safe read-only GET requests unless explicitly testing a guarded command on disposable data.
- Record exact blockers if source-built GUI smoke testing is unavailable.

## Acceptance Criteria

- Automated tests cover as much reliability behavior as can be tested without manual GUI control.
- Smoke checklist includes the reproduction commands from the MEK-RPG handoff and expected results after fixes.
- Repeatable commands are added to `KNOWN_COMMANDS.md` if they become durable.
- Verification passes or blockers are documented.

## Open Questions

- Should endpoint response-time expectations be documented as hard local thresholds or as "bounded/no full-state traversal" behavior?

## Completion Notes

- Completed on `2026-06-26`.
- MekHQ source commit: `81afcee70a` (`Add local API reliability regression tests`).
- Smoke checklist: `docs/current/MEK_RPG_LIVE_MEKHQ_API_RELIABILITY_SMOKE_CHECKLIST.md`.
- Verification passed from `external/src/mekhq`: `.\gradlew.bat --no-daemon :MekHQ:test --tests mekhq.service.LocalCampaignStateExporterTest --tests mekhq.service.LocalControlServiceHttpTest --tests mekhq.service.LocalCommandReadinessExporterTest`.
- Verification passed from `external/src/mekhq`: `.\gradlew.bat --no-daemon :MekHQ:compileJava :MekHQ:checkstyleMain :MekHQ:checkstyleTest`.
- Live GUI smoke was not run because no source-built MekHQ GUI with a safe campaign was under agent control.
- Source push remains blocked because upstream `MegaMek/mekhq` returned 403 for `walt-raymond-williams`.
