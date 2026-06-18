# Tabletop Result MUL Workflow

This note records the source-confirmed MekHQ/MegaMek workflow for exporting scenario MULs, resolving a scenario manually, and importing a battle-record MUL back into MekHQ.

Evidence labels:

- `Confirmed from source`: directly observed in the local source checkout.
- `Inferred from source`: follows from the observed code path but has not been validated through the UI in this workspace.

## Summary

- `Confirmed from source`: MekHQ's Briefing tab `Get MUL` action exports ordinary setup MUL files through `BriefingTab.deployListFile()` and `EntityListFile.saveTo(File, Collection<Entity>)`.
- `Confirmed from source`: MekHQ's manual scenario resolution path imports a single battle-record MUL through `MekHQ.resolveScenario(Scenario)`, `ChooseMulFilesDialog`, and `ResolveScenarioTracker.processMulFiles()`.
- `Confirmed from source`: the imported file is parsed by MegaMek's `MULParser`; MekHQ consumes the battle-record sections `survivors`, `allies`, `salvage`, `retreated`, `devastated`, and `kills`.
- `Confirmed from source`: campaign player units are matched primarily by each entity's `externalId`, which must be the campaign `Unit` UUID. Crew/personnel matching uses crew-member `externalId` values that match MekHQ `Person` UUIDs.
- `Confirmed by user`: during the `2026-06-18` MekHQ exploration shakedown, after resolving/ending a MegaMek-launched match, the UI offered a specific option to save a MUL.
- `Inferred from source`: a robust tabletop result generator should not merely modify the setup `<unit>` MUL. It should generate a battle-record `<record>` MUL with the sections MegaMek writes after a game.

## Export Setup MULs

`Confirmed from source`: [BriefingTab.java](../../external/src/mekhq/MekHQ/src/mekhq/gui/BriefingTab.java) creates the `Get MUL` button and wires it to `deployListFile()`. The export path:

1. Collects the selected scenario's deployed campaign units.
2. Calls `unit.resetPilotAndEntity()` before adding the campaign entity to the export list.
3. Saves the player list with `EntityListFile.saveTo(file, chosen)`.
4. For AtB scenarios, separately exports player-allied forces and each bot force, using generic BV when applicable.

Relevant source:

- `mekhq.gui.BriefingTab#deployListFile`
- `mekhq.gui.BriefingTab#determineMULFilePath`
- `megamek.common.units.EntityListFile#saveTo(File, ArrayList<Entity>, int, boolean)`

Important distinction: this export creates ordinary `<unit>` MUL files. It is a force setup artifact, not the battle-record artifact consumed by manual resolution.

## Manual Result Import

`Confirmed from source`: [MekHQ.java](../../external/src/mekhq/MekHQ/src/mekhq/MekHQ.java) handles the Resolve Manually flow:

1. `MekHQ.resolveScenario(Scenario)` computes battlefield control with `playerHasFieldControl(selectedScenario, null)`.
2. It constructs `ResolveScenarioTracker`.
3. It opens `ChooseMulFilesDialog`.
4. If the dialog is not cancelled, it opens `ResolveScenarioWizardDialog`.
5. The wizard later calls `tracker.resolveScenario(status, report)` to apply the selected resolution.

`Confirmed from source`: [ChooseMulFilesDialog.java](../../external/src/mekhq/MekHQ/src/mekhq/gui/dialog/ChooseMulFilesDialog.java) only lets the user browse for one MUL file and calls `tracker.processMulFiles()` when advancing.

Relevant source:

- `mekhq.MekHQ#resolveScenario(Scenario)`
- `mekhq.gui.dialog.ChooseMulFilesDialog#btnNextActionPerformed`
- `mekhq.gui.dialog.ResolveScenarioWizardDialog#btnFinishActionPerformed`
- `mekhq.campaign.ResolveScenarioTracker#processMulFiles`
- `mekhq.campaign.ResolveScenarioTracker#resolveScenario`

## Battle-Record MUL Sections

`Confirmed from source`: [MULParser.java](../../external/src/megamek/megamek/src/megamek/common/loaders/MULParser.java) recognizes a top-level `record` element and these result sections:

- `survivors`
- `allies`
- `salvage`
- `retreated`
- `devastated`
- `kills`

`Confirmed from source`: [EntityListFile.java](../../external/src/megamek/megamek/src/megamek/common/units/EntityListFile.java) writes those same sections in `EntityListFile.saveTo(File, Client, Player)`, which is the MegaMek battle-record writer. The writer sorts in-game, retreated, graveyard, and devastated entities into the sections and records kill credit as `<kill killed="..." killer="..."/>`.

`Confirmed by user`: the save-MUL option is exposed in the live MegaMek/MekHQ play flow after a match is resolved or ended. This needs one more UI pass to record the exact button/menu label and whether the saved file is a setup-style `<unit>` MUL, a battle-record `<record>` MUL, or a salvage-only artifact.

Result-section meaning in MekHQ import:

- `survivors`: friendly/player or allied units still present at the end of battle. MekHQ records their entity damage state and live crew state.
- `allies`: allied units that MekHQ should track as allied `TestUnit` results in AtB-style scenarios.
- `salvage`: destroyed, wrecked, or otherwise salvageable entities. Friendly campaign units in this section may become total losses if the player does not control the field. Enemy entities become potential salvage only if MekHQ believes the player controls the field.
- `retreated`: entities that escaped. Friendly campaign units here are found and not total losses.
- `devastated`: utterly destroyed entities. Friendly campaign units here are total losses; enemy entities are tracked as devastated enemy units for casualty/prisoner handling but are not normal salvage.
- `kills`: maps killed enemy display names to the killer's `externalId`, or `None`.

