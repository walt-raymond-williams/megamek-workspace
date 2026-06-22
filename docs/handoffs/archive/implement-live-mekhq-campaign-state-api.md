# Agent Handoff: Implement Live MekHQ Campaign State API

## Issue

- GitHub issue: `#36`
- Roadmap entry: `Implement live read-only MekHQ campaign state API for MEK-RPG`
- Priority: `High`

## Goal

Add read-only live campaign-state endpoints to the existing local MekHQ control API prototype so MEK-RPG can request current campaign context from the running MekHQ GUI app without requiring the user to save first.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/MEKHQ_ADVANCE_DAY_CONTROL_API_PROTOTYPE.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_IMPLEMENTATION_PLAN.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_FEEDBACK_MEMO.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEK_RPG_LIVE_MEKHQ_API_RESPONSE_MEMO.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT_SCHEMA.md`
- `docs/current/MEK_RPG_MEKHQ_BRIDGE_PRIMITIVES.md`

## Expected Output

- `GET /campaign/summary` endpoint in the local MekHQ control API.
- `GET /campaign/state?sections=...` endpoint preserving the checkpoint top-level grouping.
- Source-backed DTO/trust-envelope output for the first useful live campaign fields.
- Structured warnings and unsupported entries for partial, unsafe, unknown, or unsupported fields.
- Sanitized fixture snippets or docs showing representative `summary`, `state`, dirty/unknown dirty-state, and warning-heavy output.
- Updated workspace docs recording source files touched, endpoint contract, verification, and any live-test blockers.
- Source commit in `external/src/mekhq`; workspace docs committed and pushed.

## Files And Areas

Likely files to read or edit:

- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/MekHQ.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/Campaign.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/personnel/Person.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/unit/Unit.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/finances/Finances.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/mission/Mission.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/mission/Contract.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/mission/Scenario.java`
- `docs/current/MEKHQ_ADVANCE_DAY_CONTROL_API_PROTOTYPE.md`
- `docs/current/KNOWN_COMMANDS.md`
- `docs/current/TASKS.md`
- `docs/current/ROADMAP.md`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
Set-Location C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq
git status --short --branch
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat :MekHQ:compileJava
.\gradlew.bat :MekHQ:checkstyleMain
```

Optional live smoke, user-assisted:

```powershell
$env:JAVA_TOOL_OPTIONS='-Dmekhq.controlApi.enabled=true -Dmekhq.controlApi.port=32180'
.\gradlew.bat :MekHQ:run
Invoke-RestMethod -Method Get -Uri 'http://127.0.0.1:32180/campaign/summary' -TimeoutSec 10 | ConvertTo-Json -Depth 8
Invoke-RestMethod -Method Get -Uri 'http://127.0.0.1:32180/campaign/state?sections=bridge_metadata,campaign,finances,personnel,units,contracts,scenarios,repairs_and_logistics,reports,unsupported' -TimeoutSec 30 | ConvertTo-Json -Depth 12
```

Follow-up workspace commit `6756a70` recorded that a user-assisted running MekHQ campaign smoke test was performed from MEK-RPG issue `#104` on `2026-06-22` against disposable campaign `The Learning Ropes-test.cpnx`. MEK-RPG issue `#106` then confirmed that selected-section dashboard/context validation should include `bridge_metadata`; omitting `sections` also returns `bridge_metadata` because it requests all supported sections.

## Constraints

- Do not include unrelated user changes.
- Preserve uncertainty and evidence labels.
- Keep these endpoints read-only.
- Keep the control API disabled by default and bound to `127.0.0.1`.
- Do not add write/action surfaces in this issue.
- Do not expose local filesystem paths by default.
- Do not treat live API output as durable MEK-RPG state; it is live context unless saved/imported or explicitly approved by the user.
- Prefer stable DTO builders over raw internal object serialization.
- Commit completed source changes in `external/src/mekhq` before stopping.
- Commit and push workspace docs before stopping unless explicitly told not to.

## Acceptance Criteria

- `GET /campaign/summary` returns useful identity, version, read-only, snapshot/dirty-state, warning, and unsupported metadata when a campaign is loaded.
- `GET /campaign/state?sections=...` returns the existing checkpoint top-level groups for requested sections.
- Unknown section names return a clear client error with supported section names.
- Campaign-facing or hard-ledger fields include evidence/source/method-backed/warning provenance where practical.
- Dirty/unsaved state is either source-confirmed or explicitly reported as `Unknown` with a warning.
- Markets, if included, are display-only and carry automation-blocking unsupported entries when selectors are not source-confirmed.
- Compile and Checkstyle pass, or exact blockers are recorded.
- Workspace docs record endpoint contract, source commit, verification, and live-test status.

## Open Questions

- Is there a source-confirmed dirty/unsaved flag in MekHQ GUI state?
- Is there a source-owned campaign revision value, or should V1 generate a live snapshot id?
- Which full-state sections are safe to implement in V1 versus returning partial payloads with unsupported entries?
- Should the first implementation include display-only markets or leave them unsupported until later?
