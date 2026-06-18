# Agent Handoff: Implement Tabletop Battle-Record MUL Generator

## Issue

- GitHub issue: `#12`
- Roadmap entry: `Epic: Robust tabletop battle result MUL workflow`
- Priority: `High`

## Goal

If issue `#11` confirms custom generation is necessary, implement the first robust generator that converts structured tabletop battle results into MekHQ-compatible battle-record MUL files.

If issue `#11` concludes MekHQ's built-in manual result-entry/import workflow is sufficient, do not start this implementation as written. Narrow or close this issue and route the remaining work to documentation/verification instead.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/KNOWN_COMMANDS.md`
- `docs/handoffs/active/robust-tabletop-battle-result-mul-workflow.md`
- `docs/handoffs/active/choose-tabletop-result-mul-generation-strategy.md`
- `docs/handoffs/archive/define-tabletop-result-input-schema.md`
- `docs/handoffs/active/prototype-battle-record-mul-round-trip.md`

## Expected Output

- Generator code in the agreed location.
- Example input and output artifacts for a disposable scenario.
- Validation command or procedure that catches malformed or incompatible battle-record MULs before import.
- Updates to `docs/current/KNOWN_COMMANDS.md` if repeatable commands are established.

## Files And Areas

- To be determined by issue `#11`.
- `analysis/` for disposable examples.
- `docs/current/KNOWN_COMMANDS.md`

## Commands

```powershell
git status --short --branch
```

Add implementation-specific commands after `#11` records the strategy.

## Constraints

- Start only after the generation strategy is chosen and custom implementation is explicitly confirmed.
- Use MegaMek/MekHQ structured APIs where practical instead of hand-rolled XML.
- Follow `docs/current/SOURCE_CHANGE_WORKFLOW.md` before modifying any source checkout.

## Acceptance Criteria

- The generator produces a battle-record MUL accepted by the validation path.
- The implementation handles the agreed minimal schema and fails clearly for unsupported fields.
- Tests or repeatable validation steps are documented and run, or blockers are recorded.

## Open Questions

- Will `#11` confirm this implementation is needed, or will it recommend built-in MekHQ workflow plus documentation?
- If implementation is needed, which implementation location and branch will `#11` select?
- What validation should be mandatory before a generated MUL is loaded into MekHQ?
