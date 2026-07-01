# Agent Handoff: Investigate MegaMek Tactical Performance

## Issue

- GitHub issue: `#80`
- Roadmap entry: `Investigate MegaMek tactical lag and low-risk performance wins`
- Priority: `High`

## Goal

Investigate user-observed MegaMek tactical lag and identify low-risk performance improvements for gameplay, especially large battles with many units and Princess/bot control.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/MEGAMEK_TACTICAL_PERFORMANCE_INVESTIGATION.md`

## Expected Output

- Source-backed findings that separate UI/EDT responsiveness from Princess AI/pathing cost.
- A prioritized list of candidate low-risk fixes.
- Treat GitHub issue `#81` as the first focused investigation before broader `#80` work: firing-phase unit switching and target selection latency.
- If implementation is explicitly approved later, a narrow source patch in `external/src/megamek` with verification.
- If realistic profiling data is needed, a clear request for a representative save/scenario and exact reproduction steps.

## Files And Areas

Likely files to read or edit:

- `external/src/megamek/megamek/src/megamek/client/ui/clientGUI/boardview/BoardView.java`
- `external/src/megamek/megamek/src/megamek/client/ui/clientGUI/boardview/BoardViewPanel.java`
- `external/src/megamek/megamek/src/megamek/client/ui/panels/phaseDisplay/FiringDisplay.java`
- `external/src/megamek/megamek/src/megamek/client/ui/clientGUI/boardview/LOSModifierCalculator.java`
- `external/src/megamek/megamek/src/megamek/client/ui/clientGUI/boardview/spriteHandler/FiringSolutionSpriteHandler.java`
- `external/src/megamek/megamek/src/megamek/client/ui/dialogs/minimap/MinimapPanel.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/Princess.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/PathEnumerator.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/PathRanker.java`
- `external/src/megamek/megamek/src/megamek/client/bot/princess/BasicPathRanker.java`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
git -C external/src/megamek status --short --branch
git -C external/src/megamek diff -- megamek/src/megamek/client/ui/clientGUI/boardview/BoardView.java
cd external/src/megamek
.\gradlew.bat --no-daemon :megamek:compileJava
```

## Constraints

- Do not overwrite campaign saves.
- Preserve unrelated workspace changes.
- Do not commit the current experimental `BoardView.java` source edit unless the user explicitly approves implementation work.
- Keep source changes in `external/src/megamek`, not in the workspace repo.
- If a source change teaches a durable control/performance fact, update `docs/current/MEGAMEK_TACTICAL_PERFORMANCE_INVESTIGATION.md`.

## Acceptance Criteria

- Issue `#80` has source-backed findings and clear next steps.
- Candidate fixes are labeled as confirmed, inferred, or unknown.
- Any implemented fix has an attempted or completed compile/test result recorded.
- Any required user-provided reproduction data is stated concretely.

## Open Questions

- Which exact large-unit scenario or save reproduces the lag best?
- Is the biggest pain during Princess thinking, board panning/selection, movement animation, firing resolution, or all of the above?
- Should the redraw coalescing experiment be kept, revised, or discarded after review?
