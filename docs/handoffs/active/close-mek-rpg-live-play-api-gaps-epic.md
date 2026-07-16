# Agent Handoff

## Issue

- GitHub issue: `#94`
- Roadmap entry: `Epic: Close MEK-RPG live-play MekHQ API gaps`
- Priority: `High`

## Goal

Coordinate the producer-side MekHQ local API work needed to address MEK-RPG's live play gap request without accepting unbounded, unsafe, or unsourceable endpoint shapes.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_PLAY_API_GAP_PRODUCER_PLAN.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_ACTIVITY_HISTORY_SOURCE_AUDIT.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_PLAYTEST_API_GAP_CHANGE_REQUEST_2026_07_16.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_PLAYTEST_API_GAP_REPORT.md`

## Expected Output

- Child issues are executed in priority order.
- Durable source-backed decisions are recorded in `docs/current/`.
- Implemented source changes are verified and documented.
- MEK-RPG-facing contract docs explain actual supported shapes, limits, unknown markers, withheld data, and unsupported facts.

## Files And Areas

Likely files to read or edit:

- `docs/current/MEK_RPG_LIVE_PLAY_API_GAP_PRODUCER_PLAN.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `external/src/mekhq/MekHQ/src/mekhq/service/`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "pending-deployments|LocalCampaignStateExporter|LocalControlService|Transaction|getTransactions|salvage|reinforce|battle value" external/src/mekhq/MekHQ/src
```

## Constraints

- This epic is not a direct implementation task.
- Keep MEK-RPG away from routine active-save parsing during live play.
- Do not promise unbounded ledgers or exact calculations MekHQ does not own.
- Preserve fog-of-war behavior with explicit hidden/withheld markers.
- Do not include unrelated user changes.

## Acceptance Criteria

- Roadmap and tracking docs name the child issues and handoffs.
- Each child issue has a clear handoff, dependencies, and bounded acceptance criteria.
- Completed child issues update docs, tests, and source commit references.

## Open Questions

- Which source branch or writable remote should be used for MekHQ implementation commits?
- Should scenario intel become a dedicated endpoint after the design issue, or remain an extension of existing pending-deployment/state reads?
