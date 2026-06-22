# Agent Handoff

## Issue

- GitHub issue: `#33`
- Roadmap entry: `Decide production ownership path for MekHQ checkpoint exporter`
- Priority: `Medium`

## Goal

After the workspace prototype is hardened against MEK-RPG feedback, decide whether the read-only checkpoint exporter should move into MekHQ source, remain a workspace jar-backed helper, or be treated as an experimental bridge artifact.

## Result

Completed on `2026-06-22`.

- Added `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORTER_OWNERSHIP_DECISION.md`.
- Recommendation: keep the jar-backed checkpoint exporter as a workspace experimental helper for near-term use.
- Deferred MekHQ source movement to a future source-change issue only after a clear trigger: regular real-campaign exports, MEK-RPG production dependency, upstream/source-maintainer intent, or unblocked source build/test verification.
- Updated checkpoint export/prototype docs, roadmap, and task board.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT_SCHEMA.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORTER_PROTOTYPE.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EPIC_REVIEW.md`
- Results from `Harden jar-backed MekHQ checkpoint exporter output against MEK-RPG feedback`

## Expected Output

- A short decision note in `docs/current/` or an update to the checkpoint export docs.
- Recommendation among:
  - move exporter into MekHQ source
  - keep workspace helper as experimental/tooling
  - defer production ownership until more real campaign use exists
- If source movement is recommended, create or propose a follow-up source-change issue with explicit build/test blockers.

## Files And Areas

Likely files to read or edit:

- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORTER_PROTOTYPE.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- possibly a new focused decision note under `docs/current/`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
git -C external/src/mekhq status --short --branch
rg -n "CampaignFactory|CampaignXmlParser|CommandLine|export" external/src/mekhq external/src/megamek
git diff --check
```

## Constraints

- This issue is a decision/planning issue, not the source implementation itself.
- Do not modify MekHQ source unless a separate source-change issue is opened or explicitly in scope.
- Preserve read-only/write-side boundaries.
- Record current Java/Gradle verification blockers if they still apply.
- Commit and push completed docs before closing the issue.

## Acceptance Criteria

- Production ownership recommendation is recorded with tradeoffs.
- Any follow-up source issue is clearly scoped or explicitly deferred.
- Roadmap/task state is updated.
- Changes are committed and pushed.

## Open Questions

- Is the hardened workspace helper sufficient for the user and MEK-RPG near term?
- Would MekHQ upstream accept a read-only JSON checkpoint exporter, and what test surface would it require?
