# Agent Handoff

## Issue

- GitHub issue: `#105`
- Roadmap entry: `Epic: Improve Princess tactical cohesion and anti-bait movement`
- Priority: `High`

## Goal

Improve Princess movement scoring so multiple units can transition into an attack as a mutually supporting force rather than one unit advancing opportunistically alone.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/MEGAMEK_PRINCESS_AI_SOURCE_AUDIT.md`
- `docs/current/PRINCESS_TACTICAL_COHESION_TRACKING.md`
- Dependency handoff: `docs/handoffs/active/add-princess-local-support-combat-power-scoring.md`

Source/design context:

- BattleTech formations cannot be rigid: terrain, jump movement, weapon brackets, unit speed, damaged state, and objectives all matter.
- Existing experimental classes provide useful hints but not a complete coordinated-advance model.
- The movement phase ranks one entity's candidate paths at a time, so supported-advance scoring may need to infer whether friends can support or follow rather than plan all unit moves globally.

## Expected Output

- A supported-advance score or modifier that rewards mutual support, overlapping coverage, reasonable spacing, and several friendly units threatening the same enemy area.
- Preservation of tactical diversity: flankers, scouts, fire support, brawlers, slow assaults, damaged units, withdrawing units, and objective-bound units should not collapse into one blob behavior.
- Tests for supported advance and fast flanker cases.

## Files And Areas

Likely files to read or edit:

- `external/src/megamek/megamek/src/megamek/client/bot/princess/UtilityPathRanker.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/SwarmContext.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/CoverageValidator.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/EnemyTracker.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/UnitBehavior.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/BehaviorSettings.java`
- Princess/path-ranker tests under `external/src/megamek/megamek/unittests/megamek/client/bot/princess/`

## Commands

Start with:

```powershell
rg -n "SwarmCluster|cluster|formation|CoverageValidator|calculateFormation|UnitBehavior|BehaviorType|UnitRole|herd|antiCrowd" external/src/megamek/megamek/src/megamek/client/bot/princess -g "*.java"
```

Run focused tests and any broader Princess tests affected by the movement scoring change.

## Constraints

- Coordinate with issue `#104`; do not duplicate the basic isolation/combat-power score if it already exists.
- Do not require rigid formation movement.
- Do not force all Princess units into a compact group.
- Keep the implementation compatible with per-unit path ranking unless source investigation justifies a larger planner.
- Preserve existing behavior settings and unit-role diversity.

## Acceptance Criteria

- Supported group advances are rewarded relative to lone overextensions.
- Multiple friendly units threatening or covering the same enemy area contribute positively where source-appropriate.
- Fast flankers can still operate away from slow units when the local enemy exchange is not overwhelming.
- Fire support and damaged/withdrawing units are not incorrectly pulled into close formation.
- Diagnostic score terms explain the supported-advance contribution.
- Tests cover supported advance and fast-flanker behavior.

## Open Questions

- Should support be measured from current friendly positions, likely future positions, weapon coverage, or cluster geometry?
- Can `SwarmContext` clusters be extended safely, or is a smaller path-ranker helper cleaner?
