# Source Change Workflow

Use this workflow when the user asks to modify MegaMek, MekHQ, MegaMekLab, or mm-data source code.

## Purpose

Source code is local reference and may become an implementation target. Source edits belong in the relevant source checkout under `external/src/`; this workspace records task state, campaign-facing analysis, verification notes, and durable discoveries.

## Before Editing

1. Read `TASKS.md` and move the source task into `Now`.
2. Identify the target source repo:
   - `external/src/megamek`: tactical engine, game UI, scenario loading, unit loaders, combat rules, bots, maps.
   - `external/src/mekhq`: campaign app, campaign persistence, personnel, contracts, repairs, finances, markets, StratCon/AtB systems.
   - `external/src/megameklab`: construction, validation, record sheets, unit-building UI.
   - `external/src/mm-data`: unit files, maps, universe data, images, random assignment data, shared data payload.
3. Check the target repo state:

```powershell
git status --short --branch
```

4. Read nearby code and existing tests before editing.
5. Confirm whether the work affects campaign control, save parsing, UI behavior, or player-facing advice. If yes, plan the matching docs update in this workspace.

## Dirty Worktree Rules

- Do not revert or overwrite changes you did not make.
- If unrelated files are dirty, ignore them.
- If files needed for the task are dirty, inspect them and work with the existing changes.
- If the existing changes make the task unsafe or ambiguous, stop and ask the user.
- Never use destructive Git commands unless the user explicitly asks for them.

## Implementation Rules

- Keep source edits narrowly scoped to the requested behavior.
- Prefer existing project patterns, helpers, and tests.
- Do not move campaign saves or runtime payload into the source repos.
- Do not use this workspace repo for source patches; use the target source checkout.
- If a source change reveals a reusable mechanic, file format, command, or control path, record it in `docs/current/`.

## Build And Test Verification

Use the target repo's Gradle wrapper on Windows. See `KNOWN_COMMANDS.md` for the current command list and environment notes.

At minimum, a source change should record:

- target repo
- branch and dirty state before editing
- files changed
- build/test command attempted
- command result
- known residual risk if verification did not fully run

Current known environment issue:

- `Confirmed locally`: the shell currently resolves `java` and `javac` to Java 8.
- `Confirmed locally`: JDK 21 is installed at `C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot`.
- `Confirmed from source`: the checked-in Gradle build files use Java toolchain 21.
- `Confirmed locally`: Gradle wrapper task discovery currently fails because `gradle/gradle-daemon-jvm.properties` requests daemon `toolchainVersion=17`, and no local JDK 17 or toolchain download URL is configured.

Until that environment issue is resolved, mark Gradle build/test commands as blocked rather than verified.

## Documentation Follow-Through

After source investigation or source edits:

- Update `SOURCE_CODE_GUIDE.md` for important source entry points.
- Update `KNOWN_COMMANDS.md` for verified commands or known blockers.
- Update `SAVE_FORMAT_NOTES.md` for campaign save field meanings or load/save behavior.
- Update `BATTLETECH_CONTEXT.md` for durable mechanics that affect interpretation.
- Update `TASKS.md` when the work finishes, blocks, or changes scope.

Use evidence labels from `DOCUMENTATION_WORKFLOW.md`.

## Commit Posture

There may be two separate commits:

- source repo commit: only if the user wants source changes committed in the target repo
- workspace commit: documentation, reports, task-board updates, and durable notes

Do not mix source checkout changes into this workspace repo. `external/` is ignored here and should stay ignored.

## Open Questions

- Should this workspace standardize on installing JDK 17 for Gradle daemon compatibility, or updating/removing generated daemon JVM settings in the source checkouts?
- Which Gradle commands should be treated as the default verification set once local execution is unblocked?
