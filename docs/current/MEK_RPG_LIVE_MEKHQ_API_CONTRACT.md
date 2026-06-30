# MEK-RPG Live MekHQ API Contract

Status: consumer-facing contract for the local MekHQ control API used by MEK-RPG.

Purpose: give MEK-RPG one stable starting point for reading live MekHQ campaign state and requesting guarded MekHQ-owned campaign commands. This document summarizes the implemented local API surface and links to fixtures for concrete JSON shapes.

## Scope

`Confirmed from source`: this API is the disabled-by-default local control API in `external/src/mekhq`, implemented primarily in:

- `MekHQ/src/mekhq/service/LocalControlService.java`
- `MekHQ/src/mekhq/service/LocalCampaignStateExporter.java`
- `MekHQ/src/mekhq/service/LocalCommandReadinessExporter.java`

The API runs inside the already-open MekHQ GUI process. MekHQ remains the campaign authority; MEK-RPG is a client.

## Base URL And Enablement

Default base URL:

```text
http://127.0.0.1:32180
```

`Confirmed from source`: the API starts only when MekHQ is launched with:

```text
-Dmekhq.controlApi.enabled=true
```

Optional port override:

```text
-Dmekhq.controlApi.port=<port>
```

Local workspace launch helper:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File tools\start-mekhq-control-api.ps1
```

## Safety Contract

- `Confirmed from source`: the API binds to loopback and is intended for local MEK-RPG/MekHQ coordination, not remote service exposure.
- `Confirmed from source`: read endpoints do not save or mutate the loaded campaign.
- `Decision`: mutating endpoints must route through MekHQ-owned logic, not direct save-file or XML edits.
- `Decision`: MEK-RPG must not call mutating endpoints against a campaign the user cares about unless the user has intentionally loaded the correct campaign and the request guards match the current campaign state.
- `Decision`: `saveAfterSuccess` is opt-in. A command that does not explicitly request saving must not save the campaign.
- `Decision`: MEK-RPG should run dry-runs before high-value mutations such as purchases or contract acceptance.

## Endpoint Summary

| Method | Path | Purpose | Mutation |
| --- | --- | --- | --- |
| `GET` | `/status` | Control API health and loaded-campaign identity snapshot. | No |
| `POST` | `/advance-day` | Legacy guarded one-day advance prototype. | Yes |
| `GET` | `/campaign/summary` | Fast top-level live campaign summary. | No |
| `GET` | `/campaign/state` | Sectioned live campaign state payload. | No |
| `GET` | `/campaign/personnel/detail` | Explicit selected-person/character-sheet detail by person UUID. | No |
| `GET` | `/campaign/pending-deployments` | Purpose-built current scenario/deployment commitment lookup. | No |
| `GET` | `/campaign/commands` | Command readiness, selectors, state revision, and blockers. | No |
| `POST` | `/campaign/command/status-note` | Append a guarded `GENERAL` campaign report note. | Yes |
| `POST` | `/campaign/command/personnel/status` | Apply conservative RPG-side personnel status changes. | Yes |
| `POST` | `/campaign/command/personnel/fatigue` | Apply guarded personnel fatigue deltas. | Yes |
| `POST` | `/campaign/command/markets/unit-offers/purchase` | Purchase one guarded non-black-market unit-market offer. | Yes |
| `POST` | `/campaign/command/contracts/accept` | Accept one guarded contract-market offer. | Yes |

`GET /campaign/commands` may also report blocked or not-yet-implemented command rows such as funds adjustment, personnel hire, medical treatment, prosthetic surgery, repair/procurement, and standalone save. MEK-RPG must treat those rows as discovery metadata, not callable commands, unless their row says the command is available and the endpoint exists.

## Common HTTP Behavior

- Unsupported methods return HTTP `405` with a refused control response.
- Campaign endpoints return HTTP `409` when no campaign is loaded.
- Unknown `sections` values on `/campaign/state` return HTTP `400` with `unknown_sections` and `supported_sections`.
- Successful read responses are HTTP `200`.
- Section collector failures in `/campaign/state` should still return HTTP `200` with `response_status: "partial"`, `partial_response: true`, warnings, unsupported entries, and failed collector timing.
- Command validation failures normally return structured command responses with `status` such as `refused`, `blocked`, or `failed`.

## Read Response Conventions

Read responses can include:

- `schema_name`
- `schema_version`
- `producer`
- `producer_version`
- `exported_at`
- `api_mode`
- `read_only`
- `state_revision`
- `warnings`
- `unsupported`
- `collector_timing`
- `endpoint_timing`
- `response_status`
- `partial_response`

`warnings` are consumer-visible caveats for otherwise usable data.

`unsupported` entries are explicit contract boundaries. MEK-RPG must not infer unsupported fields from neighboring display-only data.

`state_revision` is a live-read revision string suitable for guard checks in the same loaded process/state. It is not a durable campaign save identifier.

`endpoint_timing` has the endpoint path and elapsed time for the request.

`collector_timing` rows identify collector names and elapsed times. They are diagnostic, not gameplay data.

## GET /status

Purpose: confirm that the local API is running and whether a campaign is loaded.

Example:

```powershell
Invoke-RestMethod -Method Get -Uri 'http://127.0.0.1:32180/status' -TimeoutSec 5
```

Important fields:

- `status`
- `httpStatus`
- `message`
- `campaignName`
- `campaignId`
- `dateBefore`
- `dateAfter`
- `visibleDialogs`
- `saveAttempted`

Consumer rule: MEK-RPG should call `/status` before campaign reads and after any failed command to verify that the control server is still available.

## GET /campaign/summary

Purpose: fast current campaign context for frequent refreshes.

Example:

```powershell
Invoke-RestMethod -Method Get -Uri 'http://127.0.0.1:32180/campaign/summary' -TimeoutSec 15
```

Important fields:

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
- `pendingDeployments`
- `warnings`
- `unsupported`
- `collector_timing`
- `endpoint_timing`

Known limitation: `dirtyState` remains explicit `Unknown` because no source-confirmed campaign-wide unsaved flag is exposed by this API pass.

## GET /campaign/state

Purpose: sectioned campaign state for MEK-RPG context panels and deeper refreshes.

Supported sections:

```text
bridge_metadata, campaign, finances, personnel, units, contracts, scenarios,
forces, repairs_and_logistics, markets, reports, unsupported
```

Examples:

```powershell
Invoke-RestMethod -Method Get `
  -Uri 'http://127.0.0.1:32180/campaign/state?sections=bridge_metadata,campaign,contracts,scenarios,reports' `
  -TimeoutSec 45

