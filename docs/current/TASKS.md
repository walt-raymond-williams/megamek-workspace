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

1. Create help-file usage guidance for agents.
   - Status: `In progress`
   - Owner: `Codex`
   - Goal: Decide how future agents should use local MekHQ/MegaMek help files, in-app glossary resources, and installed documentation when answering campaign, scenario, rules, workflow, or source-behavior questions.
   - Output: A durable workflow note or skill-style guide under `docs/current/` that tells agents where the useful help files live, when to consult them, how to prioritize source/docs/campaign data, and how to cite uncertainty.
   - Notes: Initial research found high-value sources in `external/installs/MekHQ-0.51.00/docs`, `external/src/mekhq/MekHQ/docs`, `external/src/mekhq/MekHQ/resources/mekhq/resources/GlossaryEntry.properties`, `external/src/mekhq/MekHQ/src/mekhq/campaign/utilities/glossary/DocumentationEntry.java`, and `external/src/megamek/megamek/docs`. PDFs are valuable but need extraction tooling; plain text, Markdown, HTML, Java, and properties files are immediately searchable.

## Next

1. Inspect the active campaign save without modifying it.
   - Goal: Extract a factual campaign snapshot from `The Learning Ropes.cpnx.gz`.
   - Output: Update `ACTIVE_CAMPAIGN.md`, `SAVE_FORMAT_NOTES.md`, and a first campaign status report.

2. Identify MekHQ save/load source classes.
   - Goal: Confirm how `.cpnx.gz` files are loaded and saved in the local MekHQ source.
   - Output: Update `SAVE_FORMAT_NOTES.md` and `SOURCE_CODE_GUIDE.md`.

3. Produce the first campaign status report.
   - Goal: Practice the full campaign-analysis workflow on the active sample campaign.
   - Output: A report under `campaigns/learning-ropes/reports/`.

## Backlog

- Create private GitHub repo and migrate work tracking to GitHub Issues.
  - Status: `Not started`
  - Owner: `Mixed`
  - Goal: Use GitHub Issues/Projects for epics, backlog, active work, roadmaps, requirements, and agent-managed task execution instead of expanding the local markdown board.
  - Output: Private GitHub repo, issue labels/templates, initial epics/issues, documented GitHub workflow, and a simplified `TASKS.md` that points to GitHub as the primary tracker.
  - Notes: Before designing this, inspect the Sunnytown HQ project as the reference pattern because it already uses GitHub issues, roadmaps, requirements, and agent-managed task tracking/work execution.
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

- `2026-06-18`: Added `SOURCE_CHANGE_WORKFLOW.md`, tightened task-board transition rules, and documented current source build/test commands and Gradle blocker.
- `2026-06-18`: Added lightweight task tracking in `TASKS.md` and made it part of the documentation workflow.
- `2026-06-18`: Marked `The Learning Ropes.cpnx.gz` as the active practice campaign in `ACTIVE_CAMPAIGN.md`.
- `2026-06-18`: Established the initial workspace documentation baseline in commit `e858543`.

## Update Rules

- Update this file before switching from one major workstream to another.
- Move completed tasks to `Done` with the date and relevant commit if available.
- Move blocked tasks to `Blocked` with the exact missing input or condition.
- Keep durable findings in the relevant `docs/current/` file; this board tracks work, not detailed research.
