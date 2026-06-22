# MEK-RPG MekHQ Checkpoint Export

Status: source-backed export contract for GitHub issue `#25`, created `2026-06-21`; updated after MEK-RPG issues `#67`, `#68`, and consumer feedback issues `#84` through `#89` completed.

Purpose: define a read-only MekHQ campaign checkpoint export that MEK-RPG can consume without writing `.cpnx`, `.cpnx.gz`, or extracted campaign XML.

Companion draft schema: `MEK_RPG_MEKHQ_CHECKPOINT_EXPORT_SCHEMA.md`.

MEK-RPG consumer contract: `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_READ_ONLY_CHECKPOINT_EXPORT_CONTRACT.md`.

Disposable-save validation: `MEK_RPG_MEKHQ_CHECKPOINT_VALIDATION.md`.

Jar-backed prototype: `MEK_RPG_MEKHQ_CHECKPOINT_EXPORTER_PROTOTYPE.md`.

Ownership decision: `MEK_RPG_MEKHQ_CHECKPOINT_EXPORTER_OWNERSHIP_DECISION.md`.

## Boundary

- `Confirmed from source`: MekHQ campaign saves are broad serialized campaign objects. Loading and saving are owned by `CampaignFactory#createCampaign(...)`, `CampaignXmlParser`, `CampaignGUI#saveCampaign(...)`, and `Campaign#writeToXML(...)`.
- `Confirmed from source`: `Campaign#writeToXML(...)` serializes reports, options, units, personnel, missions, formations, finances, locations, bases, shopping list, kills, skill/ability data, parts, personnel market, unit market, and contract market.
- `Inferred`: the first production-quality bridge should be a MekHQ-backed exporter that loads a campaign through MekHQ code and emits JSON using MekHQ methods for derived values. Raw XML parsing remains useful for fallback inspection and MEK-RPG's current helper, but it should not claim exact method-derived semantics where MekHQ source owns the calculation.
- `Confirmed by user`: MEK-RPG accepted the current top-level JSON shape for adapter experiments and asked to keep `evidence`, `source_owner`, `method_backed`, `warnings`, and `unsupported`.
- `Confirmed by user`: MEK-RPG's near-term consumed-field priorities are unit condition, personnel, contracts, reports/warnings, and campaign/finance basics.
- `Confirmed from MEK-RPG docs`: completed consumer feedback keeps the current top-level grouping, preserves trust-boundary fields, asks the producer to replace object-string location values with stable display/id fields, deepen active contract terms through `Contract` getters, keep market offers display-only, and keep `unsupported` mandatory.
- `Out of scope`: day advancement, purchases, sales, contract accept/decline, repair actions, personnel hiring/assignment, tactical-result application, direct XML edits, and save writeback.

## Recommended Implementation Shape

Use a source-backed checkpoint helper or future MekHQ export command:

1. Load an explicit `.cpnx`, `.cpnx.gz`, or plain XML campaign path through MekHQ's campaign loader.
2. Build a read-only DTO from the loaded `Campaign`.
3. Use serialized object IDs as stable references where present.
4. Use MekHQ methods for display-ready or calculated values.
5. Include `evidence`, `source_owner`, and `warnings` fields for every major group.
6. Emit JSON to stdout or an explicit output path outside the source save.
7. Refuse to write or normalize the MekHQ save.

The first version can be a contract document plus MEK-RPG adapter target. A later implementation could live in MekHQ source, this workspace as a jar-backed helper, or MEK-RPG as a consumer adapter. `Inferred`: MekHQ source is the best long-term owner for method-backed exports; MEK-RPG is the best owner for consuming the JSON into RPG campaign files.

## JSON Shape

Recommended top-level shape, aligned with MEK-RPG issue `#67`:

```json
{
  "bridge_metadata": {
    "schema_name": "mekhq-read-only-checkpoint",
    "schema_version": "0.1",
    "producer": "Unknown",
    "producer_version": "Unknown",
    "input_path": "C:/path/to/save.cpnx.gz",
    "save_timestamp": "Unknown",
    "save_size_bytes": 0,
    "gzip": true,
    "mekhq_save_version": "0.51.00",
    "read_only": true,
    "warnings": []
  },
  "campaign": {},
  "finances": {},
  "personnel": [],
  "units": [],
  "contracts": [],
  "scenarios": [],
  "markets": {
    "unit_offers": [],
    "personnel_applicants": [],
    "contract_offers": []
  },
  "repairs_and_logistics": {},
  "reports": {},
  "unsupported": []
}
```

