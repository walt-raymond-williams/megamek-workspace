# MEK-RPG Live MekHQ Activity History Source Audit

## Purpose

This note audits MekHQ history-like campaign data sources for epic `#56` and child issue `#57`. It classifies which sources should feed a richer read-only local activity-history API, which should remain in existing state sections, and which are application/debug artifacts rather than campaign ledger history.

## Summary Recommendation

`Confirmed from source`: the safest API sequence is:

1. Design a bounded dedicated activity-history shape in issue `#58` before implementation.
2. Treat current daily report categories as the existing dashboard/report section, not durable multi-day history.
3. Treat `Campaign.inMemoryLogHistory` historical daily logs as optional live-session recent history only. They are useful when enabled, but source search found no save serialization for that list.
4. Treat `Person` log families as durable per-person history, but require explicit person/target filters and explicit medical/patient opt-in rules.
5. Treat finance transactions and scenario reports as durable domain histories that can be included in a unified timeline only after the design defines filters, limits, and duplicate handling.
6. Keep application/debug files under `logs/` and `MMLogger` output out of the campaign activity API.

## Source Inventory

| Source | Source owner | Save behavior | UI consumer | Date semantics | Text behavior | Risk | API classification |
| --- | --- | --- | --- | --- | --- | --- | --- |
| Current daily report categories | `Campaign#getCurrentReport()` and sibling getters; exported by `LocalCampaignStateExporter#reportsSection()` | Current reports are written through `Campaign#writeToXML(...)` as part of campaign save behavior already documented in the live API workstream. | Command Center daily report panels and current local API `reports` section. | Exporter stamps current campaign date on each current report entry. | Existing exporter strips HTML tags and truncates long lines. | Medium payload risk; low privacy beyond existing report section. | Keep in `GET /campaign/state?sections=reports`; do not present as durable multi-day history. |
| Historical daily report memory | `Campaign.inMemoryLogHistory`, `Campaign#beginReport(...)`, `Campaign#addReport(...)`, `HistoricalLogEntry` | Source search found no serialization of `inMemoryLogHistory`; entries are live memory only. | `HistoricalDailyReportDialog` reads `gui.getCampaign().inMemoryLogHistory`. | `HistoricalLogEntry` date is `Campaign#getLocalDate()` at report creation; dialog filters by days between entry date and current campaign date. | Raw report text, commonly HTML-ish report strings; dialog appends it into a daily report panel. | Medium payload risk; medium confusion risk because it is optional and not save-backed. | Expose only as optional live-session recent history with clear `source_persistence=memory_only`, or unsupported if design rejects memory-only data. |
| Per-person personal/service/award/custom logs | `Person#personnelLog` accessed through `getPersonalLog()`; `PersonalLogger`, `ServiceLogger`, `AwardLogger`, custom UI log entry paths. | Serialized under `<personnelLog>` with `LogEntry#writeToXML(...)`; legacy getter `getPersonnelLog()` delegates to `getPersonalLog()`. | `PersonViewPanel`; `EditLogControl`; personnel-table add/edit menu paths. | Each `LogEntry` has its own `LocalDate`; getters sort by date ascending. | `LogEntry` stores date, desc CDATA, and type. Text can contain names and narrative details. | Medium privacy risk; potentially high payload on long campaigns. | Good dedicated endpoint candidate; require person filter or bounded roster-wide defaults. |
| Per-person assignment logs | `Person#assignmentLog`; `AssignmentLogger`; legacy migration from older personal-log text patterns. | Serialized under `<assignmentLog>`; older `<personnelLog>` entries with assignment-like text are retyped and moved during load. | `PersonViewPanel`; `EditLogControl`. | Per-entry `LocalDate`; getter sorts ascending. | `LogEntry` CDATA desc and `ASSIGNMENT` type. | Low to medium privacy; useful campaign timeline data. | Good candidate for per-person history with target/date/type filters. |
| Per-person performance logs | `Person#performanceLog`; `PerformanceLogger`; legacy migration from older personal-log text patterns. | Serialized under `<performanceLog>`; some older performance entries are retyped from `<personnelLog>` and some patient-like entries from `<performanceLog>` are moved to patient log during load. | `PersonViewPanel`; `EditLogControl`. | Per-entry `LocalDate`; getter sorts ascending. | `LogEntry` CDATA desc and `PERFORMANCE` type, except migrated patient entries. | Medium privacy; can reveal XP, treatment work, and character development. | Candidate for per-person history with explicit family/type filters. |
| Per-person medical logs | `Person#medicalLog`; `MedicalLogger`; medical controllers and injury systems. | Serialized under `<medicalLog>`; older medical entries inside `<personnelLog>` are moved to medical log during load. | `PersonViewPanel`; `MedicalViewDialog`; `EditLogControl`; medical UI. | Per-entry `LocalDate`; getter sorts ascending. | Injury/treatment narrative in CDATA desc; may include medical conditions, surgeries, pregnancy, death/infirmary events. | High privacy and sensitivity; payload can be meaningful. | Dedicated endpoint only; require explicit person target and explicit `include=medical`-style opt-in. |
| Per-person patient logs | `Person#patientLog`; `PatientLogger`; legacy migration from older performance/personal text patterns. | Serialized under `<patientLog>`; some older entries are moved from `<personnelLog>` or `<performanceLog>` during load. | `PersonViewPanel`; `EditLogControl`. | Per-entry `LocalDate`; getter sorts ascending. | Doctor/patient treatment narrative in CDATA desc. | High privacy and cross-person sensitivity. | Dedicated endpoint only; require explicit person target and explicit `include=patient`-style opt-in. |
| Per-person scenario logs | `Person#scenarioLog`; `ServiceLogger#participatedInScenarioDuringMission(...)`; scenario resolution. | Serialized under `<scenarioLog>`. | `PersonViewPanel`; `EditScenarioLogControl`; scenario awards code. | Per-entry `LocalDate`; getter sorts ascending, though `PersonViewPanel` reverses for display in one path. | Scenario participation/capture text in CDATA desc. | Medium privacy; strong timeline value. | Good candidate for per-person history and optional unified activity feed. |
| Finance transactions | `Finances#getTransactions()`; `Transaction`; `Finances#debit(...)`; `Finances#credit(...)`. | Serialized under `<finances><transactions>`; each transaction writes type, date, amount, description. | Finance CSV export; existing local API exports recent five transactions. | Each transaction has explicit `LocalDate`. | Description is plain string from source paths; existing exporter sanitizes description. | Medium financial sensitivity; potentially large payload. | Keep finance section for summary/recent rows; activity API can include bounded transaction rows with type/date filters. |
| Scenario reports | `Scenario#getReport()` and `Scenario#setReport(...)`; `ResolveScenarioTracker` sets report/status/date at resolution. | Serialized as `<report>` inside each scenario; scenario also writes status and date. | `ScenarioViewPanel`; `AtBScenarioViewPanel`; existing local API scenario DTO. | Scenario date is explicit; `ResolveScenarioTracker` sets date to current campaign date on resolution. | Report is rendered as Markdown/HTML in UI; existing exporter sanitizes. | Medium payload; can duplicate battle daily reports. | Keep in scenarios section by default; activity API may include scenario-report entries if design defines duplicate handling. |
| Maintenance/logistics report activity | `Maintenance` and related services write `TECHNICAL` and `ACQUISITIONS` daily reports; `Unit#lastMaintenanceReport` stores a latest per-unit snapshot. | Daily report lines follow report behavior; `Unit#lastMaintenanceReport` is serialized as CDATA only when maintenance checking is enabled and the field is non-empty. | Command Center technical/acquisitions report panels; existing local API unit/logistics rows. | Daily report date is current campaign date; `lastMaintenanceReport` has no independent date field. | HTML-ish maintenance text; existing exporter sanitizes current snapshot. | Medium payload; low standalone historical reliability for `lastMaintenanceReport`. | Treat daily report lines as report history; keep `lastMaintenanceReport` as current logistics context, not timeline history. |
| Market/procurement state | `ShoppingList`, unit/personnel/contract markets, acquisitions reports. | Shopping list and markets serialize current offers/orders; acquisitions reports record some events separately. | Market dialogs, shopping list UI, current local API `markets` and logistics sections. | Market rows usually have state-specific dates/days-to-wait, not a unified event date. | Display strings and descriptions are source-owned but often selector-adjacent. | High confusion risk with command selectors and current offers. | Do not include current market rows in activity history by default. Include only acquisitions report lines or finance transactions unless design creates a separate procurement-history source. |
| Application/debug logs and generated battle artifacts | `MMLogger`, install `logs/`, generated `salvage.mul`, `entitystatus.txt`, `game_actions_*.tsv`. | Files outside campaign save; not campaign ledger serialization. | Debugging, MegaMek battle artifacts, prior tabletop workflow investigation. | File timestamps or game telemetry, not campaign ledger dates. | Raw debug/telemetry; may include paths/errors. | High confusion and privacy/path risk. | Explicitly unsupported for local campaign activity-history API. Keep only in troubleshooting workflows. |

