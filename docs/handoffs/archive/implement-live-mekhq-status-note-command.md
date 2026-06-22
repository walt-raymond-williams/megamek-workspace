# Agent Handoff

## Issue

- GitHub issue: `#50`
- Roadmap entry: `Implement guarded live MekHQ campaign status-note command`
- Priority: `High`
- Parent epic: `#44`

## Goal

Implement the first low-risk non-day-advance guarded MekHQ command for MEK-RPG by adding an auditable campaign status/report note through MekHQ-owned report logic.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/GUARDED_LIVE_MEKHQ_COMMAND_API_TRACKING.md`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCommandReadinessExporter.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/Campaign.java`

## Expected Output

- Source endpoint, likely `POST /campaign/command/status-note`.
- Shared guarded command-envelope validation for this first non-day-advance command.
- Dry-run validation path that reports the intended note without mutating.
- Apply path that calls `Campaign#addReport(...)`.
- Response with before/after report counts, created report facts, prompt/save facts, warnings, and audit context.
- Documentation and fixture updates that mark status-note available or implemented in `GET /campaign/commands`.

## Files And Areas

Likely files to read or edit:

- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCommandReadinessExporter.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporter.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/Campaign.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/reporting/DailyReportType.java`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/GUARDED_LIVE_MEKHQ_COMMAND_API_TRACKING.md`
- `docs/templates/mekhq-live-campaign-commands.fixture.json`

## Commands

Useful source checks:

```powershell
rg -n "addReport|DailyReportType|currentReport|reportLine" external/src/mekhq/MekHQ/src/mekhq -g "*.java"
```

Verification from `external/src/mekhq`:

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat :MekHQ:compileJava
.\gradlew.bat :MekHQ:checkstyleMain
```

## Constraints

- Do not include unrelated user changes.
- Do not edit campaign saves directly.
- Preserve the disabled-by-default, loopback-only local control API posture.
- Mutating apply mode must run on the Swing event dispatch thread and respect the existing single-command guard.
- Do not accept arbitrary HTML from MEK-RPG unless the implementation sanitizes or tightly constrains it.
- Do not implement funds, personnel, contract, market purchase, medical, repair, or scenario mutation in this issue.
- Do not save unless `saveAfterSuccess` is explicit and implemented through MekHQ save logic.

## Acceptance Criteria

- Request validates command name/version, idempotency key, campaign id/name/date guards, `dryRun`, prompt policy, save policy, report category, note text, and audit context.
- Dry-run returns `dry_run` and intended side effects without changing report counts.
- Apply mode calls `Campaign#addReport(...)`, returns `applied`, and reports before/after counts.
- Refused/blocked/failed responses include machine-readable `statusReason`.
- `GET /campaign/commands` reflects the new command readiness state after implementation.
- MekHQ compile/checkstyle pass; disposable-campaign live smoke test is run or the exact blocker is recorded.

## Open Questions

- Which report categories should be allowed in V1: only `GENERAL`, or a restricted set such as `GENERAL`, `PERSONNEL`, `MEDICAL`, `FINANCES`, `ACQUISITIONS`, and `BATTLE`?
- Should the note body be plain text only, or should the endpoint generate a small source-owned HTML prefix for MEK-RPG audit context?
- Should idempotency cache support be introduced as part of this first command or as a reusable helper immediately before this endpoint?
