# Agent Handoff

## Issue

- GitHub issue: `#78`
- Roadmap entry: `Investigate MegaMek live combat narration bridge`
- Priority: `Medium`

## Goal

Determine the best V1 architecture for a MegaMek-side live combat narration bridge that can observe tactical game state and emit concise narration or pilot dialogue into MegaMek chat or an external MEK-RPG consumer.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_MEKHQ_INTEGRATION_ASSESSMENT.md`
- `docs/current/MEK_RPG_MEKHQ_COLLABORATION_BRIEF.md`

## Expected Output

- A source-backed note under `docs/current/` describing viable integration paths and tradeoffs.
- A recommended V1 architecture: external observer client, built-in bot/client mode, server-side hook, MekHQ-launched companion, or another path.
- Follow-up issue candidates if implementation is ready.
- Roadmap and task-board updates that reflect the recommendation.

## Files And Areas

Likely files to read:

- `external/src/megamek/megamek/src/megamek/client/AbstractClient.java`
- `external/src/megamek/megamek/src/megamek/client/IClient.java`
- `external/src/megamek/megamek/src/megamek/client/Client.java`
- `external/src/megamek/megamek/src/megamek/client/HeadlessClient.java`
- `external/src/megamek/megamek/src/megamek/client/bot/BotClient.java`
- `external/src/megamek/megamek/src/megamek/client/bot/ui/swing/BotGUI.java`
- `external/src/megamek/megamek/src/megamek/common/event/GameListener.java`
- `external/src/megamek/megamek/src/megamek/common/event/GameReportEvent.java`
- `external/src/megamek/megamek/src/megamek/server/Server.java`
- `external/src/megamek/megamek/src/megamek/server/totalWarfare/TWGameManager.java`
- `external/src/mekhq/MekHQ/src/mekhq/GameThread.java`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "sendChat|GameListener|GameReportEvent|GameNewActionEvent|gameEntityChange|setObserver|isObserver|externalId" external/src/megamek external/src/mekhq
```

## Constraints

- Do not modify MegaMek or MekHQ source in this issue unless a follow-up implementation task is explicitly created.
- Treat this as discovery/design first; preserve uncertainty with evidence labels.
- Do not include unrelated user changes.
- Commit completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- Existing roadmap and GitHub issues are checked for duplicates.
- Source owners and event/chat entry points are cited with file paths/classes.
- Feasibility is classified as yes, no, or blocked with concrete blockers.
- The recommendation identifies how V1 should observe combat, how it should map units/pilots where possible, and how it should publish narration.
- Follow-up issues are proposed only if the implementation path is clear enough to execute.

## Open Questions

- Can a non-playing observer client receive enough report/action/entity detail for useful narration without revealing double-blind information beyond what the user allows?
- Should narration come from a general narrator identity first, or per-pilot chat identities later?
- Can MekHQ-provided `externalId` values or other metadata reliably map MegaMek entities back to campaign units and pilots during a launched scenario?
- Should generated narration be written into MegaMek chat, an external MEK-RPG log/API, or both?
