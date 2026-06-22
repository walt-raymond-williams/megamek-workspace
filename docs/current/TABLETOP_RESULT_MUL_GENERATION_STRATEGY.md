# Tabletop Result MUL Generation Strategy

This decision note closes GitHub issue `#11`. It compares MekHQ's built-in manual result workflow against custom generation options for feeding physical tabletop battle results back into MekHQ.

Evidence labels follow `DOCUMENTATION_WORKFLOW.md`.

## Decision

Use MekHQ's built-in Resolve Manually workflow as the baseline path, and only add custom generation as a workspace helper when manual result entry or manual battle-record editing becomes too slow or error-prone.

If custom generation proceeds, narrow issue `#12` to a workspace experimental helper that uses the installed MekHQ `0.51.00` MegaMek/MekHQ jars and native MegaMek serialization/parser APIs. Do not implement a MekHQ source feature yet, and do not hand-write battle-record XML except for small, review-only diagnostics.

Do not create a feature integration branch or feature tracking doc yet. Keep work on `master` as small workspace commits unless issue `#12` expands into source changes, a multi-commit tool, or a reviewed integration branch.

## Why

- `Confirmed from source`: MekHQ already imports battle results through Resolve Manually by selecting a battle-record `<record>` MUL, then applying battlefield control, scenario status, salvage, BLC, personnel, and report choices through its own wizard. See `TABLETOP_RESULT_MUL_WORKFLOW.md`.
- `Confirmed from generated MUL`: a real MegaMek-generated battle-record file from the shakedown has the expected `<record>` sections and preserves player campaign unit UUIDs in entity `externalId`. See `GENERATED_BATTLE_RECORD_MUL_STUDY.md`.
- `Confirmed locally`: the installed jars can write a controlled battle-record file with MegaMek's native entity serialization and parse it back with `MULParser`, preserving result buckets, entity external id, crew external id, and a pilot hit. This was re-run successfully on `2026-06-22`.
- `Blocked`: live MekHQ Resolve Manually import into a disposable campaign remains blocked on user-operated UI validation or a fixed Windows Computer Use helper. This blocks end-to-end confidence, but not the strategy decision.
- `Confirmed locally`: JDK 21 is active in the shell and the installed `MekHQ-0.51.00` suite is present.
- `Confirmed locally`: source Gradle builds remain blocked by the Java 17 daemon/toolchain issue recorded in `KNOWN_COMMANDS.md`, so a MekHQ source feature has higher verification cost right now.

## Options Compared

| Option | Fit | Pros | Cons | Decision |
| --- | --- | --- | --- | --- |
| Built-in MekHQ Resolve Manually with operator-entered results | Good baseline | No code, uses MekHQ's own prompts and closeout logic, safest for first real campaign | Manual damage/casualty transcription can be slow; exact repair/salvage state is easy to miss | Use first |
| Manual edit/review of a MegaMek battle-record MUL | Situational | Uses a real `<record>` shape and native entity XML | Entity XML is broad; hand edits can break ids, crew state, ammo, crits, transport, or section buckets | Use only for diagnosis/review |
| Workspace Java helper using installed jars | Best first custom path | Uses `EntityListFile`/`MULParser`, avoids source checkout edits, already proven feasible, version-tied to local install | Still needs careful schema mapping and live MekHQ import validation | Narrow `#12` to this if generation is needed |
| MekHQ source feature | Future candidate | Best long-term UX if upstream/source-owned workflow is desired | Gradle verification blocked; UI/product design needed; higher maintenance and review cost | Defer |
| Hand-written XML generator in PowerShell/Python | Poor | Quick for tiny cases | High risk because entity serialization is complex and source-owned | Do not use |
| MEK-RPG or external app owns generation | Not first | Could integrate with narrative campaign memory later | Adds cross-repo coupling before local workflow is proven | Defer |

## Recommended First Workflow

For the next real tabletop session or disposable validation:

1. Export setup MULs from MekHQ's Briefing tab.
2. Play the tabletop battle.
3. Capture the summary-level result fields from `TABLETOP_RESULT_INPUT_SCHEMA.md`: scenario outcome, battlefield-control recommendation, player unit end states, crew hits/ejections, enemy wrecks, retreated/devastated units, and kill credit.
4. Resolve in MekHQ using the built-in Resolve Manually workflow when possible.
5. If a full battle-record `<record>` MUL is available from MegaMek, use that for import validation; do not use `logs/salvage.mul`.
6. Record exact UI steps and observed campaign effects in issue `#10` or `#13`.

