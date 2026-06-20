# MEK-RPG And MekHQ Integration Assessment

Status: initial assessment from local MEK-RPG docs and local MekHQ source inspection on `2026-06-20`.

## Summary

MekHQ can help MEK-RPG most when the RPG campaign crosses into mercenary-company, vehicle, BattleMech, DropShip, contract, repair, salvage, personnel, and tactical-battle consequences.

The recommended boundary is:

- MEK-RPG remains the narrative and A Time of War authority for PCs, NPCs, relationships, secrets, contacts, social consequences, personal-scale scenes, safety/tone, and RPG-only advancement.
- MekHQ becomes an optional force-and-logistics ledger for unit-scale assets and a tactical handoff tool when exact BattleTech state matters.
- Integration should begin with a manually maintained parallel MekHQ campaign and read-only summary extraction, not direct `.cpnx.gz` save editing or a source change.

## Evidence

- `Confirmed from MEK-RPG docs`: MEK-RPG already routes full tactical BattleTech handling to Classic BattleTech, MegaMek, or MekHQ when hex positioning, heat, armor locations, weapon ranges, critical hits, salvage, repairs, or force history matter. See `C:\Users\waltr\Documents\mek-rpg\gm\switch-to-classic-battletech.md`.
- `Confirmed from MEK-RPG docs`: MEK-RPG campaign memory is repository-native Markdown under `campaigns/<campaign-id>/`, with `assets.md`, `missions.md`, `pcs.md`, `npcs.md`, `relationships.md`, and `current-state.md` as separate owners. See `C:\Users\waltr\Documents\mek-rpg\docs\current\CAMPAIGN_MEMORY_STRATEGY.md`.
- `Confirmed from MEK-RPG docs`: MEK-RPG's contract summary explicitly says full BattleTech mercenary contract construction, force accounting, repair budgets, and strategic logistics should use MekHQ or another BattleTech campaign tool when that detail matters. See `C:\Users\waltr\Documents\mek-rpg\rules\campaign\contracts.md`.
- `Confirmed from source`: MekHQ loads `.cpnx`, `.cpnx.gz`, or XML campaign saves by detecting gzip magic bytes and parsing campaign XML through `CampaignXmlParser`. See `external/src/mekhq/MekHQ/src/mekhq/campaign/CampaignFactory.java`.
- `Confirmed from source`: MekHQ saves campaign state as XML, optionally gzip-compressed, through `CampaignGUI.saveCampaign(...)` and `Campaign.writeToXML(...)`. Major written areas include reports, options, units, personnel, missions, formations, finances, locations, player bases, shopping list, kills, skill types, special abilities, and parts. See `external/src/mekhq/MekHQ/src/mekhq/gui/CampaignGUI.java` and `external/src/mekhq/MekHQ/src/mekhq/campaign/Campaign.java`.
- `Confirmed from source`: MekHQ manual scenario resolution imports a battle-record MUL through `MekHQ.resolveScenario(...)`, then applies casualties, salvage, scenario status, and post-scenario handling through the resolve wizard flow. See `docs/current/TABLETOP_RESULT_MUL_WORKFLOW.md`.
- `Confirmed from source`: MekHQ has safe UI-supported roster and OPFOR control workflows for GM adding/removing player units, scenario regeneration, bot-force editing, and fixed OPFOR setup MULs. See `docs/current/MECH_ROSTER_CONTROL_WORKFLOWS.md`.

## What MekHQ Can Usefully Own

Good candidates for MekHQ ownership or mirrored ownership:

- BattleMech, vehicle, aerospace, DropShip, and support-unit rosters.
- Unit armor, internal damage, critical damage, ammo, heat-facing tactical battle records, and repair state.
- Technicians, doctors, administrators, pilots, gunners, crews, ranks, roles, injuries, and deployment history when they are unit-scale campaign staff.
- Finances, purchases, maintenance costs, salaries, parts, acquisitions, debts, and shopping lists when those mechanics matter.
- Contracts, scenarios, battlefield control, salvage rights, recovered units, kill credit, prisoner/casualty consequences, and post-battle reports.
- Transport capacity and logistics pressure for owned DropShips, bays, cargo, deployed formations, and travel.

These are exactly the areas where MEK-RPG's Markdown state would otherwise have to recreate a lot of bookkeeping.

## What Should Stay In MEK-RPG

Do not try to make MekHQ the source of truth for:

- A Time of War character sheets, attributes, traits, lifepath details, Edge, RPG XP, personal goals, and non-tactical character advancement.
- NPC secrets, motives, promises, attitudes, family ties, relationship leverage, and scene framing.
- GM-only hooks, mysteries, hidden faction pressures, safety/tone notes, and child/co-player boundaries.
- Personal combat scenes that do not need full vehicle-scale or BattleMech-scale tactical handling.
- Ambiguous legal or narrative asset facts, such as whether a DropShip title is clean, unless and until the table decides the fact becomes a hard campaign ledger entry.

MekHQ can represent a pilot, unit, injury, or asset, but it cannot preserve all RPG context around why that object matters.

## Recommended Integration Stages

### Stage 1: Parallel Ledger

Create a disposable or deliberate MekHQ campaign that mirrors only the unit-scale portion of a MEK-RPG campaign:

1. Choose one MEK-RPG campaign folder as the narrative source.
2. Create a separate MekHQ campaign save for the associated company, ship, or combat unit.
3. Use MekHQ UI/GM controls to add units, pilots, support staff, transport, and starting funds.
4. Record the MekHQ save path and campaign identity in the MEK-RPG campaign `assets.md` or a future bridge note.
5. Keep MEK-RPG `assets.md` as the human-readable summary, with MekHQ treated as the detailed ledger.

This avoids save surgery and lets the user decide whether MekHQ's bookkeeping is worth the added ceremony.

### Stage 2: Tactical Handoff

When MEK-RPG produces a mission that needs exact tactical handling:

1. MEK-RPG records the narrative objective, stakes, terrain, factions, and special constraints.
2. MekHQ supplies the force roster, unit condition, pilots, transport readiness, and scenario or setup MULs.
3. The fight is played in MegaMek or on the tabletop.
4. MekHQ resolves the result through its normal post-scenario flow or through the battle-record MUL workflow already being investigated in this workspace.
5. MEK-RPG imports only the narrative summary: who survived, what was damaged, what was salvaged, who is angry, what changed, and what the next scene is.

This uses each tool where it is strongest.

### Stage 3: Read-Only Bridge Helper

After at least one real parallel-ledger campaign exists, build a small helper that reads MekHQ saves or reports and generates MEK-RPG-friendly Markdown summaries.

Candidate outputs:

- `assets.md` section for unit roster, DropShip/transport status, funds, debt, salvage, repairs, and cargo.
- `missions.md` section for active MekHQ contracts or scenarios.
- `pcs.md` or `npcs.md` annotations for linked pilots, injuries, fatigue, and assigned units.
- A tactical handoff packet with unit IDs, pilot names, gunnery/piloting values, damage status, force BV, and scenario objective notes.

The helper should be read-only first. Direct writes into `.cpnx.gz` should remain out of scope until source-backed save mapping and UI validation are much stronger.

### Stage 4: Deeper Automation Only If Proven Useful

Possible later work:

- Generate battle-record MULs from tabletop results so MekHQ can apply damage, salvage, casualties, and kill credit.
- Export MekHQ summaries into MEK-RPG campaign folders after every tactical session.
- Add a stable cross-reference field convention: MekHQ `Unit` UUIDs, `Person` UUIDs, and MEK-RPG character/asset slugs.
- Consider MekHQ source changes only after the bridge proves a recurring manual pain point that cannot be solved with UI workflows, MULs, or read-only extraction.

## Risks And Tradeoffs

- MekHQ may add too much bookkeeping for small RPG scenes. Use it only when the unit-scale ledger matters.
- A Time of War skills and MekHQ/MegaMek gunnery-piloting values do not map perfectly. Conversion assumptions should be recorded in MEK-RPG before a tactical handoff.
- MekHQ saves are broad XML documents with many interlinked IDs. Direct editing is risky; prefer UI workflows and read-only parsing.
- MEK-RPG's campaign state includes GM secrets and narrative ambiguity that should not be flattened into MekHQ prematurely.
- Parallel ledgers can drift. The bridge needs explicit ownership: MekHQ for exact unit/logistics state, MEK-RPG for narrative state and player-facing continuity.

## Near-Term Recommendation

Do not integrate code yet.

The next useful experiment is to create one MEK-RPG-to-MekHQ pilot workflow:

1. Pick the MEK-RPG campaign `playtest-galatea-dropship` or the first real campaign save.
2. In MekHQ, create a safe campaign that models only the hard assets: funds, DropShip candidate, pilots/crew if known, and any combat units.
3. Add a MEK-RPG bridge note with the MekHQ save path, what MekHQ owns, what MEK-RPG owns, and the next tactical handoff trigger.
4. After one tactical or logistics event, compare the cost of maintaining both states against the value MekHQ provided.

If that experiment works, create a GitHub issue for a read-only MekHQ-to-MEK-RPG summary helper.
