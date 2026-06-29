# MEK-RPG Live MekHQ Pilot Assignment And TO&E Source Audit

Status: completed source audit for GitHub issue `#71` on `2026-06-29`.

Purpose: identify MekHQ source owners for pilot/crew assignment and TO&E force edits before designing guarded local API commands for MEK-RPG.

Consumer request: `C:\Users\waltr\Documents\mek-rpg\docs\current\MEGAMEK_TOE_PILOT_ASSIGNMENT_API_HANDOFF.md`.

## Summary

`Confirmed from source`: MekHQ already has model-level mutation methods for assigning crew and moving units in the TO&E, but the most detailed eligibility checks live in Swing menu builders rather than reusable command/services.

`Decision`: issue `#72` should proceed as an API design issue, but it should not assume implementation can safely call the low-level model methods directly. V1 should either extract/shared-use assignment validators from the UI menus or add a narrow command service that uses the same source rules before it calls the model methods.

## Pilot And Crew Assignment Owners

`Confirmed from source`: `MekHQ/src/mekhq/campaign/unit/Unit.java` owns the actual crew mutation surface:

- `Unit#addPilotOrSoldier(...)`, `addDriver(...)`, `addGunner(...)`, `addVesselCrew(...)`, `setNavigator(...)`, and `setTechOfficer(...)` assign people to crew role collections, call `Person#setUnit(this)`, refresh the MegaMek `Entity` crew through `resetPilotAndEntity()`, write assignment logs, and trigger `PersonCrewAssignmentEvent`.
- `Unit#remove(Person, boolean)` removes a person from tech, driver, gunner, vessel crew, navigator, tech officer, engineer, and the person's current unit reference, then refreshes crew state and logs when requested.
- `Unit#clearCrew(boolean keepTech)` clears assigned crew, but returns without mutation when `Unit#isDeployed()` is true.
- `Unit#resetPilotAndEntity()` and helper code around crew-slot assignment copy MekHQ `Person` state, hits, skills, portraits, names, external ids, and active/missing state into the MegaMek `Entity` crew.

`Confirmed from source`: location compatibility is model-level, not only UI-level. The crew assignment methods call `LocationUtils.areSameEffectiveLocation(...)` and report a technical warning instead of assigning if the person and unit are not co-located.

`Confirmed from source`: the model mutation methods do not fully validate role eligibility before assignment. For example, `Unit#addPilotOrSoldier(...)` trusts the caller once location and registration pass. Role and unit-type eligibility are mostly enforced by menu construction in `MekHQ/src/mekhq/gui/menus/AssignPersonToUnitMenu.java` and `MekHQ/src/mekhq/gui/menus/AssignUnitToPersonMenu.java`.

## Assignment UI Validation

`Confirmed from source`: `AssignPersonToUnitMenu` validates from the person side before offering assignment actions:

- refuses empty selection, deployed personnel, and non-employed personnel
- requires active status and not-current-prisoner status for assignment choices
- requires selected people to share a compatible profession grouping
- filters target units through the effective location hangar and `Unit#isAvailable()`
- matches roles to unit types, such as MekWarrior grouping for `Mek`, aerospace grouping for `Aero`, conventional aircraft pilots for `ConvFighter`, vehicle crew roles for tanks/VTOL, vessel roles for small craft/jumpships, soldiers for infantry, and battle armor personnel for battle armor
- removes the person from their old unit before assigning to the new role, using transfer logging when campaign transfer options are enabled
- exposes unassignment by calling `person.getUnit().remove(person, true)` and clearing tech unit assignments

`Confirmed from source`: `AssignUnitToPersonMenu` validates from the unit side before offering assignment actions:

- refuses empty unit selections and units where `!unit.isAvailable()`
- rejects null or unsupported entities
- for non-tech crew assignment, only supports one selected unit at a time
- filters people to active, non-prisoner, employed, unassigned, same-place personnel
- applies the same role/unit-type matching families before calling `Unit` mutation methods
- exposes crew clear actions through `Unit#clearCrew(keepTechs)`

`Inference`: these two menus are the best source evidence for V1 eligibility rules, but they are UI components that create menu items and action listeners. A guarded API should not instantiate them as its validation engine. Extract a shared validator or implement a small service that mirrors these rules with tests.

## Unit Availability And Lock Semantics

