# Agent Handoff

## Issue

- GitHub issue: `#101`
- Roadmap entry: `Remove RL bomb pods from automatic aerospace bot loadouts`
- Priority: `High`

## Goal

Modify the local MegaMek/MekHQ source tree so automatic AI/bot/OpFor aerospace bomb loadout generation never assigns Rocket Launcher bomb pods, while preserving manual/player use, equipment definitions, construction rules, and compatibility with existing scenarios that already contain RL pods.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/KNOWN_COMMANDS.md`
- `docs/current/MEGAMEK_PROJECT_PROFILE.md`

Task-specific starting points:

- Confirm the active implementation at `external/src/megamek/megamek/src/megamek/client/generator/TeamLoadOutGenerator.java`.
- Search both `external/src/megamek` and `external/src/mekhq` for automatic OpFor, bot, StratCon, scenario, or campaign bomb-loadout logic that explicitly adds Rocket Launcher bomb pods.
- Inspect Gradle files and project documentation before choosing test/build/package commands.

## Expected Output

- Focused source changes that remove `BombTypeEnum.RL` only from automatic generated bomb-loadout selection.
- Tests covering ground-map auto loadouts, aerospace-battle auto loadouts, early-era no-valid-choice behavior, legal non-RL automatic assignment, and manually configured RL bomb-pod loadability.
- Workspace documentation update recording the source commit, exact behavior change, verification commands/results, rebuilt launchable package path, and any unrelated failures or blockers.
- A coherent commit in the affected source checkout, plus a workspace commit updating tracking/docs as needed.

## Files And Areas

Likely files to read or edit:

- `external/src/megamek/megamek/src/megamek/client/generator/TeamLoadOutGenerator.java`
- MegaMek tests near `TeamLoadOutGenerator` or bot loadout generation.
- Any MekHQ campaign/StratCon/OpFor generator code found by source search that independently adds RL bomb pods to automatic loadouts.
- Workspace docs that record durable source-change results, likely `docs/current/ROADMAP.md`, `docs/current/TASKS.md`, `docs/current/KNOWN_COMMANDS.md`, or a focused note under `docs/current/` if the change teaches reusable source/build context.

Do not edit:

- Bomb or weapon definitions solely to hide RL pods.
- Player/manual loadout selection paths.
- Scenario loading or serialization support for existing RL bomb pods.
- Campaign save files.

## Commands

Start with source and worktree checks:

```powershell
git status --short --branch
git -C external/src/megamek status --short --branch
git -C external/src/mekhq status --short --branch
rg "BombTypeEnum\.RL|Rocket Launcher|TeamLoadOutGenerator|bomb load" external/src/megamek external/src/mekhq
```

Determine exact verification commands from project files before running them. The issue requires:

- relevant focused tests first
- normal project verification/build tasks for this checkout
- build of the distributable MegaMek/MekHQ package used to launch the application

## Constraints

- Do not include unrelated user changes.
- Preserve the repository's existing code style.
- Do not make broad formatting changes or refactors.
- Remove RL only from automatic generated loadouts.
- If no valid automatic bomb choices remain for a unit/year, leave the unit without external ordnance instead of throwing from empty random selection.
- Commit completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- The active `TeamLoadOutGenerator` implementation is located and confirmed.
- `BombTypeEnum.RL` is removed from ground-map aerospace automatic bomb choices.
- `BombTypeEnum.RL` is removed from air-to-air aerospace automatic bomb choices.
- Special-case RL technology-date bypass logic is removed so every bomb type must pass `BombType.get(typeName).isAvailableIn(year, false)`.
- The full MegaMek/MekHQ local source workspace is searched for other automatic RL bomb-pod additions, and any automatic generated-loadout additions are removed.
- Manual/player RL bomb-pod selection remains valid.
- Existing scenarios with RL bomb pods can still load.
- Empty valid bomb-choice lists do not crash generation.
- Focused tests prove the required behavior.
- Relevant tests and normal build/distribution verification are run, with exact commands and results reported.
- The final report lists every changed file, behavior change, verification commands/results, rebuilt package path, and any unrelated failures.

## Open Questions

- Which Gradle task produces the launchable/distributable MegaMek/MekHQ package for this checkout?
- Are there existing tests for `TeamLoadOutGenerator`, or should the focused coverage be added around the nearest existing generator/bot-loadout test harness?
- Does MekHQ add any independent StratCon/campaign aerospace ordnance after MegaMek's team loadout generator runs?
