# Agent Handoff

## Issue

- GitHub issue: `#75`
- Roadmap entry: `Epic: Add guarded TO&E and pilot assignment commands for MEK-RPG`
- Priority: `High`

## Goal

Implement guarded MekHQ local API commands for TO&E force creation, rename, empty-force deletion, and unit movement.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- Design note from issue `#72`
- Read selector implementation from issue `#73`
- Source audit from issue `#71`

## Expected Output

- MekHQ source endpoint(s) for `toe.move_unit`, `toe.create_force`, `toe.rename_force`, and `toe.delete_empty_force`.
- Dry-run and apply behavior with structured refusal responses.
- Updated readiness rows, docs, tests, and fixtures where practical.

## Files And Areas

Likely files to read or edit:

- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCommandReadinessExporter.java`
- force/TO&E source owners identified by issue `#71`
- `external/src/mekhq/MekHQ/test`
- `docs/current/`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
.\gradlew.bat --no-daemon :MekHQ:test --tests mekhq.service.LocalControlServiceHttpTest --tests mekhq.service.LocalCommandReadinessExporterTest
.\gradlew.bat --no-daemon :MekHQ:compileJava :MekHQ:checkstyleMain
```

Run source commands from `external/src/mekhq`.

## Constraints

- Do not move units by display name or tree row.
- Do not bypass MekHQ force tree validation.
- Do not delete non-empty forces.
- Do not auto-answer arbitrary dialogs.
- Do not save unless `saveAfterSuccess=true` is explicitly supported and requested.

## Acceptance Criteria

- Dry-run validates without mutation.
- Apply mode updates MekHQ-owned force tree/unit membership state.
- Invalid parent/child movement, stale force ids, non-empty deletion, duplicate/invalid force names, and deployment/scenario locks are refused with structured reason codes.
- Post-command `GET /campaign/state` exposes the updated force tree.
- A negative API request is followed by `/status` or `GET /campaign/commands` to prove the local server remains available.

## Open Questions

- Are force names globally unique, sibling-unique, or free-form in MekHQ?
- Which scenario/deployment states should lock force movement in V1?
