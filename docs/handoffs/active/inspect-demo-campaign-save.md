# Agent Handoff: Inspect Demo Campaign Save

## Issue

- GitHub issue: `#2`
- Roadmap entry: `Inspect demo campaign save`
- Priority: `Medium`

## Goal

Extract a factual campaign snapshot from `campaigns/demo/ai-ready-demo.cpnx.gz` without modifying the save.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/ACTIVE_CAMPAIGN.md`
- `docs/current/CAMPAIGN_ANALYSIS_WORKFLOW.md`
- `docs/current/SAVE_FORMAT_NOTES.md`
- `docs/current/DOCUMENTATION_WORKFLOW.md`

## Expected Output

- Updated `docs/current/ACTIVE_CAMPAIGN.md` with confirmed campaign identity and current-state facts.
- Updated `docs/current/SAVE_FORMAT_NOTES.md` with any confirmed structure findings.
- A first demo campaign status report under `campaigns/demo/reports/`.
- Any reusable extraction commands added to `docs/current/KNOWN_COMMANDS.md`.

## Files And Areas

Likely files to read or edit:

- `campaigns/demo/ai-ready-demo.cpnx.gz`
- `docs/current/ACTIVE_CAMPAIGN.md`
- `docs/current/SAVE_FORMAT_NOTES.md`
- `docs/current/KNOWN_COMMANDS.md`
- `campaigns/demo/reports/`
- `analysis/tmp/` or another ignored temporary extraction location.

## Commands

Useful commands or checks:

```powershell
git status --short --branch
Get-ChildItem campaigns/demo
```

## Constraints

- Do not overwrite or move the demo save.
- Work on copies or temporary extracts.
- Separate confirmed save facts from source-backed findings and inferences.
- Commit completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- Campaign date, unit identity, finances, forces, personnel, contracts, repairs, and obvious alerts are summarized where available.
- Unknown or unparsed areas are explicitly marked.
- Report output is stored in the repository only if it is a durable demo artifact.
- `ROADMAP.md` and `TASKS.md` reflect completion or blockers.

## Open Questions

- Should generated extracts live under `analysis/generated/demo/` once the extraction shape stabilizes?
- Which fields should be required in the first campaign status report?
