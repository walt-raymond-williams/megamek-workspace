# MEK-RPG Live MekHQ API Prototype

Status: implemented locally in MekHQ source for GitHub issue `#36`.

Purpose: expose read-only live campaign context from the already-running MekHQ GUI app so MEK-RPG can refresh GM context without requiring a save first.

## Source Changes

Target repo:

- `external/src/mekhq`

Source branch:

- `codex/mekhq-advance-day-control-api`

Source commit:

- `7d3b345327` (`Add local live campaign state API`)
- `dc214d946d` (`Harden live campaign state metadata`)
- `d38a500960` (`Deepen live campaign personnel unit finance state`)
- `495b58faef` (`Deepen live campaign contract scenario state`)
- `911a338788` (`Deepen live campaign logistics market reports`)
- `e19740b110` (`Expose command readiness endpoint`)
- `4429d99ea2` (`Add guarded status note command`)
- `0451eb53d4` (`Add guarded contract accept command`)
- `51dbfbe645` (`Add local control API readiness tests`)
- `5effaa5517` (`Instrument local control API read paths`)
- `72424e4d9c` (`Make local campaign state export partial`)
- `ba865793c5` (`Expose pending deployment API`)

Files changed:

- `MekHQ/src/mekhq/service/LocalControlService.java`
- `MekHQ/src/mekhq/service/LocalCampaignStateExporter.java`
- `MekHQ/src/mekhq/service/LocalCommandReadinessExporter.java`

`Confirmed from source`: the new endpoints are added to the existing disabled-by-default local control API. The API still starts only with `-Dmekhq.controlApi.enabled=true`, binds to `127.0.0.1`, and runs in-process with the open MekHQ GUI app.

`Confirmed from source`: the live campaign-state endpoints are read-only DTO builders. They do not save, mutate campaign objects, purchase market offers, hire personnel, accept contracts, assign repairs, or apply tactical results.

## Endpoints

Summary:

```http
GET /campaign/summary
```

State:

```http
GET /campaign/state?sections=bridge_metadata,campaign,finances,personnel,units,contracts,scenarios,repairs_and_logistics,markets,reports,unsupported
```

Pending deployments:

```http
GET /campaign/pending-deployments
GET /campaign/pending-deployments?personName=Moreno
GET /campaign/pending-deployments?personId=<person-uuid>
```

Command readiness:

```http
GET /campaign/commands
```

Status-note command:

```http
POST /campaign/command/status-note
```

Contract accept command:

```http
POST /campaign/command/contracts/accept
```

Supported state sections:

```text
bridge_metadata, campaign, finances, personnel, units, contracts, scenarios,
repairs_and_logistics, markets, reports, unsupported
```

If `sections` is omitted, the endpoint returns all supported sections. Unknown section names return HTTP `400` with `unknown_sections` and `supported_sections`.

If no campaign is loaded in the MekHQ GUI, both campaign endpoints return HTTP `409` with a local control response saying no campaign is loaded.

`GET /campaign/commands` follows the same loaded-campaign rule. It returns read-only command readiness and selector metadata; it does not mutate the campaign.

`GET /campaign/pending-deployments` follows the same loaded-campaign rule. It returns only current/pending `ScenarioStatus.CURRENT` scenario facts, source-confirmed player unit links, current unit crew ids/names/roles/statuses, optional person-id or person-name commitment lookup, collector timing, endpoint timing, warnings, and unsupported metadata. It is read-only and does not inspect markets, reports, repairs, finances, or broad personnel/unit state.

`POST /campaign/command/status-note` follows the shared guarded command-envelope posture. It appends a plain-text MEK-RPG audit note to the loaded campaign's `GENERAL` report through `Campaign#addReport(...)`; supports dry-run validation; requires campaign id/name/date guards, idempotency key, explicit `dryRun`, `promptPolicy=refuse_if_prompt`, client audit context, and plain-text note text; and refuses unsupported report categories, HTML, visible dialogs, stale campaign guards, or missing save paths.

