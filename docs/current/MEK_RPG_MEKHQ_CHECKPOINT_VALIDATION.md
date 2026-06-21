# MEK-RPG MekHQ Checkpoint Validation

Status: validation note for GitHub issue `#28`, created `2026-06-21`.

Purpose: compare the draft read-only checkpoint schema, the MEK-RPG XML helper, source-backed expectations, and a disposable MekHQ save without mutating `.cpnx`, `.cpnx.gz`, or extracted campaign XML.

## Inputs

- Disposable save input: `analysis/tmp/issue-22/Autosave-1-The Learning Ropes-30250720.cpnx.gz`
- Plain extracted sibling present but not modified: `analysis/tmp/issue-22/Autosave-1-The Learning Ropes-30250720.cpnx`
- MEK-RPG helper: `C:\Users\waltr\Documents\mek-rpg\scripts\summarize-mekhq-save.py`
- Helper documentation: `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_SAVE_SUMMARY_HELPER.md`
- Consumer contract: `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_READ_ONLY_CHECKPOINT_EXPORT_CONTRACT.md`
- Draft MegaMek schema: `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT_SCHEMA.md`
- MekHQ source checkout: `external/src/mekhq`

No helper JSON output or raw save payload is committed. The validation recorded summary facts only.

## Verification Run

Command shape:

```powershell
python C:\Users\waltr\Documents\mek-rpg\scripts\summarize-mekhq-save.py `
  C:\Users\waltr\Documents\megamek-workspace\analysis\tmp\issue-22\Autosave-1-The Learning Ropes-30250720.cpnx.gz `
  --format json
```

Result: helper executed successfully and emitted parseable JSON.

Summary facts from helper output:

| Area | Observed value | Evidence | Notes |
| --- | --- | --- | --- |
| Helper | `summarize-mekhq-save.py` `0.1.0` | `Confirmed from MekHQ import` | Read-only helper writes JSON to stdout. |
| Save version | `0.51.00` | `Confirmed from MekHQ import` | Matches installed suite generation. |
| Compression | gzip `true` | `Confirmed from MekHQ import` | Input was `.cpnx.gz`. |
| Campaign | `The Learning Ropes` | `Confirmed from MekHQ import` | Bundled/sample campaign context, not a private user campaign. |
| Date | `3025-07-20` | `Confirmed from MekHQ import` | Serialized helper value. |
| Faction | `MERC` | `Confirmed from MekHQ import` | Serialized helper value. |
| Current system id | `Astrokaszy` | `Confirmed from MekHQ import` | Rich location/route semantics still need MekHQ method/UI validation. |
| Funds | `91255718 CSB` | `Inferred` | Helper sums serialized finance transactions. Schema should prefer `Finances#getBalance()`. |
| Personnel count | `106` | `Confirmed from MekHQ import` | Person IDs and names are usable checkpoint cross-references. |
| Unit count | `27` | `Confirmed from MekHQ import` | Unit IDs and entity names are usable checkpoint cross-references. |
| Contract count | `1` | `Confirmed from MekHQ import` | Active mission/contract facts are present. |
| Scenario count | `2` | `Confirmed from MekHQ import` | Scenario ids/status/date are present. |
| Unit market offers | `48` | `Confirmed from MekHQ import` | Helper marks final prices unsupported. |
| Personnel market applicants | `6` | `Confirmed from MekHQ import` | Applicant person IDs are present. |
| Shopping list items | `5` | `Confirmed from MekHQ import` | Shopping list is logistics pressure, not proof of inventory. |

Representative helper samples:

| Area | Sample | Validation result |
| --- | --- | --- |
| Personnel | `fd15b53b-14fa-4c36-ae9a-111c3ccd27ec`, `Michelle Moreno "Double-M"`, role `MEKWARRIOR`, rank `34`, status `ACTIVE` | IDs and display names map cleanly into schema `personnel[]`; rank/role labels, salary, fatigue effects, and injuries still need MekHQ methods. |
| Unit | `21d83f0a-a3c9-435c-a6eb-d6043ddde12f`, `Griffin GRF-1N`, scenario id `1`, linked part count `46` | IDs/entity identity map cleanly into schema `units[]`; damage state must be method-backed by `Unit#getDamageState()`. |
| Contract | id `1`, `3025 - FWL - Castrovia Objective Raid`, employer `Free Worlds League`, status `ACTIVE`, deadline `3025-10-03` | Active contract fields map cleanly into schema `contracts[]`; contract terms should use `Contract` getters in a MekHQ exporter. |
| Scenario | id `1`, contract id `1`, `Deep Raid`, status `CURRENT`, date `3025-07-20` | Scenario id/status/date map cleanly into schema `scenarios[]`. |
| Unit market | `Prime Mover (Succession Wars)`, market `CIVILIAN`, percent `110`, transit `0`, price `Unsupported` | Confirms schema needs `UnitMarketOffer#getPrice()` and must warn that no stable offer UUID is exposed. |
| Personnel market | `e5c1b400-9d08-4cb0-91fd-7f65d11a4b7b`, `Mardea Muhammed`, role `LAM_PILOT`, status `ACTIVE` | Applicant IDs map cleanly into `markets.personnel_applicants[]`; hiring remains out of scope. |

