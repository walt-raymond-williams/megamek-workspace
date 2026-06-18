# Agent Handoff: Prototype Battle-Record MUL Round Trip

## Issue

- GitHub issue: `#10`
- Roadmap entry: `Epic: Robust tabletop battle result MUL workflow`
- Priority: `High`

## Goal

Prove that a generated or minimally edited battle-record MUL can be imported through MekHQ Resolve Manually and produces the expected campaign-facing effects without overwriting campaign saves.

Current status on `2026-06-18`: installed-jar writer/parser validation is complete, but the live MekHQ Resolve Manually import pass is blocked because Computer Use reports `Computer Use native pipe path is unavailable`.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEGAMEK_PROJECT_PROFILE.md`
- `docs/current/KNOWN_COMMANDS.md`
- `docs/handoffs/active/robust-tabletop-battle-result-mul-workflow.md`
- `docs/handoffs/archive/confirm-battle-record-mul-source-workflow.md`
- `docs/handoffs/archive/define-tabletop-result-input-schema.md`

## Expected Output

- Completed partial output: `docs/current/BATTLE_RECORD_MUL_ROUND_TRIP_VALIDATION.md` documents a generated battle-record `<record>` MUL round trip through `EntityListFile.writeEntityList(...)` and `MULParser`.
- Still needed: live MekHQ Resolve Manually import steps, expected effects, and observed effects for disposable campaign data.
- Still needed: decide whether this issue should remain blocked for a user-operated UI pass or whether issue `#11` can proceed with source/parser validation as sufficient strategy input.

## Files And Areas

- `analysis/tmp/`
- `analysis/`
- `docs/current/`
- `external/installs/MekHQ-0.51.00`

## Commands

```powershell
git status --short --branch
Test-Path 'C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\MekHQ.exe'
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
javac -cp 'MegaMek.jar;MekHQ.jar;lib/*' 'C:\Users\waltr\Documents\megamek-workspace\analysis\tmp\Issue10BattleRecordRoundTrip.java'
java -cp 'MegaMek.jar;MekHQ.jar;lib/*;C:\Users\waltr\Documents\megamek-workspace\analysis\tmp' Issue10BattleRecordRoundTrip 'C:\Users\waltr\Documents\megamek-workspace\analysis\tmp\issue-10-battle-record-round-trip.mul'
```

## Constraints

- Do not overwrite active campaign saves.
- Use copies or disposable campaign/scenario data under `analysis/tmp/`.
- Record exact manual MekHQ UI steps if UI verification is required.

## Acceptance Criteria

- Completed: a generated controlled battle-record MUL with `survivors`, `salvage`, `retreated`, `devastated`, and `kills` is accepted by MegaMek's `MULParser`, the parser used by MekHQ's manual resolution path.
- Still needed: the same file shape is selected through MekHQ's Resolve Manually UI in a disposable campaign.
- Still needed: observed campaign effects are compared against expected effects.
- Still needed: any mismatch is documented with likely source locations or follow-up issues.

## Open Questions

- Can the first round trip use installed jars only, or does it need source build support?
- Which campaign fixture should be used for destructive result-import testing?
- Who will perform the live UI import pass while Computer Use is unavailable: the user manually, or a future Codex session after the helper is fixed?
