# Fixed OPFOR MUL Pool Workflow

This note records the prototype workflow for GitHub issue `#18`: creating fixed OPFOR MUL pools that can be loaded into MekHQ bot formations for physical-miniatures play.

Evidence labels follow `DOCUMENTATION_WORKFLOW.md`.

## Status

- `Confirmed from source`: `CustomizeBotForceDialog.loadUnits(...)` opens a unit file with `FileDialogs.openUnits(...)`, parses it through `new MULParser(file, campaign.getGameOptions())`, and stores `parser.getEntities()` as the bot force's fixed entities.
- `Confirmed from source`: `CustomizeBotForceDialog.saveUnits(...)` writes fixed bot entities with `EntityListFile.saveTo(file, fixedEntities)`.
- `Confirmed from source`: `EntityListFile.saveTo(File, ArrayList<Entity>)` writes an ordinary top-level `<unit version="...">` MUL file, not a battle-record `<record>` MUL.
- `Confirmed from source`: `MULParser` resolves each `<entity chassis="..." model="...">` through `MekSummaryCache`, then loads the matching `.mtf` or `.blk` unit through `MekFileParser` unless the MUL embeds construction data.
- `Confirmed locally`: a local scratch Java probe using the installed MekHQ/MegaMek `0.51.00` jars generated a placeholder three-unit OPFOR MUL with `EntityListFile.saveTo(...)` and parsed it back with `MULParser` as three entities.
- `Unknown`: the user's real physical miniature inventory is not available yet, so no committed MUL should claim to represent actual owned miniatures.

## What A Fixed OPFOR Pool Is

A fixed OPFOR pool is a normal MegaMek setup MUL that lists exact units for a bot force. In MekHQ's scenario editor, loading that MUL into a bot formation replaces the generated/random fixed-entity list for that bot force with the entities parsed from the file.

This is separate from tabletop result import:

- Fixed OPFOR pool: top-level `<unit>`, used before a battle to define what units appear.
- Battle result file: top-level `<record>`, used after a battle by MekHQ's Resolve Manually workflow.

Do not reuse a battle-record MUL as a fixed OPFOR pool, and do not expect a setup MUL to carry post-battle casualty/salvage sections.

## Recommended File Layout

Use real inventory here after the miniature list exists:

```text
campaigns/miniatures/physical-miniature-roster.csv
campaigns/miniatures/opfor-pools/<pool-id>.mul
campaigns/miniatures/opfor-pools/<pool-id>.notes.md
```

Use templates and scratch paths before real inventory exists:

```text
docs/templates/OPFOR_MUL_POOL_MANIFEST.csv
analysis/tmp/issue-18-placeholder-opfor-light-medium-heavy.mul
```

The `analysis/tmp/` MUL is deliberately ignored by git. It is useful proof that installed MegaMek serialization works, but it is generated from full unit definitions and should not be treated as user inventory or committed as campaign data.

## Pool Manifest

Track planned pools in a small CSV before generating files. Use:

```text
docs/templates/OPFOR_MUL_POOL_MANIFEST.csv
```

Recommended columns:

| Column | Meaning |
| --- | --- |
| `pool_id` | Stable id such as `opfor-3025-light-raiders`. |
| `pool_status` | `template`, `draft`, `confirmed`, `retired`, or `blocked`. |
| `pool_name` | Human-readable name shown in notes. |
| `intended_use` | Scenario role such as `light raiders`, `training fight`, `heavy lance`, or `vehicle screen`. |
| `era` | Intended campaign era or date band. |
| `faction_flavor` | Player-facing flavor, not necessarily a MekHQ faction key. |
| `bv_min` / `bv_max` | Approximate total BV band for selecting the pool. |
| `unit_count` | Number of entities expected in the generated MUL. |
| `source_roster_filter` | How rows are selected from the physical miniature roster. |
| `unit_lookup_names` | Semicolon-separated MekHQ/MegaMek lookup names. |
| `output_mul_path` | Where the generated or saved MUL should live. |
| `verification_status` | `not_generated`, `generated`, `parser_verified`, `ui_loaded`, or `blocked`. |
| `notes` | Constraints, proxy allowances, or unresolved questions. |

