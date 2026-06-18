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

- Status: `Issue created`
- Priority: `High`
- Issue: `#1`
- Owner: `Mixed`
- Goal: Separate reusable AI-ready workflow guidance from MegaMek/MekHQ-specific paths, source maps, save files, and rules posture.
- Why it matters: The repo should serve as both a real MegaMek worked example and a reusable pattern for other projects.
- Expected output: Generic workflow docs plus a MegaMek project profile that owns local assumptions and domain-specific guidance.
- Handoff notes: Preserve the current MegaMek campaign/source investigation strength while making the generic pattern obvious to outsiders. Active handoff: `docs/handoffs/active/split-generic-ai-ready-workflow.md`.
- Dependencies: None.
- Open questions: Should project profiles live under `projects/megamek/` or stay under `docs/current/` until the structure stabilizes?

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
- Expected output: Durable workflow note or skill-style guide, linked from `WORKSPACE.md` or the future MegaMek project profile.
- Handoff notes: Resume from `docs/current/HELP_FILE_USAGE_GUIDANCE_STATE.md`. Active handoff: `docs/handoffs/active/create-help-file-usage-guidance.md`.
- Dependencies: PDF extraction tooling would improve coverage but is not required for text, Markdown, HTML, Java, and properties files.
- Open questions: Should this become a reusable project-profile pattern?
