# Agent Handoff: Choose Tabletop Result MUL Generation Strategy

## Issue

- GitHub issue: `#11`
- Roadmap entry: `Epic: Robust tabletop battle result MUL workflow`
- Priority: `High`

## Result

Completed on `2026-06-22`.

Decision note:

- `docs/current/TABLETOP_RESULT_MUL_GENERATION_STRATEGY.md`

Decision:

- Use MekHQ's built-in Resolve Manually workflow as the baseline.
- If custom generation is needed, narrow issue `#12` to a workspace experimental Java helper that uses installed MegaMek/MekHQ jars and native MegaMek serialization/parser APIs.
- Do not implement a MekHQ source feature yet.
- Do not hand-write broad battle-record XML.
- Do not create a feature integration branch or feature tracking doc yet.

Verification:

- Re-ran the issue `#10` installed-jar writer/parser proof on `2026-06-22`.
- Observed expected counts for `survivors`, `salvage`, `retreated`, `devastated`, and `kills`.
- Observed survivor entity external id, crew external id, and crew hits preserved.
- Confirmed JDK 21 is active and the installed MekHQ executable exists.

Updated:

- `docs/current/TABLETOP_RESULT_MUL_GENERATION_STRATEGY.md`
- `docs/current/KNOWN_COMMANDS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/handoffs/archive/implement-tabletop-battle-record-mul-generator.md`

## Remaining Notes

Issue `#10` remains blocked on live MekHQ Resolve Manually import validation. Issue `#12` was closed as unnecessary for the first campaign; reopen only if built-in/manual workflow proves too slow.
