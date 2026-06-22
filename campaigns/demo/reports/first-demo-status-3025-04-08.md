# First Demo Campaign Status

Date created: `2026-06-22`

Input save: `campaigns/demo/ai-ready-demo.cpnx.gz`

Inspection method: copied save to `analysis/tmp/issue-2/`, parsed gzip XML read-only with PowerShell/.NET XML APIs, and checked method-backed summary fields with `tools/mekhq-checkpoint-exporter/run-mekhq-checkpoint-exporter.ps1`.

## Facts

- `Confirmed from save`: campaign name is `The Learning Ropes`.
- `Confirmed from save`: campaign date is `3025-04-08`.
- `Confirmed from save`: faction is `MERC`.
- `Confirmed from MekHQ export`: current location is `Canopus IV, Canopus IV`.
- `Confirmed from MekHQ export`: funds are `CSB 90938250`; active loans are `false`.
- `Confirmed from save`: formation commander is Michelle Moreno `"Double-M"` (`MEKWARRIOR`).
- `Confirmed from save`: there are `106` personnel: `94` active, `7` camp followers, and `5` students.
- `Confirmed from save`: key personnel counts include `12` MekWarriors, `12` Mek techs, `40` ground vehicle crew, `7` soldiers, `10` dependents, `6` administrators, `4` mechanics, `3` vessel pilots, `3` vessel gunners, `2` vessel crew, and `1` doctor.
- `Confirmed from MekHQ export`: there are `25` units and all are currently `Undamaged`.
- `Confirmed from MekHQ export`: repair backlog is `0` parts needed, `0` parts needing service, and `0` units under repair.
- `Confirmed from save`: there are no active missions and no active scenarios.
- `Confirmed from save`: contract market has one offer, `3025 - FWL - Castrovia Objective Raid`, employer `Free Worlds League`, target system `Astrokaszy`, start `3025-07-03`, end `3025-10-03`, salvage `100%`, support `100%`, transport compensation `100%`, advance `25%`.
- `Confirmed from MekHQ export`: unit market has `37` offers. Market entries remain display/opportunity context, not automation selectors.

## Force Picture

BattleMechs: Griffin GRF-1N, Locust LCT-1E, Centurion CN9-A, Flea FLE-4, Crab CRB-20, Trebuchet TBT-5N, Stinger STG-3R, Spider SDR-5V, Grasshopper GHR-5H, two Stalker STK-4Ps, and Crusader CRD-3R.

Support assets include a Leopard (2537), flatbed trucks, Scorpion Light Tanks, a rifle foot squad, a MASH Truck, a Sherpa Armored Truck/Mobile Canteen, and four BattleMech Recovery Vehicles.

## Interpretation

- `High confidence`: the demo campaign is not yet on an active contract. The next campaign decision is whether to take the FWL Objective Raid offer or wait for other work/opportunities.
- `High confidence`: the force is healthy for a first contract. No units are damaged or under repair in the method-backed checkpoint summary.
- `Medium confidence`: the company has a strong cash cushion, but recent transaction history shows many equipment purchases and recurring maintenance/overhead/salary costs. Profitability should still be checked before accepting a contract.
- `Medium confidence`: transport is a hidden pressure point. The save reputation section reports transportation rating `-5`, and `ACTIVE_CAMPAIGN.md` records transport requirements exceeding some capacities. Verify transport/bay details in MekHQ before assuming every unit can deploy cleanly.
- `Low confidence`: exact StratCon/AtB behavior is configured in options, but no active scenario exists yet. UI inspection would be needed to confirm how the next generated scenario appears.

## Recommended Next Actions

1. Before accepting the Objective Raid, compare its destination, travel date, and payout against transport requirements and monthly cost pressure.
2. Use the twelve undamaged BattleMechs as the first deployable combat baseline; keep support vehicles and recovery assets as logistics context unless MekHQ assigns them to a scenario.
3. Treat market unit offers as shopping/scene opportunities only. Do not use them as purchase automation selectors.
4. If this demo campaign becomes the recurring example, build a small repeatable extractor that emits this report shape without preserving raw XML.

## Unknowns

- Exact cargo/bay occupancy was not validated against the MekHQ UI.
- Exact AtB/StratCon next-scenario behavior was not validated because there is no active scenario in the save.
- The report did not classify all campaign reports or daily alerts; it focused on current identity, force, finance, contract-market, and readiness facts.
