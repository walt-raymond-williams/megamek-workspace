# Tabletop Result Entry Feasibility

This note records a brief discovery pass on whether MekHQ already supports manual tabletop battle result entry, and whether an external photo/OCR-driven entry tool is worth exploring.

Evidence labels:

- `Confirmed from source`: directly observed in the local source checkout.
- `Inferred from source`: follows from observed code paths but has not been validated through the UI in this workspace.
- `Feasibility assessment`: engineering judgment based on confirmed behavior and expected UX constraints.

## Existing MekHQ Support

- `Confirmed from source`: MekHQ has a Resolve Manually flow that asks for one `.mul` result file in `ChooseMulFilesDialog`. Its user-facing text says to select a MUL file for surviving and salvaged units from the end of a MegaMek game.
- `Confirmed from source`: If a result MUL is imported, `ResolveScenarioTracker` reads battle-record sections and initializes scenario result state for units, personnel, salvage, prisoners, kills, objectives, rewards, and preview.
- `Confirmed from source`: If no battle result file is selected, `ResolveScenarioTracker.initUnitsAndPilotsWithoutBattle()` initializes deployed units and crews in pre-battle state. This allows manual resolution without a generated result file, but does not automatically encode tabletop damage.
- `Confirmed from source`: `ResolveScenarioWizardDialog` includes panels for player units, player personnel, salvage, captured personnel, kill assignment, costs/payouts, objective status, and preview.
- `Confirmed from source`: The wizard's player-unit panel includes a `Total Loss` checkbox and an `Edit Unit` button for each deployed unit. The resource text explicitly says the user can manually edit damage suffered by each unit.
- `Confirmed from source`: `ResolveScenarioWizardDialog.editUnit(...)` opens MegaMek's `UnitEditorDialog` against the result entity being reviewed.
- `Confirmed from source`: `UnitEditorDialog` can edit armor, internal structure, rear armor, equipment critical hits, and many system criticals such as engine, gyro, sensors, life support, cockpit, actuators, vehicle systems, proto systems, and large-craft bay damage.
- `Confirmed from source`: This brief pass did not find ammo expenditure editing in `UnitEditorDialog`; it appears focused on damage and critical state rather than reconstructing every post-battle transient.

## Practical Meaning

MekHQ already has a built-in human-review workflow for manual scenario closeout. It is probably good enough for a low-volume tabletop campaign if the GM is willing to:

1. resolve the scenario manually,
2. skip or provide a battle-record MUL,
3. mark total losses,
4. edit surviving unit damage one unit at a time,
5. set pilot hits, MIA, or KIA,
6. choose salvage and prisoners,
7. assign kills,
8. verify objectives and scenario status.

The weakness is input ergonomics. The built-in flow is not optimized for fast transcription from paper record sheets. It is safer than raw save editing, but it may be tedious for several units because each unit must be opened and edited through a generic damage editor.

## External UI Feasibility

`Feasibility assessment`: A separate web app is feasible and probably useful if tabletop play becomes regular. The lowest-risk version should not edit campaign saves directly. It should produce a MekHQ-compatible battle-record MUL or a structured result file that a generator converts into a battle-record MUL.

Recommended architecture:

1. Export scenario setup MULs from MekHQ.
2. Import those setup MULs into the web app to know the exact units, locations, original armor/internal values, equipment, and campaign UUIDs.
3. Let the user enter final state manually or with photo assistance.
4. Validate final state against the original unit data.
5. Generate a battle-record MUL with the sections MekHQ expects: survivors, allies, salvage, retreated, devastated, and kills.
6. Import the generated battle-record MUL through MekHQ Resolve Manually.
7. Let MekHQ's existing wizard handle final review, personnel consequences, salvage, objectives, and closeout.

This keeps MekHQ as the campaign authority and avoids fragile `.cpnx.gz` editing.

## Photo / Record Sheet Reading Feasibility

`Feasibility assessment`: Reading BattleTech record sheets from photos is plausible but should be human-in-the-loop.

What is realistic:

- Detect the unit sheet identity or ask the user to confirm it.
- Use the exported setup MUL as the source of truth for the unit layout.
- Read handwritten or marked armor/internal bubbles or numbers well enough to prefill likely final values.
- Detect obvious crossed-out equipment or critical-hit marks as candidate destroyed items.
- Present a review screen with confidence indicators before generating a result.

What is risky:

- Fully automatic interpretation from arbitrary lighting, skew, handwriting, pencil marks, erasures, and custom sheets.
- Reliably distinguishing armor damage, internal damage, transferred damage, critical hits, ammo expenditure, heat, pilot hits, consciousness, ejection, and retreat state without user confirmation.
- Trusting OCR output directly for campaign updates.

Recommended approach:

- Start with manual entry backed by validation.
- Add photo import as an assistant that pre-fills fields, never as the final authority.
- Prefer a guided capture flow: one unit per photo, corners visible, known record sheet template, user confirms unit, app highlights uncertain locations.
- Store both the original photo and the reviewed structured result for auditability.

## Recommendation

Do not edit campaign saves for tabletop damage entry. Use MekHQ's Resolve Manually flow as the authoritative import path.

For near-term play, use the built-in wizard and `Edit Unit` dialog. For a durable project feature, build a small result-entry UI that imports scenario MULs, captures final unit state, and emits a battle-record MUL. Photo reading is a good second phase after the manual schema and round-trip generator are validated.

## Source References

- `external/src/mekhq/MekHQ/src/mekhq/gui/dialog/ChooseMulFilesDialog.java`
- `external/src/mekhq/MekHQ/src/mekhq/gui/dialog/ResolveScenarioWizardDialog.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/ResolveScenarioTracker.java`
- `external/src/megamek/megamek/src/megamek/client/ui/dialogs/UnitEditorDialog.java`
- `external/src/mekhq/MekHQ/resources/mekhq/resources/ResolveScenarioWizardDialog.properties`
- `docs/current/TABLETOP_RESULT_MUL_WORKFLOW.md`