Open validation need: the exact minimal XML emitted for each entity should be validated in issue `#10` with a real or generated round trip, because entity damage, ammo, crit, crew, transport, and ejection details are serialized throughout `EntityListFile.writeEntityList(...)`.

Issue `#10` partial validation: `BATTLE_RECORD_MUL_ROUND_TRIP_VALIDATION.md` confirms locally that a generated `<record>` file using `EntityListFile.writeEntityList(...)` round-trips through `MULParser` with `survivors`, `salvage`, `retreated`, `devastated`, `kills`, entity external ids, crew external ids, and crew hits preserved. The remaining unvalidated step is live MekHQ Resolve Manually import into a disposable campaign.

## Campaign Mapping

`Confirmed from source`: [GameThread.java](../../external/src/mekhq/MekHQ/src/mekhq/GameThread.java) sets each campaign unit entity's external id before sending it to MegaMek:

```java
entity.setExternalIdAsString(unit.getId().toString());
```

`Confirmed from source`: `ResolveScenarioTracker.loadUnitsAndPilots(File)` uses entity `externalId` to match result entities back to `unitsStatus` entries that were initialized from the scenario's deployed campaign units.

`Confirmed from source`: `MULParser` reads entity `externalId` into `Entity#setExternalIdAsString(...)`, and reads crew-member `externalId` into crew slots. `EntityListFile` writes both entity and crew external ids when present.

Implications for a generated tabletop result MUL:

- Friendly campaign units must retain their campaign `Unit` UUID in entity `externalId`.
- Campaign personnel should retain their `Person` UUIDs in crew external ids so MekHQ can assign wounds, deaths, missing status, deployment XP, and kill credit to the correct personnel.
- Enemy entities may have generated UUIDs if they do not correspond to campaign units, but stable UUIDs make salvage/prisoner/ejection state easier to reason about.
- A missing or `-1` external id prevents direct campaign-unit matching and can cause MekHQ to treat an entity as allied/new salvage rather than the intended campaign unit.

## Personnel And Casualty Handling

`Confirmed from source`: after loading MUL sections, `ResolveScenarioTracker.processMulFiles()` calls `recoverUnfoundDeployedUnits()` and `checkStatusOfPersonnel()`.

Important behavior:

- If no MUL file is selected, `initUnitsAndPilotsWithoutBattle()` returns all deployed units and crew in pre-battle state. This is useful for manual resolution without a result file, but it does not encode tabletop damage.
- Personnel status is inferred from the post-battle entity and crew state. For single-pilot units, crew hits are used directly. For vehicles, infantry, large craft, transported units, and ejections, MekHQ uses additional logic and some random rolls.
- Ejected friendly crew are tracked through `EjectedCrew` entities and crew external ids.
- Crew not found in `pilots` or `mia` can be treated as dead for devastated cases.
- Kill credit is assigned later from the `kills` map by matching killer external id to a campaign unit.

Implication: the later result schema needs enough information to represent entity damage/removal section, crew hits/ejection, and kill credit. It should not try to precompute every personnel consequence MekHQ already computes.

## Salvage And Scenario Closeout

`Confirmed from source`: `ResolveScenarioTracker.resolveScenario(ScenarioStatus, String)` applies the wizard-selected status and report after the import and review steps. It updates personnel, units, finances/BLC, salvage, loot, scenario status, scenario report, formation assignments, game entities, networks, and scenario date.

Important behavior:

- Battlefield control is computed before import in `MekHQ.resolveScenario(Scenario)` and passed into `ResolveScenarioTracker`.
- Enemy entities in the `salvage` section become potential salvage only when `control` is true.
- Friendly wrecks in `salvage` can become total losses when `control` is false.
- CamOps salvage and standard salvage are handled after the wizard flow by `SalvagePostScenarioPicker` or `CamOpsSalvageUtilities.resolveSalvage(...)`.
- Scenario final status is not read from the MUL; the wizard-selected `ScenarioStatus` is applied during closeout.

Open issue: issue `#7` still needs to document the salvage rules and options in more detail. This issue confirms the source workflow and where salvage hooks are applied.

## Guidance For Child Issues

- `#9` schema design is complete in `TABLETOP_RESULT_INPUT_SCHEMA.md`; it models table-captured result facts separately from future battle-record MUL generation fields.
- `#10` round-trip validation should compare a generated `<record>` file against MekHQ import behavior, especially friendly unit matching, crew hits/ejection, salvage, retreated units, devastated units, and kill credit.
- `#10` has completed the installed-jar writer/parser part of that validation in `BATTLE_RECORD_MUL_ROUND_TRIP_VALIDATION.md`; live campaign import remains blocked on UI automation or user-operated validation.
- `#11` generation strategy should prefer using MegaMek/MekHQ serialization APIs or generated entities over hand-written XML where possible, because entity XML is broad and source-owned.
- `#12` implementation must preserve entity and crew external ids.
- `#13` manual verification should include Resolve Manually with a selected battle-record MUL, wizard review, scenario status selection, salvage dialogs, and post-closeout campaign inspection.
