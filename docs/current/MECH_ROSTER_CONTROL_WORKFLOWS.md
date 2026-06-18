# MekHQ Roster Control Workflows

This note records source- and local-doc-supported ways to control player and OPFOR rosters for a physical-miniatures MekHQ campaign.

Evidence labels follow `DOCUMENTATION_WORKFLOW.md`.

## Summary

- `Confirmed from source`: the New Player Quickstart opens the bundled campaign `campaigns/The Learning Ropes.cpnx.gz`; it is a campaign save, not a generated starting-roster wizard.
- `Confirmed from source`: the safest player-roster workflow is to work in a copied/saved campaign and use MekHQ's unit add/remove flows, not direct campaign XML edits.
- `Confirmed from source`: GM-mode unit add uses `Campaign.addNewUnit(...)`, which creates a campaign `Unit`, initializes parts, assigns the entity external id to the unit UUID, adds it to the game, initializes transport space, and sets quality.
- `Confirmed from source`: GM-mode unit removal uses `Campaign.removeUnit(...)`, which removes unit parts, crew/tech assignments, formation placement, transport references, hangar state, duplicate-name tracking, and emits a removal report/event.
- `Confirmed from local docs`: StratCon's documented quick fix for bad OPFOR is Briefing tab scenario edit followed by `Regenerate Bot Forces`.
- `Confirmed from source`: `Regenerate Bot Forces` calls `AtBDynamicScenarioFactory.finalizeScenario(...)`, which clears old bot forces and reruns the scenario-template force generation path.
- `Confirmed from source`: scenario editing can also add, edit, and delete bot formations; bot formations can load fixed units from MUL files, save their fixed unit list to MUL, and configure random-unit generation parameters.
- `Confirmed from local docs`: custom AtB RATs require RAT files under `data/rat` and metadata XML under `data/universe/ratdata`; those collections appear in the AtB RAT configuration section of Campaign Options.

## Player Starting Roster

`Confirmed from source`: `MHQConstants.LAUNCHER_NEW_PLAYER_QUICKSTART_PATH` points at `campaigns/The Learning Ropes.cpnx.gz`, and `StartupScreenPanel` uses that constant for the New Player Quickstart button. This means the quickstart roster is stored in the save.

`Confirmed from source`: the quickstart roster replacement workflow is documented in `QUICKSTART_ROSTER_REPLACEMENT_VERIFICATION.md`. It confirms the GM add/remove source path and safe disposable-save setup; live UI click-through remains pending.

Recommended practical workflow:

1. Start the New Player Quickstart, then immediately save under a new campaign name/path.
2. Enable GM mode if exact roster control matters more than acquisition rolls.
3. Add legal physical-miniature units through the Unit Market button:
   - If the campaign's unit-market method is enabled, `CampaignGUI.showUnitMarket()` opens `UnitMarketDialog`; in GM mode, `Add GM` adds selected offers instantly without debiting funds.
   - If the unit-market method is disabled, `CampaignGUI.showUnitMarket()` opens `MekHQUnitSelectorDialog`; in GM mode, `Add GM` directly adds the selected unit.
4. Remove unwanted quickstart units from the Hangar right-click `GM Mode > Remove Unit`.
5. Reassign pilots, technicians, TO&E slots, transport assignments, and scenario deployments in the UI after roster changes.

Important mechanics:

- `Confirmed from source`: `MekHQUnitSelectorDialog.addGM()` calls `campaign.addNewUnit(selectedUnit.getEntity(), false, 0, quality)`. The `false` means the GM-added unit does not automatically receive new pilots from this path.
- `Confirmed from source`: `UnitMarketPane.addSelectedOffers()` finalizes selected market offers with instant delivery but also calls `addNewUnit(..., false, ...)`; personnel still need separate assignment or hiring.
- `Confirmed from source`: normal purchase paths debit finances or add shopping-list acquisition work. GM add is cleaner for a parent-run physical roster where the desired starting force is a scenario premise rather than a market result.

Open validation:

- `Unknown`: the exact UI path for enabling GM mode in this install has not been verified in a live session.
- `Unknown`: the best balance between preserving the quickstart's personnel/parts inventory and replacing units story-first depends on the user's desired starting roster.

## OPFOR Control

There are three useful levels of OPFOR control.

### Fast Per-Scenario Control

`Confirmed from local docs`: `external/installs/MekHQ-0.51.00/docs/StratCon/stratcon-faq-2.6.md` says that if OPFOR is too large, absent, not TO&E-based, or unwinnable, the user can right-click the scenario in the Briefing tab, choose edit, and click `Regenerate Bot Forces`.

`Confirmed from source`: in `CustomizeScenarioDialog`, current `AtBDynamicScenario` instances show a `Regenerate Bot Forces` button when bot forces already exist. That button calls `AtBDynamicScenarioFactory.finalizeScenario(...)`.

`Confirmed from source`: `finalizeScenario(...)` clears old bot forces, clears attached/player-allied and bot-template lookup state, recalculates player effective BV/unit count, regenerates forces from the scenario template, resets map/deployment/destination data, applies modifiers, upgrades bot crews if abilities are enabled, and marks the scenario finalized.

