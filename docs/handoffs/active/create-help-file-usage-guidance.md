# Agent Handoff: Create Help-File Usage Guidance

## Issue

- GitHub issue: `#4`
- Roadmap entry: `Create help-file usage guidance`
- Priority: `Medium`

## Goal

Decide how agents should use local MegaMek/MekHQ help files, glossary resources, installed docs, and source code when answering campaign, scenario, rules, workflow, or behavior questions.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/HELP_FILE_USAGE_GUIDANCE_STATE.md`
- `docs/current/DOCUMENTATION_WORKFLOW.md`
- `docs/current/SOURCE_CODE_GUIDE.md`

## Expected Output

- A durable guidance document under `docs/current/`.
- Updated links from `docs/current/WORKSPACE.md` and any other relevant current docs.
- Clear priority order for save data, local docs/help files, source code, and external sources.
- Explicit copyright posture for rules/help references.

## Files And Areas

Likely files to read or edit:

- `docs/current/HELP_FILE_USAGE_GUIDANCE_STATE.md`
- `docs/current/WORKSPACE.md`
- `docs/current/DOCUMENTATION_WORKFLOW.md`
- `external/installs/MekHQ-0.51.00/docs`
- `external/src/mekhq/MekHQ/docs`
- `external/src/mekhq/MekHQ/resources/mekhq/resources/GlossaryEntry.properties`
- `external/src/megamek/megamek/docs`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg "Glossary|docs|help" external/src/mekhq external/src/megamek
```

## Constraints

- Do not reproduce large copyrighted rulebook or help passages.
- Summarize mechanics and cite local docs/source context.
- Keep durable guidance in `docs/current/`; leave raw extraction scratch in ignored analysis paths.
- Commit completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- Future agents know when to consult local help files versus source code.
- Useful local help/doc paths are recorded.
- The guidance distinguishes confirmed local docs, confirmed source behavior, inferred mechanics, and unknowns.
- `ROADMAP.md` and `TASKS.md` reflect completion or blockers.

## Open Questions

- Should this become a reusable project-profile pattern after the generic workflow split?
