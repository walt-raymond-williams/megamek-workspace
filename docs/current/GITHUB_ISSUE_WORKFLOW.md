# GitHub Issue Workflow

Use this workflow when turning roadmap items into GitHub Issues for execution, discussion, or agent handoff.

## Operating Model

- `docs/current/ROADMAP.md` is the durable planning source.
- GitHub Issues are trackable execution units created from roadmap entries.
- `docs/current/TASKS.md` tracks immediate local work and should point back to the roadmap when planning details live there.
- Handoff documents give agents enough context to work from an issue without rediscovering the whole repo.

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
4. Move completed local work in `TASKS.md` to `Done`.
5. Commit documentation updates.

## Current Local State

- `Confirmed locally`: GitHub CLI exists at `C:\Program Files\GitHub CLI\gh.exe`.
- `Confirmed locally`: no Git remote was configured when this workflow was written on `2026-06-18`.
