# Agent Handoff

## Issue

- GitHub issue: `#61`
- Roadmap entry: `Epic: Investigate richer MekHQ activity history API`
- Priority: `Medium`

## Goal

Add representative activity-history fixtures and regression tests for the richer MekHQ history API slices.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_ACTIVITY_HISTORY_API_TRACKING.md`
- Design note from issue `#58`
- Implementation notes from issues `#59` and `#60`

## Expected Output

- Sanitized fixture JSON and MekHQ source tests covering the activity-history API contract.
- Documentation updates linking fixtures and verification commands.

## Files And Areas

Likely files to read or edit:

- `docs/templates/`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`
- `external/src/mekhq/MekHQ/test/`
- `external/src/mekhq/MekHQ/src/mekhq/service/`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
.\gradlew.bat :MekHQ:test --tests mekhq.service.LocalCampaignStateExporterTest
.\gradlew.bat :MekHQ:compileJava
.\gradlew.bat :MekHQ:checkstyleMain :MekHQ:checkstyleTest
```

## Constraints

- Wait for issue `#58`.
- Do not commit raw user campaign saves.
- Keep fixtures sanitized and small enough for review.

## Acceptance Criteria

- Fixtures are sanitized and live under `docs/templates/` or another documented location.
- Tests prove default limits/windowing, explicit filters, unsupported entries, sanitization, and privacy behavior.
- Existing live API fixtures/docs are updated to reference the new history behavior.
- Verification commands are recorded in `docs/current/` or issue close-out.

## Open Questions

- Should this remain a separate final coverage issue, or should future implementation issues fold in enough tests to close it as redundant?
