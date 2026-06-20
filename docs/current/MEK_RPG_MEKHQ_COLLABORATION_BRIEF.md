# MEK-RPG / MekHQ Collaboration Brief

Status: shareable coordination brief for MEK-RPG team review, created `2026-06-20`.

## Purpose

We are exploring whether MEK-RPG can use MekHQ as the hard-facts campaign ledger for unit-scale BattleTech play while MEK-RPG remains the A Time of War narrative and GM-assistant layer.

The goal is not to replace MEK-RPG with MekHQ. The goal is to avoid rebuilding systems MekHQ already tracks well: dates, contracts, markets, units, pilots, DropShips, finances, repairs, salvage, casualties, and tactical battle consequences.

Full technical assessment: `docs/current/MEK_RPG_MEKHQ_INTEGRATION_ASSESSMENT.md`.

## Proposed Ownership Boundary

MekHQ should own hard campaign facts when they matter mechanically:

- campaign date and travel/downtime progression
- current location
- funds, payroll, debt, purchases, and sale values
- BattleMechs, vehicles, aerospace, DropShips, JumpShips, cargo, transport capacity, and repair state
- pilots, crews, technicians, doctors, administrators, fatigue, injury, assignments, and deployment history
- contracts, scenarios, battlefield control, salvage, kill credit, prisoners, and post-battle consequences
- unit market, personnel market, and contract market offers

MEK-RPG should own RPG and narrative facts:

- A Time of War PC sheets, attributes, traits, Edge, XP, lifepath details, and personal goals
- NPC motives, secrets, promises, relationships, leverage, faction tension, and scene framing
- inspections, negotiations, legal ambiguity, title problems, rumors, and hidden risks
- safety/tone boundaries and co-player preferences
- personal-scale scenes that do not require hex-scale tactical state

Calendar rule proposal:

- Once linked, MekHQ is authoritative for campaign date.
- MEK-RPG can run scenes within a day, but day advancement should happen in MekHQ first, then MEK-RPG reads the saved MekHQ state.

## User Burden Estimate

Low-burden path:

1. User plays RPG scenes in MEK-RPG.
2. User opens MekHQ only to commit hard ledger actions: advance day, buy/sell, accept contract, assign staff, resolve repairs, resolve tactical battle.
3. User saves MekHQ.
4. MEK-RPG reads the MekHQ save and presents the updated hard facts as natural GM context.

This is acceptable if read-only automation exists.

High-burden path to avoid:

- Manually copying every contract, market offer, repair, injury, purchase, and date change into both projects.
- Advancing MEK-RPG and MekHQ calendars independently.
- Letting MEK-RPG invent hard logistics that MekHQ later contradicts.

## Automation Roadmap

### Phase 1: Read-Only MekHQ Save Summary

Build a helper that reads a `.cpnx.gz` MekHQ save and emits either JSON, Markdown, or both.

Initial fields:

- campaign name, date, faction, location
- funds and major financial warnings
- owned units and transport assets
- DropShip status, cargo/transport capacity if practical
- personnel roster, key roles, injuries, fatigue, assignments
- active contracts and scenarios
- unit market offers, including DropShips and BattleMechs when present
- contract market offers
- repair/acquisition alerts and daily report highlights

This is the safest first step because it does not alter MekHQ state.

### Phase 2: RPG Overlay For Markets And Contracts

Use MekHQ market and contract offers as scene seeds:

- A unit market offer becomes a sales yard, broker listing, auction, black-market lead, or DropShip inspection.
- A contract offer becomes an MRBC briefing, employer meeting, clause negotiation, intelligence review, or moral dilemma.
- A personnel-market applicant becomes an interview, background check, loyalty risk, or recruitment scene.
- A repair or acquisition delay becomes a parts hunt, favor request, debt pressure, or downtime scene.

MEK-RPG handles the roleplay. MekHQ remains the tool that applies the final transaction.

### Phase 3: Tactical Handoff

When a mission needs exact tactical handling:

1. MEK-RPG defines narrative stakes, objective, opposition intent, terrain, and special constraints.
2. MekHQ supplies roster, pilots, unit condition, transport, and setup artifacts.
3. Battle is played in MegaMek or on the tabletop.
4. MekHQ applies the result through normal scenario resolution or a battle-record MUL workflow.
5. MEK-RPG reads the resulting save and narrates consequences.

### Phase 4: Write Automation Only After Proof

Do not start by editing MekHQ saves directly.

Safer write paths:

1. Use MekHQ UI, then save.
2. Use artifacts MekHQ already understands, such as MULs.
3. Add a MekHQ source-backed command or helper that calls MekHQ campaign methods.
4. Direct XML edits only for narrow, source-confirmed fields after round-trip validation.

Headless day advancement is a later investigation. Source inspection found `Campaign.newDay()` is the core hook, but the current daily flow reaches GUI objects and dialogs, so a reliable noninteractive command is not a simple existing feature.

## Collaboration Questions For MEK-RPG Team

1. Which MEK-RPG campaign should be the pilot: `playtest-galatea-dropship`, the first real campaign, or a new disposable bridge test?
2. Should MEK-RPG store a single bridge pointer file, for example `campaigns/<id>/mekhq-bridge.md`, with the MekHQ save path and ownership boundary?
3. Should the first helper output Markdown directly into the campaign folder, JSON for the GM assistant to read, or both?
4. Which fields matter most for live play: contracts, unit market, DropShips, repairs, personnel, active scenarios, or finances?
5. How should MEK-RPG represent MekHQ-linked people and assets: by MekHQ UUID, MEK-RPG slug, display name, or a cross-reference table?
6. How much uncertainty should MEK-RPG preserve around market listings? Example: MekHQ knows a unit exists and has a price; MEK-RPG can add inspection/title/condition drama before the player commits.
7. Is it acceptable for the first version to require the user to advance days and perform transactions in MekHQ manually?
8. What is the minimum useful "RPG market scene" for a first proof: DropShip purchase, BattleMech purchase, contract negotiation, hiring interview, or repair/parts hunt?

## Proposed First Joint Issue

Title: Build read-only MekHQ save summary for MEK-RPG bridge

Goal:

- Given a MekHQ `.cpnx.gz` campaign save, produce a MEK-RPG-readable summary of hard campaign facts without modifying the save.

Acceptance criteria:

- Reads gzip-compressed and plain MekHQ campaign XML safely.
- Emits campaign date, location, funds, unit roster, personnel summary, active contracts/scenarios, market offers, and obvious repair/logistics alerts where present.
- Preserves MekHQ IDs for units/personnel when available.
- Does not write to the MekHQ save.
- Produces output suitable for MEK-RPG play mode to use as GM context.
- Documents unsupported fields and source-confirmed field mappings.

Recommended home:

- Implementation could live in MEK-RPG if the output is primarily for MEK-RPG play.
- Source mapping and MekHQ behavior notes should stay mirrored or linked from this MegaMek workspace until stable.

## Documents To Share

Start with:

- `docs/current/MEK_RPG_MEKHQ_COLLABORATION_BRIEF.md`
- `docs/current/MEK_RPG_MEKHQ_INTEGRATION_ASSESSMENT.md`

Useful supporting MegaMek workspace docs:

- `docs/current/TABLETOP_RESULT_MUL_WORKFLOW.md`
- `docs/current/MECH_ROSTER_CONTROL_WORKFLOWS.md`
- `docs/current/SAVE_FORMAT_NOTES.md`
- `docs/current/ROADMAP.md` section: `Explore MEK-RPG and MekHQ campaign bridge`

Useful MEK-RPG docs to compare against:

- `C:\Users\waltr\Documents\mek-rpg\docs\current\CAMPAIGN_MEMORY_STRATEGY.md`
- `C:\Users\waltr\Documents\mek-rpg\gm\switch-to-classic-battletech.md`
- `C:\Users\waltr\Documents\mek-rpg\rules\campaign\contracts.md`
- `C:\Users\waltr\Documents\mek-rpg\campaigns\_template\assets.md`
