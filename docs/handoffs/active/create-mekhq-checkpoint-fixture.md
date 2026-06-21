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

- A sanitized JSON fixture, likely `docs/templates/mekhq-read-only-checkpoint.fixture.json`.
- Fixture notes or schema updates if a field shape needs clarification.
- Roadmap/task updates and commit.

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

- Fixture matches the MEK-RPG issue `#67` top-level consumer shape: `bridge_metadata`, `campaign`, `finances`, `personnel`, `units`, `contracts`, `scenarios`, `repairs_and_logistics`, `markets`, `reports`, and `unsupported`.
- Fixture includes fake but realistic MekHQ UUIDs/integer ids and display names.
- Fixture includes method-backed examples for finances, personnel salary, unit damage state, unit market final price, and a sanitized report line.
- Fixture includes unsupported/warning examples for missing unit-market stable offer id and cargo/transport validation.
- Fixture is linked from the schema or related current doc.

## Open Questions

- Should the fixture include duplicate unit-market offers to test selector warnings?
- Should the fixture include both active contract and contract-market offer examples, or keep the first fixture compact?
