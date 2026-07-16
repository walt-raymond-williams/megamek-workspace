# MEK-RPG Live-Play API Gap Producer Plan

Status: producer-side plan for turning MEK-RPG live play gaps into bounded MekHQ local API work.

Source request: `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_PLAYTEST_API_GAP_CHANGE_REQUEST_2026_07_16.md`

Source gap report: `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_PLAYTEST_API_GAP_REPORT.md`

## Position

MEK-RPG is right to keep live play on the MekHQ local API and away from routine active `.cpnx`, `.cpnx.gz`, XML, or raw save parsing. The producer-side response should not blindly accept every requested endpoint shape, though. MekHQ should expose source-owned facts through bounded read endpoints, explicit `unknown` / `withheld` / `unsupported` markers, and partial-response metadata where collectors are slow or a requested fact is not source-owned.

This plan prioritizes live play needs that change table decisions:

1. Scenario intel, deployment requirements, player-force BV, reinforcement details, and visible/estimated OpFor summaries.
2. Bounded finance, salvage, reputation, and XP histories that can answer "what changed and why?"
3. Inventory mass, cargo, transport, and recovery capacity where MekHQ has reliable source-owned calculations.
4. Personnel turnover and HR/admin pressure only where MekHQ tracks the needed history or capacity signals.
5. Fixtures, docs, and smoke checks that prove the endpoints stay bounded and preserve partial-response behavior.

## Producer Rules

- Prefer extending existing endpoints before adding parallel shapes: `/campaign/pending-deployments`, `/campaign/state?sections=...`, `/campaign/personnel/detail`, and the planned activity-history API should carry much of this work.
- Do not provide unbounded campaign-history dumps. Ledger and history endpoints must require or default to limits, date windows, category filters, and target filters.
- Do not invent calculations MekHQ does not own. If cargo, recovery, HR pressure, reputation projection, or fog-of-war data is not source-confirmed, return explicit `unknown`, `withheld`, or `unsupported` entries.
- Keep fog of war intact. Exact OpFor entities should appear only when MekHQ already allows disclosure; otherwise expose estimates, aggregate summaries, confidence, and hidden reasons.
- Keep read endpoints read-only, loopback-only, and disabled-by-default with the existing local control API posture.
- Keep section collectors lazy and partial-response capable. New expensive collectors must report timing and failure metadata.

## MEK-RPG Package Feedback

Before this request is handed back as a polished consumer package, MEK-RPG should fix two packaging issues:

- Commit the gap-report entries cited by the change request, so the pushed source report matches the request.
- Add the open `2026-07-16 - Reinforcement arrival details unavailable for pending scenario` finding to the change request as a first-class Priority 1 scenario/deployment requirement.

Those are consumer-package traceability fixes, not blockers for producer-side planning.

## Workstream Split

### Epic: close MEK-RPG live-play API gaps

- Issue: `#94`
- Goal: Coordinate the producer-side MekHQ API work needed to address the open MEK-RPG playtest gaps without accepting unbounded or unsourceable endpoint requests.
- Output: Source-backed design decisions, implemented high-priority endpoint slices, fixtures/docs, and smoke checks.
- Recommended sequence: design contract first, scenario intel second, ledger/history and logistics next, personnel turnover after the history contract is settled, fixtures/smoke last.

### Design bounded live-play gap API contract

- Issue: `#95`
- Goal: Audit the MEK-RPG request against existing MekHQ local API source, existing completed epics, and open activity-history work.
- Expected output: A design note that chooses exact endpoint paths, query parameters, default limits, unknown/unsupported markers, and source-owner boundaries for every requested gap.
- Key decision: Finance/reputation/XP/personnel history should build on the existing activity-history design path instead of introducing unrestricted history endpoints.

### Scenario intel, deployment, BV, and reinforcement export

- Issue: `#96`
- Goal: Extend pending/current scenario reads with player-force BV, assigned-unit BV, deployment requirement facts, reinforcement metadata, and visible or estimated OpFor summaries.
- Expected output: Source implementation and tests for `/campaign/pending-deployments`, scenario state DTOs, or a dedicated scenario-intel read if source ownership requires it.
- Highest-value fields: `player_force_total_bv`, assigned-unit `battle_value`, `deployment_requirements`, `reinforcements[]`, `bot_forces[].known_entities[]`, `bot_forces[].estimated_summary`, fog-of-war markers, and deployment-zone/edge facts where known.

### Bounded salvage, finance, reputation, and XP ledgers

- Issue: `#100`
- Goal: Expose bounded, queryable history for finance transactions, salvage dispositions, contract performance/reputation, and personnel XP sources where source-owned histories exist.
- Expected output: Endpoint/source design aligned with issue `#58` activity-history rules, plus implementation for the first supported ledger families.
- Constraints: Require limits/date windows and filters. Return `unsupported` when MekHQ does not track projected reputation, item-level salvage sale provenance, or XP source deltas.

### Inventory, cargo, transport, and recovery capacity

- Issue: `#97`
- Goal: Expose inventory mass/value and transport/recovery capacity from source-owned MekHQ data without guessing unsupported capacity math.
- Expected output: Inventory/cargo/transport DTOs, aggregate totals, exact-or-unknown capacity fields, and source-backed warnings for missing capacity calculations.
- Constraints: If MekHQ only tracks display relationships or partial transport assignment stubs, expose those facts and explicit `unsupported` metadata instead of calculating from raw save fields ad hoc.

### Personnel turnover and HR/admin pressure

- Issue: `#98`
- Goal: Expose personnel departure/turnover history and admin/HR pressure facts only where MekHQ source tracks them.
- Expected output: Source audit plus implementation or explicit unsupported entries for `personnel_turnover[]` and `admin_hr_pressure`.
- Dependencies: activity-history design issue `#58` and existing `/campaign/personnel/detail` behavior.

### Docs, fixtures, regression tests, and live smoke checklist

- Issue: `#99`
- Goal: Update the local API contract, fixtures, and verification checklist so MEK-RPG can adapt to the actual producer-supported shapes.
- Expected output: Updated `MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`, sanitized fixtures, targeted service tests, and a disposable-campaign live smoke checklist.
- Acceptance target: MEK-RPG can tell which requested reads are implemented, bounded, unknown, withheld, or unsupported without falling back to active-save parsing during play.

## Open Questions

- Which source branch should receive the next MekHQ API implementation commits, given the current upstream push permission blocker?
- Should scenario intel be a dedicated endpoint or remain folded into `/campaign/pending-deployments` and scenario state DTOs?
- Which salvage and reputation histories are durable source data versus only report text?
- Does MekHQ track enough transport/recovery capacity to expose exact values, or should V1 intentionally expose partial capacity facts with unsupported entries?
- Does MekHQ have a real HR/admin pressure model tied to turnover, or only personnel logs and current role counts?
