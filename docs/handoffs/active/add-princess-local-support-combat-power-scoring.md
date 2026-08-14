# Agent Handoff

## Issue

- GitHub issue: `#104`
- Roadmap entry: `Epic: Improve Princess tactical cohesion and anti-bait movement`
- Priority: `High`

## Goal

Add movement scoring that estimates whether a candidate destination leaves the moving Princess unit locally supported or dangerously isolated, including a computationally reasonable local combat-power comparison.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/MEGAMEK_PRINCESS_AI_SOURCE_AUDIT.md`
- `docs/current/PRINCESS_TACTICAL_COHESION_TRACKING.md`
- Handoff for dependency `#103`: `docs/handoffs/active/harden-princess-utility-ranker-diagnostics.md`

Relevant source facts:

- Existing movement scoring already estimates the moving unit's offensive and defensive tradeoff, but it does not directly compare local friendly support against enemies able to engage the destination.
- `CoverageValidator` only checks whether at least one ally is within a fraction of weapon range; treat it as a small utility, not a complete isolation model.
- `EnemyTracker` tracks visible enemy profiles with simple threat scoring and damage potential. Verify correctness before relying on it.
- `SwarmContext` holds strategic goals and clusters, but it is not a finished formation doctrine.

## Expected Output

- A graded support/isolation/combat-power movement score, preferably in the experimental utility path first.
- Named diagnostic terms for friendly support, enemy threat, local combat-power ratio or equivalent, and isolation penalty.
- Tests demonstrating suicide isolation ranks below coherent alternatives while legitimate tactical opportunity remains possible.

## Files And Areas

Likely files to read or edit:

- `external/src/megamek/megamek/src/megamek/client/bot/princess/UtilityPathRanker.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/BasicPathRanker.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/EnemyTracker.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/CoverageValidator.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/SwarmContext.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/BehaviorSettings.java`
- `external/src/megamek/megamek/unittests/megamek/client/bot/princess/`

## Commands

Useful source search:

```powershell
rg -n "estimatedDamage|expectedDamage|calculate.*Mod|CoverageValidator|EnemyTracker|SwarmContext|BehaviorSettings|getHerd|Self Preservation|Bravery|Aggression" external/src/megamek/megamek/src/megamek/client/bot/princess -g "*.java"
```

Run focused tests chosen from the MegaMek source tree and record exact commands/results.

## Constraints

- Use inexpensive heuristics. Do not introduce expensive full combat simulation into path ranking.
- Make isolation a graded utility consideration, not a hard prohibition.
- Preserve legitimate isolated movement for forced withdrawal, escape, explicit destinations/objectives, deliberate fast flanking, finishing crippled targets, and clearly favorable expected exchanges.
- Prefer existing Princess abstractions over a new role-classification framework.
- Preserve behavior-setting semantics.

## Acceptance Criteria

- Candidate paths include friendly support, enemy threat, local combat-power, and isolation-related score terms.
- Modest offensive gain that creates an unsupported local 1-v-3 or 1-v-4 is substantially devalued.
- A clearly favorable tactical opportunity can still win despite temporary lower cohesion.
- Existing settings such as Aggression, Bravery, Self Preservation, Herding, Caution/Fall Shame, Favor Higher TMM, Anti-Crowding, Exclusive Herding, and Experimental are preserved or explicitly mapped.
- Focused tests cover suicide isolation and at least one favorable exception.

## Open Questions

- Is the best cheap combat-power proxy expected damage, max weapon damage at relevant range, BV/durability weighting, or a combined score?
- How should already-moved enemies and friends be weighted compared with units that still have movement available?
