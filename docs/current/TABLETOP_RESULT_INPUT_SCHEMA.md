# Tabletop Result Input Schema

This note defines the minimum practical tabletop result schema for GitHub issue `#9`. It is meant to support three possible workflows:

1. Entering results through MekHQ's built-in manual resolution UI.
2. Editing or reviewing a battle-record MUL exported from MegaMek.
3. Later generating a battle-record MUL if issue `#11` decides custom tooling is justified.

Evidence labels follow `DOCUMENTATION_WORKFLOW.md`.

## Summary

- `Confirmed from source`: MekHQ manual result import consumes a battle-record `<record>` MUL with `survivors`, `allies`, `salvage`, `retreated`, `devastated`, and `kills` sections. See `TABLETOP_RESULT_MUL_WORKFLOW.md`.
- `Confirmed from source`: campaign unit matching depends on entity `externalId` matching the MekHQ campaign `Unit` UUID. Campaign personnel matching depends on crew external ids matching MekHQ `Person` UUIDs.
- `Confirmed from source`: battlefield control, final scenario status, salvage allocation, contract salvage limits, salvage exchange, battle loss compensation, and CamOps recovery feasibility are resolved by MekHQ/operator workflow, not by fields in the MUL.
- `Confirmed from source`: `EntityListFile.writeEntityList(...)` writes changed armor/internal values, destroyed/blown-off locations, damaged critical slots, ammo shots, crew hits/death/ejection, external ids, and transport/cargo state when present.
- `Inferred`: the first tabletop capture format should be a short human-readable worksheet. Exact per-location/per-slot detail can be added only when the table needs high-fidelity repair, salvage, or casualty consequences.

## Result Levels

Use three levels so the first campaign can stay playable while still leaving room for accurate automation later.

| Level | Purpose | Required for first playable workflow? |
| --- | --- | --- |
| `summary` | Lets MekHQ resolve scenario status, casualties at a broad level, and salvage candidates with manual review. | Yes |
| `repair` | Captures enough armor/internal/critical/ammo state to make MekHQ repairs and BLC meaningful. | Recommended for player units and kept salvage |
| `generator` | Preserves ids and exact entity state needed to generate a battle-record MUL. | Only if custom generation proceeds |

## Required Scenario Fields

These fields are required no matter whether the final workflow is manual UI entry or generated MUL import.

| Field | Required | Meaning |
| --- | --- | --- |
| `schema_version` | Yes | Version of this capture format, starting with `1`. |
| `scenario_id` | Recommended | MekHQ scenario id or name/date if the id is not available. |
| `scenario_name` | Yes | Name shown in MekHQ. |
| `battle_date` | Recommended | Campaign date of the battle. |
| `source_setup_mul` | Recommended | Path to the exported setup MUL used at the table, if any. |
| `final_scenario_status` | Yes | Operator choice for MekHQ wizard, such as `victory`, `defeat`, `draw`, or MekHQ's exact UI value when known. |
| `battlefield_control_claim` | Yes | What the player should choose when MekHQ prompts: `player_controls_field`, `player_yields_field`, or `unknown`. |
| `battlefield_control_reason` | Recommended | Objective/narrative reason for the choice. |
| `scenario_report` | Optional | Text to paste into MekHQ's scenario report. |
| `camops_salvage_used` | Optional | Whether CamOps salvage was enabled and salvage teams were assigned before the scenario. |

Important: `battlefield_control_claim` is an operator instruction, not a MUL field. MekHQ prompts for control before processing salvage consequences.

## Required Unit Fields

Each unit that deployed, retreated, was destroyed, became salvage, or mattered for kill credit should have one record.

