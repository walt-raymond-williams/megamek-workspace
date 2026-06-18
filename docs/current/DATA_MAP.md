# MegaMek/MekHQ Data Map

This document tracks where important game and campaign data lives locally.

## Installed Suite

Root:

```text
C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00
```

Important areas:

- `campaigns/`: bundled sample campaigns, including `The Learning Ropes.cpnx.gz`
- `data/`: game data used by MegaMek and MekHQ
- `data/universe/`: factions, eras, random assignment tables, news, ranks, and other campaign-universe data
- `docs/`: local user guides, rules notes, scenario docs, StratCon docs, and technical format notes
- `logs/`: runtime logs
- `mmconf/`: local configuration
- `userdata/`: user override data

## Source Checkouts

Root:

```text
C:\Users\waltr\Documents\megamek-workspace\external\src
```

Known local checkouts:

- `megamek/`: tactical game engine, client, scenario loading, unit loaders, rules implementation, bots/Princess, maps, combat resolution
- `mekhq/`: campaign application, campaign persistence, personnel, contracts, finances, repairs, markets, StratCon/AtB-style campaign systems
- `megameklab/`: unit construction, validation, and record-sheet tooling
- `mm-data/`: shared game data repository used by the MegaMek project

When trying to understand how to control or interact with the installed suite, search source before guessing. Useful commands:

```powershell
rg "class Campaign" C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq
rg "cpnx" C:\Users\waltr\Documents\megamek-workspace\external\src
rg "Scenario" C:\Users\waltr\Documents\megamek-workspace\external\src\megamek
rg "save" C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq
```

## Campaign Files

Observed sample:

```text
C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\campaigns\The Learning Ropes.cpnx.gz
```

Confirmed from the bundled sample:

- `.cpnx.gz` is gzip-compressed XML.
- The root element is `campaign`.
- The bundled sample reports `version="0.51.00"`.
- The early XML includes campaign identity, campaign date, faction, reputation, and transportation capacity/requirements.

Detailed save-format discoveries belong in `SAVE_FORMAT_NOTES.md`.

Always inspect a copy or stream-read the gzip. Do not overwrite the original save.

## Unit and Scenario Data

Likely relevant formats:

- `.mtf`: classic BattleMech unit files
- `.blk`: many non-Mek or structured unit definitions
- `.mul`: MegaMek unit list/scenario force interchange; fixed OPFOR pool setup-MUL workflow is documented in `FIXED_OPFOR_MUL_POOL_WORKFLOW.md`
- `.txt` under `data/rat`: classic random assignment table files loaded by `RandomUnitGenerator`
- `.xml` under `data/forcegenerator`: modern dynamic RAT generator data used by MekHQ's `RATGeneratorConnector`
- `.csv`: preferred first format for the user's physical-miniature inventory; see `PHYSICAL_MINIATURE_ROSTER_MODEL.md` and `docs/templates/PHYSICAL_MINIATURE_ROSTER.csv`
- scenario files: inspect local `docs/` and source parser code before assuming format details

Known source references in the MegaMek checkout:

- `megamek/common/loaders/MULParser.java`
- `megamek/common/units/EntityListFile.java`
- `megamek/client/generator/RandomUnitGenerator.java`
- `megamek/client/ratgenerator/RATGenerator.java`
- `mekhq/campaign/universe/RATGeneratorConnector.java`
- `megamek/common/loaders/MtfFile.java`
- `megamek/common/loaders/BLKFile.java`
- `megamek/common/scenario/ScenarioLoader.java`

Known source areas to map next:

- campaign load/save classes in `mekhq`
- contract and scenario generation in `mekhq`
- repair, maintenance, personnel, and market logic in `mekhq`
- MegaMek game/scenario launch path from MekHQ into MegaMek

## Open Questions

- Where does the user's active MekHQ campaign save live?
- Which MekHQ campaign systems will be enabled: StratCon, Against the Bot, marketplace, maintenance, fatigue, advanced repairs?
- Which BattleTech era/faction/campaign premise should be treated as the user's campaign baseline?
