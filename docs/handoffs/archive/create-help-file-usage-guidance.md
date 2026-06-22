# Agent Handoff: Create Help-File Usage Guidance

## Issue

- GitHub issue: `#4`
- Roadmap entry: `Create help-file usage guidance`
- Priority: `Medium`

## Goal

Decide how agents should use local MegaMek/MekHQ help files, glossary resources, installed docs, and source code when answering campaign, scenario, rules, workflow, or behavior questions.

## Result

Completed on `2026-06-22`.

Added `docs/current/HELP_FILE_WORKFLOW.md`, which tells future agents to route evidence in this order unless the user asks otherwise:

1. campaign/save/scenario data for current campaign facts
2. local installed docs, source docs, in-app glossary resources, and help files for player-facing concepts and intended behavior
3. local source code for exact implementation behavior, file formats, automation paths, calculations, and UI triggers
4. user confirmation for table rules, optional systems, physical-miniature constraints, and live UI results
5. external or official sources when local evidence cannot answer the question

The workflow records useful local paths, including installed docs, MekHQ and MegaMek source docs, `GlossaryEntry.properties`, `DocumentationEntry.properties`, StratCon docs, and `mm-help.txt.html`. It also records source-confirmed in-app glossary/documentation owners: `GlossaryEntry`, `DocumentationEntry`, `GlossaryDialog`, and the Auto Resolve help path.

Updated links and routing notes in:

- `docs/current/WORKSPACE.md`
- `docs/current/MEGAMEK_PROJECT_PROFILE.md`
- `docs/current/DOCUMENTATION_WORKFLOW.md`
- `docs/current/CAMPAIGN_ANALYSIS_WORKFLOW.md`
- `docs/current/KNOWN_COMMANDS.md`
- `docs/current/HELP_FILE_USAGE_GUIDANCE_STATE.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`

## Verification

- Confirmed installed and source-side doc trees exist.
- Confirmed in-app documentation index and glossary source classes.
- Confirmed `pdftotext` is available through Poppler.
- Confirmed Python `pypdf` is importable; `pdfplumber` and `PyPDF2` are not available in the default shell.
- Ran `git diff --check` before commit.

## Remaining Notes

The open question of whether this pattern should become a reusable project-profile template is deferred to generic workflow evolution. The MegaMek/MekHQ-specific guidance is complete for current issue close-out.
