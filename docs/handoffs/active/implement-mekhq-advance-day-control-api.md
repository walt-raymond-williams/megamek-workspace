# Agent Handoff

## Issue

- GitHub issue: `#35`
- Roadmap entry: `Implement local MekHQ Advance Day control API prototype`
- Priority: `Medium`

## Goal

Implement or precisely prototype-plan a narrow local-only MekHQ source control API that can be called while MekHQ is already open with a campaign loaded. The command should invoke exactly one real Advance Day path through `Campaign#newDay()` inside the loaded GUI app, with guardrails and structured results.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/KNOWN_COMMANDS.md`
- `docs/current/MEKHQ_ADVANCE_DAY_GUI_CONTROL_SEAM_SPIKE.md`
- `docs/current/SOURCE_CODE_GUIDE.md`

## Expected Output

- A concrete local API/command seam design for the running MekHQ app.
- Source changes in `external/src/mekhq` if the chosen seam is feasible in this pass.
- Workspace docs updated with the selected API shape, source files touched, verification status, and live user-assisted test instructions.

Current progress:

- Source prototype added in `external/src/mekhq`:
  - `MekHQ/src/mekhq/service/LocalControlService.java`
  - `MekHQ/src/mekhq/MekHQ.java`
- Source branch: `codex/mekhq-advance-day-control-api`
- Source commit: `9046a8075e` (`Add local advance day control API prototype`)
- Workspace prototype note added:
  - `docs/current/MEKHQ_ADVANCE_DAY_CONTROL_API_PROTOTYPE.md`
- Gradle compile remains blocked by the Java 17 daemon/toolchain issue.
- Fallback `javac` checks against installed MekHQ `0.51.00` jars passed for the new service and modified `MekHQ.java`.
- Live endpoint testing has not run and should wait for the user.

## Files And Areas

Likely source files to inspect or edit:

- `external/src/mekhq/MekHQ/src/mekhq/gui/CampaignGUI.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/CampaignController.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/Campaign.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/CampaignNewDayManager.java`
- any existing MekHQ application startup, command, socket, debug, or local-service entry points discovered through source search

Likely workspace files to edit:

- `docs/current/MEKHQ_ADVANCE_DAY_CONTROL_API_PROTOTYPE.md`
- `docs/current/SOURCE_CODE_GUIDE.md`
- `docs/current/KNOWN_COMMANDS.md` if a repeatable run/build/control command is proven or newly blocked
- `docs/current/TASKS.md`
- `docs/current/ROADMAP.md`

## Commands

Useful starting commands:

```powershell
git status --short --branch
git -C external/src/mekhq status --short --branch
rg -n "advanceDay|newDay\\(|CampaignNewDayManager|AdvanceTimePanel" external/src/mekhq/MekHQ/src/mekhq -g "*.java"
rg -n "socket|server|http|localhost|command|debug|jmx|ipc|pipe|rest|api" external/src/mekhq/MekHQ/src/mekhq -g "*.java"
```

Known build/test blocker to re-check before claiming verification:

```powershell
cd C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq
.\gradlew.bat :MekHQ:test
```

Current docs record that Gradle execution may fail because the daemon requests Java 17 while local shell Java/toolchain state is mismatched.

## Constraints

- Do not include unrelated user changes.
- Do not disrupt issue `#23` real-life campaign setup.
- Live UI/prototype testing should wait for the user to be present.
- Do not mutate `.cpnx`, `.cpnx.gz`, or XML directly.
- Do not use coordinate clicking or OS-level screen automation as the primary control method.
- Do not add prompt auto-answering.
- Do not add multi-day automation.
- Do not add contract, purchase, repair, salvage, Resolve Manually, roster, or MEK-RPG writeback automation in this issue.
- Run only against copied/disposable saves if a live prototype test is attempted later.
- Commit completed workspace documentation changes before stopping unless explicitly told not to.
- Source repo commits are separate from workspace commits; follow `SOURCE_CHANGE_WORKFLOW.md`.

## Acceptance Criteria

- A concrete local API transport or command seam is selected and documented.
- The command refuses to run when no campaign is loaded.
- The command refuses to run when expected campaign identity or expected date checks fail.
- The command invokes exactly one `Campaign#newDay()` path inside the loaded GUI app.
- The command returns structured `advanced`, `blocked`, `failed`, or `refused` results.
- Save-after-success is disabled by default and only writes to an explicit disposable path when requested.
- The implementation does not answer dialogs automatically.
- Workspace docs record verification status and live user-assisted test instructions.

## Open Questions

- Should the current disabled-by-default localhost HTTP service remain the first transport after live validation?
- Is there already a better MekHQ-supported local service or debug hook to extend, or should this remain a local-only patch?
- Can modal/prompt detection be made reliable enough for the first prototype without adding a general dialog policy layer?
