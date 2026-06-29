# MEK-RPG Live MekHQ Pilot Assignment And TO&E Command Design

Status: design completed for GitHub issue `#72` on `2026-06-29`.

Purpose: define the guarded local MekHQ command contract for MEK-RPG pilot assignment, pilot unassignment, pilot swaps, and conservative TO&E force edits.

Consumer request: `C:\Users\waltr\Documents\mek-rpg\docs\current\MEGAMEK_TOE_PILOT_ASSIGNMENT_API_HANDOFF.md`.

Source audit: `docs/current/MEK_RPG_LIVE_MEKHQ_PILOT_TOE_SOURCE_AUDIT.md`.

## Summary

`Decision`: add this command family only after issue `#73` exposes source-owned read selectors and guard facts. The mutating endpoints should not accept display names, table rows, or client-inferred eligibility.

`Decision`: V1 should be conservative:

- `units.assign_pilot` assigns one free eligible person to one available uncrewed single-pilot unit.
- `units.unassign_pilot` removes one assigned person from one non-deployed unit.
- `units.swap_pilots` is a separate atomic command and should not be emulated by MEK-RPG as two independent assignments.
- `toe.move_unit` moves one available unit to one non-deployed target force.
- `toe.create_force` and `toe.rename_force` use explicit request names and no Swing dialogs.
- `toe.delete_empty_force` deletes only a non-origin, non-deployed, empty force with no child forces.

`Decision`: V1 should refuse assignment to mothballed units, direct replacement of an occupied pilot slot, multi-crew role assignment, non-empty force deletion, and generic batch updates. These can be revisited after selectors, validators, and fixture coverage exist.

`Implementation precondition`: issue `#74` and issue `#75` should either extract/shared-use source validators from the Swing assignment/TO&E paths or add a narrow MekHQ command service that mirrors those source rules with focused regression tests before calling low-level `Unit` or `Campaign` mutation methods.

## Readiness Rows

`GET /campaign/commands` should expose these rows after issue `#73`:

| Command | V1 readiness before implementation | Required selector detail |
| --- | --- | --- |
| `units.assign_pilot` | `blocked` with `endpoint_not_implemented` until issue `#74` | `person_assignment_candidates`, `unit_crew_candidates`, role eligibility hints, crew slot facts |
| `units.unassign_pilot` | `blocked` with `endpoint_not_implemented` until issue `#74` | current unit/person assignment facts and deployment locks |
| `units.swap_pilots` | `blocked` with `deferred_until_atomic_assignment_service` until issue `#74` deliberately supports it | two person selectors, two unit selectors, both before/after eligibility checks |
| `toe.move_unit` | `blocked` with `endpoint_not_implemented` until issue `#75` | unit force membership, target force selector, deployment/cycle blockers |
| `toe.create_force` | `blocked` with `endpoint_not_implemented` until issue `#75` | parent force selector and source-backed or conservative name policy |
| `toe.rename_force` | `blocked` with `endpoint_not_implemented` until issue `#75` | force selector, current name, parent id, name policy |
| `toe.delete_empty_force` | `blocked` with `endpoint_not_implemented` until issue `#75` | force selector, parent id, child count, unit count, origin/deployment flags |
| `toe.batch_update` | `blocked` with `v_later_batch_not_supported` | none in V1 |

`Decision`: default readiness should stay cheap. Expensive or broad selector construction can use `selectorDetail=full`, but pilot/TO&E selectors should be bounded enough for normal readiness when practical because assignment planning is an interactive MEK-RPG workflow.

## Selector Requirements

Issue `#73` should expose these selector groups through `GET /campaign/commands`, `GET /campaign/state`, or both. Command selectors belong in readiness when they encode command-specific guard facts; broader display state can live in `/campaign/state`.

### Personnel

Each selectable or blocked person row should include:

