# MEK-RPG Live MekHQ Planetary API Design

Status: design for GitHub issue `#85`.

Purpose: define a read-only local MekHQ API endpoint that returns Navigation-tab-style planetary and parent-system facts for the loaded campaign date.

## Scope

`Confirmed from source`: this design is grounded in the issue `#84` source audit and these local source owners:

- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporter.java`
- `external/src/mekhq/MekHQ/src/mekhq/gui/MapTab.java`
- `external/src/mekhq/MekHQ/src/mekhq/gui/view/PlanetViewPanel.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/Campaign.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/universe/Systems.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/universe/PlanetarySystem.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/universe/Planet.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/universe/SourceableValue.java`

V1 remains a live read endpoint inside the disabled-by-default, loopback-only MekHQ local control API. It must not save, mutate the campaign, inspect Swing selection state, edit planetary overrides, or use raw save/XML parsing.

## Endpoint

Decision:

```http
GET /campaign/planetary/detail
```

Reasoning:

- It matches the explicit selected-detail style of `GET /campaign/personnel/detail`.
- It leaves room for future list/search endpoints such as `/campaign/planetary/search`.
- It makes clear that the endpoint returns one resolved system and one selected planet, not the whole map.

Unsupported methods return HTTP `405` with a refused control response.

No loaded campaign returns HTTP `409`, matching `/campaign/summary`, `/campaign/state`, `/campaign/personnel/detail`, `/campaign/pending-deployments`, and `/campaign/commands`.

## Query Parameters

V1 accepts exact selectors only. Do not implement fuzzy or partial matching in this issue.

| Parameter | Required | Behavior |
| --- | --- | --- |
| `systemId` | No | Exact `Campaign#getSystemById(String)` selector. Preferred stable selector. |
| `systemName` | No | Exact case-insensitive effective system name at the loaded campaign date. Implementation should scan `Campaign#getSystems()` and return ambiguity instead of using the first-match behavior of `Campaign#getSystemByName(String)` directly. |
| `planetId` | No | Exact `PlanetarySystem#getPlanetById(String)` selector inside the resolved system. |
| `planetPosition` | No | Integer system position for `PlanetarySystem#getPlanet(int)`. |
| `planetName` | No | Exact case-insensitive effective planet name or printable name at the loaded campaign date. If `systemId` or `systemName` is supplied, search only that system. If no system selector is supplied, scan campaign systems and require exactly one match. |

Selector rules:

- At least one of `systemId`, `systemName`, or `planetName` is required.
- Prefer `systemId` when MEK-RPG already has it from an earlier response.
- If `systemId` and `systemName` are both supplied, `systemId` resolves the system and `systemName` is a guard that must match the resolved system's effective name.
- If more than one planet selector is supplied, they are guards on the same selected planet. For example, `planetId` plus `planetName` must identify the same planet.
- If a system is resolved and no planet selector is supplied, select `system.getPrimaryPlanet()`, matching the useful default of the Navigation system panel.
- If the system has no selected/default planet, return HTTP `404` with `status_reason: "planet_not_found"`.

Examples:

```text
/campaign/planetary/detail?systemId=Terra
/campaign/planetary/detail?systemName=Terra
/campaign/planetary/detail?systemId=Terra&planetId=Terra
/campaign/planetary/detail?systemId=Terra&planetPosition=3
/campaign/planetary/detail?planetName=Galatea
```

## Date Behavior

Decision: V1 uses only the loaded campaign date from `Campaign#getLocalDate()`.

The response must echo:

- `campaign_date`
- `effective_date`
- `date_policy: "loaded_campaign_date"`

`asOfDate` is deliberately deferred. If an implementation chooses to parse it in issue `#86`, it should only accept a value equal to the loaded campaign date or return HTTP `400` with `status_reason: "as_of_date_not_supported"`.

Reasoning:

- `Confirmed from source`: many planet and system values are date-sensitive through `Planet#getEventData(...)` and `PlanetarySystem#getEventData(...)`.
- `Confirmed from source`: academies use `PlanetarySystem#getFilteredAcademies(Campaign)`, campaign options, current year, and user/campaign context.
- `Decision`: a read meant to mirror the loaded Navigation tab should not silently use wall-clock time or a caller-supplied historical/future date until source support is explicitly designed.

