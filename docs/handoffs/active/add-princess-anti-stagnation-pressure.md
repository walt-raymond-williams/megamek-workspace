# Agent Handoff

## Issue

- GitHub issue: `#107`
- Roadmap entry: `Epic: Improve Princess tactical cohesion and anti-bait movement`
- Priority: `High`

## Goal

Add anti-stagnation engagement pressure so Princess does not hold a strong defensive posture indefinitely when the opponent refuses to approach.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/MEGAMEK_PRINCESS_AI_SOURCE_AUDIT.md`
- `docs/current/PRINCESS_TACTICAL_COHESION_TRACKING.md`
- Dependency handoffs for `#104`, `#105`, and `#106`.

Design context:

- Anti-stagnation is not optional. It is the counterweight to anti-isolation.
- Engagement pressure must prefer supported or coordinated advances. If it just makes one unit impatient, it recreates the original failure.
- The source implementation must determine whether pressure belongs in Princess-wide state, cluster state, per-unit state, or path scoring.

## Expected Output

- A bounded pressure-to-engage signal that rises when meaningful engagement is absent and decays/resets after engagement.
- Integration with support/combat-power and formation-aware scoring so pressure favors coherent advances.
- Tests for long-term stalemate behavior.
- Diagnostics that expose engagement pressure.

## Files And Areas

Likely files to read or edit:

- `external/src/megamek/megamek/src/megamek/client/bot/princess/Princess.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/PathRankerState.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/UtilityPathRanker.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/SwarmContext.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/FireControl.java`
- Princess tests and possible scenario-runner tests.

## Commands

Useful source search:

```powershell
rg -n "currentRound|damage|engage|strategic|destination|forcedWithdrawal|FireControl|PathRankerState|SwarmContext" external/src/megamek/megamek/src/megamek/client/bot/princess -g "*.java"
```

Run focused tests and broader Princess movement tests affected by engagement-pressure state.

## Constraints

- Do not undo the anti-isolation work from `#104` and `#105`.
- Pressure should reset or decay after source-verified engagement signals such as damage exchange, meaningful attacks, objective progress, or tactical contact.
- Prefer existing state holders over new global systems unless source investigation shows they are inadequate.
- Keep normal logging quiet.

## Acceptance Criteria

- Princess becomes gradually more willing to advance when no meaningful engagement is occurring.
- The pressure prefers supported/coordinated advances and does not promote isolated suicide charges.
- Pressure decays or resets after meaningful engagement.
- Weapon-range mismatch, objectives/destinations, enemy withdrawal, or local friendly superiority are considered where inexpensive and source-appropriate.
- Long-term stalemate tests show Princess eventually creates action without collapsing into piecemeal attacks.
- Diagnostics expose the pressure contribution.

## Open Questions

- What is the best source signal for "meaningful engagement": attacks made, expected damage, actual damage, line-of-sight/range, objective progress, or a combination?
- Should pressure be shared by the force or local to a cluster/role group?
