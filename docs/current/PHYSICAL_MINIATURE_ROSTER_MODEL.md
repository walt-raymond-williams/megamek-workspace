# Physical Miniature Roster Model

This note defines the physical-miniature roster data model for GitHub issue `#20`. It is for tracking the user's real BattleTech miniatures so MekHQ player roster choices, OPFOR substitution, fixed OPFOR MUL pools, and possible custom RATs can all use the same inventory source.

Evidence labels follow `DOCUMENTATION_WORKFLOW.md`.

## Status

- `Confirmed from source`: MekHQ can add player units through GM UI workflows and can load fixed OPFOR bot formations from MUL files. See `MECH_ROSTER_CONTROL_WORKFLOWS.md` and `QUICKSTART_ROSTER_REPLACEMENT_VERIFICATION.md`.
- `Inferred`: a CSV inventory is the best first format because it is human-editable, diffable, spreadsheet-friendly, and easy for later scripts to parse.
- `Unknown`: the user's actual miniature inventory has not been provided. Example rows in this note and template are placeholders only.

## Recommended File

Use:

```text
campaigns/miniatures/physical-miniature-roster.csv
```

Until real inventory exists, use the template:

```text
docs/templates/PHYSICAL_MINIATURE_ROSTER.csv
```

Do not treat template/example rows as confirmed inventory.

## Schema

Required columns:

| Column | Required | Meaning |
| --- | --- | --- |
| `mini_id` | Yes | Stable local identifier for the physical miniature, such as `MINI-0001`. Never reuse an id for a different miniature. |
| `inventory_status` | Yes | `confirmed`, `example`, `wanted`, `retired`, or `unknown`. Real owned miniatures should be `confirmed`. |
| `quantity` | Yes | Count of identical miniatures represented by this row. Use `1` when paint/proxy/status differs by individual model. |
| `chassis` | Yes | Canonical chassis or common unit name, for example `Wolverine`. |
| `variant` | Recommended | Specific variant, for example `WVR-6R`; use `unknown` if the miniature is visually identifiable but not variant-locked. |
| `unit_type` | Yes | MekHQ/MegaMek-style unit type such as `Mek`, `Tank`, `VTOL`, `Infantry`, `BattleArmor`, `ConventionalFighter`, `Aero`, `DropShip`. |
| `weight_class` | Recommended | `UL`, `L`, `M`, `H`, `A`, `SH`, or blank if not useful for the unit type. |
| `tech_base` | Recommended | `IS`, `Clan`, `Mixed`, or `unknown`. |
| `era_start_year` | Optional | Earliest year this row should normally appear in campaign generation, if known. |
| `faction_tags` | Optional | Semicolon-separated faction/use tags, such as `MERC;MOC;PIR`. These are player-facing tags, not necessarily MekHQ faction keys. |
| `player_use` | Yes | `yes`, `no`, or `maybe`; whether this miniature can be used in the player's company. |
| `opfor_use` | Yes | `yes`, `no`, or `maybe`; whether this miniature can be used as opposition. |
| `proxy_policy` | Yes | `exact`, `close`, `loose`, or `display_only`. |
| `proxy_notes` | Optional | What this model can stand in for, if any. |
| `bv` | Optional | Battle Value for the chosen variant and rules context, if known. |
| `bv_source` | Optional | Source for the BV value, for example `MegaMek 0.51.00`, `MUL`, `record sheet`, or `unknown`. |
| `mekhq_lookup_name` | Recommended | Name expected to search well in MekHQ/MegaMek unit selectors. |
| `unit_file_hint` | Optional | Known `.mtf`, `.blk`, or MegaMek summary hint if discovered later. |
| `paint_scheme` | Optional | Visual force/paint note, useful when the same chassis exists in multiple paint schemes. |
| `model_notes` | Optional | Physical condition, base markings, magnetization, missing arms, etc. |
| `campaign_notes` | Optional | Story or campaign-use note. |

## Controlled Values