| Field | Required | Meaning |
| --- | --- | --- |
| `unit_result_id` | Yes | Stable id inside this result file, such as `U1`. |
| `side` | Yes | `player`, `ally`, or `enemy`. |
| `display_name` | Yes | Name shown on the record sheet or MekHQ/MegaMek setup MUL. |
| `unit_external_id` | Generator | Entity external id; for player campaign units this must be the campaign `Unit` UUID. |
| `crew_external_ids` | Generator | Crew/person external ids; for campaign personnel these should be MekHQ `Person` UUIDs. |
| `chassis` | Recommended | Chassis/common name. |
| `model` | Recommended | Variant/model. |
| `table_status` | Yes | What happened on the tabletop: `active_end`, `escaped`, `forced_withdrawal`, `immobile`, `wrecked`, `devastated`, `captured`, or `unknown`. |
| `mekhq_result_bucket` | Yes | Intended import bucket: `survivor`, `ally`, `salvage`, `retreated`, or `devastated`. |
| `can_escape` | Recommended | Whether the unit could leave the field if the player does not control it. |
| `crew_state` | Yes | `healthy`, `wounded`, `dead`, `ejected`, `missing`, `captured`, `not_tracked`, or `unknown`. |
| `crew_hits` | Recommended | Hits per pilot/crew slot if known. Use `dead` for killed crew. |
| `killed_by_unit_result_id` | Optional | Unit that gets kill credit, if known. |
| `salvage_intent` | Optional | Table intent before MekHQ wizard: `keep`, `sell`, `leave_to_employer`, `escaped`, or `unknown`. |
| `notes` | Optional | Human explanation of odd cases. |

### Bucket Mapping

Use this mapping for MekHQ/MUL import planning:

| Table outcome | Player unit bucket | Enemy unit bucket | Notes |
| --- | --- | --- | --- |
| Still fighting / operational at end | `survivor` | `salvage` if enemy cannot escape, otherwise usually not tracked as salvage | MegaMek battle-record writer puts in-game enemy entities that cannot escape into `salvage`. |
| Friendly retreated or withdrew off-board | `survivor` | N/A | MegaMek writer includes friendly retreated units in `survivors`. |
| Enemy retreated or escaped | N/A | `retreated` | Not normal salvage. |
| Wrecked, shutdown, disabled, abandoned, salvageable | `salvage` | `salvage` | Friendly wrecks may become total losses if player yields field. Enemy wrecks become potential salvage only if player controls field. |
| Utterly destroyed / ammo-vaporized / no useful wreck | `devastated` | `devastated` | Not normal kept salvage. Enemy devastated units may still matter for prisoners/casualties. |
| Allied unit survived | `ally` | N/A | AtB-style allied units may be tracked separately. |

## Damage Fields

For the first playable workflow, record exact damage for all player units and any enemy salvage the player may keep. For enemy units that escaped or were devastated, summary status may be enough unless kill/casualty detail matters.

| Field | Level | Meaning |
| --- | --- | --- |
| `armor_by_location` | repair | Remaining armor by location, including rear armor where applicable. |
| `internal_by_location` | repair | Remaining internal structure by location. |
| `destroyed_locations` | repair | Locations with internal structure reduced to zero. |
| `blown_off_locations` | repair | Locations physically blown off, especially Mek limbs. |
| `breached_locations` | repair | Locations breached in vacuum/underwater rules if relevant. |
| `critical_hits` | repair | Per-location critical slot hits/destroyed/missing/repairable state. |
| `ammo_remaining` | repair | Remaining shots by ammo bin, including bin location if known. |
| `motive_damage` | repair | Vehicle motive hits, penalties, or immobilization. |
| `aero_criticals` | repair | Avionics, sensors, engine, FCS, CIC, thrust, fuel, SI, KF, sail, or similar aerospace state. |
| `transport_state` | generator | Loaded cargo/passenger relationships at end of battle. |

`Confirmed from source`: `EntityListFile.getLocString(...)` writes only changed damage/ammo/slot state. A future generator can start from a setup entity and apply these changes before using MegaMek serialization.

## Personnel Fields

| Field | Required | Meaning |
| --- | --- | --- |
| `crew_member_ref` | Recommended | Name, role, or MekHQ person id if available. |
| `crew_slot` | Optional | Slot index for multi-crew units. |
| `hits` | Recommended | Current pilot/crew hits after battle, or `dead`. |
| `ejected` | Recommended | Whether this crew member ejected. |
| `ejection_state` | Optional | `recovered`, `missing`, `captured`, `picked_up_by_unit`, or `unknown`. |
| `picked_up_by_unit_result_id` | Optional | Unit that picked up the ejected crew, if applicable. |
| `temporary_crew_losses` | Optional | Vehicle/infantry/blob crew killed or wounded if not individually tracked. |

