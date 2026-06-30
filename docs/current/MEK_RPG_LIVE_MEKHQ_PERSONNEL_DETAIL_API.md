# MEK-RPG Live MekHQ Personnel Detail API

## Purpose

This note closes GitHub issue `#82` by recording the source audit, V1 read-only endpoint design, implementation, verification, and remaining gaps for exposing Personnel tab selected-character details through the local MekHQ control API.

## Source Audit

`Confirmed from source`: `PersonnelTab` does not provide a stable API-facing selected-person identifier. It builds the detail surface by reading the selected table row and constructing `PersonViewPanel` with that `Person`. The local API should therefore require an explicit `personId` instead of guessing from Swing selection.

Source owners:

- `PersonnelTab`: selection-to-detail-panel flow through `getSelectedPersons()` and `refreshPersonView()`.
- `PersonViewPanel`: selected-person profile composition, including identity, awards, skills, options, fatigue/other summary panels, and log panels.
- `Person`: stable source for ids, names, role/status/prisoner state, unit assignment, XP, fatigue, salary, biography, skills, options, injuries, and serialized log families.
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

## Deferred Scope

`Confirmed from source`: V1 exposes award summary flags only. Individual award names, tier/icon/tool-tip metadata, detailed family relationship sections, kill log details, and full medical/injury treatment objects remain deferred.

`Decision`: This endpoint is a character detail endpoint, not the final activity-history API. It can expose bounded per-person log families for the requested person, but issue `#58` should still design the broader history endpoint before roster-wide or cross-domain timeline exports are implemented.

## Verification

`Confirmed locally`: from `external/src/mekhq` after source commit `b68bc1b8ca`:

```powershell
.\gradlew.bat --no-daemon :MekHQ:test --tests mekhq.service.LocalCampaignStateExporterTest --tests mekhq.service.LocalControlServiceHttpTest
.\gradlew.bat --no-daemon :MekHQ:compileJava :MekHQ:checkstyleMain :MekHQ:checkstyleTest
```

Both commands returned `BUILD SUCCESSFUL` on `2026-06-30`.

`Blocked`: pushing the MekHQ source commit to `origin` failed because `origin` is `https://github.com/MegaMek/mekhq.git` and GitHub returned `403` for the current account.
