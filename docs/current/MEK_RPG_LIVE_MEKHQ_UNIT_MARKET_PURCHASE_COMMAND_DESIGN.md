# MEK-RPG Live MekHQ Unit-Market Purchase Command Design

Status: source design for GitHub issue `#49` on `2026-06-23`.

Purpose: define how MEK-RPG should request a live MekHQ unit-market purchase, such as buying a DropShip, while MekHQ remains the authoritative ledger for price, finance, delivery, reports, and market removal.

## Recommendation

`Decision`: implement a narrow V1 endpoint, `POST /campaign/command/markets/unit-offers/purchase`, for one non-black-market offer at a time after `GET /campaign/commands` exposes source-generated live offer selectors.

`Decision`: V1 selectors should be live-session selectors, not durable save-file identifiers. `UnitMarketOffer#writeToXML()` does not serialize a unique offer id, and `AbstractUnitMarket#writeBodyToXML(...)` writes only the offer list. A command can still be safe for the currently loaded campaign if MekHQ generates an opaque selector from the current in-memory offer plus a full source-backed fingerprint and refuses when that fingerprint is no longer unique.

`Decision`: duplicate or ambiguous offers must be refused. The command must not buy by row index, localized display name, partial market fields, or client-computed hash.

## Source Map

`Confirmed from source`: `UnitMarketOffer` stores market type, unit type, `MekSummary`, price percent, transit duration, and campaign options. `getEntity()` loads a new `Entity` from the `MekSummary` source file and entry name. `getPrice()` derives final price from the unit cost, offer percent, and campaign option multipliers for mixed-tech, Clan, or Inner Sphere units.

`Confirmed from source`: `UnitMarketOffer#writeToXML()` serializes only `market`, `unitType`, `unit`, `percent`, and `transitDuration`. It does not serialize a UUID or durable offer id.

`Confirmed from source`: `UnitMarketPane#purchaseSelectedOffers()` owns the current UI purchase flow. It loads the entity, checks funds, debits `TransactionType.UNIT_PURCHASE`, handles black-market swindle logic, then calls private `finalizeEntityAcquisition(...)`.

`Confirmed from source`: `UnitMarketPane#finalizeEntityAcquisition(...)` calls `Campaign#addNewUnit(entity, false, days, UnitMarketType.getQuality(...))`, writes a delivery report when delivery is not instant, removes the offer from the unit market, and refreshes the table model.

`Confirmed from source`: `Campaign#addNewUnit(...)` creates a campaign `Unit`, initializes parts and diagnostics, sets days-to-arrival, mothballs delayed deliveries when that option is enabled, assigns unit/entity ids, initializes transport space, adds transport capability when not mothballed, writes an acquisitions report, and fires `UnitNewEvent`.

`Confirmed from source`: `UnitMarketType#getQuality(...)` can be random when random unit qualities are enabled, and black-market quality can also be random. Black-market purchases in `UnitMarketPane#purchaseSelectedOffers()` can randomly swindle the campaign by debiting partial price without adding the unit.

## Selector Design

Readiness should replace the current blocked `unit_market_offers` selector status with candidate rows only after source implementation adds selector generation.

Each candidate selector row should include:

- `selectorType`: `unit_market_offer_live_selector`.
- `offerSelector`: opaque MekHQ-generated token. MEK-RPG must echo it, but must not construct it.
- `selectorScope`: current process and current `stateRevision`.
- `fingerprint`: canonical source-owned fingerprint fields for diagnostics, not an authority by itself.
- `duplicateCount`: number of currently loaded offers with the same canonical fingerprint.
- `selectable`: true only when `duplicateCount == 1`, entity loading succeeds, the offer is not black market for V1, and price can be computed.
- `refusalReason`: machine-readable reason when not selectable.
- guard facts: market type, unit type code and display label, unit name, MekSummary source file path/name if safe to expose locally, MekSummary entry name, weight class, base cost, price percent, computed final price, transit duration, instant-delivery option, mothball-delivery option, random-quality option, current balance, and source owners.

Canonical fingerprint should be built inside MekHQ from source-backed facts such as:

- `UnitMarketOffer#getMarketType().name()`
- `UnitMarketOffer#getUnitType()`
- `UnitMarketOffer#getUnit().getName()`
- `UnitMarketOffer#getUnit().getSourceFile()`
- `UnitMarketOffer#getUnit().getEntryName()`
- `UnitMarketOffer#getPercent()`
- `UnitMarketOffer#getTransitDuration()`
- `UnitMarketOffer#getPrice()`

The implementation may include the in-memory offer index inside the opaque token for lookup speed, but the command must revalidate the full fingerprint and `duplicateCount == 1` before mutation. If the token points at a different offer after a market refresh or day advance, return `stale_selector`.

## V1 Endpoint

Endpoint:

```text
POST /campaign/command/markets/unit-offers/purchase
```

Required request fields, in addition to the shared command envelope from `MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`:

- `offerSelector`: opaque selector from `GET /campaign/commands`.
- `expectedOfferFingerprint`: exact selector fingerprint returned by MekHQ.
- `expectedUnitName`: MekSummary unit name.
- `expectedUnitType`: unit type code or canonical type name.
- `expectedMarketType`: `UnitMarketType.name()`.
- `expectedPrice`: final computed price as a machine-safe amount plus currency label.
- `expectedPricePercent`: offer percent.
- `expectedTransitDuration`: days from the offer.
- `expectedInstantDelivery`: current `CampaignOptions#isInstantUnitMarketDelivery()`.
- `expectedMothballDelivery`: current `CampaignOptions#isMothballUnitMarketDeliveries()`.
- `expectedRandomUnitQuality`: current `CampaignOptions#isUseRandomUnitQualities()`.
- `expectedBalance`: current `Finances#getBalance()`.
- `deliveryPolicy`: `accept_campaign_option`, `require_instant`, or `require_transit`.
- `qualityPolicy`: V1 should accept `accept_campaign_quality_logic`; requests for exact quality should be refused while random quality can be enabled.
- `appendAuditReport`: optional, default true, for a plain-text `GENERAL` MEK-RPG audit note in addition to MekHQ's normal finance/acquisition reports.

