# Princess Tactical Cohesion Tracking

## Purpose

GitHub Issues are the execution source of truth. This file is the compact local recovery snapshot for the Princess tactical cohesion initiative: branch recommendation, issue state, dependency order, and handoff paths.

## Integration Branch

- Branch: `codex/princess-tactical-cohesion-dev`
- Base: `master`
- Human review required before merge: `Yes`
- Reason: The feature is a multi-issue behavior change to Princess movement. Partial slices should be reviewed together before any default-behavior change is considered.

## Issue Snapshot

- Last refreshed: `2026-08-14`
- Epic:
  - `#102`: Improve Princess tactical cohesion and anti-bait movement.
- Open:
  - `#103`: Harden experimental Princess utility movement and diagnostics.
  - `#104`: Add Princess local support and combat-power movement scoring.
  - `#105`: Add Princess formation-aware supported advance scoring.
  - `#106`: Add Princess tactical posture hysteresis against baiting.
  - `#107`: Add Princess anti-stagnation engagement pressure.
  - `#108`: Add Princess tactical cohesion regression scenarios and validation harness.
- Closed: None.
- Blocked: None at planning time.

## Recommended Next Step

- Issue: `#103`
- Why next: It hardens `UtilityPathRanker` as the experimental sandbox, verifies the no-visible-enemy edge case, and adds diagnostic score terms before new behavior terms are layered on.
- Handoff: `docs/handoffs/active/harden-princess-utility-ranker-diagnostics.md`

## Dependency Order

1. `#103`: Harden the experimental utility path and diagnostics.
2. `#104`: Add local support, isolation, and combat-power scoring.
3. `#105`: Add formation-aware supported advance scoring.
4. `#106`: Add tactical posture hysteresis against baiting.
5. `#107`: Add anti-stagnation engagement pressure.
6. `#108`: Build out validation. Harness discovery may start after `#103`; final acceptance depends on `#104` through `#107`.

## Verification State

- Commands passed: Not applicable; this planning task did not modify MegaMek source.
- Source checked:
  - `Princess#getPathRanker(Entity)` selects `UtilityPathRanker` only when `BehaviorSettings#isExperimental()` is true.
  - `UtilityPathRanker#rankPath(...)` has an unguarded `enemies.getFirst()` fallback check.
  - `BasicPathRanker` populates `RankedPath#getScores()` for BotLogger diagnostics; `UtilityPathRanker` currently does not populate equivalent score terms.
  - Existing Princess tests live under `external/src/megamek/megamek/unittests/megamek/client/bot/princess`.
  - Scenario-runner/test-resource infrastructure exists, including `ScenarioGameRunner` and `testresources/data/scenarios/test_setups/BotvBot.mms`; implementation issue `#108` must verify the best harness.
- Manual checks: None yet.
- Known blockers: None for starting `#103`.

## Related Docs

- `docs/current/ROADMAP.md`
- `docs/current/MEGAMEK_PRINCESS_AI_SOURCE_AUDIT.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/handoffs/active/princess-tactical-cohesion-epic.md`
