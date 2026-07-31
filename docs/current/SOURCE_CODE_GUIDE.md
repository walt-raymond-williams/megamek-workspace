# Source Code Guide

This workspace relies heavily on source inspection. The source checkouts are not decoration; they are how agents should answer "how does this actually work?"

## Local Source Locations

```text
C:\Users\waltr\Documents\megamek-workspace\external\src\megamek
C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq
C:\Users\waltr\Documents\megamek-workspace\external\src\megameklab
C:\Users\waltr\Documents\megamek-workspace\external\src\mm-data
```

Installed suite for runtime comparison:

```text
C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00
```

## How To Use Source

Use source when answering:

- what a campaign XML field means
- where saves are loaded or written
- how a campaign action is triggered from the UI
- how MekHQ launches MegaMek scenarios
- how contracts, finances, repairs, personnel, markets, and StratCon systems calculate outcomes
- whether command-line, import/export, or automation hooks exist
- what source change would be needed to expose better campaign control

For source modification, follow `SOURCE_CHANGE_WORKFLOW.md`. For local commands, use `KNOWN_COMMANDS.md`.

## Search Patterns

Start broad, then narrow:

```powershell
rg "cpnx" C:\Users\waltr\Documents\megamek-workspace\external\src
rg "Campaign" C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq
rg "save" C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq
rg "Scenario" C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq
rg "launch" C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq
rg "MULParser|MtfFile|BLKFile|ScenarioLoader" C:\Users\waltr\Documents\megamek-workspace\external\src\megamek
```

When a code path is important, record the class/file and the conclusion in the relevant `docs/current/` note.

### MekHQ Campaign Save/Load

Use these entry points when answering how `.cpnx`, `.cpnx.gz`, or campaign XML files work:

```powershell
rg "cpnx|CampaignXml|load.*campaign|save.*campaign" C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq\MekHQ\src -g "*.java"
rg "GZIPInputStream|GZIPOutputStream|writeToXML|CampaignXmlParser" C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq\MekHQ\src -g "*.java"
```

Confirmed source map:

- `mekhq.campaign.CampaignFactory`: load entry point. `createCampaign(InputStream)` wraps non-markable streams, checks the first two bytes for the gzip magic header, decompresses gzip saves, otherwise treats the stream as XML, then delegates to `CampaignXmlParser`.
- `mekhq.campaign.io.CampaignXmlParser`: XML parser. `parse()` creates a `Campaign`, parses the DOM, reads root version, performs milestone upgrade-path handling, checks null entities, and processes top-level campaign nodes.
- `mekhq.campaign.Campaign`: canonical XML writer. `writeToXML(PrintWriter, boolean)` writes the root `campaign` element and major campaign sections.
- `mekhq.gui.CampaignGUI`: manual save implementation. `saveCampaign(...)` appends `.cpnx` when needed, backs up existing files, writes gzip only when the path ends in `.gz`, and delegates XML content to `Campaign#writeToXML(...)`.
- `mekhq.gui.FileDialogs` and `mekhq.io.FileType`: file-dialog extension behavior. Campaign open/save uses `FileType.CPNX`, which accepts `cpnx`, `cpnx.gz`, and `xml`; the default save extension follows the user's gzip preference.
- `mekhq.service.AutosaveService`: autosave implementation. Autosaves are written through `GZIPOutputStream` and named `Autosave-<n>-<campaign>-<date>.cpnx.gz`.

## Modification Posture

If the user asks to modify MegaMek/MekHQ:

1. Work in the appropriate source repo under `C:\Users\waltr\Documents\megamek-workspace\external\src`.
2. Check `git status --short --branch` before edits.
3. Follow `SOURCE_CHANGE_WORKFLOW.md`.
4. Keep this workspace for notes, plans, campaign reports, and durable discoveries.
5. Verify with the source repo's own build/test workflow, using `KNOWN_COMMANDS.md` for current commands and blockers.
6. If the change affects campaign control, update this workspace's current docs.

## Current Build Notes

- `Confirmed from source`: the four local source repos use Gradle wrappers.
- `Confirmed from source`: project build files configure Java toolchain 21.
- `Confirmed locally`: Gradle wrapper execution is currently blocked by generated daemon JVM settings that request toolchain 17 without a local JDK 17 or configured toolchain download.

Do not claim a source build or test passed until the relevant Gradle command has actually run successfully.

## Current Source Findings

