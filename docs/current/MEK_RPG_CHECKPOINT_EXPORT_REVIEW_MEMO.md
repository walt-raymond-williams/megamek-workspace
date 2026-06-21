# MEK-RPG Checkpoint Export Review Memo

Date: 2026-06-21

Audience: MEK-RPG team

Purpose: request review of the MegaMek-side read-only MekHQ checkpoint export artifacts before we create the next round of adapter-facing or production-hardening issues.

## Summary

The MegaMek workspace completed the first read-only checkpoint export workstream for MEK-RPG. The work stayed read-only: no MekHQ saves were mutated, no write automation was added, and no headless day advancement or campaign actions were implemented.

The current result is suitable for MEK-RPG adapter experiments, but it is not yet a production MekHQ CLI/export feature.

## Delivered Artifacts

- Sanitized fixture:
  - `C:\Users\waltr\Documents\megamek-workspace\docs\templates\mekhq-read-only-checkpoint.fixture.json`
- Draft schema:
  - `C:\Users\waltr\Documents\megamek-workspace\docs\current\MEK_RPG_MEKHQ_CHECKPOINT_EXPORT_SCHEMA.md`
- Source-backed export contract:
  - `C:\Users\waltr\Documents\megamek-workspace\docs\current\MEK_RPG_MEKHQ_CHECKPOINT_EXPORT.md`
- Disposable-save validation:
  - `C:\Users\waltr\Documents\megamek-workspace\docs\current\MEK_RPG_MEKHQ_CHECKPOINT_VALIDATION.md`
- Jar-backed prototype notes:
  - `C:\Users\waltr\Documents\megamek-workspace\docs\current\MEK_RPG_MEKHQ_CHECKPOINT_EXPORTER_PROTOTYPE.md`
- Prototype exporter:
  - `C:\Users\waltr\Documents\megamek-workspace\tools\mekhq-checkpoint-exporter\`
- Epic close-out review:
  - `C:\Users\waltr\Documents\megamek-workspace\docs\current\MEK_RPG_MEKHQ_CHECKPOINT_EPIC_REVIEW.md`

## What Works Now

- The sanitized fixture parses as JSON and matches the agreed MEK-RPG top-level consumer shape.
- The prototype exporter can load an explicit copied `.cpnx.gz` through installed MekHQ jars.
- The prototype emits parseable checkpoint JSON to stdout.
- The prototype produces method-backed examples for:
  - campaign date/name
  - finance balance
  - personnel full title, role, salary, fatigue, and hits
  - unit condition and repair counts
  - unit-market final price
  - current and technical report lines
- The output preserves warnings and unsupported fields instead of silently inventing unsafe values.

Verified sample run used:

```text
C:\Users\waltr\Documents\megamek-workspace\analysis\tmp\issue-22\Autosave-1-The Learning Ropes-30250720.cpnx.gz
```

Observed parsed output included:

- schema: `mekhq-read-only-checkpoint`
- campaign: `The Learning Ropes`
- date: `3025-07-20`
- balance: `CSB 91255718`
- personnel: `106`
- units: `27`
- contracts: `1`
- scenarios: `2`
- unit-market offers: `48`

## Current Caveats

- The exporter is a workspace prototype, not an official MekHQ command.
- It uses installed MekHQ jars and a PowerShell wrapper.
- It uses reflection to instantiate MekHQ without launching the GUI because `MekHQ#getInstance()` is protected and the installed jar seals the `mekhq` package.
- Cargo/transport pressure remains `Needs MekHQ inspection`.
- Contract-term extraction is shallow in the prototype.
- Report classification is shallow; current and technical reports are sanitized, but full alert classification is not complete.
- Unit-market offers still lack a source-confirmed stable offer UUID, so they are not safe automation selectors.
- No write-side workflow is included.

## Review Questions

Please review the fixture, schema, validation note, and prototype notes with these questions in mind:

1. Is the top-level JSON shape convenient for MEK-RPG adapter code?
2. Are `evidence`, `source_owner`, `method_backed`, `warnings`, and `unsupported` useful in the form shown?
3. Which fields are highest priority for MEK-RPG next: logistics, contracts, reports, unit condition, personnel, markets, or something else?
4. Is the sanitized fixture sufficient for adapter/bootstrap tests, or should it include additional edge cases?
5. Should MEK-RPG consume the workspace prototype output directly for experiments, or wait for a cleaner MekHQ-owned exporter?
6. Should the next shared milestone be MEK-RPG adapter tests, MegaMek exporter hardening, or moving the exporter into MekHQ source?
7. Are there fields MEK-RPG wants removed, renamed, or grouped differently before the shape becomes harder to change?

## Suggested Next Issues After Review

Potential MegaMek-side issues:

- Harden the jar-backed checkpoint exporter output against the schema.
- Add a repeatable smoke test for fixture JSON and prototype JSON.
- Deepen read-only contract term extraction.
- Deepen read-only logistics and transport/cargo warnings.
- Investigate whether a production exporter should move into MekHQ source.
- Investigate stable read-only market identifiers without implementing purchase automation.

Potential MEK-RPG-side issues:

- Add adapter tests using the sanitized fixture.
- Add adapter tests using prototype output from a disposable save.
- Decide which checkpoint fields should appear in campaign-facing context files.
- Define how MEK-RPG should surface warnings and unsupported fields to the GM.

## Boundary Reminder

This checkpoint workstream is read-only. Any future write-side action, including day advancement, contract accept/decline, hiring, repair, purchases, sales, assignments, or tactical result application, should be a separate issue with explicit selectors, preconditions, prompt policy, and disposable-save validation.
