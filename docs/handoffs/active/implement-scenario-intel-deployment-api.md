# Agent Handoff

## Issue

- GitHub issue: `#96`
- Roadmap entry: `Implement scenario intel, deployment, BV, and reinforcement export`
- Priority: `High`

## Goal

Expose source-owned pending/current scenario intel needed for mission planning: player-force BV, assigned-unit BV, deployment requirements, reinforcement metadata, visible/estimated OpFor summaries, and fog-of-war markers.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_LIVE_PLAY_API_GAP_PRODUCER_PLAN.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`
- The design note produced by the bounded contract issue.
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_PLAYTEST_API_GAP_REPORT.md`

## Expected Output

- MekHQ source changes extending `/campaign/pending-deployments`, scenario state DTOs, or a source-approved scenario-intel endpoint.
- Tests for player-force BV, assigned-unit BV, reinforcement metadata, OpFor visibility/estimates, and hidden/unknown markers.
- Workspace docs updated with source commit, endpoint shape, and verification.

## Files And Areas

Likely files to read or edit:

- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporter.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporterTest.java`
- Scenario, StratCon, AtB, force, and unit assignment source classes found by `rg`.
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "pending-deployments|bot_forces|battle value|getBV|reinforce|reinforcement|deployment|Scenario|StratCon" external/src/mekhq/MekHQ/src
.\gradlew.bat --no-daemon :MekHQ:compileJava :MekHQ:checkstyleMain :MekHQ:checkstyleTest
```

## Constraints

- Preserve fog of war. Return exact entity lists only when MekHQ source already allows disclosure.
- Do not infer deployment requirements from narrative descriptions unless marked as an estimate or unsupported.
- Keep endpoint responses bounded and partial-response capable.
- Do not parse active saves as a workaround.

## Acceptance Criteria

- MEK-RPG can produce a mission-intel brief from API data alone for known fields.
- Reinforcement success, force mapping, arrival turn, entry edge, and deployment zone are exact, unknown, withheld, or unsupported instead of absent.
- Player and OpFor BV gaps have source-backed fields or explicit unsupported entries.
- Tests and docs cover hidden and known-data cases.

## Open Questions

- Where does MekHQ store reinforcement arrival/entry details before launching MegaMek, and are they intentionally hidden until tactical launch?
- Are contract/scenario deployment requirements structured in source or only implicit in scenario generation logic?
