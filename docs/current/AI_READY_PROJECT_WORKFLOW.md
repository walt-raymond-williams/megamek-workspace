# AI-Ready Project Workflow

This is the reusable workflow pattern this public repository is demonstrating. It is intended for projects where agents need durable context, safe execution rules, source-grounded investigation, and clear handoffs across sessions.

This repo is the canonical artifact. Do not split work into a separate template repository unless the user explicitly asks; improve the public working repo directly.

## Core Pattern

An AI-ready project should keep four kinds of memory distinct:

- `Agent instructions`: how agents should behave, what they may touch, and what must be verified before close-out.
- `Current knowledge`: durable project facts that should survive chat history loss.
- `Execution state`: the active task board, GitHub Issues, handoff documents, branches, and PR state.
- `Domain profile`: local paths, data safety rules, domain concepts, source locations, and interpretation rules for the specific project.

The reusable workflow belongs in `docs/current/` and templates. Domain-specific assumptions belong in a project profile, not scattered through generic process docs.

## Recommended Repository Shape

Use this shape as the default for a new AI-ready project:

```text
AGENTS.md
README.md
docs/
  current/
    AI_READY_PROJECT_WORKFLOW.md
    DOCUMENTATION_WORKFLOW.md
    GITHUB_ISSUE_WORKFLOW.md
    ROADMAP.md
    TASKS.md
    <PROJECT>_PROJECT_PROFILE.md
  handoffs/
    active/
    archive/
  templates/
analysis/
```

Project-specific folders can be added for inputs, fixtures, generated reports, source checkouts, or local payloads. The project profile should explain those folders and identify which ones are ignored, precious, or safe to regenerate.

## Project Profile Contract

Each project profile should answer:

- What is this workspace for?
- What inputs or local data must be protected?
- Where do source checkouts, installs, generated files, and reports live?
- What domain concepts must agents understand before giving confident advice?
- When should agents inspect source instead of relying on memory?
- What commands verify the local environment, tests, or generated outputs?
- Which docs own durable findings for this domain?

The profile should be specific enough for an agent to start safely, but not duplicate every generic workflow rule.

## Work Intake

Use `ROADMAP.md` for durable planning and sequencing. Use GitHub Issues for executable work when a task is ready for assignment, outside discussion, or agent handoff.

Use `TASKS.md` as the local task board:

- `Now`: work currently in progress.
- `Next`: ready near-term queue.
- `Backlog`: useful but not ready or not urgent.
- `Blocked`: work waiting on a specific condition.
- `Done`: recent completions worth preserving for continuity.

Before starting a queued task, move it into `Now`. When it completes, move it into `Done` and preserve relevant issue, commit, and verification details.

## Handoffs

Use one handoff document per agent-executed issue:

- Active handoffs live in `docs/handoffs/active/`.
- Completed handoffs move to `docs/handoffs/archive/`.
- Handoffs contain execution context, not durable architecture or domain facts.

If a finding will matter after the issue closes, move it into the narrowest relevant `docs/current/` file before archiving the handoff.

## Branches And PRs

Small documentation or investigation tasks can land directly on the stable branch when the repository workflow allows it.

Use a feature integration branch when a workstream has multiple dependent issues, needs sequential handoffs, or should be reviewed as an integrated slice before merging to the stable branch. Keep branch state in an optional `docs/current/<FEATURE>_TRACKING.md` only when `ROADMAP.md` plus handoffs are not enough.

Before opening a final PR for a broad integration branch, use a PR-readiness issue when the branch includes multiple completed issues or meaningful workflow/product changes.

## Close-Out

When repository changes are made:

1. Run the relevant verification or record the blocker.
2. Update durable docs and task state.
3. Stage only files that belong to the completed work.
4. Commit the coherent change.
5. Push the commit to GitHub unless the user explicitly said not to push.
6. Confirm the branch is not ahead of upstream.
7. Close or comment on the GitHub issue with the pushed commit and verification result.

Do not call a task complete if the required close-out commit exists only locally.

## Adaptation Checklist

When using this workflow as a reference for another project:

1. Write a project profile before doing risky work.
2. Identify protected local inputs and ignored payloads.
3. Create or update `ROADMAP.md`, `TASKS.md`, and `DOCUMENTATION_WORKFLOW.md`.
4. Add handoff and report templates that match the domain.
5. Confirm GitHub remote, issue labels, branch naming, and push rules.
6. Record repeatable commands before relying on them.
7. Keep generic process docs free of domain assumptions.
