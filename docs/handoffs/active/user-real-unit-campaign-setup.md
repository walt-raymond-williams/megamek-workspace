# User Checklist: Set Up Real-Life Unit Campaign In MekHQ

## Issue

- GitHub issue: `#23`
- Roadmap entry: `Epic: Control MekHQ player and OPFOR mech rosters`
- Priority: `High`

## Goal

Create a disposable-but-realistic MekHQ campaign/save whose player unit reflects the user's actual tabletop force: specific physical mechs, pilots/techs/support personnel, equipment, and DropShip/transport assumptions. This campaign becomes the better test target for later tabletop battle-result and MUL import validation.

## Required Context

Useful docs before or during the pass:

- `docs/current/PHYSICAL_MINIATURE_ROSTER_MODEL.md`
- `docs/current/MECH_ROSTER_CONTROL_WORKFLOWS.md`
- `docs/current/QUICKSTART_ROSTER_REPLACEMENT_VERIFICATION.md`
- `docs/current/GENERATED_BATTLE_RECORD_MUL_STUDY.md`
- `docs/current/TABLETOP_RESULT_MUL_WORKFLOW.md`

## User Setup Steps

1. Start MekHQ `0.51.00`.
2. Create or copy a campaign save that is safe to experiment with.
3. Give it a recognizable name, such as `Real Unit Setup Test` or another clear working name.
4. Add the player-owned mechs that match the physical collection you want to use first.
5. Add or adjust pilots, techs, doctors, admin/logistics staff, and other support personnel enough that MekHQ can run normal repair/logistics loops.
6. Add the intended DropShip or transport asset, if this real unit should own one.
7. Add notable spare equipment, parts, ammo, or supplies only if they matter for the first validation pass.
8. Assign pilots to units and organize the TO&E enough that a scenario can deploy the intended force.
9. Save the campaign and record the exact save path.

## Report Back

Please report:

- Save path and campaign name.
- MekHQ version if visible.
- Campaign date and faction/unit identity.
- Each player mech added: chassis, variant, pilot, and whether it corresponds to a confirmed physical miniature.
- DropShip/transport added, including variant if known.
- Support staff added or changed: techs, doctors, admin/logistics, astechs, or other key roles.
- Any equipment/ammo/parts manually added.
- Exact UI paths or prompts that were surprising or important.
- Any errors, warnings, or awkward setup steps.
- Whether this save is safe for Codex to copy and inspect under `analysis/tmp/`.

## Constraints

- Do not overwrite any campaign save you care about.
- Treat this as a setup/validation campaign until we deliberately promote it to an active campaign.
- Exact physical inventory does not need to be perfect on the first pass; mark any proxies or uncertain variants clearly.
- If MekHQ asks to resolve or advance campaign consequences during setup, pause and report the prompt unless it is obviously harmless.

## Acceptance Criteria

- A new or copied MekHQ campaign save exists and is safe for inspection.
- The player roster includes the intended real-life mechs for the first tabletop workflow test.
- Pilots and basic support staff are present enough to make the unit usable.
- Intended DropShip/transport assumptions are represented or explicitly noted as not yet represented.
- The user reports enough detail for Codex to update durable docs and inspect the save copy.

## Follow-Up

After this user task, Codex should:

- Copy and inspect the reported save, never modifying the original.
- Update `ACTIVE_CAMPAIGN.md` or a dedicated real-unit setup note if the save should become the working campaign.
- Revisit issue `#10` using this campaign as the more realistic target for manual result import validation.
