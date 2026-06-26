# MEK-RPG Live MekHQ API Timeout Audit

Status: completed for GitHub issue `#63` on `2026-06-26`.

## Result

`Confirmed by MEK-RPG handoff`: live MEK-RPG play timed out on `GET /campaign/summary`, `GET /campaign/state`, and `GET /campaign/commands`, including narrowed `sections=` requests with 45-second and 60-second client timeouts.

`Confirmed from source`: `LocalControlService` registers `/campaign/summary`, `/campaign/state`, and `/campaign/commands` on a single `HttpServer` executor thread named `mekhq-local-control-api`. One slow request can therefore queue later local API requests behind it.

`Confirmed from source`: source commit `5effaa5517` in `external/src/mekhq` adds response-visible timing diagnostics:

- `/campaign/summary`: top-level `endpoint_timing` and `collector_timing`.
- `/campaign/state`: top-level `endpoint_timing` plus per-section `collector_timing`.
- `/campaign/commands`: top-level `endpoint_timing` plus readiness/selector `collector_timing`.
- `LocalControlService` logs a warning when a read endpoint takes at least `1000` ms.

## Collector Map

`GET /campaign/summary`

- `LocalControlService#handleCampaignSummary(...)` calls `new LocalCampaignStateExporter(campaign).summary()`.
- `Confirmed from source`: the summary reads campaign identity/date/version, current system/location, dirty-state warnings, and unsupported entries. It should be lightweight, but it can still be blocked behind another request because the HTTP executor is single-threaded.

`GET /campaign/state`

- `LocalControlService#handleCampaignState(...)` calls `LocalCampaignStateExporter#state(parseSections(exchange))`.
- `Confirmed from source`: `sections=` filtering is section-gated. Requested sections are built only when included, and omitted `sections` requests every supported section.
- Likely stall points:
  - `personnel`: walks `Campaign#getAllPersonnel()` and builds per-person assignment, fatigue, injury, salary, leadership, and market-membership rows.
  - `units`: walks `Campaign#getUnits()` and builds entity, availability, crew, maintenance, repair, and transport rows.
  - `scenarios`: walks `Campaign#getScenarios()` and calls scenario force, objective, bot-force, bot-force-stub, map, condition, salvage, and report getters.
  - `repairs_and_logistics`: repeatedly walks units for parts/service/repair pressure, builds repair queue rows, shopping-list rows, cargo, and transport summaries.
  - `markets`: walks unit, personnel, and contract market rows. Unit market display rows avoid full entity loading, but contract rows reuse the deeper contract DTO.
  - `reports`: reads report category counts and up to `12` sanitized rows per category.

`GET /campaign/commands`

- `LocalControlService#handleCampaignCommands(...)` calls `LocalCommandReadinessExporter#commands()`.
- `Confirmed from source`: readiness builds a state revision from unit-market and contract-market fingerprints, then builds selector groups for campaign, current personnel, current units, personnel-market applicants, contract-market offers, unit-market offers, and command rows.
- Likely stall points:
  - `state_revision`: `LocalContractMarketSelectors#stateRevision(...)` fingerprints both unit-market offers and contract-market offers.
  - `selectors.unit_market_offers`: `LocalUnitMarketOfferSelectors#selectorCandidates(...)` can call `UnitMarketOffer#getEntity()` and `UnitMarketOffer#getPrice()` per offer.
  - `selectors.contract_market_offers`: `LocalContractMarketSelectors#selectorCandidates(...)` builds guard facts, prompt facts, destination/payment fields, and fingerprints for each offer.
  - `command_readiness`: currently recomputes contract and unit-market selector availability for command rows.

## Recommendations

Next issue: `#64`, keep summary and command readiness fast and bounded.

Start with the timing fields from `5effaa5517` during a live disposable-campaign smoke test. If `/campaign/summary` shows low collector time but high endpoint time after another request, the single-thread queue is the active problem. If `/campaign/commands` shows high `state_revision`, `selectors.unit_market_offers`, or `selectors.contract_market_offers`, cache or bound selector construction before adding more command rows.

Issue `#65` later made `/campaign/state` partial-response capable for section collector failures and verified narrowed requests do not traverse unrelated personnel/unit/scenario collections. True per-section timeout cancellation remains deferred because timed-out background collectors could keep reading live campaign state concurrently.

## Verification

`Confirmed locally`: from `external/src/mekhq`, `.\gradlew.bat :MekHQ:test --tests mekhq.service.LocalCommandReadinessExporterTest --tests mekhq.service.LocalControlServiceHttpTest` passed on `2026-06-26`. The run emitted existing deprecation and unchecked-operation warnings.

`Confirmed locally`: from `external/src/mekhq`, `.\gradlew.bat :MekHQ:compileJava :MekHQ:checkstyleMain` passed on `2026-06-26`.

`Blocked`: source push is still blocked because `external/src/mekhq` uses `https://github.com/MegaMek/mekhq.git` as `origin`, and GitHub returned `Permission to MegaMek/mekhq.git denied to walt-raymond-williams`.
