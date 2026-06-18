# Agent Handoff

## Issue

- GitHub issue: `#20`
- Roadmap entry: `Epic: Control MekHQ player and OPFOR mech rosters`
- Priority: `High`

## Goal

Define a compact physical-miniature roster data model that can drive player roster choices, OPFOR substitution, fixed MUL pools, and possible custom RAT generation.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MECH_ROSTER_CONTROL_TRACKING.md`
- `docs/current/MECH_ROSTER_CONTROL_WORKFLOWS.md`
- `docs/current/MEGAMEK_PROJECT_PROFILE.md`

## Expected Output

- A durable schema/design note under `docs/current/`.
- A recommended file format for the user's miniature list.
- A small example roster with placeholder data if the real list is not yet available.

## Files And Areas

Likely files to read or edit:

- `docs/current/MECH_ROSTER_CONTROL_WORKFLOWS.md`
- `docs/current/MECH_ROSTER_CONTROL_TRACKING.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- possibly `docs/templates/`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
```

## Constraints

- Do not invent the user's actual miniature inventory.
- Keep the model useful for both exact variants and loose proxy/substitution play.
- Commit completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- The model captures chassis, variant, quantity, unit type, weight, era/faction notes, BV source if known, and legal-use flags for player/OPFOR/either.
- The model distinguishes confirmed inventory from placeholder/example data.
- The design explains how it would feed fixed OPFOR MULs or custom RATs later.

## Open Questions

- Does the user want exact variants only, or are close visual proxies acceptable?
- Should painted faction/force ownership matter for generation, or only chassis/variant availability?