`POST /campaign/command/contracts/accept` follows the shared guarded command-envelope posture with `promptPolicy=explicit_known_choices`. It accepts one current contract-market offer selected by source `Mission#getId()` plus `expectedStateRevision` and guard facts from `GET /campaign/commands`; supports dry-run validation; uses process-local idempotency; keeps save-after-success explicit and opt-in; and refuses visible dialogs, stale guards, unsupported prompt choices, or unknown prompt branches before mutation.

## Output Contract

`GET /campaign/summary` returns:

- `status`
- `campaignId`
- `campaignName`
- `campaignDate`
- `mekhqVersion`
- `apiSchemaVersion`
- `apiMode`
- `readOnly`
- `currentSystem`
- `currentLocation`
- `dirtyState`
- `stateRevision`
- `snapshotId`
- `pendingDeployments`
- `warnings`
- `unsupported`
- `response_status`
- `partial_response`
- `collector_timing`
- `endpoint_timing`

`GET /campaign/state` preserves the MEK-RPG checkpoint top-level grouping:

- `bridge_metadata`
- `campaign`
- `finances`
- `personnel`
- `units`
- `contracts`
- `scenarios`
- `repairs_and_logistics`
- `markets`
- `reports`
- `unsupported`
- `collector_timing`
- `endpoint_timing`

`Confirmed from source`: campaign identity/date/location, current balance, loan balance, personnel role/status/fatigue/hits/salary, unit status/damage/repair counts, contract terms, scenario status/report, and classified report buckets are built from MekHQ methods rather than raw XML.

`Confirmed from source`: source commit `dc214d946d` hardens location output so the summary and `campaign.location` fields use `Campaign#getCurrentSystem()`, `Campaign#getCurrentLocation()`, and `AbstractLocation` travel-state methods instead of relying on `AbstractLocation#toString()` as the only display representation. The state payload now includes `current_location_display_name`, `table_safe_location_label`, `current_planet_name`, and expanded travel-state fields.

`Confirmed from source`: source commit `d38a500960` deepens the finance, personnel, and unit sections. Finance output now includes loan defaults, active loan summaries, and derived warnings for negative balance, overdue loans, and loan defaults. Personnel output now includes assignment dates, deployed/employed flags, compact injury counts/severity, leadership markers, and current-personnel market membership. Unit output now includes availability/deployability, commander id, maintenance site, and read-only transport assignment/carried-unit summaries.

`Confirmed from source`: source commit `495b58faef` deepens contract and scenario output. Contract DTOs now include descriptions, dates, travel days, payment summaries, salvage summaries, rental summaries, and scenario links. Scenario DTOs now include descriptions, linked scenario ids, StratCon type, map and planetary condition summaries, player force ids/unit ids, salvage assignments, objective summaries, bot-force summaries, bot-force stubs, and a read-only tactical-result context block.

`Confirmed from source`: source commit `911a338788` deepens logistics, reports, and market safeguards. Repairs/logistics output now includes repair pressure counts, a display-only repair queue, shopping-list pressure and rows from `IAcquisitionWork`, transport/cargo relationship summaries, and an automation guard marking repair execution, assignment, procurement execution, and stable selectors unavailable. Reports now include per-category metadata/counts alongside sanitized report rows. Markets now expose display-only summaries and optional unit/personnel/contract rows while explicitly setting `automation_ready: false` and adding unsupported entries for stable offer selectors and market mutation commands.

`Confirmed from source`: source commit `e19740b110` adds `GET /campaign/commands`, a read-only readiness endpoint for MEK-RPG action discovery. It exposes campaign/person/unit/applicant/contract candidate selectors from source-backed ids, reports `advanceDayOnce` as the only currently available mutating command, and marks status-note, funds adjustment, personnel status, medical treatment, contract acceptance, personnel hire, unit purchase, repair/procurement, and standalone save as blocked with machine-readable reason codes. Unit-market purchase remains blocked with `stable_offer_selector_missing` because `UnitMarketOffer#writeToXML()` still has no unique stable offer id.

