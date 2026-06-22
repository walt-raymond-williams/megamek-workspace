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

`Unknown`: no source-confirmed dirty/unsaved campaign flag was found in this V1 pass, so `dirtyState` is explicit `Unknown` with a warning and an unsupported entry.

`Unsupported`: V1 does not expose stable market offer selectors, stable repair-work ids, full cargo/transport semantics, personnel injuries/skills, or write/action surfaces. Markets are intentionally display-only/empty with automation-blocking unsupported entries.

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

`Not yet live-tested`: no user-assisted running MekHQ campaign smoke test was performed in this issue. Use a copied/disposable campaign and the source-built GUI before treating live output as validated against the UI.

Suggested smoke commands after launching source-built MekHQ with a disposable campaign loaded:

```powershell
Invoke-RestMethod -Method Get -Uri 'http://127.0.0.1:32180/campaign/summary' -TimeoutSec 10 |
  ConvertTo-Json -Depth 8

Invoke-RestMethod -Method Get `
  -Uri 'http://127.0.0.1:32180/campaign/state?sections=campaign,finances,personnel,units,contracts,scenarios,repairs_and_logistics,reports,unsupported' `
  -TimeoutSec 30 |
  ConvertTo-Json -Depth 12
```

## Source Push Status

`Blocked`: source commit `7d3b345327` is committed locally in `external/src/mekhq`, but pushing that branch failed because the checkout remote is `https://github.com/MegaMek/mekhq.git` and the authenticated account lacks write permission.

The workspace documentation commit can still be pushed to `walt-raymond-williams/megamek-workspace`.
