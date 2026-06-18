# Agent Handoff: Identify MekHQ Save/Load Source Classes

## Issue

- GitHub issue: `#3`
- Roadmap entry: `Identify MekHQ save/load source classes`
- Priority: `Medium`

## Goal

Confirm how `.cpnx.gz` campaign files are loaded and saved in the local MekHQ source checkout.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CODE_GUIDE.md`
- `docs/current/SAVE_FORMAT_NOTES.md`
- `docs/current/KNOWN_COMMANDS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`

## Expected Output

- Updated `docs/current/SAVE_FORMAT_NOTES.md` with source-backed loading/saving notes.
- Updated `docs/current/SOURCE_CODE_GUIDE.md` with relevant classes, packages, or search entry points.
- Updated `docs/current/KNOWN_COMMANDS.md` if useful source search commands are confirmed.
- Roadmap/task updates for any downstream parser or source-inspection work.

## Files And Areas

Likely files to read or edit:

- `external/src/mekhq`
- `docs/current/SAVE_FORMAT_NOTES.md`
- `docs/current/SOURCE_CODE_GUIDE.md`
- `docs/current/KNOWN_COMMANDS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
Test-Path external/src/mekhq
rg "cpnx|CampaignXml|load.*campaign|save.*campaign" external/src/mekhq
```

## Constraints

- This issue is source inspection, not source modification.
- Cite source files/classes for behavior claims.
- Do not edit source checkouts unless the issue scope changes explicitly.
- Commit completed workspace documentation changes before stopping unless explicitly told not to.

## Acceptance Criteria

- The load/save path for `.cpnx.gz` files is identified with file/class references.
- Any compression/XML handling that affects parsing is documented.
- Remaining unknowns are explicit and assigned to follow-on work if needed.
- `ROADMAP.md` and `TASKS.md` reflect completion or blockers.

## Open Questions

- Should source checkout paths remain fixed under `external/src`, or should they become configurable through a project profile?