- `personId`: `Person#getId()` UUID string.
- `displayName`: current display name for stale-human guard display only.
- `primaryRole` and relevant secondary role/grouping facts.
- `status`, active/dead/absent flags, prisoner state, employed status, deployed flag.
- current assignment: `assignedUnitId`, assigned unit name, and crew role if source-backed.
- location guard facts sufficient to check co-location with a unit.
- eligibility hints where source-backed, such as `eligibleSinglePilotUnitTypes` and `assignmentBlockers`.

### Units

Each selectable or blocked unit row should include:

- `unitId`: `Unit#getId()` UUID string.
- display name, chassis/model/type, entity type family, and transport-relevant class where already available.
- `status`, `isAvailable`, `isPresent`, `isDeployed`, refit/mothball/mothballed flags, scenario id, and location facts.
- current crew slots and occupant person ids for V1 single-pilot slots.
- current `formationId`, formation name/path, and scenario/force deployment facts.
- assignment blockers such as `unit_not_available`, `unit_deployed`, `unit_mothballed`, `unsupported_entity`, `crew_slot_occupied`, or `multi_crew_not_supported`.

### Forces

Each force row should include:

- `forceId`: restored campaign formation id.
- `name`, `parentForceId`, child force ids, unit ids, and tree path.
- `scenarioId`, deployed flag, origin/root flag, unit count, child count.
- conservative mutation blockers such as `force_deployed`, `force_is_origin`, `force_has_units`, `force_has_children`, or `target_would_create_cycle`.

## Shared Request Rules

All commands use the shared command envelope from `MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`:

- `command`
- `commandVersion`
- `idempotencyKey`
- `expectedCampaignId` or `expectedCampaignName`
- `expectedDate`
- `expectedStateRevision`
- command-specific target selectors
- command-specific expected guard facts
- `dryRun`
- `promptPolicy`
- `saveAfterSuccess`
- optional `savePath` when saving is supported and requested
- `clientContext`

`Decision`: `dryRun` is required and supported for every V1 command. Dry-run validates the same selector and guard path as apply mode, then stops before mutation, reports, events, prompts, and saves.

`Decision`: `promptPolicy` defaults to `refuse_if_prompt`. These commands should not instantiate Swing menus or answer arbitrary `JOptionPane` dialogs. Create and rename force names must be explicit request fields.

`Decision`: `saveAfterSuccess=false` remains the normal MEK-RPG flow. If a later implementation supports saving, it must use MekHQ save logic and never save after dry-run, refused, blocked, or failed responses.

## Pilot Commands

### `units.assign_pilot`

Endpoint candidate: `POST /campaign/command/units/assign-pilot`.

V1 scope:

- one person
- one unit
- single-pilot unit families confirmed by source validation
- no direct replacement
- no mothballed, mothballing, refitting, deployed, in-transit/not-present, unsupported, or already crewed units
- no inactive, absent, dead, current-prisoner, non-employed, deployed, already assigned, or non-co-located people

Required fields:

- `personId`
- `unitId`
- `expectedPerson`: display name, status, prisoner state, role/grouping, current unit id, deployed flag, location guard
- `expectedUnit`: display name, entity/unit type, status, `isAvailable`, scenario id, formation id, current pilot slot occupancy, location guard
- `assignmentPolicy`: V1 accepts only `replaceExistingPilot=false`, `allowMothballedUnitAssignment=false`, `allowUnavailablePersonnel=false`, and `allowDeployedUnitChange=false`

Mutation owner after validation:

- `Unit#addPilotOrSoldier(...)` or other source-confirmed role-specific `Unit` assignment method selected by the MekHQ validator/service.

Post-command verification facts:

- before/after person assignment
- before/after unit crew slot occupancy
- state revision before/after
- assignment log/report/event facts if surfaced by implementation

### `units.unassign_pilot`

Endpoint candidate: `POST /campaign/command/units/unassign-pilot`.

V1 scope:

- one assigned person from one non-deployed unit
- no crew clear-all shortcut
- no unassignment from deployed unit or deployed person

Required fields:

