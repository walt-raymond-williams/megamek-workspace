# MEK-RPG Live MekHQ API Reliability Tracking

## Purpose

GitHub Issues are the execution source of truth. This file is the compact recovery snapshot for epic `#62`, which tracks reliability fixes after live MEK-RPG play was blocked by local MekHQ API timeouts on `2026-06-26`.

## Workstream Shape

- Parent epic: `#62`, "Epic: Stabilize live MekHQ API reliability for MEK-RPG play"
- Status: `Issues #63 through #67 complete; reliability hardening queue complete pending human review`
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
- Closed:
  - `#63`: Audit live MekHQ API timeout sources and add collector timing instrumentation.
  - `#64`: Keep MekHQ summary and command readiness endpoints fast and bounded.
  - `#65`: Make live MekHQ state section filtering lazy and partial-response capable.
  - `#66`: Expose lightweight pending scenario and deployment commitment data.
  - `#67`: Add live MekHQ API reliability regression tests and smoke checklist.
- Open:
  - None.
- Blocked:
  - None in this reliability hardening slice.

## Recommended Next Step

- Issue: human review / source publication
- Why next: the reliability child issues are locally complete, but MekHQ source pushes remain blocked by upstream repository permissions.
- Handoff: archived issue `#67` handoff at `docs/handoffs/archive/add-live-mekhq-api-reliability-tests.md`.

## Verification State

- Commands passed:
  - From `external/src/mekhq`: `.\gradlew.bat :MekHQ:test --tests mekhq.service.LocalCommandReadinessExporterTest --tests mekhq.service.LocalControlServiceHttpTest`.
  - From `external/src/mekhq`: `.\gradlew.bat :MekHQ:compileJava :MekHQ:checkstyleMain`.
  - From `external/src/mekhq`: `.\gradlew.bat --no-daemon :MekHQ:test --tests mekhq.service.LocalCampaignStateExporterTest --tests mekhq.service.LocalControlServiceHttpTest --tests mekhq.service.LocalCommandReadinessExporterTest`.
  - From `external/src/mekhq`: `.\gradlew.bat --no-daemon :MekHQ:compileJava :MekHQ:checkstyleMain :MekHQ:checkstyleTest`.
  - From `external/src/mekhq`: `.\gradlew.bat --no-daemon :MekHQ:test --tests mekhq.service.LocalCampaignStateExporterTest`.
  - From `external/src/mekhq`: `.\gradlew.bat --no-daemon :MekHQ:test --tests mekhq.service.LocalControlServiceHttpTest`.
  - From `external/src/mekhq`: `.\gradlew.bat --no-daemon :MekHQ:compileJava :MekHQ:checkstyleMain :MekHQ:checkstyleTest`.
- Source commits:
  - `5effaa5517` in `external/src/mekhq`: `Instrument local control API read paths`.
  - `9ad8fa5f4a` in `external/src/mekhq`: `Bound local command readiness selectors`.
  - `72424e4d9c` in `external/src/mekhq`: `Make local campaign state export partial`.
  - `ba865793c5` in `external/src/mekhq`: `Expose pending deployment API`.
  - `81afcee70a` in `external/src/mekhq`: `Add local API reliability regression tests`.
- Documentation:
  - `docs/current/MEK_RPG_LIVE_MEKHQ_API_TIMEOUT_AUDIT.md`.
  - `docs/current/MEK_RPG_LIVE_MEKHQ_API_RELIABILITY_SMOKE_CHECKLIST.md`.
- Completed behavior:
  - `Confirmed from source`: default `GET /campaign/commands` now uses a cheap `commands-lite-*` revision and omits expensive unit-market and contract-market selector construction with machine-readable `selector_generation_deferred` rows.
  - `Confirmed from source`: full market selector guard facts remain available through `GET /campaign/commands?selectorDetail=full` or `includeExpensiveSelectors=true`.
  - `Confirmed from source`: current personnel, current unit, and personnel-market applicant selectors have bounded default limits; oversized groups return `selector_limit_exceeded` instead of unbounded rows.
  - `Confirmed from source`: narrowed `GET /campaign/state?sections=...` requests do not collect unrequested personnel/unit/scenario sections, and requested section collector failures now return partial HTTP `200` payloads with `response_status`, `partial_response`, structured warnings, failed collector timing, and unsupported details.
  - `Confirmed from source`: `GET /campaign/pending-deployments` returns current scenario ids/names/dates/statuses, source-confirmed unit assignments, crew/person ids and roles, optional `personId`/`personName` commitment lookup, and explicit unsupported metadata for UI-selected viewpoint person state. The same compact object is included in `GET /campaign/summary`.
  - `Confirmed from source`: source commit `81afcee70a` adds loaded-campaign HTTP regression coverage for `/campaign/summary`, `/campaign/pending-deployments`, default `/campaign/commands`, narrowed `/campaign/state`, bad state-section post-failure availability, and default command readiness deferral of expensive contract guard facts.
- Manual checks: live GUI smoke checklist added at `docs/current/MEK_RPG_LIVE_MEKHQ_API_RELIABILITY_SMOKE_CHECKLIST.md`; not run in this session because no source-built MekHQ GUI with a safe loaded campaign was under agent control.
- Known blockers: live smoke verification needs a source-built MekHQ app with the control API enabled and a safe loaded campaign. Java-level per-section timeout cancellation remains deferred pending safer Swing/threading design. Source push is blocked because `external/src/mekhq` points at upstream `MegaMek/mekhq`, and GitHub returned `Permission to MegaMek/mekhq.git denied to walt-raymond-williams`.

## Related Docs

- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_TIMEOUT_AUDIT.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_RELIABILITY_SMOKE_CHECKLIST.md`
- `docs/current/GUARDED_LIVE_MEKHQ_COMMAND_API_TRACKING.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEGAMEK_API_RELIABILITY_HANDOFF_2026-06-26.md`
- `docs/handoffs/archive/audit-live-mekhq-api-timeouts.md`
