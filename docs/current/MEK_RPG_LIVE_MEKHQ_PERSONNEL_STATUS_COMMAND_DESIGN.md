# MEK-RPG Live MekHQ Personnel Status Command Design

Status: design completed for GitHub issue `#47` on `2026-06-22`.

Purpose: define a guarded live MekHQ command for MEK-RPG character events that change a MekHQ person's campaign status outside MekHQ tactical scenario resolution.

## Recommendation

`Decision`: implement a narrow V1 endpoint, `POST /campaign/command/personnel/status`, that calls `Person#changeStatus(Campaign, LocalDate, PersonnelStatus)` for source-owned side effects. Do not directly call `Person#setStatus(...)`.

`Confirmed from source`: `Person#changeStatus(...)` in `external/src/mekhq/MekHQ/src/mekhq/campaign/personnel/Person.java` is the central mutation path for status changes. It writes personnel reports and service log entries, sets or clears retirement/death dates, handles resurrection reports, removes non-active personnel from unit crew assignments, clears doctor and tech work, releases commander and second-in-command flags for departed personnel, handles genealogy effects for deaths, clears education fields, restores education tag-alongs to active status, and fires `PersonStatusChangedEvent`.

`Confirmed from source`: `CampaignEventProcessor#handlePersonUpdate(...)` invalidates active-personnel caches when person events fire, and `Unit#remove(Person, boolean)` clears crew slots, resets pilot/entity state, logs assignment removal, and fires crew-assignment events.

V1 should therefore use the existing status workflow and restrict itself to transitions whose side effects are understandable from source inspection.

## V1 Endpoint

Endpoint:

```text
POST /campaign/command/personnel/status
```

Required request fields, in addition to the shared command envelope from `MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`:

- `personId`: MekHQ `Person` UUID from command readiness or live campaign state.
- `expectedPersonName`: display guard for the selected person.
- `expectedCurrentStatus`: current `PersonnelStatus.name()`.
- `expectedCurrentPrisonerStatus`: current `PrisonerStatus.name()`; V1 should require `FREE` unless the transition is a simple `POW -> ACTIVE` recovery.
- `expectedUnitId`: current assigned unit UUID, or `null`.
- `expectedUnitName`: current assigned unit name, or `null`.
- `newStatus`: allowed target `PersonnelStatus.name()`.
- `rpgEventType`: one of the V1 allowlisted intent codes below.
- `rpgEventSource`: source label such as MEK-RPG scene/session/action id.
- `narrativeReason`: short plain-text reason for audit/report context.
- `retirementPayoutPolicy`: required for `RETIRED`, `RESIGNED`, or `LEFT`; V1 should accept only `none_requested`.

Optional V1 field:

- `appendStatusNote`: default `true`; when true, add a short `GENERAL` status-note report after the status change using the existing status-note command helper path, or the same source-owned `Campaign#addReport(...)` behavior, so MEK-RPG has an audit string that contains the RPG event source and reason. The normal `PERSONNEL` report from `changeStatus(...)` remains the authoritative MekHQ status report.

Response should include before/after:

- person id, name, status, prisoner status, unit id/name, doctor id, active/departed/absent/dead flags, commander/second-in-command flags, date of death, retirement date, salary eligibility, active-personnel membership, and assigned tech-job count if easy to expose.
- side effects observed: unit crew removed, tech jobs cleared, reports added, service-log entries added if countable, command flags changed, cache/event facts, save facts, prompt facts, and audit context.

## Allowed V1 Transitions

`Decision`: V1 should allow only single-person transitions with clear RPG intent. It should reject bulk status changes until single-target behavior is live-tested.

Allowed target statuses:

- `MIA` for narrative disappearance where no MekHQ scenario result should own the casualty.
- `POW` for player personnel captured by an enemy outside tactical result import. `Confirmed from source`: `CapturePrisoners#processPrisoner(...)` uses `POW` for captured player characters; separate `PrisonerStatus.PRISONER` is used for NPC prisoners.
- `ACTIVE` only for recovery/return from `MIA`, `POW`, `MISSING`, `AWOL`, `ON_LEAVE`, `ON_MATERNITY_LEAVE`, or `STUDENT`; V1 should refuse resurrection from dead statuses unless a later GM-only resurrection issue is opened.
- `RETIRED`, `RESIGNED`, or `LEFT` for deliberate non-tactical departure, with `retirementPayoutPolicy=none_requested`.
- `DEFECTED` or `DESERTED` for explicit RPG-side betrayal/abandonment events.
- Non-tactical death causes `HOMICIDE`, `ACCIDENTAL`, `NATURAL_CAUSES`, `UNDETERMINED`, or `SUICIDE`, only when `rpgEventType` states this is outside a MekHQ scenario.