`Confirmed from source`: `ResolveScenarioTracker.checkStatusOfPersonnel()` uses pilot/crew presence, crew hits, ejection entities, missing status, unit loss state, and random/campaign-option logic for vehicles, infantry, large craft, prisoners, and casualties. The schema should capture the tabletop facts and let MekHQ calculate the campaign consequences.

## Kill Credit Fields

Kill credit is optional for basic resolution but important for pilot records.

| Field | Required | Meaning |
| --- | --- | --- |
| `killed_unit_result_id` | Yes for known kills | Unit that was destroyed, devastated, or otherwise credited as killed. |
| `killer_unit_result_id` | Optional | Friendly unit that gets credit. |
| `killer_external_id` | Generator | Campaign unit UUID for the killer when generating `<kill>` records. |
| `kill_credit_note` | Optional | Use when kill credit is shared, uncertain, environmental, artillery, mines, or scenario objective based. |

`Confirmed from source`: MegaMek writes `<kill killed="display name" killer="external id"/>`; if no valid killer external id exists, it writes `None`.

## Minimum First-Campaign Schema

For the first real tabletop session, capture this smaller set:

```yaml
schema_version: 1
scenario:
  scenario_name:
  battle_date:
  final_scenario_status:
  battlefield_control_claim:
  battlefield_control_reason:
  scenario_report:
units:
  - unit_result_id:
    side:
    display_name:
    chassis:
    model:
    table_status:
    mekhq_result_bucket:
    crew_state:
    crew_hits:
    killed_by_unit_result_id:
    damage_summary:
    salvage_intent:
    notes:
kills:
  - killed_unit_result_id:
    killer_unit_result_id:
    kill_credit_note:
```

This is enough to drive a careful manual MekHQ result-entry session and to identify which units need more detailed repair/salvage transcription.

## Full Future Schema

If custom generation proceeds, expand each unit record:

```yaml
schema_version: 1
scenario:
  scenario_id: "42"
  scenario_name: "Hold the Pass"
  battle_date: "3025-04-19"
  source_setup_mul: "analysis/generated/example/Hold_the_Pass_player.mul"
  final_scenario_status: "victory"
  battlefield_control_claim: "player_controls_field"
  battlefield_control_reason: "Player held the objective and enemy withdrew."
  camops_salvage_used: false
  scenario_report: "Player held the pass after destroying the enemy Locust."
units:
  - unit_result_id: "P1"
    side: "player"
    display_name: "Wolverine WVR-6R"
    unit_external_id: "campaign-unit-uuid"
    crew_external_ids:
      - "pilot-person-uuid"
    chassis: "Wolverine"
    model: "WVR-6R"
    table_status: "active_end"
    mekhq_result_bucket: "survivor"
    can_escape: true
    crew_state: "wounded"
    crew_hits:
      - crew_member_ref: "Lt. Example"
        hits: 1
        ejected: false
    damage:
      armor_by_location:
        right_arm: 6
        center_torso: 18
      internal_by_location:
        right_arm: 8
      destroyed_locations: []
      blown_off_locations: []
      critical_hits:
        - location: "right_arm"
          slot: 5
          state: "hit"
          equipment: "Autocannon/5"
      ammo_remaining:
        - location: "right_arm"
          ammo_type: "IS Ammo AC/5"
          shots: 12
    killed_by_unit_result_id:
    salvage_intent:
    notes: "Survived with light pilot injury."
kills:
  - killed_unit_result_id: "E1"
    killer_unit_result_id: "P1"
    kill_credit_note: "Final damage from Wolverine AC/5."
```

## Example: Simple Player Unit

This is a summary-level record suitable for first-session manual entry:

```yaml
unit_result_id: "P1"
side: "player"
display_name: "Wolverine WVR-6R"
chassis: "Wolverine"
model: "WVR-6R"
table_status: "active_end"
mekhq_result_bucket: "survivor"
crew_state: "wounded"
crew_hits:
  - crew_member_ref: "assigned pilot"
    hits: 1
    ejected: false
damage_summary: "Right arm armor damaged; AC/5 ammo bin has 12 shots remaining; no destroyed locations."
salvage_intent:
notes: "Needs repair transcription before final MekHQ closeout if using exact repairs."
```

