# Agent Handoff

## Issue

- GitHub issue: `#45`
- Roadmap entry: `Define guarded live MekHQ command envelope and prompt policy`
- Priority: `High`
- Parent epic: `#44`

## Goal

Define the common request/response contract for all future mutating MekHQ local API commands.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/MEKHQ_ADVANCE_DAY_CONTROL_API_PROTOTYPE.md`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`

## Expected Output

- Command envelope fields for command name/version, idempotency key, expected campaign identity/date, target guard fields, dry-run, save policy, and MEK-RPG audit context.
- Response shape for applied/refused/blocked/failed/dry-run results.
- Prompt/dialog policy for commands that would otherwise show UI.
- Reusable acceptance criteria for child command implementation issues.

## Files And Areas

Likely files to read or edit:

- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`

## Constraints

- Keep the API disabled by default and loopback-only.
- Do not make broad implementation changes in this issue unless explicitly split into an implementation issue.
- Preserve Advance Day's guard-field lessons.

## Acceptance Criteria

- Future command issues can reuse the envelope without redefining safety policy.
- Retry/idempotency and dry-run semantics are explicit.
- Save-after-success behavior remains explicit and opt-in while prototyping.

## Open Questions

- Should idempotency keys be remembered only in memory or persisted in campaign reports/metadata?
- Should dry-run be mandatory for every command or best-effort per command family?
