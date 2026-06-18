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

- Source-grounded workflow notes in `docs/current/SOURCE_CODE_GUIDE.md` or a focused `docs/current/` note.
- Confirmed classes, methods, and data flow for scenario export, manual resolution, battle-record MUL parsing, campaign unit matching, personnel/casualty handling, salvage, and scenario status.
- Updates to the epic handoff if source confirmation changes the issue sequence.

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

- Source references are cited with file paths and method/class names.
- The documented flow covers export, manual result selection, MUL parsing, entity-to-campaign mapping, personnel/casualty handling, salvage, and scenario status.
- Unconfirmed behavior is clearly labeled as inferred or unresolved.

## Open Questions

- Which battle-record MUL fields does MekHQ consume directly versus compute after scenario resolution?
- Which fields are required for a minimal tabletop result import?
