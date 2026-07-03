# MEK-RPG Live MekHQ Planetary API Source Audit

Status: source audit for GitHub issue `#84`.

Purpose: map the MekHQ Navigation tab planetary/system facts to source methods before designing a read-only local API endpoint in issue `#85`.

## Scope

`Confirmed from source`: this audit covers the Navigation tab side information panel produced by:

- `external/src/mekhq/MekHQ/src/mekhq/gui/NavigationTab.java`
- `external/src/mekhq/MekHQ/src/mekhq/gui/MapTab.java`
- `external/src/mekhq/MekHQ/src/mekhq/gui/view/PlanetViewPanel.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/universe/Planet.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/universe/PlanetarySystem.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/universe/Systems.java`

This is a read-only audit. No MekHQ source endpoint was implemented for `#84`.

## UI Path

`Confirmed from source`: `NavigationTab` owns two subtabs, `MapTab` and `LocationsTab`. The planetary/system information panel is created by `MapTab`, not directly by `NavigationTab`.

- `NavigationTab#showSystem(...)` selects the map subtab and calls `MapTab#switchSystemsMap(PlanetarySystem)`.
- `NavigationTab#showPlanet(...)` keeps primary planets on the interstellar system view, but calls `MapTab#switchPlanetaryMap(Planet)` for non-primary planets.
- `MapTab#refreshSystemView()` renders `new PlanetViewPanel(system, campaign)` for the selected interstellar system.
- `MapTab#refreshPlanetView()` renders `new PlanetViewPanel(system, campaign, selectedPlanetPosition)` for a selected planet in the planetary system view.
- `PlanetViewPanel` uses `campaign.getLocalDate()` for all date-sensitive values.

`Confirmed from source`: if `PlanetViewPanel(system, campaign)` receives no planet position, it requests system position `0`, then falls back to `system.getPrimaryPlanet()` when no planet exists at position `0`. Therefore a system-level selection shows the system panel plus the primary planet panel when one exists.

## Lookup Behavior

`Confirmed from source`: the map search field is populated from `Campaign#getSystemNames()`, which iterates the campaign's `systemsInstance` and uses `PlanetarySystem#getPrintableName(campaign.getLocalDate())`.

`Confirmed from source`: `Campaign#getSystemByName(String)` delegates to `Systems#getSystemByName(name, campaign.getLocalDate())`. That lookup is exact, case-insensitive, and date-sensitive because it compares the supplied text with `PlanetarySystem#getName(when)`.

`Confirmed from source`: `Campaign#getSystemById(String)` delegates to `Systems#getSystemById(String)`.

`Confirmed from source`: `PlanetarySystem#getPlanetById(String)` performs an exact id match across planets in the system, and `PlanetarySystem#getPlanet(int)` selects by system position.

Design input for `#85`: V1 should accept exact system id and exact planet id/system position for deterministic lookup. If V1 accepts a name, it should either follow the UI's exact case-insensitive system-name lookup or return/refuse with candidate rows instead of guessing on partial names.

## Date-Sensitive Data

`Confirmed from source`: `Planet` stores dated `PlanetaryEvent` rows. `Planet#getEventData(...)` returns the latest applicable event value as of the requested date, falling back to base values when no event value is present.

`Confirmed from source`: these planet values can be date-sensitive through event data:

- name and short name
- faction ownership list and owner display text
- day length
- pressure
- atmosphere
- atmospheric composition
- temperature
- percent water
- life form
- HPG rating
- hiring hall override
- population
- socio-industrial data

`Confirmed from source`: `PlanetarySystem` also stores dated system events for nadir and zenith recharge stations. `PlanetarySystem#getRechargeStationsText(...)` and `PlanetarySystem#getRechargeTimeText(...)` depend on campaign date.

Design input for `#85`: every response should echo the effective date used for lookup and calculation, normally the loaded campaign date. Optional `asOfDate` support can be designed later, but V1 should not silently use wall-clock time.

## Source Metadata

`Confirmed from source`: most planetary fields are `SourceableValue<T>`. `SourceableValueLabel` displays the value and adds a tooltip with source/version metadata, treating values without `source` as noncanon.

Design input for `#85`: expose sourceable fields as structured objects, not only display strings. A useful shape is:

```json
{
  "value": "...",
  "display": "...",
  "source": "...",
  "version": "...",
  "canon": true,
  "source_owner": "Planet#getSourced..."
}
```

The existing local API uses `value`, `evidence`, `method_backed`, `source_owner`, and `warnings` envelopes in `LocalCampaignStateExporter`; the planetary endpoint should stay consistent with that style while adding `source`, `version`, and `canon` for `SourceableValue` fields.