## Prototype Pool Shape

Until real inventory exists, use clearly marked placeholder units only. The local scratch prototype used:

| Unit | Role | Why it is useful for the prototype |
| --- | --- | --- |
| `Locust LCT-1V` | scout/harasser | Confirms a light unit resolves from `MekSummaryCache`. |
| `Wolverine WVR-6R` | medium skirmisher | Confirms a common medium unit resolves and writes ammo/location state. |
| `Thunderbolt TDR-5S` | heavy anchor | Confirms a heavier unit resolves and writes multiple ammo bins. |

These are examples only. They are not confirmed user-owned miniatures.

## Generation Path

For a no-source-change workflow:

1. Filter `campaigns/miniatures/physical-miniature-roster.csv` to rows where `inventory_status=confirmed` and `opfor_use=yes`.
2. Choose a small pool by role and BV band, using exact variants first and explicitly accepted proxies second.
3. Search each `mekhq_lookup_name` in MekHQ/MegaMek to confirm the unit resolves.
4. Generate or save an ordinary setup MUL with `EntityListFile.saveTo(...)`.
5. Parse the generated MUL with `MULParser` and confirm the entity count and warnings.
6. Load the MUL into a scenario bot formation through MekHQ's scenario editor.
7. Save the campaign under a deliberate name after verifying the bot formation.

When using the UI only, step 4 can be done inside `CustomizeBotForceDialog` by adding/selecting fixed entities and pressing its save-units button. When using a helper tool later, prefer calling MegaMek classes rather than hand-writing entity XML.

## MekHQ Load Path

Source-confirmed path:

1. Open a generated scenario.
2. Open scenario edit/customization.
3. Go to the bot force / other force editor.
4. Open the bot formation in `CustomizeBotForceDialog`.
5. Use the load-units action and select a setup `.mul`.
6. MekHQ parses it with `MULParser(file, campaign.getGameOptions())`.
7. MekHQ stores `parser.getEntities()` as the bot force's fixed entities.

Live UI click-through has not been performed for this issue. The parser and writer path was verified locally against installed jars; the visual UI load should be included in the next hands-on MekHQ shakedown or when issue `#17` gets its user-operated UI pass.

## Local Verification

`Confirmed locally`: from the installed suite directory:

```powershell
& 'C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot\bin\javac.exe' -cp 'MegaMek.jar;MekHQ.jar;lib/*' -d 'C:\Users\waltr\Documents\megamek-workspace\analysis\tmp' 'C:\Users\waltr\Documents\megamek-workspace\analysis\tmp\Issue18MulPrototype.java'
& 'C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot\bin\java.exe' -cp 'C:\Users\waltr\Documents\megamek-workspace\analysis\tmp;MegaMek.jar;MekHQ.jar;lib/*' Issue18MulPrototype 'C:\Users\waltr\Documents\megamek-workspace\analysis\tmp\issue-18-placeholder-opfor-light-medium-heavy.mul'
```

Result:

```text
wrote=C:\Users\waltr\Documents\megamek-workspace\analysis\tmp\issue-18-placeholder-opfor-light-medium-heavy.mul
entities=3
```

The scratch probe is intentionally not committed. The durable point is that the installed jars can generate and parse a setup MUL using the same classes MekHQ uses.

## Recommendation

For the first physical-miniatures campaign, use fixed OPFOR MUL pools as a manual-control layer:

1. Let MekHQ generate the mission, map context, bot count, and rough BV.
2. Pick a fixed pool from confirmed physical inventory that is close to the generated difficulty.
3. Load the pool into the bot force.
4. Adjust only when the physical table requires it.

Do not build custom RATs until after the real miniature roster exists and one or two fixed-pool sessions show where manual substitution is painful. That keeps the first workflow simple enough to run at the table.

## Source References

- `mekhq.gui.dialog.CustomizeBotForceDialog#loadUnits`
- `mekhq.gui.dialog.CustomizeBotForceDialog#saveUnits`
- `megamek.common.loaders.MULParser`
- `megamek.common.units.EntityListFile#saveTo(File, ArrayList<Entity>)`
- `megamek.common.loaders.MekSummaryCache`
- `megamek.common.loaders.MekFileParser`