Invoke-RestMethod -Method Get `
  -Uri 'http://127.0.0.1:32180/campaign/state?sections=bridge_metadata,units,repairs_and_logistics' `
  -TimeoutSec 45
```

If `sections` is omitted, all supported sections are returned.

MEK-RPG rule: include `bridge_metadata` when a selected-section response will be validated as live MekHQ context. Omitting `sections` also includes `bridge_metadata`.

Top-level groups:

- `bridge_metadata`
- `campaign`
- `finances`
- `personnel`
- `units`
- `forces`
- `contracts`
- `scenarios`
- `repairs_and_logistics`
- `markets`
- `reports`
- `unsupported`
- `collector_timing`
- `endpoint_timing`

Reliability contract:

- Narrow `sections=` requests should collect only requested sections.
- Failed requested sections should produce partial data instead of taking down the whole response.
- MEK-RPG should surface `response_status`, `partial_response`, warnings, and unsupported entries to the GM instead of silently treating partial data as complete.

## GET /campaign/pending-deployments

Purpose: fast current operation/deployment facts without traversing broad campaign state.

Examples:

```powershell
Invoke-RestMethod -Method Get -Uri 'http://127.0.0.1:32180/campaign/pending-deployments' -TimeoutSec 15

Invoke-RestMethod -Method Get -Uri 'http://127.0.0.1:32180/campaign/pending-deployments?personName=Moreno' -TimeoutSec 15

Invoke-RestMethod -Method Get -Uri 'http://127.0.0.1:32180/campaign/pending-deployments?personId=<person-uuid>' -TimeoutSec 15
```

Query parameters:

- `personName`: optional name fragment for viewpoint-person commitment lookup.
- `personId`: optional exact person UUID for viewpoint-person commitment lookup.

Important fields:

- `schema_name`
- `schema_version`
- `state_revision`
- `pending_scenario_count`
- `scenarios`
- `viewpoint_person`
- `warnings`
- `unsupported`
- `collector_timing`
- `endpoint_timing`

Source-backed behavior:

- Reports only current/pending scenarios.
- Uses source-confirmed scenario force/unit assignment data.
- Uses current `Unit#getCrew()` data for crew/person commitment lookup.
- If no person match or no current commitment exists, the endpoint should still return HTTP `200` with an empty commitment list.

