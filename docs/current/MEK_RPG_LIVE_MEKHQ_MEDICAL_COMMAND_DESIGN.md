# MEK-RPG Live MekHQ Medical Command Design

Status: source design for GitHub issue `#48` on `2026-06-23`.

Purpose: define how MEK-RPG should request RPG-side medical, injury, prosthetic, fatigue, and medical-expense outcomes while MekHQ remains the authoritative roster and recovery ledger.

## Summary

`Confirmed from source`: MekHQ medical state is not one system. A guarded command must distinguish:

- Classic hit recovery through `Person#getHits()`, `Person#setHits(...)`, `Person#heal()`, and `MedicalController`.
- Advanced Medical injuries through `Person#getInjuries()`, `Person#addInjury(...)`, `Person#removeInjury(...)`, `Injury`, `InjuryType`, and `InjuryUtil`.
- Alternate Advanced Medical prosthetics and implants through permanent-modification `Injury` records, `ProstheticType`, and `AdvancedReplacementLimbDialog`.
- Fatigue through `Person#getFatigueDirect()`, `Person#getAdjustedFatigue()`, `Person#changeFatigue(...)`, `Person#setFatigue(...)`, and `Person#changePermanentFatigue(...)`.
- Medical expenses through finance transactions, with prosthetic surgery currently using `Finances#debit(TransactionType.REPAIRS, ...)` in `AdvancedReplacementLimbDialog` and classic limb replacement using `TransactionType.MEDICAL_EXPENSES` in `PersonnelTableMouseAdapter`.

`Decision`: V1 should not try to implement a complete "medical treatment" endpoint. The lowest-risk implementation slice is a guarded fatigue adjustment/recovery command, because it can use source-owned `Person#changeFatigue(...)`, has simple before/after verification, and does not need injury-generation randomness, surgery prompts, prosthetic availability rules, or doctor skill rolls.

`Decision`: structured injury treatment and prosthetic application should stay blocked until source code exposes a non-dialog medical service or a future source issue extracts the reusable logic from `AdvancedReplacementLimbDialog`. MEK-RPG should use `campaign.status_note` for narrative-only care that should not change MekHQ medical state.

## Source Map

`Confirmed from source`: `MedicalController` owns daily healing orchestration. It reads campaign options, validates doctor assignment, handles MASH theatre capacity, and routes to classic healing, `InjuryUtil.resolveDailyHealing(...)`, or `AdvancedMedicalAlternateHealing.processNewDay(...)`.

`Confirmed from source`: in non-advanced medical, `Person#heal()` reduces classic hits by one and clears the doctor assignment when no fixing remains. `MedicalController#checkNaturalHealing(...)` and its private doctor-assisted path reset wait periods and medical assignment state.

`Confirmed from source`: in Advanced Medical, `InjuryUtil.resolveDailyHealing(...)` generates `GameEffect` objects for treatment, untreated effects, and natural healing. Those effects can remove injuries, make injuries permanent, change recovery timers, award XP, consume Edge, update doctor task counters, add medical logs, and reset unit pilot/entity state.

`Confirmed from source`: in Alternate Advanced Medical, `AdvancedMedicalAlternateHealing.processNewDay(...)` evaluates all non-permanent injuries, applies prosthetic penalties, uses BODY or Surgery checks, may change fatigue when fatigue rules are enabled, may remove injuries, make them permanent, extend recovery, clear doctor assignments, and write medical or patient logs.

`Confirmed from source`: prosthetics and implants are modeled as permanent-modification injuries. `InjurySubType#isPermanentModification()` returns true for prosthetic or implant subtypes, `Person#getProstheticInjuries()` filters those records, and `Injury#getHits()` treats prosthetic, implant, and flaw injuries as zero TW-scale hits in the alternate model.

