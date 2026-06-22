# Agent Handoff: Implement Tabletop Battle-Record MUL Generator

## Issue

- GitHub issue: `#12`
- Roadmap entry: `Epic: Robust tabletop battle result MUL workflow`
- Priority: `High`

## Result

Closed on `2026-06-22` as unnecessary for the first campaign.

Reason:

- Strategy issue `#11` selected MekHQ's built-in Resolve Manually workflow as the baseline.
- The strategy preserved a future custom path as a workspace installed-jar helper, but did not authorize building it now.
- Live MekHQ Resolve Manually import validation remains blocked through issue `#10`.
- User task `#23` should create the realistic campaign target before destructive/import validation resumes.
- Building a generator before the built-in/manual workflow is documented and tried would add tooling before proving the need.

Future reopen path:

- Reopen this issue or create a new narrowed issue only if manual workflow is too slow or error-prone.
- Keep the helper in this workspace, likely under `tools/tabletop-result-mul-helper/`.
- Use installed MegaMek/MekHQ jars and native `EntityListFile`/`MULParser` behavior.
- Preserve campaign unit UUIDs and crew external ids.
- Validate generated output by parsing it before any MekHQ import.
- Do not implement a MekHQ source feature unless a later source-change issue justifies it.

See:

- `docs/current/TABLETOP_RESULT_MUL_GENERATION_STRATEGY.md`
- `docs/current/BATTLE_RECORD_MUL_ROUND_TRIP_VALIDATION.md`
- `docs/current/TABLETOP_RESULT_INPUT_SCHEMA.md`
- `docs/current/TABLETOP_RESULT_MUL_WORKFLOW.md`
