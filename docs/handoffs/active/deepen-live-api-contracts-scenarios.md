# Agent Handoff

## Issue

- GitHub issue: `#41`
- Roadmap entry: `Add live API contract and scenario depth with fixtures`
- Priority: `Medium`

## Goal

Expand live read-only API coverage for active contracts and scenarios, then add richer sanitized fixtures for MEK-RPG adapter tests.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEGAMEK_LIVE_API_CHANGE_REQUEST.md`

Prefer completing or reviewing issue `#39` first so this work reuses the settled trust envelope and unsupported-entry shape.

## Expected Output

- Source-backed mapping for active contract and scenario live fields.
- API DTO/output expansion or structured unsupported entries for unavailable fields.
- At least one sanitized active-contract/scenario-rich fixture, or a documented blocker if no suitable disposable data exists.
- Documentation explaining empty, unavailable, and unsupported contract/scenario context.

## Files And Areas

Likely files to read or edit:

- `external/src/mekhq`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`
- `docs/templates/mekhq-live-campaign-*.fixture.json`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg "class Contract|class Scenario|Scenario|Contract" external/src/mekhq/MekHQ/src
.\gradlew.bat :MekHQ:compileJava
.\gradlew.bat :MekHQ:checkstyleMain
```

Run Gradle commands from `external/src/mekhq`. Record exact blockers if source verification cannot run.

## Constraints

- This is not a contract accept/decline API.
- This is not tactical result application.
- Do not commit raw campaign saves.
- Use copied/disposable saves for any fixture generation or live smoke test.
- Do not include unrelated user changes.
- Commit completed workspace changes before stopping unless explicitly told not to.

## Acceptance Criteria

- `GET /campaign/state` can represent useful non-empty contract/scenario context when MekHQ has it loaded.
- Unsupported gaps are structured and evidence-labeled.
- Fixture/docs give MEK-RPG a stable adapter-test example or a clear blocker.
- Verification commands or blockers are recorded.

## Open Questions

- Which copied/disposable campaign should supply active contract/scenario fixture data?
- Which tactical-result pointers exist before a scenario has been resolved?