Use these conventions throughout:

- `id`: full MekHQ UUID or integer id exactly as MekHQ exposes it.
- `display_name`: MekHQ method-backed name where practical.
- `raw_code`: serialized enum/string/code when useful for cross-checking.
- `label`: human-friendly text derived by MekHQ methods or carefully mapped enums.
- `evidence`: one of the workspace evidence labels from `DOCUMENTATION_WORKFLOW.md`.
- `source_owner`: class or method responsible for the value.
- `method_backed`: boolean or `Unknown`, matching MEK-RPG's #67 consumer contract.
- `warnings`: field-level caveats.

Do not shorten or regenerate MekHQ IDs. If an ID is missing, emit `null` plus a warning instead of inventing a substitute.

## Field Contract

| Group | Fields | Preferred owner | Evidence and notes |
| --- | --- | --- | --- |
| `bridge_metadata` | input path, save timestamp, size, gzip/plain XML, save version, import timestamp | `CampaignFactory#createCampaign(...)`; save root/version; filesystem metadata | `Confirmed from source`: gzip/plain handling belongs to the loader. `Confirmed from save`: bundled sample version is `0.51.00`. |
| `campaign` | campaign id, name, local date, start date, faction, GM mode, campaign options summary | `Campaign#getName()`; `Campaign#getLocalDate()`; `Campaign#getFaction()`; `Campaign#writeToXML(...)` | Use methods for current date/name/faction. Preserve serialized id when present. |
| `location` | current system id/name, current location node, travel/base hints, known locations | `Campaign#getCurrentSystem()`; `Campaign#getCurrentLocation()`; location XML; `LocationNode` | Current system is safe. Route/base semantics need validation against disposable saves. |
| `finances` | current balance, active loan/debt flags, loan balance, recent transactions, assets count | `Campaign#getFinances()`; `Finances#getBalance()`; `Finances#getLoanBalance()`; `Finances#hasActiveLoans()`; `Finances#writeToXML(...)` | `Confirmed from source`: balance is calculated and cached from transactions. XML transaction summing is a fallback, not the preferred contract. |
| `personnel` | person id, full title/name, rank, roles, status, unit assignment, fatigue, hits, injuries, salary, skills summary | `Campaign#getAllPersonnel()`; `Person#getId()`; `Person#getFullTitle()`; `Person#getPrimaryRole()`; `Person#getStatus()`; `Person#getFatigueDirect()`; `Person#getInjuries()`; `Person#getSalary(Campaign)` | Salary, skill level, injury effects, and availability should be method-backed. Raw roles/status are useful guard fields. |
| `units` | unit id, display name, entity chassis/model/type, status, scenario id, crew ids, tech/engineer ids, damage state, maintenance report, transport assignments | `Campaign#getUnits()`; `Unit#getId()`; `Unit#getEntity()`; `Unit#getStatus()`; `Unit#getCrew()`; `Unit#getDamageState()`; `Unit#getLastMaintenanceReport()`; transport assignment methods | `Confirmed from source`: unit damage state delegates to MegaMek `Entity#getDamageLevel(false)`. Do not infer exact armor/internal/crit state from shallow XML. |
| `contracts` | mission/contract id, name, type, status, system, employer/enemy, start/end date, payment/advance, command rights, salvage, support/transport terms, scenarios | `Campaign#getMissions()`; `Mission#getId()`; `Mission#getName()`; `Mission#getStatus()`; `Mission#getScenarios()`; `Contract` getters | Contract objects use integer ids. Contract terms have method-backed string/value accessors; acceptance remains out of scope. |
| `scenarios` | scenario id, mission id, name, status, date, report, objectives, assigned player formation, bot-force summaries | `Scenario#getId()`; `Scenario#getMissionId()`; `Scenario#getName()`; `Scenario#getStatus()`; `Scenario#getDate()`; `Scenario#getReport()`; `Scenario#getForces(Campaign)`; `Scenario#getBotForces()` | Keep report/objective text sanitized. Tactical result application remains MekHQ resolve-wizard work, not export work. |
| `markets.unit_offers` | market type, unit type, unit name, percent, transit duration, final price, entity identity | `Campaign#getUnitMarket()`; `AbstractUnitMarket#getOffers()`; `UnitMarketOffer#getPrice()`; `UnitMarketOffer#getEntity()`; `UnitMarketOffer#writeToXML(...)` | `Confirmed from source`: serialized offers do not expose a unique offer UUID; final price is method-derived and loads the entity. Include duplicate-offer warning. |
| `markets.personnel_applicants` | applicant person id/name/role/status/skills, attached entity refs when present | `Campaign#getPersonnelMarket()`; `PersonnelMarket#getPersonnel()`; `Person` methods; `PersonnelMarket#writeToXML(...)` | Applicant ids can be preserved. Hiring/removal belongs to market/dialog methods and is out of scope. |
| `markets.contract_offers` | contract offer id, name, employer, status, dates, terms, clause mods | `Campaign#getContractMarket()`; `AbstractContractMarket#getContracts()`; `Contract` getters; `AbstractContractMarket#writeToXML(...)` | Contract offers are serialized as contract mission objects and have integer ids. Confirm id stability in representative saves before any future write command. |
| `repairs_and_logistics` | shopping list items, parts needed, parts needing service, assigned techs, minutes left, cargo capacity, cargo tonnage, bay capacity/occupancy | `Campaign#getShoppingList()`; `ShoppingList#writeToXML(...)`; `Unit#getParts()`; `Unit#getPartsNeeded()`; `Unit#getPartsNeedingService(...)`; `Campaign#getTargetFor(...)`; `CargoStatistics`; `HangarStatistics` | Repair queues and cargo pressure are method-heavy. `CargoStatistics#getCargoTonnage(...)` has a source FIXME around DropShip assignments, so label exact cargo pressure as needs validation. |
| `reports` | current, skill, technical, finances, acquisitions, medical, personnel, battle, politics, aggregate report lines | `Campaign#getCurrentReport()` and sibling report getters; `Campaign#writeToXML(...)`; `Campaign#addReport(...)` | Reports are stored as HTML-ish strings/CDATA. Sanitize for MEK-RPG and classify as alerts only after rules are defined. |
| `unsupported` | missing ids, XML-only values, fields requiring GUI/context, rejected write-side intents | Exporter contract | Use this section aggressively. Missing or unsafe values should be visible to MEK-RPG instead of silently omitted. |

