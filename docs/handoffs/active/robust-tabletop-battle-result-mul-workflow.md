# Agent Handoff: Robust Tabletop Battle Result MUL Workflow

## Issue

- GitHub issue: `#6`
- Roadmap entry: `Epic: Robust tabletop battle result MUL workflow`
- Priority: `High`

## Goal

Coordinate the decomposed epic for a robust workflow where MekHQ generates a scenario, the user plays the tactical battle by hand on the tabletop, and the results are fed back through MekHQ's built-in manual scenario resolution/import capabilities.

Current posture: this is a learning and workflow-discovery effort. The existing discovery remains valuable even if the final recommendation is to use MekHQ's built-in workflow with documentation and no custom generator. Do not treat custom implementation as locked in before strategy issue `#11`.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/GITHUB_ISSUE_WORKFLOW.md`
- `docs/current/MEGAMEK_PROJECT_PROFILE.md`
- `docs/current/SOURCE_CODE_GUIDE.md`
- `docs/current/KNOWN_COMMANDS.md`

Source references already identified:

- `external/src/mekhq/MekHQ/src/mekhq/gui/BriefingTab.java`
  - MekHQ exports deployed player, ally, and bot forces with `EntityListFile.saveTo(...)`.
- `external/src/mekhq/MekHQ/src/mekhq/GameThread.java`
  - MekHQ sets player unit `externalId` to the campaign unit UUID before sending entities to MegaMek.
- `external/src/mekhq/MekHQ/src/mekhq/MekHQ.java`
  - `resolveScenario(...)` opens `ChooseMulFilesDialog`, processes MUL files, then runs the resolve wizard and post-scenario handler.
- `external/src/mekhq/MekHQ/src/mekhq/campaign/ResolveScenarioTracker.java`
  - Parses battle-record MULs, maps entities back to campaign units by `externalId`, processes personnel, salvage, BLC, loot, and scenario status.
- `external/src/megamek/megamek/src/megamek/common/units/EntityListFile.java`
  - Writes ordinary unit MULs and battle-record MULs with `survivors`, `allies`, `salvage`, `retreated`, `devastated`, and `kills`.
- `external/src/megamek/megamek/src/megamek/common/loaders/MULParser.java`
  - Reads battle-record MUL sections consumed by MekHQ.

## Expected Output

- Child GitHub issues for discovery, prototype, strategy, possible implementation, verification, and documentation. Created issues: `#7` through `#13`.
- Clear recommendation on whether the work should use a feature integration branch and feature tracking doc. Current recommendation: defer branch/tracking creation until strategy issue `#11`; create `codex/tabletop-result-mul-dev` before `#12` if implementation spans source changes or multiple commits.
- A durable design note under `docs/current/` if decomposition confirms architecture or workflow decisions that future agents need.
- Updated `ROADMAP.md` and `TASKS.md` reflecting the decomposed plan.

## Child Issues

- `#7`: Investigate MekHQ and BattleTech salvage rules.
  - Status: Completed on `2026-06-18`.
  - Findings: `docs/current/SALVAGE_RULES_NOTES.md`
  - Handoff: `docs/handoffs/archive/investigate-salvage-rules.md`
- `#8`: Confirm battle-record MUL source workflow for tabletop result import.
  - Status: Completed on `2026-06-18`.
  - Findings: `docs/current/TABLETOP_RESULT_MUL_WORKFLOW.md`
  - Handoff: `docs/handoffs/archive/confirm-battle-record-mul-source-workflow.md`
- `#9`: Define tabletop battle result input schema for MekHQ MUL generation.
  - Handoff: `docs/handoffs/active/define-tabletop-result-input-schema.md`
- `#10`: Prototype battle-record MUL round-trip validation against MekHQ.
  - Handoff: `docs/handoffs/active/prototype-battle-record-mul-round-trip.md`
- `#11`: Choose MUL generation strategy for tabletop result workflow.
  - Handoff: `docs/handoffs/active/choose-tabletop-result-mul-generation-strategy.md`
- `#12`: Implement robust tabletop battle-record MUL generator.
  - Handoff: `docs/handoffs/active/implement-tabletop-battle-record-mul-generator.md`
- `#13`: Verify and document tabletop result entry workflow for MekHQ.
  - Handoff: `docs/handoffs/active/verify-document-tabletop-result-entry-workflow.md`

