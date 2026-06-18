# Documentation Workflow

This workspace depends on documentation as operational memory. Update docs when a discovery will change how a future agent should inspect, interpret, automate, or advise on the campaign.

## Documentation Roles

- `AGENTS.md`: agent instructions, safety posture, project rules, and required workflows.
- `README.md`: human quick start and workspace orientation.
- `docs/current/`: current durable knowledge that should guide future sessions.
- `docs/current/TASKS.md`: current work board for active tasks, queued work, blocked items, and recently completed work.
- `docs/templates/`: reusable report formats and briefing structures.
- `campaigns/`: local campaign inputs and deliberate campaign snapshots.
- `analysis/`: generated extracts, scratch work, and temporary parsing output.
- `external/`: ignored local payload for installs, downloads, and source checkouts.

Prefer one current source of truth for each fact. If a fact belongs in a more specific document, link to that document instead of duplicating it in multiple places.

## When To Update Docs

Update `docs/current/` when any of the following become clear:

- the active campaign save path, campaign identity, contract, or enabled campaign systems
- the meaning of a MekHQ campaign field, scenario field, unit field, or report value
- the source class or method responsible for a campaign action or UI behavior
- a repeatable command, parser, extraction method, or verification step
- a BattleTech or MekHQ mechanic that materially changes campaign advice
- a known risk, open question, or assumption that future agents should not rediscover
- a task starts, finishes, becomes blocked, or changes priority in a way future agents need to see

Do not record every temporary observation. Record discoveries that are likely to matter again.

## Where Updates Go

- Use `ACTIVE_CAMPAIGN.md` for the current campaign file, identity, enabled systems, priorities, and unresolved campaign-specific questions.
- Use `TASKS.md` for active work, near-term queue, backlog, blocked items, and recently completed work.
- Use `KNOWN_COMMANDS.md` for commands that are safe, repeatable, and useful across sessions.
- Use `SOURCE_CHANGE_WORKFLOW.md` for source modification, build/test, dirty-worktree, and documentation follow-through rules.
- Use `SAVE_FORMAT_NOTES.md` for confirmed or suspected MekHQ save structure, field meanings, and source references.
- Use `DATA_MAP.md` for local data locations and durable format references.
- Use `SOURCE_CODE_GUIDE.md` for source search strategy and important source entry points.
- Use `BATTLETECH_CONTEXT.md` for reusable game concepts and mechanics that affect interpretation.
- Use `CAMPAIGN_ANALYSIS_WORKFLOW.md` for the analysis sequence used when reading campaign or scenario state.

If no existing file fits, create a focused document under `docs/current/` with a narrow title and add a link from `WORKSPACE.md` or the closest relevant current doc.

## Evidence Labels

Use explicit evidence labels for save fields, mechanics, and behavior:

- `Confirmed from save`: directly observed in a campaign or scenario file.
- `Confirmed from source`: verified in local MegaMek, MekHQ, MegaMekLab, or mm-data source.
- `Confirmed from local docs`: verified in installed suite docs.
- `Confirmed by user`: stated by the user as campaign fact or preference.
- `Inferred`: a reasonable conclusion from available evidence, but not yet confirmed.
- `Unknown`: intentionally tracked gap.

When a conclusion relies on source code, cite the class or file path. When a conclusion relies on a save file, identify the save file and version if available.

## Update Sequence

1. Identify the durable discovery.
2. Choose the narrowest current doc that owns that fact.
3. Add or update the note with an evidence label.
4. Update `TASKS.md` if work status changed.
5. Preserve uncertainty instead of smoothing it away.
6. Remove or update stale contradictions in nearby docs.
7. If the change affects agent behavior, update `AGENTS.md`.
8. If the change affects human onboarding, update `README.md`.
9. Run a consistency pass before committing.

## Consistency Pass

Before committing documentation updates, check:

- paths are correct for this workspace
- version strings are intentional, especially `0.51.0` versus `0.51.00`
- `external/` is still treated as ignored local payload
- raw campaign saves are not accidentally staged
- open questions are still accurate
- duplicated facts do not disagree
- source-backed claims include enough file/class context to be found again

Useful command:

```powershell
git status --short
```

## Commit Posture

Documentation commits should be small enough to explain. A good commit message describes the operational improvement, for example:

```text
Establish MegaMek campaign workspace documentation
```

Do not commit ignored local payload, temporary extracts, or raw campaign saves unless the user explicitly asks for a campaign snapshot.
