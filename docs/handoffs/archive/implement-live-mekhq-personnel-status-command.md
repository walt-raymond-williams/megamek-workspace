# Agent Handoff

## Issue

- GitHub issue: `#51`
- Roadmap entry: `Implement guarded live MekHQ personnel status command`
- Priority: `High`
- Parent epic: `#44`

## Goal

Implement the V1 guarded personnel status command for MEK-RPG events that happen outside MekHQ tactical scenario resolution.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_PERSONNEL_STATUS_COMMAND_DESIGN.md`
- `docs/current/GUARDED_LIVE_MEKHQ_COMMAND_API_TRACKING.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- MekHQ source around `LocalControlService`, `LocalCommandReadinessExporter`, `Person#changeStatus(...)`, `PersonnelStatus`, `Person#setPrisonerStatus(...)`, `PrisonerStatus`, `Unit#remove(...)`, `CampaignEventProcessor`, `ResolveScenarioTracker`, and `CapturePrisoners`.

## Expected Output

- `POST /campaign/command/personnel/status`.
- `GET /campaign/commands` readiness row for `personnel.status`.
- Shared command-envelope validation: campaign/date/target guards, idempotency, required `dryRun`, opt-in save, and `promptPolicy=refuse_if_prompt`.
- Dry-run response with before/after facts and intended side effects.
- Apply path that calls `Person#changeStatus(Campaign, LocalDate, PersonnelStatus)`, not `setStatus(...)`.
- Refusal rules from the design note.
- Compile and checkstyle verification from `external/src/mekhq`.

## V1 Allowlist

Allow only single-person transitions:

- `MIA` for narrative disappearance.
- `POW` for player personnel captured outside tactical resolution.
- `ACTIVE` from `MIA`, `POW`, `MISSING`, `AWOL`, `ON_LEAVE`, `ON_MATERNITY_LEAVE`, or `STUDENT`.
- `RETIRED`, `RESIGNED`, or `LEFT` only with `retirementPayoutPolicy=none_requested`.
- `DEFECTED` or `DESERTED`.
- `HOMICIDE`, `ACCIDENTAL`, `NATURAL_CAUSES`, `UNDETERMINED`, or `SUICIDE` only for non-tactical death events.

## Required Refusals

- `tactical_result_required`: tactical casualties, tactical `KIA`, crew hits, ejection, pickup, salvage, or kill-credit facts.
- `medical_command_required`: wounds, disease, medical complications, prosthetics, hit recovery, fatigue recovery, injury recovery.
- `prisoner_command_required`: prisoner status mutation, freeing, executing, jettisoning, recruiting, removing prisoners, bondsman conversion.
- `retirement_payout_unsupported`: final payout, employee-turnover, family-follow, or contract-break processing expected.
- `dead_recovery_unsupported`: resurrection from dead statuses.
- `unsupported_status`, `stale_target`, and `prompt_required` as applicable.

## Verification

Required:

- `.\gradlew.bat :MekHQ:compileJava` from `external/src/mekhq`.
- `.\gradlew.bat :MekHQ:checkstyleMain` from `external/src/mekhq`.

Live disposable campaign smoke tests if a source-built MekHQ instance is available:

- `ACTIVE -> MIA`.
- Assigned crew `ACTIVE -> POW` removes unit crew assignment.
- `MIA -> ACTIVE`.
- `ACTIVE -> RETIRED` with no payout.
- One non-tactical death cause.

Record blockers explicitly if live testing is not possible.

## Constraints

- Do not route scenario/tabletop casualties through this endpoint.
- Do not mutate `PrisonerStatus` in V1.
- Do not implement retirement payouts or employee-turnover behavior in V1.
- Do not resurrect dead personnel in V1.
- Do not direct-write save files.
