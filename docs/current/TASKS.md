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

1. Run MekHQ quickstart roster UI validation.
   - Status: `Not started`
   - Owner: `User`
   - Goal: Manually validate that a disposable New Player Quickstart campaign can have one unit added and one original unit removed through MekHQ GM controls.
   - Output: Report the disposable save path, exact GM mode/add/remove UI paths, units added/removed, prompts/errors, and any pilot/TO&E/transport follow-up so Codex can finish issue `#17`.
   - Notes: GitHub issue `#21`; user task that unblocks agent issue `#17`; active checklist `docs/handoffs/active/user-quickstart-roster-ui-validation.md`; do not overwrite the bundled quickstart save.

2. Prototype battle-record MUL round-trip validation.
   - Status: `Not started`
   - Owner: `Codex`
   - Goal: Prove that a generated or minimally edited battle-record MUL can be imported through MekHQ Resolve Manually and produces expected campaign-facing effects on disposable data.
   - Output: Documented round-trip procedure, expected/observed effects, and notes on automated versus manual validation.
   - Notes: GitHub issue `#10`; child of epic `#6`; active handoff `docs/handoffs/active/prototype-battle-record-mul-round-trip.md`; follows completed schema issue `#9`.

3. Run MekHQ campaign exploration live-assist shakedown.
   - Status: `Not started`
   - Owner: `Mixed`
   - Goal: Start MekHQ and Codex together, then follow `CAMPAIGN_EXPLORATION_PLAN.md` while the user manually operates MekHQ and Codex records findings.
   - Output: Live-session notes and durable updates to current docs about campaign creation, first contract flow, travel, owned Leopard transport, aerospace transit scenario handling, repairs, salvage, finances, and open source follow-ups.
   - Notes: GitHub issue `#16`; active handoff `docs/handoffs/active/run-mekhq-campaign-exploration.md`; use a disposable exploration save unless the user explicitly selects another save.

4. Inspect the active demo campaign save without modifying it.
   - Status: `Not started`
   - Owner: `Codex`
   - Goal: Extract a factual campaign snapshot from `campaigns/demo/ai-ready-demo.cpnx.gz`.
   - Output: Update `ACTIVE_CAMPAIGN.md`, `SAVE_FORMAT_NOTES.md`, and a first campaign status report.
   - Notes: GitHub issue `#2`; active handoff `docs/handoffs/active/inspect-demo-campaign-save.md`.

5. Identify MekHQ save/load source classes.
   - Status: `Not started`
   - Owner: `Codex`
   - Goal: Confirm how `.cpnx.gz` files are loaded and saved in the local MekHQ source.
   - Output: Update `SAVE_FORMAT_NOTES.md` and `SOURCE_CODE_GUIDE.md`.
   - Notes: GitHub issue `#3`; active handoff `docs/handoffs/active/identify-mekhq-save-load-source-classes.md`.

6. Create help-file usage guidance for agents.
   - Status: `Not started`
   - Owner: `Codex`
   - Goal: Decide how future agents should use local MekHQ/MegaMek help files, in-app glossary resources, installed documentation, and source code.
   - Output: A durable workflow note or skill-style guide under `docs/current/`.
   - Notes: GitHub issue `#4`; active handoff `docs/handoffs/active/create-help-file-usage-guidance.md`; resume from `docs/current/HELP_FILE_USAGE_GUIDANCE_STATE.md`.

7. Turn this repo into an AI-ready project workflow demo.
   - Goal: Evolve this workspace into a reusable AI-ready project pattern with MegaMek/MekHQ as the worked example: source investigation, requirements discovery, verified commands, contributor handoff, campaign/save-file analysis, and agent memory.
   - Output: Clear repo positioning, generic workflow docs, MegaMek project profile, issue/requirement/PR templates, demo campaign fixture, and a decision on whether GitHub Projects should be used.

8. Produce the first campaign status report.
   - Goal: Practice the full campaign-analysis workflow on the active sample campaign.
   - Output: A report under `campaigns/demo/reports/`.

## Backlog

- Decide a report naming convention for campaign reports.
- Decide whether generated parser outputs should live under `analysis/generated/` by campaign name.
- Build a repeatable campaign summary extraction script after save structure is confirmed.
- Create source-reference notes for common MekHQ campaign actions and UI buttons.
- Low-priority epic `#15`: investigate photo-assisted BattleTech record sheet parsing with OpenCV/template matching, OCR fallback for unit names, and preferred unit matching through MekHQ UUIDs, short IDs, QR codes, or printed labels.

