# Help File Workflow

Use this workflow when answering MekHQ, MegaMek, BattleTech, campaign, scenario, UI, or rules-adjacent questions. The goal is to use the best local evidence first, explain uncertainty plainly, and avoid turning copyrighted or implementation-specific material into unsupported memory claims.

## Source Priority

Use this order unless the user asks for a different kind of answer:

1. Campaign/save/scenario data for facts about the user's current campaign.
2. Local installed docs, source docs, in-app glossary resources, and help files for player-facing concepts, UI guidance, and intended behavior.
3. Local source code for exact implementation behavior, file formats, automation paths, calculations, UI triggers, and anything where docs may be stale or incomplete.
4. User confirmation for table rules, optional systems, house rules, physical-miniature constraints, and live UI results.
5. External or official sources only when local data/docs/source cannot answer the question or when the user explicitly asks for outside confirmation.

If these sources disagree, do not smooth over the conflict. Prefer exact current campaign data for current state, prefer source code for how this local install behaves, and record the disagreement as `Unknown` or `Inferred` until it is resolved.

## What To Use

Use campaign data when the question is about the current save:

- date, unit, commander, faction, location, active contract, scenarios, finance, repairs, personnel, unit status, transport, markets, and alerts
- enabled campaign options and optional systems observed in the save
- exact current values that may differ from generic rules or docs

Use local docs and help files when the question is about concepts or player-facing guidance:

- what a MekHQ feature is meant to do
- glossary-level explanations such as StratCon, fatigue, maintenance, mission types, contracts, resupply, force generation, support points, prisoners, or deployment
- MegaMek battle flow, client settings, maps, movement, firing, hosting, Princess, scenario templates, RATs, force generators, user directories, and customization
- onboarding-style answers for "what does this button/system mean?"

Use source code when the question asks "how does this actually work in this install?":

- save/load fields and file formats
- formulas, generated values, state transitions, and side effects
- repair, maintenance, fatigue, market, contract, salvage, and scenario resolution behavior
- UI action wiring, dialog prompts, import/export paths, automation hooks, and command-line possibilities
- whether a workflow can be automated safely or needs live user operation

Use external sources sparingly:

- official or primary sources for BattleTech rules context when local docs/source are insufficient
- current public project documentation only when the local checkout cannot answer a version-sensitive question
- never paste large rulebook/help passages; summarize and cite the local file, class, or official source context

## Local Help And Doc Map

Installed suite docs:

```text
C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\docs
```

Source-side docs:

```text
C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq\MekHQ\docs
C:\Users\waltr\Documents\megamek-workspace\external\src\megamek\megamek\docs
```

High-value searchable text:

```text
C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq\MekHQ\resources\mekhq\resources\GlossaryEntry.properties
C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq\MekHQ\resources\mekhq\resources\DocumentationEntry.properties
C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq\MekHQ\docs\StratCon\stratcon-faq-2.6.md
C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq\MekHQ\docs\StratCon\Stratcon_Tips.md
C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\docs\mm-help.txt.html
```

In-app glossary and documentation source map:

- `Confirmed from source`: `mekhq.campaign.utilities.glossary.GlossaryEntry` enumerates in-app glossary entries, backed by `GlossaryEntry.properties`.
- `Confirmed from source`: `mekhq.campaign.utilities.glossary.DocumentationEntry` maps in-app documentation entries to `docs/GlossaryDocs/*.pdf`.
- `Confirmed from source`: `mekhq.gui.dialog.glossary.GlossaryDialog` opens the combined glossary/documentation dialog from the campaign UI.
- `Confirmed from source`: `AutoResolveBehaviorSettingsDialog.properties` points Auto Resolve help at `docs/help/en/AutoResolve.html`.

Useful doc areas:

- `docs/GlossaryDocs`: New Player Guide, ACAR, Admin Skills, Combat Teams, Faction Standings, Personnel Modules, Resupply & Convoys, Unit Markets, and related PDFs.
- `docs/StratCon`: immediately searchable StratCon FAQ and tips.
- `docs/help/en`: MegaMek and MekHQ HTML help, including Auto Resolve and MegaMek battle/map/movement help.
- `docs/Customization`: scenario templates, RAT and force generator material, user directory docs, map/editor/customization notes, and MMU examples.
- `docs/MegaMek` and `docs/Rules and Technical Stuff` in the MegaMek source checkout for MegaMek-specific behavior and implementation notes.

## Evidence Labels

Use the evidence labels from `DOCUMENTATION_WORKFLOW.md`:

- `Confirmed from save`: directly observed in a campaign or scenario file.
- `Confirmed from local docs`: verified in installed or source-side help/docs/glossary resources.
- `Confirmed from source`: verified in local MegaMek, MekHQ, MegaMekLab, or mm-data source.
- `Confirmed by user`: stated by the user as campaign fact, table rule, preference, or live UI result.
- `Inferred`: reasonable conclusion from available evidence, not yet confirmed.
- `Unknown`: tracked gap.

When answering the user, cite file/class context when it materially supports the answer. When updating durable docs, include the evidence label and the path or class needed to rediscover the evidence.

## Search And Extraction

Start with text search:

```powershell
rg -n "fatigue|maintenance|contract|StratCon|resupply|force generation" external/src/mekhq/MekHQ/resources external/src/mekhq/MekHQ/docs external/installs/MekHQ-0.51.00/docs
rg -n "scenario|RAT|force generator|movement|Princess|AutoResolve" external/src/megamek/megamek/docs external/installs/MekHQ-0.51.00/docs
```

Use the in-app documentation index when looking for PDF-backed help:

```powershell
rg -n "GlossaryDocs|DocumentationEntry|GlossaryEntry" external/src/mekhq/MekHQ/src external/src/mekhq/MekHQ/resources
```

For PDFs, extract only what you need into ignored scratch space:

```powershell
New-Item -ItemType Directory -Force analysis\tmp\docs
pdftotext "external/src/mekhq/MekHQ/docs/GlossaryDocs/Unit Markets.pdf" analysis\tmp\docs\unit-markets.txt
rg -n "contract|availability|market" analysis\tmp\docs\unit-markets.txt
```

`Confirmed locally`: `pdftotext` is available on this machine through Poppler, and Python `pypdf` is importable. `pdfplumber` and `PyPDF2` were not available in the default shell at the time this note was written.

## Copyright And Rules Posture

Do not reproduce long passages from rulebooks, local help PDFs, or docs. Summarize in your own words, cite the relevant local file/class/source context, and keep direct quotes short only when they are necessary.

BattleTech has layered rules and optional systems. Ask or inspect campaign options when the answer changes materially by ruleset. If the question is about the user's table rather than MekHQ behavior, separate:

- what MekHQ appears to do
- what the local docs say
- what BattleTech rules generally imply
- what the user must decide as table policy

## Durable Updates

When a help/doc/source lookup teaches something reusable, update the narrowest owning document:

- `BATTLETECH_CONTEXT.md` for reusable campaign interpretation concepts.
- `CAMPAIGN_ANALYSIS_WORKFLOW.md` for campaign-analysis sequence changes.
- `SAVE_FORMAT_NOTES.md` for save fields and campaign XML behavior.
- `SOURCE_CODE_GUIDE.md` for source entry points and exact implementation paths.
- `KNOWN_COMMANDS.md` for repeatable search, extraction, launch, or verification commands.
- A focused `docs/current/` note for substantial mechanics or workflow findings.

Keep raw extracted text under `analysis/tmp/` unless the user explicitly asks for a durable generated artifact.