`Confirmed from source`: source commit `4429d99ea2` adds `POST /campaign/command/status-note`. V1 accepts only `reportCategory=GENERAL` or an omitted category, rejects note/client-context HTML, requires `clientContext`, blocks when visible dialogs exist under `promptPolicy=refuse_if_prompt`, supports `dryRun`, records applied notes via `Campaign#addReport(DailyReportType.GENERAL, ...)`, reports before/after general-report counts, supports process-local idempotency for applied commands, and keeps save-after-success explicit and opt-in through `CampaignGUI.saveCampaign(...)`.

`Unknown`: no source-confirmed dirty/unsaved campaign flag was found in this V1 pass. Source search found editor-local unsaved state, but not a campaign-wide dirty flag for the loaded campaign, so `dirtyState` remains explicit `Unknown` with a warning and a structured unsupported entry naming `MekHQ GUI save-state tracking` as the recommended owner.

`Unsupported`: V1 does not expose stable repair-work ids, repair execution, repair assignment, shopping-list purchase/priority mutation, personnel hire/fire, contract accept/decline, market refresh, negotiation, standalone save, or broad writeback commands. Unit-market purchase now has source-generated live-session selectors in the local source branch, but repair/acquisition rows remain display-only and must not be treated as durable selectors.

`Confirmed from source`: source commit `0451eb53d4` adds `POST /campaign/command/contracts/accept`. Apply mode credits advance and transport payments as `TransactionType.CONTRACT_PAYMENT`, calls `Campaign#addMission(...)`, calls `Contract#acceptContract(...)`, processes the non-dialog faction-standing report path, removes the offer from `AbstractContractMarket`, can append a `GENERAL` MEK-RPG audit report, and returns the new mission id assigned by `Campaign#addMission(...)`. Known prompt choices are explicit: accept known contract challenge confirmations, acknowledge known informational prompts without showing dialogs, decline travel/mothball automation, decline rentals, and refuse unknown prompts. Live disposable-campaign smoke testing is still not run.

`Confirmed locally`: source commit `51dbfbe645` adds unit-test coverage for the local command API surface. `LocalCommandReadinessExporterTest` asserts the expected command rows/endpoints/statuses are present, contract selectors expose guard facts and prompt choices, and `state_revision` changes when contract-market state changes. `LocalControlServiceHttpTest` starts the loopback HTTP service with no campaign loaded and verifies `/status`, no-campaign blocking, invalid contract-accept JSON refusal, and post-failure server availability.

`Confirmed from source`: source commit `5effaa5517` adds timing diagnostics for reliability work. `/campaign/summary`, `/campaign/state`, and `/campaign/commands` now include top-level `endpoint_timing`; summary and command readiness include `collector_timing`; state responses include per-requested-section `collector_timing`. `LocalControlService` logs a warning when a read endpoint takes at least `1000` ms.

`Confirmed from source`: source commit `72424e4d9c` makes `GET /campaign/state?sections=...` partial-response capable for section collector failures. Requested sections are still collected lazily in section order, and narrowed requests do not traverse unrequested personnel/unit/scenario collections. If a requested section collector throws a runtime exception, already-collected sections are returned with HTTP `200`, `response_status: "partial"`, `partial_response: true`, a structured top-level warning, a failed-section placeholder with `reason_code: "section_collector_failed"`, an `unsupported` entry for the missing section payload, and failed collector timing. Java-level per-section timeout cancellation is intentionally deferred because a timed-out background worker could keep reading live campaign state concurrently.

