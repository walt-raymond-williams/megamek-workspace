# Agent Handoff

## Issue

- GitHub issue: `#99`
- Roadmap entry: `Add live-play gap API docs, fixtures, regression tests, and smoke checklist`
- Priority: `High`

## Goal

Update API docs, sanitized fixtures, regression tests, and a live smoke checklist for the implemented MEK-RPG live-play gap endpoints.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_PLAY_API_GAP_PRODUCER_PLAN.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_RELIABILITY_SMOKE_CHECKLIST.md`
- Implementation notes from the child issues in this epic.

## Expected Output

- Updated consumer-facing contract docs.
- Sanitized fixtures for scenario intel, bounded ledgers/history, inventory/transport, and personnel turnover as implemented.
- Regression tests for bounded responses, filters, unsupported markers, and partial-response behavior.
- Disposable-campaign live smoke checklist or completed smoke result.

## Files And Areas

Likely files to read or edit:

- `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`
- `docs/current/MEK_RPG_LIVE_PLAY_API_GAP_PRODUCER_PLAN.md`
- `docs/templates/`
- `external/src/mekhq/MekHQ/src/test/`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
.\gradlew.bat --no-daemon :MekHQ:compileJava :MekHQ:checkstyleMain :MekHQ:checkstyleTest
```

## Constraints

- Fixtures must be sanitized and should not contain active campaign private data.
- Do not weaken the local-only/read-only safety contract.
- Smoke checks must use copied/disposable campaigns when mutation is not involved and must not parse active saves as the normal workaround.

## Acceptance Criteria

- MEK-RPG has concrete payload examples for every implemented gap area.
- Unsupported/unknown/withheld cases are documented with examples.
- Tests or smoke steps prove bounded reads and partial-response behavior.
- Roadmap and task board reflect the final state.

## Open Questions

- Which disposable campaign should provide the richest fixture/smoke evidence for scenario intel and logistics?