V1 should not expose every `PersonnelStatus` value even though the enum supports them. The command should use a small source-reviewed allowlist and return `unsupported_status` for the rest.

## Refusal Rules

Refuse with `tactical_result_required` when:

- the event happened during a MekHQ-tracked scenario, tabletop battle, or MegaMek battle result
- MEK-RPG has entity damage, crew hits, ejection, pickup, salvage, or kill-credit facts
- the requested status is `KIA` from tactical combat

`Confirmed from source`: `ResolveScenarioTracker#processPersonnelStatus(...)` updates crew hits, XP, kills, `MIA`, `KIA`, capture/MIA consequences, fatigue, and retirement-defection payout handling during scenario closeout. Tactical casualties should continue through Resolve Manually/battle-record MUL workflows.

Refuse with `medical_command_required` for:

- `WOUNDS`, `DISEASE`, `CONTAGIOUS_DISEASE`, `MEDICAL_COMPLICATIONS`, `PREGNANCY_COMPLICATIONS`, prosthetics, injury recovery, hit recovery, or fatigue recovery

Refuse with `prisoner_command_required` for:

- changing `PrisonerStatus`
- freeing, executing, jettisoning, recruiting, or removing prisoners
- `ENEMY_BONDSMAN`, `BONDSREF`, `SEPPUKU`, `IMPRISONED`, or bondsman conversion flows

`Confirmed from source`: prisoner operations use separate `Person#setPrisonerStatus(...)`, prisoner capture utilities, and UI commands with confirmation prompts, faction-standing effects, reports, and sometimes `Campaign#removePerson(...)`.

Refuse with `retirement_payout_unsupported` when:

- the caller expects final payout, contract-break, family-follow, retirement-defection tracker, or employee-turnover behavior

`Confirmed from source`: `Campaign#applyRetirement(...)` has its own payout, defection/resignation, family departure, and turnover tracking behavior. The V1 endpoint should not pretend a direct status change is the same as full retirement processing.

Refuse with `dead_recovery_unsupported` when:

- current status is dead and target status is not dead

`Confirmed from source`: `changeStatus(...)` has a resurrection path, but resurrection is high-risk enough to keep out of V1.

Refuse with `stale_target` when:

- expected name, status, prisoner status, unit id/name, date, campaign id/name, or state revision does not match

Refuse with `prompt_required` when:

- visible dialogs exist before mutation or the command would need confirmation. V1 should not show or answer Swing prompts.

## Verification For Implementation

Implementation issue should be source-only plus disposable-campaign verified:

1. Add readiness row `personnel.status` with allowed status list and per-person selector candidates.
2. Add dry-run validation that returns intended before/after facts and side effects without mutation.
3. Apply through `Person#changeStatus(...)` on the Swing event dispatch thread.
4. Verify compile and checkstyle from `external/src/mekhq`.
5. Live-test on copied/disposable campaign personnel:
   - `ACTIVE -> MIA`
   - assigned crew `ACTIVE -> POW` removes unit crew assignment
   - `MIA -> ACTIVE`
   - `ACTIVE -> RETIRED` with `retirementPayoutPolicy=none_requested`
   - non-tactical `ACTIVE -> ACCIDENTAL` or `HOMICIDE`
6. Confirm after each mutation: personnel report count increased, status/service log changed, unit crew link cleared when expected, active personnel cache reflects the new status, salary eligibility changes, commander flags do not leave the campaign without a commander when second-in-command exists, and no save occurs unless `saveAfterSuccess=true`.

## Deferred Work

- Full tactical casualty application remains with issue `#10` and the Resolve Manually/battle-record MUL workflow.
- Medical injury/prosthetic/fatigue outcomes were source-designed in issue `#48`; fatigue implementation is tracked by issue `#53`, while broad treatment and prosthetic surgery remain blocked until source-owned medical/prosthetic services exist.
- Prisoner status changes, prisoner release/execution/removal, bondsman conversion, and faction-standing consequences need a separate prisoner command design.
- Retirement payouts and employee-turnover processing need a separate retirement/turnover command design if MEK-RPG needs those exact economics.
