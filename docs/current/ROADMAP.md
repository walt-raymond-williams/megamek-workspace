# Roadmap

This is the repository-owned planning file for the AI-ready workflow demo. Keep it current even when execution happens through GitHub Issues.

## Purpose

The roadmap is the durable source for:

- major workstreams
- sequencing decisions
- issue candidates
- handoff context for future agents
- deferred scope that may change after earlier work lands

GitHub Issues are execution units created from this file when work is ready to assign, discuss, or hand off. Do not create all future issues upfront if the content is likely to change.

## Roadmap Entry Format

Use this shape for entries that may become GitHub issues:

```markdown
### Short title

- Status: `Idea` | `Ready for issue` | `Issue created` | `In progress` | `Done` | `Blocked`
- Priority: `High` | `Medium` | `Low`
- Issue: `Not created` or `#123`
- Owner: `Codex` | `User` | `Mixed` | `Unassigned`
- Goal:
- Why it matters:
- Expected output:
- Handoff notes:
- Dependencies:
- Open questions:
```

## Issue Creation Rules

- Create GitHub issues from roadmap entries when the task is ready for execution or outside discussion.
- Keep the roadmap entry after creating the issue, and add the issue number.
- Update the roadmap when completed work changes future scope.
- The issue should contain enough context for an agent to start without rereading the whole repository.
- For agent execution, attach or link a handoff document based on `docs/templates/AGENT_HANDOFF.md`.

## Current Workstreams

### Establish GitHub issue tracking from roadmap

- Status: `Done`
- Priority: `High`
- Issue: `Not created`
- Owner: `Mixed`
- Goal: Make `ROADMAP.md` the planning source that guides GitHub issue creation and agent handoffs.
- Why it matters: The repo needs trackable execution without losing flexible long-term planning in a versioned file.
- Expected output: GitHub issue workflow, issue/handoff templates, active/archive handoff lifecycle, documented commands or blockers for creating issues, and first issues created from roadmap entries.
- Handoff notes: Start with `docs/current/GITHUB_ISSUE_WORKFLOW.md` and `docs/templates/AGENT_HANDOFF.md`. The GitHub repo exists at `https://github.com/walt-raymond-williams/megamek-workspace`.
- Dependencies: None for issue creation; GitHub remote and authenticated `gh` session are confirmed locally.
- Open questions: Should GitHub Projects be used after the first issue set exists?

### Split generic AI-ready workflow from MegaMek profile

- Status: `Done`
- Priority: `High`
- Issue: `#1`
- Owner: `Mixed`
- Goal: Separate reusable AI-ready workflow guidance from MegaMek/MekHQ-specific paths, source maps, save files, and rules posture.
- Why it matters: The public repo should serve as both the real MegaMek worked example and the reusable pattern; there should not be a second repo to keep in sync unless the user explicitly asks for one.
- Expected output: Generic workflow docs plus a MegaMek project profile that owns local assumptions and domain-specific guidance. Completed with `docs/current/AI_READY_PROJECT_WORKFLOW.md` and `docs/current/MEGAMEK_PROJECT_PROFILE.md`.
- Handoff notes: Completed by making the generic workflow visible without requiring MegaMek knowledge, keeping MegaMek domain guidance in a first-class project profile, and linking both from `AGENTS.md`, `README.md`, `WORKSPACE.md`, and workflow docs. Archived handoff: `docs/handoffs/archive/split-generic-ai-ready-workflow.md`.
- Dependencies: None.
- Open questions: None for issue `#1`; keep this as one public canonical repo unless the user explicitly asks for a separate template or profile repository.

### Compare workflow against Sunny Town HQ

- Status: `Done`
- Priority: `High`
- Issue: `#5`
- Owner: `Codex`
- Goal: Compare this repo's AI workflow, roadmap, handoff, issue, branch, and PR-review process against Sunny Town HQ and import useful patterns.
- Why it matters: Sunny Town HQ already contains a working agentic workflow for epics, feature integration branches, per-issue handoffs, PR readiness review, and human-gated merges to main.
- Expected output: Updated workflow docs, roadmap/task changes, and a recommendation on which Sunny Town HQ patterns should become standard here.
- Handoff notes: Completed by adopting Sunny Town HQ's useful patterns without copying its exact layout: epic issues for broad outcomes, optional feature tracking docs for multi-issue integration branches, PR-readiness review issues, open-PR handoffs for complex branch PRs, and explicit human review before merge to `master`. Archived handoff: `docs/handoffs/archive/compare-sunny-town-hq-workflow.md`.
- Dependencies: Local Sunny Town HQ checkout is present but has its own dirty worktree; inspect only, do not modify it.
- Open questions: None for issue `#5`; future broad workstreams should decide case-by-case whether they need a feature tracking doc.

### Inspect demo campaign save

- Status: `Done`
- Priority: `Medium`
- Issue: `#2`
- Owner: `Codex`
- Goal: Extract a factual campaign snapshot from `campaigns/demo/ai-ready-demo.cpnx.gz`.
- Why it matters: The demo save is the concrete example for save-file investigation, reporting, and requirements discovery.
- Expected output: Completed with updated `ACTIVE_CAMPAIGN.md`, `SAVE_FORMAT_NOTES.md`, `KNOWN_COMMANDS.md`, and first report `campaigns/demo/reports/first-demo-status-3025-04-08.md`.
- Handoff notes: Follow `CAMPAIGN_ANALYSIS_WORKFLOW.md`. Do not overwrite the save. Archived handoff: `docs/handoffs/archive/inspect-demo-campaign-save.md`.
- Dependencies: None.
- Open questions: Should generated extracts live under `analysis/generated/demo/` once a repeatable extractor exists?

### Identify MekHQ save/load source classes

- Status: `Done`
- Priority: `Medium`
- Issue: `#3`
- Owner: `Codex`
- Goal: Confirm how `.cpnx.gz` files are loaded and saved in local MekHQ source.
- Why it matters: Save-file interpretation should be grounded in implementation behavior.
- Expected output: Completed with source-backed updates to `SAVE_FORMAT_NOTES.md`, `SOURCE_CODE_GUIDE.md`, and `KNOWN_COMMANDS.md`.
- Handoff notes: Completed on `2026-06-22`. Archived handoff: `docs/handoffs/archive/identify-mekhq-save-load-source-classes.md`. Source inspection confirmed `CampaignFactory` gzip magic-byte detection, `CampaignXmlParser` parse ownership, `CampaignGUI` manual save gzip behavior, `FileDialogs`/`FileType` extension handling, `AutosaveService` gzip autosaves, and `Campaign#writeToXML(...)` major serialized sections.
- Dependencies: None for close-out.
- Open questions: Should source checkout paths remain fixed under `external/src` or become configurable through a project profile?

### Create help-file usage guidance

- Status: `Done`
- Priority: `Medium`
- Issue: `#4`
- Owner: `Codex`
- Goal: Decide how agents should use local MegaMek/MekHQ help files, glossary resources, installed docs, and source code.
- Why it matters: Help files are a safer user-facing reference for concepts, while source is needed for exact behavior.
- Expected output: Completed with `docs/current/HELP_FILE_WORKFLOW.md`, links from current workspace/profile/workflow docs, repeatable commands in `KNOWN_COMMANDS.md`, and updated state note.
- Handoff notes: Completed on `2026-06-22`. Archived handoff: `docs/handoffs/archive/create-help-file-usage-guidance.md`. Guidance now routes agents across campaign data, local help/glossary/docs, source code, user confirmation, and external sources, with copyright and evidence-label posture.
- Dependencies: None for close-out. `pdftotext` is available locally through Poppler, and Python `pypdf` is importable.
- Open questions: Whether this should later become a reusable project-profile pattern remains a generic workflow question, not a blocker for MegaMek guidance.

### Run MekHQ campaign exploration live-assist shakedown

- Status: `Done`
- Priority: `High`
- Issue: `#16`
- Owner: `Mixed`
- Goal: Run the hands-on MekHQ exploration plan as a live user/Codex session while the user manually operates MekHQ and Codex acts as a campaign aide, source/docs reference, and documentation recorder.
- Why it matters: The workspace needs to move from documentation-only planning into an actual MekHQ shakedown that tries campaign creation, contracts, travel, owned transport, aerospace, scenario resolution, repair, salvage, finances, and close-out.
- Expected output: Completed with session notes in `docs/current/CAMPAIGN_EXPLORATION_PLAN.md`; follow-up work is tracked in issue `#22` for generated battle-record MUL study and issue `#10` for live manual Resolve Manually import validation.
- Handoff notes: Completed on `2026-06-19`. Archived handoff: `docs/handoffs/archive/run-mekhq-campaign-exploration.md`. The session launched MekHQ, used the user-operated live-assist workflow, inspected post-play saves/logs, and captured campaign-facing lessons from the first resolved MegaMek scenario.
- Dependencies: None for close-out. Future aerospace and manual-import work should continue through narrower follow-up tasks instead of reopening this shakedown issue.
- Open questions: Can a custom transit aerospace battle be attached cleanly to an active contract, or should it be tracked as a manual/GM narrative scenario? Which Leopard variant and two aerospace fighters should be used for the first dedicated aerospace test?

### Investigate MegaMek tactical lag and low-risk performance wins

- Status: `Done`
- Priority: `High`
- Issue: `#80`
- Owner: `Codex`
- Goal: Investigate user-observed MegaMek tactical lag and identify low-risk performance improvements for large battles, especially when many units are on the board and Princess/bot AI is active.
- Why it matters: The tactical game needs to stay responsive enough for campaign play. If UI redraw churn, avoidable EDT work, or obvious repeated calculations are contributing to lag, small targeted fixes may improve playability before deeper AI/pathing optimization is attempted.
- Expected output: Completed with source-backed findings in `docs/current/MEGAMEK_TACTICAL_PERFORMANCE_INVESTIGATION.md`, separating firing-solution/UI redraw candidates from minimap and Princess/pathing profiling territory. No source patch was approved or committed for this investigation.
- Handoff notes: Completed on `2026-07-01`; archived handoff: `docs/handoffs/archive/investigate-megamek-tactical-performance.md`. Focused bug issue `#81` completed the firing-phase source map and recommends first testing with default-on firing-solution overlays disabled. A small uncommitted `BoardView.java` redraw-coalescing experiment remains in `external/src/megamek`; treat it as an experiment to inspect, not an approved finished fix.
- Dependencies: Future implementation needs explicit approval, a representative large-unit save/scenario or live reproduction steps, and targeted timing/profiling before deeper Princess/pathing changes.
- Open questions: Does disabling View > Firing Solutions materially improve the firing-phase unit-switching and targeting lag identified in issue `#81`? Does closing the minimap, disabling isometric/FOV/shadows/labels/animations, or profiling Princess turns identify a separate dominant lag source?
  - Child/focused issues:
    - `#81`: Investigate firing-phase unit switching and targeting lag. Completed on `2026-06-29`; archived handoff: `docs/handoffs/archive/investigate-firing-phase-targeting-lag.md`. Recommended first check is disabling View > Firing Solutions, because source confirms the default-on overlay scans all entities and runs `WeaponAttackAction.toHit(...)` for viable targets on unit/arc updates.

### Epic: Robust tabletop battle result MUL workflow

