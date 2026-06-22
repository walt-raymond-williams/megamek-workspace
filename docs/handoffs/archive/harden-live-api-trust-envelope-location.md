# Agent Handoff

## Issue

- GitHub issue: `#39`
- Roadmap entry: `Harden live API trust envelope, dirty state, and location labels`
- Priority: `High`

## Goal

Harden the existing read-only live MekHQ campaign API around `bridge_metadata`, dirty/unsaved-state reporting, selected-section metadata, structured unsupported entries, and stable current system/location labels.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEGAMEK_LIVE_API_CHANGE_REQUEST.md`

## Expected Output

- Source/API update or source-backed implementation plan for dirty/unsaved campaign state. If no safe source owner exists, keep `Unknown` but emit a structured unsupported entry with owner/evidence metadata.
- Full and sparse `GET /campaign/state` responses can include stable, human-readable current system and location labels when campaign data exists.
- Selected-section docs and fixtures include `bridge_metadata` explicitly.
- Unsupported entries consistently include `area`, `field`, `reason`, `evidence`, `recommended_owner`, and `blocks_automation` where practical.
- Workspace docs record verification and any remaining uncertainty.

## Files And Areas

Likely files to read or edit:

- `external/src/mekhq`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`
- `docs/templates/mekhq-live-campaign-*.fixture.json`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
Get-ChildItem -Path external/src/mekhq -Force
rg "campaign/state|bridge_metadata|unsupported|dirty|Location|Current" external/src/mekhq
.\gradlew.bat :MekHQ:compileJava
.\gradlew.bat :MekHQ:checkstyleMain
```

Run Gradle commands from `external/src/mekhq`. Record exact blockers if source verification cannot run.

## Constraints

- Preserve the live API boundary: local-only, read-only, disabled by default unless explicitly enabled.
- Do not add write/action APIs.
- Do not parse active `.cpnx`, `.cpnx.gz`, or XML saves as the normal loaded-campaign path.
- Use copied/disposable saves for any live endpoint smoke test.
- Do not include unrelated user changes.
- Commit completed workspace changes before stopping unless explicitly told not to.

## Acceptance Criteria

- Issue `#39` has source-backed findings or source changes for trust envelope, dirty-state reporting, location labels, selected-section examples, and unsupported-entry shape.
- Verification commands or blockers are recorded.
- Roadmap/task board are updated at close-out.

## Open Questions

- Is there a safe MekHQ source owner for dirty/unsaved campaign state?
- What exact fields should be used for a table-safe current location label when the campaign has travel or system context but no richer location object?

## Completion Notes

- Completed on `2026-06-22` for GitHub issue `#39`.
- MekHQ source commit: `dc214d946d` (`Harden live campaign state metadata`).
- Verification passed from `external/src/mekhq`: `.\gradlew.bat :MekHQ:compileJava` and `.\gradlew.bat :MekHQ:checkstyleMain`.
- Dirty/unsaved state remains `Unknown`; source search found editor-local unsaved state but no campaign-wide dirty flag for the loaded campaign.
- Location output now avoids relying on `AbstractLocation#toString()` as the only display value and adds `current_location_display_name`, `table_safe_location_label`, `current_planet_name`, and expanded `travel_state` fields.
