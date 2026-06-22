# Workspace Current State

## Purpose

This public workspace is the canonical AI-ready project workflow demo, with MegaMek/MekHQ as the worked example. It shows how to make a complex existing project usable by agents through durable context, source-grounded investigation, requirements discovery, verified commands, and safe domain-data analysis. The local workspace and GitHub repo should stay aligned; do not maintain a second repo for the same work unless the user explicitly asks.

The reusable workflow surface is `AI_READY_PROJECT_WORKFLOW.md`. The MegaMek-specific domain profile is `MEGAMEK_PROJECT_PROFILE.md`.

Primary outcomes:

- understand what MekHQ stores
- explain what a campaign situation means
- help choose reasonable next actions
- inspect source code to understand real behavior, UI actions, file formats, and automation points
- support source modifications when they help campaign control or interpretation
- gradually build a local BattleTech/MekHQ knowledge base
- demonstrate a reusable agentic workflow through this public canonical repo
- preserve a versioned demo save for repeatable save-file and requirements-discovery examples

## Documentation Operating Model

- `AI_READY_PROJECT_WORKFLOW.md`: reusable workflow pattern demonstrated by this public canonical repo.
- `MEGAMEK_PROJECT_PROFILE.md`: MegaMek/MekHQ local assumptions, paths, data safety rules, source posture, and domain guidance.
- `DOCUMENTATION_WORKFLOW.md`: required process for updating durable notes.
- `ROADMAP.md`: repository-owned roadmap and issue-candidate source of truth.
- `GITHUB_ISSUE_WORKFLOW.md`: process for creating GitHub Issues and agent handoffs from roadmap entries.
- `TASKS.md`: active work board, next queue, backlog, blocked items, and recent completions.
- `CAMPAIGN_EXPLORATION_PLAN.md`: first hands-on MekHQ exploration plan, including new-campaign setup, first contract flow, owned transport, and the planned human-controlled aerospace transit scenario.
- `FIXED_OPFOR_MUL_POOL_WORKFLOW.md`: source-confirmed fixed OPFOR setup-MUL workflow for loading curated physical-miniature pools into MekHQ bot formations.
- `CUSTOM_RAT_STRATEGY.md`: decision note comparing fixed OPFOR MUL pools, classic custom RATs, modern force-generator data, workspace tooling, and source changes.
- `MEK_RPG_MEKHQ_COLLABORATION_BRIEF.md`: shareable coordination brief for the MEK-RPG team, including ownership boundaries, automation phases, questions, and a proposed first joint issue.
- `MEK_RPG_MEKHQ_INTEGRATION_ASSESSMENT.md`: assessment of how the sister MEK-RPG repository can use MekHQ as a unit-scale campaign ledger and tactical handoff tool without replacing RPG campaign memory.
- `MEK_RPG_CHECKPOINT_EXPORT_REVIEW_MEMO.md`: shareable review memo and response record for the read-only checkpoint export, including MEK-RPG consumer-side issue queue `#84` through `#89`.
- `MEK_RPG_LIVE_MEKHQ_API_FEEDBACK_MEMO.md`: shareable feedback memo asking whether MEK-RPG wants the save/checkpoint import contract to move toward a live localhost MekHQ read-only state API.
- `MEK_RPG_MEKHQ_CHECKPOINT_EXPORTER_OWNERSHIP_DECISION.md`: decision note keeping the checkpoint exporter as a workspace experimental helper for now, with triggers for a future MekHQ source-owned exporter issue.
- `HELP_FILE_WORKFLOW.md`: routing guidance for using campaign data, local help/glossary/docs, source code, user confirmation, and external sources.
- `docs/current/<FEATURE>_TRACKING.md`: optional compact state snapshots for multi-issue integration branches.
- `docs/handoffs/active/`: open issue handoff documents for agent-executed work.
- `docs/handoffs/archive/`: completed issue handoff documents after close-out.
- `ACTIVE_CAMPAIGN.md`: current campaign save, identity, contract, enabled systems, priorities, and unknowns.
- `KNOWN_COMMANDS.md`: repeatable commands for verification, launch, source search, and safe inspection.
- `SOURCE_CHANGE_WORKFLOW.md`: source modification, build/test, dirty-worktree, and documentation follow-through process.
- `SAVE_FORMAT_NOTES.md`: MekHQ campaign save structure, evidence labels, field meanings, and source references.
- `DATA_MAP.md`: local data locations and file formats.
- `SOURCE_CODE_GUIDE.md`: source inspection strategy.
- `HELP_FILE_WORKFLOW.md`: local help, glossary, documentation, and source-evidence workflow.
- `BATTLETECH_CONTEXT.md`: reusable BattleTech and MekHQ interpretation context.
- `CAMPAIGN_ANALYSIS_WORKFLOW.md`: campaign and scenario analysis sequence.

If a discovery will matter in a later session, record it in the narrowest relevant `docs/current/` file.

## MegaMek Profile Summary

Full local setup, data safety posture, source inspection expectations, BattleTech rules posture, and verification checks live in `MEGAMEK_PROJECT_PROFILE.md`.

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
- Visibility: `Public`
- Branch: `master` tracks `origin/master`
- Evidence: `Confirmed locally` after changing visibility with GitHub CLI on `2026-06-18`.

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
12. Keep this public repo as the canonical working artifact while improving the generic workflow and MegaMek profile in place.
