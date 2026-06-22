# Help File Usage Guidance State

## Status

Superseded on `2026-06-22` by `HELP_FILE_WORKFLOW.md`.

This file is retained as the research stub that fed GitHub issue `#4`. Future agents should use `HELP_FILE_WORKFLOW.md` for active guidance.

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

## PDF Tooling Status

`Confirmed locally`: `pdftotext` is now available through Poppler, and Python `pypdf` is importable in the default shell.

`Confirmed locally`: `pdfplumber` and `PyPDF2` were not available in the default shell on `2026-06-22`.

Implication: `.md`, `.txt`, `.html`, `.properties`, and `.java` files can be searched immediately with `rg`; PDFs can be extracted to ignored scratch text with `pdftotext` when needed.

## Guidance Completed

Completed in `HELP_FILE_WORKFLOW.md`:

- When agents should consult local docs before source code.
- When source code should override or verify local docs.
- How to handle BattleTech rulebook-adjacent content without reproducing copyrighted passages.
- How to cite evidence labels: `Confirmed from local docs`, `Confirmed from source`, `Inferred`, and `Unknown`.
- Repeatable commands in `KNOWN_COMMANDS.md` for searching local docs/glossary resources and extracting PDFs.
