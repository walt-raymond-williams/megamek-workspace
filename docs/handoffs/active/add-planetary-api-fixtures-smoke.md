# Agent Handoff

## Issue

- GitHub issue: `#87`
- Roadmap entry: `Epic: Expose planetary information in the MekHQ API`
- Priority: `Medium`

## Goal

Finalize consumer-facing documentation and fixtures for the planetary information API, and define or run a safe live smoke test.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/PLANETARY_INFORMATION_API_TRACKING.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`
- `docs/current/MEK_RPG_LIVE_MEKHQ_PLANETARY_API_DESIGN.md`
- Implementation notes from issue `#86`

## Expected Output

- Updated consumer-facing API contract.
- Sanitized fixture JSON under `docs/templates/`.
- Repeatable command examples in `docs/current/KNOWN_COMMANDS.md`.
- Live smoke checklist or recorded live smoke result against safe data.

## Files And Areas

Likely files to read or edit:

- `docs/current/MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md`
- `docs/current/KNOWN_COMMANDS.md`
- `docs/current/PLANETARY_INFORMATION_API_TRACKING.md`
- `docs/templates/`
- `tools/start-mekhq-control-api.ps1`

## Commands

Useful commands or checks:

```powershell
git status --short --branch
powershell -NoProfile -ExecutionPolicy Bypass -File tools\start-mekhq-control-api.ps1
Invoke-RestMethod -Method Get -Uri 'http://127.0.0.1:32180/status' -TimeoutSec 5
Invoke-RestMethod -Method Get -Uri 'http://127.0.0.1:32180/<planetary-endpoint>?planetName=<name>' -TimeoutSec 15
```

## Constraints

- Do not include unrelated user changes.
- Preserve uncertainty and evidence labels.
- Use copied/disposable campaign data for live validation.
- If live GUI testing requires user presence, record the exact blocker instead of forcing it.
- Commit completed repository changes before stopping unless explicitly told not to.

## Acceptance Criteria

- `MEK_RPG_LIVE_MEKHQ_API_CONTRACT.md` lists the new endpoint, query parameters, response conventions, limitations, and timeout guidance.
- Sanitized fixture JSON represents a successful planet lookup and at least one error/ambiguity shape if useful.
- `KNOWN_COMMANDS.md` includes repeatable `Invoke-RestMethod` examples.
- Live smoke checklist uses source-built MekHQ with control API enabled and a safe loaded campaign, or records why live smoke is blocked.
- Roadmap, tracking doc, and task board are updated and changes are committed.

## Open Questions

- Which planet/system should be used for the canonical sanitized fixture?
- Should live smoke wait for the user's real-life campaign, or use the demo/practice campaign first?
