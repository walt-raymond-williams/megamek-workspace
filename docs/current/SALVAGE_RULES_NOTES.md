# Salvage Rules Notes

This note records how MekHQ `0.51.00` decides post-scenario salvage outcomes and what that means for a generated tabletop battle-record MUL.

Evidence labels:

- `Confirmed from source`: directly observed in the local MekHQ source checkout.
- `Confirmed from local docs`: observed in installed MekHQ resource text or bundled docs.
- `Confirmed from official public source`: observed on an official public Catalyst Game Labs page.
- `Inferred from source`: follows from source behavior but has not been UI-tested in this workspace.
- `Unconfirmed`: not yet verified against the active campaign save or a user-provided BattleTech rulebook page.

## Summary

- `Confirmed from source`: salvage candidates are created from battle result entities, mainly enemy entities in the battle-record MUL `salvage` section when the player claims battlefield control.
- `Confirmed from source`: battlefield control is a MekHQ resolve-time choice, not a field read from the MUL. It affects whether enemy wrecks become potential salvage and whether friendly wrecks are lost.
- `Confirmed from source`: the Resolve Scenario wizard lets the player classify potential salvage as kept, sold, escaped, or left to the employer. Those choices feed `ResolveScenarioTracker` lists and are applied during scenario closeout.
- `Confirmed from source`: contract salvage rights are value-based. MekHQ tracks cumulative player and employer salvage value on the contract and computes the player's current salvage percentage from those values.
- `Confirmed from source`: salvage exchange does not let the player keep selected wrecks in the normal wizard path. Instead, employer-assigned salvage value can generate a salvage-exchange cash payout.
- `Confirmed from source`: CamOps salvage changes the workflow by requiring pre-scenario salvage formations and salvage techs, then post-scenario recovery capacity/time validation before `CamOpsSalvageUtilities.resolveSalvage(...)` applies the result.
- `Confirmed from local docs`: MekHQ's CamOps salvage UI says it is a basic implementation of Rules as Written salvage rules, with no field stripping and no salvage-quality weapon effects.
- `Unconfirmed`: the active demo campaign's salvage options are unknown until the save is inspected.

## Source Map

Primary source references:

- `mekhq.MekHQ#playerHasFieldControl`
  - Prompts the player to claim or yield battlefield control before constructing the `ResolveScenarioTracker`.
- `mekhq.campaign.ResolveScenarioTracker#loadUnitsAndPilots`
  - Loads `survivors`, `allies`, `salvage`, `retreated`, and `devastated` result sections from the MUL parser.
- `mekhq.campaign.ResolveScenarioTracker#resolveScenario`
  - Applies personnel, unit losses, battle loss compensation, salvage, loot, scenario status, reports, and cleanup.
- `mekhq.gui.dialog.ResolveScenarioWizardDialog`
  - Builds the non-CamOps salvage page and records keep/sell/leave/escape choices.
- `mekhq.campaign.mission.camOpsSalvage.CamOpsSalvageUtilities#resolveSalvage`
  - Adds kept salvage to the campaign, pays immediate sales, applies salvage exchange, and records contract salvage value.
- `mekhq.gui.dialog.camOpsSalvage.SalvagePostScenarioPicker`
  - Handles CamOps post-scenario recovery assignment, time/capacity validation, and final salvage allocation.
- `mekhq.campaign.mission.Contract`
  - Stores `salvagePct`, `salvageExchange`, `battleLossComp`, `salvagedByUnit`, and `salvagedByEmployer`.
- `mekhq.campaign.mission.AtBContract`
  - Adds Clan-era special salvage behavior and StratCon deployment timing.
- `mekhq.campaign.unit.Unit#canSalvage`
  - Checks whether a unit can participate in CamOps salvage operations.

## Battle-Record MUL Inputs

`Confirmed from source`: issue `#8` established that manual tabletop results should be imported as a battle-record `<record>` MUL. Salvage-relevant sections are:

- `survivors`: friendly or allied units that remain on the field. They can still become losses if they cannot escape and the player yields control, or if their removal condition is devastated.
- `allies`: allied units tracked for AtB-style scenarios.
- `salvage`: wrecked or otherwise salvageable entities. Friendly campaign units in this section are matched by `externalId`; enemy entities in this section become potential salvage only when battlefield control is true.
- `retreated`: escaped entities. Friendly campaign units here are found and are not total losses.
- `devastated`: utterly destroyed entities. Friendly campaign units are total losses; enemy entities are tracked for devastated enemy and prisoner/casualty handling, not as ordinary kept salvage.
- `kills`: kill credit mapping. It does not directly create salvage, but it affects personnel kill records.

## Battlefield Control

