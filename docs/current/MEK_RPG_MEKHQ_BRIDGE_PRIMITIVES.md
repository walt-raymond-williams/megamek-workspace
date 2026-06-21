# MEK-RPG MekHQ Bridge Primitives

Status: source-backed map for GitHub issue `#24`, created `2026-06-21`.

Purpose: identify MekHQ-side facts, method candidates, artifact paths, and blockers that MEK-RPG can use when queuing hard-ledger pending actions without editing MekHQ saves directly.

## Summary

- `Confirmed from source`: MekHQ save loading and saving are source-owned by `CampaignFactory#createCampaign(...)`, `CampaignXmlParser`, `CampaignGUI#saveCampaign(...)`, and `Campaign#writeToXML(...)`. A bridge should read checkpoint facts from saved campaigns or from a future MekHQ-owned exporter, not write `.cpnx`, `.cpnx.gz`, or extracted XML.
- `Confirmed from source`: stable read-only fields exist for campaign identity/date/faction, units, personnel, missions/scenarios, finances transactions, locations, shopping list, parts, personnel market, unit market, and StratCon contract market because `Campaign#writeToXML(...)` writes those sections.
- `Confirmed from source`: many useful display values are derived by MekHQ methods, not plain serialized facts. Examples: `Finances#getBalance()`, `UnitMarketOffer#getPrice()`, `Unit#getDamageState()`, transport summaries, repair targets, acquisition rolls, and contract acceptance effects.
- `Confirmed from source`: `Campaign#newDay()` is the core day-advance hook, but `CampaignNewDayManager#newDay()` immediately uses GUI state through `campaign.getApp().getCampaigngui().getCommandCenterTab()` and later daily processing can trigger events, reports, prompts, procurement, finances, personnel cleanup, repairs, and market changes. It is not a low-risk headless command as-is.
- `Confirmed from source`: the safest tactical result path remains MekHQ's built-in scenario setup MUL export plus Resolve Manually battle-record MUL import. The import remains dialog/wizard-driven.
- `Inferred`: the first low-risk implementation should be a MekHQ-backed read-only export/checkpoint helper. The smallest write-command candidate after that is probably contract accept/decline by selected saved contract-market offer, but only if the command owns confirmation policy for AtB/StratCon dialogs and post-accept prompts.

## Read-Only Export Fields

Recommended contract: expose these through a MekHQ-owned exporter or by carefully parsing saved XML with source-confirmed fallbacks. Preserve full MekHQ UUIDs and include unsupported-field warnings.

| Area | Safe facts | Best source/API owner | Notes |
| --- | --- | --- | --- |
| Save metadata | input path, gzip/plain XML, save timestamp, save version | `CampaignFactory#createCampaign(...)`; `CampaignGUI#saveCampaign(...)`; `Campaign#writeToXML(...)` | `Confirmed from source`: gzip detection uses magic bytes; saving uses UTF-8 and `GZIPOutputStream` when path ends `.gz`. |
| Campaign identity | campaign id, date, name, faction, start date, GM mode | `Campaign#writeToXML(...)`; `Campaign#getLocalDate()` | Serialized under `info`; safe checkpoint fields. |
| Location/travel | current location, system id, known locations, location node | `Campaign#getCurrentLocation()`; `Campaign#getCurrentSystem()`; location XML; `LocationNode` | `Confirmed from source`: current campaign location is written separately before the `locations` list. Rich route/base semantics need method-based interpretation. |
| Finances | balance, transactions, loans, assets, debt flags | `Finances#getBalance()`; `Finances#writeToXML(...)` | Balance is derived by summing transaction amounts and caching. Use `getBalance()` in a MekHQ exporter; XML-only readers may sum transactions but should label exactness carefully. |
| Personnel roster | id, display name, rank, roles, status, unit assignment, fatigue, injuries, salary inputs | `Person#writeToXML(...)`; `Campaign#getAllPersonnel()`; `HumanResources` APIs | Read serialized facts, but availability and pay meaning should be method-backed when exact. Injuries are structured `Injury` objects, not just text. |
| Units | unit id, entity identity, status, crew links, transport assignments, repairable parts | `Unit#writeToXML(...)`; `Unit#getEntity()`; `Unit#getStatus()`; `Unit#getCrew()`; `Unit#getParts()` | Use `Unit` methods for status, damage state, transport summaries, and repair needs. Raw entity/part XML is broad. |
| Damage/repair summary | damage state, parts needing service, parts needed, assigned techs, minutes left | `Unit#getDamageState()`; `Unit#getPartsNeedingService(...)`; `Unit#getPartsNeeded()`; `Campaign#getTargetFor(...)` | Do not infer full tactical condition from a shallow XML count. |
| Contracts and scenarios | active mission/contract ids, names, employer/enemy, status, dates, scenarios | `Mission#writeToXML(...)`; `Contract`; `AtBContract`; `Scenario` | Status is serialized; payment, salvage, support, StratCon state, and follow-up effects are method-owned. |
| Unit market | offer market type, unit type/name, percent, transit duration | `AbstractUnitMarket#writeToXML(...)`; `UnitMarketOffer` | Final price requires `UnitMarketOffer#getPrice()` because it loads entity cost and applies campaign tech-base multipliers. Offers do not appear to carry a standalone stable offer UUID. |
| Personnel market | applicant `Person` entries, attached entity references, paid recruitment settings | `PersonnelMarket#writeToXML(...)`; `NewPersonnelMarket` subclasses | Applicant `Person` ids are useful, but market refresh/removal is owned by market methods. |
| Contract market | available contract objects, generated AtB/StratCon offer state | `AbstractContractMarket#getContracts()`; `AbstractContractMarket#writeToXML(...)` | Contract objects have ids after creation. Selection by id is safer than row index; confirm id presence in sample saves before automation. |
| Procurement/logistics | shopping list, acquisition report, parts, repair bays | `ShoppingList#writeToXML(...)`; `Campaign#goShopping(...)`; parts XML | Shopping-list entries are pending acquisition work, not proof that the item exists. |
| Daily reports/alerts | current, skill, technical, finances, acquisitions, medical, personnel, battle, politics, aggregate reports | `Campaign#writeToXML(...)` report sections | Stored as report lines with markup/CDATA. A helper should classify and sanitize them before using as alerts. |