## Daily Reports Versus Historical Daily Logs

`Confirmed from source`: `LocalCampaignStateExporter#reportsSection()` exports current report categories from `Campaign#getCurrentReport()`, `getSkillReport()`, `getTechnicalReport()`, `getFinancesReport()`, `getAcquisitionsReport()`, `getMedicalReport()`, `getPersonnelReport()`, `getBattleReport()`, `getPoliticsReport()`, and `getAggregateReport()`. It includes metadata that reports are sanitized by stripping HTML and truncating long lines.

`Confirmed from source`: `Campaign#addReport(DailyReportType, String)` ignores blank strings, optionally appends a `HistoricalLogEntry` to `inMemoryLogHistory` when `MekHQ.getMHQOptions().getHistoricalDailyLog()` is true, then adds the line to the selected current daily report category. Unified daily report options can redirect report type to `GENERAL`, and aggregate daily report options can duplicate non-aggregate lines into `AGGREGATE`.

`Confirmed from source`: `Campaign#beginReport(...)` adds a blank `HistoricalLogEntry` for the current date when historical daily logs are enabled, then begins each current daily report category.

`Confirmed from source`: `Campaign#addInMemoryLogHistory(...)` prunes entries older than `MHQConstants.MAX_HISTORICAL_LOG_DAYS` by comparing entry date to the new entry date, then appends the new entry. `MHQConstants.MAX_HISTORICAL_LOG_DAYS` is `120`.

