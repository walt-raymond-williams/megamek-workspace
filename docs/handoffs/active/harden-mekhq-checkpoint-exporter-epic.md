# Agent Handoff

## Issue

- GitHub issue: `#30`
- Roadmap entry: `Epic: Harden MekHQ checkpoint exporter after MEK-RPG adapter feedback`
- Priority: `Medium`

## Goal

Track the producer-side MegaMek follow-up after MEK-RPG completed adapter experiments and consumed-field feedback for the read-only MekHQ checkpoint export.

This is an epic. Do not implement directly from this handoff. Work through the child issues linked from the roadmap and GitHub issue.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_CHECKPOINT_EXPORT_REVIEW_MEMO.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT_SCHEMA.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORTER_PROTOTYPE.md`

MEK-RPG feedback source docs:

- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_CHECKPOINT_CROSS_BOARD_TRACKING_PROPOSAL.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_CHECKPOINT_CONSUMED_FIELD_MAPPING.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_CHECKPOINT_WARNING_SURFACING.md`

## Expected Output

- Child issues closed or explicitly superseded.
- MegaMek-side checkpoint schema/export docs reflect completed MEK-RPG feedback, not pending feedback.
- Prototype exporter output is hardened where practical against the consumed-field map.
- Production ownership decision is recorded after hardening, not before.

## Files And Areas

Likely files to read or edit:

- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEK_RPG_CHECKPOINT_EXPORT_REVIEW_MEMO.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT_SCHEMA.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORTER_PROTOTYPE.md`
- `tools/mekhq-checkpoint-exporter/`
- `docs/handoffs/active/`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
gh issue list --repo walt-raymond-williams/megamek-workspace --state open --limit 100
gh issue view <issue> --repo walt-raymond-williams/mek-rpg --comments
```

## Constraints

- Keep the checkpoint queue read-only.
- Do not implement write-side actions, day advancement, market purchases, hiring, repair execution, contract accept/decline, tactical-result application, or save/XML mutation.
- Preserve `evidence`, `source_owner`, `method_backed`, `warnings`, and `unsupported`.
- Keep markets display/opportunity-only until stable source-confirmed selectors exist.
- Do not treat workspace prototype output as a production exporter contract.

## Acceptance Criteria

- Child issues are linked from the epic.
- Roadmap and task docs point to the child handoffs.
- The epic can be closed only after child work is complete and close-out docs are pushed.

## Open Questions

- After hardening, should a production exporter live in MekHQ source, this workspace as a helper, or another reviewed owner?
- Does the hardened prototype expose enough method-backed fields for MEK-RPG's current needs without source-level changes?
