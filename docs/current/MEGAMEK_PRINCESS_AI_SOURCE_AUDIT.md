# MegaMek Princess AI Source Audit

Evidence: `Confirmed from source` unless a note is explicitly labeled otherwise.

Source snapshot inspected: local MegaMek checkout at `external/src/megamek`, Princess package `external/src/megamek/megamek/src/megamek/client/bot/princess`, built-in behavior file `external/src/megamek/megamek/mmconf/princessBehaviors.xml`, and bundled user help `external/src/megamek/megamek/docs/MegaMek/princess/PrincessBotDocumentation.html`.

## Executive Summary

Princess is a phase-driven heuristic AI client, not a planner with one global objective function. `Princess` extends `BotClient` and answers each MegaMek turn phase by choosing a legal action for the unit whose turn it is. The main control class is `Princess.java`; most movement scoring lives in `BasicPathRanker.java`; most shooting logic lives in `FireControl.java`.

The core behavior model is:

- Configure a small set of behavior sliders and flags in `BehaviorSettings`.
- For each unit, classify the unit into a coarse behavior state: forced withdrawal, move to destination, move to contact, engaged, or no path to destination.
- Use `Precognition` and `PathEnumerator` to keep likely move paths and enemy reachable areas precomputed.
- Rank candidate paths with weighted heuristics for fall risk, expected outgoing damage, expected incoming damage, distance to enemies, distance to allies, facing, movement modifier, hazards, crowding, retreat progress, sprint exposure, minefields, and artillery.
- Build firing plans by estimating expected damage, criticals, kill probability, heat cost, target priority, commander value, overkill, civilian/ejected-pilot penalties, and ammo choices.
- Apply special case systems around forced withdrawal, honor, morale, artillery, infantry combat, AMS modes, enhanced targeting, searchlights, spotting, clubs, unjamming, transports, aerospace, and experimental swarm/utility behavior.

The most practical conclusion for changing Princess is that there are several separate levers:

- Preset behavior tuning can often be changed in `princessBehaviors.xml` or `BehaviorSettingsFactory.java`.
- Normal movement personality changes belong in `BasicPathRanker.rankPath(...)` and helper terms.
- Experimental/swarm movement changes belong in `UtilityPathRanker`; they only affect behavior settings with `experimental=true`.
- Target choice and fire allocation changes belong in `FireControl.calculateUtility(...)`, `getBestFiringPlan(...)`, and `WeaponFireInfo`.
- Phase and fallback behavior changes belong in `Princess.calculateFiringTurn(...)`, `continueMovementFor(...)`, and post-processing helpers.
- Human commandability changes belong in `ChatCommands` and the `commands/` package.

The biggest architectural risk is that Princess is not cleanly separated into "strategy", "tactics", and "action generation". Source changes can easily have cross-phase effects because movement scoring calls firing estimates, firing target lists receive strategic movement targets, and forced withdrawal/honor/morale change both movement and firing decisions.

## Primary Source Map

Princess control and phase ownership:

- `Princess.java`: bot lifecycle, behavior settings, phase entry points, movement/firing/physical/targeting turns, deployment, enhanced targeting, morale invocation, chat command processing, path/fire-control selection, AMS mode changes, heat maps, and initiative reroll decisions.
- `BehaviorSettings.java`: behavior flags, slider indices, numerical value tables, target lists, XML serialization.
- `BehaviorSettingsFactory.java`: built-in Java defaults: `DEFAULT`, `BERSERK`, `COWARDLY`, `ESCAPE`, `RUTHLESS`, `PIRATE`, `CONVOY`.
- `mmconf/princessBehaviors.xml`: shipped configurable presets, including role-like presets such as Skirmisher, Sniper, Scout, missile boats, Striker, Juggernaut, Brawler, Ambusher, and v2 defaults.

Movement:

