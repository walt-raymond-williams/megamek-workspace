# Agent Handoff

## Issue

- GitHub issue: `#24`
- Roadmap entry: `Explore MEK-RPG and MekHQ campaign bridge`
- Priority: `Medium`

## Goal

Map safe MekHQ bridge primitives for MEK-RPG pending actions: stable read-only campaign facts, source/API owners, safe action-method candidates, GUI blockers, tactical result artifact paths, and the smallest future implementation issues.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEGAMEK_WORKSPACE_BRIDGE_REQUEST.md`
- `docs/current/MEK_RPG_MEKHQ_INTEGRATION_ASSESSMENT.md`
- `docs/current/MEK_RPG_MEKHQ_COLLABORATION_BRIEF.md`
- `docs/current/TABLETOP_RESULT_MUL_WORKFLOW.md`
- `docs/current/MECH_ROSTER_CONTROL_WORKFLOWS.md`
- `docs/current/SAVE_FORMAT_NOTES.md`
- `docs/current/SOURCE_CODE_GUIDE.md`

Useful MEK-RPG context, read-only unless explicitly asked otherwise:

- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_BRIDGE_DATA_MODEL.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_LINKED_PLAY_LOOP.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_PENDING_APPLICATION_WORKFLOW.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_SAVE_SUMMARY_HELPER.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_PENDING_WORKFLOW_PLAYTEST_VALIDATION.md`

## Expected Output

- A source-backed bridge primitive map under `docs/current/`.
- Updates to `docs/current/ROADMAP.md` and `docs/current/TASKS.md` if the investigation changes sequencing.
- One or two recommended follow-up issue candidates, not broad writeback promises.

## Files And Areas

Likely files to read:

- `external/src/mekhq/MekHQ/src/mekhq/campaign/`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/market/`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/personnel/`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/unit/`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/mission/`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/parts/`
- `external/src/mekhq/MekHQ/src/mekhq/gui/`
- `external/src/megamek/megamek/src/megamek/common/` for entity/MUL references when needed.

Likely files to edit:

- `docs/current/MEK_RPG_MEKHQ_BRIDGE_PRIMITIVES.md` or a similarly focused current doc.
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg "class Campaign" external/src/mekhq/MekHQ/src
rg "newDay|advanceDay|advance day" external/src/mekhq/MekHQ/src
rg "UnitMarketOffer|PersonnelMarket|ContractMarket|ShoppingList" external/src/mekhq/MekHQ/src
rg "writeToXML|parseFromXML|CampaignXmlParser" external/src/mekhq/MekHQ/src
rg "resolveScenario|battle record|MUL" external/src/mekhq/MekHQ/src external/src/megamek/megamek/src
```

## Constraints

- Do not include unrelated user changes.
- Preserve uncertainty and evidence labels.
- Do not directly edit `.cpnx`, `.cpnx.gz`, or raw XML saves.
- Treat MEK-RPG as read-only unless a later task explicitly requests changes there.
- Use local MekHQ/MegaMek source as the authority when behavior matters.
- Commit completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- Stable read-only campaign export fields are documented with best source/API owners.
- Unsupported or derived fields that should not be trusted from raw XML alone are documented.
- Safe method/API candidates are mapped for `day-advancement`, `purchase-sale`, `contract`, `repair-logistics`, `personnel`, `injury-availability`, `tactical-outcome`, and `finance`.
- GUI/dialog dependencies that block noninteractive use are identified.
- Existing tactical result artifact paths are clarified.
- One or two smallest safe future implementation issues are recommended.
- The no-direct-save-editing boundary is preserved.

## Open Questions

- Which pending action type is the lowest-risk first command/helper candidate after source inspection?
- Are market and contract offers stable enough to select by serialized IDs, or do they need a safer selector contract?
- Can any daily processing be made noninteractive without a MekHQ source change, or is UI/manual save still the practical boundary?
