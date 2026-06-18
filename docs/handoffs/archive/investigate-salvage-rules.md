# Agent Handoff: Investigate MekHQ And BattleTech Salvage Rules

## Issue

- GitHub issue: `#7`
- Roadmap entry: `Investigate MekHQ and BattleTech salvage rules`
- Parent epic: `#6` (`Epic: Robust tabletop battle result MUL workflow`)
- Priority: `High`

## Goal

Determine how MekHQ decides what salvage the player can keep, sell, exchange, ransom, or lose after a scenario, and compare MekHQ behavior against relevant BattleTech campaign salvage rules at a high level.

This is an investigation task. Do not implement the tabletop result MUL generator here.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEGAMEK_PROJECT_PROFILE.md`
- `docs/current/SOURCE_CODE_GUIDE.md`
- `docs/current/KNOWN_COMMANDS.md`
- `docs/handoffs/active/robust-tabletop-battle-result-mul-workflow.md`

Source areas to inspect:

- `external/src/mekhq/MekHQ/src/mekhq/campaign/ResolveScenarioTracker.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/handler/PostScenarioDialogHandler.java`
- `external/src/mekhq/MekHQ/src/mekhq/gui/dialog/ResolveScenarioWizardDialog.java`
- `external/src/mekhq/MekHQ/src/mekhq/gui/dialog/camOpsSalvage/`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/mission/Contract.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/mission/AtBContract.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/parts/`
- `external/src/mekhq/MekHQ/resources/mekhq/resources/`
- `external/installs/MekHQ-0.51.00/docs`

## Expected Output

- Completed on `2026-06-18`: added durable notes in `docs/current/SALVAGE_RULES_NOTES.md`.
- Completed on `2026-06-18`: added a short player-facing summary of:
  - what creates salvage candidates
  - how battlefield control matters
  - how contract salvage percentages or salvage exchange matter
  - how MekHQ handles enemy wrecks, player wrecks, devastated units, retreated units, loot, and CamOps salvage
  - what the tabletop result MUL generator must record versus what MekHQ calculates during resolution
- Completed on `2026-06-18`: issue close-out should point to commit and source notes.

## Files And Areas

Likely files to edit:

- `docs/current/SALVAGE_RULES_NOTES.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CODE_GUIDE.md` if new reusable source map entries are discovered
- `docs/handoffs/active/investigate-salvage-rules.md`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "salvage|Salvage|battlefield control|control|salvageExchange|salvage exchange|battle loss|BLC|ransom|leftover|actualSalvage|potentialSalvage" external/src/mekhq/MekHQ/src external/src/mekhq/MekHQ/resources external/installs/MekHQ-0.51.00/docs
rg -n "isUseCamOpsSalvage|resolveSalvage|SalvagePostScenarioPicker|CamOpsSalvage|salvageFormations|salvageTechs" external/src/mekhq/MekHQ/src
```

## Rule Reference Guidance

- Prefer local MekHQ docs/source for how this installation behaves.
- For actual BattleTech rules, use official or primary references where available, or user-provided rulebook page references.
- Do not reproduce large copyrighted rule passages. Summarize mechanics and cite the source context.
- If official rules are not locally available, record that limitation and identify what book/source needs user confirmation.

## Constraints

- Do not overwrite or modify campaign saves.
- Do not make MegaMek/MekHQ source changes for this task.
- Keep facts, source-confirmed behavior, rulebook-confirmed behavior, and inference separate.
- Preserve uncertainty when MekHQ behavior depends on campaign options.
- Commit completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- Completed: MekHQ salvage flow is traced from manual scenario resolution through post-scenario processing.
- Completed: the note identifies what result-MUL sections influence salvage: `salvage`, `retreated`, `devastated`, `survivors`, and battlefield-control choice.
- Completed: the note explains how contract terms, battle loss compensation, salvage exchange, CamOps salvage, and optional campaign settings affect outcomes.
- Completed with limitation: the note compares MekHQ behavior against Campaign Operations at a summarized level, using MekHQ source/UI text and the official Catalyst product page; detailed RAW comparison still needs user-provided rulebook/page references.
- Completed: the note lists implications for the robust tabletop battle-result MUL workflow.

## Open Questions

- Which optional salvage rules does the active campaign use?
- Which BattleTech rule source does the user want as the authoritative tabletop baseline?
- Are there MekHQ differences from tabletop rules that should become configurable in the eventual generator?
