# MegaMek Project Profile

This is the project-specific profile for the MegaMek/MekHQ campaign workspace. It contains local paths, domain posture, data safety rules, source inspection expectations, and BattleTech/MekHQ interpretation guidance. Generic agent workflow belongs in `AI_READY_PROJECT_WORKFLOW.md`.

## Purpose

This workspace is an AI-assisted operations desk for understanding and running a MekHQ campaign. It is also the worked example for the reusable AI-ready project workflow.

Primary jobs:

- inspect campaign, unit, scenario, personnel, logistics, finance, and repair data
- explain campaign state in BattleTech and MekHQ terms
- inspect MegaMek/MekHQ source to understand file formats, workflows, UI actions, automation hooks, and mechanics
- identify risks, opportunities, and next actions for the campaign
- preserve discoveries so future sessions inherit confirmed knowledge

Be explicit about uncertainty. If a rule, file format, UI behavior, or game mechanic is inferred rather than confirmed from local data, local docs, source, or the user, say so.

## Local Installation

- Suite version: MekHQ/MegaMek/MegaMekLab `0.51.0`
- Extracted folder: `C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00`
- Archive: `C:\Users\waltr\Documents\megamek-workspace\external\downloads\MekHQ-0.51.0.tar.gz`
- Java: Eclipse Temurin JDK `21.0.11`
- Java path: `C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot`

Known source checkouts:

- `C:\Users\waltr\Documents\megamek-workspace\external\src\megamek`
- `C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq`
- `C:\Users\waltr\Documents\megamek-workspace\external\src\megameklab`
- `C:\Users\waltr\Documents\megamek-workspace\external\src\mm-data`

`external/` is ignored by the workspace repo. Treat it as local payload, not repository documentation.

## Domain Data

- Local campaign inputs belong under `campaigns/`.
- Temporary/generated extracts belong under `analysis/`.
- Repeatable report formats live in `docs/templates/`.
- Raw campaign saves are ignored by default and should be committed only when the user explicitly wants a snapshot committed.

Campaign files can contain active user campaign state. Do not overwrite, move, or delete campaign saves unless the user explicitly asks. Work on copies under `analysis/tmp/` when experimenting.

## Current Domain Docs

- `ACTIVE_CAMPAIGN.md`: current campaign save, identity, contract, enabled systems, priorities, and unknowns.
- `CAMPAIGN_ANALYSIS_WORKFLOW.md`: campaign and scenario analysis sequence.
- `BATTLETECH_CONTEXT.md`: reusable BattleTech and MekHQ interpretation context.
- `DATA_MAP.md`: local data locations and file formats.
- `SAVE_FORMAT_NOTES.md`: MekHQ campaign save structure, evidence labels, field meanings, and source references.
- `SOURCE_CODE_GUIDE.md`: source inspection strategy and important source areas.
- `HELP_FILE_WORKFLOW.md`: how to route questions across campaign data, local docs/help/glossary resources, source code, user confirmation, and external sources.
- `SOURCE_CHANGE_WORKFLOW.md`: source modification, build/test, dirty-worktree, and documentation follow-through process.
- `KNOWN_COMMANDS.md`: repeatable commands for verification, launch, source search, and safe inspection.

## Campaign Interpretation Workflow

For campaign or scenario interpretation:

1. Identify the exact input files and their versions.
2. Check `TASKS.md` for current priorities.
3. Check `ACTIVE_CAMPAIGN.md` for known current campaign context.
4. Inspect structure before interpreting. For compressed campaign files, extract to `analysis/tmp/` or read from a temporary location.
5. Build a small factual summary first: date, faction/unit, active contract, force composition, personnel, finances, repairs, location, pending scenarios, and alerts.
6. Interpret the situation through BattleTech/MekHQ concepts: tonnage, tech base, BV, armor/internal damage, heat, ammo, pilot skills, morale, fatigue, maintenance burden, transport, supply, and contract obligations.
7. Separate facts, inferences, and recommendations.
8. Save durable discoveries in the narrowest relevant `docs/current/` file.

Useful campaign answers should usually include what is happening now, why it matters mechanically, what choices the player has, what risks are easy to miss, and what remains uncertain.

## Source Inspection Posture

Source code is a first-class reference for this workspace. Inspect local source before giving confident answers about how MegaMek or MekHQ behaves.

Read source when it answers questions like:

- how MekHQ stores a campaign value
- how scenario generation computes an outcome
- how repair, maintenance, fatigue, market, or contract logic works
- what a file format field means
- how the UI triggers an action
- where an import/export feature lives
- whether a command-line or automation path exists
- what would need to change to alter behavior

If code changes become the task, keep source changes in the source checkout, not this workspace. This workspace should hold investigation notes, plans, and campaign-facing analysis.

## BattleTech Rules Posture

BattleTech has many rule layers and optional systems. Do not pretend all tables and edge cases are known from memory.

- Prefer local MegaMek/MekHQ docs, source code, and campaign data when explaining how this install behaves.
- Use `HELP_FILE_WORKFLOW.md` to choose between save data, local help/glossary/docs, source inspection, user confirmation, and external sources.
- Use official or primary sources when browsing is necessary.
- Do not reproduce large copyrighted rulebook passages. Summarize mechanics and cite source context when possible.
- Ask which ruleset or optional systems are active when it materially changes the answer.

## Verification

Useful local checks:

```powershell
java -version
javac -version
Test-Path 'C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\MekHQ.exe'
Test-Path 'C:\Users\waltr\Documents\megamek-workspace\external\src\megamek'
Test-Path 'C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq'
Test-Path 'C:\Users\waltr\Documents\megamek-workspace\external\src\megameklab'
Test-Path 'C:\Users\waltr\Documents\megamek-workspace\external\src\mm-data'
```

For parsing work, verify by comparing extracted summaries against MekHQ's UI or known campaign facts when possible.
