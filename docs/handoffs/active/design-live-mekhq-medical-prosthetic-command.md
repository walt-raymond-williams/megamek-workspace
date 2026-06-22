# Agent Handoff

## Issue

- GitHub issue: `#48`
- Roadmap entry: `Design live MekHQ medical treatment and prosthetic command API`
- Priority: `High`
- Parent epic: `#44`

## Goal

Design guarded commands for RPG-side medical events that should update MekHQ injury, recovery, prosthetic, fatigue, or medical expense state.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- MekHQ source under `campaign/personnel/medical`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/personnel/Person.java`

## Expected Output

- Source-backed map of MekHQ injury, healing, prosthetic, and fatigue APIs.
- Clear separation of simple status/fatigue updates, structured injury treatment, prosthetic application, and medical cost handling.
- Proposed endpoint shape, likely `POST /campaign/command/personnel/medical-treatment`.
- Refusal rules for ambiguous or option-dependent medical changes.

## Files And Areas

Likely files to read or edit:

- `external/src/mekhq/MekHQ/src/mekhq/campaign/personnel/medical/`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/personnel/Person.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/personnel/enums/PersonnelStatus.java`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`

## Constraints

- Do not model prosthetics as a plain note if MekHQ has structured medical state that should own it.
- Do not bypass campaign medical options or recovery rules.
- Do not implement before source confirms exact APIs.

## Acceptance Criteria

- The design identifies the lowest-risk medical mutation that can be automated first.
- The design lists verification facts: injury list/status, prosthetic state, medical reports, expenses, fatigue/hits, and availability.
- Follow-up implementation issue is narrow and disposable-campaign testable.

## Open Questions

- How does MekHQ represent prosthetics in the active rules/options?
- Which RPG-side medical events should be tracked as structured MekHQ medical state versus narrative notes?
