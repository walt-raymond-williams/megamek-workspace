# MegaMek Tactical Performance Investigation

This note tracks the source-backed investigation into user-observed MegaMek tactical lag and potential low-risk speedups.

## Current Status

- `Confirmed from source`: issue `#80` is complete as a source investigation. No MegaMek source patch was approved or committed for this issue.
- `Confirmed by user`: MegaMek can become very laggy and unresponsive during gameplay, especially when there are many units on the board and AI/Princess controls units.
- `Confirmed by user`: A current concrete reproduction is the firing phase: switching between firing units is slow, and selecting or targeting enemies is slow.
- `Confirmed from source`: Tactical board rendering is centered around `external/src/megamek/megamek/src/megamek/client/ui/clientGUI/boardview/BoardView.java` and `BoardViewPanel.java`.
- `Confirmed from source`: `BoardView` schedules a redraw worker through `TimerSingleton` every 20 ms via `SwingUtilities.invokeLater(...)`. Many UI and game events also request board repaint or redraw work.
- `Inferred`: if the Swing event dispatch thread is already busy, uncoalesced redraw worker scheduling may contribute to perceived unresponsiveness because queued redraw work can compete with user input and game/UI updates.
- `Confirmed from source`: the firing-solution overlay is enabled by default in `GUIPreferences` and `FiringDisplay.selectEntity(...)` calls `ClientGUI.showFiringSolutions(...)` after each firing unit selection. `FiringSolutionSpriteHandler.showFiringSolutions(...)` clears existing sprites, scans `game.getEntitiesVector()` several times, and calls `WeaponAttackAction.toHit(...)` for every viable target, plus building hex and spotted-hex cases.
- `Confirmed from source`: clicking or cycling to a target calls `FiringDisplay.target(...)` and then `FiringDisplay.updateTarget(...)`, which can call `LosEffects.calculateLOS(...)`, `Compute.inVisualRange(...)`, `Compute.inSensorRange(...)`, `WeaponAttackAction.toHit(...)`, `Compute.effectiveDistance(...)`, and several button/status updaters for each selected target or selected weapon change.
- `Confirmed from source`: `FiringDisplay.refreshAll(...)`, used during firing unit selection and action updates, calls `BoardView.redrawEntity(currentEntity())`, refreshes the unit display, selects the first weapon, calls `updateTarget()`, updates the done panel, and then calls `ClientGUI.updateFiringArc(...)`; `ClientGUI.updateFiringArc(...)` can call `showFiringSolutions(...)` again.
- `Inferred`: in large battles, the default firing-solution overlay is the first likely issue `#81` multiplier because a single unit switch can do focused-target to-hit work plus an all-target to-hit overlay rebuild, and the path may be reached more than once through `refreshAll()` and the explicit `showFiringSolutions(...)` call at the end of `selectEntity(...)`.
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

## Issue 80 Broad Findings

- `Confirmed from source`: `BoardView` game listeners call `updateEcmList()` and redraw paths on entity new/remove/change events. `BoardView.selectEntity(...)` also clears FOV image cache state, calls `updateEcmList()`, and highlights the selected entity.
- `Confirmed from source`: `BoardView.updateEcmList()` calls `ComputeECM.computeAllEntitiesECMInfo(game.getEntitiesVector())` and then scans `game.getEntitiesVector()` again to build ECM/ECCM overlay maps. This is explicitly documented in the code as expensive and intended to be precalculated only when entity changes occur, but current selection paths can still invoke it.
- `Confirmed from source`: `BoardView.redrawAllEntities()` rebuilds entity, isometric, wreck, C3, and flyover sprite collections by scanning wrecked entities and `game.getEntitiesVector()`, then calls `updateEcmList()` and schedules redraw.
- `Confirmed from source`: `BoardView` painting draws hexes, minefields, artillery, deployment overlays, C3, flyover, moving, ghost, attack, movement, path, flight-path, and over-terrain sprite layers. `drawSprites(...)` iterates each sprite collection and `drawHexSpritesForHex(...)` comments that `sprite.getBounds()` can be expensive.
- `Confirmed from source`: `MinimapPanel.refreshMap()` posts `drawMapable` with `SwingUtilities.invokeLater(...)`. `drawMap()` loops the board dimensions for terrain redraw and, when configured, loops again for height display and unit/symbol overlays. This makes minimap redraw a plausible contributor on large boards, but no source evidence yet ties it directly to the reported firing-phase unit-switch lag.
- `Confirmed from source`: Princess movement work is not a single repaint problem. `Princess#getMovePathsAndSetNecessaryTargets(...)` clears and updates movable areas, chooses between normal path sets and long-range bulldozer paths, clips/decorates paths, checks LOS to leveling targets, and returns similar precomputed paths for destination behaviors.
- `Confirmed from source`: `PathEnumerator.recalculateMovesFor(...)` generates movement candidates with different path finders by unit type, including non-aero longest-path, backward, prone, jumping, and long-range paths. `BasicPathRanker` evaluates path danger with LOS and weapon-range checks, sprint exposure, facing, fall, self-preservation, hazards, and related factors.
- `Inferred`: UI/EDT responsiveness work and Princess AI/pathing work should be handled as separate follow-ups. The first can likely be tested with UI toggles and targeted instrumentation; the second needs representative scenarios and profiling before behavior changes.
- `Inferred`: the current local `BoardView.java` redraw-worker coalescing experiment is plausible as a queued-EDT pressure reduction, but it does not remove synchronous computation in firing-solution generation, ECM/FOV updates, minimap redraw, or Princess path ranking.

