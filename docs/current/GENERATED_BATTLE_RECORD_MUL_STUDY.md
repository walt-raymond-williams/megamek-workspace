# Generated Battle-Record MUL Study

This note records issue `#22`: study of the live MegaMek battle-record MUL saved during the first MekHQ exploration shakedown.

Evidence labels follow `DOCUMENTATION_WORKFLOW.md`.

## Summary

- `Confirmed from generated MUL`: `external/installs/MekHQ-0.51.00/The Learning Ropes.mul` is a battle-record file with top-level `<record version="0.51.00">`, not a setup `<unit>` MUL.
- `Confirmed from generated MUL`: this file contains `survivors`, `salvage`, `devastated`, and `kills`. It does not contain populated `allies` or `retreated` sections.
- `Confirmed from generated MUL`: result buckets are end-of-battle state buckets, not simple side buckets. Player campaign units appear in `survivors`, `salvage`, and `devastated` depending on what happened to them.
- `Confirmed from generated MUL and generated logs`: `logs/salvage.mul` is a plain `<unit>` salvage/export list with only four recoverable entities. It is not enough for MekHQ battle-result import because it lacks survivors, devastated units, retreated units, and kill rows.
- `Confirmed from save copy`: the resolved MekHQ scenario `Diversion Engagement` is `DRAW`; its report shows the far-edge movement objective failed and the destroy/rout objective completed.
- `Confirmed by user`: the user ended the match after killing enemies and accepting battlefield control, but did not complete the movement/exit objective. The save-state report matches that account.

## Artifacts Studied

- Full battle-record MUL: `external/installs/MekHQ-0.51.00/The Learning Ropes.mul`
- Salvage/log MUL: `external/installs/MekHQ-0.51.00/logs/salvage.mul`
- Entity status report: `external/installs/MekHQ-0.51.00/logs/entitystatus.txt`
- Game action telemetry: `external/installs/MekHQ-0.51.00/logs/game_actions_1.tsv`
- Save copy only: `analysis/tmp/issue-22/Autosave-1-The Learning Ropes-30250720.cpnx`

The raw save and generated artifacts under `external/` were not modified.

## Battle-Record Structure

`Confirmed from generated MUL`: the full file has this structure:

| Section | Count | Examples |
| --- | ---: | --- |
| `survivors` | 5 | Trebuchet TBT-5N, Grasshopper GHR-5H, two Stalker STK-4P units, ejected pilot Recruit Naizen Hodges |
| `allies` | 0 | No populated section observed in this file |
| `salvage` | 5 | ejected pilot Stepán Michudo, Crab CRB-20, Griffin GRF-1N, Flea FLE-4, Union (2710) (CV) |
| `retreated` | 0 | No populated section observed in this file |
| `devastated` | 4 | Centurion CN9-A, ejected pilot Recruit Ryuzaburo Ine, BattleMaster BLR-1G-DC, Crusader CRD-3R |
| `kills` | 3 | BattleMaster killed by Stalker UUID `eb6d60a7-d9ac-4f08-b1fc-af447f1c6ca8`; ejected pilot killed by `None`; Union killed by Grasshopper UUID `43cba584-04f9-4cad-8315-9369b49b3292` |

`Confirmed from generated MUL`: campaign unit UUIDs are preserved in `externalId`. Examples:

- Trebuchet TBT-5N: `66763236-66f0-451e-a273-6cb740b76a5a`
- Grasshopper GHR-5H: `43cba584-04f9-4cad-8315-9369b49b3292`
- Crab CRB-20: `4653009b-0108-465c-bad3-cc19c3cb742c`
- Griffin GRF-1N: `21d83f0a-a3c9-435c-a6eb-d6043ddde12f`
- Centurion CN9-A: `9899788b-fe00-4595-912b-fe46526a7003`
- Crusader CRD-3R: `fc819fbb-9314-49ae-9fcf-0474af6f7a72`

`Confirmed from save copy`: those UUIDs match campaign `Unit` ids for player campaign units in the save copy.

## Damage, Ammo, Crew, And Pickup State

`Confirmed from generated MUL`: the battle-record file preserves only changed per-entity state, using the same entity XML style observed in source-backed notes:

- Pilot hits and death: Trebuchet pilot has `hits="1"`; Griffin pilot has `hits="2"`; Crab pilot has `hits="Dead"`.
- Ejection and pickup: ejected pilot entities use `type="Leg"`, `ejected="true"`, and `pickUpId`. Recruit Naizen Hodges has `pickUpId="23744290-3886-4ffd-916e-23c34e9c1abf"` and appears in `survivors`; Stepán Michudo has `pickUpId="66763236-66f0-451e-a273-6cb740b76a5a"` and appears in `salvage`.
- Destroyed locations: examples include Griffin center torso and left torso marked `isDestroyed="true"`, Flea center torso and multiple limbs destroyed, and Centurion center torso destroyed in `devastated`.
- Critical slot state: examples include `isHit`, `isDestroyed`, `isRepairable`, and `isMissing` on slots.
- Ammo state: examples include Trebuchet LRM-15 bins at `shots="2"` and `shots="0"`, Stalker SRM/LRM ammo counts, and Union AC/5 and LRM-20 ammo counts.
- Large-craft state: Union records fuel, structural integrity, heat sinks, transport bays, armor/internal state, ammo, and weapon critical damage.