`Confirmed from source`: `MekHQ.resolveScenario(...)`, live-game resolution, and auto-resolve all call `playerHasFieldControl(...)` before building the tracker. The prompt offers control or yield, with extra AtB dynamic-scenario context when the scenario template defines battlefield control text.

Effects observed in source:

- Friendly units in the `salvage` section are assigned as total losses when the player does not control the field.
- Friendly units in `survivors` or `allies` can still be marked lost when they cannot escape and the player does not control the field.
- Enemy entities in `salvage` become `potentialSalvage` only when the player controls the field.
- Enemy ejected crew in `salvage` are handled as prisoners/traitors/ejections rather than as units to keep.
- Live-game resolution has analogous behavior for graveyard and devastated entities.

Implication: the generator should not try to encode battlefield control into the MUL. It should ensure correct result sections and then let the MekHQ resolve prompt supply control.

## Non-CamOps Salvage Flow

`Confirmed from source`: when `CampaignOptions.isUseCamOpsSalvage()` is false, the Resolve Scenario wizard shows the standard salvage panel if `tracker.getPotentialSalvage()` is non-empty and the mission allows salvage.

The wizard choices map to tracker lists:

- Keep selected salvage: `ResolveScenarioTracker#salvageUnit` adds the candidate to `actualSalvage`.
- Sell selected salvage immediately: `ResolveScenarioTracker#sellUnit` adds it to `ransomedSalvage`, exposed as `getSoldSalvage()`.
- Leave it to the employer: `ResolveScenarioTracker#doNotSalvageUnit` adds it to `leftoverSalvage`.
- Mark escaped: the candidate is not added to kept, sold, or leftover lists.

During closeout, `ResolveScenarioTracker#resolveScenario` calls:

```text
CamOpsSalvageUtilities.resolveSalvage(campaign, mission, scenario, actualSalvage, soldSalvage, leftoverSalvage)
```

`Confirmed from source`: `resolveSalvage(...)` then:

- Adds kept salvage as campaign test units with the scenario/contract delivery time.
- Credits cash for immediate sale of sold salvage.
- Adds kept or sold salvage value to `Contract.salvagedByUnit`.
- Adds leftover salvage value to `Contract.salvagedByEmployer`.
- If the contract uses salvage exchange, computes the player's payout as a percentage of employer-take-home salvage value and credits `SALVAGE_EXCHANGE`.

## Contract Salvage Rights

`Confirmed from source`: `Contract` defaults to 50 percent salvage rights and 50 percent battle loss compensation for general contracts, but actual contracts can change these values.

Important fields:

- `salvagePct`: maximum player salvage share in normal salvage rights, or the exchange payout percent in salvage exchange.
- `salvageExchange`: switches from keep/sell selection to exchange-style compensation.
- `battleLossComp`: percent used for battle loss compensation on lost units or missing/damaged parts.
- `salvagedByUnit`: cumulative value assigned to the player's side across the contract.
- `salvagedByEmployer`: cumulative value assigned to the employer side across the contract.

`Confirmed from source`: `Contract.calculateSalvagePercentage(playerShare, employerShare)` computes the player's current salvage share as an integer percent using ceiling rounding. This is why a tiny excess over the cap can be shown as over the contract limit.

`Confirmed from source`: the non-CamOps wizard disables additional keep/sell choices when the current calculated share exceeds the contract cap and the cap is below 100 percent. With 100 percent salvage rights, the player can keep everything.

`Confirmed from source`: if `Contract.isSalvageExchange()` is true, standard keep/sell boxes are disabled in the non-CamOps wizard. The later `resolveSalvage(...)` path pays the player a percentage of employer-assigned salvage instead.

`Confirmed from source`: `ResolveScenarioTracker#isEmployerEvokingSpecialClause` treats an AtB pre-Tukayyid Clan-enemy/non-Clan-employer contract as salvage exchange. `AtBContract` also contains Clan salvage override/special-clause logic.

## Battle Loss Compensation

`Confirmed from source`: battle loss compensation is separate from salvage selection.

During `ResolveScenarioTracker#resolveScenario`:

- Total-loss campaign units are removed from the campaign and can generate BLC based on `Contract.getBattleLossComp()`.
- Recovered campaign units are updated with their post-battle entity state. If they are not repairable, MekHQ marks them as salvage.
- Missing or newly damaged parts can generate BLC based on the value difference, adjusted by campaign options such as `isBLCSaleValue()` and `isPayForRepairs()`.

Implication: the result MUL should capture the unit's actual damage and section status. MekHQ calculates BLC from campaign units, contract terms, and campaign options during closeout.

## CamOps Salvage

`Confirmed from source`: CamOps salvage is controlled by `CampaignOptions.isUseCamOpsSalvage()` and `CampaignOptions.isUseRiskySalvage()`.

