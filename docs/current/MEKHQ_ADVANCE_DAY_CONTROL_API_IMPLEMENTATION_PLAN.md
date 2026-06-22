# MekHQ Advance Day Control API Implementation Plan

## Purpose

This plan turns the issue `#34` spike into an implementation roadmap for issue `#35`: expose a local-only API or command seam in the running MekHQ GUI app so Codex can request exactly one real Advance Day action without screen-coordinate clicking or direct save editing.

## Working Decision

`Inferred from source`: the first implementation should be an in-process local control endpoint attached to the running MekHQ app, not a separate headless process. MekHQ must already be open and a campaign must already be loaded.

The command should call `Campaign#newDay()` directly, or a small new wrapper around it, because `CampaignController#advanceDay()` currently returns `void` and loses the success/cancel signal.

## Recommended First API Shape

Transport options to investigate in order:

1. Localhost HTTP endpoint bound only to `127.0.0.1`.
   - Pros: easy for Codex, scripts, and future tools to call; easy JSON request/response contract; good fit for explicit status results.
   - Cons: adds a small local server surface to MekHQ; needs careful disable-by-default or local-only gating.
2. Loopback socket with a simple JSON line protocol.
   - Pros: smaller dependency surface than HTTP; still callable from scripts.
   - Cons: more custom client code and less convenient debugging.
3. Local command/drop-file queue watched by MekHQ.
   - Pros: simple and avoids a listening network socket.
   - Cons: more brittle for locking, response delivery, stale files, and user confusion.
4. JMX/debug hook.
   - Pros: JVM-native.
   - Cons: awkward for casual Codex/tooling use and likely more setup than this personal workflow needs.

Recommendation: start with disabled-by-default localhost HTTP on `127.0.0.1` if the source dependencies make that lightweight. Fall back to a loopback socket if HTTP would introduce too much framework or packaging work.

## Command Contract

Request:

```json
{
  "command": "advanceDayOnce",
  "expectedCampaignName": "The Learning Ropes",
  "expectedDate": "3025-07-20",
  "saveAfterSuccess": false,
  "savePath": null
}
```

Response:

```json
{
  "status": "advanced",
  "campaignName": "The Learning Ropes",
  "dateBefore": "3025-07-20",
  "dateAfter": "3025-07-21",
  "newDayReturned": true,
  "saveAttempted": false,
  "saveSucceeded": null,
  "message": "Advanced exactly one day."
}
```

Allowed statuses:

- `advanced`: `Campaign#newDay()` returned true and date advanced by exactly one day.
- `blocked`: `Campaign#newDay()` returned false or a user prompt/blocker stopped progression.
- `failed`: an exception or unexpected state occurred.
- `refused`: preflight checks failed, such as no campaign loaded or expected name/date mismatch.

## Guardrails

- Bind only to loopback or use a purely local mechanism.
- Keep disabled by default unless a local development option or launch flag enables it.
- Require a campaign to be loaded.
- Require expected date for the first prototype.
- Allow expected campaign name or id check.
- Execute on the Swing event dispatch thread.
- Invoke exactly one day advance per command.
- Do not auto-answer dialogs.
- Default to no save.
- Save only to an explicit caller-provided disposable path after confirmed success.
- Return structured output instead of relying on log text.

## Implementation Phases

### Phase 1: Source Recon

- Search for existing MekHQ startup, local service, command, debug, HTTP, socket, or IPC patterns.
- Identify the best place to own a local-only development control service.
- Confirm how to access the active `CampaignGUI`, `Campaign`, frame, and campaign file path from that owner.

### Phase 2: Minimal Command Wrapper

- Add an `advanceDayOnce` command method that:
  - captures campaign name/id and date before
  - validates expected campaign/date input
  - runs `Campaign#newDay()` on the Swing event dispatch thread
  - captures date after
  - returns `advanced`, `blocked`, `failed`, or `refused`

### Phase 3: Optional Save Hook

- If `saveAfterSuccess` is true, require an explicit path.
- Refuse missing or unsafe-looking paths.
- Call existing `CampaignGUI.saveCampaign(...)` rather than writing XML directly.
- Include save result in the response.

### Phase 4: Build And Static Verification

- Re-check the MekHQ source worktree state before edits.
- Try the relevant Gradle command from `KNOWN_COMMANDS.md`.
- If Gradle remains blocked by Java/toolchain state, record the exact failure and keep source changes small enough for later live verification.

### Phase 5: User-Assisted Live Test

Do this only when the user is present:

- launch the locally modified MekHQ build
- open a copied/disposable campaign save
- call the local control API with expected campaign/date and no save
- confirm exactly one date advance in the UI
- repeat against a known blocker/prompt scenario if available
- test explicit save-after-success only to a disposable output path

## Relationship To Other Tickets

Issue `#35` should not close or replace issues `#10`, `#13`, `#17`, `#21`, or `#23`.

If the Advance Day control API works, create follow-up tickets for other seams:

- Resolve Manually / battle-record MUL import command seam
- GM roster add/remove command seam
- read-only checkpoint export integration with running MekHQ app

Each follow-up should keep the same posture: loaded-app identity checks, source-owned MekHQ logic, no coordinate clicking, no direct save mutation, and user-approved prompt policy.

## Open Questions

- What launch flag or config option should enable the local control endpoint?
- Should the endpoint be packaged only in a local branch or made generally available but disabled by default?
- What is the safest rule for allowed save paths?
- Can modal dialog detection be useful without becoming prompt auto-answering?