`Confirmed from source`: `Unit#isAvailable(boolean ignoreRefit)` requires `isPresent()`, not deployed, not refitting unless ignored, not mothballing, and not mothballed. The UI assignment menus use `Unit#isAvailable()` for pilot/crew and TO&E unit assignment.

`Confirmed from source`: `Unit#isDeployed()` is based on `scenarioId != -1`. `Person#isDeployed()` delegates to the assigned unit's scenario id. `Formation#isDeployed()` checks its own scenario id and treats a child as deployed when a parent formation is deployed.

`Confirmed from source`: `Unit#getStatus()` and color reason methods expose visible states for deployed, in transit/not present, refitting, mothballing, mothballed, unmaintained, non-functional, and uncrewed units. These states should become read selector guard facts and command refusal reason codes.

`Decision`: V1 pilot assignment commands should default to refusing assignment when a unit is not `Unit#isAvailable()` or a person is deployed, inactive, current-prisoner, non-employed, already assigned, or not co-located. Any exception, such as allowing assignment to a mothballed unit for pre-planning, should be explicit API policy and source-reviewed separately because the current menus do not offer it.

## TO&E Force Edit Owners

`Confirmed from source`: `MekHQ/src/mekhq/campaign/Campaign.java` owns the main TO&E mutation methods:

- `Campaign#addFormation(Formation, Formation)` assigns a new formation id, attaches it to the parent, propagates scenario id, stores it in the formation map, updates commanders, and recalculates combat teams when StratCon is enabled.
- `Campaign#moveFormation(Formation, Formation)` prevents null/self moves, removes the formation from its old parent, attaches it under the new parent, propagates scenario id, standardizes/inherits formation type when needed, and repopulates formation levels.
- `Campaign#addUnitToFormation(Unit, int)` removes the unit from its previous formation, logs transfer/removal, updates `Unit#formationId` and `Unit#scenarioId`, applies formation tech assignment when possible, adds the unit to the new formation, triggers organization events, and recalculates combat teams when StratCon is enabled.
- `Campaign#removeFormation(Formation)` removes the formation id, clears unit formation/scenario ids for units in the formation, removes deployed formation ids from scenarios, detaches from the parent, clears StratCon track assignments, and recalculates combat teams.
- `Campaign#removeUnitFromFormation(Unit)` removes a unit from its formation, clears formation/scenario ids, and handles C3 network cleanup.

`Confirmed from source`: `MekHQ/src/mekhq/campaign/force/Formation.java` owns hierarchy state and derived formation behavior. It stores subformations and unit ids, updates formation commanders from eligible unit commanders, reports deployment through parent/scenario state, and has comments warning that direct `Formation#addUnit(...)` and `Formation#removeUnit(...)` should not be used by callers that need unit formation ids maintained.

`Decision`: TO&E commands should call `Campaign` methods, not direct `Formation` collection methods.

## TO&E UI Validation And Prompt Dependencies

`Confirmed from source`: `MekHQ/src/mekhq/gui/handler/TOETransferHandler.java` is the drag/drop owner for moving units and formations in the TO&E tree. It prevents dropping onto the selected source, prevents formation cycles, refuses deployed units and deployed formations in `importData(...)`, then calls `Campaign#addUnitToFormation(...)` or `Campaign#moveFormation(...)`.

`Confirmed from source`: `MekHQ/src/mekhq/gui/menus/AssignUnitToForceMenu.java` is the menu owner for assigning units to forces. It refuses empty input, unavailable units, null entities, and unsupported entities before offering force assignment. Its clear-assignment action calls `Campaign#removeUnitFromFormation(...)`, removes inherited formation tech assignment, clears transport assignment through `TOEMouseAdapter.clearTransportAssignment(...)`, and triggers organization events.

`Confirmed from source`: `MekHQ/src/mekhq/gui/adapter/TOEMouseAdapter.java` owns many right-click TO&E actions:

- create force uses `JOptionPane.showInputDialog(...)` for the force name, then `Campaign#addFormation(...)`
- rename force uses `JOptionPane.showInputDialog(...)`, then `Formation#setName(...)`
- delete force uses `JOptionPane.showConfirmDialog(...)`, clears transport assignments for the deleted force, removes subformations, then calls `Campaign#removeFormation(...)`
- add unit calls `Campaign#addUnitToFormation(...)`
- remove unit calls `Campaign#removeUnitFromFormation(...)`, clears formation tech and transport assignment, and triggers organization events
- set lance commander uses `Formation#setOverrideFormationCommanderID(...)` and `Formation#updateCommander(...)`

