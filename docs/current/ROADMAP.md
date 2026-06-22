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

- Status: `Issue created`
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

- Status: `Done`
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

- Status: `In progress`
- Priority: `Medium`
- Issue: `#35`
- Owner: `Codex`
- Goal: Implement or precisely prototype-plan a narrow local-only MekHQ source control API that can be called while MekHQ is already open with a campaign loaded, invoking exactly one real `Campaign#newDay()` path inside the loaded GUI app.
- Why it matters: Issue `#34` proved the concept is viable but brittle. The next useful step is to turn that conclusion into a callable local control seam so Codex or another local helper can drive MekHQ through source-owned logic instead of screen coordinates or save editing, while preserving MekHQ as the hard campaign ledger.
- Expected output: Source-backed prototype plus `docs/current/MEKHQ_ADVANCE_DAY_CONTROL_API_PROTOTYPE.md`, documenting the API transport, input/output contract, source files touched, safety checks, verification status, and live user-assisted test instructions. Initial implementation plan: `docs/current/MEKHQ_ADVANCE_DAY_CONTROL_API_IMPLEMENTATION_PLAN.md`.
- Handoff notes: Active handoff: `docs/handoffs/active/implement-mekhq-advance-day-control-api.md`. Local source prototype committed in `external/src/mekhq` on branch `codex/mekhq-advance-day-control-api` at `9046a8075e`; it adds `mekhq.service.LocalControlService` and wires it into `MekHQ`, starts only with `-Dmekhq.controlApi.enabled=true`, binds to `127.0.0.1`, and exposes `/status` plus `POST /advance-day`.
- Dependencies: MekHQ must already be open with a campaign loaded for live testing. Live prototype testing should wait for the user to be present. Source build/test verification may still be blocked by the Java 17 Gradle daemon/toolchain issue. Use only copied/disposable saves for execution tests.
- Open questions: Can visible modal/prompt detection be made reliable enough without a general prompt policy layer? Should this prototype stay local-only after live validation, or become the first member of a broader local control API?