## Derived Or Unsafe Values

- `Confirmed from source`: `Finances#getBalance()` calculates balance from transactions and caches by transaction count. XML readers may sum transactions as a fallback but should label the result as XML-derived.
- `Confirmed from source`: `UnitMarketOffer#getPrice()` loads the offered entity and applies campaign tech-base price multipliers. XML exposes percent and unit name, not the full final-price calculation.
- `Confirmed from source`: `Unit#getDamageState()` delegates to MegaMek entity damage level. Exact armor, internal structure, critical slots, ammo, and heat-facing condition need deeper entity/part interpretation.
- `Confirmed from source`: `Person#getSalary(Campaign)`, skill levels, fatigue effects, and injury effects depend on campaign options and personnel methods.
- `Confirmed from source`: `Contract` payment, transport reimbursement, support, command rights, salvage, and advance values have method-backed getters. Do not treat contract XML edits as a safe way to accept or alter contract terms.
- `Confirmed from source`: `CargoStatistics` and `HangarStatistics` provide method-backed cargo and bay summaries, but cargo tonnage still carries a source FIXME about DropShip assignments. Export as a useful advisory field with validation warnings.
- `Confirmed from source`: daily reports are stored as formatted strings and may include links or HTML. They should be sanitized and classified before MEK-RPG uses them as alerts.
- `Confirmed from source`: unit market offers lack a source-confirmed unique offer UUID in `UnitMarketOffer#writeToXML(...)`. Future purchase automation cannot safely select by row/name alone.

## Unsupported In The First Contract

Do not export these as authoritative unless a later issue source-confirms and validates them:

- exact headless day-advance effects
- purchase, sale, hire, contract accept/decline, repair, assignment, or tactical-result writeback selectors
- exact cargo pressure for every transport state without disposable-save validation
- exact unit condition beyond summarized damage/repair state
- hidden StratCon state beyond saved contract/scenario fields
- final tactical outcome consequences before MekHQ's resolve flow applies them
- RPG-only narrative overlays, secrets, relationships, title risks, or unresolved rumors

