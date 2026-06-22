# Agent Handoff

## Issue

- GitHub issue: `#32`
- Roadmap entry: `Harden jar-backed MekHQ checkpoint exporter output against MEK-RPG feedback`
- Priority: `Medium`

## Goal

Improve the workspace jar-backed read-only checkpoint exporter and repeatable checks so its output better matches MEK-RPG's completed consumed-field mapping and warning policy.

## Result

Completed on `2026-06-22`.

- Hardened `campaign.location.current_location` from an object-string value into stable display/id fields with transit and source-owner metadata.
- Added method-backed core `Contract` getter terms for `Contract` missions, including command rights, salvage, support, transport, advance, payout, and travel fields.
- Preserved market offers as display/opportunity-only and kept automation-blocking `unsupported` entries.
- Added repeatable smoke check `tools/mekhq-checkpoint-exporter/test-mekhq-checkpoint-exporter.ps1`.
- Updated prototype, schema, known-command, roadmap, and task docs.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/SOURCE_CHANGE_WORKFLOW.md`
- `docs/current/KNOWN_COMMANDS.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT_SCHEMA.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORTER_PROTOTYPE.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_VALIDATION.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_CHECKPOINT_CONSUMED_FIELD_MAPPING.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_CHECKPOINT_WARNING_SURFACING.md`

## Expected Output

- Prototype `campaign.location` output uses stable display/id fields instead of object-string values where practical.
- Active contract terms are deepened through method-backed `Contract` getters where practical.
- Existing trust-boundary fields remain present.
- Market offers remain display/opportunity-only, with selector warnings and `unsupported` entries preserved.
- A repeatable smoke check validates fixture/prototype JSON shape or documents the exact blocker.
- Prototype notes and known commands are updated.

## Files And Areas

Likely files to read or edit:

- `tools/mekhq-checkpoint-exporter/MekHqCheckpointExporter.java`
- `tools/mekhq-checkpoint-exporter/run-mekhq-checkpoint-exporter.ps1`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORTER_PROTOTYPE.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT_SCHEMA.md`
- `docs/current/KNOWN_COMMANDS.md`
- possibly `docs/templates/mekhq-read-only-checkpoint.fixture.json` only if fixture guidance changes

## Commands

Useful commands or checks:

```powershell
git status --short --branch
$save = 'C:\Users\waltr\Documents\megamek-workspace\analysis\tmp\issue-22\Autosave-1-The Learning Ropes-30250720.cpnx.gz'
$json = powershell -ExecutionPolicy Bypass -File tools\mekhq-checkpoint-exporter\run-mekhq-checkpoint-exporter.ps1 $save
$parsed = $json | ConvertFrom-Json
git diff --check
```

If the copied disposable save is absent, either recreate a safe copied disposable save or record the blocker.

## Constraints

- Read-only only. Do not mutate `.cpnx`, `.cpnx.gz`, XML, or campaign state.
- Do not add write-side commands.
- Do not rely on source build verification unless the Java/Gradle blocker is resolved.
- Use installed jars or source inspection as appropriate.
- Preserve warnings/unsupported rather than guessing unsafe values.
- Commit and push completed work before closing the issue.

## Acceptance Criteria

- Prototype output shape addresses MEK-RPG's key hardening feedback where practical.
- Any unimplemented feedback is explicitly documented with reason and next owner.
- Smoke verification ran successfully or a precise blocker is recorded.
- No real saves, raw XML payloads, or ignored local payload are committed.
- Changes are committed and pushed.

## Open Questions

- Which `Contract` getter set is sufficient for the first hardened output?
- Is a workspace smoke script enough, or should this wait for a MekHQ-source test after production ownership is decided?
