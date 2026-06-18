# Workspace Current State

## Purpose

This workspace is the user's MegaMek/MekHQ campaign assistant. Its job is to make campaign data intelligible and actionable.

Primary outcomes:

- understand what MekHQ stores
- explain what a campaign situation means
- help choose reasonable next actions
- inspect source code to understand real behavior, UI actions, file formats, and automation points
- support source modifications when they help campaign control or interpretation
- gradually build a local BattleTech/MekHQ knowledge base

## Documentation Operating Model

- `DOCUMENTATION_WORKFLOW.md`: required process for updating durable notes.
- `ACTIVE_CAMPAIGN.md`: current campaign save, identity, contract, enabled systems, priorities, and unknowns.
- `KNOWN_COMMANDS.md`: repeatable commands for verification, launch, source search, and safe inspection.
- `SAVE_FORMAT_NOTES.md`: MekHQ campaign save structure, evidence labels, field meanings, and source references.
- `DATA_MAP.md`: local data locations and file formats.
- `SOURCE_CODE_GUIDE.md`: source inspection strategy.
- `BATTLETECH_CONTEXT.md`: reusable BattleTech and MekHQ interpretation context.
- `CAMPAIGN_ANALYSIS_WORKFLOW.md`: campaign and scenario analysis sequence.

If a discovery will matter in a later session, record it in the narrowest relevant `docs/current/` file.

## Known Local Installation

- Suite version: MekHQ/MegaMek/MegaMekLab `0.51.0`
- Extracted folder: `C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00`
- Archive: `C:\Users\waltr\Documents\megamek-workspace\external\downloads\MekHQ-0.51.0.tar.gz`
- Java: Eclipse Temurin JDK `21.0.11`
- Java path: `C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot`

## Known Source Area

- Source root: `C:\Users\waltr\Documents\megamek-workspace\external\src`
- MegaMek checkout: `C:\Users\waltr\Documents\megamek-workspace\external\src\megamek`
- MekHQ checkout: `C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq`
- MegaMekLab checkout: `C:\Users\waltr\Documents\megamek-workspace\external\src\megameklab`
- MM Data checkout: `C:\Users\waltr\Documents\megamek-workspace\external\src\mm-data`

Still potentially useful later:

- `https://github.com/MegaMek/mm-data`

Use source inspection heavily. When the user asks how to control the campaign, automate a step, interpret a saved field, or change behavior, search the source before relying on memory.

## Immediate Next Milestones

1. Identify the active MekHQ campaign save location.
2. Update `ACTIVE_CAMPAIGN.md` from save data or user-confirmed facts.
3. Learn how `.cpnx.gz` campaign files are structured and record findings in `SAVE_FORMAT_NOTES.md`.
4. Produce the first campaign status report from a real or sample campaign.
5. Map the fields MekHQ uses for forces, personnel, units, repair state, finances, contracts, and scenarios.
6. Map the source classes that load/save campaigns and perform common campaign actions.
7. Build repeatable extraction or automation scripts only after the data shape and code paths are understood.