- `Precognition.java`: background mirror of game state and path cache updater.
- `PathEnumerator.java`: path generation for ground, infantry, aerospace, vector movement, low altitude, spheroid, prone, jumping, and long-range destination paths.
- `PathRanker.java`: common path validation, closest-target lookup, path success probability, and best-path selection.
- `BasicPathRanker.java`: normal additive utility path scoring for most units.
- `InfantryPathRanker.java`: infantry-specific movement scoring.
- `NewtonianAerospacePathRanker.java`: advanced aerospace/vector path ranking.
- `UtilityPathRanker.java`: experimental multiplicative/scaled utility ranker with swarm/formation/coverage logic.
- `UnitBehavior.java`: per-unit behavior state and waypoint queue.

Firing and attacks:

- `FireControl.java`: target list construction, to-hit estimation, full firing plan construction, heat-aware plan selection, target utility, ammo loading, spotting, searchlights, clubs, and unjamming.
- `InfantryFireControl.java`: infantry firing specialization.
- `MultiTargetFireControl.java`: multi-target-capable units, large craft, multi-gunner, Multi-Trac, and Multi-Tasker behavior.
- `WeaponFireInfo.java`: per-weapon shot estimate, expected damage, kill/crit probability, and attack action data.
- `FiringPlan.java`: collection of `WeaponFireInfo` actions plus expected damage, heat, utility, sort, and action-vector conversion.
- `PhysicalInfo.java`, `PhysicalAttackType.java`, `PhysicalCalculator` in `megamek.client.bot`: physical attack scoring and best physical selection.

Special systems:

- `ArtilleryTargetingControl.java`, `ArtilleryCommandAndControl.java`: indirect artillery target selection and chat-command-controlled artillery modes.
- `MoraleUtil.java`: optional morale/broken-unit checks.
- `HonorUtil.java`: tracks broken or dishonored enemies for forced-withdrawal honor behavior.
- `InfantryCombatHelper.java`: building/vessel infantry-vs-infantry initiation, reinforcement, and withdrawal thresholds.
- `HeatMap.java`, `EnemyTracker.java`, `CoverageValidator.java`, `SwarmContext.java`, `SwarmCenterManager.java`: memory/experimental behavior helpers.
- `ChatCommands.java` and `commands/*.java`: allied chat controls for changing behavior during a match.

## Phase Flow

`Princess.java` is the phase dispatcher owner:

- Movement: `calculateMoveTurn()` calls `continueMovementFor(getEntityToMove())`.
- Firing: `calculateFiringTurn()` chooses a shooter, asks the appropriate `FireControl` for a best plan, and sends attack actions.
- Targeting/off-board: `calculateTargetingOffBoardTurn()` handles TAG/artillery/off-board disengage.
- Physical attacks: `calculatePhysicalTurn()` delegates to `PhysicalCalculator.getBestPhysical(...)`.
- Infantry combat declarations: `calculatePreEndDeclarationsTurn()` and `calculateInfantryVsInfantryCombatTurn()` use `InfantryCombatHelper`.
- Movement setup: `initMovement()` checks morale, resets behavior cache, assigns swarm clusters, updates threat assessment, and builds extra strategic targets.
- Fire-control setup: `initFiring()` rebuilds target damage tracking and picks up building targets that contain hostile infantry.

Relevant source anchors:

- `Princess.java:984` firing turn
- `Princess.java:1234` targeting/off-board turn
- `Princess.java:2239` movement turn
- `Princess.java:2254` physical turn
- `Princess.java:2282` pre-end infantry declarations
- `Princess.java:2354` infantry-vs-infantry combat turn
- `Princess.java:2978` movement initialization

## Behavior Settings

Princess behavior is controlled by indexed settings rather than direct arbitrary weights. The main value tables are in `BehaviorSettings.java`:

