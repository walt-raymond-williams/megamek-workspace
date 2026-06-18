# Custom RAT Strategy For Physical-Miniature OPFOR

This note records the recommendation for GitHub issue `#19`: whether custom random assignment tables are worth using for physical-miniature OPFOR control.

Evidence labels follow `DOCUMENTATION_WORKFLOW.md`.

## Recommendation

Use fixed OPFOR setup-MUL pools first. Do not build custom RATs for the first playable parent-run physical-miniatures campaign.

Custom generation becomes worth revisiting only after three things are true:

1. The physical-miniature inventory exists as confirmed data.
2. The collection is large enough that manual pool selection is annoying rather than helpful.
3. The campaign regularly needs MekHQ to generate OPFOR automatically from owned-mini constraints instead of letting the GM substitute close-BV physical units.

Until then, custom RATs add more moving parts than they remove.

## Decision Matrix

| Path | Best use | Cost | Recommendation |
| --- | --- | --- | --- |
| Regenerate bot forces | A generated OPFOR is close but one draw is bad. | Very low. | Use freely during play. |
| Manual substitution | The generated OPFOR is mechanically fine but the table needs physical stand-ins. | Low. | Good default for a small collection. |
| Fixed OPFOR setup-MUL pools | The GM wants exact known units for tonight's scenario. | Medium. | Preferred first controlled workflow; documented in `FIXED_OPFOR_MUL_POOL_WORKFLOW.md`. |
| Custom classic `.txt` RATs | The user wants selectable old-style RAT tables by source/category. | Medium-high, and current MekHQ OPFOR path needs UI validation. | Defer. |
| Custom modern force-generator data | The user wants MekHQ's automated OPFOR generator itself constrained by owned minis. | High. | Consider only after real inventory and repeated play prove fixed pools are too manual. |
| MekHQ source changes | The user needs first-class physical-inventory-aware generation in the app. | Very high. | Not justified yet. |

## Why Fixed MUL Pools Win First

`Confirmed from source`: MekHQ bot formations can load exact setup MULs through `CustomizeBotForceDialog`, which parses entities with `MULParser`. That path is narrow, easy to test, and does not require editing installed data directories or understanding all scenario-generation fallback logic.

`Confirmed locally`: issue `#18` generated a placeholder setup MUL with installed MekHQ/MegaMek `0.51.00` jars and parsed it back as three entities.

`Inferred`: for a parent-run game with a small-to-medium physical collection, the GM will usually want control at the point of play: "this mission needs fast raiders" or "we have these three minis painted and ready." Fixed pools match that human workflow better than a global random generator.

## What The Installed Custom-RAT Docs Say

`Confirmed from local docs`: `external/installs/MekHQ-0.51.00/docs/StratCon/Custom RATs.txt` says custom AtB RATs require:

- RAT files under `data/rat`
- metadata XML under `data/universe/ratdata`
- one metadata file per collection
- unique collection `source` names
- one metadata entry for each RAT table name that should be available
- faction keys, unit types, weight classes, and equipment ratings to match against generation criteria

`Confirmed from local data`: the installed suite has `data/rat/rat_default.zip` and many metadata files under `data/universe/ratdata/*.xml`.

This means even a small custom classic RAT collection needs two coordinated artifacts: the actual weighted table and the metadata that tells MekHQ when to use it.

## Source Findings

`Confirmed from source`: `megamek.client.generator.RandomUnitGenerator` is the classic text-RAT loader. It loads `.txt` files and `.zip` files from `Configuration.armyTablesDir()`, which defaults to `data/rat`. The first non-comment line is the RAT name. Later rows are `unit name,weight`; entries beginning with `@` reference another RAT.

`Confirmed from source`: `RandomUnitGenerator` validates that non-reference entries resolve in `MekSummaryCache`.

`Confirmed from source`: MekHQ campaign OPFOR generation uses `RATGeneratorConnector`, which wraps `megamek.client.ratgenerator.RATGenerator`, the modern dynamic RAT generator.

`Confirmed from source`: `RATGeneratorConnector` builds `UnitGeneratorParameters` from faction, unit type, weight class, year, quality, movement modes, and mission roles, then calls `UnitTable.findTable(...)` / `UnitTable.generateUnits(...)`.

`Confirmed from source`: `RATGenerator` initializes from `Configuration.forceGeneratorDir()`, which defaults to `data/forcegenerator`.

