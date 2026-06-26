# MEK-RPG Live MekHQ API Reliability Tracking

## Purpose

GitHub Issues are the execution source of truth. This file is the compact recovery snapshot for epic `#62`, which tracks reliability fixes after live MEK-RPG play was blocked by local MekHQ API timeouts on `2026-06-26`.

## Workstream Shape

- Parent epic: `#62`, "Epic: Stabilize live MekHQ API reliability for MEK-RPG play"
- Status: `Issue created`
- Integration branch: use the active local MekHQ API branch unless a later source implementation slice needs a new branch.
- Human review required before merge to `master`: `Yes`, if source changes land.

## Incident Summary

- `Confirmed by MEK-RPG handoff`: `GET /campaign/summary` succeeded once, then later timed out with 15-second and 60-second client timeouts.
- `Confirmed by MEK-RPG handoff`: `GET /campaign/state` timed out repeatedly for full and narrowed section requests.
- `Confirmed by MEK-RPG handoff`: `GET /campaign/commands` timed out with 10-second and 20-second client timeouts.
- `Confirmed by user through MEK-RPG handoff`: current pending operations during the pause were a tank-base defense and an insurgency.
- `Confirmed by MEK-RPG handoff`: the table needed to verify Michelle "Double-M" Moreno's current operation commitment from live MekHQ-owned data.

## Issue Snapshot

- Last refreshed: `2026-06-26`
- Closed: none.
- Open:
  - `#63`: Audit live MekHQ API timeout sources and add collector timing instrumentation.
  - `#64`: Keep MekHQ summary and command readiness endpoints fast and bounded.
  - `#65`: Make live MekHQ state section filtering lazy and partial-response capable.
  - `#66`: Expose lightweight pending scenario and deployment commitment data.
  - `#67`: Add live MekHQ API reliability regression tests and smoke checklist.
- Blocked:
  - `#64`, `#65`, `#66`, and `#67` should consult `#63` when practical.

## Recommended Next Step

- Issue: `#63`
- Why next: the endpoint timeout symptoms cross summary, state, and command readiness. A source-backed collector map and timing story should prevent fixes from guessing at the wrong stall point.
- Handoff: `docs/handoffs/active/audit-live-mekhq-api-timeouts.md`

## Verification State

- Commands passed: issue creation and documentation-only tracking in this workspace.
- Manual checks: none yet.
- Known blockers: live smoke verification needs a source-built MekHQ app with the control API enabled and a safe loaded campaign. Source pushes from `external/src/mekhq` may still need a writable fork/remote.

## Related Docs

- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`
- `docs/current/GUARDED_LIVE_MEKHQ_COMMAND_API_TRACKING.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEGAMEK_API_RELIABILITY_HANDOFF_2026-06-26.md`
- `docs/handoffs/active/audit-live-mekhq-api-timeouts.md`
