# Agent Handoff: Implement Tabletop Battle-Record MUL Generator

## Issue

- GitHub issue: `#12`
- Roadmap entry: `Epic: Robust tabletop battle result MUL workflow`
- Priority: `High`

## Goal

Issue `#11` completed on `2026-06-22`. It did not authorize a MekHQ source feature or broad implementation. If custom generation is still needed, implement the first generator as a narrowed workspace experimental helper that converts structured tabletop battle results into MekHQ-compatible battle-record MUL files using installed MegaMek/MekHQ jars and native serialization/parser APIs.

If MekHQ's built-in manual result-entry/import workflow is sufficient for the first campaign, do not start this implementation. Comment and close this issue as unnecessary for now, then route remaining work to documentation/verification issue `#13`.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/KNOWN_COMMANDS.md`
- `docs/current/TABLETOP_RESULT_MUL_GENERATION_STRATEGY.md`
- `docs/handoffs/active/robust-tabletop-battle-result-mul-workflow.md`
- `docs/handoffs/archive/choose-tabletop-result-mul-generation-strategy.md`
- `docs/handoffs/archive/define-tabletop-result-input-schema.md`
- `docs/handoffs/active/prototype-battle-record-mul-round-trip.md`

## Expected Output

- Generator code in the agreed location.
- Example input and output artifacts for a disposable scenario.
- Validation command or procedure that catches malformed or incompatible battle-record MULs before import.
- Updates to `docs/current/KNOWN_COMMANDS.md` if repeatable commands are established.

## Files And Areas

- Recommended first location if this proceeds: `tools/tabletop-result-mul-helper/`.
- `analysis/` for disposable examples.
- `docs/current/KNOWN_COMMANDS.md`

## Commands

```powershell
git status --short --branch
```

Add implementation-specific commands after `#11` records the strategy.

## Constraints

- Start only if custom generation is still explicitly desired after reading `TABLETOP_RESULT_MUL_GENERATION_STRATEGY.md`.
- Do not modify MegaMek/MekHQ source for this issue.
- Use MegaMek/MekHQ structured APIs where practical instead of hand-rolled XML.
- Follow `docs/current/SOURCE_CHANGE_WORKFLOW.md` before modifying any source checkout.

## Acceptance Criteria

- The generator produces a battle-record MUL accepted by the validation path.
- The implementation handles the agreed minimal schema and fails clearly for unsupported fields.
- Tests or repeatable validation steps are documented and run, or blockers are recorded.

## Open Questions

- Is built-in MekHQ workflow plus documentation enough for the first campaign, making this issue unnecessary for now?
- If implementation is needed, can the helper stay as a small workspace installed-jar tool without a feature branch?
- What validation should be mandatory before a generated MUL is loaded into MekHQ?