## Source-Backed Comparison

- `Confirmed from source`: `Finances#getBalance()` exists in `external/src/mekhq/MekHQ/src/mekhq/campaign/finances/Finances.java` and should own method-backed balance export. The helper's transaction sum is a useful fallback but remains `Inferred`.
- `Confirmed from source`: `Unit#getDamageState()`, `Unit#getPartsNeeded()`, `Unit#getPartsNeedingService(...)`, and `Unit#getLastMaintenanceReport()` exist in `external/src/mekhq/MekHQ/src/mekhq/campaign/unit/Unit.java`. The helper's linked-part count is not an authoritative damage state.
- `Confirmed from source`: `Person#getSalary(Campaign)`, `Person#getFullTitle()`, `Person#getPrimaryRoleDesc()`, `Person#getFatigueDirect()`, and `Person#getHits()` exist in `external/src/mekhq/MekHQ/src/mekhq/campaign/personnel/Person.java`. Salary and availability semantics need method-backed export.
- `Confirmed from source`: `UnitMarketOffer#getEntity()` and `UnitMarketOffer#getPrice()` exist in `external/src/mekhq/MekHQ/src/mekhq/campaign/market/unitMarket/UnitMarketOffer.java`; `UnitMarketOffer#writeToXML(...)` is the serialization owner. Final price should be method-backed.
- `Confirmed from source`: `Campaign#getCurrentReport()`, `Campaign#getTechnicalReport()`, and `Campaign#writeToXML(...)` exist in `external/src/mekhq/MekHQ/src/mekhq/campaign/Campaign.java`. Reports should be sanitized before MEK-RPG consumes them as alerts.
- `Confirmed from source`: `Campaign#writeToXML(...)` serializes units, human resources, missions, finances, locations, shopping list, parts, personnel market, unit market, and contract market. This matches the schema's top-level areas.

## Schema Impact

No schema field rename is required from this validation pass.

Validated decisions:

- Keep the top-level shape aligned with MEK-RPG: `bridge_metadata`, `campaign`, `finances`, `personnel`, `units`, `contracts`, `scenarios`, `repairs_and_logistics`, `markets`, `reports`, and `unsupported`.
- Keep `reports` as a first-class schema area even though the current helper does not classify daily reports.
- Keep `unsupported` mandatory for unit-market final price when only helper/XML output is available, exact unit damage state, exact transport/cargo pressure, and daily report alert classification.
- Preserve both `method_backed` and `evidence` fields because the helper and future MekHQ exporter will produce different trust levels for the same conceptual fields.
- Keep the sanitized fixture's missing unit-market stable selector example; the disposable save has many unit-market offers and the helper exposes no stable offer id.

## UI Validation

MekHQ UI comparison was not completed in this pass.

Blocker: no user-operated UI pass was available during the validation, and prior workspace notes record the Windows Computer Use helper as unavailable for live MekHQ click-through. Opening a save in the UI can also create autosave side effects unless performed on a disposable copy with explicit user oversight. This issue therefore validates source/XML/helper alignment and leaves UI spot-checking as a future optional pass.

Recommended UI spot checks when available:

- Confirm campaign name/date/location/funds for the copied save.
- Confirm at least one unit id/display name/status and one personnel id/display name/status.
- Confirm that one unit-market offer displays a final price not present in helper XML output.
- Confirm a unit damage/readiness summary against MekHQ's UI and `Unit#getDamageState()` semantics.
- Confirm report tabs are either empty or display sanitized lines for the current date.

## Conclusion

Acceptance criteria for issue `#28` are satisfied without changing the schema. The disposable save proves that the MEK-RPG helper can populate stable IDs and serialized cross-reference fields, while source inspection confirms the future exporter still needs MekHQ methods for balance, salary, damage state, market final price, transport/cargo validation, and report classification.
