# MEK-RPG Live MekHQ Command API Strategy

Status: command envelope, readiness discovery, guarded status-note command, guarded personnel status command, medical/prosthetic source design, guarded personnel fatigue command, unit-market purchase source design, and guarded unit-market purchase command completed through issue `#54` on `2026-06-23`.

Purpose: record the strategy shift from read-only live state toward narrowly scoped MekHQ-owned commands that mutate the already-loaded campaign through MekHQ logic, not through save-file edits.

## Strategy Shift

`Confirmed locally`: the local MekHQ control API has already proven one write-side command by advancing the loaded GUI campaign exactly one day through `Campaign#newDay()` with campaign/date guard fields.

`Decision`: future MEK-RPG command work should continue using the running MekHQ GUI app as the campaign authority. MEK-RPG may request actions, but MekHQ must own validation, side effects, reports, finances, market updates, unit/personnel changes, and save behavior.

This means command endpoints should:

- stay disabled by default and loopback-only while prototyping
- require loaded-campaign guard fields such as campaign id/name, campaign date, and expected state
- run on the Swing event dispatch thread when touching GUI-owned state
- prefer existing MekHQ controller, dialog, or domain workflows over recreating business logic
- refuse when a stable selector, precondition, or prompt policy is missing
- return before/after facts, warnings, visible prompt/dialog state, and whether a save was attempted
- avoid direct `.cpnx`, `.cpnx.gz`, or XML writes from MEK-RPG

## Workstream Shape

`Decision`: track guarded command API work as its own epic, not as more read-only live API cleanup.

The write-side workstream should be split by MekHQ domain because each mutation has different selectors, prompt behavior, side effects, and verification needs. MEK-RPG should send high-level intent, while MekHQ applies campaign consequences through source-owned workflows.

Core child tracks:

- Command envelope, prompt policy, idempotency, dry-run, save policy, and before/after response format.
- Command readiness and selector discovery so MEK-RPG knows which actions can be offered safely.
- Campaign status/note mutation as the smallest likely non-day-advance write.
- Unit-market purchase, including DropShip purchase, after stable offer selectors are available.
- Personnel death/status changes for RPG events that happen outside MekHQ scenario resolution.
- Medical treatment and prosthetic changes for RPG-side recovery, surgery, or equipment events.
- Later command families for contract decisions, personnel hiring, GM funds corrections, repair/procurement execution, and tactical result automation.

## Common Command Envelope

`Decision`: every mutating endpoint should use a shared request/response envelope unless a later source implementation issue records a narrower exception. This contract is intentionally more explicit than the existing `/advance-day` prototype so future endpoints do not quietly drift on retry, prompt, save, or campaign-identity behavior.

### Request Fields

Every mutating request should require:

- `command`: exact command name/version.
- `commandVersion`: integer or semantic version for request-shape compatibility.
- `idempotencyKey`: MEK-RPG-generated key for safe retry detection; required for all non-read-only commands.
- `expectedCampaignId` or `expectedCampaignName`; prefer id when MEK-RPG has already read it from the live API.
- `expectedDate`: current MekHQ campaign date.
- `expectedStateRevision`: preferred once command readiness exposes a state revision; until then commands must use target-specific guard fields.
- target selectors and guard fields, such as person id, unit id, offer selector, expected status, expected balance, expected price, expected injury state, or expected market membership.
- `dryRun`: required field for every endpoint; endpoints may refuse with `dry_run_unsupported` only when source behavior cannot be validated safely without mutation.
- `saveAfterSuccess`: explicit save policy; default must remain false while prototyping.
- `savePath`: required only when `saveAfterSuccess` is true; use MekHQ's save path, not raw XML writing.
- `promptPolicy`: explicit policy for UI prompts, using values such as `refuse_if_prompt`, `suppress_known_safe_nags`, or a later endpoint-specific policy.
- `clientContext`: MEK-RPG source/action reference for audit reports, including actor label, source scene/session id when available, and user-facing reason.

For high-risk commands, require additional before-state guards. Examples:

- funds commands: expected balance and transaction type
- unit-market purchases: offer selector, expected unit display name/type, expected price, expected transit/delivery behavior, and expected current balance
- personnel status commands: person id, expected name, expected current status, expected unit assignment, and RPG event source
- medical commands: person id, expected injury/fatigue/prosthetic state, treatment type, cost policy, and active medical-system assumptions

### Response Fields

Responses should include:

- `status`: one of `applied`, `dry_run`, `refused`, `blocked`, or `failed`; legacy prototype statuses such as `advanced` should be treated as command-specific aliases, not the long-term shared shape.
- `statusReason`: machine-readable code such as `wrong_campaign`, `stale_state`, `missing_selector`, `prompt_required`, `dry_run_unsupported`, or `source_exception`.
- `message`: short user-facing explanation.
- `command`, `commandVersion`, and `idempotencyKey`.
- before/after campaign date, campaign name/id, and state revision when available.
- before/after target facts for the selected person, unit, market offer, contract, transaction, or medical state.
- side effects applied or side effects that would be applied during dry-run.
- reports, transactions, units, personnel, contracts, market offers, or medical entries created, removed, or changed.
- `warnings` and `unsupported` arrays using the read-only live API style where possible.
- prompt/dialog facts: policy requested, prompts suppressed, prompts detected, prompt refusal reason, and final visible dialog count.
- save result: whether save was requested, attempted, path, success, and failure message.
- audit context echoed from `clientContext` after validation/sanitization.

### Idempotency

`Decision`: implement idempotency in memory first, scoped to the live MekHQ process and command name. Do not persist idempotency keys into campaign saves or reports until there is a specific source-backed reason.

Minimum behavior:

- If the same `idempotencyKey` repeats while the original command is still running, return `blocked` with a duplicate-running reason.
- If the same key repeats after a successful command in the same process and the command/target guards match, return the cached command result without mutating again when feasible.
- If the same key repeats with different command text, target selector, or guard fields, return `refused` for idempotency-key conflict.
- Idempotency cache lifetime may be process-local and bounded; the response must disclose when retry memory is process-local.
- Commands must still require before-state guards because a restarted MekHQ process will not remember earlier keys.

### Dry-Run

`Decision`: `dryRun` must be present for every mutating command request so callers make mutation intent explicit. Support is endpoint-specific:

- For simple validation-only commands, dry-run should validate guards and report intended side effects without mutation.
- For commands whose source-owned workflow cannot preview side effects safely, return `refused` with `dry_run_unsupported` and explain the missing preview path.
- A command that supports dry-run must use the same validation path as apply mode where practical, then stop before mutation.
- Dry-run responses must not create reports, transactions, market removals, unit/personnel changes, saves, or prompt side effects.

### Save Policy

`Decision`: save-after-success remains explicit and opt-in for all guarded commands while this is a local prototype.

Rules:

- `saveAfterSuccess` defaults to false.
- `savePath` is required when `saveAfterSuccess` is true.
- Saving must use MekHQ's source-owned save path, such as `CampaignGUI.saveCampaign(...)`, not direct XML or gzip writes from MEK-RPG.
- Failed, refused, blocked, or dry-run commands must not save.
- A command response must say whether a save was requested, attempted, succeeded, failed, or skipped.

### Prompt And Dialog Policy

`Confirmed from local prototype`: `/advance-day` originally reported only final visible dialog count, which missed dialogs the user manually dismissed during the command. `dismissAdvanceDayNags=true` later added a narrow source-owned nag suppression path through `NagController.withDailyNagsSuppressed(...)`.

`Decision`: future commands should default to `promptPolicy=refuse_if_prompt` unless the implementation has a source-confirmed, endpoint-specific way to suppress or answer only known-safe prompts.

Prompt rules:

- Do not auto-answer arbitrary Swing dialogs.
- Do not proceed when a command would require a confirmation, faction, rental, medical, market, save, or other UI prompt unless the endpoint has an explicit source-backed prompt policy.
- If a known safe prompt is suppressed, report its category and count in the response.
- If an unexpected prompt is detected, return `blocked` or `failed` and leave enough detail for user/manual follow-up.
- Track prompt behavior during the command, not just final visible dialog count, whenever source implementation can do so.

