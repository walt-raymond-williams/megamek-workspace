# Agent Handoff: Investigate Firing Phase Targeting Lag

## Issue

- GitHub issue: `#81`
- Roadmap entry: `Investigate MegaMek tactical lag and low-risk performance wins`
- Priority: `High`

## Goal

Investigate the current user-observed MegaMek firing-phase bug: switching between firing units and selecting/targeting enemies is very slow and unresponsive.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/MEGAMEK_TACTICAL_PERFORMANCE_INVESTIGATION.md`

## Expected Output

- Source-backed explanation of what runs when switching firing units and when selecting, hovering, or confirming targets.
- Identification of likely repeated expensive work, especially nested entity/action scans, repeated LOS/to-hit calculations, repeated board redraws, and avoidable target-list or firing-solution rebuilds.
- A prioritized recommendation for one or more low-risk fixes.
- No implementation unless the user explicitly approves source changes.

## Completion

- Completed on `2026-06-29` as an investigation-only pass.
- Findings are recorded in `docs/current/MEGAMEK_TACTICAL_PERFORMANCE_INVESTIGATION.md`.
- Main source-backed suspect is the default-on firing-solution overlay path: `FiringDisplay.selectEntity(...)` and `ClientGUI.updateFiringArc(...)` can route to `FiringSolutionSpriteHandler.showFiringSolutions(...)`, which scans all entities and calls `WeaponAttackAction.toHit(...)` for viable targets.
- Recommended first live check: disable View > Firing Solutions and reproduce the firing-phase unit switching and target selection lag.
- Recommended first source fix, if approved later: remove duplicate/debounce firing-solution rebuilds during unit selection before broader performance changes.

## Files And Areas

Likely files to inspect first:

- `external/src/megamek/megamek/src/megamek/client/ui/panels/phaseDisplay/FiringDisplay.java`
- `external/src/megamek/megamek/src/megamek/client/ui/clientGUI/boardview/BoardView.java`
- `external/src/megamek/megamek/src/megamek/client/ui/clientGUI/boardview/LOSModifierCalculator.java`
- `external/src/megamek/megamek/src/megamek/client/ui/SharedUtility.java`
- `external/src/megamek/megamek/src/megamek/client/ui/clientGUI/MapMenu.java`
- `external/src/megamek/megamek/src/megamek/client/ui/clientGUI/boardview/spriteHandler/FiringSolutionSpriteHandler.java`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
git -C external/src/megamek status --short --branch
rg -n "redrawEntity|redrawAllEntities|repaint|target|Target|toHit|LOS|FiringSolution" external/src/megamek/megamek/src/megamek/client/ui/panels/phaseDisplay/FiringDisplay.java
rg -n "getEntitiesVector|getActionsVector|LOSModifierCalculator|computeAllEntitiesECMInfo|redrawEntity|redrawAllEntities" external/src/megamek/megamek/src/megamek/client/ui -g "*.java"
```

## Constraints

- Do not overwrite campaign saves.
- Preserve unrelated workspace changes.
- The current uncommitted `BoardView.java` redraw-coalescing source edit is an experiment from the broader issue `#80`, not an approved fix for this bug.
- Keep any source changes in `external/src/megamek`, not in the workspace repo.

## Acceptance Criteria

- The firing-phase unit-switch and targeting path is mapped to concrete source methods.
- Findings separate confirmed source behavior from inference.
- The next recommended source change, if any, is narrow and testable.
- Any required user reproduction detail is stated concretely.

## Open Questions

- Does the lag happen on click-to-select unit, keyboard next-unit cycling, target hover, target click, weapon toggling, or all of these?
- Does disabling minimap, isometric mode, FOV, shadows, labels, or firing-solution overlays materially change the lag?
- Is the lag specific to a high-unit-count save/scenario that should be copied under `analysis/tmp/` for profiling?