## Example: Enemy Salvage Candidate

```yaml
unit_result_id: "E1"
side: "enemy"
display_name: "Locust LCT-1V"
chassis: "Locust"
model: "LCT-1V"
table_status: "wrecked"
mekhq_result_bucket: "salvage"
crew_state: "ejected"
crew_hits:
  - crew_member_ref: "enemy pilot"
    hits: 2
    ejected: true
ejection_state: "captured"
damage_summary: "Center torso intact; right leg destroyed; engine not confirmed destroyed."
killed_by_unit_result_id: "P1"
salvage_intent: "keep"
notes: "Only becomes potential salvage if player claims battlefield control in MekHQ."
```

## Required Regardless Of Workflow

Capture these even if no custom tool is ever written:

- scenario outcome and battlefield-control choice
- every deployed player unit's end state
- every player crew member's wounds, death, ejection, missing, or capture state
- enemy units that were wrecked/salvageable, devastated, or retreated
- likely kill credit
- enough damage notes to repair player units and kept salvage accurately
- any odd transport, pickup, ejection, or prisoner facts

## Only Required For Custom Generation

These can wait until issue `#11` decides a generator is needed:

- exact `unit_external_id` and `crew_external_ids` copied from MekHQ/MegaMek exports
- exact armor/internal values for every location
- exact critical slot indexes and repairability flags
- exact ammo bin identities and remaining shots
- exact transport bay/cargo relationships
- generated enemy UUID strategy
- validation rules for mapping tabletop status to battle-record MUL sections

## Unresolved Edge Cases

- Ejected crew pickup: source supports ejection entities and pickup ids, but this needs a round-trip test in issue `#10`.
- Vehicles and infantry: MekHQ derives some casualties from unit state and campaign options; exact tabletop casualty capture may need a richer sub-schema.
- Large craft, aerospace, and space battles: additional velocity, altitude, fuel, SI, thrust, bay, KF, sail, and escape-craft state may matter.
- Battlefield control: should be chosen in MekHQ from tabletop objective results, but the exact house rule for deciding control is still campaign-specific.
- Salvage ownership: final keep/sell/leave/employer choices belong in MekHQ's salvage wizard, not the battle result file.
- CamOps salvage: requires pre-scenario salvage-team assignment; result capture alone is not sufficient.
- Off-board retreat: friendly retreat maps differently from enemy retreat in MegaMek's battle-record writer.
- Ammunition fidelity: exact ammo remaining is only needed when repair/reload cost and future readiness should be accurate.
- Critical repairability: a tabletop sheet may not record whether a destroyed slot is repairable; future tooling may need a review step rather than guessing.

## Recommendation For Issue #10

Prototype round-trip validation should start with four small cases:

1. Player unit survives with one pilot hit and armor damage.
2. Enemy unit is wrecked in `salvage` and becomes potential salvage when player claims field control.
3. Enemy unit retreats and does not become salvage.
4. Player unit is wrecked in `salvage` and is recovered or lost depending on battlefield control.

Do not start with aerospace, transport bays, infantry, or ejections. Those are high-complexity cases and can wait outside the first proof.

## Source References

- `mekhq.campaign.ResolveScenarioTracker#processMulFiles`
- `mekhq.campaign.ResolveScenarioTracker#loadUnitsAndPilots`
- `mekhq.campaign.ResolveScenarioTracker#checkStatusOfPersonnel`
- `mekhq.campaign.ResolveScenarioTracker#resolveScenario`
- `megamek.common.units.EntityListFile#saveTo(File, Client, Player)`
- `megamek.common.units.EntityListFile#writeEntityList`
- `megamek.common.units.EntityListFile#getLocString`
- `megamek.common.loaders.MULParser`
- `docs/current/TABLETOP_RESULT_MUL_WORKFLOW.md`
- `docs/current/SALVAGE_RULES_NOTES.md`
