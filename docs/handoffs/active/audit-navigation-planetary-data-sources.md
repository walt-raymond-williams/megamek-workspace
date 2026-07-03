# Agent Handoff

## Issue

- GitHub issue: `#84`
- Roadmap entry: `Epic: Expose planetary information in the MekHQ API`
- Priority: `High`

## Goal

Identify exactly which fields MekHQ displays in the Navigation side tab for a selected planet/system and map each field to source methods, date dependencies, localization/display formatting, and unsupported/API-risk notes.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/PLANETARY_INFORMATION_API_TRACKING.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`

## Expected Output

- A source-backed audit note under `docs/current/`, recommended path `docs/current/MEK_RPG_LIVE_MEKHQ_PLANETARY_API_SOURCE_AUDIT.md`.
- Roadmap/tracking/task updates that point the next agent to the design issue.
- Any follow-up GitHub issue adjustment if the initial child split is missing a necessary step.

## Files And Areas

Likely files to read:

- `external/src/mekhq/MekHQ/src/mekhq/gui/NavigationTab.java`
- `external/src/mekhq/MekHQ/src/mekhq/gui/view/PlanetViewPanel.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/universe/Planet.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/universe/PlanetarySystem.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/universe/Systems.java`
- academy, disease, bioweapon, and `SourceableValue` helpers referenced from `PlanetViewPanel`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
rg -n "class PlanetViewPanel|SourceableValueLabel|getPlanetPanel|getSystemPanel" external/src/mekhq/MekHQ/src/mekhq
rg -n "getSystemByName|getPlanetById|getPlanet\\(" external/src/mekhq/MekHQ/src/mekhq/campaign external/src/mekhq/MekHQ/src/mekhq/gui
```

## Constraints

- Do not include unrelated user changes.
- Preserve uncertainty and evidence labels.
- This issue is source audit only; do not implement the endpoint here unless the user explicitly broadens the task.
- Commit completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- Every visible Navigation-tab planetary/system field is mapped to source methods or marked unsupported/uncertain.
- Date-sensitive fields and campaign-date behavior are identified.
- Planet/system lookup options by name/id are documented.
- API-design inputs are explicit enough for issue `#85`.
- Roadmap, tracking doc, and task board are updated and changes are committed.

## Open Questions

- Should V1 accept only planet names, or also system ids/planet ids for exact disambiguation?
- Should V1 return all matching candidates on ambiguous names, or refuse with candidate rows?
- Which UI-only formatted fields need raw values plus display labels in the API?