`Confirmed from source`: `AdvancedReplacementLimbDialog` currently owns prosthetic application side effects. Normal confirm pays for surgeries, chooses a local surgeon if selected, performs Surgery checks, adds skill-check reports, removes old injuries, adds marker injuries, adds recovery injuries, grants implant/personnel options according to campaign options, can add failed-surgery recovery, calls `campaign.personUpdated(patient)`, and can change status to `MEDICAL_COMPLICATIONS` if total injury severity reaches the death threshold. GM confirm applies the selected prosthetics without rolls or payment.

`Confirmed from source`: classic replacement-limb handling in `PersonnelTableMouseAdapter#replaceLimb(...)` is a separate UI path. It finds sufficiently skilled doctors, increases cost when none are available, opens `ReplacementLimbDialog`, debits `TransactionType.MEDICAL_EXPENSES`, replaces the lost-limb injury with `InjuryTypes.REPLACEMENT_LIMB_RECOVERY`, marks it worked on, and resets the unit.

## Endpoint Family

Use separate commands rather than one broad endpoint:

- `POST /campaign/command/personnel/fatigue`: first safe implementation slice.
- `POST /campaign/command/personnel/medical-treatment`: future structured injury treatment command, blocked for V1.
- `POST /campaign/command/personnel/prosthetic-surgery`: future prosthetic or implant command, blocked until reusable source service exists.
- `POST /campaign/command/personnel/medical-expense`: future GM medical expense correction, only if it is not masquerading as treatment or purchase logic.

All commands must use the shared command envelope from `MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`: campaign/date guards, person selector and target guards, explicit `dryRun`, process-local idempotency, `promptPolicy=refuse_if_prompt`, opt-in save policy, prompt/save facts, before/after target facts, warnings, unsupported entries, and audit context.

## V1 Fatigue Command

Recommended endpoint: `POST /campaign/command/personnel/fatigue`.

Allow only explicit RPG/rest/event fatigue changes:

- `fatigueDelta`: integer, positive for fatigue gain, negative for recovery.
- `expectedFatigueDirect`: required.
- `expectedAdjustedFatigue`: required when fatigue-affecting SPAs or flaws are active.
- `expectedPermanentFatigue`: required for visibility, but V1 should not mutate permanent fatigue.
- `reason`: plain-text RPG/source explanation.
- `clientContext`: shared audit context.
- `appendAuditReport`: optional, default true, using `DailyReportType.GENERAL` or a future medical report category only after source-reviewing report text expectations.

Apply mode should call `Person#changeFatigue(delta)`, not `setFatigue(...)`, because `changeFatigue(...)` applies source-owned fatigue gain scaling from SPAs and flaws and clamps to MekHQ's fatigue range. A direct set should remain a future GM correction command, not a normal RPG event command.

V1 refusal rules:

- Refuse if campaign fatigue rules are disabled unless the request explicitly says this is a GM correction and a later command version supports that.
- Refuse permanent fatigue mutation.
- Refuse if the selected person is not active, is dead, or the expected status/prisoner/unit guards do not match.
- Refuse if the before-state fatigue fields do not match.
- Refuse if `promptPolicy` is not `refuse_if_prompt`.
- Refuse if the request tries to combine fatigue with injury healing, prosthetic application, medical expense payment, personnel status change, or day advancement.

Verification facts:

- before/after raw fatigue
- before/after adjusted fatigue
- before/after permanent fatigue
- fatigue campaign options: enabled flag and fatigue rate
- current status, prisoner status, unit assignment, and medical injury counts
- report count before/after when audit report is appended
- prompt and save facts

## Structured Injury Treatment

Future endpoint: `POST /campaign/command/personnel/medical-treatment`.

This should not be implemented as "delete an injury" or "set hits to zero" from MEK-RPG. Source review shows healing can change XP, task counters, Edge, doctor assignment, unit reset state, medical logs, report logs, injury permanence, recovery timers, fatigue, and death status.

Safe future approaches:

- "Run one source-owned daily medical tick for this patient" only if it can be isolated from full campaign day advancement and returns all side effects.
- "GM adjudicated injury removal" only if explicitly marked as GM correction, with exact injury UUIDs, expected injury facts, no doctor roll, no cost, and an audit report.
- "Classic hit recovery" only for non-advanced-medical campaigns, using `Person#heal()` or the same logic as `MedicalController`, with expected hits and wait-period facts.