`Confirmed from generated logs`: `entitystatus.txt` agrees with the broad end state: Trebuchet, Grasshopper, and two Stalkers are listed as surviving combat units; Crab, Griffin, Flea, and Union are listed as salvage-state entities; Centurion, Crusader, and a pilot are listed under utterly destroyed/not-available-for-salvage.

## Full Record MUL Versus salvage.mul

`Confirmed from generated logs`: `logs/salvage.mul` has top-level `<unit version="0.51.00">` and contains four entities:

- Crab CRB-20
- Griffin GRF-1N
- Flea FLE-4
- Union (2710) (CV)

Differences:

- `The Learning Ropes.mul` is a battle result. It has section context, player survivor state, wrecks, devastated units, ejected pilot state, and kill credit.
- `salvage.mul` is a salvage/export list. It has no `<record>` wrapper, no result buckets, no survivors, no devastated units, and no `<kills>`.
- `The Learning Ropes.mul` includes the ejected enemy pilot Stepán Michudo in `salvage`; `salvage.mul` does not include that pilot entity.
- `The Learning Ropes.mul` includes the Union in `salvage`; this matches `salvage.mul`, but the full record also gives its battle context and kill credit.

Implication: a future import test should select a full battle-record `<record>` MUL, not `logs/salvage.mul`.

## Mapping To The Resolved Scenario

`Confirmed from save copy`: `Diversion Engagement` report:

- Reach destination edge with 50% of listed forces: failed, `-2` scenario victory points.
- Destroy or rout 75% of listed forces: completed, `+2` scenario victory points.
- Final status: `DRAW`.

`Confirmed by user`: this matches the play account: the user destroyed/routed the enemy, accepted battlefield control, and ended without walking enough forces across the map.

`Confirmed from save copy`: scenario text said enemy battlefield control was expected and there would be no time for salvage. The user's accepted battlefield-control prompt may have affected post-battle salvage handling, but the scenario objective report still produced a draw.

`Confirmed from save copy`: MekHQ campaign kill records after resolution contain two kill entries:

- BattleMaster BLR-1G credited to Centurion CN9-A and pilot Ryuzaburo Ine.
- Pike Support Vehicle credited to Trebuchet TBT-5N and pilot Misbah bin Hud.

`Confirmed from generated MUL`: the raw saved battle-record kill rows in `The Learning Ropes.mul` differ from those post-resolution campaign kill entries. The full MUL records BattleMaster BLR-1G-DC killed by a Stalker UUID, ejected pilot Stepán Michudo killed by `None`, and Union killed by the Grasshopper UUID.

`Unknown`: whether the difference is caused by the user saving a MUL from a later/current MegaMek state, campaign-side kill-credit post-processing, multiple scenario/log artifacts in the same install directory, or another MegaMek/MekHQ handoff behavior. Treat campaign kill logs and raw battle-record `<kills>` as related but not automatically identical until the next controlled import test.

## What This Teaches The Generator Workflow

- A generated result MUL must preserve player campaign unit `externalId` values exactly.
- Bucket assignment matters more than side labels. Player units can be `survivors`, `salvage`, or `devastated`; enemy units can be `salvage`, `retreated`, or `devastated`.
- Ejected crew can be represented as their own `Pilot`/`Leg` entities with `pickUpId`, and the owning crew member still needs a stable `externalId`.
- Salvage/export MULs are useful for inspecting recoverable wrecks, but they are the wrong artifact for full scenario import.
- Damage fidelity is broad: armor, internals, destroyed locations, critical slots, ammo, ejection, crew hits/death, large-craft structural state, transport bay state, and pickup state can all appear in one file.
- Kill credit needs extra validation. Do not assume that a campaign kill record is a direct copy of every raw `<kill>` row.

## Checklist For The Next Manual Import Attempt

Use issue `#10` for the live import validation, but carry this checklist into the session:

1. Use a disposable campaign/scenario and save before resolving.
2. Record exact scenario id, scenario name, deployed player unit UUIDs, and the setup MUL path before launching or importing.
3. When MekHQ asks for result input, select a full `<record>` battle-record MUL, not `logs/salvage.mul`.
4. Record the exact battlefield-control prompt wording and chosen answer.
5. Record the exact final scenario status chosen in the resolve wizard.
6. After finish, inspect unit damage, pilot hits/deaths/ejections, salvage candidates, devastated units, and campaign kill-credit records.
7. Compare post-resolution campaign kill records against the raw `<kills>` rows in the selected MUL.
8. If a movement objective and a destruction objective both exist, record each objective result separately; final scenario status may be `DRAW` even after all enemies are destroyed.