- Status: `In progress`
- Priority: `High`
- Issue: `#6`
- Owner: `Mixed`
- Goal: Design a robust workflow for using MekHQ to generate scenarios, playing the battle by hand on tabletop, and feeding accurate results back into MekHQ through its built-in manual-resolution/result-entry capabilities.
- Why it matters: The user wants MekHQ to remain the campaign authority while resolving tactical battles with physical BattleTech play. A robust result-entry path would reduce manual campaign edits, preserve MekHQ repair/salvage/personnel logic, and make tabletop play practical inside this workspace.
- Expected output: A decomposed multi-issue workstream covering source investigation, result schema design, prototype round-trip tests, generation strategy, possible implementation, documentation, and validation against MekHQ's Resolve Manually workflow.
- Handoff notes: This is an epic, not a direct implementation task. It has been decomposed into child issues `#7` through `#13`; implementation should proceed through those issues. Active epic handoff: `docs/handoffs/active/robust-tabletop-battle-result-mul-workflow.md`.
- Dependencies: Local MegaMek/MekHQ source and install are present under `external/`; source build/test commands are currently blocked by the Java 17 Gradle daemon/toolchain issue, so early work may need to run against installed jars or focus on source-reading and UI/manual verification.
- Child issues:
  - `#7`: Investigate MekHQ and BattleTech salvage rules. Completed on `2026-06-18`; source findings are in `docs/current/SALVAGE_RULES_NOTES.md`.
  - `#8`: Confirm battle-record MUL source workflow for tabletop result import. Completed on `2026-06-18`; source findings are in `docs/current/TABLETOP_RESULT_MUL_WORKFLOW.md`.
  - `#9`: Define tabletop battle result input schema for MekHQ MUL generation. Completed on `2026-06-18`; schema is in `docs/current/TABLETOP_RESULT_INPUT_SCHEMA.md`.
  - `#10`: Prototype battle-record MUL round-trip validation against MekHQ. Installed-jar writer/parser validation is documented in `docs/current/BATTLE_RECORD_MUL_ROUND_TRIP_VALIDATION.md`; live MekHQ Resolve Manually import is blocked on the Windows Computer Use helper.
  - `#22`: Study generated battle-record MUL from MekHQ shakedown. Completed on `2026-06-19`; live-file findings are in `docs/current/GENERATED_BATTLE_RECORD_MUL_STUDY.md`.
  - `#11`: Choose MUL generation strategy for tabletop result workflow. Completed on `2026-06-22`; decision is in `docs/current/TABLETOP_RESULT_MUL_GENERATION_STRATEGY.md`.
  - `#12`: Implement robust tabletop battle-record MUL generator. Closed on `2026-06-22` as unnecessary for the first campaign; future generated-MUL work should reopen this as a narrowed workspace installed-jar helper only after built-in/manual workflow is documented or proves too slow.
  - `#13`: Verify and document tabletop result entry workflow for MekHQ. Blocked on live/user-operated Resolve Manually import validation and exact UI observations.
- Recommended sequence: Source workflow confirmation `#8`, salvage research `#7`, input schema `#9`, generated-MUL study `#22`, strategy issue `#11`, and conditional generator issue `#12` are complete. Issue `#10` has validated native battle-record MUL writer/parser round trip but still needs a live MekHQ Resolve Manually import pass; carry the checklist from `GENERATED_BATTLE_RECORD_MUL_STUDY.md` into that user-operated pass. Issue `#13` is blocked until those UI/manual facts exist.
- Branch/tracking recommendation: Do not create an integration branch now. Only create `codex/tabletop-result-mul-dev` and a compact feature tracking doc if issue `#12` expands into source changes, a multi-commit helper, or coordinated `#12`/`#13` implementation work.
- Implementation posture: `#11` recommends MekHQ's built-in Resolve Manually workflow as the baseline. If custom generation is needed, implement it first as a workspace Java helper using installed MegaMek/MekHQ jars and native `EntityListFile`/`MULParser` behavior; do not start a MekHQ source feature or hand-written XML generator yet.
- Open questions: Is built-in/manual entry sufficient for the first real campaign once user task `#23` creates a realistic campaign? Who will perform the live Resolve Manually import pass while Computer Use remains unavailable?

### Investigate MekHQ and BattleTech salvage rules

- Status: `Done`
- Priority: `High`
- Issue: `#7`
- Owner: `Codex`
- Goal: Explain how MekHQ determines salvage eligibility, salvage rights, salvage exchange, battlefield control effects, contract salvage terms, and post-scenario salvage processing, then compare those behaviors against the relevant BattleTech campaign salvage rules at a high level.
- Why it matters: The tabletop result MUL workflow must know what result data affects salvage and what MekHQ will calculate itself after manual scenario resolution.
- Expected output: Completed with `docs/current/SALVAGE_RULES_NOTES.md`, a source-grounded salvage behavior note with player-facing implications and tabletop result-MUL guidance.
- Handoff notes: Completed on `2026-06-18`. Archived handoff: `docs/handoffs/archive/investigate-salvage-rules.md`.
- Dependencies: Local source is available under `external/src`; official BattleTech rulebooks may need user-provided page references or official/primary public references. Do not reproduce large copyrighted rules text.
- Open questions: Which optional MekHQ salvage systems are enabled in the active campaign? Which BattleTech campaign-rule source should be treated as authoritative for the user's table: Campaign Operations, Chaos Campaign, Mercenaries rules, or another source?

### Epic: Control MekHQ player and OPFOR mech rosters

- Status: `In progress`
- Priority: `High`
- Issue: `#14`
- Owner: `Mixed`
- Goal: Figure out the best workflow for setting and controlling MekHQ campaign rosters for a parent-run tabletop campaign, including both the player's mercenary company roster and generated opposition forces.
- Why it matters: The user wants to manage the campaign and narration for his son while using actual physical BattleTech miniatures. MekHQ should remain the campaign authority, but the player starting roster and OPFOR generation need to align with the units available at the table.
- Expected output: Source- and UI-grounded recommendation for roster control, followed by child issues for useful implementation or tooling. Initial discovery is recorded in `docs/current/MECH_ROSTER_CONTROL_WORKFLOWS.md`.
- Handoff notes: This is an epic, not a direct implementation task. Active epic handoff: `docs/handoffs/active/mech-roster-control-epic.md`. Feature tracking snapshot: `docs/current/MECH_ROSTER_CONTROL_TRACKING.md`. Discovery on `2026-06-18` found a no-source-change first workflow: copy/save the quickstart, use GM unit add/remove for player roster changes, use scenario edit/regenerate or fixed OPFOR setup MULs for physical-miniature OPFOR control, and defer custom RATs until the physical miniature list exists.
- Dependencies: Local MekHQ install and source are available under `external/`. Source build/test commands remain blocked by the Java 17 Gradle daemon/toolchain issue. The user's physical miniature list is needed before final OPFOR restrictions or custom RATs can be built.
- Recommended sequence: `#20` defined the physical miniature roster model, `#18` documented the fixed OPFOR setup-MUL pool path, and `#19` decided not to build custom RATs yet. The next practical roster-control step is user task `#23`: set up a safe MekHQ campaign that reflects the real-life player unit, including mechs, pilots, support staff, equipment, and DropShip/transport assumptions. `#17` remains blocked on live UI, with the smaller quickstart validation task tracked as `#21`.
  - Child issues:
    - `#17`: Verify quickstart roster replacement workflow. Blocked on live MekHQ UI pass after source/safe-copy verification.
    - `#21`: User task: run MekHQ quickstart roster UI validation. Created to unblock `#17`.
    - `#23`: User task: set up real-life unit campaign in MekHQ. Created to produce the realistic campaign save that should precede the issue `#10` manual battle-record MUL import pass.
    - `#18`: Prototype fixed OPFOR MUL pools. Completed on `2026-06-18`; workflow is in `docs/current/FIXED_OPFOR_MUL_POOL_WORKFLOW.md`.
    - `#19`: Decide custom RAT strategy for physical-miniature OPFOR. Completed on `2026-06-18`; recommendation is in `docs/current/CUSTOM_RAT_STRATEGY.md`.
    - `#20`: Define physical-miniature roster data model. Completed on `2026-06-18`; findings are in `docs/current/PHYSICAL_MINIATURE_ROSTER_MODEL.md`.
- Open questions: Which exact miniatures and variants should be considered legal for the player and OPFOR pools? Is it acceptable for MekHQ to generate a scenario and then have the GM substitute close-BV physical units manually, or should generation itself be constrained? Should any tooling live in this workspace, in MekHQ source, or as data-only custom RAT files?

### Epic: Investigate photo-assisted record sheet parsing

- Status: `Done`
- Priority: `Low`
- Issue: `#15`
- Owner: `Mixed`
- Goal: Track a low-priority exploratory idea for using photos of physical BattleTech record sheets to prefill tabletop battle result data for MekHQ result entry.
- Why it matters: If tabletop play becomes regular, photo-assisted parsing could reduce the manual transcription burden for armor/internal pips, pilot hits, and critical-hit line-through marks. It should remain subordinate to the manual result schema and battle-record MUL workflow.
- Expected output: If activated, a feasibility note or prototype comparing OpenCV/template matching, OCR, and modern vision models; a recommendation for unit identity through MekHQ UUIDs, short IDs, QR codes, printed labels, and OCR fallback; and a decision on whether this belongs in a web app, local helper, or later result-entry tool.
- Handoff notes: No active implementation handoff yet. Keep this as backlog until the manual tabletop result schema and battle-record MUL generation workflow are further along.
- Dependencies: Needs sample record sheet photos, known record sheet templates, and a physical-sheet identity strategy. Prefer MekHQ/MUL external IDs, generated short IDs, QR codes, or printed labels over OCR-only matching.
- Open questions: Which record sheet formats should be supported first? Can the tabletop packet include printed IDs or QR codes tied to MekHQ UUIDs? How consistent will marking style be for filled pips, critical-hit line-through marks, and pilot hits? What confidence/review UI is needed before generated results are trusted?

### Explore MEK-RPG and MekHQ campaign bridge

- Priority: `Medium`
- Issue: `#24`
- Owner: `Mixed`
- Goal: Test whether MekHQ can act as a unit-scale logistics and tactical ledger for the sister MEK-RPG workspace without replacing MEK-RPG's narrative campaign memory, starting with a source-backed map of safe bridge primitives.
- Why it matters: MEK-RPG now has read-only MekHQ save summary, campaign bootstrap, pending-action validation, and a manually validated day-advance/re-import loop. The next risk is knowing which fields and hard-ledger actions can be trusted from serialized saves, which require MekHQ method calls, and which are still too GUI-coupled for noninteractive automation.
- Expected output: Completed in `docs/current/MEK_RPG_MEKHQ_BRIDGE_PRIMITIVES.md` with stable read-only export fields, unsafe or derived XML-only fields, pending-action API candidates, GUI/dialog blockers, tactical result artifact paths, and two smallest safe future implementation issue recommendations.
- Handoff notes: Completed on `2026-06-21`. Archived handoff: `docs/handoffs/archive/map-safe-mekhq-bridge-primitives.md`. Recommendation: start future integration with a MekHQ-backed read-only checkpoint exporter. The smallest plausible write-command candidate is a narrow contract-market accept/decline command by stable contract id, but only after sample-save id confirmation and explicit noninteractive prompt policy.
- Dependencies: None for issue `#24` close-out. Future implementation still depends on local MekHQ/MegaMek source and may be limited by the current Java/Gradle toolchain blocker for source builds.
- Open questions: Should the read-only exporter live in MekHQ source, MEK-RPG, or this workspace as a jar-backed helper? Are contract-market offer ids stable enough in representative saves for an accept/decline command? What noninteractive policy should be used for AtB/StratCon contract confirmation, faction-standing greeting, start prompt, and facility-rental prompts?