## Fields To Treat As Derived Or Unsafe From Raw XML Alone

- `Confirmed from source`: funds balance should be method-backed by `Finances#getBalance()` where possible. XML transaction summing is a useful approximation but should be labelled.
- `Confirmed from source`: unit market final price should be method-backed by `UnitMarketOffer#getPrice()`, which loads the entity and applies mixed-tech, Clan, or Inner Sphere campaign multipliers.
- `Confirmed from source`: unit condition should be method-backed by `Unit#getDamageState()`, `Unit#getPartsNeedingService(...)`, `Unit#getPartsNeeded()`, and MegaMek `Entity` state. A shallow XML parser should not claim exact armor/internal/critical readiness.
- `Confirmed from source`: transport and cargo pressure should be method-backed by `Unit` transport summaries and assignments, not only bay tags or unit names.
- `Confirmed from source`: repair and acquisition outcomes should be applied by `Campaign#fixPart(...)`, `Campaign#goShopping(...)`, `Campaign#acquireEquipment(...)`, and related `IAcquisitionWork`/`IPartWork` objects. Direct queue edits are unsafe.
- `Confirmed from source`: contract acceptance is more than setting a status. `ContractMarketDialog#acceptContract(...)` credits advance/transport funds, adds the mission, calls `Contract#acceptContract(...)` or `AtBContract#acceptContract(...)`, processes faction standings, opens start/rental prompts, and removes the market offer.
- `Confirmed from source`: personnel hiring is more than moving XML nodes. `PersonnelMarketDialog#hirePerson(...)` checks funds, calls `Campaign#recruitPerson(...)`, handles attached entities, removes the applicant from the market, and refreshes models.
- `Confirmed from source`: tactical outcomes should not be direct XML edits. MekHQ applies them through `ResolveScenarioTracker` and the resolve wizard after parsing a battle-record MUL.

## Pending Action Primitive Map

### Day Advancement

- Candidate hook: `Campaign#newDay()` delegates to `CampaignNewDayManager#newDay()`.
- Required input: loaded `Campaign`, expected baseline campaign id/date, save destination, noninteractive policy for all prompts and event interruptions.
- Confirmation on re-import: same campaign id, date advanced by one day, reports/finances/procurement/repairs/markets changed as expected.
- Blocker: `CampaignNewDayManager#newDay()` immediately clears daily report nags through `campaign.getApp().getCampaigngui().getCommandCenterTab()`. Daily processing also triggers campaign events, autosave, procurement, finances, personnel cleanup, repairs, and reports. A reliable command likely requires source work to separate noninteractive services from GUI assumptions.
- Recommendation: keep manual UI day-advance plus saved re-import for now. Do not start write automation here.

### Purchase / Sale

