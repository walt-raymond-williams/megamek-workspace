# GitHub Issue Workflow

Use this workflow when turning roadmap items into GitHub Issues for execution, discussion, or agent handoff.

## Operating Model

- `docs/current/ROADMAP.md` is the durable planning source.
- GitHub Issues are trackable execution units created from roadmap entries.
- `docs/current/TASKS.md` tracks immediate local work and should point back to the roadmap when planning details live there.
- Handoff documents give agents enough context to work from an issue without rediscovering the whole repo.
- Active handoff documents live in `docs/handoffs/active/`.
- Completed handoff documents move to `docs/handoffs/archive/` after the issue is done, committed, and any merge or close-out work is complete.

## Handoff Lifecycle

Use one handoff document per agent-executed GitHub issue.

1. Create the handoff from `docs/templates/AGENT_HANDOFF.md` under `docs/handoffs/active/`.
2. Link the handoff path from the GitHub issue body.
3. Link the GitHub issue number from the handoff.
4. Keep architectural or durable process findings in `docs/current/`, not only in the handoff.
5. When the issue is complete, move the handoff to `docs/handoffs/archive/`.
6. Update `ROADMAP.md`, `TASKS.md`, and any affected current docs before committing the close-out change.

Handoffs are execution context. `docs/current/` remains the live source for durable architecture, workflow, source, and campaign knowledge.

## Before Creating Issues

1. Check repository status:

```powershell
git status --short --branch
git remote -v
```

2. Check GitHub CLI auth if using `gh`:

```powershell
gh auth status
```

3. Choose a roadmap entry with status `Ready for issue`.
4. Confirm the entry has a clear goal, expected output, dependencies, and open questions.
5. Draft a handoff using `docs/templates/AGENT_HANDOFF.md` when the issue is intended for an agent.

## Creating An Issue

Create issues gradually. Do not file every future roadmap item at once if later discoveries may change the work.

Recommended issue body structure:

```markdown
## Goal

## Context

## Expected Output

## Handoff

## Dependencies / Blockers

## Acceptance Criteria
```

For agent-executed work, use `.github/ISSUE_TEMPLATE/agent-task.md` when creating the issue through GitHub.

After creating the issue:

1. Update the roadmap entry:
   - `Status: Issue created`
   - `Issue: #<number>`
2. If work starts immediately, move or reference it in `TASKS.md`.
3. Commit the roadmap and task-board update.

## Agent Handoff

For agent-executed issues:

- Link the GitHub issue.
- Include the roadmap entry title.
- Identify required docs to read first.
- Identify expected files to modify.
- List commands to run or known blockers.
- State what must be committed before the agent stops.

Use `docs/templates/AGENT_HANDOFF.md` for the handoff text.

## Completion

When an issue is done:

1. Update the roadmap entry to `Done`.
2. Record the issue number and commit hash if useful.
3. Update or remove stale downstream roadmap assumptions.
4. Move the issue handoff from `docs/handoffs/active/` to `docs/handoffs/archive/`.
5. Move completed local work in `TASKS.md` to `Done`.
6. Commit documentation updates.

## Current Local State

- `Confirmed locally`: GitHub CLI exists at `C:\Program Files\GitHub CLI\gh.exe`.
- `Confirmed locally`: GitHub CLI is authenticated as `walt-raymond-williams`.
- `Confirmed locally`: `origin` points to `https://github.com/walt-raymond-williams/megamek-workspace.git`.
- `Confirmed locally`: `master` tracks `origin/master`.
- `Confirmed locally`: GitHub label `agent-task` exists.
- `Confirmed locally`: Active agent issues `#1` through `#4` were created from `ROADMAP.md` on `2026-06-18`.