### Define MekHQ read-only checkpoint export for MEK-RPG

- Status: `Done`
- Priority: `Medium`
- Issue: `#25`
- Owner: `Codex`
- Goal: Define or prototype a MekHQ read-only checkpoint export contract that MEK-RPG can consume without writing to MekHQ saves.
- Why it matters: Issue `#24` established that read-only checkpoint export is the safest next bridge step. MEK-RPG's sync memo asks this workspace to own the MekHQ-side source-backed field contract before either team attempts write automation.
- Expected output: Completed in `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT.md` with MekHQ method/API owners, a recommended JSON export shape, field evidence labels, derived-value notes, unsupported-field warnings, and a validation sequence.
- Handoff notes: Completed on `2026-06-21`. Archived handoff: `docs/handoffs/archive/define-mekhq-read-only-checkpoint-export.md`. The contract keeps the first bridge read-only, recommends a MekHQ-backed DTO over raw XML as the long-term exporter, and leaves headless day advancement plus pending-action writeback out of scope.
- Dependencies: Local MekHQ/MegaMek source checkouts. Source builds may remain blocked by the local Java/Gradle toolchain state, so source inspection and documentation/prototype planning are acceptable.
- Open questions: Should the first implementation live in MekHQ source, this workspace as a jar-backed helper, or MEK-RPG as a consumer adapter? Which disposable save should be used for the first contract-vs-helper validation pass?

### Epic: Deliver MekHQ read-only checkpoint export for MEK-RPG

- Status: `Done`
- Priority: `Medium`
- Issue: `#26`
- Owner: `Codex`
- Goal: Deliver the MegaMek-side artifacts needed for MEK-RPG to consume a future MekHQ-owned read-only checkpoint export.
- Why it matters: MEK-RPG has a consumer contract from issue `#67`; the MegaMek workspace now needs concrete fixture, validation, and prototype-exporter work so the RPG team can build against stable examples instead of only prose.
- Expected output: Completed with sanitized fixture, disposable-save validation, jar-backed read-only exporter prototype, and final epic review.
- Handoff notes: Completed on `2026-06-21`. Final review: `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EPIC_REVIEW.md`. Keep future write-side work separate; this epic added no write automation, direct save mutation, or headless day advancement.
- Dependencies: MEK-RPG consumer contract `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_READ_ONLY_CHECKPOINT_EXPORT_CONTRACT.md`; MegaMek docs `MEK_RPG_MEKHQ_CHECKPOINT_EXPORT.md` and `MEK_RPG_MEKHQ_CHECKPOINT_EXPORT_SCHEMA.md`; local MekHQ source/install under `external/`.
- Child issues:
  - `#27`: Create sanitized MekHQ checkpoint export fixture. Completed on `2026-06-21`; fixture is `docs/templates/mekhq-read-only-checkpoint.fixture.json`; archived handoff: `docs/handoffs/archive/create-mekhq-checkpoint-fixture.md`.
  - `#28`: Validate MekHQ checkpoint schema against disposable save. Completed on `2026-06-21`; validation note is `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_VALIDATION.md`; archived handoff: `docs/handoffs/archive/validate-mekhq-checkpoint-schema.md`.
  - `#29`: Prototype read-only MekHQ checkpoint exporter. Completed on `2026-06-21`; prototype is `tools/mekhq-checkpoint-exporter/`; findings are in `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORTER_PROTOTYPE.md`; archived handoff: `docs/handoffs/archive/prototype-mekhq-checkpoint-exporter.md`.
- Recommended sequence: Complete. Future production exporter ownership should be reviewed as a new issue if needed.
- Open questions: None for issue `#26`; possible future source-owned exporter work is outside this epic.
- Post-review MEK-RPG queue: `Confirmed from MEK-RPG docs`: MEK-RPG completed consumer-side epic `walt-raymond-williams/mek-rpg#84` and child issues `#85` through `#89`. Completed feedback says the top-level shape is acceptable, trust-boundary fields must stay, location values need stable display/id fields, active contract terms need deeper `Contract` getter extraction, warnings/unsupported entries must remain first-class, and markets remain display/opportunity-only. Market-selector and automation-adjacent work remains blocked on stable source-confirmed identifiers; write-side actions remain out of scope for this read-only checkpoint queue.

### Create sanitized MekHQ checkpoint export fixture

- Status: `Done`
- Priority: `High`
- Issue: `#27`
- Owner: `Codex`
- Goal: Create a sanitized JSON fixture matching MEK-RPG's read-only checkpoint consumer shape.
- Why it matters: MEK-RPG can build adapter and bootstrap tests against a fake but realistic payload before a real MekHQ exporter exists.
- Expected output: Completed with `docs/templates/mekhq-read-only-checkpoint.fixture.json`, using fake names/ids, method-backed provenance fields, warnings, duplicate-selector market examples, and unsupported entries.
- Handoff notes: Completed on `2026-06-21`. Archived handoff: `docs/handoffs/archive/create-mekhq-checkpoint-fixture.md`.
- Dependencies: None beyond current docs. No live save or source build required.
- Open questions: None for issue `#27`; duplicate-looking unit-market offers are included to test selector warnings.

### Validate MekHQ checkpoint schema against disposable save

- Status: `Done`
- Priority: `Medium`
- Issue: `#28`
- Owner: `Codex`
- Goal: Compare the draft checkpoint schema and current MEK-RPG helper output against a disposable MekHQ save and, where possible, MekHQ UI facts.
- Why it matters: Validation should identify gaps before the prototype exporter locks in JSON field names or unsupported-field semantics.
- Expected output: Completed with `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_VALIDATION.md`; schema/export docs now link the validation note. UI comparison is recorded as not completed because no safe user-operated UI pass was available.
- Handoff notes: Completed on `2026-06-21`. Archived handoff: `docs/handoffs/archive/validate-mekhq-checkpoint-schema.md`.
- Dependencies: A disposable MekHQ save or safe copied sample save. MekHQ UI comparison may require user operation if automated UI control remains unavailable.
- Open questions: None for issue `#28`; future UI spot-checking can use a disposable copy if the user wants stricter visual validation.

### Prototype read-only MekHQ checkpoint exporter

- Status: `Done`
- Priority: `Medium`
- Issue: `#29`
- Owner: `Codex`
- Goal: Prototype or precisely plan a read-only exporter that loads a MekHQ campaign through MekHQ code or installed jars and emits JSON matching the checkpoint schema.
- Why it matters: A method-backed exporter is the long-term way to provide MEK-RPG exact derived values such as balance, unit market price, personnel salary, damage state, logistics summaries, and sanitized reports.
- Expected output: Completed with jar-backed prototype `tools/mekhq-checkpoint-exporter/` and findings in `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORTER_PROTOTYPE.md`.
- Handoff notes: Completed on `2026-06-21`. Archived handoff: `docs/handoffs/archive/prototype-mekhq-checkpoint-exporter.md`.
- Dependencies: Local MekHQ source/install. Existing Gradle/source build blocker may prevent source-level implementation; jar-backed helper feasibility needs discovery.
- Open questions: Installed jars can load a campaign through a workspace helper after MekHQ data initialization; future production work should decide whether to move this into MekHQ source to avoid reflection and external helper packaging limits.

### Epic: Harden MekHQ checkpoint exporter after MEK-RPG adapter feedback

- Status: `Done`
- Priority: `Medium`
- Issue: `#30`
- Owner: `Codex`
- Goal: Turn MEK-RPG's completed checkpoint adapter experiment feedback into producer-side MegaMek schema/exporter hardening work.
- Why it matters: MEK-RPG issues `walt-raymond-williams/mek-rpg#84` through `#89` closed the feedback loop that previously blocked MegaMek-side hardening. The workspace should now convert that feedback into a tighter read-only exporter shape before considering production ownership.
- Expected output: Completed with updated MegaMek checkpoint docs, hardened jar-backed prototype output, repeatable smoke check, and production ownership decision.
- Handoff notes: Archived epic handoff: `docs/handoffs/archive/harden-mekhq-checkpoint-exporter-epic.md`.
- Dependencies: Completed MEK-RPG issues `#85` through `#89`; local docs under `C:\Users\waltr\Documents\mek-rpg\docs\current\`; local MekHQ source/install under `external/`; source build remains limited by the Java/Gradle blocker.
- Child issues:
  - `#31`: Reconcile MegaMek checkpoint docs with completed MEK-RPG feedback. Completed; archived handoff: `docs/handoffs/archive/reconcile-mekhq-checkpoint-feedback.md`.
  - `#32`: Harden jar-backed MekHQ checkpoint exporter output against MEK-RPG feedback. Completed; archived handoff: `docs/handoffs/archive/harden-mekhq-checkpoint-exporter-output.md`.
  - `#33`: Decide production ownership path for MekHQ checkpoint exporter. Completed; archived handoff: `docs/handoffs/archive/decide-mekhq-checkpoint-production-ownership.md`.
- Recommended sequence: Complete. Keep the workspace helper as experimental tooling for now; defer MekHQ source movement until a separately scoped source issue is justified.
- Open questions: None for issue `#30`; future source ownership can be reopened through the triggers in `MEK_RPG_MEKHQ_CHECKPOINT_EXPORTER_OWNERSHIP_DECISION.md`.

### Reconcile MegaMek checkpoint docs with completed MEK-RPG feedback

- Status: `Done`
- Priority: `Medium`
- Issue: `#31`
- Owner: `Codex`
- Goal: Replace pending-feedback language in MegaMek checkpoint docs with concrete results from completed MEK-RPG issues `#84` through `#89`.
- Why it matters: Future agents should not think the checkpoint exporter is still waiting on MEK-RPG consumed-field mapping or warning policy; that feedback now exists and should guide implementation.
- Expected output: Completed by updating checkpoint review/schema/export docs and roadmap/task notes with MEK-RPG's consumed-field map, warning-surfacing policy, adapter-test results, edge-fixture coverage, and schema-hardening feedback.
- Handoff notes: Archived handoff: `docs/handoffs/archive/reconcile-mekhq-checkpoint-feedback.md`.
- Dependencies: MEK-RPG docs `MEKHQ_CHECKPOINT_CROSS_BOARD_TRACKING_PROPOSAL.md`, `MEKHQ_CHECKPOINT_CONSUMED_FIELD_MAPPING.md`, and `MEKHQ_CHECKPOINT_WARNING_SURFACING.md`.
- Open questions: None for `#31`; implementation work continues in exporter hardening issue `#32`.

### Harden jar-backed MekHQ checkpoint exporter output against MEK-RPG feedback

- Status: `Done`
- Priority: `Medium`
- Issue: `#32`
- Owner: `Codex`
- Goal: Improve the workspace jar-backed exporter so its output better matches MEK-RPG's consumed-field mapping and warning policy.
- Why it matters: MEK-RPG adapter tests now prove the shape is consumable, but the producer still has known rough edges such as object-string location values, shallow active contract terms, and missing repeatable smoke checks.
- Expected output: Completed by hardening prototype location output into stable display/id fields, adding method-backed `Contract` getter terms, preserving market selector warnings and mandatory `unsupported` entries, adding smoke verification, and updating prototype/schema docs.
- Handoff notes: Archived handoff: `docs/handoffs/archive/harden-mekhq-checkpoint-exporter-output.md`.
- Dependencies: Completed documentation reconciliation; disposable copied save for prototype verification, or a recorded blocker if absent.
- Open questions: None for `#32`; remaining production ownership tradeoffs continue in issue `#33`.

