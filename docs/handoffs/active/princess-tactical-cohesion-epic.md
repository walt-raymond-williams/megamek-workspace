# Agent Handoff

## Issue

- GitHub issue: `#102`
- Roadmap entry: `Epic: Improve Princess tactical cohesion and anti-bait movement`
- Priority: `High`

## Goal

Coordinate the Princess tactical cohesion initiative. Do not implement directly from the epic. Use it to keep the child issues, roadmap, feature tracking, and source-guided design aligned.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/MEGAMEK_PROJECT_PROFILE.md`
- `docs/current/MEGAMEK_PRINCESS_AI_SOURCE_AUDIT.md`
- `docs/current/PRINCESS_TACTICAL_COHESION_TRACKING.md`

Child handoffs:

- `docs/handoffs/active/harden-princess-utility-ranker-diagnostics.md`
- `docs/handoffs/active/add-princess-local-support-combat-power-scoring.md`
- `docs/handoffs/active/add-princess-supported-advance-scoring.md`
- `docs/handoffs/active/add-princess-posture-hysteresis.md`
- `docs/handoffs/active/add-princess-anti-stagnation-pressure.md`
- `docs/handoffs/active/add-princess-tactical-cohesion-validation.md`

## Expected Output

- Child issues are completed in a dependency-aware order.
- Roadmap and tracking docs remain accurate as implementation findings change scope.
- The epic stays focused on the behavioral target: Princess avoids baited piecemeal attacks while still advancing under pressure or opportunity.

## Files And Areas

Likely docs to maintain:

- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/PRINCESS_TACTICAL_COHESION_TRACKING.md`
- Child handoffs under `docs/handoffs/active/`

Likely source areas for child issues:

- `external/src/megamek/megamek/src/megamek/client/bot/princess/Princess.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/BasicPathRanker.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/UtilityPathRanker.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/PathRanker.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/RankedPath.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/PathRankerState.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/SwarmContext.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/EnemyTracker.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/CoverageValidator.java`

## Commands

Start with source and workspace checks:

```powershell
git status --short --branch
git -C external/src/megamek status --short --branch
rg -n "class UtilityPathRanker|enemies\.getFirst|class SwarmContext|class EnemyTracker|class CoverageValidator|RankedPath|getScores|behaviorSettings\.isExperimental|getPathRanker" external/src/megamek/megamek/src/megamek/client/bot/princess -g "*.java"
```

Choose Gradle verification from the MegaMek source tree after reading project files and nearby tests.

## Constraints

- Do not implement from the epic issue directly.
- Keep broad behavior work on a feature branch such as `codex/princess-tactical-cohesion-dev`.
- Prefer the experimental `UtilityPathRanker` path for ambitious behavior changes until validated.
- Preserve existing Princess behavior settings and tactical diversity.
- Treat anti-isolation and anti-turtling as paired requirements.
- Do not include unrelated user changes.

## Acceptance Criteria

- Child issues `#103` through `#108` are complete or intentionally replanned with roadmap updates.
- The final behavior discourages dangerous unsupported advances as a graded utility factor.
- The final behavior still advances when stalemate, objective pressure, range mismatch, or opportunity justify it.
- Tests and live-game validation cover bait, supported advance, legitimate opportunity, stalemate, fast flanker, suicide isolation, and personality-regression cases.
- Roadmap, tracking, issue bodies, and handoffs agree at close-out.

## Open Questions

- Which behavior pieces should remain experimental and which should later graduate into default `BasicPathRanker` behavior?
- Where should tactical memory live if `PathRankerState` is insufficient?
- What is the cheapest useful combat-power heuristic for large games?