## Response Envelope

Successful response:

```json
{
  "schema_name": "mekhq-live-planetary-detail",
  "schema_version": "0.1",
  "producer": "mekhq-local-control-api",
  "producer_version": "<suite version>",
  "mekhq_version": "<suite version>",
  "exported_at": "<instant>",
  "api_mode": "local-read-only-live-context",
  "read_only": true,
  "campaign_id": "<uuid>",
  "campaign_name": "<name>",
  "campaign_date": "3025-04-08",
  "effective_date": "3025-04-08",
  "date_policy": "loaded_campaign_date",
  "state_revision": "live-...",
  "snapshot_id": "live-...",
  "lookup": {},
  "supported_lookup": {},
  "system": {},
  "selected_planet": {},
  "planets": [],
  "warnings": [],
  "unsupported": [],
  "collector_timing": [],
  "endpoint_timing": {}
}
```

Conventions:

- Keep snake_case for new read-side payloads, matching `bridge_metadata`, `state_revision`, `collector_timing`, and recent detail endpoint style.
- Use `status: "ready"` and `status_reason: "planetary_detail_ready"` for the success payload.
- Include `response_status: "complete"` unless the implementation intentionally supports partial response; V1 can fail the whole endpoint if the selected system/planet collector fails.
- Reuse the existing live state revision pattern: a process-local snapshot id made from campaign id, loaded date, and export instant. It is a guard hint, not a durable save id.

## Lookup Echo

The `lookup` object should echo normalized input and resolved selectors:

```json
{
  "requested": {
    "system_id": "Terra",
    "system_name": null,
    "planet_id": null,
    "planet_position": null,
    "planet_name": null
  },
  "resolved": {
    "system_id": "Terra",
    "system_name": "Terra",
    "planet_id": "Terra",
    "planet_name": "Terra",
    "planet_position": 3,
    "selection_source": "primary_planet_default"
  }
}
```

`supported_lookup` should report:

```json
{
  "system_id": true,
  "system_name_exact_case_insensitive": true,
  "planet_id": true,
  "planet_position": true,
  "planet_name_exact_case_insensitive": true,
  "planet_name_global_exact": true,
  "partial_name_search": false,
  "ui_selected_navigation_target": false,
  "as_of_date": false,
  "source_owner": "Campaign#getSystems(), Campaign#getSystemById(), PlanetarySystem#getPlanetById(), PlanetarySystem#getPlanet(int)"
}
```

If the caller used `planetName` without a system selector, include a warning that global exact planet-name lookup can be ambiguous and clients should cache the returned `system_id` and `planet_id` for repeat reads.

## Error Behavior

Use structured read-response errors rather than raw exceptions.

| HTTP | `status` | `status_reason` | When |
| --- | --- | --- | --- |
| `400` | `refused` | `missing_selector` | No `systemId`, `systemName`, or `planetName`. |
| `400` | `refused` | `invalid_planet_position` | `planetPosition` is not an integer. |
| `400` | `refused` | `conflicting_selectors` | Supplied selectors resolve to different systems or planets. |
| `400` | `refused` | `as_of_date_not_supported` | Caller supplies unsupported `asOfDate`. |
| `404` | `not_found` | `system_not_found` | No system matches `systemId` or exact `systemName`. |
| `404` | `not_found` | `planet_not_found` | System resolved, but no planet matches selector/default. |
| `409` | `blocked` | `no_campaign_loaded` | No campaign is loaded in the MekHQ GUI. |
| `409` | `ambiguous` | `ambiguous_system_name` | Exact effective system name matches more than one system. |
| `409` | `ambiguous` | `ambiguous_planet_name` | Exact planet name matches more than one planet. |
| `500` | `failed` | `planetary_detail_failed` | Unexpected source exception. |

Ambiguity payloads should include bounded `candidates` rows:

```json
{
  "status": "ambiguous",
  "status_reason": "ambiguous_planet_name",
  "message": "planetName matched multiple planets at the loaded campaign date. Retry with systemId and planetId.",
  "candidates": [
    {
      "system_id": "Example",
      "system_name": "Example",
      "planet_id": "Example I",
      "planet_name": "Example I",
      "planet_position": 1,
      "primary_planet": true
    }
  ]
}
```