### Decide production ownership path for MekHQ checkpoint exporter

- Status: `Done`
- Priority: `Medium`
- Issue: `#33`
- Owner: `Codex`
- Goal: Decide whether the read-only checkpoint exporter should move into MekHQ source, remain a workspace helper, or stay experimental until more campaign use exists.
- Why it matters: Source movement has maintenance and verification costs. The decision should use hardened prototype evidence instead of jumping straight from consumer feedback to source implementation.
- Expected output: Completed with `MEK_RPG_MEKHQ_CHECKPOINT_EXPORTER_OWNERSHIP_DECISION.md`, recommending the workspace helper remain experimental near-term and deferring MekHQ source movement to a later separately scoped issue.
- Handoff notes: Archived handoff: `docs/handoffs/archive/decide-mekhq-checkpoint-production-ownership.md`.
- Dependencies: Hardened prototype output issue should complete first.
- Open questions: None for `#33`; upstream/source acceptance remains a future question only if a source-change issue is opened.

### Spike source-level MekHQ GUI control seam for Advance Day

- Status: `Done`
- Priority: `Medium`
- Issue: `#34`
- Owner: `Codex`
- Goal: Determine whether a locally modified MekHQ build can expose a safe, source-level control seam that invokes the real Advance Day GUI action path without coordinate clicking, direct save editing, or reimplementing campaign business logic.
- Why it matters: Advance Day exercises MekHQ's actual daily campaign lifecycle, including repairs, maintenance, healing, personnel changes, payroll, procurement, contract events, market changes, reports/logging, scenario or StratCon progression, and prompts requiring user decisions. This is the right first write-side automation spike because MekHQ should remain the hard campaign ledger.
- Expected output: Completed with `docs/current/MEKHQ_ADVANCE_DAY_GUI_CONTROL_SEAM_SPIKE.md`.
- Handoff notes: Archived handoff: `docs/handoffs/archive/spike-mekhq-advance-day-gui-control-seam.md`. The source path is `CampaignGUI#initTopPanel()` -> `AdvanceTimePanel` -> `CampaignController#advanceDay()` -> `Campaign#newDay()` -> `CampaignNewDayManager#newDay()`. Recommendation: proceed with a narrow local-only in-process MekHQ GUI command prototype, not a detached headless helper.
- Dependencies: Future prototype validation should use copied/disposable saves, preferably after or alongside user task `#23`. Source build/test verification may still be blocked by the Java 17 Gradle daemon/toolchain issue. Existing issues `#10`, `#13`, and `#17` remain separate and blocked on live UI/user validation.
- Open questions: What exact local transport should expose the in-process command, and which copied save should be used for the first prototype validation?

### Implement local MekHQ Advance Day control API prototype

- Status: `Done`
- Priority: `Medium`
- Issue: `#35`
- Owner: `Codex`
- Goal: Implement or precisely prototype-plan a narrow local-only MekHQ source control API that can be called while MekHQ is already open with a campaign loaded, invoking exactly one real `Campaign#newDay()` path inside the loaded GUI app.
- Why it matters: Issue `#34` proved the concept is viable but brittle. The next useful step is to turn that conclusion into a callable local control seam so Codex or another local helper can drive MekHQ through source-owned logic instead of screen coordinates or save editing, while preserving MekHQ as the hard campaign ledger.
- Expected output: Completed with source-backed prototype plus `docs/current/MEKHQ_ADVANCE_DAY_CONTROL_API_PROTOTYPE.md`, documenting the API transport, input/output contract, source files touched, safety checks, Gradle compile verification, and live user-assisted test instructions. Initial implementation plan: `docs/current/MEKHQ_ADVANCE_DAY_CONTROL_API_IMPLEMENTATION_PLAN.md`.
- Handoff notes: Archived handoff: `docs/handoffs/archive/implement-mekhq-advance-day-control-api.md`. Local source prototype committed in `external/src/mekhq` on branch `codex/mekhq-advance-day-control-api` at `9046a8075e`; it adds `mekhq.service.LocalControlService` and wires it into `MekHQ`, starts only with `-Dmekhq.controlApi.enabled=true`, binds to `127.0.0.1`, and exposes `/status` plus `POST /advance-day`. `.\gradlew.bat :MekHQ:compileJava` passed on `2026-06-22`.
- Dependencies: MekHQ must already be open with a campaign loaded for live endpoint validation. Live prototype testing should wait for the user to be present. Use only copied/disposable saves for execution tests.
- Open questions: Can visible modal/prompt detection be made reliable enough without a general prompt policy layer? Should this prototype stay local-only after live validation, or become the first member of a broader local control API?

### Implement live read-only MekHQ campaign state API for MEK-RPG

- Status: `Done`
- Priority: `High`
- Issue: `#36`
- Owner: `Codex`
- Goal: Extend the local-only MekHQ control API with read-only live campaign-state endpoints for MEK-RPG: `GET /campaign/summary` and `GET /campaign/state?sections=...`.
- Why it matters: The Advance Day prototype proved MekHQ can expose a local in-process API while the GUI app is open. MEK-RPG's response memo confirms it wants live API data as a freshness layer over save/checkpoint imports, preserving MekHQ as the hard ledger while reducing stale reads and save-before-refresh friction.
- Expected output: Completed with MekHQ source commit `7d3b345327` and `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`, plus sanitized fixtures under `docs/templates/mekhq-live-campaign-*.fixture.json`.
- Handoff notes: Completed on `2026-06-22`. Archived handoff: `docs/handoffs/archive/implement-live-mekhq-campaign-state-api.md`. The API remains disabled by default, loopback-only, read-only for live state, and grouped like the checkpoint export. `.\gradlew.bat :MekHQ:compileJava` and `.\gradlew.bat :MekHQ:checkstyleMain` passed. Follow-up issue `#37` tracks the workspace close-out for commit `6756a70`, which recorded the later user-assisted disposable-campaign smoke test and the selected-section `bridge_metadata` lesson from MEK-RPG issue `#106`. Source push is blocked by lack of write permission to upstream `MegaMek/mekhq`.
- Dependencies: Existing source branch `codex/mekhq-advance-day-control-api`; source commits `9046a8075e`, `17207baa90`, and `7d3b345327`. The first user-assisted live endpoint smoke test is recorded in `docs/current/MEK_RPG_LIVE_MEKHQ_API_PROTOTYPE.md`.
- Open questions: A source-confirmed dirty/unsaved flag remains unknown, so V1 reports dirty state as `Unknown` with a warning. Future passes should decide whether to deepen full injuries, skills, cargo/transport, markets, and stable repair selectors.

### Record live MekHQ API smoke-test follow-up

- Status: `Done`
- Priority: `Medium`
- Issue: `#37`
- Owner: `Codex`
- Goal: Track and close the after-the-fact documentation cleanup for workspace commit `6756a70`, which recorded live smoke-test evidence for the issue `#36` read-only MekHQ campaign-state API.
- Why it matters: The roadmap and task board should not keep telling future agents that the live API still lacks any disposable-campaign smoke test after MEK-RPG issue `#104` and follow-up issue `#106` provided that evidence.
- Expected output: Completed by updating `docs/current/TASKS.md`, this roadmap, and the archived issue `#36` handoff to reflect the live smoke result and the need to include `bridge_metadata` in selected-section MEK-RPG dashboard/context requests.
- Handoff notes: This was a close-out bookkeeping issue related to closed workspace issue `#36`, not a new source/API feature and not related to any currently open workspace issue.
- Dependencies: Workspace commit `6756a70`; MEK-RPG issues `#104` and `#106`.
- Open questions: None for this tracking issue.

### Epic: Expand live MekHQ campaign API for MEK-RPG

- Status: `Done`
- Priority: `High`
- Issue: `#38`
- Owner: `Mixed`
- Goal: Turn MEK-RPG's live adapter feedback into producer-side MegaMek/MekHQ live API work so MEK-RPG can load and refresh an active MekHQ campaign from `GET /campaign/summary` and `GET /campaign/state` without parsing `.cpnx`, `.cpnx.gz`, or XML saves.
- Why it matters: MEK-RPG issue `#107` proved the consumer should use the live local read-only API for loaded campaigns, but its change request identifies producer-side gaps in trust metadata, dirty-state reporting, location labels, unsupported entries, finance/personnel/unit depth, contracts/scenarios, logistics/reports, and display-only market safeguards.
- Expected output: Completed through child issues `#39` through `#42`. The live API now has hardened trust/location metadata, deeper finance/personnel/unit context, deeper contract/scenario context, and logistics/report/market display-only safeguards. The read-only, loopback-only, disabled-by-default API boundary remains intact.
- Handoff notes: MEK-RPG handoff source is `C:\Users\waltr\Documents\mek-rpg\docs\current\MEGAMEK_LIVE_API_CHANGE_REQUEST.md`. Archived child handoffs record the completed execution.
- Dependencies: Completed issue `#36` live API prototype, follow-up issue `#37` selected-section `bridge_metadata` lesson, and the MekHQ source branch/commits recorded in the issue `#36` roadmap entry. Source push to upstream `MegaMek/mekhq` remains blocked unless a writable fork/remote is configured.
- Child issues:
  - `#39`: Harden live API trust envelope, dirty state, and location labels. Completed on `2026-06-22`; archived handoff: `docs/handoffs/archive/harden-live-api-trust-envelope-location.md`.
  - `#40`: Deepen live API finance, personnel, and unit sections. Completed on `2026-06-22`; archived handoff: `docs/handoffs/archive/deepen-live-api-finance-personnel-units.md`.
  - `#41`: Add live API contract and scenario depth with fixtures. Completed on `2026-06-22`; archived handoff: `docs/handoffs/archive/deepen-live-api-contracts-scenarios.md`.
  - `#42`: Deepen live API logistics, reports, and market safeguards. Completed on `2026-06-22`; archived handoff: `docs/handoffs/archive/deepen-live-api-logistics-reports-markets.md`.
- Recommended sequence: Complete. Future live API expansion should be opened as narrower follow-up issues after MEK-RPG consumes these fields against a real campaign.
- Open questions: Should a writable MekHQ fork/remote be configured for source branch publication?

### Epic: Stabilize live MekHQ API reliability for MEK-RPG play