Refuse in V1:

- adding arbitrary injury types by display name
- deleting or marking healed an injury without exact injury UUID and expected injury facts
- forcing a treatment roll from outside the source-owned medical controller
- changing doctor assignment or wait periods as part of a broad command
- tactical casualty or scenario-result injuries that belong in MekHQ's scenario resolution/MUL workflow
- any operation whose preview cannot be dry-run without mutation

## Prosthetics And Implants

Future endpoint: `POST /campaign/command/personnel/prosthetic-surgery`.

`Confirmed from source`: prosthetic state belongs to Alternate Advanced Medical as permanent-modification injury records and may also grant pilot or personnel options. It is not safe to model prosthetics as a plain note.

Required source work before implementation:

- Extract a reusable service from `AdvancedReplacementLimbDialog` for planning, validating, pricing, applying, and reporting prosthetic surgeries without Swing UI state.
- Keep source-owned eligibility checks for body location, faction, year, planetary tech, location/on-planet state, legality, local surgeon, surgery skill, Edge, cost, recovery injury type, old-injury removal, marker injury creation, associated options, unit reset, report/log entries, and medical-complication death.
- Expose a readiness selector for eligible prosthetic types and exact body locations. Selectors must use prosthetic enum keys and `BodyLocation`, not localized display names.

V1 should refuse:

- prosthetic or implant requests when alternate advanced medical is disabled
- requests selected by localized name or UI row
- requests that need an interactive confirmation dialog
- normal surgery with rolls until dry-run can preview all possible side effects or the response clearly marks roll-dependent outcomes
- payment-only surgery that does not apply source-owned prosthetic state
- direct removal of existing prosthetic injuries unless designed as a separate GM correction command

## Medical Expenses

`Confirmed from source`: medical costs are not all under one transaction type. Prosthetic surgery uses `TransactionType.REPAIRS` in `AdvancedReplacementLimbDialog`, while classic limb replacement uses `TransactionType.MEDICAL_EXPENSES` in `PersonnelTableMouseAdapter#replaceLimb(...)`.

`Decision`: do not implement generic "pay medical bill" as part of the first medical command. If MEK-RPG needs a manual expense correction, use a future GM funds/expense command with expected balance, transaction type, amount, reason, and audit context. Treatment and prosthetic commands should own their source-backed costs when they become safe.

## Readiness Changes

`GET /campaign/commands` should continue reporting broad `personnel.medical_treatment` as blocked until a source-owned treatment service exists.

Implemented readiness rows from issue `#53`:

- `personnel.fatigue`: available when a campaign is loaded and fatigue rules are enabled; otherwise blocked with `fatigue_rules_disabled`.
- `personnel.medical_treatment`: blocked with `source_service_missing` or `option_aware_design_required`.
- `personnel.prosthetic_surgery`: blocked with `prosthetic_service_missing` until dialog logic is extracted.
- `personnel.medical_expense`: blocked unless a future GM expense command is accepted.

## Follow-Up

Issue `#53` implemented `POST /campaign/command/personnel/fatigue` in local MekHQ source commit `ef6ef99ef9`.

`Confirmed from source`: V1 calls `Person#changeFatigue(...)`, validates the shared command envelope, requires person/name/status/prisoner/unit and fatigue guards, refuses disabled fatigue rules, inactive or dead personnel, visible dialogs, mixed medical/prosthetic/expense/status effects, permanent-fatigue mutation, unsupported prompt/save policy, and stale target facts.

`Confirmed locally`: `.\gradlew.bat :MekHQ:compileJava` and `.\gradlew.bat :MekHQ:checkstyleMain` passed from `external/src/mekhq`.

`Unknown`: live disposable-campaign smoke testing has not run. It requires a source-built MekHQ instance launched with `mekhq.controlApi.enabled=true` and a copied campaign loaded.