## Firing Phase Focus

Issue `#81` maps to these source paths:

- `Confirmed from source`: next/previous firing unit commands route through `FiringDisplay.selectEntity(int)` in `external/src/megamek/megamek/src/megamek/client/ui/panels/phaseDisplay/FiringDisplay.java`.
- `Confirmed from source`: `selectEntity(...)` first calls `target(null)`, `clearAttacks()`, and `refreshAll()` when the selected unit changes. It then sets the current entity, displays the unit, can call `target(lastTarget)`, clears marked hexes, highlights the unit, calls `refreshAll()` again, caches visible targets, recenters the board, updates firing controls, and finally calls `clientgui.showFiringSolutions(currentEntity())`.
- `Confirmed from source`: `refreshAll()` calls `BoardView.redrawEntity(currentEntity())`, `UnitDisplay.displayEntity(...)`, `WeaponPanel.selectFirstWeapon()`, `updateTarget()`, `updateDonePanel()`, and `ClientGUI.updateFiringArc(currentEntity())`. `updateFiringArc(...)` calls `showFiringSolutions(entity)` outside movement-display special handling.
- `Confirmed from source`: `cacheVisibleTargets()` calls `game.getValidTargets(currentEntity())`, sorts the returned entities by range, and stores them for next/previous target cycling.
- `Confirmed from source`: next/previous target cycling routes through `getNextTarget(...)` and `jumpToTarget(...)`. With `onlyValid` enabled, it calls `WeaponAttackAction.toHit(...)` while walking visible targets; after selecting a target it centers/selects the board hex and calls `target(targ)`.
- `Confirmed from source`: target clicks route through `hexSelected(...)`, `chooseTarget(...)`, and `target(...)`. `chooseTarget(...)` checks entities in the clicked hex with `game.getEntitiesVector(...)` or `game.getEnemyEntities(...)`, optionally checks flyovers through `BoardView.getEntitiesFlyingOver(...)`, and may include building or woods hex targets.
- `Confirmed from source`: `target(...)` validates off-board/deployed state, handles VGL auto-targeting, updates the visible-target index, can compute closest flight path for ground-to-air targeting, then calls `updateTarget()` and shows the aimed-shot dialog state.
- `Confirmed from source`: `updateTarget()` computes spot eligibility with `LosEffects.calculateLOS(...)` and, under double-blind, visual/sensor range checks. For a selected weapon and target, it calls `WeaponAttackAction.toHit(...)`, updates aimed-shot cover, computes effective distance, updates target/to-hit text, and then updates searchlight/RHS/SPA/jam/turret controls.
- `Confirmed from source`: `FiringSolutionSpriteHandler.showFiringSolutions(...)` clears current firing-solution sprites, scans all entities to find spotters/Narc targets, scans all entities again to calculate viable target firing solutions via `WeaponAttackAction.toHit(...)`, scans all entities again for spotted hexes, creates `FiringSolutionSprite` instances, and adds them to the board view.
- `Confirmed from source`: `BoardView.redrawEntity(...)` rebuilds sprite collections for the entity, updates C3 links, flyover sprites, ECM maps through `updateEcmList()`, highlights the selected entity, and schedules redraw. `updateEcmList()` calls `ComputeECM.computeAllEntitiesECMInfo(game.getEntitiesVector())` and then scans `game.getEntitiesVector()` again.
- `Confirmed from source`: `BoardView.selectEntity(...)`, called from `ClientGUI.setSelectedEntityNum(...)`, clears FOV caches, calls `updateEcmList()`, and highlights the selected entity. This adds another full-entity ECM pass to unit selection.
- `Confirmed from source`: right-click map menu mass-fire support in `MapMenu` also calls `WeaponAttackAction.toHit(...)` per weapon before selecting and firing weapons. This is not the primary reported path unless the lag is specifically in context-menu firing.

