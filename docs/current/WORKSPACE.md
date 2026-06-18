# Workspace Current State

## Purpose

This workspace is becoming an AI-ready project workflow demo, with MegaMek/MekHQ as the worked example. It should show how to make a complex existing project usable by agents through durable context, source-grounded investigation, requirements discovery, verified commands, and safe domain-data analysis.

Primary outcomes:

- understand what MekHQ stores
- explain what a campaign situation means
- help choose reasonable next actions
- inspect source code to understand real behavior, UI actions, file formats, and automation points
- support source modifications when they help campaign control or interpretation
- gradually build a local BattleTech/MekHQ knowledge base
- demonstrate a reusable agentic workflow that can be adapted to other existing codebases
- preserve a versioned demo save for repeatable save-file and requirements-discovery examples

## Documentation Operating Model

- `DOCUMENTATION_WORKFLOW.md`: required process for updating durable notes.
- `ROADMAP.md`: repository-owned roadmap and issue-candidate source of truth.
- `GITHUB_ISSUE_WORKFLOW.md`: process for creating GitHub Issues and agent handoffs from roadmap entries.
- `TASKS.md`: active work board, next queue, backlog, blocked items, and recent completions.
- `ACTIVE_CAMPAIGN.md`: current campaign save, identity, contract, enabled systems, priorities, and unknowns.
- `KNOWN_COMMANDS.md`: repeatable commands for verification, launch, source search, and safe inspection.
- `SOURCE_CHANGE_WORKFLOW.md`: source modification, build/test, dirty-worktree, and documentation follow-through process.
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

## GitHub Repository

- Remote: `https://github.com/walt-raymond-williams/megamek-workspace`
- Origin URL: `https://github.com/walt-raymond-williams/megamek-workspace.git`
- Visibility: `Private`
- Branch: `master` tracks `origin/master`
- Evidence: `Confirmed locally` after `gh repo create` and push on `2026-06-18`.

## Immediate Next Milestones

1. Keep `ROADMAP.md` as the durable planning source and issue-candidate list.
2. Maintain `TASKS.md` as the current local work board.
3. Create GitHub Issues gradually from roadmap entries when work is ready to execute or hand off.
4. Use `campaigns/demo/ai-ready-demo.cpnx.gz` as the active practice/demo campaign.
5. Update `ACTIVE_CAMPAIGN.md` from save data after safe inspection.
6. Learn how `.cpnx.gz` campaign files are structured and record findings in `SAVE_FORMAT_NOTES.md`.
7. Produce the first campaign status report from the active demo campaign.
8. Map the fields MekHQ uses for forces, personnel, units, repair state, finances, contracts, and scenarios.
9. Map the source classes that load/save campaigns and perform common campaign actions.
10. Resolve or document the Gradle daemon Java 17 blocker before relying on source build/test verification.
11. Build repeatable extraction or automation scripts only after the data shape and code paths are understood.
12. Split generic AI-ready workflow guidance from MegaMek-specific project profile details.