- Status: `Done`
- Priority: `High`
- Issue: `#62`
- Owner: `Mixed`
- Goal: Make the local MekHQ control API reliable enough for live MEK-RPG play, especially for fast current-state facts that prevent stale scene framing.
- Why it matters: During live MEK-RPG play on `2026-06-26`, `/campaign/summary`, narrowed `/campaign/state` reads, and `/campaign/commands` timed out. That blocked confirmation of current pending operations, Michelle "Double-M" Moreno's operation commitment, unit/repair pressure, and command readiness. The table had to fall back to stale notes until the player corrected the current operations.
- Expected output: A decomposed reliability workstream covering source-backed timeout diagnosis, bounded summary/commands behavior, lazy or partial state section collection, a lightweight pending-deployments read path, and regression/live-smoke verification.
- Handoff notes: MEK-RPG incident source is `C:\Users\waltr\Documents\mek-rpg\docs\current\MEGAMEK_API_RELIABILITY_HANDOFF_2026-06-26.md`. Tracking note: `docs/current/MEK_RPG_LIVE_MEKHQ_API_RELIABILITY_TRACKING.md`. Timeout audit note: `docs/current/MEK_RPG_LIVE_MEKHQ_API_TIMEOUT_AUDIT.md`. Implementation child issues `#63` through `#67` are closed; user-assisted live GUI smoke issue `#68` passed on `2026-06-26`, and epic `#62` is closed.
- Dependencies: Completed live read-only API epic `#38`, guarded command API work `#44` through `#55`, current activity-history epic `#56`, and local MekHQ source under `external/src/mekhq`. Source pushes from `external/src/mekhq` may still need a writable fork/remote.
- Child issues:
  - `#63`: Audit live MekHQ API timeout sources and add collector timing instrumentation. Completed on `2026-06-26`; archived handoff: `docs/handoffs/archive/audit-live-mekhq-api-timeouts.md`; source commit `5effaa5517`.
  - `#64`: Keep MekHQ summary and command readiness endpoints fast and bounded. Completed on `2026-06-26`; archived handoff: `docs/handoffs/archive/bound-mekhq-summary-commands-endpoints.md`; source commit `9ad8fa5f4a`.
  - `#65`: Make live MekHQ state section filtering lazy and partial-response capable. Completed on `2026-06-26`; archived handoff: `docs/handoffs/archive/lazy-mekhq-state-sections-partial-responses.md`; source commit `72424e4d9c`.
  - `#66`: Expose lightweight pending scenario and deployment commitment data. Completed on `2026-06-26`; archived handoff: `docs/handoffs/archive/expose-mekhq-pending-deployments-endpoint.md`; source commit `ba865793c5`.
  - `#67`: Add live MekHQ API reliability regression tests and smoke checklist. Completed on `2026-06-26`; archived handoff: `docs/handoffs/archive/add-live-mekhq-api-reliability-tests.md`; source commit `81afcee70a`; smoke checklist: `docs/current/MEK_RPG_LIVE_MEKHQ_API_RELIABILITY_SMOKE_CHECKLIST.md`.
- Follow-up:
  - `#68`: User task: run live MekHQ API reliability smoke checklist. Completed on `2026-06-26`; live smoke passed against source-built MekHQ with loaded campaign `The Learning Ropes`.
- Recommended sequence: Complete. Issues `#64` and `#65` bounded command readiness and made narrowed state responses partial for section collector failures, issue `#66` added the purpose-built pending-deployment read path, issue `#67` locked in automated regression coverage and a smoke checklist, and issue `#68` confirmed the live loaded-campaign read path did not time out.
- Open questions: Java-level per-section timeout cancellation remains deferred because timed-out background collectors could keep reading live campaign state concurrently. Source pushes from `external/src/mekhq` remain blocked until a writable fork/remote is configured.

### Investigate MegaMek live combat narration bridge

- Status: `Issue created`
- Priority: `Medium`
- Issue: `#78`
- Owner: `Codex`
- Goal: Determine the best V1 architecture for a MegaMek-side live combat narration bridge that can observe tactical game state and emit concise narration or pilot dialogue into MegaMek chat or an external MEK-RPG consumer.
- Why it matters: MEK-RPG can now read live MekHQ campaign state, but tactical battles launched into MegaMek are still a separate live runtime. A combat observer/narrator would let the campaign assistant react to movement, attacks, damage, pilot injuries, destruction, phase changes, and victory while the battle is being played.
- Expected output: A source-backed feasibility/design note under `docs/current/` that compares an external observer client, built-in bot/client mode, server-side hook, MekHQ-launched companion, and any local API option; identifies event/chat entry points; recommends a V1 path; and proposes follow-up issues only if implementation is ready.
- Handoff notes: Active handoff: `docs/handoffs/active/investigate-megamek-combat-narration-bridge.md`. A duplicate check on `2026-06-28` found no open or closed GitHub issue and no roadmap entry specifically for live MegaMek observer/chat narration. Initial source pass found plausible hooks in `AbstractClient#sendChat(...)`, `GameListener`, `GameReportEvent`, bot-client listener patterns, and server observer handling, but no running observer proof yet.
- Dependencies: Existing MEK-RPG live MekHQ read/command work for campaign context; local MegaMek source under `external/src/megamek`; local MekHQ source under `external/src/mekhq` for launch/entity identity mapping; a future running MegaMek scenario for live smoke validation.
- Open questions: Can an observer client receive enough event/report detail without violating double-blind expectations? Can MegaMek entities be reliably mapped back to MekHQ campaign units and pilots during a launched scenario? Should V1 write narration into MegaMek chat only, an external stream/log/API only, or both? Should V1 be a general narrator before per-pilot dialogue?

### Harden live API trust envelope, dirty state, and location labels

- Status: `Done`
- Priority: `High`
- Issue: `#39`
- Owner: `Codex`
- Goal: Harden the existing read-only live MekHQ campaign API around `bridge_metadata`, dirty/unsaved-state reporting, selected-section metadata, structured unsupported entries, and stable current system/location labels.
- Why it matters: MEK-RPG now validates live API responses through `bridge_metadata`; sparse responses or object-string location values make the adapter less reliable and can push consumers toward stale save-derived context.
- Expected output: Completed with MekHQ source commit `dc214d946d`, which keeps dirty state explicit `Unknown` with stronger unsupported metadata, builds location labels from `Campaign#getCurrentLocation()` and `AbstractLocation` methods instead of `toString()`, adds `current_location_display_name`, `table_safe_location_label`, `current_planet_name`, and expanded `travel_state` fields, and updates live API fixtures/docs.
- Handoff notes: Completed on `2026-06-22`. Archived handoff: `docs/handoffs/archive/harden-live-api-trust-envelope-location.md`. `.\gradlew.bat :MekHQ:compileJava` and `.\gradlew.bat :MekHQ:checkstyleMain` passed from `external/src/mekhq`.
- Dependencies: Epic `#38`; current live API prototype from issue `#36`; MEK-RPG issue `#107` adapter behavior and issue `#109` change request package.
- Open questions: No campaign-wide dirty/unsaved flag was found in this pass; future source work can revisit if MekHQ later exposes one.

### Deepen live API finance, personnel, and unit sections

- Status: `Done`
- Priority: `Medium`
- Issue: `#40`
- Owner: `Codex`
- Goal: Expose richer method-backed finance, personnel, and unit context through the live read-only API.
- Why it matters: MEK-RPG needs active campaign operational context such as balance/debt, personnel availability/injuries/pay, and unit condition/crew/repair/transport links without reading raw saves.
- Expected output: Completed with MekHQ source commit `d38a500960`, which adds finance loan/default/warning summaries, personnel assignment/leadership/injury-summary/current-personnel membership fields, and unit availability/deployability/maintenance/transport summaries. Updated live API docs and fixtures.
- Handoff notes: Completed on `2026-06-22`. Archived handoff: `docs/handoffs/archive/deepen-live-api-finance-personnel-units.md`. `.\gradlew.bat :MekHQ:compileJava` and `.\gradlew.bat :MekHQ:checkstyleMain` passed from `external/src/mekhq`.
- Dependencies: Epic `#38`; preferably issue `#39` first so the trust envelope and unsupported-entry shape are settled.
- Open questions: Full personnel skill export, detailed injury objects/recovery timelines, and write-side transport reassignment remain deferred.

### Add live API contract and scenario depth with fixtures

- Status: `Done`
- Priority: `Medium`
- Issue: `#41`
- Owner: `Codex`
- Goal: Expand live read-only API coverage for active contracts and scenarios and add richer live fixtures for MEK-RPG adapter tests.
- Why it matters: MEK-RPG needs contract/scenario context for real operational play, including employer/enemy, locations, deadlines, payment/salvage summaries, scenario links, participants, objectives, reports, and tactical-result pointers where available.
- Expected output: Completed with MekHQ source commit `495b58faef`, which adds contract description/date/travel/payment/salvage/rental/scenario-link fields and scenario description/link/map/conditions/player-force/salvage/objective/bot-force/tactical-result context. Updated live API docs and active-contract/scenario-rich fixture examples.
- Handoff notes: Completed on `2026-06-22`. Archived handoff: `docs/handoffs/archive/deepen-live-api-contracts-scenarios.md`. `.\gradlew.bat :MekHQ:compileJava` and `.\gradlew.bat :MekHQ:checkstyleMain` passed from `external/src/mekhq`.
- Dependencies: Epic `#38`; preferably issue `#39` first; disposable campaign data with active contract/scenario context.
- Open questions: Tactical result application remains out of scope; live scenario context reports status/report/objective pointers only.

### Deepen live API logistics, reports, and market safeguards

- Status: `Done`
- Priority: `Medium`
- Issue: `#42`
- Owner: `Codex`
- Goal: Deepen repairs/logistics/report coverage while keeping market data display-only with explicit automation safeguards.
- Why it matters: MEK-RPG needs repair/acquisition pressure and categorized reports for tabletop context, but action-adjacent market data must not imply purchase, hire/fire, accept/decline, repair execution, save, or writeback support.
- Expected output: Completed with MekHQ source commit `911a338788`, which adds repair pressure counts, display-only repair queue rows, shopping-list pressure/rows, cargo/transport relationship summaries, report metadata/counts, market summaries/rows, and explicit automation guards/unsupported entries for selectors and mutation commands.
- Handoff notes: Completed on `2026-06-22`. Archived handoff: `docs/handoffs/archive/deepen-live-api-logistics-reports-markets.md`. `.\gradlew.bat :MekHQ:compileJava` and `.\gradlew.bat :MekHQ:checkstyleMain` passed from `external/src/mekhq`.
- Dependencies: Epic `#38`; preferably issue `#39` first; representative disposable campaign data with damage, repairs, acquisition pressure, reports, or market entries.
- Open questions: Stable repair work item ids and market offer selectors remain unavailable for automation; future command work needs a separate selector and prompt-policy design.

### Epic: Investigate richer MekHQ activity history API

