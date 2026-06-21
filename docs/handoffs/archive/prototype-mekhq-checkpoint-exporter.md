# Agent Handoff

## Issue

- GitHub issue: `#29`
- Roadmap entry: `Prototype read-only MekHQ checkpoint exporter`
- Priority: `Medium`

## Goal

Prototype or precisely plan a read-only MekHQ checkpoint exporter that loads a campaign through MekHQ code or installed jars and emits JSON matching MEK-RPG's checkpoint consumer contract.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT_SCHEMA.md`
- `docs/current/SOURCE_CODE_GUIDE.md`
- `docs/current/KNOWN_COMMANDS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md` if source changes become necessary
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_READ_ONLY_CHECKPOINT_EXPORT_CONTRACT.md`

## Expected Output

- Completed with prototype exporter `tools/mekhq-checkpoint-exporter/`.
- The wrapper emits checkpoint JSON and does not modify saves.
- Current docs updated with classpath/toolchain findings, verification, and limitations in `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORTER_PROTOTYPE.md`.

## Files And Areas

Likely files and source areas to inspect:

- `external/src/mekhq/MekHQ/src/mekhq/campaign/CampaignFactory.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/io/CampaignXmlParser.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/Campaign.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/finances/Finances.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/unit/Unit.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/personnel/Person.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/market/`
- `external/installs/MekHQ-0.51.00/`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
java -version
javac -version
Test-Path 'C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\MekHQ.exe'
rg "createCampaign|CampaignFactory|CampaignXmlParser" external/src/mekhq/MekHQ/src/mekhq
```

## Constraints

- Do not include unrelated user changes.
- Preserve uncertainty and evidence labels.
- Do not overwrite or edit campaign saves.
- Exporter must be read-only and must not execute campaign actions.
- No headless day advancement, purchase/sale, contract accept/decline, repair, personnel mutation, or tactical result writeback.
- If source modification becomes necessary, follow `SOURCE_CHANGE_WORKFLOW.md` and keep source changes in the target source repo, not this workspace.
- Commit and push completed workspace documentation/tooling changes before stopping unless explicitly told not to.

## Acceptance Criteria

- Done: loads an explicit `.cpnx`, `.cpnx.gz`, or plain XML path read-only through installed MekHQ jars.
- Done: emits JSON with the top-level shape from `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT_SCHEMA.md`.
- Done: uses method-backed MekHQ values where feasible and labels serialized, needs-inspection, and unsupported fields.
- Done: records verification commands and limitations.
- Done: explicitly states that no writeback was implemented.

## Open Questions

- Resolved: installed MekHQ/MegaMek jars can be invoked from a workspace helper after replaying the non-GUI data initialization sequence from `DataLoadingDialog`.
- Resolved: the existing Java/Gradle blocker does not block this jar-backed prototype; JDK 21 and `javac` are used directly.
- Resolved for prototype: first implementation lives in this workspace. Production ownership should be revisited after review; MekHQ source is the likely long-term owner.