Known limitation: the API does not expose a source-confirmed currently selected MekHQ UI person. MEK-RPG should send `personId` or `personName`.

## GET /campaign/personnel/detail

Purpose: expose Personnel tab selected-character detail data through a read-only, explicit-person endpoint.

Example:

```powershell
Invoke-RestMethod -Method Get `
  -Uri 'http://127.0.0.1:32180/campaign/personnel/detail?personId=<person-uuid>' `
  -TimeoutSec 15
```

Optional query parameters:

- `personId`: required exact person UUID.
- `includeMedical=true`: include the selected person's medical log family.
- `includePatient=true`: include the selected person's patient log family.
- `logLimit=<n>`: bounded per-family log limit; defaults to `10` and clamps to `50`.

Important fields:

- `schema_name: "mekhq-live-personnel-detail"`
- `campaign_id`, `campaign_name`, `campaign_date`
- `state_revision`
- `person.identity`
- `person.status`
- `person.assignment_context`
- `person.skills`
- `person.options_and_abilities`
- `person.awards`
- `person.injury_summary`
- `person.logs`
- `person.privacy`
- `warnings`
- `unsupported`

Source-backed behavior:

- `Confirmed from source`: the endpoint requires `personId` because the local API does not expose a reliable current Swing Personnel tab selection.
- Missing or invalid `personId` returns HTTP `400`.
- A person UUID that is not in the loaded campaign returns HTTP `404`.
- Default log output includes bounded personal, assignment, performance, and scenario log families.
- Medical and patient log families are excluded by default and require explicit `includeMedical=true` or `includePatient=true` on the same explicit person request.
- Skills expose source ids/display labels, subtype, roleplay flag, level, bonus, XP progress, natural aptitude, final value, and experience level.
- Traits/options/special abilities expose stable option ids plus display labels and values. Display labels are presentation text, not selectors.

Known limitations:

- V1 exposes award summary flags/counts, not individual award tooltip/icon/tier metadata.
- V1 does not expose full injury/treatment objects, family-tree panels, or kill-log rows.
- Broader roster-wide or cross-domain activity history remains deferred to the activity-history design workstream.

## GET /campaign/commands

Purpose: discover which commands MEK-RPG may safely offer, why commands are blocked, and which selectors/guard facts are available.

Examples:

```powershell
Invoke-RestMethod -Method Get -Uri 'http://127.0.0.1:32180/campaign/commands' -TimeoutSec 20

Invoke-RestMethod -Method Get -Uri 'http://127.0.0.1:32180/campaign/commands?selectorDetail=full' -TimeoutSec 60
```

Query parameters:

- `selectorDetail=full`: request expensive full selector guard facts.
- `includeExpensiveSelectors=true`: equivalent opt-in for expensive selectors.

Important fields:

- `schema_name`
- `schema_version`
- `status`
- `campaign`
- `state_revision`
- `selector_policy`
- `selectors`
- `command_readiness`
- `collector_timing`
- `endpoint_timing`
- `warnings`

Consumer rules:

- MEK-RPG should build action buttons from `command_readiness`, not by guessing from display-only state rows.
- A command row with `status: "available"` is still not enough by itself; the request must include the command-specific selectors and guard facts.
- A command row with `reason_code: "selector_generation_deferred"` means the default response intentionally avoided expensive guard facts. Retry with `selectorDetail=full` when the user is actually entering that command workflow.
- Unit-market offer selectors are live-session selectors scoped to the current process and state revision. MEK-RPG must not cache them across MekHQ restarts or state changes.
- Contract-market offer ids are more durable while the offer remains in the saved market, but MEK-RPG must still send expected guard facts and `expectedStateRevision`.
- Pilot assignment and TO&E rows expose read selectors and guard facts through `person_assignment_candidates`, `unit_crew_candidates`, and `forces`, but the mutating endpoints remain blocked until implementation issues `#74` and `#75`.

## Mutating Command Envelope

All modern mutating endpoints except the legacy `/advance-day` prototype use a guarded command envelope.

Common request fields:

- `command`: exact command name.
- `commandVersion`: currently `1` for implemented guarded commands.
- `idempotencyKey`: MEK-RPG-generated key for safe retry detection.
- `expectedCampaignId` or `expectedCampaignName`; prefer id after a live read.
- `expectedDate`: current MekHQ campaign date.
- `expectedStateRevision`: required by selector-heavy commands and recommended whenever readiness provides one.
- target selectors and expected target facts.
- `dryRun`: required; use `true` before high-risk apply calls.
- `promptPolicy`: endpoint-specific, usually `refuse_if_prompt`; contract accept uses explicit known choice policies.
- `saveAfterSuccess`: optional, default false.
- `savePath`: required when `saveAfterSuccess` is true.
- `clientContext`: plain-text MEK-RPG audit context.

