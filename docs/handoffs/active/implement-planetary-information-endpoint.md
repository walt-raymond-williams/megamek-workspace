# Agent Handoff

## Issue

- GitHub issue: `#86`
- Roadmap entry: `Epic: Expose planetary information in the MekHQ API`
- Priority: `High`

## Goal

Implement the designed read-only endpoint in MekHQ source so a local client can request planetary information by planet name and receive Navigation-tab-style data.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/PLANETARY_INFORMATION_API_TRACKING.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/KNOWN_COMMANDS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_PLANETARY_API_SOURCE_AUDIT.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_PLANETARY_API_DESIGN.md`

## Expected Output

- MekHQ source changes under `external/src/mekhq` implementing the read-only endpoint.
- Focused service/HTTP tests for success and error paths.
- Workspace docs updated with source commit, verification, and remaining live-smoke status.

## Files And Areas

Likely files to read or edit:

- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporter.java` or a new focused exporter class if the design calls for it
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlServiceHttpTest.java`
- `external/src/mekhq/MekHQ/src/test/java/mekhq/service/LocalCampaignStateExporterTest.java` or new focused tests
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
cd external/src/mekhq
.\gradlew.bat --no-daemon :MekHQ:test --tests mekhq.service.LocalControlServiceHttpTest
.\gradlew.bat --no-daemon :MekHQ:compileJava :MekHQ:checkstyleMain :MekHQ:checkstyleTest
```

## Constraints

- Do not include unrelated user changes.
- Preserve uncertainty and evidence labels.
- Do not mutate or save campaigns from this endpoint.
- Do not overwrite campaign saves; use copied/disposable data for live validation.
- Commit completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- Endpoint is registered in `LocalControlService` and supports GET only.
- Request accepts planet name and follows the design issue matching/ambiguity rules.
- Response is read-only and includes campaign metadata plus planet/system data from source-owned methods.
- Tests cover success, not found, ambiguous match if applicable, bad/missing query, no loaded campaign, and method-not-allowed behavior.
- Relevant Gradle compile/test/checkstyle commands pass or exact blockers are recorded.
- Workspace docs and handoff are updated with source commit/verifications.

## Open Questions

- Should implementation reuse `LocalCampaignStateExporter` helpers or create a dedicated planetary exporter to avoid growing the state exporter further?
- Can tests use existing campaign fixture builders, or do they need a small in-memory planetary-system fixture?
