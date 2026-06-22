# Tasks

This is the lightweight task board for the MegaMek workspace. Keep it current when work starts, finishes, gets blocked, or changes priority.

## How To Use This Board

- `Now`: active work that should be handled before starting unrelated expansion.
- `Next`: queued work with clear value and a likely near-term order.
- `Backlog`: useful work that is not yet ready or not yet urgent.
- `Blocked`: work waiting on user input, missing files, source confirmation, or an external state change.
- `Done`: recently completed work worth preserving for continuity.

Keep task titles short. Add enough context that a future agent can resume without rereading the whole chat.

## Task Entry Format

Use this shape for active and queued work:

```markdown
1. Task title.
   - Status: `Not started` | `In progress` | `Blocked` | `Done`
   - Owner: `Codex` | `User` | `Mixed`
   - Goal:
   - Output:
   - Notes:
```

## Transition Rules

- Before starting a queued task, move it from `Next` to `Now`.
- Keep `Now` small; one active task is preferred unless tasks are genuinely parallel.
- When a task finishes, move it to `Done` with the date and relevant commit if available.
- When a task blocks, move it to `Blocked` with the exact blocker and the next needed input or condition.
- If task scope changes, update the task immediately instead of leaving stale intent in the board.
- Commit task-board updates with the related documentation or implementation work when reasonable.

## Now

1. Set up real-life unit campaign in MekHQ.
   - Status: `In progress`
   - Owner: `User`
   - Goal: Create or copy a safe MekHQ campaign whose player unit reflects the actual tabletop force: chosen mechs, pilots, tech/support staff, equipment, and DropShip/transport assumptions.
   - Output: Report the save path, campaign name, roster/transport/support setup, important UI paths/prompts, any errors, and whether the save is safe for Codex to copy and inspect.
   - Notes: GitHub issue `#23`; child of epic `#14`; active checklist `docs/handoffs/active/user-real-unit-campaign-setup.md`; this should happen before the issue `#10` manual battle-record MUL import pass.

## Next

1. Implement live MekHQ command readiness and selector discovery.
   - Status: `Not started`
   - Owner: `Codex`
   - Goal: Let MEK-RPG ask MekHQ which mutating commands are currently available and why blocked commands are blocked.
   - Output: Endpoint/section design, selector policy, and implementation or follow-up implementation issue.
   - Notes: GitHub issue `#46`; child of epic `#44`; active handoff `docs/handoffs/active/implement-live-mekhq-command-readiness-selectors.md`; depends on reading issue `#45`.

2. Discover first guarded live MekHQ command API easy wins for MEK-RPG.
   - Status: `Not started`
   - Owner: `Codex`
   - Goal: Source-check and rank the first safe write-side live MekHQ API endpoints after the read-only live state work, with a command envelope and follow-up implementation tickets.
   - Output: GitHub issue, active handoff, and updated `MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md` with easy-win ranking and blockers.
   - Notes: GitHub issue `#43`; child of epic `#44`; active handoff `docs/handoffs/active/discover-live-mekhq-command-api-easy-wins.md`; new post-`#38` MEK-RPG strategy shift. Initial candidates are command readiness/selector discovery, campaign status/note mutation, personnel death/status, medical/prosthetics, GM-only funds adjustment, contract decision, personnel hire, and unit-market purchase after stable selectors.

3. Design personnel death/status command API.
   - Status: `Not started`
   - Owner: `Codex`
   - Goal: Source-check how MEK-RPG narrative death, capture, disappearance, retirement, or recovery should mutate MekHQ personnel state.
   - Output: Design note, endpoint proposal, refusal rules, verification facts, and a narrowed implementation issue if safe.
   - Notes: GitHub issue `#47`; child of epic `#44`; active handoff `docs/handoffs/active/design-live-mekhq-personnel-status-command.md`.

4. Design medical treatment and prosthetic command API.
   - Status: `Not started`
   - Owner: `Codex`
   - Goal: Source-check how MEK-RPG medical treatment, prosthetics, injury recovery, fatigue/hit recovery, and medical expenses should mutate MekHQ medical state.
   - Output: Design note, endpoint proposal, option-dependent refusal rules, verification facts, and a narrowed implementation issue if safe.
   - Notes: GitHub issue `#48`; child of epic `#44`; active handoff `docs/handoffs/active/design-live-mekhq-medical-prosthetic-command.md`.

