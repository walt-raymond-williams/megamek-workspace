# Agent Handoff

## Issue

- GitHub issue: `#103`
- Roadmap entry: `Epic: Improve Princess tactical cohesion and anti-bait movement`
- Priority: `High`

## Goal

Harden `UtilityPathRanker` so experimental Princess movement remains usable when no visible enemies exist, and add diagnostic score terms that make utility decisions inspectable before larger tactical-cohesion behavior is added.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/MEGAMEK_PRINCESS_AI_SOURCE_AUDIT.md`
- `docs/current/PRINCESS_TACTICAL_COHESION_TRACKING.md`

Source findings to verify before editing:

- `Princess#getPathRanker(Entity)` selects `UtilityPathRanker` only when `BehaviorSettings#isExperimental()` is true.
- `UtilityPathRanker#rankPath(...)` calls `shouldFallBack(pathCopy, movingUnit, enemies.getFirst())`, which can throw when the visible enemy list is empty.
- `PathRanker#rankPaths(...)` catches path-ranking exceptions, but losing ranked paths is not a good no-contact movement behavior.
- `BasicPathRanker` writes named score terms into `RankedPath#getScores()` for BotLogger TSV diagnostics; `UtilityPathRanker` currently builds a reason string but does not populate comparable score terms.

## Expected Output

- Focused MegaMek source change in the experimental movement path.
- No-visible-enemy behavior that still ranks useful paths using strategic goals, safety, movement, self preservation, or other existing terms that apply without contact.
- Named diagnostic score terms for existing utility factors.
- Focused tests for the edge case and score-term population.

## Files And Areas

Likely files to read or edit:

- `external/src/megamek/megamek/src/megamek/client/bot/princess/UtilityPathRanker.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/BasicPathRanker.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/PathRanker.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/RankedPath.java`
- `external/src/megamek/megamek/unittests/megamek/client/bot/princess/BasicPathRankerTest.java`
- Nearby Princess/path-ranker tests, or a new focused `UtilityPathRankerTest` if that matches the local test style.

## Commands

Start with:

```powershell
git status --short --branch
git -C external/src/megamek status --short --branch
rg -n "class UtilityPathRanker|enemies\.getFirst|RankedPath|getScores|BotLogger|rankPath" external/src/megamek/megamek/src/megamek/client/bot/princess external/src/megamek/megamek/unittests/megamek/client/bot/princess -g "*.java"
```

Then identify and run the narrowest relevant Gradle test command from the MegaMek source tree. Record exact commands and results.

## Constraints

- Keep this issue to hardening and observability. Do not add the broader support/combat-power behavior here.
- Do not add noisy normal gameplay logging.
- Preserve existing `BasicPathRanker` behavior unless a shared diagnostic helper is clearly warranted.
- Do not include unrelated workspace or source changes.

## Acceptance Criteria

- Experimental `UtilityPathRanker` no longer fails path ranking merely because visible enemies are absent.
- No-contact fallback behavior is source-appropriate and does not manufacture a fake enemy.
- `RankedPath#getScores()` includes named terms for the major existing `UtilityPathRanker` factors.
- Diagnostics are suitable for BotLogger/debug inspection without normal gameplay noise.
- Focused automated tests cover no-visible-enemy ranking and score-term population.
- Workspace tracking docs are updated if implementation findings change later child issue scope.

## Open Questions

- Should no-visible-enemy fallback omit `fallBack` entirely, use a neutral modifier, or derive a strategic/no-contact modifier?
- Should diagnostic score keys match `BasicPathRanker` naming where factors overlap?