Use small controlled values where possible. It keeps later scripts boring, and boring is exactly what we want when the other half of the system is a campaign save.

Recommended values:

- `inventory_status`: `confirmed`, `example`, `wanted`, `retired`, `unknown`
- `unit_type`: `Mek`, `Tank`, `VTOL`, `Naval`, `Infantry`, `BattleArmor`, `ProtoMek`, `ConventionalFighter`, `Aero`, `Small Craft`, `DropShip`, `JumpShip`, `WarShip`
- `weight_class`: `UL`, `L`, `M`, `H`, `A`, `SH`, `C`
- `tech_base`: `IS`, `Clan`, `Mixed`, `unknown`
- `player_use` / `opfor_use`: `yes`, `no`, `maybe`
- `proxy_policy`: `exact`, `close`, `loose`, `display_only`

## Example Rows

These are examples only.

```csv
mini_id,inventory_status,quantity,chassis,variant,unit_type,weight_class,tech_base,era_start_year,faction_tags,player_use,opfor_use,proxy_policy,proxy_notes,bv,bv_source,mekhq_lookup_name,unit_file_hint,paint_scheme,model_notes,campaign_notes
MINI-EX-0001,example,1,Wolverine,WVR-6R,Mek,M,IS,3025,MERC;MOC,yes,maybe,exact,,1101,MegaMek 0.51.00,Wolverine WVR-6R,,unpainted,example row only,good starter player unit example
MINI-EX-0002,example,1,Locust,LCT-1V,Mek,L,IS,3025,PIR;MERC,no,yes,exact,,432,MegaMek 0.51.00,Locust LCT-1V,,red primer,example row only,light OPFOR example
MINI-EX-0003,example,2,Scorpion,unknown,Tank,L,IS,3025,General,no,yes,close,any light tracked combat vehicle,,unknown,Scorpion,,green,example row only,vehicle proxy example
```

## How This Feeds MekHQ Workflows

### Player Roster Replacement

Use rows where:

- `inventory_status = confirmed`
- `player_use = yes`
- `proxy_policy = exact` or an explicitly accepted proxy value

The `mekhq_lookup_name` column tells the user what to search for in MekHQ's unit selector or market. After adding units, pilots, technicians, TO&E, and transport still need normal MekHQ assignment.

### Manual OPFOR Substitution

Use rows where:

- `inventory_status = confirmed`
- `opfor_use = yes`
- scenario era/faction/weight/BV are close enough for the table

For a quick game, sort/filter by `weight_class`, `unit_type`, and `bv`, then substitute a close physical unit for the generated MekHQ OPFOR.

### Fixed OPFOR MUL Pools

Use rows where:

- `opfor_use = yes`
- `mekhq_lookup_name` maps to a MegaMek unit
- `bv` is known or can be checked in MegaMek/MekHQ

Later issue `#18` can group rows into MUL pools by BV band, force role, faction flavor, and weight class.

### Custom RATs

Use rows where:

- `opfor_use = yes`
- `unit_type`, `weight_class`, `tech_base`, `era_start_year`, and faction tags are populated enough to map into RAT metadata

Later issue `#19` should decide whether this is worth the overhead. For a small physical collection, fixed MULs and manual substitution may remain better.

## Validation Rules

For real inventory:

1. `mini_id` must be unique.
2. `inventory_status=confirmed` means the user has verified the physical miniature exists.
3. `quantity` must be a positive integer.
4. `player_use` and `opfor_use` should not both be `no` unless the row is display-only or retired.
5. `variant=unknown` is allowed, but `proxy_notes` should explain how the mini may be used.
6. `bv` must include `bv_source` when populated.
7. `mekhq_lookup_name` should be tested in MekHQ/MegaMek before using the row for generated MUL tooling.

## Open Questions

- Does the user want exact variants only for player units, or are close visual proxies acceptable?
- Should painted faction/force ownership affect selection, or should generation only care about chassis, variant, and BV?
- Should aerospace, vehicles, infantry, battle armor, and DropShips share one roster file or move to separate files if the collection grows?
