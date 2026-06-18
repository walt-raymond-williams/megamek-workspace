# AGENTS.md

## Project Posture

This is an AI-assisted MegaMek/MekHQ campaign workspace. Treat it as a living operations desk for understanding BattleTech game state and as a source-guided control room for learning how MegaMek/MekHQ actually works.

The main job is to help the user understand and run a MekHQ campaign:

- inspect campaign, unit, scenario, personnel, logistics, finance, and repair data
- explain what the data means in BattleTech and MekHQ terms
- inspect source code to understand file formats, workflows, buttons, automation hooks, and mechanics
- identify risks, opportunities, and next actions
- preserve discoveries so the next Codex session starts with better context

Be practical, explicit, and honest about uncertainty. If a rule, file format, UI behavior, or game mechanic is inferred rather than confirmed from local data/docs/source, say so.

## Commit Discipline

When an agent makes repository changes, it should leave them in a coherent commit and push that commit to GitHub before ending the task unless the user explicitly says not to commit or not to push.

- Check `git status --short` before staging.
- Stage only files that belong to the completed work.
- Do not include unrelated user changes unless the user explicitly asks.
- Prefer small, reviewable commits with plain commit messages.
- Push completed commits to the tracked GitHub branch before closing the related task or issue.
- After pushing, confirm `git status --short --branch` no longer shows the branch ahead of its upstream.
- If a later issue appears, the commit should be easy to inspect, edit, revert, or replace.
- If verification cannot run, record the blocker in the final response and, when durable, in `docs/current/`.

## Workspace Shape

- Current-state knowledge lives in `docs/current/`.
- Documentation updates must follow `docs/current/DOCUMENTATION_WORKFLOW.md`.
- Roadmap and issue-candidate planning belongs in `docs/current/ROADMAP.md`.
- GitHub issue creation and agent handoff rules belong in `docs/current/GITHUB_ISSUE_WORKFLOW.md`.
- Open issue handoffs belong in `docs/handoffs/active/`.
- Completed issue handoffs belong in `docs/handoffs/archive/` after issue close-out.
- Active work tracking belongs in `docs/current/TASKS.md`.
- Current campaign context belongs in `docs/current/ACTIVE_CAMPAIGN.md` once a campaign is identified.
- Repeatable commands belong in `docs/current/KNOWN_COMMANDS.md`.
- Source modifications must follow `docs/current/SOURCE_CHANGE_WORKFLOW.md`.
- MekHQ campaign save structure belongs in `docs/current/SAVE_FORMAT_NOTES.md`.
- Repeatable report formats live in `docs/templates/`.
- Local campaign inputs belong under `campaigns/`.
- Temporary/generated extracts belong under `analysis/`.
- Source checkouts live inside this workspace at `external\src`.
- Installed playable suite lives inside this workspace at `external\installs\MekHQ-0.51.00`.
- Release archives live inside this workspace at `external\downloads`.
- `external/` is ignored by the workspace repo. Treat it as local payload, not workspace documentation.

Known source checkouts:

- `C:\Users\waltr\Documents\megamek-workspace\external\src\megamek`
- `C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq`
- `C:\Users\waltr\Documents\megamek-workspace\external\src\megameklab`
- `C:\Users\waltr\Documents\megamek-workspace\external\src\mm-data`

When docs disagree, prefer `docs/current/` over older notes.

Roadmap posture:

- Keep `docs/current/ROADMAP.md` as the durable planning source.
- Create GitHub Issues gradually from roadmap entries when work is ready for execution or external discussion.
- Use epic issues for broad outcomes that need discovery and child issues; do not assign an agent to implement an epic directly.
- Use `docs/templates/AGENT_HANDOFF.md` for work that another agent should be able to pick up from the issue.
- Use one handoff file per agent-executed issue under `docs/handoffs/active/`, then move it to `docs/handoffs/archive/` after the issue is done.
- For multi-issue workstreams or feature integration branches, keep a compact feature tracking doc under `docs/current/` that records branch, issue state, next management step, and handoff paths.
- Keep durable architecture, workflow, source, and campaign knowledge in `docs/current/`; do not leave it only in an issue handoff.
- After creating or completing an issue, update the roadmap, commit the tracking change, push it to GitHub, and confirm the remote issue or PR points at the pushed commit.

Branch and merge posture:

