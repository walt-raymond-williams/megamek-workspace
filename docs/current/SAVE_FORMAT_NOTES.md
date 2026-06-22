# MekHQ Save Format Notes

This file tracks confirmed and suspected facts about MekHQ campaign save files. Use evidence labels from `DOCUMENTATION_WORKFLOW.md`.

## Confirmed Facts

- `Confirmed from save`: The bundled sample `C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\campaigns\The Learning Ropes.cpnx.gz` is gzip-compressed XML.
- `Confirmed from save`: The bundled sample root element is `campaign`.
- `Confirmed from save`: The bundled sample reports `version="0.51.00"`.
- `Confirmed from save`: The early XML includes campaign identity, campaign date, faction, reputation, and transportation capacity/requirements.
- `Confirmed from save`: The demo fixture `campaigns/demo/ai-ready-demo.cpnx.gz` is also gzip-compressed XML with root `campaign` and `version="0.51.00"`.
- `Confirmed from save`: Current campaign identity is stored under `/campaign/info`; in the demo save this includes id `ea0d334a-1582-459a-9084-b349f0baca5a`, date `3025-04-08`, name `The Learning Ropes`, and faction `MERC`.
- `Confirmed from source`: MekHQ accepts campaign files with `cpnx`, `cpnx.gz`, or `xml` extensions through `FileType.CPNX`.
- `Confirmed from source`: `CampaignFactory#createCampaign(InputStream)` detects gzip by reading the first two bytes for the gzip magic header `0x1f 0x8b`; gzip streams are wrapped in `GZIPInputStream`, otherwise the stream is treated as plain XML.
- `Confirmed from source`: `CampaignXmlParser#parse()` creates a fresh `Campaign`, parses the XML document, reads the root `campaign` version, runs milestone upgrade-path handling, then processes top-level campaign child nodes in multiple passes.
- `Confirmed from source`: Manual campaign save uses `CampaignGUI.saveCampaign(...)`. If the selected path lacks `.cpnx` or `.cpnx.gz`, MekHQ appends `.cpnx`; paths ending in `.gz` are written through `GZIPOutputStream`, otherwise written as plain UTF-8 XML.
- `Confirmed from source`: The save dialog's default extension follows the `preferGzippedCampaignFile` option, choosing `.cpnx.gz` or `.cpnx`; autosaves are always written as gzip-compressed `Autosave-...cpnx.gz` files.

## Safe Inspection Workflow

1. Identify the exact save path and version.
2. Do not overwrite or edit the original save.
3. Stream-read the gzip or copy it to `analysis/tmp/`.
4. Inspect XML structure before interpreting field meanings.
5. Prefer structured XML parsing once the relevant sections are known.
6. Compare extracted summaries against MekHQ UI or user-confirmed facts when possible.

## Major XML Areas

- `Confirmed from source`: `Campaign#writeToXML(...)` writes reports, options, units, personnel, missions/scenarios, formations, finances, locations, bases, location node, shopping list, kills, skill types, special abilities, random skill preferences, parts, personnel market, unit market, contract market, and combat teams where applicable.
- `Confirmed from source`: `Campaign#writeToXML(...)` writes the XML declaration and opens the root as `<campaign version="...">`, then writes `/campaign/info`, campaign/game options, planetary overrides, units, human resources, missions, formations, finances, current location and locations, player bases, shopping list, kills, skill configuration, parts, markets, and StratCon/combat-team state where applicable.
- `Confirmed from source`: several exported checkpoint values should be method-backed instead of trusted from raw XML alone. Examples include `Finances#getBalance()`, `UnitMarketOffer#getPrice()`, `Unit#getDamageState()`, `Person#getSalary(Campaign)`, and cargo/bay summaries through `CargoStatistics` and `HangarStatistics`.
- `Inferred`: Raw XML parsing is acceptable for read-only fallback inspection, but MEK-RPG should treat exact funds, market prices, damage state, salary, transport pressure, and report-alert classification as source-backed checkpoint fields once a MekHQ-backed exporter exists.

## Field Meanings

Record field meanings here as they are confirmed.