`Confirmed from local docs`: the dynamic RAT generator data under `data/forcegenerator` is much richer than a simple owned-mini table. It models faction hierarchy, rating levels, eras, chassis availability, model availability, salvage sources, weight distributions, movement modes, and mission roles.

`Unknown`: this issue did not live-click the Campaign Options RAT configuration UI. The installed custom-RAT document describes metadata under `data/universe/ratdata`, but source search in this workspace only found `MHQConstants.RAT_INFO_DIR` as a constant and did not find active Java references to `ratdata`. Treat the classic custom-RAT UI path as requiring manual UI validation in MekHQ `0.51.00` before relying on it.

## Minimum Inventory Data Needed

Do not attempt any custom generation path until these fields are populated for confirmed miniatures:

- `inventory_status=confirmed`
- `quantity`
- `chassis`
- `variant`
- `unit_type`
- `weight_class`
- `tech_base`
- `era_start_year`
- `faction_tags`
- `opfor_use`
- `proxy_policy`
- `bv`
- `bv_source`
- `mekhq_lookup_name`

For modern force-generator data, also decide:

- how physical faction tags map to MegaMek/MekHQ faction keys
- whether proxies count as the exact generated unit or only as GM substitutions
- whether availability weights should represent lore rarity, physical quantity, table-fun variety, or all three
- how to avoid generating units not physically available when scenario templates ask for a weight class or role that the collection cannot satisfy

## If We Later Build Classic RATs

Use this only if manual UI validation shows MekHQ `0.51.00` still uses the `data/rat` plus `data/universe/ratdata` path for the specific campaign generation option the user wants.

Proposed output:

```text
analysis/generated/miniatures/rat/<collection>.zip
analysis/generated/miniatures/ratdata/<collection>.xml
docs/current/CUSTOM_RAT_VERIFICATION.md
```

Acceptance criteria:

1. Generate `.txt` RAT files whose first non-comment line matches each metadata `<rat name='...'>`.
2. Include only `inventory_status=confirmed` rows, or clearly marked accepted proxies.
3. Validate every unit name through `MekSummaryCache`.
4. Copy the generated artifacts into a disposable install or `userdata` path only after confirming the correct load path.
5. Verify in MekHQ Campaign Options that the collection appears and can be selected.
6. Generate a disposable scenario and confirm the OPFOR draws only from the expected custom table.

## If We Later Build Modern Force-Generator Data

Use this only if the user wants MekHQ automated OPFOR generation constrained to owned miniatures across many scenarios.

Proposed output:

```text
analysis/generated/miniatures/forcegenerator/
docs/current/PHYSICAL_MINIATURE_FORCE_GENERATOR_DESIGN.md
```

Acceptance criteria:

1. Clone the minimal required `data/forcegenerator` structure into generated workspace output.
2. Add or override faction/year availability so confirmed owned units are available.
3. Preserve enough parent/fallback behavior that MekHQ still generates an OPFOR when a scenario asks for a nearby faction, rating, weight class, or mission role.
4. Run a Java verification probe through `RATGenerator.reloadFromDir(...)` or an equivalent installed-jar harness.
5. Generate sample OPFOR lists by faction, year, unit type, weight, and role.
6. Validate a disposable MekHQ scenario generation pass.

This path is powerful but heavy. It should be treated as a small data-engineering project, not a quick config tweak.

## Current Decision

No new implementation issue is justified yet.

Next practical steps:

1. Complete a user-operated MekHQ UI pass for `#17` when possible.
2. Build the real `campaigns/miniatures/physical-miniature-roster.csv`.
3. Use `OPFOR_MUL_POOL_MANIFEST.csv` to create one or two confirmed fixed OPFOR pools.
4. Play or simulate one scenario with fixed-pool substitution.
5. Reopen custom generation only if fixed pools are clearly too manual.

## Source References

- `external/installs/MekHQ-0.51.00/docs/StratCon/Custom RATs.txt`
- `external/installs/MekHQ-0.51.00/docs/RAT and Force Generator Stuff/rat-generator.txt`
- `external/installs/MekHQ-0.51.00/docs/StratCon/stratcon-faq-2.6.md`
- `megamek.client.generator.RandomUnitGenerator`
- `megamek.client.ratgenerator.RATGenerator`
- `megamek.client.ratgenerator.UnitTable`
- `mekhq.campaign.universe.RATGeneratorConnector`
- `mekhq.campaign.mission.BotForceRandomizer`
- `mekhq.MHQConstants#RAT_INFO_DIR`
- `megamek.common.Configuration#armyTablesDir`
- `megamek.common.Configuration#forceGeneratorDir`