- Status: `In progress`
- Priority: `Medium`
- Issue: `#56`
- Owner: `Mixed`
- Goal: Investigate and plan how the local MekHQ API should expose richer campaign activity histories beyond the current daily report samples.
- Why it matters: MEK-RPG and campaign-assistant workflows need more than the current report slice: they need timeline-aware history for campaign reports, personnel service/medical/scenario/assignment/performance events, finance transactions, scenario outcomes, maintenance notes, and other activity sources without parsing raw saves or confusing campaign ledger data with application/debug logs.
- Expected output: A source-backed design and child issue queue for exposing activity histories through the local MekHQ API while preserving the local-only, read-only safety posture. The investigation should decide which histories belong in `GET /campaign/state`, which deserve separate endpoints, and what date ranges, limits, filters, sanitization, evidence labels, and unsupported entries are required.
- Handoff notes: Created as broad epic issue `#56`; do not assign an agent to implement it directly. Expanded on `2026-06-23` with tracking note `docs/current/MEK_RPG_LIVE_MEKHQ_ACTIVITY_HISTORY_API_TRACKING.md` and child issues `#57` through `#61`. Audit issue `#57` is complete; start design issue `#58` next.
- Dependencies: Completed live read-only API epic `#38`, current guarded command epic `#44`, and source patterns in `LocalCampaignStateExporter`, `Campaign#addReport(...)`, `HistoricalDailyReportDialog`, `Person` log getters, and `LogEntryType`.
- Child issues:
  - `#57`: Audit MekHQ activity-history source owners. Completed on `2026-06-23`; audit note: `docs/current/MEK_RPG_LIVE_MEKHQ_ACTIVITY_HISTORY_SOURCE_AUDIT.md`; archived handoff: `docs/handoffs/archive/audit-mekhq-activity-history-sources.md`.
  - `#58`: Design read-only MekHQ activity-history API. Next recommended agent issue; active handoff: `docs/handoffs/active/design-mekhq-activity-history-api.md`.
  - `#69`: Investigate All Reports UI warning dependency in MekHQ API. Created on `2026-06-27`; active handoff: `docs/handoffs/active/investigate-all-reports-ui-warning-api-dependency.md`.
  - `#59`: Implement historical daily report activity export. Wait for `#57` and `#58`; active handoff: `docs/handoffs/active/implement-mekhq-historical-daily-report-history.md`.
  - `#60`: Implement MekHQ per-person activity log export. Wait for `#57` and `#58`, preferably after `#59`; active handoff: `docs/handoffs/active/implement-mekhq-person-activity-log-export.md`.
  - `#61`: Add MekHQ activity-history fixtures and tests. Wait for `#58` and implementation slices unless folded into each implementation issue; active handoff: `docs/handoffs/active/add-mekhq-activity-history-fixtures-tests.md`.
- Open questions: Should activity history be a new `history` state section, a dedicated `/campaign/history` endpoint, or both? How far back should default exports go? Should per-person medical and patient logs require opt-in sections or explicit target filters?

### Expose Personnel tab character details through the live MekHQ API

- Status: `Done`
- Priority: `High`
- Issue: `#82`
- Owner: `Codex`
- Goal: Ensure the local read-only MekHQ API can expose the same character-facing information a user sees when selecting a person in the MekHQ Personnel tab, including identity, role/status, assignments, traits/options, roleplay and combat skills, personal/service logs, medical/assignment/scenario/performance logs where safe, special abilities, awards, and other profile details.
- Why it matters: MEK-RPG and campaign-assistant workflows need character sheets and roleplay context from MekHQ without requiring direct UI scraping, raw save parsing, or manual copy/paste from the Personnel tab. MekHQ should remain the source-owned roster ledger while consumers get bounded, sanitized, source-backed person detail data.
- Expected output: Completed with MekHQ source commit `b68bc1b8ca`, which adds `GET /campaign/personnel/detail?personId=<uuid>` with explicit person selection, read-only envelope metadata, core identity/status/assignment facts, skills, active options/special abilities, award summary, injury summary, and bounded log families. Medical and patient logs require explicit `includeMedical=true` / `includePatient=true`.
- Handoff notes: Completed on `2026-06-30`. Source audit and endpoint design are in `docs/current/MEK_RPG_LIVE_MEKHQ_PERSONNEL_DETAIL_API.md`; archived handoff: `docs/handoffs/archive/expose-personnel-tab-character-details-api.md`. This remains related to activity-history epic `#56`, but the broader history contract is still owned by issue `#58`.
- Dependencies: Completed live read-only API work, current `LocalCampaignStateExporter` and `/campaign/state` personnel section, activity-history audit `#57`, pending activity-history design `#58`, and local MekHQ source under `external/src/mekhq`.
- Open questions: Future work can add individual award metadata, kill-log rows, family-tree details, and richer injury/treatment objects after the activity-history and privacy contracts are settled. Source push remains blocked because `external/src/mekhq` points at upstream `MegaMek/mekhq` and GitHub returned `403`.

### Investigate All Reports UI warning dependency in MekHQ API

- Status: `Issue created`
- Priority: `High`
- Issue: `#69`
- Owner: `Codex`
- Goal: Determine whether the local MekHQ API uses the same UI-facing "All Reports" path that the user observed showing warnings over or near its buttons, and whether that dependency creates inefficiency, timeout risk, or UI-warning leakage.
- Why it matters: The API should expose bounded, source-owned report/history data without driving inefficient UI paths or relying on controls that produce confusing warning overlays in MekHQ.
- Expected output: A source-backed note under `docs/current/` that says whether any API endpoint relies on the All Reports UI/report path; if yes, identify affected endpoint(s), risk, and safer alternatives; if no, record the evidence and update relevant API docs/handoffs to resolve the concern.
- Handoff notes: Active handoff: `docs/handoffs/active/investigate-all-reports-ui-warning-api-dependency.md`. This was created from user-observed UI behavior only; no source inspection was performed when the issue was created.
- Dependencies: Related to epic `#56`, issue `#58`, and current live MekHQ API docs. The investigation should inspect source when executed and use disposable/read-only live validation only if needed.
- Open questions: Is "All Reports" a UI-only aggregation, a shared report collector, or unrelated to the current API? Are any current read endpoints using a broad report path where a bounded purpose-built report/history export would be safer?

### Discover first guarded live MekHQ command API easy wins for MEK-RPG

- Status: `Done`
- Priority: `High`
- Issue: `#43`
- Owner: `Codex`
- Goal: Convert the post-read-only strategy shift into a source-backed plan for guarded live MekHQ command endpoints that can mutate the already-loaded campaign through MekHQ-owned logic.
- Why it matters: MEK-RPG wants to move beyond read-only freshness into player/RPG-driven actions such as campaign status updates and market purchases. The Advance Day prototype proves MekHQ can safely run a narrow command in-process, but most writes need stable selectors, guard fields, prompt policy, and disposable-campaign verification before they are safe.
- Expected output: Completed with updated `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`. The source-backed recommendation is to implement a guarded campaign status/report note first, because `Campaign#addReport(...)` is lower risk than finance, market, personnel, contract, medical, repair, or scenario mutations.
- Handoff notes: Completed on `2026-06-22`. Archived handoff: `docs/handoffs/archive/discover-live-mekhq-command-api-easy-wins.md`. Follow-up implementation issue: `#50`.
- Dependencies: Completed live read-only API epic `#38`; Advance Day control API issue `#35`; source branch `external/src/mekhq` on `codex/mekhq-advance-day-control-api`; disposable campaign data for any mutation tests.
- Open questions: Resolved by issue `#50` for V1: status-note allows only `GENERAL` and plain text. Future versions can revisit report-type allowlists or source-generated HTML.

### Epic: Guarded live MekHQ command API for MEK-RPG

- Status: `In progress`
- Priority: `High`
- Issue: `#44`
- Owner: `Mixed`
- Goal: Build a guarded write-side local MekHQ command API so MEK-RPG can request high-level campaign mutations while MekHQ remains the source-owned campaign ledger.
- Why it matters: MEK-RPG needs more than read-only state: RPG-side play can buy units or DropShips, change character status, kill a character, resolve medical treatment, apply prosthetics, accept opportunities, or make GM corrections. These actions must use MekHQ logic instead of direct save edits or generic state patches.
- Expected output: A common command envelope, command-readiness/selector discovery, and source-backed command designs for campaign notes, contract selection, unit-market purchase, personnel death/status, and medical/prosthetic treatment, followed by narrow implementation issues once safe selectors and prompt policies are known.
- Handoff notes: Epic handoff: `docs/handoffs/active/guarded-live-mekhq-command-api-epic.md`. Strategy note: `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md`. Feature tracking snapshot: `docs/current/GUARDED_LIVE_MEKHQ_COMMAND_API_TRACKING.md`. This epic should not be implemented directly; child issues own discovery/design/implementation slices.
- Dependencies: Completed Advance Day command prototype `#35`, completed live read-only API epic `#38`, completed discovery issue `#43`, completed first command issue `#50`, completed personnel status command issue `#51`, completed personnel fatigue command issue `#53`, completed unit-market purchase command issue `#54`, MekHQ source branch `codex/mekhq-advance-day-control-api`, and disposable campaign data for mutating tests.
- Child issues:
  - `#43`: Discover first guarded live MekHQ command API easy wins for MEK-RPG. Completed on `2026-06-22`; archived handoff: `docs/handoffs/archive/discover-live-mekhq-command-api-easy-wins.md`.
  - `#45`: Define guarded live MekHQ command envelope and prompt policy. Completed on `2026-06-22`; archived handoff: `docs/handoffs/archive/design-live-mekhq-command-envelope.md`.
  - `#46`: Implement live MekHQ command readiness and selector discovery. Completed on `2026-06-22`; archived handoff: `docs/handoffs/archive/implement-live-mekhq-command-readiness-selectors.md`.
  - `#50`: Implement guarded live MekHQ campaign status-note command. Completed on `2026-06-22`; archived handoff: `docs/handoffs/archive/implement-live-mekhq-status-note-command.md`.
  - `#47`: Design live MekHQ personnel death and status command API. Completed on `2026-06-22`; design note: `docs/current/MEK_RPG_LIVE_MEKHQ_PERSONNEL_STATUS_COMMAND_DESIGN.md`; archived handoff: `docs/handoffs/archive/design-live-mekhq-personnel-status-command.md`.
  - `#51`: Implement guarded live MekHQ personnel status command. Completed on `2026-06-22`; archived handoff: `docs/handoffs/archive/implement-live-mekhq-personnel-status-command.md`.
  - `#48`: Design live MekHQ medical treatment and prosthetic command API. Completed on `2026-06-23`; design note: `docs/current/MEK_RPG_LIVE_MEKHQ_MEDICAL_COMMAND_DESIGN.md`; archived handoff: `docs/handoffs/archive/design-live-mekhq-medical-prosthetic-command.md`.
  - `#53`: Implement guarded live MekHQ personnel fatigue command. Completed on `2026-06-23`; archived handoff: `docs/handoffs/archive/implement-live-mekhq-personnel-fatigue-command.md`.
  - `#49`: Design live MekHQ unit-market purchase command API. Completed on `2026-06-23`; design note: `docs/current/MEK_RPG_LIVE_MEKHQ_UNIT_MARKET_PURCHASE_COMMAND_DESIGN.md`; archived handoff: `docs/handoffs/archive/design-live-mekhq-unit-market-purchase-command.md`.
  - `#54`: Implement guarded live MekHQ unit-market purchase command. Completed on `2026-06-23`; archived handoff: `docs/handoffs/archive/implement-live-mekhq-unit-market-purchase-command.md`.
  - `#52`: Design live MekHQ contract selection command API. Completed on `2026-06-23`; design note: `docs/current/MEK_RPG_LIVE_MEKHQ_CONTRACT_ACCEPT_COMMAND_DESIGN.md`; archived handoff: `docs/handoffs/archive/design-live-mekhq-contract-selection-command.md`.
  - `#55`: Implement guarded live MekHQ contract accept command. Completed on `2026-06-23`; archived handoff: `docs/handoffs/archive/implement-live-mekhq-contract-accept-command.md`.
  - `#79`: Smoke test contract accept prompts and MekHQ UI refresh. Created on `2026-06-29`; active handoff: `docs/handoffs/active/smoke-test-contract-accept-ui-refresh.md`.
  - `#70`: Epic: Add guarded TO&E and pilot assignment commands for MEK-RPG. Created on `2026-06-28`; active handoff: `docs/handoffs/active/toe-pilot-assignment-command-api-epic.md`.
