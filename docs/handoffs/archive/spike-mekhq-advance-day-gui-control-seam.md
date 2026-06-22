# Agent Handoff

## Issue

- GitHub issue: `#34`
- Roadmap entry: `Spike source-level MekHQ GUI control seam for Advance Day`
- Priority: `Medium`

## Goal

Determine whether a locally modified MekHQ build can expose a safe source-level GUI control seam that invokes the real Advance Day path without coordinate clicking, direct save editing, or reimplementing campaign business logic.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/KNOWN_COMMANDS.md`
- `docs/current/MEK_RPG_MEKHQ_BRIDGE_PRIMITIVES.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORTER_OWNERSHIP_DECISION.md`

## Expected Output

- `docs/current/MEKHQ_ADVANCE_DAY_GUI_CONTROL_SEAM_SPIKE.md`

The spike document should include:

- Source files/classes involved in the Advance Day UI action.
- The actual method/action path used by the GUI.
- GUI dependencies encountered.
- Whether the action can be invoked directly from a local control seam.
- Whether blocking dialogs/prompts can be detected cleanly.
- Whether save-after-success can be controlled safely.
- Risks and brittleness.
- A recommendation to proceed now, proceed after refactor, prefer a headless command layer, or defer.

## Files And Areas

Likely files to inspect:

- `external/src/mekhq`
- `external/src/megamek`
- `docs/current/SOURCE_CODE_GUIDE.md`
- `docs/current/KNOWN_COMMANDS.md`

Likely files to edit:

- `docs/current/MEKHQ_ADVANCE_DAY_GUI_CONTROL_SEAM_SPIKE.md`
- `docs/current/SOURCE_CODE_GUIDE.md` if the source path discovery is reusable.
- `docs/current/KNOWN_COMMANDS.md` if a repeatable source/build/control-seam command is proven.
- `docs/current/TASKS.md` and `docs/current/ROADMAP.md` for status close-out.

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "Advance Day|advance day|newDay|NewDay|CampaignNewDay" external/src/mekhq
rg -n "CampaignNewDayManager|newDay\\(" external/src/mekhq external/src/megamek
java -version
javac -version
```

Known blocker from current docs:

- Local source Gradle build/test verification is blocked by the Java 17 daemon/toolchain mismatch unless the environment has changed. Source reading and a narrow local prototype may still be possible, but record the exact blocker if build/test cannot run.

## Constraints

- Do not disrupt issue `#23` real-life campaign setup.
- Do not close or replace issues `#10`, `#13`, or `#17`.
- Do not propose direct `.cpnx`, `.cpnx.gz`, or XML mutation.
- Do not propose coordinate clicking or OS-level screen automation.
- Do not add prompt auto-answering, multi-day automation, contract/purchase/repair/salvage automation, or MEK-RPG writeback.
- Run only against copied/disposable campaign saves if a prototype is attempted.
- Verify a campaign is loaded before invoking Advance Day.
- Verify expected campaign name/date before invoking when provided.
- Trigger exactly one Advance Day action.
- Save only after confirmed success and only when explicitly configured.
- Commit completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- The exact Advance Day source path is identified.
- The investigation determines whether a local source-level control seam can call the same path.
- The result preserves disposable-save safety.
- The result clearly states whether to proceed with prototype implementation.
- No direct save mutation or coordinate-clicking approach is proposed.

## Open Questions

- Is the real Advance Day path cleanly exposed through a Swing `Action`, action listener, command handler, or direct method?
- Can blocking dialogs/prompts be detected without answering them?
- Does a source-level seam need a small refactor before it is safe enough for personal workflow automation?