This path may be enough for early campaign play if the user can tolerate manual review/transcription.

## Narrowed Issue #12 Scope

If issue `#12` proceeds, implement a small workspace helper rather than a MekHQ source feature.

Recommended location:

```text
tools/tabletop-result-mul-helper/
```

Recommended shape:

- Input: a copied setup MUL or selected entity source plus a structured tabletop result YAML/JSON that starts with the `summary` and `repair` fields from `TABLETOP_RESULT_INPUT_SCHEMA.md`.
- Core: load or construct MegaMek `Entity` objects with preserved campaign unit `externalId` and crew external ids.
- Mapping: assign entities to battle-record buckets `survivors`, `allies`, `salvage`, `retreated`, and `devastated`; add `kills` rows where known.
- Output: battle-record `<record>` MUL written with MegaMek native serialization.
- Validation: parse the generated file back with `MULParser` and assert bucket counts, required player unit UUIDs, crew UUIDs, crew hits, and kill rows before the file is offered for MekHQ import.
- Boundary: do not decide final battlefield control, final scenario status, salvage allocation, BLC, or CamOps recovery. Those remain MekHQ/operator workflow choices.

Minimum implementation gates:

- preserve campaign `Unit` UUIDs in player entity `externalId`
- preserve campaign `Person` UUIDs in crew external ids when known
- fail clearly for unsupported entity classes or damage fields
- refuse to overwrite campaign saves
- write generated outputs under `analysis/` or an explicit user-selected path

## Validation Hooks

Required before trusting generated MULs:

1. Compile/run helper against the installed jars with JDK 21.
2. Parse generated output with `MULParser`.
3. Assert the expected `<record>` buckets and kill rows.
4. Assert external ids for every deployed player campaign unit.
5. For units with crew detail, assert crew external ids and hits/ejection state.
6. Manually import only into a disposable campaign until issue `#10` or `#13` records a successful live MekHQ closeout.

Repeatable current proof:

```powershell
cd C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
javac -cp 'MegaMek.jar;MekHQ.jar;lib/*' 'C:\Users\waltr\Documents\megamek-workspace\analysis\tmp\Issue10BattleRecordRoundTrip.java'
java -cp 'MegaMek.jar;MekHQ.jar;lib/*;C:\Users\waltr\Documents\megamek-workspace\analysis\tmp' Issue10BattleRecordRoundTrip 'C:\Users\waltr\Documents\megamek-workspace\analysis\tmp\issue-10-battle-record-round-trip.mul'
```

Observed on `2026-06-22`: `survivors=1`, `salvage=1`, `retreated=1`, `devastated=1`, `kills=2`, survivor entity external id preserved, survivor crew external id preserved, and survivor crew hits preserved.

## Branch And Tracking Decision

No feature integration branch now.

Create `codex/tabletop-result-mul-dev` and a compact feature tracking doc only if one of these becomes true:

- issue `#12` modifies MegaMek/MekHQ source
- the helper grows beyond a small workspace tool and needs multiple coordinated commits
- issue `#12` and `#13` are worked together as one integrated feature branch
- a PR into `master` needs human review before merging a larger implementation

## Follow-Up Decisions

- Issue `#12`: proceed only as a narrowed workspace helper if the user wants generated battle-record MULs before live MekHQ import validation is complete. Otherwise, comment and close `#12` as unnecessary for the first campaign.
- Issue `#10`: remains blocked on live Resolve Manually import validation, preferably after user task `#23` creates a realistic disposable campaign target.
- Issue `#13`: should document the built-in manual workflow either way, including exact UI steps, battlefield-control prompt, scenario status choice, salvage dialogs, and post-closeout checks.

## Source References

- `docs/current/TABLETOP_RESULT_MUL_WORKFLOW.md`
- `docs/current/TABLETOP_RESULT_INPUT_SCHEMA.md`
- `docs/current/BATTLE_RECORD_MUL_ROUND_TRIP_VALIDATION.md`
- `docs/current/GENERATED_BATTLE_RECORD_MUL_STUDY.md`
- `docs/current/SALVAGE_RULES_NOTES.md`
- `megamek.common.units.EntityListFile`
- `megamek.common.loaders.MULParser`
- `mekhq.campaign.ResolveScenarioTracker`
- `mekhq.MekHQ#resolveScenario`