- Recommended sequence: The implemented guarded command slice now covers status-note, personnel status, personnel fatigue, unit-market purchase, contract accept, and pilot/TO&E read selectors. MEK-RPG live play exposed a command gap for pilot assignment and TO&E edits; focused epic `#70` now has source audit `#71`, command design `#72`, and selector issue `#73` complete. Continue with guarded implementation issues `#74` and `#75`, then fixture/smoke issue `#77`; keep batch issue `#76` optional until individual commands are stable. Issue `#79` should be run as soon as a source-built MekHQ instance has a copied campaign with a selectable contract offer, because it verifies the high-risk contract prompt chain and visible UI refresh behavior. Issues `#51`, `#53`, and `#54` still need live disposable-campaign smoke testing when representative copied campaigns are available.
- Open questions: Which medical/prosthetic state is available under the user's active MekHQ options? Does contract acceptance through the non-dialog API path automatically refresh visible MekHQ contract market, briefing, report, and finance panels, or does it need an explicit source refresh/event hook? For TO&E and pilot assignment, implementation should use extracted/shared validators or a narrow command service with focused tests before calling low-level mutation methods.

### Epic: Add guarded TO&E and pilot assignment commands for MEK-RPG

- Status: `Issue created`
- Priority: `High`
- Issue: `#70`
- Owner: `Mixed`
- Goal: Add a guarded MekHQ-owned command surface for MEK-RPG to request pilot assignment, pilot unassignment, pilot swaps, and TO&E/force-organization edits without editing saves or duplicating MekHQ validation rules.
- Why it matters: Live MEK-RPG play for Sharpe's Strikers needs to turn narrative lance planning into hard MekHQ roster and force-organization changes. MekHQ must validate crew eligibility, unit state, force structure, scenario/deployment locks, prompt requirements, and save behavior.
- Expected output: A decomposed workstream covering source audit, API design, read selector export, guarded pilot assignment commands, guarded TO&E commands, optional batch design, and fixtures/smoke coverage.
- Handoff notes: Focused child epic under guarded command epic `#44`. MEK-RPG handoff source: `C:\Users\waltr\Documents\mek-rpg\docs\current\MEGAMEK_TOE_PILOT_ASSIGNMENT_API_HANDOFF.md`. Active epic handoff: `docs/handoffs/active/toe-pilot-assignment-command-api-epic.md`. Feature tracking continues in `docs/current/GUARDED_LIVE_MEKHQ_COMMAND_API_TRACKING.md`.
- Dependencies: Completed command envelope issue `#45`; completed command readiness issue `#46`; existing local API source patterns from issues `#50`, `#51`, `#53`, `#54`, and `#55`; local MekHQ source under `external/src/mekhq`; copied/disposable campaign data for mutating tests.
- Child issues:
  - `#71`: Audit MekHQ pilot assignment and TO&E source owners. Completed on `2026-06-29`; source findings are in `docs/current/MEK_RPG_LIVE_MEKHQ_PILOT_TOE_SOURCE_AUDIT.md`.
  - `#72`: Design guarded pilot assignment and TO&E command API. Completed on `2026-06-29`; design note: `docs/current/MEK_RPG_LIVE_MEKHQ_PILOT_TOE_COMMAND_DESIGN.md`; archived handoff: `docs/handoffs/archive/design-guarded-pilot-toe-command-api.md`.
  - `#73`: Expose pilot assignment and TO&E read selectors. Completed on `2026-06-29`; source commit `53741cd082`; archived handoff: `docs/handoffs/archive/implement-pilot-toe-read-selectors.md`.
  - `#74`: Implement guarded MekHQ pilot assignment commands. Active handoff: `docs/handoffs/active/implement-guarded-pilot-assignment-commands.md`.
  - `#75`: Implement guarded MekHQ TO&E force commands. Active handoff: `docs/handoffs/active/implement-guarded-toe-force-commands.md`.
  - `#76`: Design atomic TO&E and pilot assignment batch command. Active handoff: `docs/handoffs/active/design-toe-pilot-batch-command.md`.
  - `#77`: Add pilot assignment and TO&E command fixtures and smoke checklist. Active handoff: `docs/handoffs/active/add-pilot-toe-command-fixtures-smoke.md`.
- Recommended sequence: Source audit `#71`, API design `#72`, and read selectors `#73` are complete. Implement pilot commands `#74` and TO&E commands `#75` only after the source validator/service extraction path is stable. Treat batch issue `#76` as optional and dependent on the individual command designs. Run coverage/smoke issue `#77` after the mutating slices exist.
- Open questions: Implementation must still decide exact reusable source structure, but issue `#72` sets V1 policy: require `Unit#isAvailable()` for assignment, refuse mothballed units, refuse direct replacement except through a source-safe swap command, use conservative force-name validation, and extract/shared-use validation or implement a focused command service before mutating. Issue `#73` exposed selectors and guard facts but did not implement mutation endpoints.

### Define guarded live MekHQ command envelope and prompt policy

- Status: `Done`
- Priority: `High`
- Issue: `#45`
- Owner: `Codex`
- Goal: Define the common request/response contract for all mutating MekHQ local API commands.
- Why it matters: A shared envelope prevents command endpoints from drifting into inconsistent safety behavior around campaign identity, retries, dry-runs, saves, and prompts.
- Expected output: Completed with `docs/current/MEK_RPG_LIVE_MEKHQ_COMMAND_API_STRATEGY.md` updates that define the shared request and response envelope, process-local idempotency policy, endpoint-specific dry-run rules, explicit opt-in save policy, prompt/dialog defaults, and reusable implementation acceptance criteria for epic `#44`.
- Handoff notes: Completed on `2026-06-22`. Archived handoff: `docs/handoffs/archive/design-live-mekhq-command-envelope.md`.
- Dependencies: Issues `#35`, `#38`, `#43`, and epic `#44`.
- Open questions: Future implementation issues still need source-specific proof for state revision, selector stability, and prompt capture. The issue `#45` policy starts with process-local idempotency and requires explicit `dryRun` in every mutating request.

### Implement live MekHQ command readiness and selector discovery

- Status: `Done`
- Priority: `High`
- Issue: `#46`
- Owner: `Codex`
- Goal: Expose which commands are available, blocked, or unsupported for the currently loaded MekHQ campaign, including safe selectors where available.
- Why it matters: MEK-RPG should show action buttons from MekHQ-provided readiness, not by guessing from display-only state rows.
- Expected output: Completed with MekHQ source endpoint `GET /campaign/commands`, which reports command readiness rows, machine-readable blockers, selector policy, and safe selector candidates for campaign/person/unit/applicant/contract ids. Issue `#54` later added live-session unit-market offer selectors.
- Handoff notes: Completed on `2026-06-22`. Archived handoff: `docs/handoffs/archive/implement-live-mekhq-command-readiness-selectors.md`.
- Dependencies: Issue `#45` command envelope is complete. Future domain command issues must use the readiness output and keep unit-market purchase blocked until selectors are safe.
- Open questions: Future command-specific selectors still need a durability decision: opaque live-session tokens tied to `state_revision`, durable ids that survive save/reload, or a mix by domain. Unit-market purchase V1 uses opaque live-session selectors because offers lack durable save-file ids.

### Implement guarded live MekHQ campaign status-note command

- Status: `Done`
- Priority: `High`
- Issue: `#50`
- Owner: `Codex`
- Goal: Add the first low-risk non-day-advance guarded command by letting MEK-RPG append an auditable campaign status/report note through MekHQ report logic.
- Why it matters: This proves the shared command envelope, idempotency, dry-run, opt-in save policy, and before/after response shape without touching finances, units, personnel status, markets, contracts, medical state, repairs, or scenarios.
- Expected output: Completed with local MekHQ source commit `4429d99ea2`, which adds `POST /campaign/command/status-note` using `Campaign#addReport(DailyReportType.GENERAL, ...)`, dry-run validation, report-category/text guards, prompt/save facts, before/after report counts, process-local idempotency for applied commands, updated readiness output, docs, and fixture updates.
- Handoff notes: Completed on `2026-06-22`. Archived handoff: `docs/handoffs/archive/implement-live-mekhq-status-note-command.md`.
- Dependencies: Issues `#43`, `#45`, and `#46`; MekHQ source around `LocalControlService`, `LocalCommandReadinessExporter`, `Campaign#addReport(...)`, and `DailyReportType`.
- Open questions: Future versions may allow a small report-type allowlist or source-generated HTML, but V1 intentionally allows only `GENERAL` and plain text.

### Design live MekHQ personnel death and status command API

- Status: `Done`
- Priority: `High`
- Issue: `#47`
- Owner: `Codex`
- Goal: Design guarded commands for RPG-side personnel status changes such as death, disappearance, capture, retirement, recovery, or other non-scenario status events.
- Why it matters: Characters can die or change status during RPG play outside a MekHQ tactical scenario. MekHQ still needs to own roster state, unit crew cleanup, reports, payroll/availability, and consistency.
- Expected output: Completed with `docs/current/MEK_RPG_LIVE_MEKHQ_PERSONNEL_STATUS_COMMAND_DESIGN.md` and implementation follow-up issue `#51`.
- Handoff notes: Completed on `2026-06-22`. Archived handoff: `docs/handoffs/archive/design-live-mekhq-personnel-status-command.md`. Source review found that V1 should call `Person#changeStatus(...)`, allow only conservative single-person narrative transitions, and refuse tactical casualties, medical/prosthetic outcomes, prisoner operations, retirement payouts, and resurrection.
- Dependencies: Issue `#45`; MekHQ personnel source around `Person#changeStatus(...)`, `PersonnelStatus`, unit crew links, and scenario casualty handling.
- Open questions: Full prisoner command design, retirement payout/turnover design, and GM resurrection remain deferred outside V1.

### Implement guarded live MekHQ personnel status command

- Status: `Done`
- Priority: `High`
- Issue: `#51`
- Owner: `Codex`
- Goal: Implement `POST /campaign/command/personnel/status` from the issue `#47` design so MEK-RPG can apply conservative RPG-side personnel status events through MekHQ-owned logic.
- Why it matters: This is the next guarded mutation after status-note and gives MEK-RPG a practical way to record narrative disappearance, capture, recovery, departure, betrayal, or non-tactical death without direct save edits.
- Expected output: Completed with local MekHQ source commit `32366b64a0`, which adds `POST /campaign/command/personnel/status`, reports `personnel.status` as available through `GET /campaign/commands`, validates shared envelope and target guards, supports dry-run/apply through `Person#changeStatus(...)`, optionally appends a `GENERAL` audit report, and implements V1 refusal rules for tactical, medical, prisoner, payout, resurrection, stale target, unsupported status, and prompt-required cases.
- Handoff notes: Completed on `2026-06-22`. Archived handoff: `docs/handoffs/archive/implement-live-mekhq-personnel-status-command.md`. Design note: `docs/current/MEK_RPG_LIVE_MEKHQ_PERSONNEL_STATUS_COMMAND_DESIGN.md`.
- Dependencies: Issues `#45`, `#46`, `#47`, and `#50`; MekHQ source branch `codex/mekhq-advance-day-control-api`.
- Open questions: Live smoke testing requires a copied/disposable campaign loaded in source-built MekHQ with the control API enabled. Required source checks passed: `.\gradlew.bat :MekHQ:compileJava` and `.\gradlew.bat :MekHQ:checkstyleMain`.

