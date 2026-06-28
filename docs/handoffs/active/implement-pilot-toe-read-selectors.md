# Agent Handoff

## Issue

- GitHub issue: `#73`
- Roadmap entry: `Epic: Add guarded TO&E and pilot assignment commands for MEK-RPG`
- Priority: `High`

## Goal

Expose source-owned live API read context and command readiness selector detail for pilot assignment and TO&E edits.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- Design note from issue `#72` once available
- Source audit from issue `#71`

## Expected Output

- MekHQ source changes exposing person, unit, crew, assignment, force tree, and blocker facts needed to build safe guarded requests.
- Updated docs and fixtures where practical.
- Verification results or exact blockers.

## Files And Areas

Likely files to read or edit:

- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCampaignStateExporter.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCommandReadinessExporter.java`
- `external/src/mekhq/MekHQ/test`
- `docs/current/`
- `docs/templates/mekhq-live-campaign-*.fixture.json`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
.\gradlew.bat --no-daemon :MekHQ:test --tests mekhq.service.LocalCampaignStateExporterTest --tests mekhq.service.LocalCommandReadinessExporterTest
.\gradlew.bat --no-daemon :MekHQ:compileJava :MekHQ:checkstyleMain
```

Run source commands from `external/src/mekhq`.

## Constraints

- Do not imply mutating support before endpoints exist.
- Use machine-readable blocked/deferred readiness reasons for unavailable selector detail.
- Keep expensive collectors bounded.
- Do not edit campaign saves.

## Acceptance Criteria

- Personnel selector rows expose stable ids, roles, status, availability, assignment facts, and eligibility hints where source-backed.
- Unit selector rows expose stable ids, type/status, crew slots/current occupants, force location, and assignment blockers.
- TO&E selector rows expose force ids, parent/child structure, names, unit membership, and mutation blockers.
- Readiness exposes the command family as available, blocked, or selector-detail-deferred with guard requirements.
- Tests/fixtures or documented blockers cover the new read surface.

## Open Questions

- Which eligibility hints can be source-confirmed without duplicating full MekHQ validation?
- Should force tree output live in `/campaign/state`, `/campaign/commands`, or both?