`Confirmed from source`: source commit `ba865793c5` adds `GET /campaign/pending-deployments` and embeds the same compact `pendingDeployments` object in `GET /campaign/summary`. The endpoint filters `Campaign#getScenarios()` to `ScenarioStatus#isCurrent()`, reports scenario id/mission id/name/date/status/type, force ids, individual unit ids, all assigned unit ids from `Scenario#getForces(Campaign)`, unit rows from `Campaign#getUnit(UUID)`, and crew rows from `Unit#getCrew()`. Optional `personId` and `personName` query parameters return source-confirmed commitments when a matching crew member is assigned to a current scenario unit. MekHQ UI-selected/viewpoint person state remains explicitly unsupported because no source-confirmed selected-person bridge is exposed in this API layer.

## Fixtures

Sanitized examples:

- `docs/templates/mekhq-live-campaign-summary.fixture.json`
- `docs/templates/mekhq-live-campaign-state.fixture.json`
- `docs/templates/mekhq-live-campaign-warning-heavy.fixture.json`
- `docs/templates/mekhq-live-campaign-commands.fixture.json`
- `docs/templates/mekhq-live-pending-deployments.fixture.json`

These are fake data only. They preserve the live API shape, trust-envelope fields, dirty-state warning, and unsupported/blocking entries without exposing local save paths or real campaign details.

## Verification

`Confirmed locally`: Gradle compile passed from `external/src/mekhq` on `2026-06-22` after source commit `7d3b345327`:

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat :MekHQ:compileJava
```

`Confirmed locally`: Checkstyle passed from `external/src/mekhq` on `2026-06-22`:

```powershell
.\gradlew.bat :MekHQ:checkstyleMain
```

The Checkstyle run emitted one existing warning in `MekHQ.java` for deprecated `AtBGameThread` usage; it was not introduced by the live state API work.

`Confirmed locally`: after source commit `dc214d946d`, both Gradle checks passed from `external/src/mekhq` on `2026-06-22`:

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat :MekHQ:compileJava
.\gradlew.bat :MekHQ:checkstyleMain
```

`Confirmed locally`: after source commit `d38a500960`, both Gradle checks passed from `external/src/mekhq` on `2026-06-22`:

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat :MekHQ:compileJava
.\gradlew.bat :MekHQ:checkstyleMain
```

`Confirmed locally`: after source commit `495b58faef`, both Gradle checks passed from `external/src/mekhq` on `2026-06-22`:

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat :MekHQ:compileJava
.\gradlew.bat :MekHQ:checkstyleMain
```

`Confirmed locally`: after source commit `911a338788`, both Gradle checks passed from `external/src/mekhq` on `2026-06-22`:

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat :MekHQ:compileJava
.\gradlew.bat :MekHQ:checkstyleMain
```

`Confirmed locally`: after source commit `e19740b110`, both Gradle checks passed from `external/src/mekhq` on `2026-06-22`:

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat :MekHQ:compileJava
.\gradlew.bat :MekHQ:checkstyleMain
```