### Reusable Implementation Acceptance Criteria

Every future command implementation issue under epic `#44` should prove or explicitly refuse these points:

- API remains disabled by default and loopback-only.
- Mutating work runs on the Swing event dispatch thread when it touches GUI-owned campaign state.
- Only one mutating command can run at a time unless a later source design proves safe concurrency.
- Request validates campaign identity, date, state/target guards, command version, idempotency key, dry-run intent, save policy, prompt policy, and client audit context.
- Response uses the shared statuses and includes before/after facts, side effects, warnings/unsupported blockers, prompt facts, save facts, and audit context.
- Dry-run either works without mutation or refuses explicitly.
- Save-after-success is opt-in, never happens for dry-run/refused/blocked/failed commands, and uses MekHQ save logic.
- Selectors are stable enough for the command scope; otherwise the command is blocked until `GET /campaign/commands` or equivalent can expose safe selectors.
- Verification includes at least source inspection and compile/checkstyle where source changes occur; live mutation tests must use copied or disposable campaign saves.

## Candidate Command Classes

### Campaign Status / GM Note

Near-term value: high.

Risk: low to medium, depending on where the note/status is stored.

Shape:

- Add a MEK-RPG-visible campaign status marker, note, or report entry through a source-owned MekHQ path.
- Implemented endpoint shape: `POST /campaign/command/status-note`.
- Guard with campaign id/name/date and a client-provided idempotency key.
- Return the created report/note text, report category, and new state revision.

Why this is an easy win:

- It gives MEK-RPG a first non-day-advance mutation that does not require market selectors, unit creation, salvage, contract prompts, or crew assignment.
- It helps prove command envelope, idempotency, response shape, and audit/report behavior before automating purchases.

Open source question:

`Confirmed from source`: `Campaign#addReport(DailyReportType, String)` appends report text through MekHQ's report path, honors unified/historical daily log options, updates new-report buffers, and current report lines are serialized as CDATA in `Campaign#writeToXML(...)`.

Implemented result:

- Issue `#50` implemented `POST /campaign/command/status-note` in local MekHQ source commit `4429d99ea2`.
- V1 uses `Campaign#addReport(DailyReportType.GENERAL, ...)` as the source-owned storage target.
- V1 accepts only `GENERAL` or an omitted report category, requires plain-text `noteText` and `clientContext`, rejects HTML, supports dry-run, blocks visible dialogs under `promptPolicy=refuse_if_prompt`, and supports process-local idempotency for applied commands.
- The command proves the shared envelope direction, opt-in save behavior, before/after report counts, and readiness update before higher-risk mutation endpoints.

### Command Readiness / Selector Discovery

Near-term value: high.

Risk: low.

Shape:

- Implemented endpoint shape: `GET /campaign/commands`.
- The endpoint says which command types are currently available or blocked and why blocked command types are blocked.
- It exposes stable command selector candidates only when MekHQ can produce them safely.

Why this is an easy win:

- MEK-RPG can light up or hide action buttons without guessing from display-only market rows.
- It creates the missing bridge between read-only context and future mutation endpoints.

Implemented result:

- `advanceDayOnce` is reported as available, using the legacy `/advance-day` prototype.
- Campaign/person/unit/applicant/contract selector candidates are exposed from source-backed ids and must still be paired with command-specific guard fields.
- `campaign.status_note` and `personnel.status` are now reported as available; funds adjustment, medical treatment, contract acceptance, personnel hire, unit purchase, repair/procurement, and standalone save remain blocked with machine-readable reason codes.
- Unit-market purchase remains blocked with `stable_offer_selector_missing`; `UnitMarketOffer#writeToXML()` serializes no unique stable offer id, so MEK-RPG must not select offers by display name or row index.

Remaining source question:

- Decide whether future command-specific selectors should be opaque live-session tokens tied to `state_revision`, durable ids that survive save/reload, or a mix by domain. Current readiness output treats selectors as live-session command candidates unless a command row states otherwise.

### GM Funds Adjustment

Near-term value: medium.

Risk: medium.

Source-backed candidate:

- `Campaign#addFunds(TransactionType, Money, String)` is used by `FinancesTab#addFundsActionPerformed()`.

Shape:

- Possible endpoint shape: `POST /campaign/command/adjust-funds`.
- Restrict to explicit GM/manual-correction usage, with a reason, transaction type, amount, expected balance, and campaign/date guard fields.
- Return before/after balance and created transaction details.

Why this is an easy technical win:

- The source path is simple and already exists as a UI action.

Why it should not be the first gameplay command:

- RPG events such as buying a DropShip should not be represented as a loose funds adjustment. The purchase command should debit funds as part of a higher-level MekHQ unit acquisition workflow.

### Contract Market Decision

Near-term value: medium to high.

Risk: medium to high.

Source-backed candidate:

- `ContractMarketDialog#acceptContract(...)` credits advance/transport funds, calls `campaign.addMission(selectedContract)`, invokes `selectedContract.acceptContract(campaign)`, handles faction/prompt work, and removes the contract-market offer.
- `AbstractContractMarket#getContracts()` provides contract objects; contract ids are more promising selectors than unit-market display rows.

Shape:

- Possible endpoint shape: `POST /campaign/command/contracts/accept`.
- Required selector: contract id plus expected name, employer, enemy, start date, and payment guard fields.
- Refuse if the contract path would require unresolved UI prompts or a prompt policy not implemented by the API.

Why this may be a good early real workflow:

- Contract offers are strategic MEK-RPG-facing decisions.
- Contract ids appear more stable than unit market offer selectors.

Blocker:

- Prompt policy needs source review. Contract acceptance can open confirmation, start, rental, and faction-standing dialogs.

### Personnel Market Hire

Near-term value: medium.

Risk: medium.

Source-backed candidate:

- `PersonnelMarketDialog` calls `Campaign#recruitPerson(...)`.
- Hiring can debit recruitment costs, handle attached entities, remove the applicant from the market, and update roster state.

Shape:

- Possible endpoint shape: `POST /campaign/command/personnel/hire`.
- Required selector: market applicant person id plus expected name, role, cost, and market membership.

Blocker:

- Legacy and new personnel markets differ. The first command needs to pick one market style or refuse unsupported styles.

### Personnel Death / Status From RPG Events

Near-term value: high.

Risk: medium to high.

Source-backed candidates:

- `Person#changeStatus(...)` and related personnel status methods.
- Scenario/tactical death should still prefer MekHQ's scenario resolution flow when the death happened during a MekHQ-tracked battle.

Shape:

- Possible endpoint shape: `POST /campaign/command/personnel/status`.
- Required selector: person id plus expected name, current status, current unit assignment, and RPG event source.
- Required policy: distinguish GM/RPG event status changes from tactical result imports.

Why this matters:

- MEK-RPG can have character-level events that are not generated by MekHQ's scenario resolver, such as narrative death, disappearance, retirement, capture, or recovery.
- These events need to update MekHQ's roster without bypassing status history, reports, and campaign consistency.

Design result:

- V1 should use `Person#changeStatus(Campaign, LocalDate, PersonnelStatus)`, not direct `setStatus(...)`, because `changeStatus(...)` owns personnel reports, service logs, retirement/death dates, unit crew cleanup, doctor/tech-job cleanup, command-role cleanup, genealogy effects, education cleanup, and `PersonStatusChangedEvent`.
- V1 should allow only source-reviewed single-person narrative status changes: `MIA`, `POW`, recovery to `ACTIVE` from absent states, non-payout `RETIRED`/`RESIGNED`/`LEFT`, explicit `DEFECTED`/`DESERTED`, and non-tactical death causes such as `HOMICIDE`, `ACCIDENTAL`, `NATURAL_CAUSES`, `UNDETERMINED`, or `SUICIDE`.
- V1 should refuse tactical casualties, tactical `KIA`, crew-hit/ejection/salvage/kill-credit facts, medical/prosthetic/fatigue outcomes, prisoner status changes and prisoner release/execution/removal flows, retirement payouts, and resurrection from dead statuses.
- `POW` is appropriate for captured player personnel outside tactical resolution; separate `PrisonerStatus.PRISONER` workflows are used for NPC prisoners and need a different command design.

