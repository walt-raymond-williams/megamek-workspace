# GitHub Issue Workflow

Use this workflow when turning roadmap items into GitHub Issues for execution, discussion, or agent handoff.

## Operating Model

- `docs/current/ROADMAP.md` is the durable planning source.
- GitHub Issues are trackable execution units created from roadmap entries.
- `docs/current/TASKS.md` tracks immediate local work and should point back to the roadmap when planning details live there.
- Generic workflow patterns belong in `docs/current/AI_READY_PROJECT_WORKFLOW.md`; project-specific execution assumptions belong in the active project profile.
- Handoff documents give agents enough context to work from an issue without rediscovering the whole repo.
- Active handoff documents live in `docs/handoffs/active/`.
- Completed handoff documents move to `docs/handoffs/archive/` after the issue is done, committed, and any merge or close-out work is complete.
- Feature tracking documents are optional compact state snapshots for multi-issue branches; create them only when the roadmap plus handoffs are not enough.

## Comparison With Sunny Town HQ

`Confirmed locally`: Sunny Town HQ at `C:\Users\waltr\Documents\New project` uses GitHub Issues as execution state, Markdown docs for durable planning, feature integration branches for multi-ticket slices, per-issue handoffs, PR-readiness review issues, PR-opening handoffs, and human-gated merges to `main`.

Adopt these patterns here:

- Use epic issues for broad outcomes, then split them into discovery, roadmap, child issues, and handoffs.
- Use feature tracking docs when a workstream has multiple dependent issues, an integration branch, sequential agent handoffs, or a human-gated integrated review.
- Treat PR-readiness review as its own issue for broad branches before opening the final PR.
- Treat PR opening as a small tracked task when a branch has many completed issues and needs a careful PR body.
- Keep GitHub Issues as execution history and `docs/current/` as durable project memory.

Do not copy Sunny Town HQ's exact top-level tracking-doc layout by default. This repo's standard remains `docs/current/ROADMAP.md` plus `docs/handoffs/active/` and `docs/handoffs/archive/`. Add a focused `docs/current/<FEATURE>_TRACKING.md` only when the workstream is large enough to need branch/issue snapshot recovery.

## Epic Issues

Use a GitHub issue with an epic label or clear `Epic:` title for broad outcomes that are too large for one agent implementation pass.

Epic issues should not be assigned as direct coding tasks. Use this sequence:

1. Create or select the epic issue.
2. Create a discovery or planning note under `docs/current/` when the current docs do not already cover the shape of the work.
3. Audit current docs, source, and local constraints.
4. Split the epic into child issues with acceptance criteria, implementation notes, dependencies, blockers, and verification.
5. Create a feature integration branch if the child issues must land together before `master`.
6. Create a feature tracking doc if the branch needs compact state recovery.
7. Move durable decisions into the appropriate `docs/current/` file before closing the epic.

## Handoff Lifecycle

Use one handoff document per agent-executed GitHub issue.

1. Create the handoff from `docs/templates/AGENT_HANDOFF.md` under `docs/handoffs/active/`.
2. Link the handoff path from the GitHub issue body.
3. Link the GitHub issue number from the handoff.
4. Keep architectural or durable process findings in `docs/current/`, not only in the handoff.
5. When the issue is complete, move the handoff to `docs/handoffs/archive/`.
6. Update `ROADMAP.md`, `TASKS.md`, and any affected current docs before committing the close-out change.
7. Push the close-out commit to GitHub before closing the issue or telling the user the task is complete.

Handoffs are execution context. `docs/current/` remains the live source for durable architecture, workflow, source, and campaign knowledge.

## Feature Integration Branches

For broad multi-ticket features or redesigns, use a feature integration branch instead of merging partial work directly into `master`.

- Name feature integration branches with the `codex/` prefix, for example `codex/ai-workflow-demo-dev`.
- Agents may complete individual GitHub issues on the integration branch or on smaller task branches that merge into the integration branch.
- Close a GitHub issue when its work has landed in the feature integration branch and verification has been recorded.
- Keep `master` stable until the feature slice is coherent and ready for human review.
- Before merging an integration branch into `master`, a human should review the feature end to end, run or accept the agreed verification, and confirm `docs/current/` is accurate.
- The final PR from the integration branch to `master` should reference completed issues with `Refs #123` or `Closes #123` as appropriate.

