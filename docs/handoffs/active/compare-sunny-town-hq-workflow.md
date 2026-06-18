# Agent Handoff: Compare Workflow Against Sunny Town HQ

## Issue

- GitHub issue: `#5`
- Roadmap entry: `Compare workflow against Sunny Town HQ`
- Priority: `High`

## Goal

Compare this repository's AI workflow, roadmap, handoff, issue, feature-branch, and PR-review process against Sunny Town HQ, then import useful patterns into this workspace.

## Required Context

Read these first in this repository:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/GITHUB_ISSUE_WORKFLOW.md`
- `docs/current/DOCUMENTATION_WORKFLOW.md`

Read these in Sunny Town HQ:

- `C:\Users\waltr\Documents\New project\AGENTS.md`
- `C:\Users\waltr\Documents\New project\docs\GITHUB_TRACKING_DOC_GUIDE.md`
- `C:\Users\waltr\Documents\New project\docs\EPIC_BACKLOG.md`
- `C:\Users\waltr\Documents\New project\docs\NPC_LIFE_WORK_PR_READINESS_HANDOFF.md`
- `C:\Users\waltr\Documents\New project\docs\NPC_LIFE_WORK_OPEN_PR_HANDOFF.md`

## Confirmed Reference Repo

- Local path: `C:\Users\waltr\Documents\New project`
- Remote: `https://github.com/walt-raymond-williams/sunny-town-hq.git`
- Local branch observed on `2026-06-18`: `codex/demo-polish-planning`
- Note: The Sunny Town HQ worktree had untracked/local files when inspected. Do not modify that repo for this task.

## Expected Output

- A concise comparison of this repo's workflow versus Sunny Town HQ.
- Updates to `docs/current/GITHUB_ISSUE_WORKFLOW.md`, `AGENTS.md`, and related docs if Sunny Town patterns should be adopted.
- A recommendation on tracking docs: exact Sunny Town style versus this repo's `ROADMAP.md` plus `docs/handoffs/` layout.
- Roadmap and task-board updates.

## Files And Areas

Likely files to read or edit in this repo:

- `AGENTS.md`
- `docs/current/GITHUB_ISSUE_WORKFLOW.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/WORKSPACE.md`
- `docs/current/DOCUMENTATION_WORKFLOW.md`

Likely files to inspect only in Sunny Town HQ:

- `AGENTS.md`
- `docs/GITHUB_TRACKING_DOC_GUIDE.md`
- `docs/EPIC_BACKLOG.md`
- `docs/*PR*_HANDOFF.md`
- `docs/*TRACKING.md`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
git -C 'C:\Users\waltr\Documents\New project' status --short --branch
git -C 'C:\Users\waltr\Documents\New project' remote -v
rg "Feature Integration Branch|PR readiness|tracking doc|human review|integration branch" 'C:\Users\waltr\Documents\New project\AGENTS.md' 'C:\Users\waltr\Documents\New project\docs'
```

## Constraints

- Do not modify Sunny Town HQ.
- Preserve the rule that agents may work freely on feature integration branches, but a human gates the final merge into `master`.
- Keep durable workflow decisions in `docs/current/`, not only in this handoff.
- Commit completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- This repo can find the Sunny Town HQ reference by local path and GitHub remote.
- The comparison task is represented in `ROADMAP.md`, `TASKS.md`, a GitHub issue, and this handoff.
- The human-gated final merge rule is captured in durable workflow docs.
- Follow-on workflow changes are either implemented or explicitly listed.

## Open Questions

- Should this repo add feature-specific tracking docs in addition to `ROADMAP.md`?
- Should epics become first-class GitHub issues here before the generic/MegaMek split?