Common response fields:

- `status`: `applied`, `dry_run`, `refused`, `blocked`, or `failed`.
- `statusReason`: machine-readable result code.
- `message`
- `command`
- `commandVersion`
- `idempotencyKey`
- campaign before/after facts.
- target before/after facts.
- `sideEffects`
- `warnings`
- `unsupported`
- prompt/dialog facts.
- save facts.
- echoed/sanitized audit context.

Idempotency contract:

- Idempotency is process-local.
- Reusing a key for the same completed applied command can return the cached result when feasible.
- Reusing a key with different command or guard facts is refused.
- A repeated key while the original command is running is blocked.
- Dry-runs do not reserve an applied-command idempotency result.

Prompt contract:

- Do not auto-answer arbitrary Swing dialogs.
- Commands default to refusing visible or required prompts unless the endpoint has source-backed explicit prompt choices.
- Unknown prompt branches must be refused before mutation.

Save contract:

- Failed, refused, blocked, and dry-run commands must not save.
- Applied commands save only when `saveAfterSuccess` is true and a valid `savePath` is supplied.
- Saving uses MekHQ save logic, not MEK-RPG direct file writes.

## Implemented Mutating Commands

### POST /advance-day

Legacy prototype for advancing the loaded campaign exactly one day.

Use cases:

- Local proof that the API can invoke MekHQ-owned GUI campaign logic.
- Not the preferred shape for new guarded commands.

Important request fields:

- `expectedCampaignName`
- `expectedDate`
- `dismissAdvanceDayNags`
- `saveAfterSuccess`

Consumer rule: prefer newer guarded command-envelope endpoints for future MEK-RPG actions.

### POST /campaign/command/status-note

Command name: `campaign.status_note`.

Purpose: append a plain-text MEK-RPG audit/status note to MekHQ `GENERAL` daily reports.

Required command-specific fields:

- `reportCategory`: omit or use `GENERAL`.
- `noteText`: plain text only.
- `clientContext`: required plain-text audit context.

Refuses:

- unsupported report categories.
- HTML or unsafe client context.
- visible dialogs under `promptPolicy=refuse_if_prompt`.
- stale campaign/date guards.
- missing save path when saving is requested.

### POST /campaign/command/personnel/status

Command name: `personnel.status`.

Purpose: apply conservative RPG-side personnel status changes through `Person#changeStatus(...)`.

Required command-specific fields include:

- `personId`
- `expectedPersonName`
- `expectedCurrentStatus`
- `expectedCurrentPrisonerStatus`
- optional `expectedUnitId` / `expectedUnitName` guards.
- `newStatus`
- `rpgEventType`
- `rpgEventSource`
- `narrativeReason`
- optional status-note/audit settings exposed by readiness.

Allowed scope:

- Narrative missing/captured/recovered/departed/betrayed/deserted/non-tactical death style events that the command explicitly supports.

Refuses:

- tactical casualty resolution.
- medical/prosthetic/fatigue outcomes.
- prisoner-management workflows outside this status command.
- resurrection and retirement payout flows.
- stale person/status/unit guards.

### POST /campaign/command/personnel/fatigue

Command name: `personnel.fatigue`.

Purpose: apply RPG-side fatigue changes through MekHQ personnel fatigue logic.

Required command-specific fields include:

- `personId`
- `expectedPersonName`
- `expectedCurrentStatus`
- `expectedCurrentPrisonerStatus`
- optional `expectedUnitId` / `expectedUnitName` guards.
- `expectedFatigueDirect`
- `expectedAdjustedFatigue`
- `expectedPermanentFatigue`
- `fatigueDelta`
- `rpgEventType`
- `rpgEventSource`
- `narrativeReason`
- `clientContext`

Refuses:

- disabled fatigue rules.
- inactive/dead personnel.
- stale fatigue/person/unit guards.
- mixed medical, prosthetic, expense, or status effects.
- visible dialogs under `promptPolicy=refuse_if_prompt`.

### POST /campaign/command/markets/unit-offers/purchase

Command name: `markets.unit_offers.purchase`.

