# Agent Handoff

## Issue

- GitHub issue: `#31`
- Roadmap entry: `Reconcile MegaMek checkpoint docs with completed MEK-RPG feedback`
- Priority: `Medium`

## Goal

Update MegaMek-side checkpoint documentation now that MEK-RPG issues `#84` through `#89` are complete, replacing "waiting on MEK-RPG" language with concrete consumed-field, warning-surfacing, and edge-fixture feedback.

## Result

Completed on `2026-06-22`.

- Updated MegaMek checkpoint review/schema/export docs with completed MEK-RPG feedback from issues `#84` through `#89`.
- Recorded producer-side hardening requirements: preserve top-level grouping and trust-boundary fields, replace object-string location values with stable display/id fields, deepen active contract terms through `Contract` getters, keep market offers display-only, and keep `unsupported` mandatory.
- Updated `ROADMAP.md` and `TASKS.md`; follow-on implementation remains issue `#32`.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_CHECKPOINT_EXPORT_REVIEW_MEMO.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT_SCHEMA.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EPIC_REVIEW.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_CHECKPOINT_CROSS_BOARD_TRACKING_PROPOSAL.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_CHECKPOINT_CONSUMED_FIELD_MAPPING.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_CHECKPOINT_WARNING_SURFACING.md`

## Expected Output

- MegaMek docs summarize completed MEK-RPG feedback accurately.
- Schema docs identify hardening requirements:
  - keep current top-level grouping
  - preserve trust-boundary fields
  - replace object-string prototype location values with stable display/id fields
  - deepen active contract terms through `Contract` getters
  - keep markets display-only
  - keep `unsupported` mandatory
- Roadmap/task state references the completed MEK-RPG issues and the follow-on hardening issue.

## Files And Areas

Likely files to read or edit:

- `docs/current/MEK_RPG_CHECKPOINT_EXPORT_REVIEW_MEMO.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT_SCHEMA.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EPIC_REVIEW.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
gh issue view 84 --repo walt-raymond-williams/mek-rpg --comments
gh issue view 87 --repo walt-raymond-williams/mek-rpg --comments
gh issue view 88 --repo walt-raymond-williams/mek-rpg --comments
git diff --check
```

## Constraints

- Documentation-only unless a tiny metadata/test command is needed.
- Do not modify unrelated active handoff changes.
- Preserve evidence labels and identify user-confirmed versus source-confirmed facts.
- Commit and push completed docs before closing the issue.

## Acceptance Criteria

- MEK-RPG feedback is durable in MegaMek `docs/current/`.
- No "waiting on MEK-RPG #85/#86/#87" language remains where the issues are now complete, except historical notes.
- Future implementation work is clearly handed to the exporter hardening issue.
- Changes are committed and pushed.

## Open Questions

- Should any MEK-RPG feedback be copied verbatim, or should MegaMek docs only summarize and link to the MEK-RPG docs?
