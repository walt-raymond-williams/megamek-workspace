# BattleTech Context Notes

This is the local working memory for BattleTech concepts that matter when interpreting MekHQ campaign data.

## Core Mental Model

BattleTech campaign decisions usually sit at the intersection of:

- tactical combat effectiveness
- pilot quality and survival
- repair/maintenance capacity
- cashflow
- salvage and replacement access
- contract obligations
- era/faction technology limits

Do not treat a unit as just a stat block. A damaged but rare machine, an elite pilot, or a contract with poor salvage terms can change the correct answer.

## Concepts To Track

- `BV`: rough combat value used for force comparison
- `tonnage`: physical weight class and often battlefield role signal
- `gunnery/piloting`: common pilot skill pair; lower is better in classic BattleTech
- `armor/internal`: armor damage is usually manageable; internal damage risks critical components and structure
- `heat`: sustained firing/movement pressure; high heat can shut units down or degrade performance
- `ammo`: damage potential and explosion risk; ammo-dependent units may become expensive or brittle in long campaigns
- `tech base`: Inner Sphere, Clan, mixed, primitive, advanced, etc.
- `era`: determines what equipment and factions make sense
- `maintenance burden`: how much repair capacity a force consumes between fights
- `salvage`: often the economic engine of mercenary campaigns

## MekHQ Reputation Support Rating

- `Confirmed from source`: MekHQ's CamOps support rating is the sum of three sub-modifiers in `mekhq.campaign.camOpsReputation.SupportRating`: partially crewed large craft, administration, and technicians.
- `Confirmed from source`: the partially crewed large-craft modifier is `-5` if any active large craft is not fully crewed; otherwise it is `0`.
- `Confirmed from source`: the administration modifier is `-5` if available adult administrators, employed non-dependent civilians, and doctors are fewer than the required count; otherwise it is `0`. Required administrators are `ceil(total personnel requirement / 10)` for pirate or mercenary factions and `ceil(total personnel requirement / 20)` otherwise. The total personnel requirement includes technician requirements plus active, non-mothballed unit crew/load estimates.
- `Confirmed from source`: technician requirements use the current transportation requirement counts: BattleMechs plus ProtoMeks, vehicles, aerospace fighters plus small craft, and Battle Armor. Available techs are active Mek, Mechanic, Aero, and Battle Armor technicians. Total available techs below total requirement gives `-5`; `100-150%` gives `0`; `151-175%` gives `+5`; `176-200%` gives `+10`; above `200%` gives `+15`.
- `Confirmed from source`: support rating is not separately consumed by repair or maintenance code in the inspected path. It contributes to total unit reputation in `ReputationController`; total reputation then affects contract market rolls, contract pay/reputation factor, and personnel-market availability filtering.

## Agent Caution

BattleTech has many optional rules and MekHQ has many campaign-system toggles. Before giving firm advice, check whether the relevant optional rule is active or frame the advice with assumptions.

Prefer saying "under the usual Total Warfare-style assumption..." over making a confident but ungrounded claim.