V1 should allow only a single offer per request. Multi-offer purchases can be added later after live tests prove response and rollback semantics.

## Apply Behavior

`Decision`: the implementation should extract reusable source logic from `UnitMarketPane` rather than driving JTable selection. The new service should preserve the same MekHQ side effects:

- load the entity through `UnitMarketOffer#getEntity()`
- compute price through `UnitMarketOffer#getPrice()`
- check funds before mutation
- debit `TransactionType.UNIT_PURCHASE` with the existing purchase description pattern
- add the unit through `Campaign#addNewUnit(...)`
- pass `0` days when instant delivery is enabled, otherwise `offer.getTransitDuration()`
- use `UnitMarketType.getQuality(campaign, offer.getMarketType())`
- add the non-instant delivery report
- remove the purchased offer from `Campaign#getUnitMarket().getOffers()`
- optionally append a plain-text MEK-RPG audit report

The command should run on the Swing event dispatch thread and honor the same one-mutating-command-at-a-time rule as the other guarded command endpoints.

Dry-run should validate all selectors and guards, load the entity, compute price, check current funds, report intended delivery/quality policy, and stop before finance debit, unit add, report append, offer removal, or save.

## Refusal Rules

Refuse with `stable_offer_selector_missing` when readiness has not exposed live selectors.

Refuse with `duplicate_offer_ambiguous` when more than one current offer has the same canonical fingerprint.

Refuse with `stale_selector` when the opaque selector is unknown, belongs to a prior `stateRevision`, points outside the current offer list, or the current offer facts differ from `expectedOfferFingerprint`.

Refuse with `stale_offer_guard` when expected unit name, unit type, market type, price, percent, transit duration, delivery option, mothball option, random-quality option, or balance does not match.

Refuse with `entity_load_failed` when `UnitMarketOffer#getEntity()` returns null or throws.

Refuse with `insufficient_funds` when current balance is less than computed offer price.

Refuse with `black_market_purchase_unsupported` for V1 black-market offers because the current UI flow includes random swindle behavior that can debit money without adding the unit.

Refuse with `delivery_policy_mismatch` when the campaign instant-delivery option conflicts with `deliveryPolicy`.

Refuse with `random_quality_exact_guard_unsupported` when the request expects a specific final unit quality while random unit quality is enabled.

Refuse with `prompt_required` when visible dialogs exist before mutation or an unexpected prompt appears. V1 should not show or answer Swing prompts.

Refuse with `dry_run_required_first` only if implementation decides high-price purchases must be preceded by a matching dry-run in the same process. This is optional; full guard fields may be enough for V1.

## Response And Verification Facts

Responses should include before/after:

- campaign id, name, date, state revision, and save facts
- selector token scope, fingerprint, duplicate count, and offer membership
- market offer count
- balance and finance transaction count
- created transaction facts: type, date, amount, description, and new balance
- selected unit facts: display name, unit type, market type, base cost, price percent, final price, transit duration, entity load facts, and quality policy
- created unit facts: new `Unit` UUID, entity external id, unit name, days-to-arrival, mothballed flag, salvage flag, quality, repairable flag, transport capability summary, and whether transport capacity was registered immediately
- report counts for `ACQUISITIONS`, `FINANCES`, and optional `GENERAL` audit report
- offer removal facts: before/after presence of the exact fingerprint and market offer count
- prompt facts and unsupported/warning entries

For a DropShip purchase, verification should specifically include the created unit id, `UnitType.DROPSHIP`, days-to-arrival, mothball state, and a transport capability summary. The command should not auto-assign cargo or crew in V1.

## Readiness Changes

`GET /campaign/commands` should keep `markets.unit_offers.purchase` blocked until implementation exposes candidate selectors. After selector support lands:

- report command status `available` when at least one selectable non-black-market offer exists
- report command status `blocked` with `no_selectable_unit_offers` when the market exists but every offer is duplicate, black-market, unloadable, or unaffordable
- include selector rows with `selectable=false` and refusal reasons so MEK-RPG can explain why a visible market row cannot be purchased
- keep `safe_selectors_available=false` if the source implementation cannot generate and verify opaque selectors

The read-only `markets` section should remain display-only. Command selectors belong in `GET /campaign/commands`, not in ordinary campaign-state market rows, because they are scoped to command readiness and state revision.

## Follow-Up

Recommended implementation issue: add live-session unit-market offer selectors and implement guarded non-black-market single-offer purchase.

Implementation should be disposable-campaign testable with at least:

1. Dry-run of one unique non-black-market DropShip offer.
2. Apply of one unique non-black-market DropShip offer with `saveAfterSuccess=false`.
3. Refusal for duplicate exact offer fingerprints.
4. Refusal for stale price or balance.
5. Refusal for insufficient funds.
6. Refusal for black-market offer.
7. Verification that non-instant delivery creates a unit with days-to-arrival and removes exactly one offer.

`Unknown`: live disposable-campaign smoke testing has not run. It requires a source-built MekHQ instance launched with `mekhq.controlApi.enabled=true` and a copied campaign with representative unit-market offers.