### Design live MekHQ medical treatment and prosthetic command API

- Status: `Done`
- Priority: `High`
- Issue: `#48`
- Owner: `Codex`
- Goal: Design guarded commands for RPG-side medical treatment, prosthetic application, injury recovery, fatigue/hit recovery, and medical expenses.
- Why it matters: MEK-RPG can resolve character-level care outside MekHQ daily processing, but MekHQ should remain the hard ledger for injuries, recovery, prosthetics, and availability.
- Expected output: Completed with `docs/current/MEK_RPG_LIVE_MEKHQ_MEDICAL_COMMAND_DESIGN.md` and follow-up implementation issue `#53`.
- Handoff notes: Completed on `2026-06-23`. Archived handoff: `docs/handoffs/archive/design-live-mekhq-medical-prosthetic-command.md`. Source review found that broad medical treatment and prosthetic surgery should remain blocked until source-owned non-dialog services exist, while `Person#changeFatigue(...)` is safe enough for a narrow guarded fatigue command.
- Dependencies: Issue `#45`; MekHQ source under `campaign/personnel/medical`.
- Open questions: Future prosthetic implementation needs source service extraction from `AdvancedReplacementLimbDialog`; future structured treatment needs exact injury UUID guards and a safe source-owned treatment path.

### Implement guarded live MekHQ personnel fatigue command

- Status: `Done`
- Priority: `High`
- Issue: `#53`
- Owner: `Codex`
- Goal: Implement `POST /campaign/command/personnel/fatigue` as the first safe medical-adjacent command.
- Why it matters: MEK-RPG can apply RPG-side rest, exhaustion, and fatigue recovery events while MekHQ keeps fatigue scaling, clamping, roster state, and audit reports under source-owned logic.
- Expected output: Completed with local MekHQ source commit `ef6ef99ef9`, which adds `POST /campaign/command/personnel/fatigue`, exposes `personnel.fatigue` readiness and fatigue guard facts, validates the shared envelope and target guards, supports dry-run/apply through `Person#changeFatigue(...)`, and optionally appends a `GENERAL` audit report.
- Handoff notes: Completed on `2026-06-23`. Archived handoff: `docs/handoffs/archive/implement-live-mekhq-personnel-fatigue-command.md`. Design note: `docs/current/MEK_RPG_LIVE_MEKHQ_MEDICAL_COMMAND_DESIGN.md`. `.\gradlew.bat :MekHQ:compileJava` and `.\gradlew.bat :MekHQ:checkstyleMain` passed from `external/src/mekhq`; source push is blocked because `origin` is upstream `MegaMek/mekhq` and GitHub returned 403.
- Dependencies: Issues `#45`, `#46`, `#48`, and existing local API patterns from `#50` and `#51`.
- Open questions: Live smoke testing requires a copied/disposable campaign loaded in source-built MekHQ with the control API enabled.

### Design live MekHQ unit-market purchase command API

- Status: `Done`
- Priority: `High`
- Issue: `#49`
- Owner: `Codex`
- Goal: Design a guarded unit-market purchase command for MEK-RPG actions such as buying a DropShip from the live MekHQ market.
- Why it matters: Unit and transport purchases are high-value RPG decisions, but they must route through MekHQ price, finance, unit-add/delivery, report, and market-offer removal logic.
- Expected output: Completed with `docs/current/MEK_RPG_LIVE_MEKHQ_UNIT_MARKET_PURCHASE_COMMAND_DESIGN.md` and follow-up implementation issue `#54`.
- Handoff notes: Completed on `2026-06-23`. Archived handoff: `docs/handoffs/archive/design-live-mekhq-unit-market-purchase-command.md`. Source review found that `UnitMarketOffer#writeToXML()` has no durable unique id, so V1 should use MekHQ-generated live-session selectors scoped to current readiness state, full guard facts, and duplicate exact-offer refusal. The first implementation slice should support one unique non-black-market offer and defer black-market/multi-offer behavior.
- Dependencies: Issues `#45` and `#46`; MekHQ source around `UnitMarketOffer`, `AbstractUnitMarket`, and `UnitMarketPane#purchaseSelectedOffers()`.
- Open questions: Should source selector tokens use a process-local registry or stateless signed/canonical token? Should high-price apply requests require a matching dry-run first?

### Implement guarded live MekHQ unit-market purchase command

- Status: `Done`
- Priority: `High`
- Issue: `#54`
- Owner: `Codex`
- Goal: Implement source-generated live-session unit-market offer selectors and `POST /campaign/command/markets/unit-offers/purchase` for one guarded non-black-market offer.
- Why it matters: MEK-RPG needs high-value logistics actions such as buying a DropShip, but MekHQ must own the price, finance transaction, unit creation, delivery state, reports, and market removal.
- Expected output: Completed with local MekHQ source commit `78890ba458`, which adds source-generated live-session offer selectors to `GET /campaign/commands` and `POST /campaign/command/markets/unit-offers/purchase` for one unique non-black-market offer. V1 validates selector state revision, canonical offer fingerprint, expected unit/market/price/delivery/balance guards, `dryRun`, `promptPolicy=refuse_if_prompt`, process-local idempotency, optional audit report, and opt-in save; apply mode debits `TransactionType.UNIT_PURCHASE`, calls `Campaign#addNewUnit(...)`, writes reports, and removes the purchased offer.
- Handoff notes: Completed on `2026-06-23`. Archived handoff: `docs/handoffs/archive/implement-live-mekhq-unit-market-purchase-command.md`. Design note: `docs/current/MEK_RPG_LIVE_MEKHQ_UNIT_MARKET_PURCHASE_COMMAND_DESIGN.md`. `.\gradlew.bat :MekHQ:compileJava` and `.\gradlew.bat :MekHQ:checkstyleMain` passed from `external/src/mekhq`; source push is blocked because `origin` is upstream `MegaMek/mekhq` and the authenticated account lacks push permission.
- Dependencies: Issues `#45`, `#46`, and `#49`; MekHQ source branch `codex/mekhq-advance-day-control-api`.
- Open questions: Live smoke testing requires a copied/disposable campaign with representative unit-market offers, ideally including a DropShip offer.

### Design live MekHQ contract selection command API

- Status: `Done`
- Priority: `High`
- Issue: `#52`
- Owner: `Codex`
- Goal: Design a guarded command that lets MEK-RPG ask the loaded MekHQ campaign to accept a specific available contract from the contract market.
- Why it matters: MEK-RPG can already read available contracts, but selecting one is a strategic campaign mutation with finances, transport, reporting, faction, mission, and prompt side effects that must stay inside MekHQ-owned logic.
- Expected output: Completed with `docs/current/MEK_RPG_LIVE_MEKHQ_CONTRACT_ACCEPT_COMMAND_DESIGN.md` and follow-up implementation issue `#55`.
- Handoff notes: Completed on `2026-06-23`. Archived handoff: `docs/handoffs/archive/design-live-mekhq-contract-selection-command.md`. Source review found market contract ids are stable while an offer remains in the market, but `Campaign#addMission(...)` assigns a new active mission id on acceptance. V1 should implement `POST /campaign/command/contracts/accept` by sharing or extracting MekHQ-owned acceptance logic, making known prompt choices explicit in the request, and refusing unknown or unsupported prompt branches before mutation.
- Dependencies: Issues `#45` and `#46`; MekHQ source around `ContractMarketDialog#acceptContract(...)`, `AbstractContractMarket#getContracts()`, `Contract#acceptContract(...)`, mission insertion, advance/transport funds, rentals, faction standing, and local command readiness.
- Open questions: Later versions need explicit source services before supporting contract confirmation choices, faction-standing dialog behavior, travel automation, rentals, decline, negotiation, or market refresh.

### Implement guarded live MekHQ contract accept command

- Status: `Done`
- Priority: `High`
- Issue: `#55`
- Owner: `Codex`
- Goal: Implement `POST /campaign/command/contracts/accept` for one current contract-market offer selected by source id plus guard fields, using MekHQ-owned acceptance logic and explicit known prompt choices.
- Why it matters: Contract choice is the next strategic MEK-RPG-to-MekHQ mutation after unit purchase. MekHQ must own finance credits, mission insertion, StratCon initialization, reports, market removal, and save behavior.
- Expected output: Completed with local MekHQ source commit `0451eb53d4`, which adds `POST /campaign/command/contracts/accept` and source-generated contract-market selector guard facts to `GET /campaign/commands`. V1 validates source contract id, `expectedStateRevision`, campaign/date/location guards, expected contract terms, current balance, market-offer and active-mission counts, explicit known prompt choices, process-local idempotency, optional audit report, and opt-in save. Dry-run validates without mutation; apply mode credits advance and transport funds, calls `Campaign#addMission(...)`, invokes `Contract#acceptContract(...)`, processes the non-dialog faction-standing report path, removes the market offer, and returns the new mission id.
- Handoff notes: Completed on `2026-06-23`. Archived handoff: `docs/handoffs/archive/implement-live-mekhq-contract-accept-command.md`. Design note: `docs/current/MEK_RPG_LIVE_MEKHQ_CONTRACT_ACCEPT_COMMAND_DESIGN.md`. `.\gradlew.bat :MekHQ:compileJava` and `.\gradlew.bat :MekHQ:checkstyleMain` passed from `external/src/mekhq`; source push is blocked because `origin` is upstream `MegaMek/mekhq` and the authenticated account lacks push permission.
- Dependencies: Issues `#45`, `#46`, and `#52`; existing source command API patterns from `#50`, `#51`, `#53`, and `#54`; MekHQ source branch `codex/mekhq-advance-day-control-api`.
- Open questions: Follow-up issue `#79` now owns live smoke testing against a copied/disposable campaign with a selectable contract offer, including prompt-choice behavior, post-command API rereads, negative server-availability checks, and visible MekHQ UI refresh observations.

### Smoke test contract accept prompts and MekHQ UI refresh

- Status: `Issue created`
- Priority: `High`
- Issue: `#79`
- Owner: `Mixed`
- Goal: Prove the implemented `contracts.accept` command in a live source-built MekHQ GUI session against copied/disposable data, including known prompt handling and visible UI refresh behavior.
- Why it matters: Contract acceptance is a high-risk strategic mutation with finances, mission insertion, reports, market removal, and several possible UI prompts. Source tests cover command validation and selector behavior, but the project still needs live evidence that the command works in the GUI process and that a human looking at MekHQ sees current state afterward.
- Expected output: A live-smoke note or tracking update recording the disposable save path, source commit, dry-run/apply responses, prompt facts and choices, post-command `/status`/`/campaign/commands`/`/campaign/state` rereads, visible UI observations, and any follow-up source issue for stale panels or unsupported prompt branches.
- Handoff notes: Active handoff: `docs/handoffs/active/smoke-test-contract-accept-ui-refresh.md`. Use copied/disposable campaign saves only. Keep `saveAfterSuccess=false` unless intentionally testing a disposable save path.
- Dependencies: Completed contract accept implementation issue `#55`; source-built MekHQ launched with `-Dmekhq.controlApi.enabled=true`; copied/disposable campaign with at least one selectable contract-market offer.
- Open questions: Which copied campaign should be used for the first representative contract-market offer? Do current MekHQ panels receive enough model/event notifications from the non-dialog API path, or does the command need an explicit GUI refresh/event hook after mutation?