- `selfPreservationIndex`: maps to `2.5, 5, 7.5, 10, 12.5, 15, 17.5, 20, 22.5, 25, 30`.
- `fallShameIndex`: maps to `10, 40, 80, 100, 160, 500, 500, 500, 500, 500, 500`.
- `braveryIndex`: maps to `0.1` through `3.0`.
- `hyperAggressionIndex`: maps to `0.25, 0.5, 1, 1.5, 2, 2.5, 3, 3.5, 10, 50, 500`.
- `herdMentalityIndex`: maps to `0.1` through `2.0`.

Other important flags/fields:

- `forcedWithdrawal`: crippled units try to withdraw and avoid firing unless attacked while fleeing.
- `autoFlee`: units try to leave even if not crippled.
- `destinationEdge` and `retreatEdge`: destination and withdrawal direction.
- `strategicBuildingTargets`: hex targets to attack or clear.
- `priorityUnitTargets`: enemy unit IDs with increased target utility.
- `ignoredUnitTargets`: unit IDs skipped as targets.
- `antiCrowding`: crowding penalty weight.
- `favorHigherTMM`: reward for movement defense.
- `exclusiveHerding`: herd only with owned units rather than all friends.
- `iAmAPirate`: disables normal honor constraints by treating enemies as dishonored.
- `ignoreDamageOutput`: used by convoy-like behavior to move without valuing attacks.
- `experimental`: switches normal ground units from `BasicPathRanker` to `UtilityPathRanker`.

Source anchors: `BehaviorSettings.java:63`, `BehaviorSettings.java:137`, `BehaviorSettings.java:486`, `BehaviorSettings.java:532`, `BehaviorSettings.java:579`, `BehaviorSettings.java:725`, `BehaviorSettings.java:844`.

## Unit Behavior State

`UnitBehavior.calculateUnitBehavior(...)` computes a cached per-unit state at movement time:

- `ForcedWithdrawal`: behavior has forced withdrawal enabled and the unit is crippled, or a Mek just entered industrial killing water and can path home.
- `MoveToDestination`: auto-flee/destination edge is active, or a valid waypoint exists.
- `MoveToContact`: no visible enemies exist.
- `Engaged`: default when enemies are present.
- `NoPathToDestination`: no path exists to the forced destination or all waypoints are invalid.

Waypoints are a queue. Invalid waypoints are discarded. Chat commands can add, set, remove, or clear them.

Source anchors: `UnitBehavior.java:51`, `UnitBehavior.java:74`, `UnitBehavior.java:120`, `UnitBehavior.java:142`, `UnitBehavior.java:191`.

## Movement Generation

Princess does not rank every theoretically possible action from scratch during its turn. `Precognition` keeps a separate mirror `Game` and a `PathEnumerator`. It processes game packets and marks affected units dirty, then recalculates paths in a background thread. During movement, Princess calls `ensureUpToDate()` before ranking.

`PathEnumerator.recalculateMovesForWorker(...)` generates candidate paths by unit type and rules context:

- Airborne aero on ground maps: `AeroGroundPathFinder`, then legal/off-board filtering.
- Vector movement aero: `NewtonianAerospacePathFinder`.
- Space aero: `AeroSpacePathFinder`.
- Low-altitude aero: `AeroLowAltitudePathFinder`.
- Spheroid atmospheric movement: `SpheroidPathFinder`.
- Infantry: `InfantryPathFinder`.
- Non-aero ground units: longest running paths, backward walking paths, prone paths, and jump paths.
- Long-range destination paths: `DestructionAwareDestinationPathfinder`, using retreat edges, waypoints, enemy hot spots, opposite edge, visible targets, and adjacent target positions depending on behavior state.

For normal ground units, candidate generation uses `getRunMPWithoutMASC()` and has a TODO asking whether this causes Princess never to use MASC in normal path generation.

Source anchors: `Precognition.java:95`, `Precognition.java:104`, `Precognition.java:155`, `Precognition.java:407`, `Precognition.java:447`; `PathEnumerator.java:161`, `PathEnumerator.java:188`, `PathEnumerator.java:217`, `PathEnumerator.java:250`, `PathEnumerator.java:275`, `PathEnumerator.java:291`, `PathEnumerator.java:367`.

