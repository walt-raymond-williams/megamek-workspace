# MEK-RPG Live MekHQ Personnel Detail API

## Purpose

This note closes GitHub issue `#82` by recording the source audit, V1 read-only endpoint design, implementation, verification, and remaining gaps for exposing Personnel tab selected-character details through the local MekHQ control API.

## Source Audit

`Confirmed from source`: `PersonnelTab` does not provide a stable API-facing selected-person identifier. It builds the detail surface by reading the selected table row and constructing `PersonViewPanel` with that `Person`. The local API should therefore require an explicit `personId` instead of guessing from Swing selection.

Source owners:

- `PersonnelTab`: selection-to-detail-panel flow through `getSelectedPersons()` and `refreshPersonView()`.
- `PersonViewPanel`: selected-person profile composition, including identity, awards, skills, options, fatigue/other summary panels, and log panels.
- `Person`: stable source for ids, names, role/status/prisoner state, unit assignment, XP, fatigue, salary, biography, skills, options, injuries, and serialized log families.
- `Person` education getters: current education stage, academy set/name, resolved school display name, course index, remaining education days, journey-time fields, academy faction, academy system, and highest education.
- `EducationController#getAcademy(...)` and `Academy`: current academy/course metadata, including qualifications, curriculum, academy type, and duration.
- `ServiceLogger`: education enrollment, re-enrollment, graduation, failed graduation, failed application, and returned-from-education personal log entries.
- `PersonnelOptions`, `IOptionGroup`, and `IOption`: stable option ids and display labels for traits/options/special abilities.
- `Skill`, `SkillType`, and `Skills`: skill ids/display names, subtypes, levels, bonuses, XP progress, and adjusted values.
- `LogEntry` and `Person` log getters: personal, assignment, performance, scenario, medical, and patient log families.
- `LocalCampaignStateExporter` and `LocalControlService`: existing local API envelope, sanitization, timing, and endpoint routing.

## V1 Endpoint

`Confirmed from source`: MekHQ source commit `b68bc1b8ca` adds:

```text
GET /campaign/personnel/detail?personId=<uuid>
```

Optional query parameters:

- `includeMedical=true`: include the selected person's medical log family.
- `includePatient=true`: include the selected person's patient log family.
- `logLimit=<n>`: per-family log limit; defaults to `10`, clamps to `50`.

Response behavior:

- Returns HTTP `409` when no campaign is loaded.
- Returns HTTP `400` for missing or invalid `personId`.
- Returns HTTP `404` when the loaded campaign has no person with that UUID.
- Returns HTTP `200` with `schema_name: "mekhq-live-personnel-detail"` for a found person.
- Includes `state_revision`, campaign id/name/date, endpoint timing, collector timing, and read-only metadata.

Default payload includes:

- core identity and biography, sanitized and bounded.
- role/status/prisoner/fatigue/hits/XP/salary facts.
- unit and formation assignment context plus existing assignment guards.
- education summary with current record, highest education, latest education history event, bounded education history, and assignment-review flag.
- skills with name, display label, subtype, roleplay flag, level, bonus, XP progress, natural aptitude, final value, and experience level.
- active options/special abilities with stable option ids, group ids, group labels, display labels, and values.
- award summary flags and counts.
- injury summary reused from the current personnel state section.
- personal, assignment, performance, and scenario logs, sanitized and bounded.

Sanitized fixture:

- `docs/templates/mekhq-live-personnel-detail.fixture.json`

Privacy/default behavior:

- Medical and patient logs are excluded by default even though the response requires a single explicit `personId`.
- Excluded sensitive families report `status: "excluded"`, `available_count`, and the required query flag.
- This keeps broad dashboard refreshes and ordinary character-sheet reads from silently pulling medical/patient history.

## Education Support

`Confirmed from source`: MekHQ source commit `b9f42710a9` extends the read-only local API with student support for issue `#109`.

Whole-roster `GET /campaign/state?sections=personnel` now includes compact per-person fields:

- `education_summary`
- `skill_summary`
- `traits_or_options_summary`

Person detail now includes richer `person.education`:

- `highest_education`
- `current_record.education_status`
- `current_record.stage`
- `school_id`, `school_name`, `academy_set`, `academy_name_in_set`, `academy_faction`, `academy_system_id`
- `program_id`, `program_name`, `course_index`
- `credential_or_qualification`, `target_role`
- `enrolled_on`, `expected_graduation_on`, `days_remaining`, `education_days_remaining`
- `journey_time_days`, `travel_days_elapsed`
- `actual_graduation_on`
- `requires_assignment_review`
- `latest_history_event`
- bounded `history`

`Confirmed from source`: current enrollment/progress comes from `Person#getEduEducationStage()`, `Person#getEduAcademyName()`, `Person#getEduAcademySet()`, `Person#getEduAcademyNameInSet()`, `Person#getEduCourseIndex()`, `Person#getEduEducationTime()`, `Person#getEduJourneyTime()`, `Person#getEduDaysOfTravel()`, `Person#getEduHighestEducation()`, and related getters. Program names prefer resolved `Academy#getQualifications()` through `EducationController#getAcademy(...)`.

`Confirmed from source`: MekHQ clears current education fields when a student changes back to active status, so historical enrollment and graduation dates are reconstructed from bounded personal service-log entries created by `ServiceLogger.eduEnrolled(...)`, `eduReEnrolled(...)`, `eduGraduated(...)`, `eduGraduatedPlus(...)`, `eduGraduatedMasters(...)`, `eduGraduatedDoctorate(...)`, `eduFailed(...)`, and `returnedFromEducation(...)`.

`Inference from source`: `requires_assignment_review` is true for pending graduation/return stages, and for recently returned/graduated active personnel who are still `Recruit` rank or have no unit assignment. This is a MEK-RPG helper flag, not a MekHQ native job-assignment prompt.

## Deferred Scope

`Confirmed from source`: V1 exposes award summary flags only. Individual award names, tier/icon/tool-tip metadata, detailed family relationship sections, kill log details, and full medical/injury treatment objects remain deferred.

`Confirmed from source`: current education records are single-record `Person` fields, not a separate multi-record enrollment table. Multi-entry education history is reconstructed from personal service logs and can be incomplete if older saves lack those entries or if localized/edited text does not match the current `ServiceLogger` strings.

`Decision`: This endpoint is a character detail endpoint, not the final activity-history API. It can expose bounded per-person log families for the requested person, but issue `#58` should still design the broader history endpoint before roster-wide or cross-domain timeline exports are implemented.

## Verification

`Confirmed locally`: from `external/src/mekhq` after source commit `b68bc1b8ca`:

```powershell
.\gradlew.bat --no-daemon :MekHQ:test --tests mekhq.service.LocalCampaignStateExporterTest --tests mekhq.service.LocalControlServiceHttpTest
.\gradlew.bat --no-daemon :MekHQ:compileJava :MekHQ:checkstyleMain :MekHQ:checkstyleTest
```

Both commands returned `BUILD SUCCESSFUL` on `2026-06-30`.

`Confirmed locally`: from `external/src/mekhq` after source commit `b9f42710a9`:

```powershell
.\gradlew.bat --no-daemon :MekHQ:test --tests mekhq.service.LocalCampaignStateExporterTest
.\gradlew.bat --no-daemon :MekHQ:compileJava :MekHQ:checkstyleMain :MekHQ:checkstyleTest
```

Both commands returned `BUILD SUCCESSFUL` on `2026-08-21`.

`Blocked`: pushing the MekHQ source commit to `origin` failed because `origin` is `https://github.com/MegaMek/mekhq.git` and GitHub returned `403` for the current account.
