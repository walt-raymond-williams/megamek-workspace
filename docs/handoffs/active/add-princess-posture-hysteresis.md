# Agent Handoff

## Issue

- GitHub issue: `#106`
- Roadmap entry: `Epic: Improve Princess tactical cohesion and anti-bait movement`
- Priority: `High`

## Goal

Prevent very small utility changes from making Princess abruptly abandon an otherwise sensible tactical posture, especially when a player feint makes one enemy slightly more attractive.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/MEGAMEK_PRINCESS_AI_SOURCE_AUDIT.md`
- `docs/current/PRINCESS_TACTICAL_COHESION_TRACKING.md`
- Dependency handoffs for `#104` and `#105`.

Design context:

- The problem is not aggression alone. Princess needs a limited commitment mechanism so a tiny utility edge does not break a good posture.
- The source architecture is mostly per-unit path ranking with shared Princess state. Start by finding the smallest source-compatible mechanism.
- Hysteresis must cooperate with anti-stagnation issue `#107`; it cannot become indefinite turtling.

## Expected Output

- A limited posture/hysteresis mechanism: for example a minimum utility improvement for major posture breaks, a bonus for maintaining a useful posture, a penalty for abruptly breaking formation, or short-lived tactical memory.
- Diagnostics that expose the hysteresis/posture contribution.
- Tests for baited single-unit advance behavior.

## Files And Areas

Likely files to read or edit:

- `external/src/megamek/megamek/src/megamek/client/bot/princess/Princess.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/PathRankerState.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/PathRanker.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/UtilityPathRanker.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/SwarmContext.java`
- Princess movement tests under `external/src/megamek/megamek/unittests/megamek/client/bot/princess/`

## Commands

Useful source search:

```powershell
rg -n "PathRankerState|initUnitTurn|continueMovementFor|getBestPath|rankPaths|last|round|memory|cluster|formation|fallback|fallBack" external/src/megamek/megamek/src/megamek/client/bot/princess -g "*.java"
```

Run focused movement tests and any affected Princess/path-ranker tests.

## Constraints

- Do not assume a persistent global state machine is necessary.
- Hysteresis must be overrideable by clear tactical opportunity, danger, objectives, withdrawal, and anti-stagnation pressure.
- Keep score changes explainable through diagnostics.
- Avoid coupling to unrelated UI or firing behavior.

## Acceptance Criteria

- Small utility deltas do not automatically cause a major posture break when support/combat-power scores say the existing posture remains sound.
- The mechanism is bounded in time, scope, or magnitude.
- Clear tactical opportunities and safety needs can override posture commitment.
- Baited single-unit advance tests demonstrate the desired resistance to simple feints.
- Diagnostics expose the posture/hysteresis term.

## Open Questions

- Should posture memory be per-Princess, per-cluster, per-unit, or only derived from current board state?
- What source signal best identifies a "major posture break" without overfitting to one map setup?
