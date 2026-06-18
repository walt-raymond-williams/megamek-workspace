# Tasks

This is the lightweight task board for the MegaMek workspace. Keep it current when work starts, finishes, gets blocked, or changes priority.

## How To Use This Board

- `Now`: active work that should be handled before starting unrelated expansion.
- `Next`: queued work with clear value and a likely near-term order.
- `Backlog`: useful work that is not yet ready or not yet urgent.
- `Blocked`: work waiting on user input, missing files, source confirmation, or an external state change.
- `Done`: recently completed work worth preserving for continuity.

Keep task titles short. Add enough context that a future agent can resume without rereading the whole chat.

## Task Entry Format

Use this shape for active and queued work:

```markdown
1. Task title.
   - Status: `Not started` | `In progress` | `Blocked` | `Done`
   - Owner: `Codex` | `User` | `Mixed`
   - Goal:
   - Output:
   - Notes:
```

## Transition Rules

- Before starting a queued task, move it from `Next` to `Now`.
- Keep `Now` small; one active task is preferred unless tasks are genuinely parallel.
- When a task finishes, move it to `Done` with the date and relevant commit if available.
- When a task blocks, move it to `Blocked` with the exact blocker and the next needed input or condition.
- If task scope changes, update the task immediately instead of leaving stale intent in the board.
- Commit task-board updates with the related documentation or implementation work when reasonable.

## Now

No active task.

## Next

1. Discover MekHQ roster-control workflows.
   - Status: `Not started`
   - Owner: `Codex`
   - Goal: Confirm in-game, data, and source-supported ways to control the player starting roster and OPFOR generation for a physical-miniatures MekHQ campaign.
   - Output: Discovery notes and child-issue recommendations for epic `#14`.
   - Notes: GitHub issue `#14`; active handoff `docs/handoffs/active/mech-roster-control-epic.md`; tracking doc `docs/current/MECH_ROSTER_CONTROL_TRACKING.md`.

2. Investigate MekHQ and BattleTech salvage rules.
   - Status: `Not started`
   - Owner: `Codex`
   - Goal: Confirm how MekHQ decides salvage eligibility and compare it against relevant BattleTech campaign salvage rules.
   - Output: Durable salvage notes under `docs/current/` and implications for tabletop result MUL generation.
   - Notes: GitHub issue `#7`; child of epic `#6`; active handoff `docs/handoffs/active/investigate-salvage-rules.md`; recommended next for epic `#6` after completing issue `#8`.

3. Inspect the active demo campaign save without modifying it.
   - Status: `Not started`
   - Owner: `Codex`
   - Goal: Extract a factual campaign snapshot from `campaigns/demo/ai-ready-demo.cpnx.gz`.
   - Output: Update `ACTIVE_CAMPAIGN.md`, `SAVE_FORMAT_NOTES.md`, and a first campaign status report.
   - Notes: GitHub issue `#2`; active handoff `docs/handoffs/active/inspect-demo-campaign-save.md`.

4. Identify MekHQ save/load source classes.
   - Status: `Not started`
   - Owner: `Codex`
   - Goal: Confirm how `.cpnx.gz` files are loaded and saved in the local MekHQ source.
   - Output: Update `SAVE_FORMAT_NOTES.md` and `SOURCE_CODE_GUIDE.md`.
   - Notes: GitHub issue `#3`; active handoff `docs/handoffs/active/identify-mekhq-save-load-source-classes.md`.

5. Create help-file usage guidance for agents.
   - Status: `Not started`
   - Owner: `Codex`
   - Goal: Decide how future agents should use local MekHQ/MegaMek help files, in-app glossary resources, installed documentation, and source code.
   - Output: A durable workflow note or skill-style guide under `docs/current/`.
   - Notes: GitHub issue `#4`; active handoff `docs/handoffs/active/create-help-file-usage-guidance.md`; resume from `docs/current/HELP_FILE_USAGE_GUIDANCE_STATE.md`.

6. Turn this repo into an AI-ready project workflow demo.
   - Goal: Evolve this workspace into a reusable AI-ready project pattern with MegaMek/MekHQ as the worked example: source investigation, requirements discovery, verified commands, contributor handoff, campaign/save-file analysis, and agent memory.
   - Output: Clear repo positioning, generic workflow docs, MegaMek project profile, issue/requirement/PR templates, demo campaign fixture, and a decision on whether GitHub Projects should be used.

7. Produce the first campaign status report.
   - Goal: Practice the full campaign-analysis workflow on the active sample campaign.
   - Output: A report under `campaigns/demo/reports/`.

## Backlog

- Decide a report naming convention for campaign reports.
- Decide whether generated parser outputs should live under `analysis/generated/` by campaign name.
- Build a repeatable campaign summary extraction script after save structure is confirmed.
- Create source-reference notes for common MekHQ campaign actions and UI buttons.

## Blocked

1. Verify Gradle build/test commands in local source repos.
   - Status: `Blocked`
   - Owner: `Mixed`
   - Goal: Run Gradle wrapper build/test commands in MegaMek, MekHQ, MegaMekLab, and mm-data.
   - Output: Mark source build/test commands as verified in `KNOWN_COMMANDS.md`.
   - Notes: Current shell resolves Java 8, JDK 21 is installed separately, and Gradle wrapper execution fails because `gradle/gradle-daemon-jvm.properties` requests daemon `toolchainVersion=17` with no local JDK 17/toolchain download configured.

## Done

- `2026-06-18`: Completed GitHub issue `#8` by confirming MekHQ's battle-record MUL source workflow for tabletop result import, documenting the flow in `TABLETOP_RESULT_MUL_WORKFLOW.md`, and archiving the handoff. Recommended next epic task is salvage rules issue `#7`.
- `2026-06-18`: Decomposed GitHub issue `#6` into child issues `#7` through `#13`, updated the roadmap and active handoffs, and recommended starting execution with source workflow confirmation issue `#8`. Commit `019367c`.
- `2026-06-18`: Made the GitHub repository public and clarified that this repo is the one canonical working/public artifact, not a staging repo for a separate template repo.
- `2026-06-18`: Completed GitHub issue `#1` by splitting reusable AI-ready workflow guidance into `AI_READY_PROJECT_WORKFLOW.md` and MegaMek/MekHQ-specific assumptions into `MEGAMEK_PROJECT_PROFILE.md`, with front-door links from `AGENTS.md`, `README.md`, and `WORKSPACE.md`.
- `2026-06-18`: Completed GitHub issue `#5` by comparing against Sunny Town HQ and updating this repo's workflow docs with epic, feature-tracking, PR-readiness, open-PR, and human-review patterns.
- `2026-06-18`: Established roadmap-driven GitHub issue tracking with active/archive handoff lifecycle, created the `agent-task` GitHub label, created issues `#1` through `#4`, and added active handoff documents for each issue.
- `2026-06-18`: Added `SOURCE_CHANGE_WORKFLOW.md`, tightened task-board transition rules, and documented current source build/test commands and Gradle blocker.
- `2026-06-18`: Added lightweight task tracking in `TASKS.md` and made it part of the documentation workflow.
- `2026-06-18`: Marked `The Learning Ropes.cpnx.gz` as the active practice campaign in `ACTIVE_CAMPAIGN.md`.
- `2026-06-18`: Established the initial workspace documentation baseline in commit `e858543`.

## Update Rules

- Update this file before switching from one major workstream to another.
- Move completed tasks to `Done` with the date and relevant commit if available.
- Move blocked tasks to `Blocked` with the exact missing input or condition.
- Keep durable findings in the relevant `docs/current/` file; this board tracks work, not detailed research.
