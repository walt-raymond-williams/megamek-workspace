# Agent Handoff: Prototype Battle-Record MUL Round Trip

## Issue

- GitHub issue: `#10`
- Roadmap entry: `Epic: Robust tabletop battle result MUL workflow`
- Priority: `High`

## Goal

Prove that a generated or minimally edited battle-record MUL can be imported through MekHQ Resolve Manually and produces the expected campaign-facing effects without overwriting campaign saves.

## Required Context

Read these first:

- `AGENTS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`
- `docs/current/MEGAMEK_PROJECT_PROFILE.md`
- `docs/current/KNOWN_COMMANDS.md`
- `docs/handoffs/active/robust-tabletop-battle-result-mul-workflow.md`
- `docs/handoffs/archive/confirm-battle-record-mul-source-workflow.md`
- `docs/handoffs/archive/define-tabletop-result-input-schema.md`

## Expected Output

- A documented round-trip procedure under `docs/current/` or `analysis/` notes.
- Input scenario MUL, generated battle-record MUL, MekHQ import steps, expected effects, and observed effects for disposable data.
- Notes on which validations can be automated and which require UI/manual checks.

## Files And Areas

- `analysis/tmp/`
- `analysis/`
- `docs/current/`
- `external/installs/MekHQ-0.51.00`

## Commands

```powershell
git status --short --branch
Test-Path 'C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\MekHQ.exe'
```

## Constraints

- Do not overwrite active campaign saves.
- Use copies or disposable campaign/scenario data under `analysis/tmp/`.
- Record exact manual MekHQ UI steps if UI verification is required.

## Acceptance Criteria

- A generated or controlled battle-record MUL is accepted by MekHQ's manual resolution path.
- Observed campaign effects are compared against expected effects.
- Any mismatch is documented with likely source locations or follow-up issues.

## Open Questions

- Can the first round trip use installed jars only, or does it need source build support?
- Which campaign fixture should be used for destructive result-import testing?
