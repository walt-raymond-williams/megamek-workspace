# Agent Handoff

## Issue

- GitHub issue: `#25`
- Roadmap entry: `Define MekHQ read-only checkpoint export for MEK-RPG`
- Priority: `Medium`

## Goal

Define or prototype a MekHQ read-only checkpoint export for MEK-RPG: a source-backed field contract and JSON shape that exposes campaign checkpoint facts without direct `.cpnx`, `.cpnx.gz`, or XML writeback.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_MEKHQ_BRIDGE_PRIMITIVES.md`
- `docs/current/MEK_RPG_MEKHQ_INTEGRATION_ASSESSMENT.md`
- `docs/current/MEK_RPG_MEKHQ_COLLABORATION_BRIEF.md`
- `docs/current/SAVE_FORMAT_NOTES.md`
- `docs/current/SOURCE_CODE_GUIDE.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEGAMEK_WORKSPACE_SYNC_MEMO.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_SAVE_SUMMARY_HELPER.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_BRIDGE_DATA_MODEL.md`

## Expected Output

- A current doc under `docs/current/` defining the read-only checkpoint export contract or prototype plan.
- A recommended JSON shape for MEK-RPG consumers.
- Source-backed notes identifying method/API owners for each field group.
- Warnings for raw-XML-only or unsupported values.
- Roadmap/task updates if sequencing changes.

## Files And Areas

Likely source areas to read:

- `external/src/mekhq/MekHQ/src/mekhq/campaign/Campaign.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/CampaignFactory.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/io/CampaignXmlParser.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/finances/`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/personnel/`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/unit/`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/mission/`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/market/`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/parts/`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/location/`

Likely files to edit:

- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT.md` or similarly focused current doc.
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CODE_GUIDE.md` if new source entry points are confirmed.

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg "writeToXML|readToXML|parseFromXML|CampaignXmlParser" external/src/mekhq/MekHQ/src/mekhq
rg "getBalance|getCurrentSystem|getCurrentLocation|getAllPersonnel|getUnits|getMissions|getUnitMarket|getPersonnelMarket|getContractMarket|getShoppingList" external/src/mekhq/MekHQ/src/mekhq/campaign
rg "class Unit|class Person|class Contract|class Scenario|class Finances|class UnitMarketOffer|class PersonnelMarket|class ShoppingList" external/src/mekhq/MekHQ/src/mekhq
```

## Constraints

- Do not include unrelated user changes.
- Preserve uncertainty and evidence labels.
- Do not edit MekHQ campaign saves or extracted raw XML.
- Treat MEK-RPG as read-only unless a later task explicitly requests changes there.
- Keep writeback, headless day advancement, purchases, repairs, personnel mutation, and contract accept/decline implementation out of scope.
- Commit completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- Identify MekHQ method/API owners for campaign date, location, funds, personnel, units, contracts, scenarios, markets, repairs/logistics, and reports.
- Distinguish serialized facts from method-derived values.
- Recommend a JSON export shape or source-backed field contract.
- Preserve stable MekHQ IDs where available.
- Include warnings and unsupported fields for values that should not be trusted from raw XML alone.
- Explicitly state that this issue does not implement writeback.

## Open Questions

- Should the first checkpoint exporter be implemented in MekHQ source, as a jar-backed helper in this workspace, or in MEK-RPG with a source-backed contract?
- Which field group should be validated first with disposable saves?
- Can the current MEK-RPG `summarize-mekhq-save.py` JSON be adapted to the recommended shape without losing source-backed method semantics?