## Movement Ranking

Normal non-infantry, non-Newtonian, non-experimental movement uses `BasicPathRanker`. `Princess.getPathRanker(Entity)` selects:

- `InfantryPathRanker` for infantry.
- `NewtonianAerospacePathRanker` for aero units using vector movement.
- `UtilityPathRanker` when `behaviorSettings.isExperimental()` is true.
- `BasicPathRanker` otherwise.

Source anchor: `Princess.java:300`.

`PathRanker.rankPaths(...)` first validates paths. It rejects illegal paths, bad low-altitude aero paths, paths that fail range policy, building-collapse paths, paths whose success probability is below fall tolerance, and RAC-unjam-incompatible running/jumping paths. If all paths are eliminated, it retries as move-to-contact.

Source anchors: `PathRanker.java:97`, `PathRanker.java:245`.

`BasicPathRanker.rankPath(...)` calculates an additive utility:

```text
utility =
  - fallMod
  + braveryMod
  - aggressionMod
  - herdingMod
  + movementMod
  - crowdingTolerance
  - facingMod
  - selfPreservationMod
  - sprintExposurePenalty
  - utility * offBoardMod
```

Main terms:

- `fallMod`: probability of failing required PSRs times the fall shame value, with guaranteed failure treated as possible unit destruction.
- `braveryMod`: `(successProbability * maximumDamageDone * braveryValue) - expectedDamageTaken`.
- `expectedDamageTaken`: PSR/fall damage, terrain hazards, minefields, estimated enemy fire/physical damage, and friendly incoming artillery.
- `aggressionMod`: distance to closest target times hyper-aggression value; high aggression strongly penalizes staying far away.
- `herdingMod`: distance from friendly center times herd mentality value.
- `movementMod`: TMM reward when no enemies are visible or `favorHigherTMM` is enabled.
- `facingMod`: penalty for not facing an enemy-centered direction.
- `selfPreservationMod`: when withdrawing or moving to destination, penalizes not getting closer to the home/destination edge and rewards arrival.
- `crowdingTolerance`: penalty for ending too close to too many enemies.
- `sprintExposurePenalty`: penalty for sprinting into enemy weapon range because sprinting gives up attacks.
- `offBoardMod`: aero-specific devaluation for paths that will force the unit off-board.

Source anchors: `BasicPathRanker.java:904`, `BasicPathRanker.java:918`, `BasicPathRanker.java:933`, `BasicPathRanker.java:950`, `BasicPathRanker.java:953`, `BasicPathRanker.java:1018`, `BasicPathRanker.java:1027`, `BasicPathRanker.java:1052`, `BasicPathRanker.java:1064`, `BasicPathRanker.java:1066`, `BasicPathRanker.java:1069`, `BasicPathRanker.java:1075`, `BasicPathRanker.java:1178`, `BasicPathRanker.java:1233`, `BasicPathRanker.java:1294`, `BasicPathRanker.java:1448`.

## Experimental Utility Ranker

`UtilityPathRanker` is a newer alternative enabled only by `BehaviorSettings.experimental`. It inherits many estimates from `BasicPathRanker`, but uses mostly normalized 0..1 terms multiplied together:

```text
utility = clamp01(
  braveryMod * fallMod * formationMod * aggressionMod * movementMod *
  selfPreservationMod * strategicMod * exposurePenalty * fallBack * facingMod
)
```

It also uses `SwarmContext`, `EnemyTracker`, and `CoverageValidator` for cluster centers, strategic double-blind goals, line formation, coverage, spacing, role-based exposure penalties, and fallback behavior.

Change implication: editing `UtilityPathRanker` will not affect default Princess unless the behavior has `experimental=true`.