Recommended sequence: `#8` and `#7` are complete. Start `#9` next, then proceed through `#10` and `#11`. Issue `#11` must explicitly decide whether `#12` should proceed, shrink to a documentation/helper task, or be closed as unnecessary because MekHQ's built-in workflow is sufficient. Finish with `#13` UI/manual documentation.

Source workflow confirmation from `#8`: MekHQ exports scenario setup MULs as ordinary `<unit>` files, but imports manual battle results through a battle-record `<record>` MUL with `survivors`, `allies`, `salvage`, `retreated`, `devastated`, and `kills` sections. Friendly campaign unit matching depends on entity `externalId` values matching campaign `Unit` UUIDs; personnel matching depends on crew external ids matching `Person` UUIDs.

Salvage confirmation from `#7`: MekHQ creates salvage candidates from result sections plus the Resolve Scenario battlefield-control choice. Contract salvage allocation, salvage exchange, BLC, and CamOps recovery are calculated during MekHQ resolution, not encoded directly in the MUL. Future schema work should record unit result state and damage accurately, but leave battlefield control and final salvage allocation to MekHQ/operator workflow.

Built-in workflow consideration: the user explicitly wants to preserve the possibility that MekHQ's existing import/manual update flow is enough. `#9` should describe the information that must be captured from tabletop play regardless of whether it is entered by hand, used to edit an exported file, or fed to a generator. `#10` should test the built-in path before custom generation is assumed.

## Files And Areas

Likely files to read or edit:

- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/GITHUB_ISSUE_WORKFLOW.md`
- `docs/current/SOURCE_CODE_GUIDE.md`
- `docs/current/KNOWN_COMMANDS.md`
- `docs/handoffs/active/`
- `external/src/mekhq/MekHQ/src/mekhq/gui/BriefingTab.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/ResolveScenarioTracker.java`
- `external/src/megamek/megamek/src/megamek/common/units/EntityListFile.java`
- `external/src/megamek/megamek/src/megamek/common/loaders/MULParser.java`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "EntityListFile.saveTo|saveDeployUnits|resolveScenario|ChooseMulFilesDialog|processMulFiles" external/src/mekhq/MekHQ/src
rg -n "ELE_SURVIVORS|ELE_SALVAGE|ELE_RETREATED|ELE_DEVASTATED|ELE_KILLS|saveTo\\(File file, Client" external/src/megamek/megamek/src/megamek/common
Test-Path 'C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\MekHQ.exe'
```

Known blocker:

- Gradle source build/test commands are currently blocked because the local Java/Gradle setup requests a Java 17 daemon toolchain that is not available/configured. See `docs/current/KNOWN_COMMANDS.md`.

## Constraints

- Do not overwrite campaign saves.
- Do not modify MegaMek/MekHQ source until a child issue explicitly scopes that work and `docs/current/SOURCE_CHANGE_WORKFLOW.md` has been followed.
- Keep the epic as a planning issue. Implementation should happen through child issues.
- Preserve uncertainty and evidence labels when documenting source behavior.
- Commit completed repository tracking changes before stopping unless explicitly told not to.

## Acceptance Criteria

- The epic has been split into child issues with clear goals, dependencies, and verification expectations. Completed with issues `#7` through `#13`.
- The child issue sequence covers at least source confirmation, minimal round-trip proof, result input schema, a strategy decision between built-in workflow and custom generation, manual/UI verification, and user-facing workflow documentation. Covered by `#8`, `#10`, `#9`, `#11`/`#12`, and `#13`.
- Roadmap and task-board entries point to the decomposed issue set.
- Any durable source findings are recorded in `docs/current/`, not only in GitHub issues.

## Open Questions

- Is MekHQ's built-in manual result-entry/import workflow sufficient with good documentation, or is custom generation/tooling necessary?
- If custom generation is necessary, should the generator be a standalone Java helper using MegaMek/MekHQ jars, a MekHQ source feature, or a workspace script that invokes MegaMek classes?
- What is the minimum tabletop result schema that covers the user's likely first campaign battles without overbuilding?
- How should we validate generated battle-record MULs before loading them into MekHQ?
- Should this use a feature integration branch such as `codex/tabletop-result-mul-dev` before implementation issue `#12`?
