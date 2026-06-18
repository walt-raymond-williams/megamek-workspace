# MegaMek AI-Ready Workspace

This public repository is the canonical working repo for the AI-ready project workflow demo, with MegaMek/MekHQ campaign analysis as the worked example. The workspace and the public GitHub repo are the same project; there is no separate template repo to keep in sync.

The reusable workflow pattern lives in `docs/current/AI_READY_PROJECT_WORKFLOW.md`. The MegaMek-specific project profile lives in `docs/current/MEGAMEK_PROJECT_PROFILE.md`.

The purpose is not primarily to develop MegaMek itself. Source code may be cloned nearby for reference, but this workspace is for:

- reading MekHQ campaign and scenario data
- learning how MegaMek/MekHQ represent BattleTech concepts
- inspecting source code to learn how to control, interact with, automate, and potentially modify the tools
- producing useful campaign briefings, risk assessments, and next-action suggestions
- maintaining durable notes so future Codex sessions get smarter instead of starting cold

## Local Setup

Known local paths:

- Release suite: `C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00`
- Suite archive: `C:\Users\waltr\Documents\megamek-workspace\external\downloads\MekHQ-0.51.0.tar.gz`
- Java 21 JDK: `C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot`
- Source checkout area: `C:\Users\waltr\Documents\megamek-workspace\external\src`
- MegaMek source: `C:\Users\waltr\Documents\megamek-workspace\external\src\megamek`
- MekHQ source: `C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq`
- MegaMekLab source: `C:\Users\waltr\Documents\megamek-workspace\external\src\megameklab`
- MM Data source: `C:\Users\waltr\Documents\megamek-workspace\external\src\mm-data`
- This workspace: `C:\Users\waltr\Documents\megamek-workspace`

Launch commands:

```powershell
cd C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00
.\MekHQ.exe
.\MegaMek.exe
.\MegaMekLab.exe
```

Java verification:

```powershell
java -version
javac -version
```

Both should report version 21.

## Workspace Map

- `AGENTS.md` tells Codex how to work in this project.
- `docs/current/` is the front door for current understanding.
- `docs/current/AI_READY_PROJECT_WORKFLOW.md` describes the reusable project workflow pattern.
- `docs/current/MEGAMEK_PROJECT_PROFILE.md` describes the MegaMek/MekHQ local profile.
- `docs/templates/` contains repeatable report formats.
- `campaigns/` is for local campaign snapshots and notes.
- `analysis/` is for generated extracts and temporary interpretation work.
- `external/` contains local source checkouts, release installs, and downloads. It is ignored by this workspace repo.

Use the source checkouts as the authority for behavior when local docs or memory are vague. This workspace keeps the notes and reports; the source repos hold any code changes.

Raw campaign saves are ignored by default. Add them deliberately when needed.

## Current Documentation

Start here when orienting a new session:

- `docs/current/WORKSPACE.md`: current workspace state, known install/source paths, and near-term milestones.
- `docs/current/AI_READY_PROJECT_WORKFLOW.md`: reusable AI-ready project structure, intake, handoff, branch, and close-out pattern.
- `docs/current/MEGAMEK_PROJECT_PROFILE.md`: local MegaMek/MekHQ paths, data safety rules, source posture, and interpretation guidance.
- `docs/current/TASKS.md`: current work board for active tasks, queued work, blocked items, and recent completions.
- `docs/current/ACTIVE_CAMPAIGN.md`: active campaign save, identity, contract, enabled systems, priorities, and open questions.
- `docs/current/DOCUMENTATION_WORKFLOW.md`: required process for keeping durable notes current.
- `docs/current/KNOWN_COMMANDS.md`: repeatable local commands for verification, launching, source search, and safe save inspection.
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`: source modification, build/test, dirty-worktree, and documentation follow-through process.
- `docs/current/SAVE_FORMAT_NOTES.md`: confirmed and suspected MekHQ save-format knowledge.
- `docs/current/DATA_MAP.md`: local data locations and file format references.
- `docs/current/SOURCE_CODE_GUIDE.md`: source inspection strategy and important source areas.

When a discovery will matter again, record it in `docs/current/` instead of leaving it only in chat.
