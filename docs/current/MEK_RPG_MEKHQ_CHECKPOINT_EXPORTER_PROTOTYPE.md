# MEK-RPG MekHQ Checkpoint Exporter Prototype

Status: jar-backed read-only prototype for GitHub issue `#29`, created `2026-06-21`; hardened for GitHub issue `#32` on `2026-06-22`.

Purpose: prove that this workspace can load an explicit MekHQ `.cpnx`, `.cpnx.gz`, or plain campaign XML path through installed MekHQ jars and emit JSON aligned with `MEK_RPG_MEKHQ_CHECKPOINT_EXPORT_SCHEMA.md` without writing to the save or executing campaign actions.

## Prototype

Files:

- `tools/mekhq-checkpoint-exporter/MekHqCheckpointExporter.java`
- `tools/mekhq-checkpoint-exporter/run-mekhq-checkpoint-exporter.ps1`

Command:

```powershell
powershell -ExecutionPolicy Bypass `
  -File tools\mekhq-checkpoint-exporter\run-mekhq-checkpoint-exporter.ps1 `
  "C:\path\to\campaign.cpnx.gz"
```

The wrapper:

- compiles the Java prototype with JDK 21
- uses installed jars from `external/installs/MekHQ-0.51.00`
- writes compiled classes and stderr capture under ignored `analysis/tmp/mekhq-checkpoint-exporter/`
- runs from the MekHQ install directory so data-relative lookups work
- writes checkpoint JSON to stdout

## Read-Only Boundary

- `Confirmed locally`: the prototype opens the explicit save path with `FileInputStream` and calls `CampaignFactory.newInstance(app).createCampaign(stream)`.
- `Confirmed locally`: the prototype does not call `Campaign#writeToXML(...)`, `CampaignGUI#saveCampaign(...)`, day advancement, purchase, hire, repair, contract acceptance, tactical result import, or any other campaign action.
- `Confirmed locally`: the validation run used copied save `analysis/tmp/issue-22/Autosave-1-The Learning Ropes-30250720.cpnx.gz`.
- `Known side effect`: using installed MekHQ jars initializes MekHQ logging and data caches. The wrapper redirects Java stderr to ignored `analysis/tmp/mekhq-checkpoint-exporter/stderr.log`; it does not write to the input save.

## Source Findings

- `Confirmed from source`: `DataLoadingDialog.Task#doInBackground()` initializes currencies, eras, financial institutions, injuries, rank systems, skills, special abilities, scenario modifiers, factions, name generators, bloodnames, systems, static directories, and `MekSummaryCache` before campaign loading.
- `Confirmed from source`: `CampaignFactory#createCampaign(InputStream)` detects gzip by header and delegates to `CampaignXmlParser`.
- `Confirmed from source`: `CampaignXmlParser#parse()` creates a `Campaign`, sets the app reference, parses XML, checks milestone upgrade path, and populates campaign state.
- `Confirmed locally`: installed `MekHQ.jar` seals package `mekhq`, so an external helper cannot compile its own class into that package. The prototype uses reflection to call protected `MekHQ#getInstance()` from a neutral default-package helper.

## Verification

Verification command:

```powershell
$save = 'C:\Users\waltr\Documents\megamek-workspace\analysis\tmp\issue-22\Autosave-1-The Learning Ropes-30250720.cpnx.gz'
$json = powershell -ExecutionPolicy Bypass -File tools\mekhq-checkpoint-exporter\run-mekhq-checkpoint-exporter.ps1 $save
$parsed = $json | ConvertFrom-Json
```

Repeatable smoke check:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File tools\mekhq-checkpoint-exporter\test-mekhq-checkpoint-exporter.ps1
```

`Confirmed locally` on `2026-06-22`: smoke check passed against copied save `analysis/tmp/issue-22/Autosave-1-The Learning Ropes-30250720.cpnx.gz`.

Observed parseable JSON summary:

| Field | Value |
| --- | --- |
| `bridge_metadata.schema_name` | `mekhq-read-only-checkpoint` |
| `bridge_metadata.producer` | `workspace-jar-backed-prototype` |
| `campaign.name.value` | `The Learning Ropes` |
| `campaign.date.value` | `3025-07-20` |
| `finances.balance.value` | `CSB 91255718` |
| `personnel.length` | `106` |
| `units.length` | `27` |
| `contracts.length` | `1` |
| `scenarios.length` | `2` |
| `markets.unit_offers.length` | `48` |
| `unsupported.length` | `4` |

Hardened output examples added for issue `#32`:

| Field | Value |
| --- | --- |
| `campaign.location.current_location.display_name` | `Astrokaszy, Astrokaszy` |
| `campaign.location.current_location.system_id` | `Astrokaszy` |
| `campaign.location.current_location.is_in_transit` | `false` |
| `contracts[0].terms.source_owner` | `Contract getters` |
| `contracts[0].terms.salvage_pct` | `100` |
| `contracts[0].terms.transport_comp_pct` | `100` |
| `contracts[0].terms.monthly_payout` | `CSB 3513703` |

Method-backed examples emitted by the prototype:

- `Finances#getBalance()`
- `Finances#getLoanBalance()`
- `Finances#hasActiveLoans()`
- `Person#getFullTitle()`
- `Person#getPrimaryRoleDesc()`
- `Person#getSalary(Campaign)`
- `Person#getFatigueDirect()`
- `Person#getHits()`
- `Unit#getCondition()`
- `Unit#getPartsNeeded()`
- `Unit#getPartsNeedingService()`
- `UnitMarketOffer#getPrice()`
- `Campaign#getCurrentReport()`
- `Campaign#getTechnicalReport()`
- `Campaign#getCurrentLocation()`
- `AbstractLocation#isOnPlanet()`, `isAtJumpPoint()`, `isInTransit()`, `getTransitTime()`, and `getPercentageTransit()`
- `PlanetarySystem#getId()` and `Planet#getId()`
- `Contract#getStartDate()`, `getEndingDate()`, `getCommandRights()`, `getSalvagePct()`, `getTransportComp()`, `getStraightSupport()`, `getBattleLossComp()`, `getAdvancePct()`, and payout amount getters

## Limitations

- `Inferred`: this is a workspace prototype, not a production MekHQ CLI. A production exporter should live in MekHQ source or expose an official non-GUI export entrypoint.
- `Needs MekHQ inspection`: cargo pressure and transport bay occupancy remain warnings. The prototype emits unsupported/needs-inspection entries instead of claiming exact logistics capacity.
- `Unsupported`: unit-market offers still have no stable offer UUID. The prototype emits `id: null`, guard fields, and automation-blocking warnings.
- `Unsupported`: write commands remain absent. There is no day advancement, save writeback, purchase, hire, repair, contract decision, or tactical-result application.
- `Needs MekHQ inspection`: the prototype now emits core `Contract` getter terms for `Contract` missions, but generic enemy identity remains unsupported because `Contract` does not expose an enemy; AtB-specific enemy fields need a source-owned exporter pass or explicit subclass handling.
- `Needs MekHQ inspection`: exact report classification is shallow. The prototype sanitizes current and technical report strings but does not classify all daily report categories into MEK-RPG alert types.

## Next Step

Use this prototype as the smallest proven implementation path for MEK-RPG adapter experiments. For durable production use, the smallest unblock step is to add a MekHQ-owned command or service that reuses the same initialization and `CampaignFactory` load path but avoids reflection, avoids external helper packaging issues, and owns stable DTO serialization directly in source.
