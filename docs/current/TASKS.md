# Tasks

This is the lightweight task board for the MegaMek workspace. Keep it current when work starts, finishes, gets blocked, or changes priority.

## How To Use This Board

- `Now`: active work that should be handled before starting unrelated expansion.
- `Next`: queued work with clear value and a likely near-term order.
- `Backlog`: useful work that is not yet ready or not yet urgent.
- `Blocked`: work waiting on user input, missing files, source confirmation, or an external state change.
- `Done`: recently completed work worth preserving for continuity.

Keep task titles short. Add enough context that a future agent can resume without rereading the whole chat.

## Now

- None.

## Next

1. Inspect the active campaign save without modifying it.
   - Goal: Extract a factual campaign snapshot from `The Learning Ropes.cpnx.gz`.
   - Output: Update `ACTIVE_CAMPAIGN.md`, `SAVE_FORMAT_NOTES.md`, and a first campaign status report.

2. Identify MekHQ save/load source classes.
   - Goal: Confirm how `.cpnx.gz` files are loaded and saved in the local MekHQ source.
   - Output: Update `SAVE_FORMAT_NOTES.md` and `SOURCE_CODE_GUIDE.md`.

3. Produce the first campaign status report.
   - Goal: Practice the full campaign-analysis workflow on the active sample campaign.
   - Output: A report under `campaigns/learning-ropes/reports/`.

## Backlog

- Decide a report naming convention for campaign reports.
- Decide whether generated parser outputs should live under `analysis/generated/` by campaign name.
- Build a repeatable campaign summary extraction script after save structure is confirmed.
- Create source-reference notes for common MekHQ campaign actions and UI buttons.

## Blocked

- None.

## Done

- `2026-06-18`: Added lightweight task tracking in `TASKS.md` and made it part of the documentation workflow.
- `2026-06-18`: Marked `The Learning Ropes.cpnx.gz` as the active practice campaign in `ACTIVE_CAMPAIGN.md`.
- `2026-06-18`: Established the initial workspace documentation baseline in commit `e858543`.

## Update Rules

- Update this file before switching from one major workstream to another.
- Move completed tasks to `Done` with the date and relevant commit if available.
- Move blocked tasks to `Blocked` with the exact missing input or condition.
- Keep durable findings in the relevant `docs/current/` file; this board tracks work, not detailed research.