Source anchors: `UtilityPathRanker.java:56`, `UtilityPathRanker.java:90`, `UtilityPathRanker.java:171`, `UtilityPathRanker.java:197`, `UtilityPathRanker.java:328`, `UtilityPathRanker.java:411`.

## Movement Post-Processing

After ranking picks a path, `Princess.performPathPostProcessing(...)` can adjust it:

- Move aero paths through shared aero movement helper.
- Add a `FLEE` step when the unit can and must flee.
- Try to unjam RACs if movement/firing conditions suggest it.
- Evade if not firing and there is no meaningful damage opportunity.
- Turn on searchlights when relevant and allowed.
- Unload transported infantry, launch fighters, or abandon carried units when a transport is no longer safe/useful.

Related helpers include `evadeIfNotFiring`, `turnOnSearchLight`, `unloadTransportedInfantry`, `launchFighters`, `shouldAbandon`, and `abandonShip`.

Source anchors: `Princess.java:3471`, `Princess.java:3483`, `Princess.java:3514`, `Princess.java:3528`, `Princess.java:3553`, `Princess.java:3583`, `Princess.java:3655`, `Princess.java:3709`, `Princess.java:3835`.

## Firing Control

`Princess.getFireControl(Entity)` chooses:

- `InfantryFireControl` for infantry.
- `MultiTargetFireControl` for units whose crew can target multiple primary targets, units with Multi-Trac, Multi-Tasker, or special unlimited primary target behavior.
- `FireControl` otherwise.

Source anchor: `Princess.java:468`.

Normal firing flow in `calculateFiringTurn()`:

1. Select the next shooter with `getEntityToFire(...)`; indirect-fire-capable units are ordered later so spotters can act first.
2. Skip firing if hidden, or if crippled/forced-withdrawing and not attacked while fleeing.
3. Compute ammo conservation thresholds.
4. Ask fire control for the best firing plan.
5. If the plan has positive expected damage, load ammo, sort shots, optionally apply aimed/called shots, update expected damage already assigned to targets, send mode changes, add searchlight/spot actions, and send attacks.
6. If no useful plan exists, try stopping swarm, unjamming, spotting, searchlights, and finding a club before sending an empty plan.

Source anchors: `Princess.java:984`, `Princess.java:2143`.

`FireControl.getBestFiringPlan(...)` loops targetable enemies, skips explicitly ignored targets, skips broken/retreating enemies unless priority targets, computes the best plan per target including legal facing changes, and returns the highest utility plan.

Target gathering includes visible enemies, indirect-fire opportunities, and additional targets such as strategic buildings.

Source anchors: `FireControl.java:2487`, `FireControl.java:2649`, `FireControl.java:2684`, `FireControl.java:2715`.

## Firing Utility

`FireControl.calculateUtility(...)` scores a firing plan as:

- Positive value for expected damage.
- Positive value for expected criticals.
- Positive value for kill probability.
- Multiplier for target command value, strategic building target, and priority unit target.
- Multiplier for target potential damage, scaled by Princess self-preservation.
- Penalty for likely overkill based on damage already assigned this round.
- Penalty for expected friendly damage.
- Penalty for civilian targets unless priority/dishonored.
- Penalty for overheating, much harsher for aerospace.
- Heavy penalty for shooting ejected pilots.

The most direct target-selection knobs are:

- `DAMAGE_UTILITY`
- `CRITICAL_UTILITY`
- `KILL_UTILITY`
- `TARGET_POTENTIAL_DAMAGE_UTILITY`
- `COMMANDER_UTILITY`
- `SUB_COMMANDER_UTILITY`
- `STRATEGIC_TARGET_UTILITY`
- `PRIORITY_TARGET_UTILITY`
- `TARGET_HP_FRACTION_DEALT_UTILITY`
- `CIVILIAN_TARGET_DISUTILITY`
- `EJECTED_PILOT_DISUTILITY`
- `OVERHEAT_DISUTILITY` and `OVERHEAT_DISUTILITY_AERO`