## Visible Field Inventory

`Confirmed from source`: `PlanetViewPanel` loads visible labels from `MekHQ/resources/mekhq/resources/PlanetViewPanel.properties`.

| UI field | Source method(s) | Date-sensitive | Visibility and API notes |
| --- | --- | --- | --- |
| System title | `PlanetarySystem#getPrintableName(LocalDate)` | Yes | Border title only. Include system id/name in response metadata. |
| Star Type (Recharge Time) | `PlanetarySystem#getSourcedStar()`, `PlanetarySystem#getRechargeTimeText(date, false)` | Partly | Star is sourceable. Recharge time depends on star and dated recharge-station state. |
| Recharging Station | `PlanetarySystem#getRechargeStationsText(date)`, backed by `getSourcedNadirCharge(date)` and `getSourcedZenithCharge(date)` | Yes | Return raw booleans plus display label. |
| Planet panel title | `Planet#getPrintableName(LocalDate)` | Yes | Uses short name if present, else full name, else `unnamed`. |
| Planet owner line | `Planet#getFactionDesc(LocalDate)` | Yes | Display row above normal fields. Return faction codes from `getSourcedFactions(date)` plus display names if feasible. |
| Planet Type | `Planet#getSourcedPlanetType()` | No | Sourceable enum. |
| Diameter | `Planet#getSourcedDiameter()` | No | Hidden for asteroid belts. Display is `km` with one decimal. |
| Position in System | `Planet#getSourcedSystemPosition()`, `Planet#getDisplayableSystemPosition()`, `Planet#getOrbitRadius()` | Partly no | Display hides asteroid belts from ordinal count and appends AU when orbit radius exists. For asteroid belts with orbit radius, display is only AU. |
| Time to Jump Point | `Planet#getTimeToJumpPoint(1)` | No, except via selected planet/orbit | Computed days at 1g, rounded in UI. Return raw days and display. |
| Year length | `Planet#getSourcedYearLength()` | No | Hidden when null. Display says Terran years. |
| Day length | `Planet#getSourcedDayLength(date)` | Yes | Hidden when null. Display says hours. |
| Surface Gravity | `Planet#getSourcedGravity()` | No | Hidden when null. Display appends `g`. |
| Atmosphere | `Planet#getSourcedAtmosphere(date)` | Yes | This is MekHQ composition category (`mekhq.campaign.universe.Atmosphere`), not MegaMek pressure. |
| Atmospheric Pressure | `Planet#getSourcedPressure(date)` | Yes | This is MegaMek planetary condition pressure (`megamek.common.planetaryConditions.Atmosphere`). Keep separate from composition atmosphere. |
| Atmospheric Composition | `Planet#getSourcedComposition(date)` | Yes | Hidden when null. UI renders as HTML. API should sanitize or return plain and display forms. |
| Equatorial Temperature | `Planet#getSourcedTemperature(date)` | Yes | Hidden when null. Display appends C. |
| Surface Water | `Planet#getSourcedPercentWater(date)` | Yes | Hidden when null. Display appends percent. |
| Highest Native Life | `Planet#getSourcedLifeForm(date)` | Yes | Hidden when null. |
| Satellites | `Planet#getSatellites()`, `Satellite#getSourcedName()`, `Satellite#getSize()`, `Planet#getSourcedSmallMoons()`, `Planet#getSourcedRing()` | No | Visible when satellites exist, small moons > 0, or ring is true. Return structured list, small moon count, and ring boolean. |
| HPG Class Type | `Planet#getSourcedHPG(date)` | Yes | Hidden when null. `Planet#getHPG(date)` defaults missing to `X`, but the UI hides null sourced values. |
| Hiring Hall | `Planet#getHiringHallLevel(date)` | Yes | Always visible. It may use an event override, otherwise computes from population, factions, HPG, and socio-industrial tech. |
| Academies | `PlanetarySystem#getFilteredAcademies(Campaign)`, `AcademyFactory`, `PlanetarySystem#getAcademiesForSystem(...)` | Yes by year and option | Visible only when filtered list is non-empty. Excludes Local Academies and Unit Education, respects the prestigious-academies campaign option, system id, non-local/non-home-school flags, officer-name exclusion, and construction/closure/destruction years. |
| Noteworthy Diseases | `CanonicalDiseaseType#getAllActiveBioweapons(systemId, date, true)` plus `getAllActiveDiseases(systemId, date, true)` | Yes | Visible only when active set is non-empty. UI shows `InjuryType#getSimpleName()`. |
| Landmasses | `Planet#getLandMasses()`, `LandMass#getSourcedName()`, `LandMass#getSourcedCapital()` | No | Visible when landmass list exists. Capital rows are indented in UI. |
| Population | `Planet#getSourcedPopulation(date)` | Yes | Hidden when null. Display uses grouping separators. |
| Socio-Industrial Levels | `Planet#getSourcedSocioIndustrial(date)`, `SocioIndustrialData#getHTMLDescription()` | Yes | UI discards default `SourceableValueLabel` text and replaces it with the HTML description. API should return raw USILR code, component ratings, and display description. |
| Description | `Planet#getDescription()` | No | Visible when non-null. Rendered from Markdown to HTML in UI. API should return raw markdown/plain text plus optional rendered/sanitized display. |

