# MEK-RPG Live MekHQ API Implementation Plan

Status: ready for agent issue, created 2026-06-22 after MEK-RPG response memo.

Purpose: turn the proven local MekHQ control API prototype into a read-only live campaign-state API that MEK-RPG can consume as a freshness layer over the existing save/checkpoint import workflow.

## Decision

`Confirmed from MEK-RPG docs`: no additional dialogue is required before a first implementation issue. MEK-RPG's response memo accepts the live localhost API direction with these constraints:

- live API data is read-only live context by default
- durable MEK-RPG campaign state still comes from save/import confirmation, explicit user approval, or a later controlled "record live checkpoint" flow
- the existing save/checkpoint path remains the offline fallback, audit trail, regression fixture source, and durability boundary
- keep the checkpoint top-level grouping for the full state endpoint
- preserve trust envelopes for campaign-facing, action-adjacent, and hard-ledger fields
- keep markets display-only in the first version
- omit write/action surfaces from this read-only API issue

## V1 Scope

Implement two local-only endpoints inside the existing `mekhq.service.LocalControlService` source prototype:

```http
GET /campaign/summary
GET /campaign/state?sections=...
```

The API remains:

- disabled by default behind `-Dmekhq.controlApi.enabled=true`
- bound to `127.0.0.1`
- in-process with the open MekHQ GUI app
- read-only for these new endpoints
- guarded against no-campaign-loaded and wrong-process assumptions

## Endpoint: GET /campaign/summary

Minimum useful fields:

- `status`
- `campaignId`
- `campaignName`
- `campaignDate`
- `mekhqVersion`
- `apiSchemaVersion`
- `apiMode`: should state local read-only/live context
- `readOnly`: true
- `currentSystem`
- `currentLocation`
- `dirtyState` or `unsavedChanges`: include only if source-confirmed; otherwise return `Unknown` plus a warning
- `stateRevision` or `snapshotId`
- top-level `warnings`
- top-level `unsupported`

`stateRevision` can be a live snapshot identifier rather than a saved checkpoint id. If no source-owned campaign revision exists, use an API-generated snapshot id based on wall-clock export time plus campaign id/date, and label it clearly as a live snapshot id rather than a MekHQ save revision.

## Endpoint: GET /campaign/state

Preserve the checkpoint export top-level grouping:

```text
bridge_metadata, campaign, finances, personnel, units, contracts, scenarios,
repairs_and_logistics, markets, reports, unsupported
```

Support a `sections` query parameter that lets callers request a subset. Unknown section names should return a clear `400` response with supported section names.

First useful sections:

- `campaign`: id, name, date, faction, system/location/travel state
- `finances`: method-backed current balance, debt/loan flags, recent finance warning/report summary
- `personnel`: ids, display names, ranks, roles, assignment, status, fatigue/injury/hits, availability
- `units`: ids, display names, chassis/model/type/weight, status, crew links, tech links, condition/repair summary
- `contracts`: active contract ids/names/status, employer/enemy, system, dates, terms summary
- `scenarios`: ids, linked contract/mission id, status, date, report/objective summary
- `repairs_and_logistics`: repair pressure, parts pressure, shopping/acquisition pressure, cargo/transport warnings
- `reports`: sanitized classified report buckets plus compact summaries where practical
- `markets`: optional display-only opportunity context only; no action selectors unless source-confirmed stable ids already exist
- `unsupported`: structured unsupported/blocker entries

If a section is too large for a safe first source pass, it may return a structured unsupported entry or warning-heavy partial payload. Do not silently invent values.

## Trust Envelope

Use a trust envelope for any field that may become campaign-facing, action-adjacent, or a hard ledger fact:

```json
{
  "value": "...",
  "evidence": "Confirmed from MekHQ API",
  "method_backed": true,
  "source_owner": "Campaign#getLocalDate()",
  "warnings": []
}
```

Trivial API metadata can be plain values. Campaign date, finances, personnel status, unit condition, contract terms, scenarios, repairs/logistics, markets, and reports should keep provenance.

## Explicit Non-Goals

Do not implement in this issue:

- market purchases
- personnel hiring
- contract accept/decline
- repair execution or assignment
- tactical result application
- save/writeback commands
- direct `.cpnx`, `.cpnx.gz`, or XML mutation
- durable MEK-RPG state recording

## Fixture And Verification Targets

Produce sanitized fixtures or fixture snippets for:

- `GET /campaign/summary`
- `GET /campaign/state?sections=campaign,finances,personnel,units,contracts,scenarios,repairs_and_logistics,reports,unsupported`
- dirty/unsaved state when source-confirmed or an explicit `Unknown` dirty-state warning if not available
- sparse/warning-heavy output

Verification should include:

- `.\gradlew.bat :MekHQ:compileJava`
- `.\gradlew.bat :MekHQ:checkstyleMain`
- targeted unit tests if a DTO builder can be isolated without GUI startup
- a local API smoke command against a running source-built MekHQ app, if the user is present for campaign loading

If live UI smoke is not performed in the implementation issue, record the exact manual command sequence and blocker.

## Source Areas

Likely starting points:

- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/MekHQ.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/Campaign.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/personnel/Person.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/unit/Unit.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/finances/Finances.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/mission/Mission.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/mission/Contract.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/mission/Scenario.java`

Prefer small DTO builder methods over returning raw internal objects. The response shape should be stable enough for MEK-RPG tests and explicit enough about unsupported or partial fields.

## Open Implementation Questions

- Is there a source-confirmed dirty/unsaved campaign indicator in the running GUI app?
- Is there an existing campaign revision counter, save timestamp, or modified flag that can become `stateRevision`, or should V1 generate a live `snapshotId`?
- Which report buckets are easy to access and sanitize from the live `Campaign` object?
- Which repair/logistics summaries can be method-backed without pulling in excessive UI/table model code?
- Should V1 include markets at all, or return a display-only partial section with automation-blocking unsupported entries?

These are implementation questions, not blockers to creating the issue.