Candidate rows should be capped, with `candidate_count` and `candidates_truncated` when the full match list is larger than the returned list.

## Sourceable Value Envelope

Planetary source data should be structured instead of display-only strings.

Use this shape for `SourceableValue<T>` backed fields:

```json
{
  "value": "A",
  "display": "Class A",
  "source": "source id or null",
  "version": "source version or null",
  "canon": true,
  "evidence": "Confirmed from MekHQ export",
  "method_backed": true,
  "source_owner": "Planet#getSourcedHPG(LocalDate)",
  "warnings": []
}
```

Notes:

- `Confirmed from source`: `SourceableValue#isCanon()` returns true when `source` is non-null.
- `Decision`: `display` is for the GM/client UI; `value` is the machine-readable raw value or enum name.
- `Decision`: when a field is computed rather than sourceable, use the same evidence/method-backed style but include `computed: true` and omit source/version.
- `Decision`: when a UI-visible field is absent because the source method returned null, omit the field from the detailed group and report absence through a compact `visibility` or `available_fields` list only if implementation needs it for tests.

## System Object

`system` should include:

- `id`: `PlanetarySystem#getId()`
- `sucs_id`: `PlanetarySystem#getSucsId()`
- `name`: effective `PlanetarySystem#getName(effectiveDate)`
- `display_name`: `PlanetarySystem#getPrintableName(effectiveDate)`
- `coordinates`: `x`, `y` from `getX()` and `getY()`
- `connector`: `PlanetarySystem#isConnector()`
- `primary_planet_position`: `PlanetarySystem#getPrimaryPlanetPosition()`
- `star`: sourceable `PlanetarySystem#getSourcedStar()`
- `recharge`: display and raw facts from `getRechargeStationsText(effectiveDate)`, `getSourcedNadirCharge(effectiveDate)`, `getSourcedZenithCharge(effectiveDate)`, `getNumberRechargeStations(effectiveDate)`, `getRechargeTime(effectiveDate, false)`, and `getRechargeTimeText(effectiveDate, false)`

The endpoint should mark connector systems with a warning because they are routing helpers, not normal inhabited campaign worlds.

## Planets Summary

`planets` should be a compact list for disambiguation and client routing:

- `id`
- `name`
- `display_name`
- `system_position`
- `displayable_system_position`
- `orbit_radius_au`
- `planet_type`
- `primary_planet`
- `selected`

This list should not duplicate every selected-planet detail.

## Selected Planet Object

`selected_planet` should include identifiers plus grouped Navigation-tab facts.

Identifier fields:

- `id`: `Planet#getId()`
- `name`: `Planet#getName(effectiveDate)`
- `display_name`: `Planet#getPrintableName(effectiveDate)`
- `system_position`: `Planet#getSystemPosition()`
- `displayable_system_position`: `Planet#getDisplayableSystemPosition()`
- `orbit_radius_au`: `Planet#getOrbitRadius()`
- `primary_planet`: selected position equals system primary position

Recommended groups:

- `ownership`: faction codes from `Planet#getSourcedFactions(effectiveDate)`, faction display from `Planet#getFactionDesc(effectiveDate)`.
- `physical`: planet type, diameter, position display, jump-point travel days at 1g, year length, day length, gravity.
- `environment`: atmosphere composition category, atmospheric pressure category, atmospheric composition text, equatorial temperature C, surface water percent, highest native life.
- `orbital_features`: satellites, small moons, dust ring.
- `communications_and_services`: HPG sourceable value plus null/default policy, hiring hall computed/source override, academies, noteworthy diseases.
- `geography`: landmasses and capitals.
- `population_and_industry`: population, socio-industrial raw code/component ratings/display description.
- `description`: raw markdown/plain text plus sanitized display text. Do not force clients to parse Swing HTML.

## Special Field Policies

HPG:

- Return `hpg.sourced` from `Planet#getSourcedHPG(effectiveDate)`.
- Return `hpg.effective_default` from `Planet#getHPG(effectiveDate)` only when useful.
- If the sourced value is null but `getHPG()` would default to `X`, include a warning or subfield such as `defaulted_when_missing: "X"` so clients do not confuse a displayed UI row with a source-backed value.

Hiring hall:

- Return `level` from `Planet#getHiringHallLevel(effectiveDate)`.
- Include `computed: true` when `Planet#getSourcedHiringHallLevel(effectiveDate)` is null.
- Include the sourced override when present.
- Add `source_owner: "Planet#getHiringHallLevel(LocalDate)"`.

Academies:

- Return a list from `PlanetarySystem#getFilteredAcademies(Campaign)`.
- Include at least `name`, `description`, and `set/source category` if source exposes it cheaply.
- Include `filtered_by_campaign_options: true`.
- Include a warning that academy availability depends on the loaded campaign year/options and user academy data.

Noteworthy diseases:

- Return active disease rows from `CanonicalDiseaseType#getAllActiveBioweapons(systemId, date, true)` and `getAllActiveDiseases(systemId, date, true)`.
- Include simple display names and type/source category when feasible.
- Mark as advisory context, not a complete medical-risk model.

Text/HTML:

- Preserve source text where the model stores text, for example `Planet#getDescription()`.
- Return sanitized display text for Markdown/HTML UI fields.
- Do not expose Swing HTML as the only representation.

## Unsupported Entries

Every successful V1 response should include these unsupported boundaries when relevant:

```json
[
  {
    "area": "planetary.lookup.ui_selected_navigation_target",
    "reason_code": "ui_selection_unavailable",
    "message": "The local control API does not expose the current Swing Navigation-tab selected planet or system.",
    "source_owner": "Future MekHQ GUI selection bridge",
    "automation_ready": false
  },
  {
    "area": "planetary.lookup.as_of_date",
    "reason_code": "historical_date_query_deferred",
    "message": "V1 planetary detail mirrors the loaded campaign date only.",
    "source_owner": "Future planetary detail date-mode design",
    "automation_ready": false
  }
]
```

Also add unsupported rows for any UI-only rendering that implementation cannot safely sanitize in issue `#86`.

## Implementation Notes For Issue 86

Recommended source shape:

- Register `/campaign/planetary/detail` in `LocalControlService#startIfEnabled()`.
- Add `handlePlanetaryDetail(HttpExchange)` beside the other read handlers.
- Keep lookup and DTO construction in a dedicated exporter/helper, likely `LocalPlanetaryDetailExporter`, instead of growing `LocalCampaignStateExporter` further.
- Use `Campaign#getSystems()` when scanning for duplicate effective system/planet names.
- Use `Campaign#getSystemById(String)` for stable system id lookup.
- Do not use `Systems.getInstance()` for campaign reads unless deliberately falling back outside loaded-campaign context.
- Reuse local API helpers for query parsing, JSON writing, endpoint timing, warning rows, unsupported rows, and source/evidence envelopes where practical.

## Expected Tests And Fixtures

Source tests for issue `#86` should cover:

- HTTP route exists and supports GET only.
- No loaded campaign returns HTTP `409`.
- Missing selectors return HTTP `400`.
- `systemId` lookup resolves a system and defaults to the primary planet.
- `systemName` exact case-insensitive lookup works.
- Unknown `systemId` or `systemName` returns HTTP `404`.
- `planetId`, `planetPosition`, and `planetName` select the intended planet within a system.
- Conflicting selectors return HTTP `400`.
- Duplicate effective system or planet names return HTTP `409` with candidate rows.
- `asOfDate` is refused or explicitly constrained to the loaded campaign date.
- Response includes campaign metadata, effective date, state revision, lookup echo, system facts, selected planet facts, warnings, unsupported entries, collector timing, and endpoint timing.
- Sourceable value DTOs include `value`, `display`, `source`, `version`, `canon`, `evidence`, `method_backed`, and `source_owner`.
- HPG null/default behavior and computed hiring-hall behavior are represented explicitly.

Fixture work for issue `#87` should add:

- A successful sanitized planetary detail payload.
- At least one error or ambiguity payload if useful for MEK-RPG adapter tests.
- A live smoke checklist using a source-built MekHQ GUI with `-Dmekhq.controlApi.enabled=true`, a safe loaded campaign, and a stable query such as a known current campaign world.

## Open Questions

- Which planet/system should become the canonical sanitized fixture for MEK-RPG adapter tests?
- Should a future endpoint expose search/list candidates separately so clients can resolve names before calling detail?
- Should arbitrary historical/future `asOfDate` support be added after academies, campaign overlays, and user data behavior are source-confirmed?