## Issue 81 Recommendations

1. `Recommended first live check`: turn off View > Firing Solutions in the MegaMek UI and reproduce the same firing-phase unit switching and target selection. This is the cleanest discriminator because the overlay is default-on and source-confirmed to run all-target `WeaponAttackAction.toHit(...)` work on unit/arc updates.
2. `Recommended first source fix if approved`: make firing-solution rebuilds lazy, debounced, or preference-gated more aggressively. A narrow candidate is to avoid duplicate `showFiringSolutions(...)` calls during `FiringDisplay.selectEntity(...)` by proving whether `refreshAll() -> updateFiringArc() -> showFiringSolutions()` already covers the same entity/weapon/ammo state before the explicit final call.
3. `Recommended second source fix if approved`: cache firing-solution calculations by `(game phase/action revision, attacker id, selected weapon id, selected ammo id, target id)` or rebuild only visible-board/visible-range sprites. This needs profiling because to-hit depends on current actions, spotting, ammo, and attacker state.
4. `Recommended third source fix if approved`: reduce repeated unit-switch full-entity work by auditing `BoardView.selectEntity(...)`, `BoardView.redrawEntity(...)`, and `updateEcmList()` call frequency. The current local `BoardView.java` redraw-worker coalescing experiment may help queued redraw pressure, but it does not remove the synchronous to-hit/ECM computation that happens before repaint.
5. `Recommended profiling`: add temporary timing logs around `FiringDisplay.selectEntity(...)`, `refreshAll()`, `ClientGUI.updateFiringArc(...)`, `FiringSolutionSpriteHandler.showFiringSolutions(...)`, `FiringDisplay.updateTarget()`, and `BoardView.updateEcmList()` while reproducing on a large-unit scenario.

## Issue 80 Recommendations

1. `Best immediate player check`: disable View > Firing Solutions and retry the laggy firing phase. If this helps, pursue the issue `#81` duplicate/debounced firing-solution rebuild path before broader renderer or AI work.
2. `Best first source patch if approved`: instrument first, then remove duplicate firing-solution rebuilds or debounce/lazily rebuild them. This is the smallest candidate tied to the user's concrete reproduction.
3. `Second UI candidate if approved`: revisit the `BoardView.java` redraw-worker coalescing experiment only after a compile and a live responsiveness comparison. Treat it as reducing queued repaint pressure, not as a complete fix for expensive source-side calculations.
4. `Third UI candidate`: instrument `BoardView.updateEcmList()`, `BoardView.selectEntity(...)`, and `BoardView.redrawEntity(...)` frequency during unit cycling. If repeated selection-only ECM recomputes are confirmed, consider dirty-state or phase-aware guards.
5. `Minimap candidate`: test with the minimap closed or hidden during the same large battle before editing `MinimapPanel`; source confirms board-sized redraw loops, but the reproduction evidence is not yet minimap-specific.
6. `Princess candidate`: collect a representative save/scenario and profile Princess turns before changing `PathEnumerator`, `PathRanker`, or `BasicPathRanker`. Low-risk work here should start with timing and candidate-count logging, not scoring changes.

## Issue 81 Residual Uncertainty

- `Unknown`: whether the user's observed lag is mostly the default firing-solution overlay, repeated ECM/FOV recalculation, expensive focused-target LOS/to-hit calculation, queued redraw pressure, or a combination.
- `Unknown`: whether disabling firing solutions, minimap, isometric mode, FOV, shadows, labels, or animations materially changes the specific firing-phase lag.
- `Inferred`: source structure makes firing-solution overlay the highest-value first suspect because it is default-on and scales with the number of entities/targets, but this still needs a live reproduction toggle or timing log.

## Open Questions

- Which gameplay state best reproduces the lag: movement phase, firing phase, AI thinking, animation playback, minimap interaction, or general board panning/selection?
- During firing phase, does the lag happen on unit selection, keyboard next-unit cycling, target hover, target click, weapon toggling, or all of these?
- Does the installed `0.51.00` build show the same lag with isometric rendering, minimap, FOV, shadows, labels, or animations disabled?
- Do we need a user-provided saved game/scenario with many units to profile a representative case?
