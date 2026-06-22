# MekHQ Checkpoint Exporter Ownership Decision

Status: decision note for GitHub issue `#33`, created `2026-06-22`.

Purpose: decide the production ownership path for the read-only MekHQ checkpoint exporter after MEK-RPG feedback reconciliation and workspace prototype hardening.

## Decision

`Inferred`: keep the jar-backed exporter in this workspace as an experimental bridge helper for near-term MEK-RPG and campaign-analysis use. Do not move it into MekHQ source yet.

Longer term, a production-quality exporter should be MekHQ-owned if the user or MEK-RPG starts depending on checkpoint JSON as a regular bridge contract. That source move should be a separate issue with explicit CLI/service design, source tests, and build verification.

## Why

- `Confirmed locally`: issue `#32` hardened the workspace helper and smoke-tested it against copied disposable save `analysis/tmp/issue-22/Autosave-1-The Learning Ropes-30250720.cpnx.gz`.
- `Confirmed locally`: the helper now emits stable location display/id fields, method-backed core `Contract` terms, method-backed finance/personnel/unit/market examples, market selector warnings, and mandatory `unsupported` entries.
- `Confirmed from source`: the helper follows MekHQ's real load path through `CampaignFactory#createCampaign(InputStream)` and `CampaignXmlParser`.
- `Confirmed from source`: MekHQ has GUI/CSV export surfaces and campaign subset export flow, but no obvious existing non-GUI JSON checkpoint command.
- `Confirmed locally`: the workspace helper still relies on installed jars and reflection because installed `MekHQ.jar` seals the `mekhq` package and `MekHQ#getInstance()` is protected.
- `Confirmed locally`: the MekHQ source checkout is clean on `main...origin/main`, but source build/test verification is still blocked by the recorded Java/Gradle daemon toolchain issue.

The workspace helper is good enough for controlled, read-only local checkpoint experiments. It is not yet good enough to call a production MekHQ interface.

## Tradeoffs

| Option | Upside | Cost/Risk | Decision |
| --- | --- | --- | --- |
| Keep workspace helper as experimental tooling | Works now, smoke-tested, avoids source churn, keeps read-only boundary obvious | Uses installed jars/reflection, tied to local install path, not an upstream contract | Recommended near term |
| Move exporter into MekHQ source now | Best long-term owner for method-backed DTOs and avoids reflection/package sealing | Needs CLI/service design, source tests, upstream-style review, and currently blocked build verification | Defer |
| Keep only docs/fixture and stop using helper | Lowest maintenance | Loses method-backed checkpoint proof and repeatable smoke path | Reject |
| Move exporter to MEK-RPG | Close to consumer adapter | Would make MEK-RPG own MekHQ method semantics and installed-jar bootstrapping | Reject for producer side |

## Future Source Issue Scope

Do not create the source-change issue yet. Create it only after one of these triggers:

- the user wants regular checkpoint exports from real campaign saves
- MEK-RPG needs a stable producer contract rather than experimental fixtures
- a human wants to propose or maintain the exporter in MekHQ source
- local MekHQ source build/test verification is unblocked

Suggested future issue title:

```text
Add source-owned read-only MekHQ checkpoint JSON exporter
```

Suggested scope:

- Add a non-GUI read-only export command or service in MekHQ source.
- Reuse MekHQ data initialization and `CampaignFactory#createCampaign(...)`.
- Emit the current `mekhq-read-only-checkpoint` top-level shape.
- Preserve `evidence`, `source_owner`, `method_backed`, `warnings`, and `unsupported`.
- Keep markets display/opportunity-only unless stable source-confirmed selectors are designed separately.
- Exclude day advancement, purchases, hiring, repairs, contract accept/decline, tactical-result application, save mutation, and direct XML writeback.
- Add source-level tests for DTO serialization where practical.
- Verify with MekHQ Gradle commands after the Java/Gradle blocker is resolved.

## Current Next Step

Use `tools/mekhq-checkpoint-exporter/test-mekhq-checkpoint-exporter.ps1` as the local smoke check when the workspace helper changes. Treat any future write-side bridge as a separate workstream with selectors, prompt policy, disposable-save validation, and saved re-import confirmation.
