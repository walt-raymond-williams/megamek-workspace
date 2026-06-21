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

- A prototype exporter, prototype plan, or blocker report.
- If implemented in this workspace, a read-only tool/script that emits checkpoint JSON and does not modify saves.
- Current docs updated with classpath/toolchain findings, verification, and limitations.

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

- Loads or plans to load an explicit `.cpnx`, `.cpnx.gz`, or plain XML path read-only.
- Emits or specifies JSON matching `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT_SCHEMA.md`.
- Uses method-backed MekHQ values where feasible and labels serialized/inferred/unsupported fields.
- Records verification commands or exact blockers.
- Explicitly states that no writeback was implemented.

## Open Questions

- Can installed MekHQ/MegaMek jars be invoked from a workspace helper without GUI startup dependencies?
- Is the existing Java/Gradle blocker relevant to a jar-backed prototype, or only to source-level implementation?
- Should the first prototype live in this workspace, MEK-RPG, or MekHQ source after validation?
