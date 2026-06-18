# Campaign Exploration Plan

This document tracks the first hands-on MekHQ exploration campaign. The goal is to try the major campaign loops with a disposable save before treating any campaign as canonical.

## Purpose

- Create a new MekHQ campaign.
- Build a small mercenary unit that can take beginner contracts.
- Accept and play through at least one contract.
- Observe contract travel, planetary arrival, deployment, scenario generation, repair, salvage, finance, and end-of-contract handling.
- Add owned transport and aerospace assets so travel and aerospace combat can be tested intentionally.
- Preserve notes that will help future Codex sessions interpret the campaign and advise on next actions.

## Tracking

- GitHub issue: `#16`
- Active handoff: `docs/handoffs/active/run-mekhq-campaign-exploration.md`
- Intended execution: the user starts MekHQ and Codex together, operates MekHQ manually, and Codex follows the handoff as a live campaign aide and documentation recorder.

## Evidence And Assumptions

- `Confirmed from local docs`: The installed `0_MHQ New Player Guide.pdf` recommends using StratCon as the digital GM for a first single-player campaign, with low starting difficulty such as the New Player preset / Ultra-Green posture.
- `Confirmed from local docs`: The guide's first-contract flow is contract market, contract selection, travel prompts, Briefing Room / Area of Operations deployment, MegaMek launch or manual/autoresolve resolution, then scenario and contract close-out.
- `Confirmed from local docs`: The guide says MekHQ abstracts hiring JumpShips and DropShips for contract travel unless the player owns transport; owned transport can reduce transport costs but adds maintenance and operating burden.
- `Confirmed from local docs`: `external/installs/MekHQ-0.51.00/docs/Aerospace Stuff/How_to_use_aerospace_units_on_ground_maps_in_MegaMek.md` says MegaMek supports aerospace units on ground maps, air-to-ground attacks, fly-off/go-around behavior, hot drops from DropShips, and space maps through the lobby's `Use Space Map` option.
- `Confirmed from local docs`: The same aerospace note says the bot can use aerospace fighters on ground maps but cannot use them on space maps yet.
- `Confirmed from source`: MekHQ has interstellar map support for calculating jump paths and GM movement / jumpdrive recharge controls in `external/src/mekhq/MekHQ/src/mekhq/gui/InterstellarMapPanel.java`.
- `Confirmed from source`: MekHQ checks JumpShip jump capability before proceeding with jumps in `external/src/mekhq/MekHQ/src/mekhq/campaign/utilities/JumpBlockers.java`.
- `Confirmed by user`: The initial exploration should avoid relying on the AI for aerospace/space combat. The user is willing to resolve battles between two human players.
- `Confirmed by user`: The desired transport experiment is a JumpShip arriving in-system, a Leopard-class DropShip heading toward the planet, and a space/aerospace encounter during that transit.
- `Confirmed by user`: The Leopard should carry at least two aerospace fighters because the user wants to test a Leopard with its two fighter bays filled.
- `Unknown`: Whether MekHQ has a clean built-in workflow for inserting a custom transit aerospace battle into a contract's travel phase without GM/manual scenario handling. Treat the first version as a manual/GM scenario unless source inspection confirms a better workflow.

## Campaign Shape

Use a disposable "exploration" campaign, not the active demo save.

Recommended starting posture:

- New campaign using the New Player preset or equivalent low difficulty.
- StratCon enabled.
- GM mode allowed for setup corrections and custom scenario insertion.
- Start with simple logistics; enable advanced complexity later only when the basic loop is understood.
- One playable ground lance as the core combat unit.
- A Leopard-class DropShip as owned transport once the basic contract loop is understood.
- Two aerospace fighters assigned to the Leopard.
- Aerospace pilots assigned before any space/aerospace scenario.
- Avoid bot-controlled space battles for the first test; use human-vs-human play or manual result entry.

## Exploration Phases

### Phase 1: Campaign Creation

1. Create a fresh campaign.
2. Record campaign name, date, faction, commander, starting planet, and enabled systems.
3. Confirm where the save file is stored.
4. Save immediately as a baseline.

Success criteria:

- A new save exists and can be loaded.
- `ACTIVE_CAMPAIGN.md` can be updated if this becomes the active practice campaign.

### Phase 2: Company Setup

1. Build or accept a small starter force.
2. Ensure at least one complete ground combat lance.
3. Ensure enough tech, medical, admin, and logistics staff to keep the unit functional.
4. Add or later acquire a Leopard-class DropShip.
5. Add two aerospace fighters and assign aerospace pilots.
6. Assign transport relationships in TO&E once the Leopard exists.

