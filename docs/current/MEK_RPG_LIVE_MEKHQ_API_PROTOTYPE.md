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

Files changed:

- `MekHQ/src/mekhq/service/LocalControlService.java`
- `MekHQ/src/mekhq/service/LocalCampaignStateExporter.java`

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

Supported state sections:

```text
bridge_metadata, campaign, finances, personnel, units, contracts, scenarios,
repairs_and_logistics, markets, reports, unsupported
```

If `sections` is omitted, the endpoint returns all supported sections. Unknown section names return HTTP `400` with `unknown_sections` and `supported_sections`.

If no campaign is loaded in the MekHQ GUI, both campaign endpoints return HTTP `409` with a local control response saying no campaign is loaded.

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
- `warnings`
- `unsupported`

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

`Confirmed from source`: campaign identity/date/location, current balance, loan balance, personnel role/status/fatigue/hits/salary, unit status/damage/repair counts, contract terms, scenario status/report, and classified report buckets are built from MekHQ methods rather than raw XML.

`Confirmed from source`: source commit `dc214d946d` hardens location output so the summary and `campaign.location` fields use `Campaign#getCurrentSystem()`, `Campaign#getCurrentLocation()`, and `AbstractLocation` travel-state methods instead of relying on `AbstractLocation#toString()` as the only display representation. The state payload now includes `current_location_display_name`, `table_safe_location_label`, `current_planet_name`, and expanded travel-state fields.

`Confirmed from source`: source commit `d38a500960` deepens the finance, personnel, and unit sections. Finance output now includes loan defaults, active loan summaries, and derived warnings for negative balance, overdue loans, and loan defaults. Personnel output now includes assignment dates, deployed/employed flags, compact injury counts/severity, leadership markers, and current-personnel market membership. Unit output now includes availability/deployability, commander id, maintenance site, and read-only transport assignment/carried-unit summaries.

`Confirmed from source`: source commit `495b58faef` deepens contract and scenario output. Contract DTOs now include descriptions, dates, travel days, payment summaries, salvage summaries, rental summaries, and scenario links. Scenario DTOs now include descriptions, linked scenario ids, StratCon type, map and planetary condition summaries, player force ids/unit ids, salvage assignments, objective summaries, bot-force summaries, bot-force stubs, and a read-only tactical-result context block.

`Confirmed from source`: source commit `911a338788` deepens logistics, reports, and market safeguards. Repairs/logistics output now includes repair pressure counts, a display-only repair queue, shopping-list pressure and rows from `IAcquisitionWork`, transport/cargo relationship summaries, and an automation guard marking repair execution, assignment, procurement execution, and stable selectors unavailable. Reports now include per-category metadata/counts alongside sanitized report rows. Markets now expose display-only summaries and optional unit/personnel/contract rows while explicitly setting `automation_ready: false` and adding unsupported entries for stable offer selectors and market mutation commands.

`Unknown`: no source-confirmed dirty/unsaved campaign flag was found in this V1 pass. Source search found editor-local unsaved state, but not a campaign-wide dirty flag for the loaded campaign, so `dirtyState` remains explicit `Unknown` with a warning and a structured unsupported entry naming `MekHQ GUI save-state tracking` as the recommended owner.

`Unsupported`: V1 does not expose stable market offer selectors, stable repair-work ids, repair execution, repair assignment, shopping-list purchase/priority mutation, unit purchase, personnel hire/fire, contract accept/decline, market refresh, negotiation, save, or writeback commands. Market rows and repair/acquisition rows are display-only context and must not be treated as durable selectors.

## Fixtures

Sanitized examples:

- `docs/templates/mekhq-live-campaign-summary.fixture.json`
- `docs/templates/mekhq-live-campaign-state.fixture.json`
- `docs/templates/mekhq-live-campaign-warning-heavy.fixture.json`

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
```

## Source Push Status

`Blocked`: the source commits listed above are committed locally in `external/src/mekhq`, but pushing that branch failed because the checkout remote is `https://github.com/MegaMek/mekhq.git` and the authenticated account lacks write permission.

The workspace documentation commit can still be pushed to `walt-raymond-williams/megamek-workspace`.