- Unit-market purchase candidate: `UnitMarketPane#purchaseSelectedOffers()` and private `finalizeEntityAcquisition(...)`; underlying operations include `UnitMarketOffer#getEntity()`, `UnitMarketOffer#getPrice()`, `Finances#debit(...)`, and `Campaign#addNewUnit(...)`.
- GM add candidate: `UnitMarketPane#addSelectedOffers()` finalizes instant delivery without charging funds.
- Required selector: market offer identity. Current serialized offer fields are market type, unit type, unit name, percent, and transit duration; no source-confirmed unique offer id was found in `UnitMarketOffer#writeToXML(...)`.
- Confirmation on re-import: funds transaction, unit added with a new campaign `Unit` UUID, offer removed, transit/delivery reflected.
- Blocker: safe selection cannot rely on table row or display name alone when duplicate offers exist. A future command needs a stable offer selector contract or MekHQ-side generated offer id.
- Sale candidate: not mapped deeply in this issue; treat sale automation as later than purchase because value, ownership, cargo, and unit removal side effects need their own source pass.

### Contract

- Accept candidate: `ContractMarketDialog#acceptContract(...)` maps the current UI flow. Core operations include `campaign.addMission(selectedContract)`, `selectedContract.acceptContract(campaign)`, advance and transport finance credits, faction-standing processing, start prompt, facility rental opportunity, and contract-market removal.
- Contract model candidates: `Contract#acceptContract(...)`; `AtBContract#acceptContract(...)` initializes StratCon state through `StratConContractInitializer` when StratCon is enabled.
- Required selector: contract id from `AbstractContractMarket#getContracts()` / serialized contract object, plus expected name/employer/date as guard fields.
- Confirmation on re-import: contract appears under missions, market offer removed, advance/transport finance transactions exist, StratCon state exists when applicable.
- Blockers: AtB contracts can require confirmation dialogs, employer liaison/clan opponent creation, faction-standing greeting, `DialogContractStart`, `ContractAutomation.contractStartPrompt(...)`, and `FacilityRentals.offerContractRentalOpportunity(...)`. A command must define default policy or refuse when prompts are required.
- Recommendation: this is the smallest plausible write-command candidate after read-only export, but only for a narrow "accept saved offer by id with no unresolved prompts" issue.

### Repair / Logistics

- Queue candidate: `ShoppingList#addShoppingItem(...)` for procurement entries, but use only with concrete `IAcquisitionWork` objects built by MekHQ logic.
- Daily acquisition candidate: `Campaign#goShopping(...)`, which decrements wait days and dispatches automatic, standard, or planetary acquisition.
- Repair candidate: `Campaign#fixPart(IPartWork, Person)`, which validates tech/location, target rolls, supplies, time, overtime, shorthanded work, costs, part reservation, events, and technical reports.
- Required ids: unit id, part/work id or source-owned work object, tech person id, expected location, baseline part state.
- Confirmation on re-import: report entry, part state/time left changed, tech minutes changed, funds/parts debited if applicable.
- Blocker: many repair/logistics actions require selecting actual `IPartWork`/`IAcquisitionWork` objects and applying campaign option rules. Direct raw shopping-list or part XML edits would bypass validation.
- Recommendation: start with read-only repair/logistics export and manual checklist. A command should target one narrow action, such as "attempt repair for part-work id with tech id", only after source confirms stable work ids.

### Personnel

- Hire applicant candidate: `PersonnelMarketDialog#hirePerson(...)` calls `Campaign#recruitPerson(...)`, handles attached entities, removes the applicant from `PersonnelMarket`, and checks recruitment funds.
- GM/bulk crew candidate: `HirePersonnelUnitAction#execute(Campaign, Unit)` generates and recruits a full crew for a unit, then resets pilot/entity state and diagnostics.
- Assignment candidates: `Unit` crew methods such as `addPilotOrSoldier`, `addDriver`, `addGunner`, `addVesselCrew`, `setNavigator`, and `setTechOfficer`; role/status setters on `Person`.
- Required ids: applicant/person id, unit id, crew slot or role, pay/GM policy, baseline funds and market membership.
- Confirmation on re-import: person appears in roster, applicant removed from market, unit crew links/external ids are updated, salary/finance effects recorded when applicable.
- Blockers: hiring and assignment are partly dialog/table driven, and exact assignment workflows have many unit-type-specific branches. Avoid direct personnel XML moves.
- Recommendation: personnel read export is safe now; write automation should wait until a specific recurring action is known, probably "hire market applicant by person id".

### Injury / Availability

