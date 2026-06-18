# Quickstart Roster Replacement Verification

This note tracks GitHub issue `#17`: verifying the no-source-change workflow for replacing the New Player Quickstart roster in a disposable campaign save.

Evidence labels follow `DOCUMENTATION_WORKFLOW.md`.

## Status

- `Confirmed locally`: `external/installs/MekHQ-0.51.00/MekHQ.exe` exists.
- `Confirmed locally`: the bundled quickstart save exists at `external/installs/MekHQ-0.51.00/campaigns/The Learning Ropes.cpnx.gz`.
- `Confirmed locally`: a disposable copy was created at `analysis/tmp/issue-17-roster-test-The Learning Ropes.cpnx.gz`.
- `Confirmed from save`: the disposable copy decompresses as campaign XML with root `<campaign version="0.51.00">`, name `The Learning Ropes`, date `3025-04-08`, and faction `MERC`.
- `Confirmed from source`: the UI and campaign code paths for GM add/remove are present and coherent.
- `Blocked`: live UI click-through was not completed because the Windows Computer Use helper reported `Computer Use native pipe path is unavailable`. The remaining user-operated validation is tracked by GitHub issue `#21` and checklist `docs/handoffs/active/user-quickstart-roster-ui-validation.md`.

## Verified Source Path

`Confirmed from source`: `CampaignGUI` creates a top-level `GM Mode` toggle button. When clicked, it calls `getCampaign().setGMMode(btnGMMode.isSelected())` and refreshes GM menu items.

`Confirmed from source`: `CampaignGUI.showUnitMarket()` has two add-unit paths:

- If the campaign unit market is enabled, it opens `UnitMarketDialog`.
- If the unit market is disabled, it opens `MekHQUnitSelectorDialog`.

`Confirmed from source`: in `UnitMarketDialog`, GM mode adds `Add (GM)` and `Remove` buttons. `Add (GM)` calls `UnitMarketPane.addSelectedOffers()`, which calls `Campaign.addNewUnit(...)` with instant delivery and no funds debit.

`Confirmed from source`: in `MekHQUnitSelectorDialog`, the `Add (GM)` button calls `addGM()`, which calls `campaign.addNewUnit(selectedUnit.getEntity(), false, 0, quality)`.

`Confirmed from source`: `Campaign.addNewUnit(...)` creates a campaign `Unit`, adds it to the hangar, sets the entity owner/game/external id, initializes parts, runs diagnostics, sets salvage if not repairable, sets arrival days, optionally generates crew, resets pilot/entity state, sets quality, adds the entity to the game, and initializes transport space.

`Confirmed from source`: in `UnitTableMouseAdapter`, the Hangar context menu adds `GM Mode > Remove Unit` only when `campaign.isGM()` is true. That command asks for confirmation and then calls `Campaign.removeUnit(...)`.

`Confirmed from source`: `Campaign.removeUnit(...)` removes unit parts, crew and tech assignments, formation placement, transport references, automatic mothball tracking, hangar state, duplicate-name tracking, and emits a removal report/event.

## Repeatable Manual Workflow

Use this workflow for a disposable verification run or for the user-facing campaign setup.

1. Start MekHQ from `external/installs/MekHQ-0.51.00/MekHQ.exe`.
2. Choose `New Player Quickstart`.
3. Immediately save the campaign under a new name/path, not over the bundled quickstart.
4. Toggle `GM Mode` on from the main campaign UI.
5. Open the Unit Market / Unit selector.
6. Add one replacement unit using `Add (GM)`.
7. Open the Hangar.
8. Right-click one unwanted quickstart unit and choose `GM Mode > Remove Unit`.
9. Confirm the removal prompt.
10. Reassign or review follow-up state:
    - pilots and crew
    - technicians
    - TO&E formation placement
    - transport and cargo assignments
    - scenario deployments, if any exist
11. Save the campaign again under the disposable path.
12. Inspect the saved campaign or UI roster to confirm the added unit exists and the removed unit is gone.

## Verification Gap

`Unknown`: the exact unit to add/remove for a meaningful player-roster test. Use placeholder units only for workflow validation; use the user's physical miniature list for a real campaign setup.

`Blocked`: this issue's full acceptance criteria require a live UI pass showing at least one unit added and one unit removed in a copied save. Source and save safety are confirmed, but the click-through itself remains pending. The user-owned unblocker is issue `#21`.

## Recommendation

Treat the workflow as source-confirmed and ready for a user-operated live pass. Do not build a save-editing tool for this step unless live UI verification proves the built-in GM workflow is too slow or error-prone.

## Source References

- `mekhq.gui.CampaignGUI#createButtonsPanel`
- `mekhq.gui.CampaignGUI#showUnitMarket`
- `mekhq.gui.dialog.UnitMarketDialog#createButtonPanel`
- `mekhq.gui.panes.UnitMarketPane#addSelectedOffers`
- `mekhq.gui.dialog.MekHQUnitSelectorDialog#addGM`
- `mekhq.gui.adapter.UnitTableMouseAdapter`
- `mekhq.campaign.Campaign#addNewUnit`
- `mekhq.campaign.Campaign#removeUnit`