This pattern is based on the confirmed local Sunny Town HQ workflow in `C:\Users\waltr\Documents\New project`.

## Feature Tracking Docs

Create a feature tracking doc when work has any of these traits:

- multiple dependent GitHub issues
- a feature integration branch
- sequential agent handoffs
- important product, workflow, or architecture context that must survive compaction
- a completed slice that should not merge to `master` until human review

Recommended path:

```text
docs/current/<FEATURE_NAME>_TRACKING.md
```

Use this compact shape:

```markdown
# <Feature Name> Tracking

## Purpose

GitHub Issues are the execution source of truth. This file is the compact local recovery snapshot for branch state, issue state, next management step, and handoff paths.

## Integration Branch

- Branch: `codex/<feature-name>-dev`
- Base: `master`
- Human review required before merge: `Yes`

## Issue Snapshot

- Last refreshed: `<YYYY-MM-DD>`
- Closed:
- Open:
- Blocked:

## Recommended Next Step

- Issue:
- Why next:
- Handoff:

## Verification State

- Commands passed:
- Manual checks:
- Known blockers:

## Related Docs

- `docs/current/ROADMAP.md`
- `docs/handoffs/active/<handoff>.md`
```

Keep tracking docs short. They are management state, not a replacement for roadmap entries, issue bodies, handoffs, or durable current-state docs.

## PR Readiness And Open PR Handoffs

Use a PR-readiness issue before opening a final PR when an integration branch contains multiple completed issues, meaningful documentation changes, or behavior that needs end-to-end human review.

A PR-readiness issue should answer:

- Is the integrated branch correct enough to open a PR?
- Are there blocking bugs or documentation gaps?
- Are completed handoffs or planning notes misleading compared with the implemented state?
- What verification ran, and what remains manual or blocked?
- Is the recommendation `ready to open PR`, `needs fixes`, or `split/follow-up first`?

Use an open-PR issue or handoff when the PR body needs careful assembly from many child issues. The PR body should include:

- summary of the integrated slice
- `Closes #...` for completed child issues and `Refs #...` for related epics
- verification commands and manual smoke results
- known non-blocking follow-ups or blockers
- a clear request for human review before merge to `master`

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
4. Push the commit to GitHub so the issue and repository state match.

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
5. Update the feature tracking doc if the issue belongs to a tracked multi-issue branch.
6. Move completed local work in `TASKS.md` to `Done`.
7. Commit documentation updates.
8. Push the commit to the tracked GitHub branch.
9. Confirm `git status --short --branch` does not show the branch ahead of upstream.
10. Close or comment on the GitHub issue with the pushed commit hash and verification results.

Do not close a GitHub issue based only on a local commit. A task is not fully closed out until the relevant commit has been pushed and GitHub has the close-out note, issue state, or PR state that references it.

## Current Local State

- `Confirmed locally`: GitHub CLI exists at `C:\Program Files\GitHub CLI\gh.exe`.
- `Confirmed locally`: GitHub CLI is authenticated as `walt-raymond-williams`.
- `Confirmed locally`: `origin` points to `https://github.com/walt-raymond-williams/megamek-workspace.git`.
- `Confirmed locally`: `master` tracks `origin/master`.
- `Confirmed locally`: GitHub label `agent-task` exists.
- `Confirmed locally`: Active agent issues `#1` through `#4` were created from `ROADMAP.md` on `2026-06-18`.
- `Confirmed locally`: Sunny Town HQ reference repo exists at `C:\Users\waltr\Documents\New project`, remote `https://github.com/walt-raymond-williams/sunny-town-hq.git`.
- `Confirmed locally`: Issue `#5` completed the workflow comparison against Sunny Town HQ on `2026-06-18`; the adopted patterns are recorded above.
