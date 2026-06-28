# Agent Handoff

## Issue

- GitHub issue: `#74`
- Roadmap entry: `Epic: Add guarded TO&E and pilot assignment commands for MEK-RPG`
- Priority: `High`

## Goal

Implement guarded MekHQ local API commands for pilot assignment, unassignment, and swaps using MekHQ-owned validation and mutation logic.

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

- MekHQ source endpoint(s) for `units.assign_pilot`, `units.unassign_pilot`, and `units.swap_pilots`.
- Dry-run and apply behavior with structured refusal responses.
- Updated readiness rows, docs, tests, and fixtures where practical.

## Files And Areas

Likely files to read or edit:

- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCommandReadinessExporter.java`
- source owners identified by issue `#71`
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

- Do not assign pilots by display name or table row.
- Do not bypass MekHQ assignment validation.
- Do not auto-answer arbitrary dialogs.
- Do not save unless `saveAfterSuccess=true` is explicitly supported and requested.

## Acceptance Criteria

- Dry-run validates without mutation.
- Apply mode updates MekHQ-owned unit/person assignment state.
- Invalid role, unavailable person, stale guards, duplicate assignment, and replacement-without-policy are refused with structured reason codes.
- Post-command `GET /campaign/state` exposes the updated assignment.
- A negative API request is followed by `/status` or `GET /campaign/commands` to prove the local server remains available.

## Open Questions

- Is `replaceExistingPilot` allowed in V1, or should replacement require `units.swap_pilots`?
- How should multi-crew units be represented in V1 selectors and commands?
