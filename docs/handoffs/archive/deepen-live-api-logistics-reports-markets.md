# Agent Handoff

## Issue

- GitHub issue: `#42`
- Roadmap entry: `Deepen live API logistics, reports, and market safeguards`
- Priority: `Medium`

## Goal

Deepen repairs/logistics/report coverage in the live read-only API while keeping market data display-only with explicit automation safeguards.

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

- Source-backed mapping for repair queues, parts/shopping/acquisition pressure, assigned techs, time remaining, cargo/transport warnings, categorized reports, and market display fields.
- API DTO/output expansion for safe method-backed read-only fields.
- Structured unsupported entries for missing stable work item ids, selectors, or action-adjacent gaps.
- Fixture/docs update preserving `automation_ready: false` unless command semantics are intentionally designed later.

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
rg "Repair|Maintenance|Acquisition|Shopping|Market|Report|Cargo|Transport" external/src/mekhq/MekHQ/src
.\gradlew.bat :MekHQ:compileJava
.\gradlew.bat :MekHQ:checkstyleMain
```

Run Gradle commands from `external/src/mekhq`. Record exact blockers if source verification cannot run.

## Constraints

- No market purchase API.
- No personnel hiring/firing API.
- No contract accept/decline API.
- No repair execution or assignment API.
- No save/writeback command.
- Market data remains display-only unless a later issue designs stable command semantics.
- Do not include unrelated user changes.
- Commit completed workspace changes before stopping unless explicitly told not to.

## Acceptance Criteria

- Repair/logistics/report sections expose richer useful read-only context where source-backed data exists.
- Market entries include explicit display-only safeguards and duplicate/selector warnings where relevant.
- Unsupported/action-adjacent gaps are structured and do not imply write capability.
- Verification commands or blockers are recorded.

## Open Questions

- Stable repair work item ids and market offer selectors were not exposed as automation-safe selectors in this pass.
- Market rows, repair rows, and shopping-list rows remain display-only. Future command work needs separate selector, duplicate-safety, prompt-policy, and writeback design.

## Completion Notes

Completed on `2026-06-22`.

MekHQ source commit:

- `911a338788` (`Deepen live campaign logistics market reports`)

Implemented:

- repair pressure counts, display-only repair queue rows, shopping-list pressure/rows, cargo/transport relationship summaries, report metadata/counts, market summaries/rows, and explicit automation guards.
- unsupported entries for stable repair work ids, repair/procurement commands, stable market offer selectors, and market mutation commands.
- fixture/docs updates preserving the read-only, loopback-only, disabled-by-default boundary.

Verification:

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat :MekHQ:compileJava
.\gradlew.bat :MekHQ:checkstyleMain
```

Both passed from `external/src/mekhq`.
