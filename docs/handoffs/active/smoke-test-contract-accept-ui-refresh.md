# Agent Handoff

## Issue

- GitHub issue: `#79`
- Roadmap entry: `Epic: Guarded live MekHQ command API for MEK-RPG`
- Priority: `High`

## Goal

Run a disposable live smoke test for `POST /campaign/command/contracts/accept`, proving the implemented prompt-choice policy works in a source-built MekHQ GUI session and recording whether visible MekHQ UI panels stay current after the API mutation.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_CONTRACT_ACCEPT_COMMAND_DESIGN.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`
- `docs/current/GUARDED_LIVE_MEKHQ_COMMAND_API_TRACKING.md`

## Expected Output

- A source-backed/live-smoke note under `docs/current/` or updates to the existing API tracking docs.
- Exact disposable campaign save path and MekHQ source commit used.
- Dry-run and apply request/response summaries for one selectable contract-market offer.
- API reread evidence after apply: `/status`, `/campaign/commands`, and relevant `/campaign/state` sections.
- Visible MekHQ UI refresh observations for the contract market, active contract/briefing view, reports, and finances.
- Any follow-up source issue if panels remain stale or a prompt branch is not modeled safely.

## Files And Areas

Likely docs to update:

- `docs/current/GUARDED_LIVE_MEKHQ_COMMAND_API_TRACKING.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`
- `docs/current/TASKS.md`
- `docs/current/ROADMAP.md`

Likely source areas to inspect if behavior is stale:

- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCommandReadinessExporter.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalContractMarketSelectors.java`
- `external/src/mekhq/MekHQ/src/mekhq/gui/dialog/ContractMarketDialog.java`
- MekHQ GUI tab/panel owners for contract market, briefing room, reports, and finances.

## Commands

Useful orientation:

```powershell
git status --short --branch
rg -n "contracts.accept|ContractAccept|acceptContract|ContractMarketDialog|fire.*Event|repaint|refresh" external/src/mekhq/MekHQ/src
```

Useful live checks after launching source-built MekHQ with `-Dmekhq.controlApi.enabled=true`:

```powershell
Invoke-RestMethod -Method Get -Uri 'http://127.0.0.1:32180/status' -TimeoutSec 5
Invoke-RestMethod -Method Get -Uri 'http://127.0.0.1:32180/campaign/commands?selectorDetail=full' -TimeoutSec 60
Invoke-RestMethod -Method Get -Uri 'http://127.0.0.1:32180/campaign/state?sections=bridge_metadata,campaign,finances,contracts,markets,reports' -TimeoutSec 45
```

## Constraints

- Use copied or disposable campaign saves only.
- Do not run apply mode against the user's real campaign unless explicitly told to.
- Do not hide unrun live checks; record them as blockers.
- Keep `saveAfterSuccess=false` unless the test intentionally writes to a disposable save path.
- Preserve explicit evidence labels: source-confirmed, live-smoke-confirmed, user-confirmed, inferred, or blocked.
- Do not include unrelated user changes when committing.

## Acceptance Criteria

- `GET /campaign/commands?selectorDetail=full` exposes at least one selectable contract offer and its guard facts.
- Dry-run `contracts.accept` validates the selected offer and reports intended side effects without mutation.
- Apply mode accepts the selected offer, removes it from the market, returns the new active mission id, credits expected contract payments, records expected report/audit behavior, and opens no unsupported dialogs.
- Prompt facts and explicit choices are recorded for challenge confirmation, faction/StratCon info, travel/mothball, rentals, and unknown prompt policy.
- API rereads show changed readiness/state and current contract, finance, market, and report facts.
- Visible MekHQ UI behavior is documented: automatic refresh, refresh after tab switch/manual action, or stale panels requiring source follow-up.
- A bad or stale request followed by `/status` or `/campaign/commands` confirms the local control server remains usable.

## Open Questions

- Which copied campaign should be used for the first representative contract-market offer?
- Do current MekHQ panels receive enough model/event notifications from the non-dialog acceptance path, or does the API need an explicit GUI refresh/event hook after mutation?