`Confirmed locally`: after source commit `4429d99ea2`, both Gradle checks passed from `external/src/mekhq` on `2026-06-22`:

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat :MekHQ:compileJava
.\gradlew.bat :MekHQ:checkstyleMain
```

`Confirmed locally`: after source commit `51dbfbe645`, local control API regression tests and the full MekHQ test task passed from `external/src/mekhq` on `2026-06-23`:

```powershell
.\gradlew.bat :MekHQ:test --tests mekhq.service.LocalCommandReadinessExporterTest --tests mekhq.service.LocalControlServiceHttpTest
.\gradlew.bat :MekHQ:test
```

`Confirmed locally`: after source commit `5effaa5517`, targeted local-control tests and compile/checkstyle passed from `external/src/mekhq` on `2026-06-26`:

```powershell
.\gradlew.bat :MekHQ:test --tests mekhq.service.LocalCommandReadinessExporterTest --tests mekhq.service.LocalControlServiceHttpTest
.\gradlew.bat :MekHQ:compileJava :MekHQ:checkstyleMain
```

`Confirmed locally`: after source commit `72424e4d9c`, state-exporter and local-control regression tests plus compile/checkstyle passed from `external/src/mekhq` on `2026-06-26`:

```powershell
.\gradlew.bat --no-daemon :MekHQ:test --tests mekhq.service.LocalCampaignStateExporterTest
.\gradlew.bat --no-daemon :MekHQ:test --tests mekhq.service.LocalCampaignStateExporterTest --tests mekhq.service.LocalControlServiceHttpTest --tests mekhq.service.LocalCommandReadinessExporterTest
.\gradlew.bat --no-daemon :MekHQ:compileJava :MekHQ:checkstyleMain :MekHQ:checkstyleTest
```

`Confirmed locally`: after source commit `ba865793c5`, pending-deployment exporter and HTTP routing tests plus compile/checkstyle passed from `external/src/mekhq` on `2026-06-26`:

```powershell
.\gradlew.bat --no-daemon :MekHQ:test --tests mekhq.service.LocalCampaignStateExporterTest
.\gradlew.bat --no-daemon :MekHQ:test --tests mekhq.service.LocalControlServiceHttpTest
.\gradlew.bat --no-daemon :MekHQ:compileJava :MekHQ:checkstyleMain :MekHQ:checkstyleTest
```

`Confirmed locally`: a user-assisted running MekHQ campaign smoke test was performed from MEK-RPG issue `#104` on `2026-06-22` against a disposable `The Learning Ropes-test.cpnx` campaign. Both summary and state endpoints responded, and the user observed no MekHQ save prompt or other visible write/save side effect after the read-only GET requests.

`Confirmed locally`: follow-up MEK-RPG issue `#106` verified that selected-section state requests must include `bridge_metadata` when the response is intended for MEK-RPG dashboard/context validation. Omitting `sections` also returns `bridge_metadata` because it requests all supported sections.

Suggested smoke commands after launching source-built MekHQ with a disposable campaign loaded:

```powershell
Invoke-RestMethod -Method Get -Uri 'http://127.0.0.1:32180/campaign/summary' -TimeoutSec 10 |
  ConvertTo-Json -Depth 8

Invoke-RestMethod -Method Get `
  -Uri 'http://127.0.0.1:32180/campaign/state?sections=bridge_metadata,campaign,finances,personnel,units,contracts,scenarios,repairs_and_logistics,reports,unsupported' `
  -TimeoutSec 30 |
  ConvertTo-Json -Depth 12

Invoke-RestMethod -Method Get -Uri 'http://127.0.0.1:32180/campaign/commands' -TimeoutSec 10 |
  ConvertTo-Json -Depth 12

Invoke-RestMethod -Method Get `
  -Uri 'http://127.0.0.1:32180/campaign/pending-deployments?personName=Moreno' `
  -TimeoutSec 10 |
  ConvertTo-Json -Depth 12

$body = @{
  command = 'campaign.status_note'
  commandVersion = 1
  idempotencyKey = 'mek-rpg-status-note-example-001'
  expectedCampaignId = '<campaign id from /campaign/summary>'
  expectedDate = '<campaign date from /campaign/summary>'
  dryRun = $true
  promptPolicy = 'refuse_if_prompt'
  reportCategory = 'GENERAL'
  noteText = 'MEK-RPG dry-run status note example.'
  clientContext = @{
    actor = 'MEK-RPG'
    sceneId = 'example-scene'
    actionId = 'example-action'
    reason = 'Dry-run smoke check'
  }
} | ConvertTo-Json -Depth 4

Invoke-RestMethod -Method Post -Uri 'http://127.0.0.1:32180/campaign/command/status-note' `
  -Body $body -ContentType 'application/json' -TimeoutSec 10 |
  ConvertTo-Json -Depth 12
```

## Source Push Status

`Blocked`: the source commits listed above are committed locally in `external/src/mekhq`, but pushing that branch failed because the checkout remote is `https://github.com/MegaMek/mekhq.git` and the authenticated account lacks write permission.

The workspace documentation commit can still be pushed to `walt-raymond-williams/megamek-workspace`.
