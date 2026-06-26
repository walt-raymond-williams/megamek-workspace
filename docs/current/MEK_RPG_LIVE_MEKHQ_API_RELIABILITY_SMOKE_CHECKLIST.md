# MEK-RPG Live MekHQ API Reliability Smoke Checklist

Status: created for GitHub issue `#67` on `2026-06-26`.

Use this checklist when a source-built MekHQ app is running with the local control API enabled and a safe copied/disposable campaign is loaded. These probes are read-only GET requests.

## Preconditions

- `Confirmed from source`: local MekHQ source commit `81afcee70a` adds HTTP/source regression coverage for the issue `#67` reliability path.
- `Confirmed locally`: the source checkout is `C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq`.
- `Confirmed locally`: start a source-built MekHQ instance with `mekhq.controlApi.enabled=true`, usually through `tools\start-mekhq-control-api.ps1`.
- `Confirmed by workflow`: load a copied or disposable campaign in the MekHQ GUI before running campaign endpoints. Do not use these commands as proof against a raw campaign save that is not already safely loaded.

## Start Or Check The API

```powershell
cd C:\Users\waltr\Documents\megamek-workspace
powershell -NoProfile -ExecutionPolicy Bypass -File tools\start-mekhq-control-api.ps1
```

In a second PowerShell session:

```powershell
$base = 'http://127.0.0.1:32180'
Invoke-RestMethod -Method Get -Uri "$base/status" -TimeoutSec 5
```

Expected result:

- HTTP `200`.
- `status` is `ready`.
- If no campaign is loaded, campaign endpoints should return HTTP `409` with a no-campaign message instead of hanging.

## Play-Critical Read Probes

Run these after loading a safe campaign:

```powershell
$base = 'http://127.0.0.1:32180'

$summary = Invoke-RestMethod -Method Get -Uri "$base/campaign/summary" -TimeoutSec 15
$pending = Invoke-RestMethod -Method Get -Uri "$base/campaign/pending-deployments" -TimeoutSec 15
$commands = Invoke-RestMethod -Method Get -Uri "$base/campaign/commands" -TimeoutSec 20
$stateScenario = Invoke-RestMethod -Method Get -Uri "$base/campaign/state?sections=bridge_metadata,campaign,contracts,scenarios,reports" -TimeoutSec 45
$stateLogistics = Invoke-RestMethod -Method Get -Uri "$base/campaign/state?sections=bridge_metadata,units,repairs_and_logistics" -TimeoutSec 45
$statePersonnel = Invoke-RestMethod -Method Get -Uri "$base/campaign/state?sections=bridge_metadata,personnel" -TimeoutSec 45
```

Expected result:

- Each request returns HTTP `200` before the client timeout.
- `summary.endpoint_timing.endpoint` is `/campaign/summary`.
- `summary.pendingDeployments` is present and includes pending scenario counts.
- `pending.endpoint_timing.endpoint` is `/campaign/pending-deployments`.
- `commands.endpoint_timing.endpoint` is `/campaign/commands`.
- Default `commands` reports `selector_generation_deferred` for expensive market selectors instead of building full guard facts.
- Narrowed `state` responses include `endpoint_timing`, `collector_timing`, `response_status`, and `partial_response`.
- If a requested state collector fails, the response should remain HTTP `200` with `response_status: partial`, already-collected sections, warnings, and unsupported details.

## Viewpoint Person Commitment Probe

Use either a person id or a name fragment when the table needs to verify whether a specific pilot is already committed to a pending operation:

```powershell
$base = 'http://127.0.0.1:32180'
Invoke-RestMethod -Method Get -Uri "$base/campaign/pending-deployments?personName=Moreno" -TimeoutSec 15
```

Expected result:

- `viewpoint_person.lookup_requested` is `true`.
- `viewpoint_person.commitment_count` and `viewpoint_person.commitments` reflect only current pending scenarios whose assigned units include that person in `Unit#getCrew()`.
- If no match is found, the endpoint should still return HTTP `200` with an empty commitment list.

## Full Selector Probe

Only run this when a command workflow needs exact market guard facts:

```powershell
$base = 'http://127.0.0.1:32180'
Invoke-RestMethod -Method Get -Uri "$base/campaign/commands?selectorDetail=full" -TimeoutSec 60
```

Expected result:

- HTTP `200` or a bounded response before the client timeout.
- Full selector facts may be slower than default `/campaign/commands`; default command readiness remains the play-critical probe.

## Known Limits

- `Confirmed from source`: Java-level per-section timeout cancellation remains deferred because background collectors could keep reading live campaign state concurrently.
- `Confirmed from source`: the API does not expose a source-confirmed currently selected MekHQ UI person; use `personId` or `personName` on `/campaign/pending-deployments`.
- `Confirmed locally`: automated tests cover loaded-campaign HTTP read responsiveness shape, narrowed state response availability, default command selector deferral, partial state collector failure, pending deployment commitment lookup, and post-failure server availability. A real GUI-loaded campaign smoke test still requires the user or a UI-capable session.
