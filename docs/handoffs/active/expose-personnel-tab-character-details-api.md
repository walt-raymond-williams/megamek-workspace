# Agent Handoff

## Issue

- GitHub issue: `#82`
- Roadmap entry: `Expose Personnel tab character details through the live MekHQ API`
- Priority: `High`

## Goal

Make a real source-backed plan and implementation for exposing MekHQ Personnel tab selected-character details through the local read-only MekHQ API.

The target is the information a user can inspect after selecting a person in the Personnel tab: details, traits/options, roleplay and campaign skills, personal/service logs, medical/assignment/scenario/performance logs where safe, special abilities, awards, assignments, status, and other profile facts that are useful to MEK-RPG or a campaign aide.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_ACTIVITY_HISTORY_SOURCE_AUDIT.md`
- `docs/handoffs/active/design-mekhq-activity-history-api.md`
- `docs/handoffs/active/implement-mekhq-person-activity-log-export.md`

## Expected Output

- Source audit note under `docs/current/` mapping the Personnel tab selected-person UI to source owners and current API gaps.
- A concrete read-only API design for person details, including endpoint path or state-section choice, query parameters, response shape, default limits, privacy defaults, sanitization, unsupported entries, and selector rules.
- MekHQ source implementation if the audit/design confirms a narrow safe V1.
- Fixture and regression coverage for representative person detail rows.
- Updates to `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`, `docs/current/ROADMAP.md`, and `docs/current/TASKS.md`.

## Files And Areas

Likely files to read or edit:

- `external/src/mekhq/MekHQ/src/mekhq/gui/PersonnelTab.java`
- `external/src/mekhq/MekHQ/src/mekhq/gui/view/PersonViewPanel.java`
- `external/src/mekhq/MekHQ/src/mekhq/gui/model/PersonnelEventLogModel.java`
- `external/src/mekhq/MekHQ/src/mekhq/gui/model/PersonnelKillLogModel.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/personnel/Person.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/personnel/PersonnelOptions.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/personnel/SpecialAbility.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/personnel/skills/Skill.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/personnel/skills/SkillType.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporter.java`
- MekHQ service tests under `external/src/mekhq/MekHQ/test/`
- Existing live API fixtures under `docs/templates/`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "class PersonViewPanel|PersonnelTab|PersonnelEventLogModel|PersonnelKillLogModel|getPersonalLog|getMedicalLog|getScenarioLog|getAssignmentLog|getPerformanceLog|getOptions|getSkill|SpecialAbility" external/src/mekhq/MekHQ/src -g "*.java"
rg -n "personnelSection|sections=|sanitize|unsupported|state_revision" external/src/mekhq/MekHQ/src/mekhq/service -g "*.java"
.\gradlew.bat --no-daemon :MekHQ:test --tests mekhq.service.LocalCampaignStateExporterTest
.\gradlew.bat --no-daemon :MekHQ:test --tests mekhq.service.LocalControlServiceHttpTest
.\gradlew.bat --no-daemon :MekHQ:compileJava :MekHQ:checkstyleMain :MekHQ:checkstyleTest
```

## Constraints

- Preserve the disabled-by-default, loopback-only, read-only API posture.
- Do not scrape Swing UI text if a source model getter exists.
- Keep default responses bounded; do not dump every log for every person by default.
- Coordinate with activity-history issue `#58` and person-log issue `#60`; do not create a second incompatible log/history shape.
- Treat medical and patient logs as sensitive: require explicit person target and explicit include flags unless the issue `#58` design decides otherwise.
- Do not edit campaign saves.
- If source changes are made in `external/src/mekhq`, follow `docs/current/SOURCE_CHANGE_WORKFLOW.md`.

## Acceptance Criteria

- The source audit identifies the Personnel tab/detail-panel source owners and labels confirmed versus inferred fields.
- The API exposes a selected-person/detail-oriented read path using stable person identifiers and current campaign/read metadata.
- V1 includes core identity, role/status, assignment/unit/force summary, skills, traits/options/special abilities where source-safe, and bounded log/detail families.
- Sensitive log families have explicit opt-in behavior and are excluded from broad roster dumps.
- Response entries include enough machine-readable identifiers and display labels for MEK-RPG to present character sheets without guessing.
- Fixtures/tests cover at least one person with combat skills, roleplay/noncombat skills, traits/options or special abilities when available, assignment data, and representative log rows.
- Documentation records any unsupported Personnel tab subpanels or fields.

## Open Questions

- Should V1 be `GET /campaign/personnel/{personId}`, `GET /campaign/personnel/detail?personId=...`, a `person_detail` state section, or a combination?
- Which MekHQ trait/option values are stable enough to expose as machine-readable identifiers versus display-only labels?
- Should personal/service/award logs live in the person-detail response, the future history endpoint, or both with shared DTOs?
- How should API consumers request medical/patient details without accidentally exposing them in dashboard-wide refreshes?