Success criteria:

- The ground lance can deploy.
- The Leopard has two aerospace fighters aboard or assigned.
- Personnel roles do not leave obvious gaps for the test.

### Phase 3: First Contract

1. Advance to a contract market refresh if needed.
2. Choose a low-skull contract with reasonable transit time, transport terms, and required combat force count.
3. Prefer simple contract types first: pirate hunting, security, garrison, or raid.
4. Accept travel prompts and let MekHQ calculate the route.
5. Record transport cost, transit time, arrival system, destination planet, and contract start date.

Success criteria:

- The campaign enters contract travel cleanly.
- The Briefing Room shows the accepted contract.
- The travel route and destination are visible.

### Phase 4: Transit Aerospace Scenario

Scenario concept:

- The JumpShip arrives at the destination system's jump point.
- The Leopard detaches and burns toward the contract planet.
- During the interplanetary transit, hostile aerospace assets intercept the Leopard or its escort.
- The two player aerospace fighters scramble from the Leopard.
- Both sides are controlled by human players.
- The battle outcome is entered back into MekHQ manually or through a GM/custom scenario workflow.

Initial scenario options:

1. Space-map interception:
   - Use MegaMek's `Use Space Map` option.
   - Keep forces small: two player aerospace fighters versus two hostile fighters, or two fighters plus a small craft if balance permits.
   - Do not use bot control for the hostile aerospace force in the first test.

2. Ground-map air approach abstraction:
   - If space-map setup is awkward, model the engagement as aerospace on a ground map or high-altitude approach.
   - Use air-to-ground/air-to-air rules only as far as MegaMek supports cleanly.

3. Manual narrative result:
   - If campaign integration is the blocker, play the battle directly in MegaMek and record results manually in MekHQ.
   - Track fighter damage, pilot injuries, ammunition/bombs expended, and any Leopard damage as manual updates.

Success criteria:

- A human-vs-human aerospace battle is played or test-launched.
- Damage and losses can be reflected back in MekHQ.
- Any gaps in MekHQ's custom scenario workflow are recorded for source follow-up.

### Phase 5: Planetfall And StratCon Deployment

1. Arrive at the contract planet.
2. Open the Briefing Room and Area of Operations.
3. Assign the ground lance to a simple scenario or contact.
4. Try one normal deployment.
5. Try one reinforcement or transport-assisted deployment if practical.
6. Launch MegaMek for one battle or resolve manually if the objective is just campaign flow.

Success criteria:

- A StratCon scenario is generated or assigned.
- Player forces deploy correctly.
- Scenario resolution returns consequences to MekHQ.

### Phase 6: Post-Battle Operations

1. Resolve salvage.
2. Repair damaged units.
3. Treat injuries.
4. Refit/rearm aerospace fighters if they were used.
5. Check finances, payroll, contract support, and battle loss compensation.
6. Advance time until at least one maintenance/repair cycle is visible.

Success criteria:

- The campaign state reflects battle consequences.
- Repair and finance burdens are visible.
- Aerospace losses or repairs are understandable.

### Phase 7: Contract Close-Out

1. Continue or fast-forward until contract conclusion is possible.
2. Resolve success/failure/partial success.
3. Record final finances, salvage, personnel changes, reputation, and force readiness.
4. Decide whether to continue the save or start over with adjusted settings.

Success criteria:

- One contract loop is complete.
- The user has enough experience to decide preferred campaign settings.

## Tracking Log Template

Use this for each session:

```markdown
## YYYY-MM-DD Session

- Save:
- MekHQ version:
- Campaign date:
- Current location:
- Contract:
- What changed:
- Battles/scenarios:
- Aerospace/transport notes:
- Finance/logistics notes:
- Repairs/injuries:
- Open questions:
- Next action:
```

## Open Questions

- What exact MekHQ UI path is best for creating a custom space/aerospace scenario during contract travel?
- Can a custom battle during transit be attached to the active contract cleanly, or should it be tracked as a manual narrative event?
- What is the most reliable way to apply post-battle aerospace fighter damage, pilot injury, ammunition expenditure, and Leopard damage back into MekHQ?
- Which Leopard variant and fighter pair should be used for the first balanced human-vs-human aerospace test?
- Should this exploration save replace `campaigns/demo/ai-ready-demo.cpnx.gz` as the active campaign, or stay separate until the loop is proven?