5. Design unit-market purchase command API.
   - Status: `Not started`
   - Owner: `Codex`
   - Goal: Source-check safe selectors and workflow for MEK-RPG-driven unit or DropShip purchases from MekHQ's live market.
   - Output: Selector design, endpoint proposal, duplicate-offer refusal rules, verification facts, and a narrowed implementation issue if safe.
   - Notes: GitHub issue `#49`; child of epic `#44`; active handoff `docs/handoffs/active/design-live-mekhq-unit-market-purchase-command.md`; do not implement purchase by row index or display name.

6. Run MekHQ quickstart roster UI validation.
   - Status: `Not started`
   - Owner: `User`
   - Goal: Manually validate that a disposable New Player Quickstart campaign can have one unit added and one original unit removed through MekHQ GM controls.
   - Output: Report the disposable save path, exact GM mode/add/remove UI paths, units added/removed, prompts/errors, and any pilot/TO&E/transport follow-up so Codex can finish issue `#17`.
   - Notes: GitHub issue `#21`; user task that unblocks agent issue `#17`; active checklist `docs/handoffs/active/user-quickstart-roster-ui-validation.md`; do not overwrite the bundled quickstart save.

7. Turn this repo into an AI-ready project workflow demo.
   - Goal: Evolve this workspace into a reusable AI-ready project pattern with MegaMek/MekHQ as the worked example: source investigation, requirements discovery, verified commands, contributor handoff, campaign/save-file analysis, and agent memory.
   - Output: Clear repo positioning, generic workflow docs, MegaMek project profile, issue/requirement/PR templates, demo campaign fixture, and a decision on whether GitHub Projects should be used.

## Backlog

- Decide a report naming convention for campaign reports.
- Decide whether generated parser outputs should live under `analysis/generated/` by campaign name.
- Build a repeatable campaign summary extraction script after save structure is confirmed.
- Create source-reference notes for common MekHQ campaign actions and UI buttons.
- Low-priority epic `#15`: investigate photo-assisted BattleTech record sheet parsing with OpenCV/template matching, OCR fallback for unit names, and preferred unit matching through MekHQ UUIDs, short IDs, QR codes, or printed labels.

## Blocked

1. Prototype battle-record MUL round-trip validation.
   - Status: `Blocked`
   - Owner: `Mixed`
   - Goal: Prove that a generated or minimally edited battle-record MUL can be imported through MekHQ Resolve Manually and produces expected campaign-facing effects on disposable data.
   - Output: `BATTLE_RECORD_MUL_ROUND_TRIP_VALIDATION.md` records a successful installed-jar writer/parser round trip, expected MekHQ effects, and the remaining live UI validation gap.
   - Notes: GitHub issue `#10`; child of epic `#6`; active handoff `docs/handoffs/active/prototype-battle-record-mul-round-trip.md`; automated proof generated an ignored scratch `<record>` MUL with `survivors`, `salvage`, `retreated`, `devastated`, and `kills`; live MekHQ Resolve Manually click-through is blocked because the Windows Computer Use helper still reports `Computer Use native pipe path is unavailable`; user task `#23` should set up the real-life unit campaign before this import pass resumes.

2. Verify quickstart roster replacement workflow.
   - Status: `Blocked`
   - Owner: `Mixed`
   - Goal: Verify the no-source-change workflow for replacing the New Player Quickstart roster in a disposable campaign save.
   - Output: `QUICKSTART_ROSTER_REPLACEMENT_VERIFICATION.md` has source-confirmed steps, safe-copy verification, and the remaining UI validation gap.
   - Notes: GitHub issue `#17`; child of epic `#14`; active handoff `docs/handoffs/active/verify-quickstart-roster-replacement.md`; live UI click-through remains blocked because the Windows Computer Use helper reported `Computer Use native pipe path is unavailable`. User-owned unblocker is issue `#21` with checklist `docs/handoffs/active/user-quickstart-roster-ui-validation.md`.