Use this when the generated OPFOR is basically acceptable but one draw is bad.

### Manual Physical-Miniatures Substitution

`Confirmed from source`: `CustomizeScenarioDialog` has an `Other Forces` tab with Add Formation, Edit Formation, and Delete Formation actions backed by `BotForce` objects.

`Confirmed from source`: `CustomizeBotForceDialog` can:

- change bot name, team, deployment, camouflage, and Princess behavior
- load fixed bot units from a MUL file through `MULParser`
- save fixed bot units to a MUL file through `EntityListFile.saveTo(...)`
- delete fixed bot units
- configure a bot-force randomizer with faction, unit type, skill, quality, focal weight class, force multiplier, conventional-unit percentage, battle armor chance, lance size, and balancing method

`Confirmed from source`: the scenario details view also lets the user right-click an individual visible bot unit and open `UnitEditorDialog`; blind-drop hides exact unit details.

Recommended physical-table workflow:

1. Let MekHQ generate the scenario to establish mission, map, deployment, BV scale, and objectives.
2. If the OPFOR is unsuitable, try `Regenerate Bot Forces`.
3. If physical minis must be exact, edit the bot formation and load a curated MUL made from available tabletop units, or add/delete bot formations manually.
4. Keep BV and role close to MekHQ's generated intent unless deliberately changing difficulty.

This is the simplest no-source-change path for "we only own these minis tonight."

### Durable Generation Control

`Confirmed from local docs`: custom AtB RAT metadata lives in `data/universe/ratdata/*.xml`, references RAT names, eras, factions, unit types, weight classes, and equipment ratings, and points at RAT files in `data/rat`.

`Confirmed from source`: scenario templates can generate forces by methods including fixed MUL and BV-scaled generation. `AtBDynamicScenarioFactory.generateFixedForce(...)` loads a fixed MUL from `MHQConstants.STRAT_CON_MUL_FILES_DIRECTORY`; `generateForce(...)` uses scenario force templates, contract faction/skill/quality, campaign difficulty, weather/planetary restrictions, unit type, roles, and BV/unit-count budgets.

Use custom RATs or fixed-MUL scenario templates only after the physical miniature list exists. Until then, manual substitution is lower risk and much faster.

## Recommendation

For the first playable parent-run campaign, avoid source changes and avoid campaign save surgery.

- Player roster: copy/save the quickstart campaign, use GM-mode Add GM to add exact player units, remove unwanted quickstart units through Hangar GM Mode, then manually assign pilots/TO&E/transport.
- OPFOR: let StratCon generate scenarios, use `Regenerate Bot Forces` first, then manually edit bot formations or load fixed OPFOR MULs when physical-mini availability matters.
- Later data tooling: once the user's miniature list is known, create a small roster data file and generate either fixed OPFOR MULs or custom RAT/ratdata entries.

## Child-Issue Candidates

1. Verify the quickstart roster replacement workflow in a disposable save.
   - Acceptance: a copied quickstart campaign has one or more original units removed, one exact chosen replacement unit added by GM mode, pilots/TO&E/transport reassigned, and the resulting campaign saved without touching the bundled quickstart.
2. Define the physical miniature roster data model.
   - Acceptance: a small CSV/Markdown/JSON schema records chassis, variant, quantity, faction/era notes, unit type, weight, BV if available, and whether the miniature can be player, OPFOR, or either.
3. Prototype fixed OPFOR MUL pools.
   - Acceptance: create a few curated OPFOR MUL files from the miniature list and verify they can be loaded through `CustomizeBotForceDialog`.
4. Decide whether custom RATs are worth it.
   - Acceptance: compare manual MUL substitution against custom `data/rat` plus `data/universe/ratdata` entries using the user's miniature list and one generated StratCon scenario.

## Source References

- `mekhq.MHQConstants#LAUNCHER_NEW_PLAYER_QUICKSTART_PATH`
- `mekhq.gui.panels.StartupScreenPanel`
- `mekhq.gui.CampaignGUI#showUnitMarket`
- `mekhq.gui.dialog.UnitMarketDialog`
- `mekhq.gui.panes.UnitMarketPane#purchaseSelectedOffers`
- `mekhq.gui.panes.UnitMarketPane#addSelectedOffers`
- `mekhq.gui.dialog.MekHQUnitSelectorDialog#addGM`
- `mekhq.gui.adapter.UnitTableMouseAdapter`
- `mekhq.campaign.Campaign#addNewUnit`
- `mekhq.campaign.Campaign#removeUnit`
- `mekhq.gui.dialog.CustomizeScenarioDialog`
- `mekhq.gui.dialog.CustomizeBotForceDialog`
- `mekhq.gui.view.AtBScenarioViewPanel`
- `mekhq.campaign.mission.BotForce`
- `mekhq.campaign.mission.AtBDynamicScenarioFactory#finalizeScenario`
- `mekhq.campaign.mission.AtBDynamicScenarioFactory#generateFixedForce`
- `mekhq.campaign.mission.AtBDynamicScenarioFactory#generateForce`
- `external/installs/MekHQ-0.51.00/docs/StratCon/Custom RATs.txt`
- `external/installs/MekHQ-0.51.00/docs/StratCon/stratcon-faq-2.6.md`