Purpose: purchase one current non-black-market unit-market offer through MekHQ-owned purchase logic.

Selector source:

- Call `GET /campaign/commands?selectorDetail=full`.
- Use the returned unit-market offer selector and guard facts.

Required command-specific fields include:

- `offerSelector`
- `expectedStateRevision`
- `expectedOfferFingerprint`
- expected unit, market, price, delivery, and balance guard facts exposed by readiness.
- `promptPolicy=refuse_if_prompt`
- `clientContext`

Refuses:

- stale selectors or stale guard facts.
- duplicate exact offers.
- entity load failures.
- insufficient funds.
- black-market offers.
- unsupported delivery or random-quality exact-guard policies.
- visible prompts.

Consumer rule: do not select unit offers by display name, row index, or MEK-RPG-computed hashes. Use MekHQ readiness selectors only.

### POST /campaign/command/contracts/accept

Command name: `contracts.accept`.

Purpose: accept one current contract-market offer through MekHQ-owned contract acceptance logic.

Selector source:

- Call `GET /campaign/commands?selectorDetail=full`.
- Use the current contract id plus guard facts.

Required command-specific fields include:

- source contract id.
- `expectedStateRevision`
- expected campaign date/location guards.
- expected contract name/employer/enemy/date/payment/mission-count/market-count guard facts exposed by readiness.
- explicit known prompt choices/policies for contract challenge, informational prompts, travel automation, mothballing, and rentals.
- `clientContext`

Apply behavior:

- Credits advance and transport payments as MekHQ contract payments.
- Calls MekHQ mission insertion and contract acceptance logic.
- Processes supported non-dialog faction/StratCon report paths.
- Removes the accepted market offer.
- Returns the new active mission id assigned by MekHQ.

Refuses:

- stale contract or market guards.
- unsupported prompt policies.
- unknown prompt branches.
- visible dialogs.
- unsupported travel, mothball, or rental automation choices.

## Blocked Or Display-Only Domains

MEK-RPG must treat these as unsupported unless a future contract revision says otherwise:

- campaign-wide dirty/unsaved flag: unknown, exposed as warning/unsupported.
- repair work execution, repair assignment, and procurement execution: display-only until stable work selectors and prompt policy exist.
- broad medical treatment and prosthetic surgery: blocked until a non-dialog source service exists.
- personnel hire: blocked pending market-style and applicant workflow design.
- GM funds adjustment: not implemented as a gameplay purchase substitute.
- standalone save command: blocked; save remains command-specific and opt-in.
- tactical result import/writeback: out of this live API contract.

## Timeout And Reliability Expectations

`Confirmed locally with user present`: on `2026-06-26`, the issue `#68` live smoke passed against source-built MekHQ with loaded campaign `The Learning Ropes` on `3025-04-08`.

Observed smoke probes all returned HTTP `200` before timeout:

- `/status` with `TimeoutSec 5`.
- `/campaign/summary` with `TimeoutSec 15`.
- `/campaign/pending-deployments` with `TimeoutSec 15`.
- `/campaign/pending-deployments?personName=Moreno` with `TimeoutSec 15`.
- default `/campaign/commands` with `TimeoutSec 20`.
- narrowed `/campaign/state` scenario/report, logistics, and personnel reads with `TimeoutSec 45`.

Consumer recommendation:

- Use short timeouts for `/status`, `/campaign/summary`, and `/campaign/pending-deployments`.
- Use `45` seconds for narrowed `/campaign/state` reads until broader real-campaign timing is known.
- Use default `/campaign/commands` for normal UI readiness.
- Use `selectorDetail=full` only when the user enters a specific command workflow.

## Fixtures

Sanitized example payloads live in `docs/templates/`:

- `mekhq-live-campaign-summary.fixture.json`
- `mekhq-live-campaign-state.fixture.json`
- `mekhq-live-campaign-warning-heavy.fixture.json`
- `mekhq-live-campaign-commands.fixture.json`
- `mekhq-live-pending-deployments.fixture.json`
- `mekhq-live-personnel-detail.fixture.json`

Fixtures preserve shape and contract semantics but are not real campaign data.

## Contract Maintenance

Update this document when:

- a new endpoint is implemented.
- a command changes required guard fields.
- `GET /campaign/commands` changes readiness or selector shape.
- a blocked domain becomes available.
- a live smoke test changes timeout expectations.
- MEK-RPG learns it needs a stricter field guarantee.

Historical implementation notes remain in:

- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_RELIABILITY_TRACKING.md`
