# MekHQ Advance Day GUI Control Seam Spike

## Purpose

GitHub issue `#34` asked whether a locally modified MekHQ build can expose a safe source-level control seam for exactly one Advance Day action without coordinate clicking, direct campaign-save mutation, or reimplementing MekHQ campaign logic.

## Recommendation

`Confirmed from source`: a local source-level GUI control seam is viable now as an in-process MekHQ command that calls the same campaign path the UI uses, provided it stays inside the loaded GUI application and uses strict guardrails.

The seam should not be a detached headless helper yet. `CampaignNewDayManager#newDay()` still depends on GUI state, GUI event subscribers, and Swing dialogs. The safest next implementation is a small local-only endpoint or command object in the running MekHQ process that:

- verifies a campaign is loaded
- verifies expected campaign id/name/date when the caller provides them
- invokes exactly one `Campaign#newDay()` on the Swing event dispatch thread
- treats a `false` return as blocked or canceled
- records whether the date advanced by exactly one day
- optionally saves to an explicit disposable path after confirmed success
- refuses prompt auto-answering

This should proceed as a narrow prototype issue against copied/disposable saves. A broader headless command layer would be cleaner long-term, but it would require refactoring GUI dependencies out of daily processing and is more work than the first useful seam needs.

## Source Path

`Confirmed from source`: the single-day UI path is:

1. `mekhq.gui.CampaignGUI#initTopPanel()` creates `AdvanceTimePanel` with `getCampaignController()::advanceDay`.
2. `mekhq.gui.view.AdvanceTimePanel` wires the Advance Day button listener. The listener disables the one-day and multi-day buttons, schedules work with `SwingUtilities.invokeLater(...)`, sets the wait cursor, and calls the supplied `advanceDay` runnable.
3. `mekhq.campaign.CampaignController#advanceDay()` calls `getLocalCampaign().newDay()` and ignores the boolean result.
4. `mekhq.campaign.Campaign#newDay()` constructs `CampaignNewDayManager` and returns `manager.newDay()`.
5. `mekhq.campaign.CampaignNewDayManager#newDay()` performs the daily lifecycle and returns `true` only after `MekHQ.triggerEvent(new NewDayEvent(campaign))`.

`Confirmed from source`: the multi-day dialog path also calls the same `Campaign#newDay()` method in a loop from `mekhq.gui.dialog.AdvanceDaysDialog`, stopping when `campaign.newDay()` returns `false`.

## GUI Dependencies Encountered

`Confirmed from source`: `CampaignNewDayManager#newDay()` is not pure campaign logic. It immediately reaches into the GUI:

- `campaign.getApp().getCampaigngui().getCommandCenterTab()` clears daily report nags before processing.
- It triggers `DayEndingEvent`; `CampaignGUI#handleDayEnding(...)` subscribes to that event and can display nag/blocker dialogs before allowing the day to end.
- It triggers `NewDayEvent`; `CampaignGUI#handleNewDay(...)` refreshes the window title, campaign controls, parts availability, market labels, and all tabs.
- It may create Swing dialogs directly, including `ImmersiveDialogSimple`, `ImmersiveDialogNotification`, `JOptionPane`, `PerformBatchall`, and rare personnel market prompts.
- `AdvanceTimePanel` and `AdvanceDaysDialog` subscribe to `NewDayEvent` or `ReportEvent` for UI synchronization.

This means the seam should call the loaded GUI application's campaign, not load a campaign in a separate process and call `newDay()` without a real `CampaignGUI`.

## Blocking And Prompt Detection

`Confirmed from source`: MekHQ already has a pre-mutation cancellation point. `CampaignNewDayManager#newDay()` triggers `new DayEndingEvent(campaign)` before autosave and before changing the campaign date. If any subscriber cancels it, `newDay()` returns `false`.

`Confirmed from source`: `CampaignGUI#handleDayEnding(...)` cancels the event for daily nags, overdue loans, invalid faction/date combinations, due scenarios, and random retirement/defection choices. Those blockers are exactly the kind of user-decision prompts the seam should surface rather than answer.

`Confirmed from source`: `DayEndingEvent` is cancellable through MegaMek's `MMEvent`/`EventBus` flow; `MekHQ.triggerEvent(...)` returns `true` when a cancellable event was canceled.