Pre-scenario:

- `BriefingTab#handleSalvageAssignments` runs only when CamOps salvage is enabled.
- If the mission is a contract with `salvagePct <= 0`, MekHQ treats it as no salvage opportunity.
- The player can select salvage-capable formations with `SalvageFormationPicker`.
- MekHQ then selects or confirms salvage techs with `SalvageTechPicker`.
- In StratCon, `CamOpsSalvageUtilities#deploySalvageTeams` assigns selected salvage teams to the track location.

Salvage-capable units:

- `Unit#canSalvage(boolean isInSpace)` requires a valid entity, appropriate ground or space salvage capability, and full crew.
- Non-Mek salvage units need cargo capacity, or in space a naval tug adaptor.
- The installed UI text says only Meks and vehicles can perform ground salvage, while only DropShips and WarShips can perform space salvage.

Post-scenario:

- `ResolveScenarioTracker#resolveScenario` shows `SalvagePostScenarioPicker` only when the player controls the field and both salvage formations and salvage techs are assigned.
- The picker validates recovery time against assigned tech minutes and validates tow/cargo/tug/bay capacity.
- Valid kept/sold/employer allocations are then processed through the same `CamOpsSalvageUtilities#resolveSalvage(...)` method.
- If risky salvage is enabled, `performRiskySalvageChecks(...)` rolls once per salvaged unit for a 2d6 result of 2; each event injures a random assigned tech, with possible Edge reroll behavior.
- Assigned salvage techs have their remaining work minutes depleted after the operation.

`Confirmed from local docs`: MekHQ's salvage formation picker says this is a basic implementation of Campaign Operations-style salvage rules; field stripping and salvage-quality weapon effects are not implemented.

## BattleTech Rule Comparison

`Confirmed from official public source`: Catalyst Game Labs describes `BattleTech: Campaign Operations` as containing rules for creating and running forces and campaign play options. See the official product page: `https://store.catalystgamelabs.com/products/battletech-campaign-operations-pdf`.

`Confirmed from local docs`: MekHQ's own CamOps salvage UI points users to `Campaign Operations` for the full salvage rules and explicitly says MekHQ implements only a basic subset.

High-level comparison:

- MekHQ follows the broad Campaign Operations idea that battlefield recovery depends on having field access, recovery-capable units, technical personnel, and time.
- MekHQ abstracts or omits some details. Local UI text explicitly says field stripping and salvage-quality weapon effects are not implemented.
- MekHQ integrates salvage with its own contract model: percentage caps, exchange rights, cumulative player/employer salvage value, immediate sales, and BLC accounting.
- MekHQ lets the player decide battlefield control at scenario resolution. A tabletop group may decide control from scenario objectives or narrative rules first, then choose the matching MekHQ prompt.

`Unconfirmed`: this workspace does not contain the Campaign Operations rulebook text, so this note does not verify page-level RAW details. A future rules audit should use the user's preferred rulebook and page references, especially if the tabletop workflow should enforce a stricter version of Campaign Operations outside MekHQ.

## Implications For The Tabletop Result MUL Workflow

The generator should record:

- Which player units survived, retreated, were wrecked/salvageable, or were devastated.
- Which enemy units are wrecked/salvageable versus devastated versus retreated.
- Entity damage state, missing/blown-off locations, crits, ammo, crew state, ejections, and transport relationships as accurately as the future schema supports.
- Stable `externalId` values for campaign units and crew so MekHQ can match units/personnel.
- Kill credit where known.

The generator should not decide:

- Final battlefield control. MekHQ prompts for it during resolution.
- Final contract salvage allocation. MekHQ's wizard and contract state enforce this.
- Final BLC values. MekHQ calculates these from the campaign unit state and contract.
- Final CamOps recovery feasibility. MekHQ validates assigned salvage teams, techs, time, capacity, and risky salvage.

Design consequences for child issues:

- `#9` should include a clear result-state field for each unit that maps to `survivors`, `salvage`, `retreated`, or `devastated`.
- `#9` should keep battlefield control outside the MUL schema or mark it as an operator instruction for MekHQ's prompt.
- `#10` should validate at least four cases: player controls field and keeps enemy salvage; player yields field and loses friendly wrecks; enemy unit retreats and is not salvage; enemy unit is devastated and does not become normal salvage.
- `#13` should document that CamOps salvage requires pre-scenario salvage-team assignment before the battle starts or auto-resolves.

## Open Questions

- Which optional salvage settings are enabled in the active campaign save?
- Does the user want Campaign Operations RAW, Chaos Campaign, or MekHQ's built-in behavior as the authoritative tabletop baseline?
- Should the future generator expose a "field control recommendation" based on tabletop objectives, even though MekHQ receives control through a prompt?
