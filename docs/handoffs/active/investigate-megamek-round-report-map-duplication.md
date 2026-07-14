# Agent Handoff

## Issue

- GitHub issue: `#93`
- Roadmap entry: `Investigate MegaMek round report repaint duplication on tactical map`
- Priority: `High`

## Goal

Investigate and, if feasible, fix the MegaMek UI/rendering bug where the floating Round Report window contents appear duplicated onto the underlying tactical board/map.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/KNOWN_COMMANDS.md`
- GitHub issue `#93`

Initial observed symptom:

- `Observed in user screenshot`: MegaMek window title was `Sharpe's Strikers - Round 7 - Firing Report phase - MegaMek`.
- `Observed in user screenshot`: A foreground `Round Report` window was open with tabs `Round 1` through `Round 7` and `Phase`.
- `Observed in user screenshot`: Report entries included `Weapons fire for Shadow Hawk SHD-2H`, attacks on `Flashman FLS-7K`, and `Weapons fire for Light Shredder Gun Emplacement`.
- `Observed in user screenshot`: Similar report text fragments and unit-icon content appeared behind the modal on the tactical board/map layer, especially along the left side and lower map area.
- `Inference`: This looks less like intentional transparency and more like stale or duplicated painting of report UI content onto the board canvas or an overlay/backing buffer.

Do not rely on the original temp screenshot path remaining available. Reproduce or capture a fresh disposable screenshot if visual evidence is needed.

## Expected Output

- Reproduction notes with exact MegaMek version/source branch, OS, JDK, display scaling, theme/look-and-feel settings if relevant, and steps to trigger the artifact.
- Source-backed map of the relevant report modal, report rendering component, board view repaint pipeline, Swing layered pane/dialog behavior, and any custom double-buffer/image-cache behavior.
- A minimal MegaMek source fix if the cause is confirmed and the change is low-risk.
- Verification notes from relevant compile/test commands, or a clear blocker if verification cannot run.
- Manual smoke checklist for opening the Round Report during firing report phase and confirming the board background is not contaminated by modal report content.
- Workspace documentation update if the investigation teaches durable MegaMek UI/rendering context.

Investigation note:

- `2026-07-14`: Source-only investigation findings are recorded in `docs/current/MEGAMEK_ROUND_REPORT_REPAINT_INVESTIGATION.md`. Per user request, no source fix has been made yet; next step is user review and approval of a reproduction/source-experiment path.

## Files And Areas

Likely files or areas to inspect:

- `external/src/megamek`
- MegaMek client UI classes around round reports, report dialogs, report panes, and phase reports.
- Board view rendering, repaint, overlay, sprite, and double-buffer code.
- Swing window/dialog setup, layered panes, glass panes, transparency, and custom look-and-feel handling.

Start with search rather than assumptions:

```powershell
rg -n "Round Report|Firing Report|Report" external/src/megamek
rg -n "BoardView|paintComponent|repaint|doubleBuffer|buffer|LayeredPane|glassPane" external/src/megamek/megamek/src
```

## Commands

Useful commands or checks:

```powershell
git status --short --branch
git -C external/src/megamek status --short --branch
java -version
javac -version
```

Check `docs/current/KNOWN_COMMANDS.md` for the currently verified MegaMek build/test commands before running source verification.

## Constraints

- Do not overwrite campaign saves.
- Use disposable scenarios/saves for reproduction.
- Keep source changes in `external/src/megamek`, not in this workspace repo.
- Before modifying source, follow `docs/current/SOURCE_CHANGE_WORKFLOW.md`.
- Separate facts from hypotheses. Label runtime observations, source-confirmed behavior, and inference explicitly.
- Do not include unrelated user changes in workspace commits.

## Acceptance Criteria

- Relevant source files/classes for report modal rendering and board repainting are identified.
- Root cause is confirmed, or the leading hypotheses and next debug steps are clearly recorded.
- If fixed, the MegaMek source change is narrowly scoped and verified with relevant local command(s).
- If not fixed, the blocker and next reproduction/debug step are recorded.
- Roadmap, handoff, and any durable docs stay current.
- Completed workspace changes are committed and pushed before stopping.

## Open Questions

- Is the duplicate content caused by the report dialog component itself, a shared report-rendering component, board view repaint/double-buffer behavior, translucent/modal window composition, stale image caching, or another overlay layer?
- Does the bug reproduce only with specific display scaling, Java version, UI theme, graphics pipeline, map size, or report content length?
- Is this related to the earlier firing-phase redraw/performance investigation around board repaint churn, or an independent modal/window composition issue?
- Should the first approved experiment be report-local opacity/background hardening, focusability/window behavior, or board repaint clearing?
