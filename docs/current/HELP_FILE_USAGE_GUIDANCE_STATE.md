# Help File Usage Guidance State

## Status

Paused on `2026-06-18` so the workspace can prioritize migrating work tracking to GitHub Issues/Projects.

## Goal

Create durable guidance for future agents on when and how to use local MegaMek/MekHQ help files, in-app glossary resources, installed documentation, and source code when answering campaign, scenario, rules, workflow, or source-behavior questions.

The intended output is either a focused workflow note under `docs/current/` or a skill-style guide that agents can follow before answering MekHQ questions.

## Research Already Captured

`Confirmed from local docs/source`: useful documentation exists in several local locations:

- Installed suite documentation: `external/installs/MekHQ-0.51.00/docs`
- MekHQ source documentation: `external/src/mekhq/MekHQ/docs`
- MegaMek source documentation: `external/src/megamek/megamek/docs`
- MekHQ glossary text resources: `external/src/mekhq/MekHQ/resources/mekhq/resources/GlossaryEntry.properties`
- MekHQ in-app documentation index: `external/src/mekhq/MekHQ/src/mekhq/campaign/utilities/glossary/DocumentationEntry.java`
- MekHQ in-app glossary enum: `external/src/mekhq/MekHQ/src/mekhq/campaign/utilities/glossary/GlossaryEntry.java`

`Confirmed from source`: MekHQ has an in-app Glossary/Documentation dialog in `external/src/mekhq/MekHQ/src/mekhq/gui/dialog/glossary/GlossaryDialog.java`.

`Confirmed from source`: `DocumentationEntry.java` is the canonical in-app index for documentation PDFs. It maps entries such as ACAR, Admin Skills, Combat Teams, Faction Standings, New Player Guide, Personnel Modules, Resupply & Convoys, and Unit Markets to files under `docs/GlossaryDocs`.

`Confirmed from source`: Auto Resolve help is wired through `AutoResolveBehaviorSettingsHelpDialog.java` and `AutoResolveBehaviorSettingsDialog.properties` to `docs/help/en/AutoResolve.html`.

## High-Value Sources

Recommended ingestion/search priority:

1. `GlossaryEntry.properties`
   - Directly searchable text.
   - Explains many MekHQ concepts such as StratCon, contracts, fatigue, support points, deployment, reinforcement, maintenance, prisoners, resupply, force generation, and mission types.

2. `DocumentationEntry.java` plus referenced PDFs under `external/src/mekhq/MekHQ/docs/GlossaryDocs`
   - Best canonical map of MekHQ's in-app documentation set.
   - PDFs are valuable but require text extraction tooling before agents can search them efficiently.

3. Installed `docs/StratCon`
   - `stratcon-faq-2.6.md` and `Stratcon_Tips.md` are immediately useful plain-text guidance for campaign play.

4. Installed `docs/mm-help.txt.html`
   - Useful for MegaMek battle flow, hosting, lobby setup, maps, movement, firing, and game phases.

5. Scenario, RAT, and force-generator docs
   - Useful when answering questions about `.mms` files, random assignment tables, generated OpFors, or force construction.

## Known Tooling Gap

`Confirmed locally`: `pdftotext`, `pdfinfo`, `pypdf`, `pdfplumber`, and `PyPDF2` were not available during the initial research pass.

Implication: `.md`, `.txt`, `.html`, `.properties`, and `.java` files can be searched immediately with `rg`, but PDF ingestion needs a converter, dependency, or other extraction path.

## Guidance Still Needed

When resumed, decide and document:

- When agents should consult local docs before source code.
- When source code should override or verify local docs.
- How to handle BattleTech rulebook-adjacent content without reproducing copyrighted passages.
- How to cite evidence labels: `Confirmed from local docs`, `Confirmed from source`, `Inferred`, and `Unknown`.
- Whether this belongs in a new `docs/current/HELP_FILE_WORKFLOW.md`, an update to `SOURCE_CODE_GUIDE.md`, an update to `CAMPAIGN_ANALYSIS_WORKFLOW.md`, or a formal Codex skill.
- Whether to add repeatable commands to `KNOWN_COMMANDS.md` for searching glossary/docs locations.

## Suggested Resume Plan

1. Re-read `TASKS.md` and this state note.
2. Inspect `GlossaryEntry.properties`, `DocumentationEntry.java`, and `docs/StratCon` again for final examples.
3. Draft a workflow that tells agents:
   - Start with campaign save data for campaign-specific facts.
   - Use local help/glossary docs for user-facing concepts and UI guidance.
   - Use source code for implementation behavior, file formats, automation, and mechanics where exact behavior matters.
   - Preserve uncertainty when docs/source/save data disagree.
4. Add links from `WORKSPACE.md`, `SOURCE_CODE_GUIDE.md`, or `CAMPAIGN_ANALYSIS_WORKFLOW.md` only if the final workflow should be part of normal agent routing.
