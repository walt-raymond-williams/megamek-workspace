# Agent Handoff: Confirm Battle-Record MUL Source Workflow

## Issue

- GitHub issue: `#8`
- Roadmap entry: `Epic: Robust tabletop battle result MUL workflow`
- Priority: `High`

## Goal

Confirm, from local MegaMek/MekHQ source, the exact workflow MekHQ uses to export scenario MULs and import battle-record MULs during Resolve Manually.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/GITHUB_ISSUE_WORKFLOW.md`
- `docs/current/MEGAMEK_PROJECT_PROFILE.md`
- `docs/current/SOURCE_CODE_GUIDE.md`
- `docs/current/KNOWN_COMMANDS.md`
- `docs/handoffs/active/robust-tabletop-battle-result-mul-workflow.md`

## Expected Output

- Completed on `2026-06-18`: source-grounded workflow notes were added in `docs/current/TABLETOP_RESULT_MUL_WORKFLOW.md`, with a pointer from `docs/current/SOURCE_CODE_GUIDE.md`.
- Completed on `2026-06-18`: confirmed classes, methods, and data flow for scenario export, manual resolution, battle-record MUL parsing, campaign unit matching, personnel/casualty handling, salvage, and scenario status.
- Completed on `2026-06-18`: roadmap sequencing was updated to recommend issue `#7` next.

## Files And Areas

- `external/src/mekhq/MekHQ/src/mekhq/gui/BriefingTab.java`
- `external/src/mekhq/MekHQ/src/mekhq/GameThread.java`
- `external/src/mekhq/MekHQ/src/mekhq/MekHQ.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/ResolveScenarioTracker.java`
- `external/src/megamek/megamek/src/megamek/common/units/EntityListFile.java`
- `external/src/megamek/megamek/src/megamek/common/loaders/MULParser.java`
- `docs/current/SOURCE_CODE_GUIDE.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`

## Commands

```powershell
git status --short --branch
rg -n "EntityListFile.saveTo|saveDeployUnits|resolveScenario|ChooseMulFilesDialog|processMulFiles" external/src/mekhq/MekHQ/src
rg -n "ELE_SURVIVORS|ELE_SALVAGE|ELE_RETREATED|ELE_DEVASTATED|ELE_KILLS|saveTo\(File file, Client" external/src/megamek/megamek/src/megamek/common
```

## Constraints

- Do not modify MegaMek/MekHQ source in this issue.
- Preserve uncertainty and evidence labels.
- Commit completed repository tracking changes before stopping unless explicitly told not to.

## Acceptance Criteria

- Completed: source references are cited with file paths and method/class names.
- Completed: the documented flow covers export, manual result selection, MUL parsing, entity-to-campaign mapping, personnel/casualty handling, salvage, and scenario status.
- Completed: unconfirmed behavior is clearly labeled as inferred or unresolved.

## Open Questions

- Deferred to `#10`: validate the exact minimal XML needed for a generated battle-record MUL with a real or generated round trip.
- Deferred to `#9`: define the minimum tabletop result input schema using the confirmed battle-record sections and external-id requirements.
