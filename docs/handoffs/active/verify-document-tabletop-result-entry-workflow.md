# Agent Handoff: Verify And Document Tabletop Result Entry Workflow

## Issue

- GitHub issue: `#13`
- Roadmap entry: `Epic: Robust tabletop battle result MUL workflow`
- Priority: `High`

## Goal

Verify the end-to-end user workflow for generating, validating, and importing tabletop battle results into MekHQ, then document it for future campaign use.

Current status on `2026-06-22`: blocked. Source and installed-jar validation exist, but this issue requires live/user-operated MekHQ Resolve Manually import observations before it can be completed.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEGAMEK_PROJECT_PROFILE.md`
- `docs/current/KNOWN_COMMANDS.md`
- `docs/handoffs/active/robust-tabletop-battle-result-mul-workflow.md`
- `docs/handoffs/archive/implement-tabletop-battle-record-mul-generator.md`
- `docs/handoffs/active/prototype-battle-record-mul-round-trip.md`

## Expected Output

- User-facing workflow documentation under `docs/current/` or `docs/templates/`.
- Manual verification notes for MekHQ Resolve Manually, including precise UI paths or screenshots if useful.
- Updated `TASKS.md`, `ROADMAP.md`, active handoffs, and any feature tracking doc to reflect what remains.

## Current Blocker

- Issue `#10` has not completed the live MekHQ Resolve Manually import pass.
- Computer Use/UI automation remains unavailable in this environment.
- User task `#23` should create the realistic campaign target before destructive/import validation resumes.
- Without exact UI/manual observations, any workflow doc would be source-guided but not verified enough to satisfy this issue.

## Files And Areas

- `docs/current/`
- `docs/templates/`
- `analysis/`
- `external/installs/MekHQ-0.51.00`

## Commands

```powershell
git status --short --branch
Test-Path 'C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\MekHQ.exe'
```

## Constraints

- Do not use active campaign saves for destructive verification.
- Record exact MekHQ version/install path and input files used.
- Keep player-facing instructions concise and practical.

## Acceptance Criteria

- A future agent or user can follow the documented workflow from scenario export to result import.
- Verification records include exact date, MekHQ version/install path, input files used, and observed result.
- Known limitations and follow-up issues are explicit.

## Open Questions

- Should the final workflow live as a current-state guide, a reusable template, or both?
- Which screenshots or UI checks are worth preserving?
