# Active Campaign

This file is the fastest entry point for current campaign context. Update it whenever the active save, contract, scenario, or campaign operating assumptions change.

## Save File

- Path: `C:\Users\waltr\Documents\megamek-workspace\campaigns\demo\ai-ready-demo.cpnx.gz`
- Source copy: `C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\campaigns\The Learning Ropes.cpnx.gz`
- MekHQ version: `0.51.00`
- Last verified: `2026-06-22`
- Evidence: `Confirmed from save`; demo copy created locally from the installed sample save, inspected read-only via copied save under `analysis/tmp/issue-2/`, and checked with the workspace jar-backed checkpoint exporter.

## Campaign Identity

- Unit: `The Learning Ropes`
- Commander: `Michelle Moreno "Double-M"` (`MEKWARRIOR`; formation commander id `fd15b53b-14fa-4c36-ae9a-111c3ccd27ec`)
- Faction: `MERC`
- Employer: `None active`
- Campaign date: `3025-04-08`
- Current location: `Canopus IV, Canopus IV`
- Funds: `CSB 90938250`

## Active Contract / Scenario

- Contract: `None active`
- Enemy: `None active`
- Mission type: `None active`
- Remaining battles: `0 active scenarios`
- Current scenario: `None active`
- Contract market offer: `3025 - FWL - Castrovia Objective Raid` on `Astrokaszy`, employer `Free Worlds League`, type `Objective Raid`, start `3025-07-03`, end `3025-10-03`, salvage `100%`, support `100%`, transport compensation `100%`, advance `25%`.

## Current Snapshot

- Personnel: `106` total; `94` active, `7` camp followers, `5` students.
- Key role counts: `12` MekWarriors, `12` Mek techs, `40` ground vehicle crew, `7` soldiers, `10` dependents, `6` administrators across command/logistics/HR/transport, `4` mechanics, `3` vessel pilots, `3` vessel gunners, `2` vessel crew, `1` doctor.
- Units: `25` total; `12` BattleMechs, `1` Leopard DropShip, `8` wheeled support vehicles, `3` tracked vehicles, and `1` leg infantry unit.
- BattleMechs: Griffin GRF-1N, Locust LCT-1E, Centurion CN9-A, Flea FLE-4, Crab CRB-20, Trebuchet TBT-5N, Stinger STG-3R, Spider SDR-5V, Grasshopper GHR-5H, two Stalker STK-4Ps, and Crusader CRD-3R.
- Method-backed unit condition: `25` undamaged; `0` parts needed; `0` parts needing service; `0` units under repair.
- Markets: `37` unit-market offers, `6` personnel-market applicants, and `1` contract-market offer.

## Enabled Systems

- StratCon: `Configured` (`stratConPlayType=NORMAL`; inferred active-capable from save options, no active scenario yet)
- Against the Bot: `Configured` (`contractMarketMethod=ATB_MONTHLY`, `unitMarketMethod=ATB_MONTHLY`, and AtB battle chance table present)
- Maintenance: `Enabled` (`checkMaintenance=true`, `useUnofficialMaintenance=true`, `maintenanceCycleDays=7`)
- Fatigue: `Disabled` (`useFatigue=false`; fatigue modifiers and injury fatigue options are present but not active as fatigue)
- Markets: `Enabled/configured` (`unitMarketMethod=ATB_MONTHLY`, `personnelMarketStyle=MEKHQ`, `contractMarketMethod=ATB_MONTHLY`)
- Advanced repair: `Enabled` for MRMS repair/salvage options; exact UI workflow not inspected
- Advanced medical: `Enabled` (`useAdvancedMedical=true`, alternate/kinder advanced medical options true)
- Other optional rules: quirks, abilities, edge, support edge, toughness, acquisitions, and Faction Standing market/pay modifiers appear configured in campaign options.

## Current Priorities

1. Identify MekHQ save/load source classes.
2. Use `campaigns/demo/reports/first-demo-status-3025-04-08.md` as the first factual report baseline.
3. If campaign analysis continues, compare selected facts against the MekHQ UI on a disposable copy.

## Open Questions

- Which optional systems are fully active in UI terms versus merely configured in saved options?
- Should future reports use method-backed checkpoint exporter output as the primary extractor, or should a lighter XML-only report script be built for demo saves?
- Should generated extracts live under `analysis/generated/demo/` once the extraction shape stabilizes?

## Update Rules

- Do not guess campaign identity from memory. Mark unknowns until confirmed by save data, source behavior, local docs, or the user.
- Record the evidence source for important changes.
- Keep raw campaign saves untracked unless the user explicitly asks for a snapshot. `campaigns/demo/ai-ready-demo.cpnx.gz` is the current intentional versioned demo fixture.