## Blocked

1. Verify quickstart roster replacement workflow.
   - Status: `Blocked`
   - Owner: `Mixed`
   - Goal: Verify the no-source-change workflow for replacing the New Player Quickstart roster in a disposable campaign save.
   - Output: `QUICKSTART_ROSTER_REPLACEMENT_VERIFICATION.md` has source-confirmed steps, safe-copy verification, and the remaining UI validation gap.
   - Notes: GitHub issue `#17`; child of epic `#14`; active handoff `docs/handoffs/active/verify-quickstart-roster-replacement.md`; live UI click-through remains blocked because the Windows Computer Use helper reported `Computer Use native pipe path is unavailable`. User-owned unblocker is issue `#21` with checklist `docs/handoffs/active/user-quickstart-roster-ui-validation.md`.

2. Verify Gradle build/test commands in local source repos.
   - Status: `Blocked`
   - Owner: `Mixed`
   - Goal: Run Gradle wrapper build/test commands in MegaMek, MekHQ, MegaMekLab, and mm-data.
   - Output: Mark source build/test commands as verified in `KNOWN_COMMANDS.md`.
   - Notes: Current shell resolves Java 8, JDK 21 is installed separately, and Gradle wrapper execution fails because `gradle/gradle-daemon-jvm.properties` requests daemon `toolchainVersion=17` with no local JDK 17/toolchain download configured.

## Done

- `2026-06-18`: Completed GitHub issue `#9` by defining the tabletop result input schema in `TABLETOP_RESULT_INPUT_SCHEMA.md`, separating first-session manual capture fields from optional future generator fields, and linking the schema from the tabletop result MUL workflow. Recommended next tabletop-result task is issue `#10`, prototype battle-record MUL round-trip validation.
- `2026-06-18`: Completed GitHub issue `#19` by documenting the custom RAT strategy in `CUSTOM_RAT_STRATEGY.md`. Recommendation: do not build custom RATs yet; use fixed OPFOR setup-MUL pools first, and revisit custom classic RATs or modern force-generator data only after confirmed inventory and fixed-pool play show a real need. Remaining roster-control child `#17` is blocked pending live MekHQ UI validation.
- `2026-06-18`: Completed GitHub issue `#18` by documenting the fixed OPFOR setup-MUL pool workflow in `FIXED_OPFOR_MUL_POOL_WORKFLOW.md`, adding `docs/templates/OPFOR_MUL_POOL_MANIFEST.csv`, and locally verifying placeholder MUL generation plus parser round trip with installed MekHQ/MegaMek jars. Raw placeholder MUL output remains ignored under `analysis/tmp/`; real inventory MULs need the user's confirmed miniature list. This fed the custom RAT strategy decision completed in issue `#19`; issue `#17` remains blocked pending live MekHQ UI validation.
- `2026-06-18`: Completed GitHub issue `#20` by defining the physical-miniature roster model in `PHYSICAL_MINIATURE_ROSTER_MODEL.md`, adding `docs/templates/PHYSICAL_MINIATURE_ROSTER.csv`, and archiving the handoff. This fed the fixed OPFOR MUL pool prototype completed in issue `#18`; issue `#17` remains blocked pending live MekHQ UI validation.
- `2026-06-18`: Completed source/docs discovery for GitHub issue `#14` by documenting MekHQ player roster and OPFOR control workflows in `MECH_ROSTER_CONTROL_WORKFLOWS.md`, then created child issues `#17` through `#20` for disposable-save quickstart roster replacement verification, physical-miniature roster data modeling, fixed OPFOR MUL pool prototyping, and a later custom RAT decision.
- `2026-06-18`: Added `CAMPAIGN_EXPLORATION_PLAN.md` to track the first hands-on MekHQ shakedown campaign, including new campaign creation, first contract flow, owned Leopard transport, two aerospace fighters, and a human-controlled transit aerospace scenario concept.
- `2026-06-18`: Completed GitHub issue `#7` by tracing MekHQ salvage behavior through manual scenario resolution, contract salvage terms, BLC, salvage exchange, and CamOps salvage. Findings are in `SALVAGE_RULES_NOTES.md`. This fed the schema issue `#9` and next round-trip validation issue `#10`.
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
