# Agent Handoff

## Issue

- GitHub issue: `#91`
- Roadmap entry: `Epic: Add Sarna-backed BattleTech immersion research workflow`
- Priority: `High`

## Goal

Apply the designed Sarna workflow to the project profile and current guidance so future agents use Sarna naturally during MEK-RPG gameplay support.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SARNA_IMMERSION_WORKFLOW_TRACKING.md`
- `docs/current/SARNA_BATTLETECH_IMMERSION_WORKFLOW.md`
- issue `#89` audit output

## Expected Output

- Updated `docs/current/` guidance.
- Any useful template updates under `docs/templates/`.
- Roadmap/tracking/task updates.

## Files And Areas

Likely files to read or edit:

- `docs/current/MEGAMEK_PROJECT_PROFILE.md`
- `docs/current/HELP_FILE_WORKFLOW.md`
- `docs/current/CAMPAIGN_ANALYSIS_WORKFLOW.md`
- `docs/current/BATTLETECH_CONTEXT.md`
- `docs/templates/`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "Sarna|BattleTechWiki|external sources|lore context" docs/current docs/templates
```

## Constraints

- Do not include unrelated user changes.
- Keep the guidance focused on gameplay value and immersion.
- Avoid duplicating the same policy paragraph across many files; link to the owning workflow doc where possible.
- Commit completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- `MEGAMEK_PROJECT_PROFILE.md` and `HELP_FILE_WORKFLOW.md` include Sarna as the preferred BattleTech lore/context source.
- Campaign analysis or BattleTech context docs include practical usage patterns for immersion.
- At least one report or note template has a light Sarna/context slot if useful.
- Guidance remains focused on gameplay value and does not overemphasize copyright discussion.
- Roadmap, tracking doc, and task board are updated and changes are committed.

## Open Questions

- Should the Sarna workflow become its own current doc, or be folded into `HELP_FILE_WORKFLOW.md` after issue `#91`?
