# Active Campaign

This file is the fastest entry point for current campaign context. Update it whenever the active save, contract, scenario, or campaign operating assumptions change.

## Save File

- Path: `C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\campaigns\The Learning Ropes.cpnx.gz`
- MekHQ version: `0.51.00`
- Last verified: `2026-06-18`
- Evidence: `Confirmed by user`; file path existence confirmed locally; version previously observed from bundled sample save.

## Campaign Identity

- Unit: `Unknown`
- Commander: `Unknown`
- Faction: `Unknown`
- Employer: `Unknown`
- Campaign date: `Unknown`
- Current location: `Unknown`

## Active Contract / Scenario

- Contract: `Unknown`
- Enemy: `Unknown`
- Mission type: `Unknown`
- Remaining battles: `Unknown`
- Current scenario: `Unknown`

## Enabled Systems

- StratCon: `Unknown`
- Against the Bot: `Unknown`
- Maintenance: `Unknown`
- Fatigue: `Unknown`
- Markets: `Unknown`
- Advanced repair: `Unknown`
- Other optional rules: `Unknown`

## Current Priorities

1. Parse the active campaign save without modifying it.
2. Identify MekHQ save/load source classes.
3. Produce the first campaign status report from `The Learning Ropes.cpnx.gz`.

## Open Questions

- Which optional MekHQ campaign systems are enabled?
- Which campaign identity fields should be treated as confirmed after save inspection?
- Should reports for this campaign live under `campaigns/learning-ropes/reports/`?

## Update Rules

- Do not guess campaign identity from memory. Mark unknowns until confirmed by save data, source behavior, local docs, or the user.
- Record the evidence source for important changes.
- Keep raw campaign saves untracked unless the user explicitly asks for a snapshot.