Source anchors: `FireControl.java:1376`, `FireControl.java:1422`, `FireControl.java:1434`, `FireControl.java:1450`, `FireControl.java:1502`.

## Heat and Ammo

Heat-aware planning starts with an alpha strike, then builds best plans under lower heat levels. Non-heat-tracking non-infantry units can use the full plan directly. Infantry and battle armor still need tradeoffs because some attack options are mutually exclusive.

`calcHeatTolerance(...)` is currently not behavior-configurable; the source has a TODO to add heat tolerance to behavior settings.

Ammo logic:

- Ammo-bearing weapons evaluate loaded ammo or relevant alternative ammo pools depending on weapon type.
- ATM, iATM, and MML evaluate all loaded ammunition.
- Ammo conservation thresholds are computed in `Princess.calcAmmoConservation(...)`; comments show target-number thresholds vary with aggression.
- `loadAmmo(...)` applies the selected ammo plan.
- Special ammo logic handles homing/TAG, heat-seeking, flak/cluster/ADA, air-to-air missiles, and bombs.

Source anchors: `Princess.java:1263`; `FireControl.java:942`, `FireControl.java:998`, `FireControl.java:1017`, `FireControl.java:2194`, `FireControl.java:2232`, `FireControl.java:2364`, `FireControl.java:2854`.

## Enhanced Targeting

Princess has a configurable enhanced targeting subsystem for aimed/called shots:

- Master switch: `enableEnhancedTargeting`.
- Target type and attacker type allowlists.
- Called shots on immobile targets can be allowed or disallowed.
- Partial cover can block enhanced targeting unless allowed.

When all attacks in a firing plan target the same entity, Princess may choose aimed shots against immobile targets or when the shooter has a targeting computer. If TacOps called shots are enabled, it may choose called shot direction instead. Mek logic favors head shots under specific conditions, weapon-bearing arms/torsos, weakened legs, or a location that can be destroyed by the plan.

Source anchors: `Princess.java:1453`, `Princess.java:1604`, `Princess.java:1670`, `Princess.java:1765`, `Princess.java:1924`, `Princess.java:2036`, `Princess.java:2054`.

## Forced Withdrawal, Honor, and Morale

Forced withdrawal appears in multiple places:

- `UnitBehavior` turns crippled units into `ForcedWithdrawal` if a path home exists.
- Movement post-processing flees the board when possible or ejects immobilized units if ejection is possible.
- Firing and physical attacks are skipped for crippled withdrawing units unless they were attacked while fleeing.
- `HonorUtil` tracks enemy broken units so Princess can ignore retreating enemies unless they dishonor themselves or are priority targets.

Morale is a separate Princess-side system. `MoraleUtil.checkMorale(...)` computes a 2d6 target number from friendly/enemy BV ratio, bravery, self-preservation, damage, and experience. Broken units are tracked locally and can rally.

Source anchors: `Princess.java:2617`, `Princess.java:3352`, `MoraleUtil.java:79`, `HonorUtil.java:42`.

## Artillery and TAG

Targeting/off-board turn logic asks `ArtilleryTargetingControl.calculateIndirectArtilleryPlan(...)` for a plan. If no artillery plan exists, Princess attempts unjamming for the off-board/targeting unit.

`ArtilleryCommandAndControl` stores chat-command artillery order state:

- `HALT`
- `AUTO`
- `BARRAGE`
- `VOLLEY`
- `SINGLE`

It also tracks special ammo intent: smoke, flare, mine.

`ArtilleryTargetingControl` estimates damage value for hex targets, builds target lists, computes indirect artillery plans, finds TAG info, finds ammo, and evaluates incoming artillery damage for movement path ranking.

Source anchors: `Princess.java:1234`, `ArtilleryCommandAndControl.java:51`, `ArtilleryCommandAndControl.java:59`, `ArtilleryTargetingControl.java:116`, `ArtilleryTargetingControl.java:408`, `ArtilleryTargetingControl.java:738`.