3. Verify and document tabletop result entry workflow.
   - Status: `Blocked`
   - Owner: `Mixed`
   - Goal: Document the end-to-end tabletop result entry workflow from scenario export to MekHQ Resolve Manually import and closeout.
   - Output: User-facing workflow documentation with exact UI paths, input files, date/version, and observed campaign effects.
   - Notes: GitHub issue `#13`; child of epic `#6`; active handoff `docs/handoffs/active/verify-document-tabletop-result-entry-workflow.md`; blocked until issue `#10` live Resolve Manually import validation can be performed by the user or a future UI-control-capable session, preferably after user task `#23` creates the real-life unit campaign.

4. Verify Gradle build/test commands in local source repos.
   - Status: `Blocked`
   - Owner: `Mixed`
   - Goal: Run Gradle wrapper build/test commands in MegaMek, MekHQ, MegaMekLab, and mm-data.
   - Output: Mark source build/test commands as verified in `KNOWN_COMMANDS.md`.
   - Notes: Current shell resolves Java 21, portable JDK 17 is installed at `C:\Users\waltr\.jdks\temurin-17`, and Gradle toolchain discovery is configured for both JDKs. The previous missing-JDK-17 daemon blocker is resolved. `.\gradlew.bat :MekHQ:compileJava`, `.\gradlew.bat :MekHQ:assemble`, and `.\gradlew.bat :MekHQ:test` passed from `external/src/mekhq` on `2026-06-22`; the broader MegaMek/MegaMekLab/mm-data build/test matrix still needs full successful reruns.

## Done

