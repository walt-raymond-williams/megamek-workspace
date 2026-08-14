# Agent Handoff

## Issue

- GitHub issue: `#108`
- Roadmap entry: `Epic: Improve Princess tactical cohesion and anti-bait movement`
- Priority: `High`

## Goal

Add automated and live-game validation coverage for the Princess tactical cohesion initiative so behavior changes can be evaluated against both defensive and offensive requirements.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/MEGAMEK_PRINCESS_AI_SOURCE_AUDIT.md`
- `docs/current/PRINCESS_TACTICAL_COHESION_TRACKING.md`
- Child handoffs for `#103` through `#107`.

Source facts to verify:

- Existing Princess unit tests live under `external/src/megamek/megamek/unittests/megamek/client/bot/princess`.
- Known relevant tests include `BasicPathRankerTest`, `FireControlTest`, `FiringPlanTest`, `PrincessTest`, and smaller helper tests.
- Scenario-runner infrastructure exists, including `external/src/megamek/megamek/src/megamek/utilities/ScenarioGameRunner.java`.
- Test scenario resources include `external/src/megamek/megamek/testresources/data/scenarios/test_setups/BotvBot.mms`.

## Expected Output

- Focused automated tests for the new scoring/state behavior.
- Scenario-style tests where practical and stable.
- A live-game smoke checklist documenting scenario setup, expected observations, logging knobs, and pass/fail criteria.
- Exact verification commands and results.

## Files And Areas

Likely files to read or edit:

- `external/src/megamek/megamek/unittests/megamek/client/bot/princess/`
- `external/src/megamek/megamek/testresources/data/scenarios/`
- `external/src/megamek/megamek/src/megamek/utilities/ScenarioGameRunner.java`
- Source files changed by `#103` through `#107`
- Workspace docs if live smoke or validation strategy needs durable notes.

## Commands

Start with harness discovery:

```powershell
rg -n "BasicPathRankerTest|UtilityPathRanker|ScenarioGameRunner|BotvBot|princesssettings|PrincessTest" external/src/megamek/megamek/unittests external/src/megamek/megamek/testresources external/src/megamek/megamek/src -g "*.java" -g "*.mms"
```

Then run the narrow and broader verification commands selected from the MegaMek Gradle setup.

## Constraints

- Do not rely only on subjective live-game observation; add automated checks where the source architecture permits.
- Keep scenario tests deterministic enough for CI/local reruns.
- Do not overfit tests to one exact final hex if the useful behavior is relative path ranking or utility ordering.
- Preserve evidence labels in any workspace docs.

## Acceptance Criteria

- Baited single-unit advance is covered: Princess should not automatically send one unit into concentrated enemy fire after a small enemy feint.
- Supported advance is covered: several Princess units can advance while retaining meaningful support.
- Legitimate tactical opportunity is covered: Princess can exploit a crippled, isolated, or otherwise favorable target.
- Long-term stalemate is covered: Princess eventually creates action, preferably by supported/coordinated advance.
- Fast flanker is covered: a mobile unit can take a legitimate flank without being wrongly treated as unsupported solely because slower units are elsewhere.
- Suicide isolation is covered: modest offensive gain that exposes one unit to several enemies ranks substantially below coherent alternatives.
- Existing behavior regression is covered: Princess presets/settings continue to produce meaningfully different movement personalities.
- Live smoke checklist records setup, logs/diagnostics to inspect, expected observations, and failure examples.

## Open Questions

- Which validations should be pure unit tests versus scenario-runner tests?
- Can BotLogger TSV output be asserted in tests, or should diagnostics be verified through `RankedPath#getScores()` directly?
