# Agent Handoff

## Issue

- GitHub issue: `#70`
- Roadmap entry: `Epic: Add guarded TO&E and pilot assignment commands for MEK-RPG`
- Priority: `High`

## Goal

Coordinate the focused guarded-command workstream for pilot assignment and TO&E edits. This epic should decompose MEK-RPG's live play request into source-audited, narrowly scoped MekHQ local API stories.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/GUARDED_LIVE_MEKHQ_COMMAND_API_TRACKING.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEGAMEK_TOE_PILOT_ASSIGNMENT_API_HANDOFF.md`

## Expected Output

- Keep child issues `#71` through `#77` sequenced and scoped.
- Preserve the rule that MEK-RPG never edits MekHQ saves or reimplements assignment/TO&E validators.
- Ensure each implementation story has source-owned selectors, guard facts, dry-run validation, prompt refusal, opt-in save behavior, and disposable-campaign verification.

## Files And Areas

Likely files to read or edit:

- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/GUARDED_LIVE_MEKHQ_COMMAND_API_TRACKING.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/handoffs/active/`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporter.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCommandReadinessExporter.java`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "assign|crew|pilot|force|TO&E|toe|Force" external/src/mekhq/MekHQ/src
```

## Constraints

- Do not implement the epic directly.
- Do not edit campaign saves directly.
- Do not treat display names, table rows, or localized labels as stable selectors.
- Do not auto-answer arbitrary MekHQ dialogs.
- Commit completed workspace documentation changes before stopping unless explicitly told not to.

## Acceptance Criteria

- Child issues `#71` through `#77` remain linked from roadmap and tracking docs.
- Each child issue has an active handoff until it is completed and archived.
- The epic can close only after pilot/TO&E command work lands or a durable stop/defer decision is documented.

## Open Questions

- Which exact MekHQ source methods own pilot/crew assignment and force tree edits?
- Can the command API reuse existing logic without Swing dialogs, or is source service extraction required?
- Which command-specific selectors should be durable ids versus live-session tokens tied to `stateRevision`?
