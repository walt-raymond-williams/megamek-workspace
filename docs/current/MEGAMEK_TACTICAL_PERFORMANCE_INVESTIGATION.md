# MegaMek Tactical Performance Investigation

This note tracks the source-backed investigation into user-observed MegaMek tactical lag and potential low-risk speedups.

## Current Status

- `Confirmed by user`: MegaMek can become very laggy and unresponsive during gameplay, especially when there are many units on the board and AI/Princess controls units.
- `Confirmed by user`: A current concrete reproduction is the firing phase: switching between firing units is slow, and selecting or targeting enemies is slow.
- `Confirmed from source`: Tactical board rendering is centered around `external/src/megamek/megamek/src/megamek/client/ui/clientGUI/boardview/BoardView.java` and `BoardViewPanel.java`.
- `Confirmed from source`: `BoardView` schedules a redraw worker through `TimerSingleton` every 20 ms via `SwingUtilities.invokeLater(...)`. Many UI and game events also request board repaint or redraw work.
- `Inferred`: if the Swing event dispatch thread is already busy, uncoalesced redraw worker scheduling may contribute to perceived unresponsiveness because queued redraw work can compete with user input and game/UI updates.
- `Confirmed from source`: Princess movement and AI decision cost is larger algorithmic territory around `PathEnumerator`, `PathRanker`, `BasicPathRanker`, `Princess#getMovePathsAndSetNecessaryTargets(...)`, and related path/fire-control classes. Treat this as profiling-first territory rather than a safe quick edit.

## Current Local Experiment

An uncommitted experimental source edit exists in `external/src/megamek`:

- File: `external/src/megamek/megamek/src/megamek/client/ui/clientGUI/boardview/BoardView.java`
- Experiment: coalesce pending redraw worker scheduling with an `AtomicBoolean` so at most one redraw worker is queued on the Swing event dispatch thread at once.
- Status: not approved as an implementation change; inspect, keep, revise, or discard only after the user explicitly resumes implementation work.
- Verification: `.\gradlew.bat --no-daemon :megamek:compileJava` was started but interrupted before a verified result.

## Candidate Investigation Areas

1. Firing-phase unit switching and target selection latency. This is tracked as GitHub issue `#81` and should be investigated before the broader sweep.
2. `BoardView` redraw scheduling and repaint coalescing.
3. `BoardView` sprite and hex drawing paths, especially large visible boards and isometric mode.
4. Minimap redraw behavior during large battles.
5. Princess path enumeration and ranking, including expensive per-entity loops.
6. ECM/FOV calculations and repeated full-entity scans during board redraw or selection changes.

## Firing Phase Focus

Issue `#81` should start by mapping the source path for unit switching and target selection in these files:

- `external/src/megamek/megamek/src/megamek/client/ui/panels/phaseDisplay/FiringDisplay.java`
- `external/src/megamek/megamek/src/megamek/client/ui/clientGUI/boardview/BoardView.java`
- `external/src/megamek/megamek/src/megamek/client/ui/clientGUI/boardview/LOSModifierCalculator.java`
- `external/src/megamek/megamek/src/megamek/client/ui/SharedUtility.java`
- `external/src/megamek/megamek/src/megamek/client/ui/clientGUI/boardview/spriteHandler/FiringSolutionSpriteHandler.java`

Look for repeated full-board or full-entity scans, repeated to-hit/LOS recalculation, repeated `redrawEntity`/`redrawAllEntities`/`repaint` calls, and target-list or firing-solution rebuilds triggered on each unit or target change.

## Open Questions

- Which gameplay state best reproduces the lag: movement phase, firing phase, AI thinking, animation playback, minimap interaction, or general board panning/selection?
- During firing phase, does the lag happen on unit selection, keyboard next-unit cycling, target hover, target click, weapon toggling, or all of these?
- Does the installed `0.51.00` build show the same lag with isometric rendering, minimap, FOV, shadows, labels, or animations disabled?
- Do we need a user-provided saved game/scenario with many units to profile a representative case?
