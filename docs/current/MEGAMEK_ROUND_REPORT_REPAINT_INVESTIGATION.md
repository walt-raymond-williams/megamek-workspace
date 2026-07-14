# MegaMek Round Report Repaint Investigation

## Scope

GitHub issue: `#93`

Investigation date: `2026-07-14`

User request: investigation only. Do not make source fixes until the findings are reviewed.

## Environment And Version Facts

- `Confirmed locally`: installed runnable suite is `external/installs/MekHQ-0.51.00`; `MegaMek.jar` and `MegaMek.exe` are dated `2026-06-06`.
- `Confirmed locally`: shell Java is Eclipse Temurin `21.0.11`.
- `Confirmed from source`: local MegaMek source checkout is `external/src/megamek`, branch `main`, commit `6993568676 Add End-Phase activation toggle for vehicle minesweepers (#8350)`.
- `Confirmed from source`: source `megamek/resources/Version.properties` reports `0.51.01`.
- `Confirmed locally`: local MegaMek source checkout has an existing uncommitted `BoardView.java` redraw-worker coalescing experiment from prior performance investigation. This investigation inspected but did not modify or revert it.
- `Confirmed from source`: `v0.51.0` tag exists. The core Round Report files inspected here have the same relevant behavior in `v0.51.0` and current `main`; current `main` has additional `BoardView.java` changes unrelated to Round Report ownership.

## Observed Symptom

- `Observed in user screenshot`: MegaMek window title was `Sharpe's Strikers - Round 7 - Firing Report phase - MegaMek`.
- `Observed in user screenshot`: foreground `Round Report` window was open with tabs `Round 1` through `Round 7` and `Phase`.
- `Observed in user screenshot`: report entries included `Weapons fire for Shadow Hawk SHD-2H`, attacks on `Flashman FLS-7K`, and `Weapons fire for Light Shredder Gun Emplacement`.
- `Observed in user screenshot`: similar report text fragments and unit-icon content appeared on or over the tactical board behind/around the foreground report window.

## Source Map

- `Confirmed from source`: the floating Round Report window is `MiniReportDisplayDialog`, constructed as `super(frame, "", false)`, so it is a non-modal `JDialog`.
  - Source: `external/src/megamek/megamek/src/megamek/client/ui/dialogs/miniReport/MiniReportDisplayDialog.java`
- `Confirmed from source`: `ClientGUI#setMiniReportDisplayDialog(...)` calls `miniReportDisplayDialog.setFocusableWindowState(false)`.
  - Source: `external/src/megamek/megamek/src/megamek/client/ui/clientGUI/ClientGUI.java`
  - Blame: this was introduced in commit `e54781c183c` (`chat retain focus, blinking cursor`), so simply removing it may regress chat/board focus behavior.
- `Confirmed from source`: the Round Report panel is `MiniReportDisplayPanel`. It builds tabs for prior rounds and the current phase, and each tab wraps report HTML in a `JTextPane` inside a `JScrollPane`.
  - Source: `external/src/megamek/megamek/src/megamek/client/ui/dialogs/miniReport/MiniReportDisplayPanel.java`
- `Confirmed from source`: `MiniReportDisplayPanel#loadHtmlScrollPane(...)` uses `BASE64ToolKit`, calls `ta.setText("<div class='report'>" + t + "</div>")`, marks the text pane non-editable, and calls `ta.setOpaque(false)` before returning `new JScrollPane(ta)`.
- `Confirmed from source`: Round Report unit images are expected report content. `Client#receiveReport(...)` replaces report `<span id='...'></span>` markers with cached `<img src='data:image/png;base64,...'>` tags when `MINI_ROUND_REPORT_SPRITES` is enabled.
  - Source: `external/src/megamek/megamek/src/megamek/client/Client.java`
- `Confirmed from source`: `MINI_ROUND_REPORT_SPRITES` defaults to `true`; `MINI_REPORT_ENABLED` defaults to `true`; `MINI_REPORT_LOCATION` defaults to `0`, which means floating dialog rather than docked panel.
  - Source: `external/src/megamek/megamek/src/megamek/client/ui/clientGUI/GUIPreferences.java`
- `Confirmed from source`: the tactical map is painted through `BoardViewPanel#paintComponent(Graphics)`, which does not call `super.paintComponent(g)` and delegates directly to `boardView.draw(g)`.
  - Source: `external/src/megamek/megamek/src/megamek/client/ui/clientGUI/boardview/BoardViewPanel.java`
- `Confirmed from source`: `BoardView#draw(Graphics)` fills the visible board area with either the board background image or the Metal theme control color, then draws hexes, sprites, paths, attack lines, and overlays. No report text, report HTML, `JTextPane`, `BASE64ToolKit`, `roundReport`, or `phaseReport` drawing path was found under `boardview`.
  - Source: `external/src/megamek/megamek/src/megamek/client/ui/clientGUI/boardview/BoardView.java`

