# MEK-RPG Live MekHQ Command API Strategy

Status: initial planning note for guarded write-side expansion, created `2026-06-22`.

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

Every mutating endpoint should require:

- `command`: exact command name/version.
- `idempotencyKey`: MEK-RPG-generated key for safe retry detection.
- `expectedCampaignId` or `expectedCampaignName`.
- `expectedDate`: current MekHQ campaign date.
- target ids and guard fields, such as person id, unit id, offer selector, expected status, expected balance, expected price, or expected injury state.
- `dryRun`: when supported, validate and return the intended side effects without mutating.
- `saveAfterSuccess`: explicit save policy; default should remain no save while prototyping.
- `clientContext`: MEK-RPG source/action reference for audit reports.

Responses should include:

- status such as `applied`, `dry_run`, `refused`, `blocked`, or `failed`
- before/after campaign date and selected target facts
- side effects applied or side effects that would be applied
- reports/transactions/unit/personnel ids created or changed
- warnings and unsupported blockers
- visible dialog/prompt count or prompt refusal reason
- save attempt/result when requested

## Candidate Command Classes

### Campaign Status / GM Note

Near-term value: high.

Risk: low to medium, depending on where the note/status is stored.

Shape:

- Add a MEK-RPG-visible campaign status marker, note, or report entry through a source-owned MekHQ path.
- Possible endpoint shape: `POST /campaign/command/status-note`.
- Guard with campaign id/name/date and a client-provided idempotency key.
- Return the created report/note text, report category, and new state revision.

Why this is an easy win:

- It gives MEK-RPG a first non-day-advance mutation that does not require market selectors, unit creation, salvage, contract prompts, or crew assignment.
- It helps prove command envelope, idempotency, response shape, and audit/report behavior before automating purchases.

Open source question:

- Confirm the best MekHQ-owned storage target. A report entry may be easiest but could be noisy; a campaign note/custom metadata field may be better if one exists.

### Command Readiness / Selector Discovery

Near-term value: high.

Risk: low.

Shape:

- Add endpoint fields or a new endpoint that says which command types are currently automation-ready and why blocked command types are blocked.
- Expose stable command selectors only when MekHQ can produce them safely.
- Possible endpoint shape: `GET /campaign/commands`.

Why this is an easy win:

- MEK-RPG can light up or hide action buttons without guessing from display-only market rows.
- It creates the missing bridge between read-only context and future mutation endpoints.

Open source question:

- Decide whether selectors should be ephemeral only for the current live session or durable across save/reload. Unit market offers currently do not have source-confirmed stable offer ids.

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

Blockers:

- Source review must confirm the right status transition APIs, report behavior, unit-crew cleanup behavior, and any dependent systems such as pay, assignments, prisoners, marriage/dependents, or personnel removal.
- Not all RPG outcomes map to simple MekHQ statuses; the command should refuse ambiguous requests.

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

### Unit Market Purchase

Near-term value: high for MEK-RPG.

Risk: high until selectors are solved.

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

Create one discovery/execution issue to identify and prototype the first safe MEK-RPG command API slice.

Recommended ordering:

1. Define the common command envelope: guard fields, idempotency key, dry-run option, save policy, before/after response, warnings, and prompt behavior.
2. Source-check campaign status/note storage and command-readiness selector exposure.
3. Rank easy wins against disposable campaign validation.
4. Create child issues for the first one or two command endpoints only.

## Current Easy-Win Ranking

1. `GET /campaign/commands` or equivalent command-readiness/selector discovery.
2. `POST /campaign/command/status-note` or equivalent campaign status/report-note mutation.
3. GM-only `POST /campaign/command/adjust-funds`, explicitly for manual correction rather than normal gameplay purchases.
4. Personnel death/status command design for RPG events that are not MekHQ tactical results.
5. Medical treatment/prosthetic command design, after source review of injury and prosthetic state.
6. Contract-market accept/decline by contract id, after prompt-policy review.
7. Personnel hire by applicant id, after market-style review.
8. Unit-market purchase by stable offer id, after selector design.
9. Repair/procurement execution, after stable work ids and repair prompt policy exist.

## Guardrail For MEK-RPG

A MEK-RPG action like "buy this DropShip" should become a high-level MekHQ command only after the command has a stable offer selector and can route through MekHQ's actual purchase workflow. It should not be implemented as "subtract money plus manually add a unit" unless explicitly flagged as a GM correction command.