`Confirmed from source`: `HistoricalDailyReportDialog` only shows the historical UI when `MekHQ.getMHQOptions().getHistoricalDailyLog()` is true. It offers day windows of `7`, `30`, `60`, `90`, and `MAX_HISTORICAL_LOG_DAYS`, reads `gui.getCampaign().inMemoryLogHistory`, groups output by log date, and appends each raw description plus `<br>` to a daily report panel.

`Confirmed from source search`: the only MekHQ source references found for `inMemoryLogHistory` are its field declaration, the append/prune path, and `HistoricalDailyReportDialog`. No save/write/read references were found. Treat this as memory-only unless a later audit finds non-Java persistence or a missing branch.

## Person Log Families

`Confirmed from source`: `Person` has six serialized log lists: `personnelLog`, `medicalLog`, `patientLog`, `scenarioLog`, `assignmentLog`, and `performanceLog`. The public `getPersonnelLog()` method is deprecated and delegates to `getPersonalLog()`.

`Confirmed from source`: `LogEntry` persists `date`, `desc`, and optional `type` fields. `LogEntryFactory` recreates typed entries from XML and defaults missing type to `PERSONAL`.

`Confirmed from source`: `LogEntryType` covers `ASSIGNMENT`, `AWARD`, `CUSTOM`, `HISTORICAL`, `MEDICAL`, `PATIENT`, `PERFORMANCE`, `PERSONAL`, and `SERVICE`.

`Confirmed from source`: `Person` load behavior includes compatibility migration. Older `<personnelLog>` entries are inspected by description and can be moved into assignment, performance, patient, or medical logs. Older `<performanceLog>` entries containing "Successfully treated" are moved to patient log.

`Confirmed from source`: `PersonViewPanel` shows personal, performance, medical, patient, assignment, and scenario logs. `EditLogControl` supports editing personal, medical, patient, assignment, and performance logs. `EditScenarioLogControl` handles scenario logs.

`Confirmed from source`: log creator classes record meaningful gameplay and life events. `ServiceLogger` writes joining, prisoner/bondsman, status, education, scenario participation, and departure events. `PersonalLogger` writes family/marriage/death-related events. `MedicalLogger` writes injury, surgery, healing, disease, pregnancy, infirmary, and treatment events. `AwardLogger` writes award and award-removal events.