- `2026-06-22`: Completed GitHub issue `#45` by defining the guarded live MekHQ command envelope and prompt policy in `MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`, creating epic tracking doc `GUARDED_LIVE_MEKHQ_COMMAND_API_TRACKING.md`, and archiving the issue handoff. The shared contract covers command/version/idempotency fields, campaign and target guards, dry-run behavior, opt-in save policy, prompt/dialog policy, response statuses, and reusable implementation acceptance criteria for epic `#44`.
- `2026-06-22`: Completed GitHub issue `#42` and epic `#38` by deepening live API logistics, reports, and market safeguards in MekHQ source commit `911a338788`. Added display-only repair queue, shopping-list pressure/rows, cargo/transport relationship summaries, report metadata/counts, market summaries/rows, and explicit unsupported automation blockers for stable selectors and mutation commands. Updated live API docs and fixtures; verified `.\gradlew.bat :MekHQ:compileJava`, `.\gradlew.bat :MekHQ:checkstyleMain`, and JSON fixture parsing.
- `2026-06-22`: Completed GitHub issue `#41` by deepening live API contract and scenario sections in MekHQ source commit `495b58faef`. Added contract description/date/travel/payment/salvage/rental/scenario-link fields and scenario description/link/map/conditions/player-force/salvage/objective/bot-force/tactical-result context. Updated live API docs and active-contract/scenario-rich fixture; verified `.\gradlew.bat :MekHQ:compileJava`, `.\gradlew.bat :MekHQ:checkstyleMain`, and JSON fixture parsing.
- `2026-06-22`: Completed GitHub issue `#40` by deepening live API finance, personnel, and unit sections in MekHQ source commit `d38a500960`. Added loan/default/warning summaries, personnel assignment/leadership/injury-summary/current-personnel fields, and unit availability/deployability/maintenance/transport summaries. Updated live API docs and fixtures; verified `.\gradlew.bat :MekHQ:compileJava`, `.\gradlew.bat :MekHQ:checkstyleMain`, and JSON fixture parsing.
- `2026-06-22`: Completed GitHub issue `#39` by hardening the live MekHQ campaign API trust/location metadata in MekHQ source commit `dc214d946d`. Dirty state remains explicit `Unknown` with stronger unsupported metadata because source search found no loaded-campaign dirty flag; location labels now come from `Campaign#getCurrentLocation()` and `AbstractLocation` methods instead of relying on `toString()`. Updated live API docs and fixtures; verified `.\gradlew.bat :MekHQ:compileJava` and `.\gradlew.bat :MekHQ:checkstyleMain` from `external/src/mekhq`.
- `2026-06-22`: Reviewed MEK-RPG live API change request `C:\Users\waltr\Documents\mek-rpg\docs\current\MEGAMEK_LIVE_API_CHANGE_REQUEST.md`, created MegaMek workspace epic `#38` and child issues `#39` through `#42`, added roadmap entries and active handoffs for producer-side live API hardening, and queued issue `#39` as the next agent task.
- `2026-06-22`: Closed follow-up GitHub issue `#37` for workspace commit `6756a70`, which recorded live smoke-test evidence for the issue `#36` read-only MekHQ campaign-state API. The follow-up confirms a user-assisted disposable-campaign smoke test from MEK-RPG issue `#104`, no observed save prompt or visible write/save side effect from read-only GET requests, and the MEK-RPG issue `#106` lesson that selected-section dashboard/context calls must include `bridge_metadata` unless `sections` is omitted.
- `2026-06-22`: Completed GitHub issue `#36` by adding `GET /campaign/summary` and `GET /campaign/state?sections=...` to the existing local MekHQ control API in source commit `7d3b345327`. The V1 live API keeps checkpoint grouping, trust envelopes, read-only/local-only guardrails, unknown dirty-state warnings, and structured unsupported entries for action-adjacent gaps. Workspace docs and sanitized fixtures are in `MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md` and `docs/templates/mekhq-live-campaign-*.fixture.json`. Verified `.\gradlew.bat :MekHQ:compileJava` and `.\gradlew.bat :MekHQ:checkstyleMain`; follow-up workspace commit `6756a70` later recorded the user-assisted disposable-campaign smoke test. Source push is blocked because `external/src/mekhq` points at upstream `MegaMek/mekhq` and the authenticated account lacks push permission.
- `2026-06-22`: Drafted `MEK_RPG_LIVE_MEKHQ_API_FEEDBACK_MEMO.md` for MEK-RPG review. The memo asks whether the existing save/checkpoint import contract should shift toward a live localhost MekHQ read-only state API, preserves the known MEK-RPG field priorities and trust-boundary fields, and requests feedback on endpoints, first-use fields, warning behavior, and durability semantics.
- `2026-06-22`: Completed GitHub issue `#35` by adding a local-only MekHQ Advance Day control API prototype in `external/src/mekhq` source commit `9046a8075e`, documenting the API in `MEKHQ_ADVANCE_DAY_CONTROL_API_PROTOTYPE.md`, and verifying `.\gradlew.bat :MekHQ:compileJava` after local JDK 17 setup. The endpoint remains disabled by default and live endpoint testing should wait for the user with copied/disposable saves.
- `2026-06-22`: User-assisted live test of the issue `#35` prototype succeeded. The source-built MekHQ app launched with `mekhq.controlApi.enabled=true`; `/status` saw loaded campaign `The Learning Ropes`; `POST /advance-day` advanced exactly one day from `3025-04-08` to `3025-04-09` with `saveAfterSuccess=false` and no save attempt. User confirmed dialogs appeared during the call and were manually dismissed, so future work should improve prompt/dialog reporting beyond the final `visibleDialogs` snapshot.
- `2026-06-22`: Added source commit `17207baa90` to let the local Advance Day API suppress MekHQ daily nag dialogs by default for the current command via `dismissAdvanceDayNags=true`. Verified with `.\gradlew.bat :MekHQ:compileJava` and `.\gradlew.bat :MekHQ:checkstyleMain`; live testing requires restarting the source-built MekHQ app.
- `2026-06-22`: User-assisted live test of `dismissAdvanceDayNags=true` succeeded after restarting the source-built MekHQ app on source commit `17207baa90`. `POST /advance-day` advanced `The Learning Ropes` from `3025-04-08` to `3025-04-09`, returned `advanceDayNagsDismissed=true`, and did not attempt a save.
- `2026-06-22`: Verified the MekHQ source test path after Java 17 setup. `.\gradlew.bat :MekHQ:test` passed from `external/src/mekhq` on branch `codex/mekhq-advance-day-control-api`, completing Checkstyle, test compilation, unit tests, and JaCoCo report with warnings only.
- `2026-06-22`: Completed GitHub issue `#34` by adding `MEKHQ_ADVANCE_DAY_GUI_CONTROL_SEAM_SPIKE.md`. Recommendation: source-level Advance Day control is viable as a narrow in-process MekHQ GUI command that calls `Campaign#newDay()` in the loaded app with campaign/date verification, one-day-only execution, no prompt auto-answering, and optional explicit disposable save-after-success. Do not treat it as a detached headless helper yet because `CampaignNewDayManager` still depends on `CampaignGUI`, event subscribers, and Swing dialogs.
- `2026-06-22`: Closed GitHub issue `#12` as unnecessary for the first campaign after issue `#11` selected MekHQ Resolve Manually as the baseline. Future generated-MUL work should reopen as a narrowed workspace installed-jar helper only if manual workflow proves too slow.
- `2026-06-22`: Completed GitHub issue `#11` by adding `TABLETOP_RESULT_MUL_GENERATION_STRATEGY.md`. Decision: use MekHQ Resolve Manually as the baseline; if custom generation is needed, narrow issue `#12` to a workspace Java helper using installed MegaMek/MekHQ jars and native serialization/parser validation, with no feature branch or source change yet.
- `2026-06-22`: Completed GitHub issue `#4` by adding `HELP_FILE_WORKFLOW.md`, linking it from current workspace/profile/workflow docs, adding local help/glossary/PDF extraction commands, and superseding `HELP_FILE_USAGE_GUIDANCE_STATE.md`.
- `2026-06-22`: Completed GitHub issue `#3` by mapping MekHQ campaign save/load source classes. `SAVE_FORMAT_NOTES.md`, `SOURCE_CODE_GUIDE.md`, and `KNOWN_COMMANDS.md` now record gzip magic-byte load detection, manual save extension/gzip behavior, always-gzipped autosaves, parser ownership, and the main source files.
- `2026-06-22`: Completed GitHub issue `#2` by inspecting `campaigns/demo/ai-ready-demo.cpnx.gz` read-only. Updated `ACTIVE_CAMPAIGN.md`, `SAVE_FORMAT_NOTES.md`, `KNOWN_COMMANDS.md`, and added first report `campaigns/demo/reports/first-demo-status-3025-04-08.md`.
- `2026-06-22`: Completed GitHub issue `#33` and epic `#30` by recording `MEK_RPG_MEKHQ_CHECKPOINT_EXPORTER_OWNERSHIP_DECISION.md`. Recommendation: keep the jar-backed checkpoint exporter as a workspace experimental helper for near-term use, and defer any MekHQ source move to a separate source-change issue triggered by real campaign use, MEK-RPG production dependency, upstream/source-maintainer intent, or unblocked source build/test verification.
- `2026-06-22`: Completed GitHub issue `#32` by hardening the jar-backed read-only checkpoint exporter. The prototype now emits stable location display/id fields, method-backed core `Contract` terms, preserved market selector warnings and mandatory `unsupported` entries, plus repeatable smoke check `tools/mekhq-checkpoint-exporter/test-mekhq-checkpoint-exporter.ps1`.
- `2026-06-22`: Completed GitHub issue `#31` by reconciling MegaMek checkpoint docs with completed MEK-RPG feedback from issues `#84` through `#89`. The docs now record accepted top-level grouping, required trust-boundary fields, location and contract hardening requirements, warning/unsupported surfacing policy, edge-fixture coverage, and the handoff to exporter hardening issue `#32`.
- `2026-06-21`: Recorded MEK-RPG checkpoint export review feedback and consumer-side issue queue `walt-raymond-williams/mek-rpg#84` through `#89`. Superseded by the `2026-06-22` reconciliation above: MEK-RPG feedback is now complete and exporter hardening should proceed through issue `#32`; market selectors and write-side actions remain out of scope.
- `2026-06-21`: Completed GitHub epic `#26` after closing child issues `#27`, `#28`, and `#29`. Final review is in `MEK_RPG_MEKHQ_CHECKPOINT_EPIC_REVIEW.md`; read-only boundary held, checkpoint handoffs are archived, and future production exporter/write-side work should be opened separately.
- `2026-06-21`: Completed GitHub issue `#29` by adding jar-backed read-only prototype `tools/mekhq-checkpoint-exporter/`. The prototype loads an explicit copied `.cpnx.gz` through installed MekHQ jars and emits parseable checkpoint JSON with method-backed balance, salary, unit condition, repair counts, unit-market final price, and sanitized report examples. Findings are in `MEK_RPG_MEKHQ_CHECKPOINT_EXPORTER_PROTOTYPE.md`.
- `2026-06-21`: Completed GitHub issue `#28` by validating the checkpoint schema against copied save `analysis/tmp/issue-22/Autosave-1-The Learning Ropes-30250720.cpnx.gz` with MEK-RPG helper `summarize-mekhq-save.py`. Findings are in `MEK_RPG_MEKHQ_CHECKPOINT_VALIDATION.md`; no schema rename was needed, and UI validation remains an optional future user-operated spot check.
- `2026-06-21`: Completed GitHub issue `#27` by adding sanitized fixture `docs/templates/mekhq-read-only-checkpoint.fixture.json` for MEK-RPG checkpoint adapter tests, linking it from the checkpoint schema, and archiving `docs/handoffs/archive/create-mekhq-checkpoint-fixture.md`.
- `2026-06-21`: Incorporated MEK-RPG issues `#67` and `#68` into the MegaMek-side checkpoint-export plan. Added `MEK_RPG_MEKHQ_CHECKPOINT_EXPORT_SCHEMA.md` as the draft exporter JSON shape aligned to MEK-RPG's consumer contract, and reaffirmed that headless day advancement/write automation remains out of scope.
- `2026-06-21`: Completed GitHub issue `#25` by defining the MekHQ read-only checkpoint export contract for MEK-RPG in `MEK_RPG_MEKHQ_CHECKPOINT_EXPORT.md`. The contract recommends a source-backed MekHQ DTO/JSON exporter, maps method/API owners for campaign identity, location, finances, personnel, units, contracts, scenarios, markets, repairs/logistics, and reports, marks raw-XML-only gaps, and keeps writeback out of scope.
- `2026-06-21`: Completed GitHub issue `#24` by mapping safe MekHQ bridge primitives for MEK-RPG pending actions in `MEK_RPG_MEKHQ_BRIDGE_PRIMITIVES.md`. The map recommends read-only checkpoint export first, identifies `Campaign#newDay()` as GUI-coupled through `CampaignNewDayManager`, maps unit market, contract, personnel, repair/logistics, tactical-result, and finance method candidates, and recommends a narrow contract-market decision command only after stable id and prompt-policy validation.
- `2026-06-20`: Added `MEK_RPG_MEKHQ_COLLABORATION_BRIEF.md` as a shareable coordination packet for the MEK-RPG team, with ownership boundaries, automation phases, open collaboration questions, and a proposed first joint read-only bridge issue.
- `2026-06-20`: Assessed the sister MEK-RPG repository for MekHQ integration opportunities. Recommendation is to keep MEK-RPG as narrative/RPG memory and use MekHQ as an optional unit-scale logistics, roster, contract, repair, salvage, and tactical-result ledger. Findings are in `MEK_RPG_MEKHQ_INTEGRATION_ASSESSMENT.md`; roadmap now has a pilot bridge candidate.
- `2026-06-19`: Completed GitHub issue `#22` by studying the generated full battle-record MUL from the MekHQ shakedown. Findings are in `GENERATED_BATTLE_RECORD_MUL_STUDY.md`: the live file is a full `<record>` with survivors/salvage/devastated/kills, `logs/salvage.mul` is only a plain salvage/export `<unit>` list, player units can land in multiple result buckets depending on end state, ejected pilot and pickup state are serialized, and the next issue `#10` manual import pass should compare raw MUL kill rows against post-resolution MekHQ campaign kill records.
- `2026-06-19`: Completed GitHub issue `#16` by running the first MekHQ campaign exploration live-assist shakedown. MekHQ launched successfully, the user played and resolved a MegaMek scenario from `The Learning Ropes`, post-play saves/logs were inspected safely, the scenario/objective draw lesson was recorded, and follow-up tasks were split into issue `#22` for studying the generated full battle-record MUL and issue `#10` for the next manual Resolve Manually import attempt. Archived handoff: `docs/handoffs/archive/run-mekhq-campaign-exploration.md`.
- `2026-06-18`: Completed GitHub issue `#9` by defining the tabletop result input schema in `TABLETOP_RESULT_INPUT_SCHEMA.md`, separating first-session manual capture fields from optional future generator fields, and linking the schema from the tabletop result MUL workflow. Recommended next tabletop-result task is issue `#10`, prototype battle-record MUL round-trip validation.
- `2026-06-18`: Completed GitHub issue `#19` by documenting the custom RAT strategy in `CUSTOM_RAT_STRATEGY.md`. Recommendation: do not build custom RATs yet; use fixed OPFOR setup-MUL pools first, and revisit custom classic RATs or modern force-generator data only after confirmed inventory and fixed-pool play show a real need. Remaining roster-control child `#17` is blocked pending live MekHQ UI validation.
- `2026-06-18`: Completed GitHub issue `#18` by documenting the fixed OPFOR setup-MUL pool workflow in `FIXED_OPFOR_MUL_POOL_WORKFLOW.md`, adding `docs/templates/OPFOR_MUL_POOL_MANIFEST.csv`, and locally verifying placeholder MUL generation plus parser round trip with installed MekHQ/MegaMek jars. Raw placeholder MUL output remains ignored under `analysis/tmp/`; real inventory MULs need the user's confirmed miniature list. This fed the custom RAT strategy decision completed in issue `#19`; issue `#17` remains blocked pending live MekHQ UI validation.
- `2026-06-18`: Completed GitHub issue `#20` by defining the physical-miniature roster model in `PHYSICAL_MINIATURE_ROSTER_MODEL.md`, adding `docs/templates/PHYSICAL_MINIATURE_ROSTER.csv`, and archiving the handoff. This fed the fixed OPFOR MUL pool prototype completed in issue `#18`; issue `#17` remains blocked pending live MekHQ UI validation.
- `2026-06-18`: Completed source/docs discovery for GitHub issue `#14` by documenting MekHQ player roster and OPFOR control workflows in `MECH_ROSTER_CONTROL_WORKFLOWS.md`, then created child issues `#17` through `#20` for disposable-save quickstart roster replacement verification, physical-miniature roster data modeling, fixed OPFOR MUL pool prototyping, and a later custom RAT decision.
- `2026-06-18`: Added `CAMPAIGN_EXPLORATION_PLAN.md` to track the first hands-on MekHQ shakedown campaign, including new campaign creation, first contract flow, owned Leopard transport, two aerospace fighters, and a human-controlled transit aerospace scenario concept.
- `2026-06-18`: Completed GitHub issue `#7` by tracing MekHQ salvage behavior through manual scenario resolution, contract salvage terms, BLC, salvage exchange, and CamOps salvage. Findings are in `SALVAGE_RULES_NOTES.md`. This fed the schema issue `#9` and next round-trip validation issue `#10`.
- `2026-06-18`: Completed GitHub issue `#8` by confirming MekHQ's battle-record MUL source workflow for tabletop result import, documenting the flow in `TABLETOP_RESULT_MUL_WORKFLOW.md`, and archiving the handoff. Recommended next epic task is salvage rules issue `#7`.
- `2026-06-18`: Decomposed GitHub issue `#6` into child issues `#7` through `#13`, updated the roadmap and active handoffs, and recommended starting execution with source workflow confirmation issue `#8`. Commit `019367c`.
- `2026-06-18`: Made the GitHub repository public and clarified that this repo is the one canonical working/public artifact, not a staging repo for a separate template repo.
- `2026-06-18`: Completed GitHub issue `#1` by splitting reusable AI-ready workflow guidance into `AI_READY_PROJECT_WORKFLOW.md` and MegaMek/MekHQ-specific assumptions into `MEGAMEK_PROJECT_PROFILE.md`, with front-door links from `AGENTS.md`, `README.md`, and `WORKSPACE.md`.
- `2026-06-18`: Completed GitHub issue `#5` by comparing against Sunny Town HQ and updating this repo's workflow docs with epic, feature-tracking, PR-readiness, open-PR, and human-review patterns.
- `2026-06-18`: Established roadmap-driven GitHub issue tracking with active/archive handoff lifecycle, created the `agent-task` GitHub label, created issues `#1` through `#4`, and added active handoff documents for each issue.
- `2026-06-18`: Added `SOURCE_CHANGE_WORKFLOW.md`, tightened task-board transition rules, and documented current source build/test commands and Gradle blocker.
- `2026-06-18`: Added lightweight task tracking in `TASKS.md` and made it part of the documentation workflow.
- `2026-06-18`: Marked `The Learning Ropes.cpnx.gz` as the active practice campaign in `ACTIVE_CAMPAIGN.md`.
- `2026-06-18`: Established the initial workspace documentation baseline in commit `e858543`.

## Update Rules

- Update this file before switching from one major workstream to another.
- Move completed tasks to `Done` with the date and relevant commit if available.
- Move blocked tasks to `Blocked` with the exact missing input or condition.
- Keep durable findings in the relevant `docs/current/` file; this board tracks work, not detailed research.