`Decision`: API V1 should not drive these Swing actions directly. Create/rename/delete commands need explicit request fields instead of dialogs. Delete should probably start as `toe.delete_empty_force` because current UI deletion recursively removes subformations and units after only a confirmation prompt, which is too broad for an external command.

## Selector Durability

`Confirmed from source`: persons and units are selected by `UUID` in the relevant model and UI paths. `Person#getId()` and `Unit#getId()` are suitable stable selectors, provided command requests also include expected display/status/assignment guard facts.

`Confirmed from source`: formations use integer ids assigned by `Campaign#addFormation(...)` and restored/imported through campaign loading. They are stable within the campaign save, but they are less self-describing than UUID selectors, so requests should include expected force name, parent id, unit ids, and current deployment state.

`Confirmed from source`: `Unit#formationId`, `Unit#scenarioId`, `Formation#scenarioId`, and `Person#getUnit()` are the main stale-state facts needed for pilot/TO&E commands.

`Decision`: `GET /campaign/commands` or a read-state section should expose person ids, unit ids, force ids, current unit/person assignment, current force membership, and source-backed blocker facts before mutating endpoints are implemented.

## Recommended V1 Command Slices

`Decision`: design issue `#72` can proceed with these slices:

1. `units.assign_pilot` for one active, employed, free, unassigned, co-located person into one available single-pilot Mek/proto/aero/conventional-fighter-style unit, using source role checks from the assignment menus and `Unit#addPilotOrSoldier(...)` or `Unit#addDriver(...)` as appropriate.
2. `units.unassign_pilot` for one non-deployed unit/person assignment, calling `Unit#remove(person, true)`.
3. `units.swap_pilots` as a later slice or as two validated removals plus two assignments only after dry-run can prove both assignments valid atomically.
4. `toe.move_unit` for one available unit to one non-deployed target formation, calling `Campaign#addUnitToFormation(...)` and preserving transport/formation-tech cleanup policy explicitly.
5. `toe.create_force` and `toe.rename_force` with explicit names, parent guards, and no Swing dialogs.
6. `toe.delete_empty_force` only for a non-origin, non-deployed formation with no units and no subformations in V1.

`Decision`: `toe.batch_update` should remain design-only until individual assignment and TO&E commands have selectors, validators, and fixture coverage.

## V1 Refusal Cases

`Decision`: the next design should include machine-readable refusal codes for at least:

- wrong campaign/date/state revision
- missing person, unit, or force selector
- stale expected person/unit/force facts
- person inactive, current prisoner, non-employed, deployed, already assigned, or not co-located
- unit deployed, not present/in transit, refitting, mothballing, mothballed, unsupported entity, null entity, already crewed when replacement is not allowed, or wrong unit type for the selected role
- role mismatch, such as non-MekWarrior assigned to a Mek pilot slot
- crew slot full
- formation missing, origin-delete requested, deployed force, parent/child cycle, non-empty delete, stale parent id, or target force deployed
- prompt required under `promptPolicy=refuse_if_prompt`

## Unknowns And Source Extraction Needs

`Unknown`: no reusable non-dialog pilot assignment service was found during this audit. The source has reusable mutation methods, but the role filters are embedded in `AssignPersonToUnitMenu` and `AssignUnitToPersonMenu`.

`Unknown`: the exact policy for assigning pilots to mothballed units is not source-confirmed as supported by current UI paths because `Unit#isAvailable()` excludes mothballed units. MEK-RPG's request mentions the Talitha window including units once unmothballed; V1 should require available units unless a future source/design issue deliberately supports mothballed assignment.

`Unknown`: TO&E force name validation appears dialog-driven and not obviously centralized. V1 design should define conservative name validation or identify existing validation before implementation.

`Inference`: source service extraction is probably required before implementation issues `#74` and `#75` if the project wants maintainable validation coverage. A small package-level validator/service around source roles, availability, co-location, and force locks would let the local API, menus, and tests share behavior instead of duplicating Swing menu logic.

## Recommended Next Step

Proceed to issue `#72`, "Design guarded pilot assignment and TO&E command API."

The design should treat issue `#73` read selectors as a prerequisite for implementation, and should add an implementation precondition that source validators or a command service must exist before mutating endpoints call `Unit` or `Campaign` mutation methods.