| Field / Area | Meaning | Evidence | Source / Save |
| --- | --- | --- | --- |
| `campaign` root | Top-level MekHQ campaign save element | Confirmed from save | Bundled sample `The Learning Ropes.cpnx.gz` |
| `version="0.51.00"` | Save version reported by bundled sample | Confirmed from save | Bundled sample `The Learning Ropes.cpnx.gz` |
| `/campaign/info` | Campaign identity area containing id, calendar date, name, faction, reputation, and transportation/admin requirements | Confirmed from save | `campaigns/demo/ai-ready-demo.cpnx.gz` |
| `/campaign/humanResources/personnel/person` | Personnel roster entries; demo save has `106` personnel | Confirmed from save | `campaigns/demo/ai-ready-demo.cpnx.gz` |
| `/campaign/units/unit/entity` | Unit identity is carried in entity attributes such as `chassis`, `model`, `type`, and `externalId`; demo save has `25` units | Confirmed from save | `campaigns/demo/ai-ready-demo.cpnx.gz` |
| `/campaign/contractMarket` | Contract-market offers can appear even when `/campaign/missions` is empty; demo save has one offer, `3025 - FWL - Castrovia Objective Raid` | Confirmed from save | `campaigns/demo/ai-ready-demo.cpnx.gz` |
| major campaign sections | Reports, units, personnel, missions/scenarios, finances, locations, shopping list, parts, personnel market, unit market, contract market, and related campaign state are serialized by `Campaign#writeToXML(...)` | Confirmed from source | `external/src/mekhq/MekHQ/src/mekhq/campaign/Campaign.java` |
| `.cpnx.gz` loading | Loader sniffs gzip magic bytes and decompresses only when the stream begins with `0x1f 0x8b`; otherwise it parses the stream as XML | Confirmed from source | `external/src/mekhq/MekHQ/src/mekhq/campaign/CampaignFactory.java` |
| campaign XML parsing | `CampaignXmlParser#parse()` owns DOM parsing, version reading, upgrade-path handling, null-entity checks, and top-level campaign node processing | Confirmed from source | `external/src/mekhq/MekHQ/src/mekhq/campaign/io/CampaignXmlParser.java` |
| manual campaign saving | `CampaignGUI.saveCampaign(...)` writes through `GZIPOutputStream` only for paths ending in `.gz`; otherwise it writes plain UTF-8 XML, with `.cpnx` appended if no campaign extension is present | Confirmed from source | `external/src/mekhq/MekHQ/src/mekhq/gui/CampaignGUI.java` |
| autosaves | `AutosaveService` writes autosaves through `GZIPOutputStream` and names them `Autosave-<n>-<campaign>-<date>.cpnx.gz` | Confirmed from source | `external/src/mekhq/MekHQ/src/mekhq/service/AutosaveService.java` |
| derived checkpoint values | Balance, market price, unit damage state, salary, cargo/bay summaries, and report-alert classification need MekHQ methods or validation before consumer-facing export | Confirmed from source / Inferred | `MEK_RPG_MEKHQ_CHECKPOINT_EXPORT.md` |

## Source References

- `C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq\MekHQ\src\mekhq\campaign\CampaignFactory.java`: campaign load entry point for stream handling and gzip detection.
- `C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq\MekHQ\src\mekhq\campaign\io\CampaignXmlParser.java`: XML-to-`Campaign` parser.
- `C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq\MekHQ\src\mekhq\campaign\Campaign.java`: campaign XML writer and major serialized sections.
- `C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq\MekHQ\src\mekhq\gui\CampaignGUI.java`: manual campaign save implementation.
- `C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq\MekHQ\src\mekhq\gui\FileDialogs.java`: campaign open/save file dialogs and default extension choice.
- `C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq\MekHQ\src\mekhq\io\FileType.java`: accepted campaign file extensions.
- `C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq\MekHQ\src\mekhq\service\AutosaveService.java`: gzip autosave writer and autosave naming.
- `C:\Users\waltr\Documents\megamek-workspace\external\src\megamek`: unit loaders, scenario loading, tactical rules, and MegaMek game behavior.

## Open Questions

- What are the major XML sections in the user's active campaign save?
- Which fields are safe to parse as stable identifiers, and which are display or transient values?
- Which representative disposable save should validate the checkpoint JSON contract against MekHQ UI and the MEK-RPG raw XML helper?
