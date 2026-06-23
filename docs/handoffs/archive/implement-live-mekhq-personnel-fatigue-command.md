# Agent Handoff

## Issue

- GitHub issue: `#53`
- Roadmap entry: `Implement guarded live MekHQ personnel fatigue command`
- Priority: `High`
- Parent epic: `#44`
- Design source: GitHub issue `#48`

## Goal

Implement `POST /campaign/command/personnel/fatigue` as the first safe medical-adjacent command for MEK-RPG.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_MEDICAL_COMMAND_DESIGN.md`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCommandReadinessExporter.java`
- `external/src/mekhq/MekHQ/src/mekhq/campaign/personnel/Person.java`

## Expected Output

- `POST /campaign/command/personnel/fatigue` in the local MekHQ control API.
- `GET /campaign/commands` reports `personnel.fatigue` readiness when fatigue rules and person selectors are safe.
- Apply mode calls `Person#changeFatigue(delta)`, not `setFatigue(...)`.
- Dry-run reports intended before/after facts without mutating.
- Apply mode optionally appends a plain-text MEK-RPG audit report.

## Files And Areas

Likely source files:

- `external/src/mekhq/MekHQ/src/mekhq/service/LocalControlService.java`
- `external/src/mekhq/MekHQ/src/mekhq/service/LocalCommandReadinessExporter.java`
- nearby local API tests or fixtures if present

Likely workspace files:

- `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_MEDICAL_COMMAND_DESIGN.md`
- `docs/current/GUARDED_LIVE_MEKHQ_COMMAND_API_TRACKING.md`
- `docs/current/TASKS.md`
- `docs/current/ROADMAP.md`

## Constraints

- Do not implement injury healing, prosthetic surgery, medical expenses, permanent fatigue mutation, day advancement, or personnel status changes in this issue.
- Refuse if campaign fatigue rules are disabled unless a later GM-correction command explicitly supports direct set behavior.
- Use `promptPolicy=refuse_if_prompt`.
- Keep save-after-success opt-in.
- Preserve the existing command envelope, idempotency, dry-run, prompt, and save patterns from status-note and personnel-status commands.

## Acceptance Criteria

- Request validation covers campaign identity/date, person id/name/status/prisoner/unit guards, expected raw fatigue, expected adjusted fatigue, expected permanent fatigue, `dryRun`, `idempotencyKey`, prompt policy, save policy, and plain-text audit reason/source.
- Refusal rules cover stale target facts, disabled fatigue rules, permanent-fatigue mutation, mixed medical/prosthetic/expense/status effects, invalid prompt policy, and unsupported save policy.
- Response includes before/after raw fatigue, adjusted fatigue, permanent fatigue, campaign option facts, status/prisoner/unit facts, injury/prosthetic counts, report counts, prompt facts, save facts, and audit context.
- `.\gradlew.bat :MekHQ:compileJava` passes from `external/src/mekhq`.
- `.\gradlew.bat :MekHQ:checkstyleMain` passes from `external/src/mekhq`.
- Workspace docs record verification and any live disposable-campaign smoke-test blocker.