- For broad multi-ticket features, create a feature integration branch instead of merging partial work directly into `master`.
- Name feature integration branches with the `codex/` prefix, for example `codex/ai-workflow-demo-dev`.
- Agents may complete issues on the integration branch or on smaller branches that merge into the integration branch.
- Keep `master` stable. A human should review and approve the integrated feature branch before it is merged into `master`.
- Before opening a final PR into `master`, create or complete a PR-readiness review issue when the branch contains multiple completed issues or meaningful workflow/product changes.
- Final PRs into `master` should reference completed issues, summarize verification, identify known blockers or follow-ups, and state the human review request clearly.

## Core Workflow

For campaign or scenario interpretation:

1. Identify the exact input files and their versions.
2. Check `docs/current/TASKS.md` for current priorities.
3. Check `docs/current/ACTIVE_CAMPAIGN.md` for known current campaign context.
4. Inspect structure before interpreting. For compressed campaign files, extract to `analysis/tmp/` or read from a temporary location.
5. Build a small factual summary first: date, faction/unit, active contract, force composition, personnel, finances, repairs, location, pending scenarios, and alerts.
6. Interpret the situation through BattleTech/MekHQ concepts: tonnage, tech base, BV, armor/internal damage, heat, ammo, pilot skills, morale, fatigue, maintenance burden, transport, supply, and contract obligations.
7. Separate facts, inferences, and recommendations.
8. Save durable discoveries in `docs/current/` when they improve future work.

For questions about how MekHQ or MegaMek behaves, inspect source before giving a confident answer. Use `rg` heavily in the source checkouts and cite the relevant class/file in the response when it materially supports the conclusion.

## BattleTech Rules Posture

BattleTech has many rule layers and optional systems. Do not pretend all tables and edge cases are known from memory.

- Prefer local MegaMek/MekHQ docs, source code, and campaign data when explaining how this install behaves.
- Use official or primary sources when browsing is necessary.
- Do not reproduce large copyrighted rulebook passages. Summarize mechanics and cite source context when possible.
- Ask the user which ruleset or optional systems they are using when it changes the answer materially.

## Data Handling

Campaign files can contain the user's active campaign state. Treat them as precious.

- Do not overwrite campaign saves.
- Do not move or delete campaign files unless explicitly asked.
- Work on copies under `analysis/tmp/` when experimenting.
- Keep raw saves untracked unless the user explicitly wants a snapshot committed.
- Prefer structured parsing over ad hoc string matching once the format is understood.

## Interpretation Style

A useful campaign answer should usually include:

- what is happening now
- why it matters mechanically
- what choices the player has
- what risks are hidden or easy to miss
- what the agent is uncertain about

Use concise tactical language. The user wants a campaign aide, not a wall of encyclopedia text.

## Source Code Role

Source code is a first-class reference for this workspace. The user expects agents to inspect it often to understand how to control, interact with, automate, and possibly modify MegaMek/MekHQ.

Read source when it answers questions like:

- how MekHQ stores a campaign value
- how scenario generation computes an outcome
- how repair, maintenance, fatigue, market, or contract logic works
- what a file format field means
- how the UI triggers an action
- where an import/export feature lives
- whether a command-line or automation path exists
- what would need to change to alter behavior

If code changes become the task, keep source changes in the source checkout, not this workspace. This workspace should hold investigation notes, plans, and campaign-facing analysis.

Before modifying source:

1. Check git status in the target source repo.
2. Read `docs/current/SOURCE_CHANGE_WORKFLOW.md`.
3. Read nearby code and existing tests.
4. Keep changes narrowly scoped.
5. Verify with the relevant command in `docs/current/KNOWN_COMMANDS.md`, or record the exact blocker.
6. Record the reason and verification in this workspace when it teaches us something durable about controlling the campaign.

## Verification

Useful local checks:

```powershell
java -version
javac -version
Test-Path 'C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\MekHQ.exe'
Test-Path 'C:\Users\waltr\Documents\megamek-workspace\external\src\megamek'
Test-Path 'C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq'
Test-Path 'C:\Users\waltr\Documents\megamek-workspace\external\src\megameklab'
Test-Path 'C:\Users\waltr\Documents\megamek-workspace\external\src\mm-data'
```

For parsing work, verify by comparing extracted summaries against MekHQ's UI or known campaign facts.

## Learning Loop

When a campaign file field, scenario field, or MekHQ mechanic becomes clear, add a compact note to `docs/current/` using the evidence labels in `docs/current/DOCUMENTATION_WORKFLOW.md`. Future agents should inherit hard-won understanding instead of rediscovering it.

When work starts, finishes, becomes blocked, or changes priority, update `docs/current/TASKS.md` so the next session can resume from the current board.