Implemented result:

- Issue `#51` implemented `POST /campaign/command/personnel/status` in local MekHQ source commit `32366b64a0`.
- V1 validates campaign/date/person target guards, expected current status, expected prisoner status, expected unit assignment, `dryRun`, `promptPolicy=refuse_if_prompt`, idempotency key, plain-text RPG reason/source fields, opt-in save policy, and conservative status/refusal rules.
- Dry-run returns before/after person facts and intended side effects without mutation. Apply mode calls `Person#changeStatus(...)`, optionally appends a `GENERAL` MEK-RPG audit report, and returns before/after status, prisoner, unit, doctor, active/departed/absent/dead, salary, commander, death/retirement date, report count, prompt, save, and audit facts.
- Required compile/checkstyle verification passed. Live disposable campaign smoke testing is still not run because it requires a source-built MekHQ instance with a copied campaign loaded.

### Medical Treatment / Prosthetics

Near-term value: high.

Risk: high until source reviewed.

Source-backed candidates:

- `Person` injury and status APIs.
- Advanced medical injury/prosthetic classes and recovery/healing paths under MekHQ's personnel medical systems.

Shape:

- Possible endpoint shape: `POST /campaign/command/personnel/medical-treatment`.
- Required selector: person id plus expected injury/status state, treatment type, date, cost policy, and RPG event source.
- Required policy: distinguish cosmetic/narrative treatment, fatigue/hit recovery, structured injury treatment, prosthetic application, and medical expense handling.

Why this matters:

- MEK-RPG may resolve character-level medical care outside MekHQ's daily medical cycle, but MekHQ still needs to remain the hard roster and recovery ledger.

Blockers:

- MekHQ medical behavior is option-dependent. A prosthetic may be a structured injury outcome, equipment/status marker, or medical-system-specific state rather than a simple note.
- Source work must identify exact APIs and safe refusal cases before any endpoint mutates injuries.

Design result:

- Issue `#48` completed the source design in `MEK_RPG_LIVE_MEKHQ_MEDICAL_COMMAND_DESIGN.md`.
- Medical state is split across classic hits, Advanced Medical injuries, Alternate Advanced Medical prosthetic/implant injury records, fatigue, finance transactions, and medical/patient logs.
- Prosthetics and implants are structured permanent-modification `Injury` records with possible pilot/personnel option side effects; they must not be represented as plain notes.
- Broad `personnel.medical-treatment` and `personnel.prosthetic-surgery` commands should remain blocked until source exposes a non-dialog service or future source work extracts the reusable logic currently held inside `AdvancedReplacementLimbDialog`.
- Issue `#53` implemented `POST /campaign/command/personnel/fatigue` in local MekHQ source commit `ef6ef99ef9`. V1 uses `Person#changeFatigue(...)`, exposes `personnel.fatigue` readiness and fatigue guard facts, refuses disabled fatigue rules, inactive/dead personnel, mixed medical/prosthetic/expense/status effects, stale target facts, visible dialogs, and unsupported save/prompt policies.

### Unit Market Purchase

Near-term value: high for MEK-RPG.

Risk: high, but narrow V1 is feasible after source-generated live-session selectors are added.

Source-backed candidate:

- `UnitMarketPane#purchaseSelectedOffers()` debits `TransactionType.UNIT_PURCHASE` and then adds or queues units through `finalizeEntityAcquisition(...)`.
- `UnitMarketOffer#getPrice()` and `UnitMarketOffer#getEntity()` are source-owned derived values.

Shape:

- Possible endpoint shape: `POST /campaign/command/markets/unit-offers/purchase`.
- Required selector: stable unit-market offer id, expected unit name/type, expected price, expected transit duration, expected current balance, and delivery policy.

Why it is not an immediate easy win:

- Current notes found no source-confirmed durable unique id in `UnitMarketOffer#writeToXML(...)`.
- Duplicate offers can share display names and similar market fields. Row-index selection is unsafe for MEK-RPG.

First step:

- Add source-generated live selectors for unit-market offers, or explicitly mark unit purchase blocked until durable selectors exist.

Design result:

- Issue `#49` completed the source design in `MEK_RPG_LIVE_MEKHQ_UNIT_MARKET_PURCHASE_COMMAND_DESIGN.md`.
- `UnitMarketOffer#writeToXML()` serializes no durable unique offer id, so V1 should use MekHQ-generated live-session selectors scoped to the current process and `stateRevision`, paired with full guard facts.
- Duplicate exact offer fingerprints must be refused with `duplicate_offer_ambiguous`; row index, display name, localized label, and client-computed hashes remain unsafe selectors.
- The first implementation slice should expose selector candidates in `GET /campaign/commands` and implement `POST /campaign/command/markets/unit-offers/purchase` for one unique non-black-market offer at a time.
- V1 should preserve the current purchase side effects from `UnitMarketPane`: price calculation through `UnitMarketOffer#getPrice()`, funds debit as `TransactionType.UNIT_PURCHASE`, unit creation through `Campaign#addNewUnit(...)`, delivery report/state, offer removal, normal acquisition/finance reports, and optional MEK-RPG audit report.
- Black-market purchases should stay blocked in V1 because the current source path includes random swindle behavior that can debit money without adding the unit.

Implemented result:

- Issue `#54` implemented `POST /campaign/command/markets/unit-offers/purchase` in local MekHQ source commit `78890ba458`.
- `GET /campaign/commands` now exposes opaque live-session unit-market offer selectors scoped to the current process and `stateRevision`, with canonical source-owned fingerprints, duplicate counts, guard facts, and non-selectable refusal reasons.
- V1 validates selector state revision, fingerprint, unit name/type, market type, price percent, final price, transit duration, instant-delivery, mothball-delivery, random-quality, current balance, `dryRun`, `promptPolicy=refuse_if_prompt`, process-local idempotency, plain-text audit context, and opt-in save policy.
- Apply mode loads the entity through `UnitMarketOffer#getEntity()`, debits `TransactionType.UNIT_PURCHASE`, adds the unit through `Campaign#addNewUnit(...)`, preserves delivery/report/offer-removal behavior, and can append a `GENERAL` MEK-RPG audit report.
- V1 refuses duplicate exact offers, stale selectors, stale guards, entity-load failures, insufficient funds, black-market offers, delivery-policy mismatches, exact quality guards, and visible prompts.

### Repair / Procurement Commands

Near-term value: medium.

Risk: high.

Source-backed candidates:

- `Campaign#fixPart(...)`, `Campaign#goShopping(...)`, `Campaign#acquireEquipment(...)`, and `IAcquisitionWork`/`IPartWork` objects.

Blocker:

- The live API does not yet expose stable repair work ids or acquisition work ids. Repairs involve tech assignment, location, target rolls, costs, supplies, and time accounting.

Recommendation:

- Keep read-only repair pressure and shopping-list context for now. Do not make repair execution an early easy win.

## Recommended First Issue Scope

Issue `#50` completed the first safe MEK-RPG command API slice.

Completed ordering:

1. Define the common command envelope: completed by issue `#45`.
2. Implement command-readiness and selector discovery: completed by issue `#46`.
3. Source-check campaign status/note storage: completed by issue `#43`.
4. Implement the first mutation endpoint: completed by issue `#50`.

## Current Easy-Win Ranking

1. GM-only `POST /campaign/command/adjust-funds`, explicitly for manual correction rather than normal gameplay purchases.
2. Contract-market accept/decline by contract id, after prompt-policy review.
3. Personnel hire by applicant id, after market-style review.
4. Repair/procurement execution, after stable work ids and repair prompt policy exist.
5. Broad medical treatment/prosthetic surgery, after a source-owned non-dialog medical/prosthetic service exists.

## Guardrail For MEK-RPG

A MEK-RPG action like "buy this DropShip" should become a high-level MekHQ command only after the command has a stable offer selector and can route through MekHQ's actual purchase workflow. It should not be implemented as "subtract money plus manually add a unit" unless explicitly flagged as a GM correction command.