- Candidate APIs: `Person#setStatus(...)`, `Person#setFatigue(...)`, `Person#setHits(...)`, `Person#addInjury(...)`, `Person#removeInjury(...)`, `Person#heal()`, and `Injury` objects.
- Required ids: person id, explicit injury type/subtype/location/severity or fatigue/status change, expected current injury state.
- Confirmation on re-import: injury/fatigue/status fields changed, medical/personnel reports updated where applicable.
- Blocker: injury semantics depend on campaign medical options and structured injury types. Tactical injuries should usually come through scenario resolution rather than a direct pending-action command.
- Recommendation: keep injury updates manual or tactical-artifact-backed until a narrow source-backed medical command is justified.

### Tactical Outcome

- Setup export: `BriefingTab#deployListFile()` exports scenario setup MULs with `EntityListFile.saveTo(...)`.
- Result import: `MekHQ#resolveScenario(...)` opens `ChooseMulFilesDialog`; `ResolveScenarioTracker#processMulFiles()` parses the battle-record MUL; `ResolveScenarioWizardDialog` applies final status/report through `ResolveScenarioTracker#resolveScenario(...)`.
- Existing artifact: MegaMek battle-record MUL with top-level `<record>` sections `survivors`, `allies`, `salvage`, `retreated`, `devastated`, and `kills`.
- Required ids: scenario id, campaign unit external ids, crew/person external ids, result sections, selected scenario status/report, salvage/control choices handled by wizard.
- Confirmation on re-import: scenario status/report, unit damage, personnel casualties/injuries, salvage, kill records, finances, and force history.
- Blocker: import is wizard/dialog-driven. The MUL carries tactical result data, but scenario final status and salvage review are still applied through MekHQ's resolve flow.
- Recommendation: continue issue `#10` live Resolve Manually validation before creating any tactical write command.

### Finance

- Candidate APIs: `Finances#credit(...)`, `Finances#debit(...)`, and higher-level campaign flows that call them.
- Required ids/fields: transaction type, amount, date, reason, optional individual payouts.
- Confirmation on re-import: transaction exists and `Finances#getBalance()` reflects it.
- Blocker: direct finance actions are easy to call but dangerous as standalone automation because most real financial changes should be coupled to a purchase, sale, contract, salary, repair, loan, or salvage workflow.
- Recommendation: allow finance export now; make write commands use higher-level domain actions first. A generic "adjust funds" helper should be GM-only and explicitly labeled as a manual correction, not normal bridge automation.

## GUI And Dialog Blockers

- `Confirmed from source`: new-day processing uses `CampaignGUI`/`CommandCenterTab` immediately and can trigger event-driven interruptions.
- `Confirmed from source`: contract market acceptance uses `ContractMarketDialog`, AtB confirmation dialogs, faction-standing greeting/start dialogs, contract automation prompt, and facility rental opportunity.
- `Confirmed from source`: personnel market hiring is dialog/table-model backed and uses selected UI state.
- `Confirmed from source`: tactical result import uses `ChooseMulFilesDialog` and `ResolveScenarioWizardDialog`.
- `Confirmed from source`: repair and personnel table actions are spread across GUI adapters and domain methods; command work should start from the domain method only after selectors/preconditions are source-confirmed.

## Tactical Handoff Packet For MEK-RPG

When MEK-RPG hands a scenario to MekHQ/MegaMek/tabletop, include:

- MekHQ campaign id, save path, save timestamp, and campaign date.
- Contract id and scenario id, if present.
- Player unit ids, display names, entity external ids, crew/person ids, pilot names, Gunnery/Piloting, current unit condition, ammo/heat notes if exported.
- Expected OPFOR or setup MUL references, terrain/objective/stakes notes, and any narrative constraints.
- Battle result artifact path after play: battle-record MUL if using MegaMek/tabletop generation, plus a human after-action summary.
- Confirmation fields to inspect after MekHQ resolution: scenario status, unit damage, casualties/injuries, salvage, prisoners, kill credit, funds, repairs, and reports.

## Recommended Follow-Up Issues

1. Build a MekHQ read-only checkpoint exporter or source-backed summary contract.
   - Goal: load a campaign through MekHQ code and emit JSON for campaign/date/location/funds/personnel/units/contracts/scenarios/markets/repairs/reports with derived fields produced by MekHQ methods where practical.
   - Why first: it improves MEK-RPG immediately and avoids writeback risk.

2. Prototype a narrow contract-market decision command in disposable data.
   - Goal: accept or decline one saved contract-market offer by stable contract id, save through MekHQ, and verify re-imported missions/finances/market removal.
   - Preconditions: source-confirm offer ids in sample saves, define noninteractive policy for AtB/StratCon prompts, and refuse when prompts cannot be safely answered.

Do not create broad "apply pending action" or "headless day advance" issues yet. The source shows too much GUI/dialog and campaign-option coupling for those to be safe first commands.