## Finance Transactions

`Confirmed from source`: `Transaction` owns `TransactionType`, `LocalDate`, `Money amount`, and `description`. XML writes all four fields and XML load parses all four fields.

`Confirmed from source`: `Finances#getTransactions()` returns the ledger list; `Finances#debit(...)` and `Finances#credit(...)` append negative and positive `Transaction` rows and trigger transaction events.

`Confirmed from source`: `Finances#writeToXML(...)` serializes transactions under `<transactions>` and `Finances#generateInstanceFromXML(...)` loads them.

`Confirmed from source`: `LocalCampaignStateExporter#recentTransactions(...)` already exports the last five transactions with date, amount, type, sanitized description, and source owner. A history API should reuse the same source owner and sanitization posture but add date/type/limit filters.

## Scenario Reports

`Confirmed from source`: `Scenario` owns `name`, `desc`, `report`, `status`, `date`, `id`, and `missionId`. XML writes `report`, `status`, and `date`; XML load reads report from `<report>`.

`Confirmed from source`: `ResolveScenarioTracker` sets scenario status, report, and date during scenario resolution.

`Confirmed from source`: `ScenarioViewPanel` displays scenario reports when present and renders `scenario.getReport()` as HTML through the Markdown renderer. `LocalCampaignStateExporter#scenarioDto(...)` already exports sanitized scenario report text and `has_report`.

`Inferred`: scenario reports should remain scenario-domain data by default. A unified activity feed could include them as `scenario.report` entries, but issue `#58` should decide how to avoid duplicate timeline rows with daily battle reports and person scenario logs.

## Maintenance, Logistics, And Markets

`Confirmed from source`: maintenance and repair-like workflows primarily add `TECHNICAL` daily report entries. `Maintenance` writes immediate maintenance, unable-to-maintain, maintenance cost, part damage, and MRMS-style technical reports through `Campaign#addReport(...)`.

`Confirmed from source`: `Unit#lastMaintenanceReport` is a current per-unit snapshot field. It is serialized as `<lastMaintenanceReport>` only when non-empty and campaign options check maintenance; it is read back during unit XML load. It has no independent date field.

`Confirmed from source`: acquisition/procurement workflows write many `ACQUISITIONS` report lines, and shopping-list/current-market state is separately serialized as current state. `LocalCampaignStateExporter` already marks market rows display-only and warns consumers not to treat row content as durable command selectors.

`Inferred`: activity history should not include current market offers, personnel applicants, or contract offers as history. Those belong in current market/readiness sections. Activity history can include acquisitions report lines, finance transactions, and later a properly audited procurement event source if one exists.

## Application And Debug Logs

`Confirmed from source and workspace docs`: MekHQ uses `MMLogger`/Log4j throughout source for application diagnostics, and this workspace has previously inspected install `logs/` battle artifacts such as `salvage.mul`, `entitystatus.txt`, and `game_actions_*.tsv`.

`Confirmed from workspace docs`: `docs/current/DATA_MAP.md` classifies `logs/` as runtime logs, and `GENERATED_BATTLE_RECORD_MUL_STUDY.md` treats `logs/salvage.mul` and related files as MegaMek battle artifacts, not campaign-save ledger data.

`Recommendation`: the activity-history API should explicitly return unsupported metadata for application/debug logs if asked. They are outside the loaded campaign state, may include local paths or debug data, and should stay in troubleshooting/tabletop-result workflows.

## Implications For Issue #58

Issue `#58` should design:

- A dedicated `GET /campaign/history` endpoint, or a `history` state section with strict defaults, before implementation.
- Default bounded output. Do not dump all person logs or all finance rows.
- Filters for date range, limit, categories/families, person ids, scenario ids, transaction types, and whether medical/patient logs are included.
- A source/persistence metadata field, especially for memory-only historical daily logs.
- Sanitization consistent with `LocalCampaignStateExporter#sanitize(...)`, plus truncation rules for long entries.
- Explicit unsupported entries for application/debug logs and current market rows.
- Duplicate handling across daily reports, scenario reports, finance transactions, and person logs.

## Next Work

Recommended next issue: `#58`, "Design read-only MekHQ activity-history API."

Why: the source owners are now mapped well enough to design endpoint shape, privacy defaults, and filter/window behavior before any source implementation.