- `personId`
- `unitId`
- `expectedAssignment`: person is assigned to the unit in the expected crew role
- `expectedPerson`: display name, status, deployed flag
- `expectedUnit`: display name, status, scenario id, current crew slot facts

Mutation owner after validation:

- `Unit#remove(person, true)`.

### `units.swap_pilots`

Endpoint candidate: `POST /campaign/command/units/swap-pilots`.

V1 design:

- The command validates both removals and both assignments before mutation.
- Apply mode should be atomic from the API consumer perspective: if either target assignment is invalid, nothing changes.
- If source implementation cannot guarantee a clean rollback or a safe ordered mutation, return `blocked` with `atomic_swap_not_supported`.

Required fields:

- `leftUnitId`, `leftPersonId`, `rightUnitId`, `rightPersonId`
- expected assignment facts for both pairs
- expected eligibility facts for both cross-assignments

`Decision`: MEK-RPG should not perform swaps by calling `unassign` and `assign` independently because that creates avoidable partial-state risk.

## TO&E Commands

### `toe.move_unit`

Endpoint candidate: `POST /campaign/command/toe/move-unit`.

V1 scope:

- one available unit
- one target force
- target force and source force not deployed
- no move into invalid cycle or missing force
- no transport-assignment promises beyond explicit implementation behavior

Required fields:

- `unitId`
- `targetForceId`
- `expectedUnit`: display name, status, scenario id, current formation id
- `expectedSourceForce`: source force id/name when assigned
- `expectedTargetForce`: target force id/name, scenario id, deployed flag

Mutation owner after validation:

- `Campaign#addUnitToFormation(unit, targetForceId)`.

Implementation must explicitly document whether it mirrors UI cleanup for inherited formation tech assignment and transport assignment. If that behavior is not source-shared yet, the command should refuse cases where cleanup would be required.

### `toe.create_force`

Endpoint candidate: `POST /campaign/command/toe/create-force`.

V1 scope:

- create one child force under one non-deployed parent
- explicit `name`
- no dialog prompts

Required fields:

- `parentForceId`
- `name`
- `expectedParentForce`: parent name, scenario id, deployed flag, child names if enforcing sibling uniqueness
- `namePolicy`: V1 should use a conservative ASCII printable name policy until source validation is identified

Mutation owner after validation:

- `Campaign#addFormation(new Formation(name), parentFormation)`.

`Decision`: V1 should enforce a conservative local command policy even if MekHQ UI allows looser names: trimmed length `1..80`, no control characters, no path separators, and no duplicate sibling name unless source confirms duplicate sibling names are acceptable and useful.

### `toe.rename_force`

Endpoint candidate: `POST /campaign/command/toe/rename-force`.

V1 scope:

- rename one non-origin, non-deployed force
- explicit `newName`
- no dialog prompts

Required fields:

- `forceId`
- `expectedForce`: current name, parent id, scenario id, deployed flag
- `newName`
- `namePolicy`

Mutation owner after validation:

- `Formation#setName(newName)`.

Implementation should trigger or mirror any source-required organization refresh. If no source-owned refresh path is identified, the implementation issue must record that before applying.

### `toe.delete_empty_force`

Endpoint candidate: `POST /campaign/command/toe/delete-empty-force`.

V1 scope:

- delete one non-origin, non-deployed force
- force must have zero units and zero child forces
- no recursive delete
- no confirmation dialog

Required fields:

- `forceId`
- `expectedForce`: name, parent id, scenario id, deployed flag, unit count `0`, child count `0`

Mutation owner after validation:

- `Campaign#removeFormation(formation)`.

`Decision`: broader delete remains unsupported because the UI path can recursively clear units/subformations after a confirmation prompt. External commands need a stricter surface.

## Response Shape

Responses use the shared status family:

- `applied`
- `dry_run`
- `refused`
- `blocked`
- `failed`

Every response should include:

