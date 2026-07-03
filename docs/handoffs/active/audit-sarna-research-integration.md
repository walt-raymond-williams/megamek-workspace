# Agent Handoff

## Issue

- GitHub issue: `#89`
- Roadmap entry: `Epic: Add Sarna-backed BattleTech immersion research workflow`
- Priority: `High`

## Goal

Find the right durable homes for Sarna.net guidance across the MegaMek project profile, help/source workflow, campaign analysis workflow, BattleTech context, and templates.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SARNA_IMMERSION_WORKFLOW_TRACKING.md`
- `docs/current/MEGAMEK_PROJECT_PROFILE.md`
- `docs/current/HELP_FILE_WORKFLOW.md`

## Expected Output

- A concise audit note or tracking update listing docs to change, current source-priority language, and recommended integration points.
- Roadmap/tracking/task updates that point the next agent to issue `#90`.

## Files And Areas

Likely files to read:

- `docs/current/MEGAMEK_PROJECT_PROFILE.md`
- `docs/current/HELP_FILE_WORKFLOW.md`
- `docs/current/CAMPAIGN_ANALYSIS_WORKFLOW.md`
- `docs/current/BATTLETECH_CONTEXT.md`
- `docs/templates/`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "external sources|official|Sarna|lore|context|BattleTech" docs/current docs/templates
```

## Constraints

- Do not include unrelated user changes.
- Keep the audit focused on gameplay and agent workflow value.
- Do not rewrite the docs in this issue unless the user explicitly broadens the scope.
- Commit completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- `MEGAMEK_PROJECT_PROFILE.md`, `HELP_FILE_WORKFLOW.md`, `CAMPAIGN_ANALYSIS_WORKFLOW.md`, `BATTLETECH_CONTEXT.md`, and relevant templates are reviewed.
- Current guidance gaps are listed without over-scoping the work.
- Recommended docs/templates to edit in issue `#91` are identified.
- Roadmap, tracking doc, and task board are updated and changes are committed.

## Open Questions

- Should Sarna guidance live mostly in `HELP_FILE_WORKFLOW.md`, or should `MEGAMEK_PROJECT_PROFILE.md` carry a short top-level directive too?
- Which campaign-facing template would benefit most from a small lore/context slot?
