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

- Status: `Issue created`
- Priority: `Medium`
- Issue: `#2`
- Owner: `Codex`
- Goal: Extract a factual campaign snapshot from `campaigns/demo/ai-ready-demo.cpnx.gz`.
- Why it matters: The demo save is the concrete example for save-file investigation, reporting, and requirements discovery.
- Expected output: Updated `ACTIVE_CAMPAIGN.md`, `SAVE_FORMAT_NOTES.md`, and a first demo campaign status report.
- Handoff notes: Follow `CAMPAIGN_ANALYSIS_WORKFLOW.md`. Do not overwrite the save. Active handoff: `docs/handoffs/active/inspect-demo-campaign-save.md`.
- Dependencies: None.
- Open questions: Should generated extracts live under `analysis/generated/demo/`?

### Identify MekHQ save/load source classes

- Status: `Issue created`
- Priority: `Medium`
- Issue: `#3`
- Owner: `Codex`
- Goal: Confirm how `.cpnx.gz` files are loaded and saved in local MekHQ source.
- Why it matters: Save-file interpretation should be grounded in implementation behavior.
- Expected output: Updates to `SAVE_FORMAT_NOTES.md`, `SOURCE_CODE_GUIDE.md`, and relevant roadmap entries.
- Handoff notes: Use `rg "cpnx"` and related source searches from `KNOWN_COMMANDS.md`. Active handoff: `docs/handoffs/active/identify-mekhq-save-load-source-classes.md`.
- Dependencies: Local MekHQ source checkout path must be valid or discovered.
- Open questions: Should source checkouts remain under `external/src` or become configurable via a project profile?

### Create help-file usage guidance

- Status: `Issue created`
- Priority: `Medium`
- Issue: `#4`
- Owner: `Codex`
- Goal: Decide how agents should use local MegaMek/MekHQ help files, glossary resources, installed docs, and source code.
- Why it matters: Help files are a safer user-facing reference for concepts, while source is needed for exact behavior.
- Expected output: Durable workflow note or skill-style guide, linked from `WORKSPACE.md` or the current MegaMek project profile.
- Handoff notes: Resume from `docs/current/HELP_FILE_USAGE_GUIDANCE_STATE.md`. Active handoff: `docs/handoffs/active/create-help-file-usage-guidance.md`.
- Dependencies: PDF extraction tooling would improve coverage but is not required for text, Markdown, HTML, Java, and properties files.
- Open questions: Should this become a reusable project-profile pattern?

### Run MekHQ campaign exploration live-assist shakedown

- Status: `Issue created`
- Priority: `High`
- Issue: `#16`
- Owner: `Mixed`
- Goal: Run the hands-on MekHQ exploration plan as a live user/Codex session while the user manually operates MekHQ and Codex acts as a campaign aide, source/docs reference, and documentation recorder.
- Why it matters: The workspace needs to move from documentation-only planning into an actual MekHQ shakedown that tries campaign creation, contracts, travel, owned transport, aerospace, scenario resolution, repair, salvage, finances, and close-out.
- Expected output: Session notes and durable documentation updates from trying `CAMPAIGN_EXPLORATION_PLAN.md`, including what worked, what failed, and what needs source follow-up.
- Handoff notes: Active handoff: `docs/handoffs/active/run-mekhq-campaign-exploration.md`. The intended workflow is for the user to start MekHQ and Codex together, then operate MekHQ manually while Codex follows the handoff.
- Dependencies: User must manually operate MekHQ or share enough UI/save context for Codex to advise. Use a disposable exploration save. Do not rely on bot-controlled space-map aerospace combat for the first test; the user intends to resolve aerospace/space battles with two human players.
- Open questions: Which exploration save path should become active? Can a custom transit aerospace battle be attached cleanly to an active contract, or should it be tracked as a manual/GM narrative scenario? Which Leopard variant and two aerospace fighters should be used for the first test?

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
  - `#9`: Define tabletop battle result input schema for MekHQ MUL generation.
  - `#10`: Prototype battle-record MUL round-trip validation against MekHQ.
  - `#11`: Choose MUL generation strategy for tabletop result workflow.
  - `#12`: Implement robust tabletop battle-record MUL generator.
  - `#13`: Verify and document tabletop result entry workflow for MekHQ.
- Recommended sequence: Source workflow confirmation `#8` and salvage research `#7` are complete. Next define the result/input schema in `#9`, then validate a minimal built-in workflow round trip in `#10`, choose strategy in `#11`, decide whether implementation `#12` is still needed, and finish with UI/manual documentation `#13`.
- Branch/tracking recommendation: Do not create an integration branch during discovery. Re-evaluate in `#11`; only create `codex/tabletop-result-mul-dev` and a compact feature tracking doc if `#11` confirms custom implementation is needed.
- Implementation posture: `#9` and `#10` are learning/discovery work that remain useful even if the final answer is "use MekHQ's built-in Resolve Manually/import workflow and document it." Do not treat `#12` as mandatory until `#11` compares the built-in workflow against custom generation or tooling.
- Open questions: Can MekHQ's built-in manual result-entry/import workflow satisfy the user's tabletop needs without custom code? If not, should the helper be a standalone Java tool using installed MegaMek/MekHQ jars, a MekHQ source feature, or a workspace script that invokes MegaMek classes? What minimum tabletop result schema is needed for armor/internal damage, crits, ammo, ejections, retreats, kills, salvage, and battlefield control? Which validations can run automatically before MekHQ import?

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
- Handoff notes: This is an epic, not a direct implementation task. Active epic handoff: `docs/handoffs/active/mech-roster-control-epic.md`. Feature tracking snapshot: `docs/current/MECH_ROSTER_CONTROL_TRACKING.md`. Discovery on `2026-06-18` found a no-source-change first workflow: copy/save the quickstart, use GM unit add/remove for player roster changes, use scenario edit/regenerate or fixed OPFOR MULs for physical-miniature OPFOR control, and defer custom RATs until the physical miniature list exists.
- Dependencies: Local MekHQ install and source are available under `external/`. Source build/test commands remain blocked by the Java 17 Gradle daemon/toolchain issue. The user's physical miniature list is needed before final OPFOR restrictions or custom RATs can be built.
- Recommended sequence: Next verify the quickstart roster replacement workflow in a disposable save, then define the physical miniature roster data model, prototype fixed OPFOR MUL pools, and only then decide whether custom RATs or workspace tooling are worth the extra complexity.
  - Child issues:
    - `#17`: Verify quickstart roster replacement workflow. Blocked on live MekHQ UI pass after source/safe-copy verification.
    - `#18`: Define physical-miniature roster data model. Completed on `2026-06-18`; findings are in `docs/current/PHYSICAL_MINIATURE_ROSTER_MODEL.md`.
    - `#19`: Prototype fixed OPFOR MUL pools.
    - `#20`: Decide custom RAT strategy for physical-miniature OPFOR.
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
