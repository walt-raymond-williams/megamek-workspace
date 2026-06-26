# Agent Handoff

## Issue

- GitHub issue: `#63`
- Roadmap entry: `Epic: Stabilize live MekHQ API reliability for MEK-RPG play`
- Priority: `High`

## Goal

Find which live MekHQ API collectors or readiness paths can stall during MEK-RPG play, then add or recommend timing instrumentation that makes future slow calls diagnosable.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_RELIABILITY_TRACKING.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`
- `docs/current/GUARDED_LIVE_MEKHQ_COMMAND_API_TRACKING.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEGAMEK_API_RELIABILITY_HANDOFF_2026-06-26.md`

## Expected Output

- Source-backed map of collectors reached by `/campaign/summary`, `/campaign/state`, and `/campaign/commands`.
- A current-doc note or tracking update identifying likely stall points and source owners.
- Minimal local-control-API timing instrumentation if safe in this slice, or a clear implementation plan if not.

## Files And Areas

Likely files to read:

- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporter.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCommandReadinessExporter.java`
- Existing local control API tests under `external/src/mekhq/MekHQ/test/`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "campaign/summary|campaign/state|campaign/commands|sections|summary|commands" external/src/mekhq/MekHQ/src/mekhq/service -g "*.java"
rg -n "LocalCampaignStateExporter|LocalCommandReadinessExporter|LocalControlService" external/src/mekhq/MekHQ/test -g "*.java"
```

## Constraints

- Preserve the disabled-by-default, loopback-only local API posture.
- Do not overwrite campaign saves.
- Do not include unrelated workspace changes.
- If source changes are made, follow `docs/current/SOURCE_CHANGE_WORKFLOW.md`.

## Acceptance Criteria

- Slow collector risks are labeled as `Confirmed from source`, `Confirmed by MEK-RPG handoff`, or `Inferred`.
- Any timing instrumentation is bounded and scoped to local API calls.
- Verification runs compile/checkstyle or records the exact blocker.
- The next recommended reliability issue is explicit.

## Open Questions

- Are timeouts caused by one collector, Swing/thread contention, command selector generation, or cumulative full-state export cost?
- Should timing metadata be response-visible, log-only, or both?

## Close-Out

- Completed on `2026-06-26`.
- Source commit: `5effaa5517` in `external/src/mekhq`.
- Durable audit: `docs/current/MEK_RPG_LIVE_MEKHQ_API_TIMEOUT_AUDIT.md`.
- Result: timing metadata is response-visible for summary/state/commands and read endpoints log a warning above `1000` ms.
- Next issue: `#64`, bound `/campaign/summary` and `/campaign/commands`.