## Non-Visible But Useful Identifiers

Design input for `#85`: include these even though they are not all label rows:

- system id from `PlanetarySystem#getId()`
- SUCS id from `PlanetarySystem#getSucsId()`
- coordinates from `PlanetarySystem#getX()` and `getY()`
- connector flag from `PlanetarySystem#isConnector()`
- primary planet position from `PlanetarySystem#getPrimaryPlanetPosition()`
- planet id from `Planet#getId()`
- planet system position from `Planet#getSystemPosition()`
- orbit radius AU from `Planet#getOrbitRadius()`

These identifiers are useful for disambiguation, repeat reads, and client routing, and avoid forcing MEK-RPG to treat display labels as selectors.

## Recommended Endpoint Inputs For Issue 85

Design a read-only endpoint around these decisions:

- Path candidate: `GET /campaign/planetary-info`.
- Required campaign state: loaded campaign, because campaign date and campaign planetary overrides matter.
- Query candidates: `systemId`, `systemName`, `planetId`, `planetPosition`, and possibly `asOfDate`.
- V1 recommendation: require one of `systemId` or `systemName`; optionally accept `planetId` or `planetPosition`; default to the primary planet when no planet selector is supplied.
- Name behavior: exact case-insensitive system-name lookup matching `Campaign#getSystemByName(...)`; do not implement partial matching in V1 unless the response explicitly reports candidates.
- Response metadata: schema name/version, campaign id/name/date, effective date, state revision, read-only flag, lookup selector echo, warnings, unsupported, and endpoint timing.
- Response body: `system`, `selected_planet`, `planets` summary list, and `display_sections` or similarly grouped Navigation-tab fields.
- Sourceable values: include raw value, display label, source, version, canon flag, method owner, and warnings.
- HTML/Markdown fields: return raw source text and sanitized display text; avoid making clients parse Swing HTML.

## Unsupported Or Risk Notes

- `Confirmed from source`: the UI has no source-confirmed "currently selected Navigation tab planet" exposed through the local API. The endpoint should require explicit selectors rather than trying to inspect Swing state.
- `Confirmed from source`: `Systems#getSystemByName(...)` returns the first exact name match from a concurrent map iteration. If duplicate effective names exist, name-only lookup may be ambiguous. Prefer ids for stable API use.
- `Confirmed from source`: campaign-specific planetary overrides are applied through the campaign systems registry. API code should use `Campaign#getSystemById(...)` and `Campaign#getSystemByName(...)`, not the global `Systems.getInstance()` unless deliberately operating without a campaign.
- `Confirmed from source`: `Planet#getHPG(date)` defaults missing HPG to `X`, but the UI only displays HPG when `getSourcedHPG(date)` is non-null. Preserve both display parity and explicit null/default semantics.
- `Confirmed from source`: `Planet#getHiringHallLevel(date)` can be derived even when no sourceable hiring-hall override exists. Mark the output as computed and include the contributing source fields when practical.
- `Confirmed from source`: academies depend on campaign options, user academy files, and current year. Treat academy rows as live campaign context, not pure canonical planet data.
- `Confirmed from source`: noteworthy disease rows depend on medical alternate disease data and strict active-system/date matching. Return them as advisory context, not a full medical risk model.

## Verification

`Confirmed from source`: source inspection only. Commands used included:

```powershell
rg -n "class PlanetViewPanel|SourceableValueLabel|getPlanetPanel|getSystemPanel" external/src/mekhq/MekHQ/src/mekhq
rg -n "getSystemByName|getPlanetById|getPlanet\\(" external/src/mekhq/MekHQ/src/mekhq/campaign external/src/mekhq/MekHQ/src/mekhq/gui
```

No build or live MekHQ smoke was required for this audit-only issue.

## Next Step

Proceed to issue `#85`: design the read-only planetary information API using the field inventory and endpoint inputs above.
