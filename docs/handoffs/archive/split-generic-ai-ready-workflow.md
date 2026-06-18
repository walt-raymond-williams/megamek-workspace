# Agent Handoff: Split Generic AI-Ready Workflow From MegaMek Profile

## Issue

- GitHub issue: `#1`
- Roadmap entry: `Split generic AI-ready workflow from MegaMek profile`
- Priority: `High`

## Goal

Separate reusable AI-ready project workflow guidance from MegaMek/MekHQ-specific paths, campaign data, source maps, and BattleTech rules posture.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/WORKSPACE.md`
- `docs/current/DOCUMENTATION_WORKFLOW.md`
- `docs/current/GITHUB_ISSUE_WORKFLOW.md`

## Expected Output

- A clear generic AI-ready workflow surface in this public canonical repository.
- A MegaMek-specific project profile or equivalent location for local assumptions and domain guidance.
- Updated links from `AGENTS.md`, `README.md`, and `docs/current/WORKSPACE.md` if structure changes.
- Roadmap and task-board updates reflecting any follow-on work.

## Files And Areas

Likely files to read or edit:

- `AGENTS.md`
- `README.md`
- `docs/current/WORKSPACE.md`
- `docs/current/ROADMAP.md`
- `docs/current/DOCUMENTATION_WORKFLOW.md`
- `docs/current/AI_READY_PROJECT_WORKFLOW.md`
- `docs/current/MEGAMEK_PROJECT_PROFILE.md`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg "MegaMek|MekHQ|BattleTech|AI-ready|project profile" AGENTS.md README.md docs
```

## Constraints

- Preserve this repository as a real MegaMek/MekHQ worked example.
- Do not remove useful MegaMek campaign/source guidance; relocate or clarify it.
- Keep durable architecture and workflow decisions in `docs/current/`.
- Commit completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- Generic workflow concepts are visible without requiring MegaMek-specific knowledge.
- MegaMek-specific assumptions have a clear home.
- Existing campaign-analysis and source-inspection workflows still point to the right docs.
- `ROADMAP.md` and `TASKS.md` reflect the result and follow-on work.

## Resolution

- This remains one public canonical repo, not a private workspace plus a separate template repo.
- The reusable workflow lives at `docs/current/AI_READY_PROJECT_WORKFLOW.md`.
- The active MegaMek/MekHQ profile lives at `docs/current/MEGAMEK_PROJECT_PROFILE.md`.
- Project profiles stay under `docs/current/` unless the user explicitly asks for a different structure.
