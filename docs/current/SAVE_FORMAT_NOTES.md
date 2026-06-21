# MekHQ Save Format Notes

This file tracks confirmed and suspected facts about MekHQ campaign save files. Use evidence labels from `DOCUMENTATION_WORKFLOW.md`.

## Confirmed Facts

- `Confirmed from save`: The bundled sample `C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\campaigns\The Learning Ropes.cpnx.gz` is gzip-compressed XML.
- `Confirmed from save`: The bundled sample root element is `campaign`.
- `Confirmed from save`: The bundled sample reports `version="0.51.00"`.
- `Confirmed from save`: The early XML includes campaign identity, campaign date, faction, reputation, and transportation capacity/requirements.

## Safe Inspection Workflow

1. Identify the exact save path and version.
2. Do not overwrite or edit the original save.
3. Stream-read the gzip or copy it to `analysis/tmp/`.
4. Inspect XML structure before interpreting field meanings.
5. Prefer structured XML parsing once the relevant sections are known.
6. Compare extracted summaries against MekHQ UI or user-confirmed facts when possible.

## Major XML Areas

- `Confirmed from source`: `Campaign#writeToXML(...)` writes reports, options, units, personnel, missions/scenarios, formations, finances, locations, bases, location node, shopping list, kills, skill types, special abilities, random skill preferences, parts, personnel market, unit market, contract market, and combat teams where applicable.
- `Confirmed from source`: several exported checkpoint values should be method-backed instead of trusted from raw XML alone. Examples include `Finances#getBalance()`, `UnitMarketOffer#getPrice()`, `Unit#getDamageState()`, `Person#getSalary(Campaign)`, and cargo/bay summaries through `CargoStatistics` and `HangarStatistics`.
- `Inferred`: Raw XML parsing is acceptable for read-only fallback inspection, but MEK-RPG should treat exact funds, market prices, damage state, salary, transport pressure, and report-alert classification as source-backed checkpoint fields once a MekHQ-backed exporter exists.

## Field Meanings

Record field meanings here as they are confirmed.

| Field / Area | Meaning | Evidence | Source / Save |
| --- | --- | --- | --- |
| `campaign` root | Top-level MekHQ campaign save element | Confirmed from save | Bundled sample `The Learning Ropes.cpnx.gz` |
| `version="0.51.00"` | Save version reported by bundled sample | Confirmed from save | Bundled sample `The Learning Ropes.cpnx.gz` |
| major campaign sections | Reports, units, personnel, missions/scenarios, finances, locations, shopping list, parts, personnel market, unit market, contract market, and related campaign state are serialized by `Campaign#writeToXML(...)` | Confirmed from source | `external/src/mekhq/MekHQ/src/mekhq/campaign/Campaign.java` |
| derived checkpoint values | Balance, market price, unit damage state, salary, cargo/bay summaries, and report-alert classification need MekHQ methods or validation before consumer-facing export | Confirmed from source / Inferred | `MEK_RPG_MEKHQ_CHECKPOINT_EXPORT.md` |

## Source References

Known source areas to map:

- `C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq`: campaign loading, saving, contracts, repairs, personnel, markets, and campaign UI behavior.
- `C:\Users\waltr\Documents\megamek-workspace\external\src\megamek`: unit loaders, scenario loading, tactical rules, and MegaMek game behavior.

Add specific class and file references here when confirmed.

## Open Questions

- What are the major XML sections in the user's active campaign save?
- Where are active contracts, scenarios, unit damage, personnel fatigue, finances, and campaign options represented?
- Which fields are safe to parse as stable identifiers, and which are display or transient values?
- Which representative disposable save should validate the checkpoint JSON contract against MekHQ UI and the MEK-RPG raw XML helper?
