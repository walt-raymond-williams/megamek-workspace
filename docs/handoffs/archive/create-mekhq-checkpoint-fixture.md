# Agent Handoff

## Issue

- GitHub issue: `#27`
- Roadmap entry: `Create sanitized MekHQ checkpoint export fixture`
- Priority: `High`

## Goal

Create a sanitized read-only MekHQ checkpoint export fixture that MEK-RPG can use for adapter/bootstrap tests before a real MekHQ exporter exists.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT_SCHEMA.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_READ_ONLY_CHECKPOINT_EXPORT_CONTRACT.md`

## Expected Output

- Completed with sanitized JSON fixture `docs/templates/mekhq-read-only-checkpoint.fixture.json`.
- Linked from `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT_SCHEMA.md`.
- Roadmap/task updates completed with issue close-out.

## Files And Areas

Likely files to read or edit:

- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT_SCHEMA.md`
- `docs/templates/`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg "MEK_RPG_MEKHQ_CHECKPOINT_EXPORT_SCHEMA|mekhq-read-only-checkpoint" docs
```

## Constraints

- Do not include unrelated user changes.
- Preserve uncertainty and evidence labels.
- Do not use real user campaign names, secrets, raw MekHQ XML, raw save payloads, or copied rulebook text.
- Keep fixture read-only. Do not include executable write-command or pending-action application fields.
- Commit and push completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- Done: fixture matches the MEK-RPG issue `#67` top-level consumer shape: `bridge_metadata`, `campaign`, `finances`, `personnel`, `units`, `contracts`, `scenarios`, `repairs_and_logistics`, `markets`, `reports`, and `unsupported`.
- Done: fixture includes fake but realistic MekHQ UUIDs/integer ids and display names.
- Done: fixture includes method-backed examples for finances, personnel salary, unit damage state, unit market final price, and sanitized report lines.
- Done: fixture includes unsupported/warning examples for missing unit-market stable offer id and cargo/transport validation.
- Done: fixture is linked from the schema doc.

## Open Questions

- Resolved: duplicate-looking unit-market offers are included to test selector warnings.
- Resolved: fixture includes both an active contract and a contract-market offer example while staying compact.
