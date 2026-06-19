# Agent Handoff: Study Generated Battle-Record MUL

## Issue

- GitHub issue: `#22`
- Roadmap entry: `Epic: Robust tabletop battle result MUL workflow`
- Priority: `High`

## Goal

Study the live MegaMek battle-record MUL generated during the `2026-06-18` MekHQ exploration shakedown. Compare it against the resolved MegaMek game artifacts and the MekHQ campaign save state, then document anything useful for future tabletop result-MUL import, validation, or generation work.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/CAMPAIGN_EXPLORATION_PLAN.md`
- `docs/current/TABLETOP_RESULT_MUL_WORKFLOW.md`
- `docs/current/BATTLE_RECORD_MUL_ROUND_TRIP_VALIDATION.md`
- `docs/current/TABLETOP_RESULT_INPUT_SCHEMA.md`

## Key Local Artifacts

- Full battle-record MUL: `C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\The Learning Ropes.mul`
- Salvage-only/log artifact: `C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\logs\salvage.mul`
- Entity status report: `C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\logs\entitystatus.txt`
- Game action telemetry: `C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\logs\game_actions_1.tsv`
- Newest observed save: `C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\campaigns\Autosave-1-The Learning Ropes-30250720.cpnx.gz`

## Initial Observations At Issue Creation

- `Confirmed from generated MUL`: `The Learning Ropes.mul` is a top-level `<record version="0.51.00">` file.
- `Confirmed from generated MUL`: it contains `survivors`, `allies`, `salvage`, `devastated`, and `kills` sections.
- `Confirmed from generated MUL`: `survivors` includes friendly campaign units with campaign `Unit` UUIDs in `externalId`.
- `Superseded by issue work`: initial quick observations about kill credit were corrected during the study. See `docs/current/GENERATED_BATTLE_RECORD_MUL_STUDY.md` for the verified raw `<kills>` rows and the separate post-resolution MekHQ campaign kill records.
- `Confirmed by user`: the battle ended after the user killed all enemies and accepted battlefield control, but the movement/exit objective was not completed; MekHQ recorded the scenario as `DRAW`.

## Expected Output

- Durable findings in `docs/current/TABLETOP_RESULT_MUL_WORKFLOW.md` or a focused companion note under `docs/current/`.
- A comparison of the full battle-record MUL, `salvage.mul`, `entitystatus.txt`, and save-state scenario/result data.
- Findings about external ids, crew ids, result sections, ammo/damage/crit serialization, kill credit, salvage/devastated classification, and fields that seem required for MekHQ import.
- A short checklist for the user's next manual import attempt.

## Suggested Commands

```powershell
git status --short --branch
Get-Content 'C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\The Learning Ropes.mul' | Select-Object -First 120
Select-String -Path 'C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\The Learning Ropes.mul' -Pattern '<record|<survivors|<allies|<salvage|<retreated|<devastated|<kills|<kill |externalId|hits=|ejected=|isHit=|isDestroyed=|shots='
Get-Content 'C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\logs\entitystatus.txt' | Select-Object -First 140
```

For save inspection, copy to `analysis/tmp/` first and do not overwrite the original save.

## Constraints

- Do not overwrite campaign saves or generated MUL artifacts.
- Work on copies under `analysis/tmp/` if parsing or transforming files.
- Do not commit raw local payload from `external/` unless explicitly requested.
- Preserve evidence labels: generated MUL, generated logs, save copy, source, or user.

## Acceptance Criteria

- The generated full battle-record MUL structure is summarized with section meanings and concrete examples from this file.
- Differences between `The Learning Ropes.mul` and `logs\salvage.mul` are documented.
- The study explains how the file maps to the resolved game: friendly survivors, enemy salvage, devastated units, and kill credit.
- The study identifies what to watch during the next user-operated manual import attempt.

## Open Questions

- Does MekHQ's manual Resolve flow accept this exact `The Learning Ropes.mul` file when selected for a disposable scenario?
- Does selecting battlefield control before completing a movement objective affect only scenario status, or also the resulting battle-record classification?
- Are `allies` in this file truly allied units for MekHQ import purposes, or enemy survivors classified by MegaMek player/team mapping?

## Completion

- Completed on `2026-06-19`.
- Durable output: `docs/current/GENERATED_BATTLE_RECORD_MUL_STUDY.md`.
- Follow-up: issue `#10` should use the study checklist during the next user-operated MekHQ Resolve Manually import pass.
