# Agent Handoff

## Issue

- GitHub issue: `#16`
- Roadmap entry: `Run MekHQ campaign exploration live-assist shakedown`
- Priority: `High`

## Goal

Assist the user during a hands-on MekHQ exploration session while the user manually operates the application. Codex should follow `docs/current/CAMPAIGN_EXPLORATION_PLAN.md`, answer questions, inspect docs/source as needed, record confirmed behavior, and update durable workspace docs after the session.

This is not a headless automation task. The expected workflow is:

1. The user starts MekHQ.
2. The user starts Codex and points it at this handoff.
3. The user interacts with MekHQ manually.
4. Codex acts as a campaign aide and documentation recorder.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/CAMPAIGN_EXPLORATION_PLAN.md`
- `docs/current/ACTIVE_CAMPAIGN.md`
- `docs/current/KNOWN_COMMANDS.md`
- `docs/current/MEGAMEK_PROJECT_PROFILE.md`
- `docs/current/DOCUMENTATION_WORKFLOW.md`

Useful local docs:

- `external/installs/MekHQ-0.51.00/docs/0_MHQ New Player Guide.pdf`
- `external/installs/MekHQ-0.51.00/docs/Aerospace Stuff/How_to_use_aerospace_units_on_ground_maps_in_MegaMek.md`

Useful source references already identified:

- `external/src/mekhq/MekHQ/src/mekhq/gui/InterstellarMapPanel.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/utilities/JumpBlockers.java`

## Expected Output

- A live-session note or update in `docs/current/CAMPAIGN_EXPLORATION_PLAN.md` using the tracking log template.
- Updates to `docs/current/ACTIVE_CAMPAIGN.md` if the exploration save becomes the active practice campaign.
- Updates to `docs/current/KNOWN_COMMANDS.md` if launch, save, or inspection commands are confirmed or corrected.
- Updates to narrower docs if the session confirms durable behavior about travel, custom scenarios, aerospace, result entry, repair, salvage, or campaign saves.
- A final user-facing summary of what was tried, what worked, what failed, and what the next session should do.

## Files And Areas

Likely files to read or edit:

- `docs/current/CAMPAIGN_EXPLORATION_PLAN.md`
- `docs/current/ACTIVE_CAMPAIGN.md`
- `docs/current/TASKS.md`
- `docs/current/KNOWN_COMMANDS.md`
- `docs/current/SAVE_FORMAT_NOTES.md`
- `docs/current/TABLETOP_RESULT_MUL_WORKFLOW.md`
- `docs/current/SALVAGE_RULES_NOTES.md`

Do not overwrite real campaign saves. Use a disposable exploration save unless the user explicitly chooses otherwise.

## Commands

Useful starting checks:

```powershell
git status --short --branch
Test-Path 'C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\MekHQ.exe'
```

Launch MekHQ if the user asks Codex to launch it:

```powershell
cd C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00
.\MekHQ.exe
```

Use JDK 21 first on `PATH` if a Java command is needed from the shell:

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
java -version
```

Safe save inspection pattern:

```powershell
New-Item -ItemType Directory -Force analysis\tmp
Copy-Item 'C:\path\to\campaign.cpnx.gz' analysis\tmp\
```

## Live-Assist Procedure

1. Confirm which save is being used: new exploration save, installed sample, or existing campaign.
2. Record the exact MekHQ version and save path.
3. Help the user follow the phases in `CAMPAIGN_EXPLORATION_PLAN.md`.
4. When the user hits uncertainty, first answer from local docs if it is a user-facing concept; inspect source before giving a confident answer about exact behavior.
5. Keep facts, inferences, and recommendations separate.
6. Record each meaningful finding in the tracking log template.
7. After the session, commit and push documentation updates unless the user explicitly says not to.

## Aerospace Transit Scenario Focus

The target custom scenario is:

- JumpShip arrives in the destination system.
- Leopard-class DropShip departs toward the contract planet.
- Two player aerospace fighters launch from the Leopard.
- Hostile aerospace assets intercept during transit.
- Both sides are controlled by human players.
- Results are reflected back into MekHQ manually or through the cleanest available custom scenario workflow.

Known posture:

- `Confirmed from local docs`: MegaMek supports aerospace units on ground maps and can enable space maps from the lobby.
- `Confirmed from local docs`: The bot is not expected to handle aerospace fighters on space maps.
- `Unknown`: The best MekHQ-native way to attach a custom transit aerospace battle to a contract during travel.

If the clean workflow is not obvious from the UI, treat the first battle as a manual/GM scenario and record the gap for source follow-up.

## Constraints

- Do not include unrelated user changes.
- Preserve uncertainty and evidence labels.
- Do not overwrite, move, or delete campaign saves unless explicitly asked.
- Work on copies under `analysis/tmp/` for save inspection.
- Avoid relying on bot-controlled space combat for this exploration.
- Commit completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- MekHQ is launched or the user confirms it is running.
- The exploration plan is attempted far enough to produce actionable findings.
- The session records what was actually tried, what worked, what failed, and what should happen next.
- Durable discoveries are documented in the appropriate `docs/current/` file.
- Repository documentation updates are committed and pushed, or the blocker is clearly recorded.

## Open Questions

- Which exact save path should become the exploration campaign?
- Should the exploration campaign replace the current active practice campaign in `ACTIVE_CAMPAIGN.md`, or remain separate?
- Which Leopard variant and aerospace fighter pair should be used for the first human-vs-human aerospace test?
- Can MekHQ attach the transit battle to an active contract, or should it be tracked as a manual narrative event?
- What is the cleanest way to apply aerospace fighter damage, pilot injury, ammunition use, and possible Leopard damage after the battle?