- `Confirmed from source`: The MEK-RPG read-only checkpoint contract should prefer a MekHQ-backed DTO/exporter over raw XML for derived values. Method/API owners include `Campaign#getLocalDate()`, `Campaign#getCurrentSystem()`, `Finances#getBalance()`, `Person#getSalary(Campaign)`, `Unit#getDamageState()`, `UnitMarketOffer#getPrice()`, `AbstractContractMarket#getContracts()`, `CargoStatistics`, and `HangarStatistics`. See `MEK_RPG_MEKHQ_CHECKPOINT_EXPORT.md` and the MEK-RPG-aligned draft schema in `MEK_RPG_MEKHQ_CHECKPOINT_EXPORT_SCHEMA.md`.
- `Confirmed from source`: MekHQ bridge automation for MEK-RPG should start with read-only checkpoint export. `Campaign#newDay()` is the core day-advance hook, but `CampaignNewDayManager#newDay()` currently reaches `CampaignGUI`/`CommandCenterTab`; contract, personnel, repair, and tactical-result actions have source-owned method candidates but meaningful GUI/dialog or selector blockers. See `MEK_RPG_MEKHQ_BRIDGE_PRIMITIVES.md`.
- `Confirmed from source`: MekHQ's manual tabletop-result path exports scenario setup MULs as ordinary `<unit>` files, but imports battle results as a MegaMek battle-record `<record>` MUL. See `TABLETOP_RESULT_MUL_WORKFLOW.md` for the source-confirmed flow through `BriefingTab`, `MekHQ.resolveScenario`, `ChooseMulFilesDialog`, `ResolveScenarioTracker`, `EntityListFile`, and `MULParser`.
- `Confirmed from source`: MekHQ salvage is resolved from battle-record result sections, battlefield-control choice, Resolve Scenario wizard choices, contract salvage terms, and optional CamOps salvage settings. See `SALVAGE_RULES_NOTES.md` for the source-confirmed flow through `ResolveScenarioTracker`, `ResolveScenarioWizardDialog`, `Contract`, `AtBContract`, `SalvagePostScenarioPicker`, `CamOpsSalvageUtilities`, and `Unit#canSalvage`.
- `Confirmed from source`: MekHQ roster-control workflows for physical-miniatures play are best handled first through UI/GM controls: quickstart is a bundled campaign save, GM unit add/remove uses `Campaign#addNewUnit` and `Campaign#removeUnit`, scenario OPFOR can be regenerated through `CustomizeScenarioDialog`, and fixed bot formations can load MULs through `CustomizeBotForceDialog`. See `MECH_ROSTER_CONTROL_WORKFLOWS.md`.
- `Confirmed from source`: quickstart roster replacement has a source-confirmed no-save-surgery path through the top-level `GM Mode` toggle, `Add (GM)`, and Hangar `GM Mode > Remove Unit`. See `QUICKSTART_ROSTER_REPLACEMENT_VERIFICATION.md`; live UI click-through is still pending.
- `Confirmed from source`: for DropShip hot-drop or tactical assault setup, assign the carried units to the DropShip with TO&E right-click `Assign Formation to Tactical Transport` or unit right-click `Assign Formation to Tactical Transport`, then deploy both the DropShip and the carried units to the scenario. `CampaignTransportType.TACTICAL_TRANSPORT` is explicitly intended for short-term tactical transport, including bay loading for tactical DropShip assaults. When MekHQ starts MegaMek, `AtBGameThread` prompts whether units assigned to the transport should deploy loaded, then calls `Utilities.loadPlayerTransports(...)` to issue MegaMek load commands. For AtB/StratCon dynamic scenarios, `ForceTemplateAssignmentDialog` also prompts to deploy transported units when deploying the transport, and `AtBDynamicScenarioFactory#setDeploymentTurnsForReinforcements(...)` excludes player-transported entities from the independent reinforcement arrival calculation so their timing follows the transport relationship. Key files: `CampaignTransportType`, `TOEMouseAdapter`, `AssignForceToTacticalTransportMenu`, `ForceTemplateAssignmentDialog`, `AtBGameThread`, and `Utilities`.
- `Confirmed from source`: MekHQ repair site is a per-unit setting, not an automatic consequence of being assigned to or carried by a DropShip. `Unit#getSiteMod()` applies the site modifier to repair and maintenance rolls through repair parts: Improvised `+2`, Field Workshop `+1`, Facility - Basic `+0`, Facility - Maintenance `-2`, and Factory Conditions `-4`. The UI exposes this through right-click `Change Site` on unit/service tables, and choosing Maintenance or Factory can invoke `FacilityRentals.processBayChangeRequest(...)`. Repairs also enforce location: `Campaign#fixPart(...)`, maintenance, and tech assignment paths require the tech and repair target to have the same effective location through `LocationUtils.areSameEffectiveLocation(...)`. DropShips and JumpShips determine their repair skill as `Tech/Vessel` via `Unit#determineUnitTechSkillType()` and `Person#canTech(...)`; large-craft parts such as docking collars, grav decks, KF components, spacecraft cooling, and large-vessel electronics use that skill where applicable. Key files: `Unit`, `Part`, `Campaign`, `Maintenance`, `LocationUtils`, `Person`, `UnitTableMouseAdapter`, `ServicedUnitsTableMouseAdapter`, and `FacilityRentals`.
- `Confirmed from source`: MekHQ campaign jump blocking for JumpShips checks for a drive core and delegates to `Jumpship#canJump()`, which only requires current KF integrity above zero. Individual damaged KF component flags are repairable state and crit-reporting state; they do not separately block campaign jumps unless the resulting KF integrity is reduced to zero. MegaMek KF-drive crits reduce KF integrity by one and, with expanded KF damage, mark subcomponents such as drive coil, charging system, field initiator, helium tank, drive controller, or LF battery. KF part repair/removal then restores or reduces the same integrity. Damaged JumpShip docking collars are functional blockers for carrying DropShips: MekHQ's ship-transport assignment checks `Entity#canLoad(...)`, and `DockingCollar#canLoad(...)` requires an undamaged collar and an undamaged DropShip docking collar. Key files: `JumpBlockers`, `Jumpship`, `TWGameManager`, `KFDriveCoil`, `KFHeliumTank`, `JumpshipDockingCollar`, `DockingCollar`, and `AssignForceToShipTransportMenu`.
- `Confirmed from source`: MekHQ routine maintenance for DropShips and JumpShips is performed by `CampaignNewDayManager#processNewDayUnits()` -> `Maintenance#doMaintenance(...)`. When due, each maintainable part rolls `2d6` against `Maintenance#getTargetForMaintenance(...)`; the margin (`roll - TN`), part quality, and the `useUnofficialMaintenance` option determine whether quality changes, damage is added, or the part is destroyed. Low-gravity worlds can worsen spacecraft maintenance checks: if planetary modifiers are enabled, the campaign is on a planet, and the unit site is below `Facility - Basic`, gravity below `0.8g` adds `+2` to the TN, partly or fully offset by the tech's `Zero-G Operations` skill. High gravity, atmosphere/pressure, and extreme temperature can also add penalties. A unit at `Facility - Basic` or better still gets the normal site modifier but skips planetary environment modifiers. Key files: `CampaignNewDayManager`, `Maintenance`, `Part`, `Unit`, `Campaign`, `HumanResources`, and `Planet`.
- `Confirmed from source`: current MegaMek calculates ordinary JumpShip KF integrity during BLK loading with `ceil(1.2 + jumpDriveWeight / 60000.0)`, where standard JumpShip drive weight is 95% of total unit weight. The 120,000-ton Merchant JumpShip BLK files in `mm-data` do not store an explicit KF-integrity override, so the current loader derives 4 integrity from a 114,000-ton standard drive (`ceil(1.2 + 1.9)`). This differs from a published Merchant record sheet value of 3; no Merchant-specific construction modifier or override was found in the current loader/data path. Key files: `Jumpship`, `BLKJumpshipFile`, `BLKFile`, and `mm-data/data/mekfiles/jumpships/3057R/IS/Merchant Jumpship (2503).blk`.
- `Confirmed from source`: MekHQ campaign saves are gzip-compressed XML only when the stream/file is actually gzip. Loading sniffs gzip magic bytes in `CampaignFactory`; saving writes gzip for `.gz` paths in `CampaignGUI`, while autosaves are always `.cpnx.gz`. See `SAVE_FORMAT_NOTES.md`.
- `Confirmed from source`: MegaMek's floating Round Report is a non-modal `MiniReportDisplayDialog` containing `MiniReportDisplayPanel`; report tabs render HTML/base64 unit sprites in a non-opaque `JTextPane`, while `BoardViewPanel` delegates painting directly to `BoardView#draw(...)`. No boardview path intentionally draws report content. See `MEGAMEK_ROUND_REPORT_REPAINT_INVESTIGATION.md`.
- `Confirmed from source`: the Advance Day button path is `CampaignGUI#initTopPanel()` -> `AdvanceTimePanel` -> `CampaignController#advanceDay()` -> `Campaign#newDay()` -> `CampaignNewDayManager#newDay()`. A local source-level control seam is viable only inside the loaded GUI app because `CampaignNewDayManager` reaches `CampaignGUI`/`CommandCenterTab`, triggers GUI subscribers for `DayEndingEvent` and `NewDayEvent`, and can create Swing dialogs during daily processing. See `MEKHQ_ADVANCE_DAY_GUI_CONTROL_SEAM_SPIKE.md`.
- `Confirmed from source`: issue `#35` source prototype adds `mekhq.service.LocalControlService` in `external/src/mekhq`, wired from `MekHQ`, disabled by default behind `mekhq.controlApi.enabled`, bound to `127.0.0.1`, and exposing `/status` plus `POST /advance-day` for guarded one-day advancement through `Campaign#newDay()`. See `MEKHQ_ADVANCE_DAY_CONTROL_API_PROTOTYPE.md`.
