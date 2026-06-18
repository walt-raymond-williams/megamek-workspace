# Active Campaign

This file is the fastest entry point for current campaign context. Update it whenever the active save, contract, scenario, or campaign operating assumptions change.

## Save File

- Path: `Unknown`
- MekHQ version: `Unknown`
- Last verified: `Unknown`
- Evidence: `Unknown`

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

1. Identify the active MekHQ campaign save location.
2. Parse the active campaign save without modifying it.
3. Produce the first campaign status report from real campaign data.

## Open Questions

- Where does the user's active MekHQ campaign save live?
- Which optional MekHQ campaign systems are enabled?
- Which campaign facts should be treated as user-confirmed baseline context?

## Update Rules

- Do not guess campaign identity from memory. Mark unknowns until confirmed by save data, source behavior, local docs, or the user.
- Record the evidence source for important changes.
- Keep raw campaign saves untracked unless the user explicitly asks for a snapshot.