- `command`, `commandVersion`, `idempotencyKey`
- campaign/date/state revision before and after
- selected person/unit/force ids and display labels
- before/after target facts for the command
- side effects applied or side effects that would apply in dry-run
- `warnings` and `unsupported`
- prompt facts
- save facts
- sanitized `clientContext`

`Decision`: use `statusReason` as the canonical machine-readable reason field. If existing implementation helpers still expose `reasonCode`, include it as an alias during transition, but new docs and tests should prefer `statusReason`.

## Refusal And Blocker Codes

Shared:

- `wrong_campaign`
- `wrong_date`
- `stale_state_revision`
- `missing_selector`
- `missing_guard_fact`
- `stale_target_guard`
- `unsupported_command_version`
- `idempotency_conflict`
- `prompt_required`
- `save_policy_unsupported`
- `source_validator_missing`
- `source_exception`

Pilot assignment:

- `person_not_found`
- `unit_not_found`
- `person_inactive`
- `person_dead_or_absent`
- `person_current_prisoner`
- `person_not_employed`
- `person_deployed`
- `person_already_assigned`
- `person_not_co_located`
- `invalid_role_for_unit`
- `unit_not_available`
- `unit_not_present`
- `unit_deployed`
- `unit_refitting`
- `unit_mothballing`
- `unit_mothballed`
- `unsupported_entity_type`
- `crew_slot_occupied`
- `crew_slot_full`
- `replacement_not_supported`
- `multi_crew_not_supported`
- `atomic_swap_not_supported`

TO&E:

- `force_not_found`
- `unit_force_stale`
- `target_force_deployed`
- `source_force_deployed`
- `unit_deployed`
- `unit_not_available`
- `formation_cycle`
- `origin_force_delete_refused`
- `non_empty_force_delete_refused`
- `force_has_child_forces`
- `force_name_blank`
- `force_name_invalid`
- `force_name_duplicate`
- `force_name_too_long`
- `transport_cleanup_required`
- `formation_tech_cleanup_required`

## Post-Command Reread Verification

MEK-RPG should continue the standard guarded command loop:

1. Read `/status`.
2. Read `/campaign/state` sections that include bridge metadata, personnel, units, and force/TO&E state.
3. Read `/campaign/commands`.
4. Build a dry-run request from source-provided selectors and guard facts.
5. Present the dry-run result to the user.
6. Apply with the same selectors, updated idempotency key, and current guard facts after approval.
7. Reread `/campaign/state` and `/campaign/commands`.
8. Trust the reread state, not the client-side intended state, as the verified MekHQ ledger result.

Implementation tests should include at least one failed command followed by `/status` or `GET /campaign/commands` to prove the local control server remains usable.

## V1 And Deferred Scope

V1:

- expose selector and guard facts in issue `#73`
- implement single-pilot assignment, unassignment, and source-safe swap only in issue `#74`
- implement unit move, create force, rename force, and empty-force delete only in issue `#75`
- keep dry-run supported for all V1 commands
- keep save opt-in and prompt refusal

Deferred:

- assigning to mothballed or unavailable units
- direct pilot replacement inside `units.assign_pilot`
- broad multi-crew vehicle, infantry, vessel, navigator, tech officer, or engineer assignment
- crew clear-all
- recursive force delete
- transport assignment edits
- formation tech assignment edits beyond safe cleanup/refusal
- broad `toe.batch_update`
- client-authored raw save patches

## Follow-Up Issue Guidance

Issue `#73` should implement the read selectors first and keep all command rows blocked until mutating endpoints exist.

Issue `#74` should implement pilot commands only after source validation is reusable outside Swing menus or captured in a focused command service with tests.

Issue `#75` should implement TO&E commands through `Campaign` methods and should explicitly handle or refuse transport and formation-tech cleanup cases.

Issue `#76` should remain optional until individual commands prove selector stability, dry-run fidelity, and safe rollback/atomicity boundaries.

Issue `#77` should add fixtures and smoke coverage for valid assignment, invalid role, duplicate assignment, stale guard, deployed locks, invalid force move, non-empty delete refusal, and post-command reread verification.