## Validation Sequence

1. Use a disposable `.cpnx.gz` and matching plain `.cpnx` save from the installed MekHQ `0.51.00` suite.
2. Run the current MEK-RPG `summarize-mekhq-save.py` helper to capture raw XML-derived output.
3. Build or simulate the MekHQ-backed DTO for the same save.
4. Compare campaign identity/date/location/funds against MekHQ UI.
5. Compare unit/personnel ids and display names against MekHQ UI.
6. Compare `Finances#getBalance()` against MEK-RPG transaction-sum balance.
7. Compare at least one unit market offer's method-derived `getPrice()` against MekHQ UI.
8. Compare one damaged or maintenance-bearing unit's `getDamageState()`, parts/service summary, and maintenance report against MekHQ UI.
9. Compare contract/scenario ids and statuses against MekHQ UI.
10. Record mismatches in this workspace before MEK-RPG treats the contract as stable.

Initial validation on `2026-06-21` used the copied sample save under `analysis/tmp/issue-22/` and is recorded in `MEK_RPG_MEKHQ_CHECKPOINT_VALIDATION.md`. It confirmed that the schema needs no field rename, but funds, salary, damage state, market final price, transport/cargo pressure, and report classification still require method-backed export or later UI validation.

Prototype follow-up on `2026-06-21` added `tools/mekhq-checkpoint-exporter/`, a jar-backed read-only exporter that loads the same copied save through `CampaignFactory` and emits parseable checkpoint JSON with method-backed examples. Findings and limitations are recorded in `MEK_RPG_MEKHQ_CHECKPOINT_EXPORTER_PROTOTYPE.md`.

## MEK-RPG Consumer Guidance

MEK-RPG should consume this as a checkpoint, not as a command log:

- Use `campaign.date` as the authoritative linked campaign date after import.
- Use `id` fields as hard cross-references and keep MEK-RPG slugs separate.
- Treat market offers as scene seeds until the user commits the hard action in MekHQ.
- Treat report lines as hard MekHQ output but sanitize and summarize them before putting them in campaign-facing Markdown.
- Treat `unsupported` as required GM context, not as noise.
- Reconcile pending actions only after a new checkpoint confirms the MekHQ-side result.
- Keep market offers as display/opportunity context until stable source-confirmed selectors exist.

## Cross-Board Feedback Tracking

`Confirmed from MEK-RPG docs`: MEK-RPG completed the consumer-side queue `walt-raymond-williams/mek-rpg#84` through `#89` after reviewing the MegaMek checkpoint export memo.

Use these completed feedback points when opening or executing future MegaMek-side exporter or schema work:

- `#85` and `#86`: adapter tests accepted both sanitized fixture and sanitized prototype-output fixture as experiment inputs.
- `#87`: consumed-field mapping requests no top-level rename/removal, but does require stable display/id location values and deeper contract-term extraction.
- `#88`: warnings and `unsupported` entries are first-class GM-facing diagnostics; blocker/manual-inspection/caution/FYI severity should be preserved or easy for adapters to derive.
- `#89`: sparse and warning-heavy fixtures prove adapters must tolerate missing optional data while preserving warnings, unsupported entries, and read-only boundaries.

## Completed Follow-Up Work

1. Sanitized fixture completed in `docs/templates/mekhq-read-only-checkpoint.fixture.json`.
2. Disposable-save validation completed in `MEK_RPG_MEKHQ_CHECKPOINT_VALIDATION.md`.
3. Jar-backed prototype completed in `tools/mekhq-checkpoint-exporter/`, with findings in `MEK_RPG_MEKHQ_CHECKPOINT_EXPORTER_PROTOTYPE.md`.
4. Epic review completed in `MEK_RPG_MEKHQ_CHECKPOINT_EPIC_REVIEW.md`.

Potential future work should proceed through the checkpoint hardening queue:

- harden exporter output against completed MEK-RPG adapter feedback from issues `#85` through `#89`
- add or document repeatable smoke checks for sanitized fixture JSON and prototype JSON
- deepen cargo/transport and contract-term extraction
- deepen active contract-term extraction through `Contract` getters
- defer moving the prototype into MekHQ source until regular real-campaign use, MEK-RPG production dependency, upstream/source-maintainer intent, or unblocked source build/test verification justifies a separate source issue
- keep any write-side probe separate from this read-only checkpoint workstream