## Current Findings

- `Confirmed from source`: there is no source evidence that `BoardView` intentionally draws Round Report content onto the map.
- `Confirmed from source`: the duplicated content matches what the Round Report renderer itself can produce: HTML report text plus embedded base64 unit sprites.
- `Confirmed from source`: the floating Round Report is a non-modal, non-focusable companion window, not a normal modal dialog. Related upstream issue `MegaMek/megamek#8279` reports that the floating report window cannot receive arrow-key focus on Win 11 / JDK 21 in 51.0.
- `Confirmed from upstream issue search`: no exact upstream issue was found for "Round Report content duplicated onto tactical board" using the obvious search terms.
- `Related upstream issue`: `MegaMek/megamek#6032` described graphical artifacts in the Round Report window. The discussion points to font rendering; changing away from the Aakar font removed those artifacts for the reporter. This is adjacent but not an exact match for content appearing on the map.
- `Related upstream issue`: `MegaMek/megamek#5220` reports the Round Report appearing above other UI/app windows on macOS. This is also adjacent because it concerns floating-window stacking/composition, not report data.

## Leading Hypotheses

1. `Inferred`: the most likely class of bug is floating-window composition or repaint exposure, not board logic. The report window is non-modal and non-focusable, and the screenshot content matches pixels produced by `MiniReportDisplayPanel`.
2. `Inferred`: the non-opaque report `JTextPane` is a plausible contributor or amplifier. If parent/viewport repainting is imperfect, stale report text and base64 unit images may remain visible or appear to bleed through. This is stronger if the artifact is inside the report window bounds or follows report scrolling.
3. `Inferred`: `BoardViewPanel#paintComponent(...)` not calling `super.paintComponent(g)` is a plausible secondary candidate only if exposed board regions are not fully repainted after the floating window moves or hides. `BoardView#draw(...)` appears to fill the visible area, so this is not the first suspect without reproduction.
4. `Inferred`: report font rendering can cause artifacts, but the upstream font case looked like vertical streaks inside the report window, not duplicated report panes on the tactical board. Treat font as a reproduction variable, not as the current root cause.

## Reproduction Matrix

Run these against the installed `0.51.00` suite first, because that is the likely user-observed runtime:

1. Record OS, display scaling, Java version, MegaMek suite version, report font, GUI scale, high-quality/high-performance graphics settings, and whether Round Report is floating or docked.
2. Enter a firing report phase with enough report text and unit sprites to require tabs/scrolling.
3. With Round Report floating, open it over the tactical map, scroll within it, drag it across the board, switch tabs, minimize/restore or hide/show it, and observe whether report pixels remain on the board.
4. Repeat with Round Report docked using `Switch Location`. If the artifact disappears only when docked, the floating `JDialog` path is strongly implicated.
5. Repeat with report sprites disabled. If unit-icon duplication disappears but text remains, report HTML is confirmed as the visual source.
6. Repeat with a different report font, especially a fixed-width font. If artifacts disappear, compare against upstream `#6032`.
7. If safe source experiments are approved later, test one variable at a time:
   - make the Round Report dialog focusable again or remove `setFocusableWindowState(false)`;
   - make the report text pane and/or scroll pane viewport opaque with an explicit background;
   - call `super.paintComponent(g)` in `BoardViewPanel#paintComponent(...)`;
   - force `boardPanel.repaint()` after showing/hiding/moving the floating Round Report.

## Fix Candidates To Review Before Implementation

- Candidate A: make floating Round Report focusable or conditionally focusable.
  - Potential upside: directly addresses the current non-focusable floating report behavior and may also help upstream `#8279`.
  - Risk: commit `e54781c183c` added non-focusability for chat focus/cursor behavior, so this could regress keyboard workflow.
- Candidate B: make report panes opaque and set explicit backgrounds on the `JTextPane`/viewport/scroll pane.
  - Potential upside: narrow, report-local rendering change; likely low risk if visual theme remains acceptable.
  - Risk: may not help if the artifact is OS-level window composition outside the dialog contents.
- Candidate C: harden board repaint clearing by calling `super.paintComponent(g)` or explicitly filling the clip in `BoardViewPanel`.
  - Potential upside: improves Swing paint contract clarity.
  - Risk: board drawing is performance-sensitive; current `BoardView#draw(...)` already fills visible area, so this should be tested carefully.

## Current Recommendation

Do not patch yet. First reproduce with the matrix above and determine whether the artifact follows the floating Round Report window, the report text pane opacity/font, or board repaint exposure. If user review approves a source experiment before full reproduction, start with Candidate B because it is the narrowest report-local change and does not intentionally alter chat focus behavior.

