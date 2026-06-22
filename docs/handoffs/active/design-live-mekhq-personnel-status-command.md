# Agent Handoff

## Issue

- GitHub issue: `#47`
- Roadmap entry: `Design live MekHQ personnel death and status command API`
- Priority: `High`
- Parent epic: `#44`

## Goal

Design a guarded command for MEK-RPG events that change a MekHQ person's status, including death, disappearance, capture, retirement, or recovery outside MekHQ tactical resolution.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/TABLETOP_RESULT_MUL_WORKFLOW.md`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/Campaign.java`
- MekHQ personnel source around `Person#changeStatus(...)` and `PersonnelStatus`.

## Expected Output

- Source-backed map of safe and unsafe personnel status transitions.
- Proposed endpoint shape, likely `POST /campaign/command/personnel/status`.
- Guard fields for person id, expected name/status, current assignment, date, and RPG event source.
- Refusal rules for tactical deaths that should go through scenario resolution instead.

## Files And Areas

Likely files to read or edit:

- `external/src/mekhq/MekHQ/src/mekhq/campaign/personnel/Person.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/personnel/enums/PersonnelStatus.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/Campaign.java`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`

## Constraints

- Do not bypass MekHQ status history, reporting, unit crew cleanup, or dependent systems.
- Do not conflate RPG narrative death with scenario-result casualties when a MekHQ scenario should own the resolution.

## Acceptance Criteria

- The design says exactly which status changes can be automated first.
- The design identifies side effects to verify after mutation: roster status, unit crew links, reports, payroll/availability, and campaign state.
- Follow-up implementation issue is narrow and testable on disposable data.

## Open Questions

- What is the correct MekHQ status for each MEK-RPG outcome: dead, captured, missing, retired, defected, medically unavailable, or recovered?
- Should a personnel status command also add a campaign report/note by default?