`Inferred`: a source seam can detect many blockers cleanly by calling `Campaign#newDay()` and checking both its boolean result and the before/after date. However, once processing is past `DayEndingEvent`, some later Swing dialogs are notifications or optional choices rather than uniform cancellable blockers. For a first prototype, do not auto-dismiss or answer them. Detect visible modal/dialog state if needed and return "blocked: user prompt visible" to the caller.

## Save-After-Success Safety

`Confirmed from source`: manual saving is available through `CampaignGUI.saveCampaign(JFrame, Campaign, File, boolean)`. It appends `.cpnx` when needed, backs up an existing target, gzip-compresses only `.gz` targets, writes `Campaign#writeToXML(...)`, and restores the backup after save failure.

`Confirmed from source`: day-advance autosave happens before the date change, based on the previous day's campaign state, through `campaign.getAutosaveService().requestDayAdvanceAutosave(campaign)`.

For a control seam, save-after-success should be opt-in and explicit:

- default to no save
- require an explicit output path under a disposable/copy location
- verify `Campaign#newDay()` returned `true`
- verify the date advanced from expected date to expected date plus one day
- call the existing save path rather than writing XML directly
- report autosave behavior separately so the caller understands that MekHQ may already have written a pre-advance autosave depending on user options

## Risks And Brittleness

- `Confirmed from source`: the current `CampaignController#advanceDay()` returns `void`, so a caller using that method cannot tell success from cancellation. A seam should wrap `Campaign#newDay()` directly or add a new method that returns a structured result.
- `Confirmed from source`: `CampaignNewDayManager` assumes a live `CampaignGUI`; a headless or jar-only helper is likely to miss GUI subscribers and may fail when it reaches `campaign.getApp().getCampaigngui()`.
- `Confirmed from source`: daily processing has side effects before and after the date change, including autosave, reports, markets, finances, personnel, units, StratCon/AtB, and event triggers.
- `Confirmed from source`: prompts are not represented by one uniform prompt API. Some are cancellable blockers before the day advances; others are dialogs created during processing.
- `Inferred`: running off the Swing event dispatch thread risks inconsistent UI state, because the existing button path uses `SwingUtilities.invokeLater(...)` and downstream handlers refresh Swing components.
- `Unknown`: live prototype behavior against a realistic copied #23 campaign remains untested because this issue was a source spike only.

## Prototype Shape

Recommended follow-up: create a small local-only MekHQ source issue for an "advance one day control seam" with this contract:

Input:

- expected campaign id or name
- expected date
- save mode: `none` or explicit disposable output file

Output:

- `status`: `advanced`, `blocked`, `failed`, or `refused`
- campaign id/name
- date before and after
- whether `Campaign#newDay()` returned true
- save path and save result when requested
- blocker/prompt summary when detectable

Implementation notes:

- expose the command from the running app after a campaign is loaded
- dispatch on the Swing event dispatch thread
- call `Campaign#newDay()`, not button coordinates and not raw save mutation
- do not call `CampaignController#advanceDay()` unless it is first changed to return the `newDay()` result
- never advance more than one day per call
- do not answer dialogs automatically

## Effect On Current Tickets

`Confirmed from source`: this spike does not directly complete issues `#10`, `#13`, or `#17`, because those concern Resolve Manually battle-result import and roster add/remove workflows rather than day advancement.

`Inferred`: it does validate the broader strategy for those blocked tickets. If the Advance Day prototype succeeds, the same pattern should be considered for future source-level control seams around Resolve Manually and GM roster add/remove: invoke MekHQ-owned methods in the live app, verify expected campaign/scenario/unit identity, avoid coordinate clicking, and save only after explicit success.

Issue `#23` should stay user-owned. A source seam can help later verification, but it cannot choose the real campaign's player roster, pilots, support staff, transport assumptions, or family/table preferences.

## Verification

- `Confirmed locally`: `external/src/mekhq` was clean on `main...origin/main` before source inspection.
- `Confirmed from source`: source files inspected under `external/src/mekhq/MekHQ/src/mekhq` and `external/src/megamek/megamek/src/megamek/common/event`.
- `Not run`: no MekHQ source build or test was run, because this was a read-only source spike and the workspace still records the Gradle Java 17 daemon/toolchain blocker.
- `Not run`: no live MekHQ prototype was attempted, because issue `#34` acceptance criteria only require source-path identification and a viability recommendation.
