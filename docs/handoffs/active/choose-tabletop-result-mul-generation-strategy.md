# Agent Handoff: Choose Tabletop Result MUL Generation Strategy

## Issue

- GitHub issue: `#11`
- Roadmap entry: `Epic: Robust tabletop battle result MUL workflow`
- Priority: `High`

## Goal

Decide whether custom battle-record MUL generation is needed at all. Compare MekHQ's built-in manual result-entry/import workflow against custom options such as a standalone Java helper using MegaMek/MekHQ classes, a MekHQ source feature, a workspace script invoking installed jars, or another safe approach.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/GITHUB_ISSUE_WORKFLOW.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/KNOWN_COMMANDS.md`
- `docs/handoffs/active/robust-tabletop-battle-result-mul-workflow.md`
- `docs/handoffs/active/prototype-battle-record-mul-round-trip.md`

## Expected Output

- A short architecture decision note under `docs/current/` comparing viable generation approaches.
- Recommendation for the first path: built-in MekHQ workflow plus documentation, lightweight helper/manual-edit workflow, or custom generator implementation.
- List of commands, dependencies, and validation hooks needed for implementation.
- Decision on whether issue `#12` should proceed, be narrowed, or be closed as unnecessary.
- Decision on whether to create `codex/tabletop-result-mul-dev` and a feature tracking doc before any implementation begins.

## Files And Areas

- `docs/current/`
- `docs/current/KNOWN_COMMANDS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/GITHUB_ISSUE_WORKFLOW.md`
- `external/src/`
- `external/installs/MekHQ-0.51.00`

## Commands

```powershell
git status --short --branch
java -version
javac -version
Test-Path 'C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\MekHQ.exe'
```

## Constraints

- Do not start implementation until the strategy is recorded.
- Do not modify MegaMek/MekHQ source without following `SOURCE_CHANGE_WORKFLOW.md`.
- Be explicit about the current Java/Gradle blocker.

## Acceptance Criteria

- The recommendation identifies where code should live and what it should call.
- The recommendation explains how generated MULs will be validated before MekHQ import.
- The recommendation explicitly evaluates the no-custom-code path using MekHQ's built-in workflow.
- The issue leaves implementation-ready next steps and avoids making unverified source claims.

## Open Questions

- Is the installed MekHQ suite sufficient for generation and validation?
- Is MekHQ's built-in manual workflow sufficient with better documentation and operator guidance?
- Should implementation happen on a feature integration branch?