## Infantry Combat

Princess has distinct logic for new infantry-vs-infantry/building combat:

- Only infantry initiates.
- Initiation compares attacker MPS to defender MPS.
- Bravery lowers the required initiation ratio.
- Reinforcement tries to keep attacker ratio above an aggression-influenced target.
- Withdrawal uses a lower threshold than initiation to avoid thrashing.
- Crippled forced-withdrawing infantry will not initiate.

Source anchors: `Princess.java:2282`, `Princess.java:2354`, `InfantryCombatHelper.java:128`, `InfantryCombatHelper.java:162`, `InfantryCombatHelper.java:204`, `InfantryCombatHelper.java:279`.

## Deployment

Deployment is heuristic:

- Crippled units are declined/removed rather than deployed.
- Turrets prefer buildings and valid roof positions.
- Advanced aerospace avoids deployment coordinates that immediately fly off-board when possible.
- Other units rank deployment hexes by hazards, path freedom, concealment, and local kernel rank around candidate coordinates.
- Facing tries to point toward deployed enemies, otherwise toward board center.

Source anchors: `Princess.java:582`, `Princess.java:686`, `Princess.java:710`, `Princess.java:733`, `Princess.java:840`.

## Chat Commands

Allied chat commands can modify behavior during a game. Current command enum entries:

- `flee`
- `behavior`
- `caution`
- `avoid`
- `artillery`
- `aggression`
- `herding`
- `bravery`
- `target`
- `prioritize`
- `show-behavior`
- `list-commands`
- `ignore-target`
- `ignore-player`
- `ignore-turrets`
- `show-dishonored`
- `clear-ignored-targets`
- `blood-feud`
- `add-waypoint`
- `remove-waypoint`
- `clear-waypoints`
- `clear-all-waypoints`
- `set-waypoints`

Source anchor: `ChatCommands.java:49`.

## Current Limitations and Fragile Areas

Confirmed from source and local docs:

- Princess is not victory-condition aware in a deep way. The bundled help says victory conditions may work mechanically, but Princess uses the same strategy regardless.
- Some aerospace behavior exists, but local help warns it is slow and weak on space maps.
- Infantry has separate logic, but local help still describes Princess as not very competent with infantry.
- Heat tolerance is not yet a behavior setting; source contains a TODO in `FireControl`.
- Normal ground path generation may avoid MASC because `PathEnumerator` uses `getRunMPWithoutMASC()` and has a TODO.
- `UtilityPathRanker.rankPath(...)` calls `enemies.getFirst()` in its fallback calculation; experimental behavior should be tested in no-visible-enemy and double-blind situations before relying on it broadly.
- Simultaneous phases are documented as technically usable but expensive, with simultaneous movement not working.
- Many optional rules are not guaranteed Princess-compatible.

Inferred:

- The safest first behavior changes are weight/threshold/preset changes, because they touch fewer contracts than changing candidate generation or packet/phase flow.
- `UtilityPathRanker` appears to be an experimental redesign path and is a good sandbox for new formation/swarm ideas before altering default behavior.

## Change Planning Guide

Use this map when deciding where to change Princess:

| Desired behavior change | Primary files/methods |
| --- | --- |
| Make Princess generally more/less aggressive | `BehaviorSettings` value tables; preset indices in `BehaviorSettingsFactory` or `princessBehaviors.xml`; `BasicPathRanker.calculateAggressionMod(...)` |
| Make Princess preserve units more | `BehaviorSettings.SELF_PRESERVATION_VALUES`; `BasicPathRanker.calculateSelfPreservationMod(...)`; movement hazard and expected-damage terms |
| Make Princess less afraid of PSRs/falls | `BehaviorSettings.FALL_SHAME_VALUES`; `PathRanker.validatePaths(...)`; `BasicPathRanker.calculateFallMod(...)` |
| Make Princess value TMM/defensive movement | `favorHigherTMM`; `BasicPathRanker.calculateMovementMod(...)`; `UtilityPathRanker.calculateMovementMod(...)` |
| Improve target selection | `FireControl.calculateUtility(...)`; command/priority utilities; target potential damage multiplier; ignored/broken/priority logic |
| Reduce overkill or improve focus fire | `FireControl.calcDamageAllocationUtility(...)`; `Princess.damageMap` updates in firing |
| Change heat behavior | `FireControl.calcHeatTolerance(...)`; `calcFiringPlansUnderHeat(...)`; possibly add behavior setting |
| Change ammo choice | `FireControl.guessFullFiringPlan(...)`, `getFullFiringPlan(...)`, `loadAmmo(...)`, and `Princess.calcAmmoConservation(...)` |
| Improve retreat behavior | `UnitBehavior.calculateUnitBehavior(...)`; `Princess.continueMovementFor(...)`; `BasicPathRanker.calculateSelfPreservationMod(...)`; `HonorUtil` |
| Improve movement through/around terrain | `PathEnumerator.recalculateMovesForWorker(...)`; `PathRanker.validatePaths(...)`; `BasicPathRanker.checkPathForHazards(...)` |
| Add objective-aware behavior | `UnitBehavior`, strategic target handling in `Princess.initMovement()`, `FireControlState.additionalTargets`, and possibly a new objective model |
| Improve command/chat control | `ChatCommands.java`; `commands/*.java`; `BehaviorSettings` serialization if persistent |
| Try new formation/swarm behavior | `UtilityPathRanker`, `SwarmContext`, `EnemyTracker`, `CoverageValidator`, `SwarmCenterManager`; set `experimental=true` |

## Recommended First Experiments

1. Add a behavior preset rather than changing defaults. Use `princessBehaviors.xml` for a data-only trial, or `BehaviorSettingsFactory` if it must be built into code paths that do not load XML.
2. For campaign OPFOR personality, tune indices first: aggression, bravery, self-preservation, fall shame, herding, anti-crowding, and TMM preference.
3. For "Princess should care about scenario objectives", add an explicit objective-to-target/waypoint layer before modifying low-level scoring. Existing strategic building targets and waypoints are the closest implementation hook.
4. For "Princess wastes fire", inspect and test `damageMap` plus `calcDamageAllocationUtility(...)` before changing target utility constants.
5. For "Princess makes bad movement choices", capture ranked path scores from `RankedPath.getScores()` and the bot logger before changing formulas. The ranker already records term-level scores.
6. For experimental formation behavior, use `experimental=true` and `UtilityPathRanker` so default behavior remains stable.

## Verification Notes

No MegaMek source was modified for this audit. Verification for this document was source inspection only:

- ripgrep searches over `external/src/megamek/megamek/src/megamek/client/bot/princess`
- direct reads of `Princess.java`, `BehaviorSettings.java`, `BehaviorSettingsFactory.java`, `UnitBehavior.java`, `PathEnumerator.java`, `Precognition.java`, `PathRanker.java`, `BasicPathRanker.java`, `UtilityPathRanker.java`, `FireControl.java`, `MultiTargetFireControl.java`, `InfantryCombatHelper.java`, `MoraleUtil.java`, `HonorUtil.java`, `ChatCommands.java`
- direct reads of bundled `PrincessBotDocumentation.html` and `mmconf/princessBehaviors.xml`

Recommended source tests before any behavior change:

- Run existing Princess unit tests under `external/src/megamek/megamek/unittests/megamek/client/bot/princess`.
- Add focused tests for changed scoring formulas where possible, especially `BasicPathRankerTest`, `FireControlTest`, `FiringPlanTest`, `BehaviorSettingsTest`, and `PrincessTest`.
- For subjective behavior changes, run bot-vs-bot scenarios and compare bot logs/ranked path outputs before and after.
