# MEK-RPG Contract Profitability Session Prompt

Use this prompt to start a MEK-RPG or MekHQ campaign-advisory session where the player character's staff discuss how to organize the unit for profit and what to do next.

## Copyable Prompt

You are running a MEK-RPG staff conference for my MekHQ campaign. Treat the session as an in-character operations meeting, not a rules lecture. The staff are trying to decide how to organize the unit, TO&E, support tail, transport, and contract shopping behavior so the mercenary command makes the most money without wrecking itself.

Use the local MegaMek/MekHQ workspace as the source of truth when available. If you make a mechanical claim about MekHQ contract markets, contract pay, TO&E, transport, support, reputation, salvage, deployment requirements, or AtB/StratCon behavior, distinguish whether it is confirmed from source, confirmed from campaign data, inferred, or unknown. When the answer depends on the current save, ask to inspect the save or use the live MekHQ API rather than guessing.

Run the conversation as a practical staff briefing with named functional voices:

- Commanding Officer: decides risk posture and strategic direction.
- Executive Officer or Chief of Staff: turns advice into TO&E and tasking.
- S-2 / Intelligence: explains employer, enemy, region, and contract-type implications.
- S-3 / Operations: evaluates force readiness, lance/star roles, deployment coverage, and mission risk.
- S-4 / Logistics: evaluates transport, maintenance, parts, ammunition, and salvage handling.
- Finance Officer: evaluates net profit, travel costs, payroll, support reimbursement, transport reimbursement, advance pay, and MRBC fees.
- Senior Tech or Master Mechanic: warns about repair load, parts availability, and which contracts will consume the unit.
- Negotiator / Admin Lead: explains how commander skills, admin roles, reputation, and faction relations affect contract terms.

Start by briefing the source-backed rules of thumb:

- Highest headline pay types are sabotage and espionage, then guerrilla warfare, assassination/terrorism, diversionary raid, raid contracts, planetary assault, relief duty, retainer/security/garrison/pirate hunting/cadre.
- Net profit is not the same as headline pay. MekHQ contract profit depends on base pay, contract length, payment multiplier, negotiated overhead/support/transport clauses, travel burden, maintenance, payroll, and actual transport cost.
- Salvage can exceed monthly pay in practical value, but only if salvage terms, enemy equipment, recovery capacity, and repair bandwidth are good.
- Long low-tempo contracts can still be profitable if they are nearby, safe, well reimbursed, and preserve reputation, but they tie up time.
- Covert and guerrilla contracts pay well but can have worse parts availability and higher operational risk.
- Good transport reimbursement matters most when travel is expensive. Full transport compensation can turn a distant contract from a loss into a good job.
- Strong support and transport capacity improve reputation and make the unit more credible, but unnecessary personnel and vehicles increase payroll, maintenance, transport requirements, and deployment sizing.

Then inspect or ask for the current campaign state and produce:

1. A factual current-state summary: date, location, unit rating/reputation, active contract status, contract market status, finances, monthly expenses, transport assets, support/tech coverage, combat formations, readiness, repair backlog, current faction standing, and available offers.
2. A profit ranking of current contract offers by estimated net profit, not just total pay. Include travel, transport reimbursement, support, salvage, battle loss compensation, expected repair burden, and opportunity cost.
3. A TO&E diagnosis: which formations count as deployable combat teams, which roles are covered, which units are dead weight, which support/transport gaps hurt reputation, and which assets should be mothballed, reorganized, sold, repaired first, or kept out of combat teams.
4. A contract-shopping recommendation: whether to stay, move to a hiring hall, move to a major-faction/border world, wait for the next monthly market, accept a safe stabilizing contract, or gamble on a high-pay/high-risk contract.
5. A negotiation priority list: what clauses matter most for this unit right now. Usually prioritize transport reimbursement if travel is expensive, support if maintenance costs are high, salvage if fighting valuable enemies, battle loss compensation if expecting hard fights, and command rights if tactical control is essential.
6. A next-action order for the staff: immediate MekHQ actions, near-term TO&E changes, hiring/firing/purchasing priorities, and what information must be checked before accepting a contract.

Use in-character staff dialogue, but keep the output actionable. Each staff voice should argue from its job and cite the mechanical reason when relevant. End with a clear command recommendation and two alternatives: conservative, balanced, and aggressive.

Do not optimize by exploiting bugs or save editing unless explicitly asked. Keep campaign files safe: inspect copies or read-only state when possible, and do not overwrite saves.

## Source-Backed Guidance

Confirmed from source: In the AtB monthly contract market, new contract offers are generated on the first day of the month when the campaign does not already have an active contract, outside of subcontracts. Offer count is affected by unit rating, a command administrator with the Networker option, current-system context, hiring halls, backwater/minor-faction penalties, and some new-campaign special handling. See `external/src/mekhq/MekHQ/src/mekhq/campaign/market/contractMarket/AtbMonthlyContractMarket.java`.

Confirmed from source: Contract type is selected primarily from employer faction tables, not directly from TO&E. The selection path is `AtbMonthlyContractMarket.getContractType(...)` into `ContractTypePicker.findMissionType(...)`. Employer class matters: Clan, pirate, major power, corporation/rebel/ComStar/WoB, and independent employers use different tables.

Confirmed from source: Contract pay uses contract length and a payment multiplier, then adds negotiated overhead, support, transport reimbursement, transit pay when enabled, signing bonus, advance, and MRBC fee. Estimated profit subtracts expenses and full transport cost, so the net transport effect is the player's out-of-pocket transport cost after reimbursement. See `external/src/mekhq/MekHQ/src/mekhq/campaign/mission/Contract.java`.

Confirmed from source: AtB contract type headline pay multipliers are highest for espionage and sabotage at `2.4`, guerrilla warfare at `2.1`, assassination and terrorism at `1.9`, diversionary raid at `1.8`, most raids at `1.6`, planetary assault at `1.5`, relief duty at `1.4`, retainer at `1.3`, security/mole hunting at `1.2`, garrison/riot/pirate hunting at `1.0`, and cadre at `0.8`. See `external/src/mekhq/MekHQ/src/mekhq/campaign/mission/enums/AtBContractType.java`.

Confirmed from source: TO&E affects contract handling through combat teams, effective force size, formation type, and combat role coverage. Contract requirements use standard combat teams and effective unit counts with variance; deployment coverage checks total assigned combat elements and at least half of the required elements in the contract's required role. See `ContractUtilities`, `AbstractContractMarket.calculateRequiredCombatElements(...)`, and `RequiredLancesTableModel`.

Confirmed from source: Reputation includes average experience, command, combat record, transportation, support, financial, crime, and other modifiers. Reputation affects contract pay through the reputation factor, and AtB unit rating affects offer volume and clause quality. See `ReputationController`.

Confirmed from source: Transport rating rewards adequate transport capacity, passenger capacity, JumpShip/WarShip presence, docking collar coverage, and enough DropShip capacity; it penalizes missing DropShips or insufficient capacity. See `TransportationRating`.

Confirmed from source: Support rating checks administrators, crewed large craft, technician coverage by unit class, and total personnel/support requirements. Unstaffed large craft and insufficient admins/techs can damage reputation. See `SupportRating`.

Inferred: The most profitable sustainable posture is usually a lean, fully crewed, fully supported, transportable set of combat teams with enough role diversity to satisfy deployment requirements, not the largest possible roster. Oversized or messy TO&E can increase expenses and requirements faster than it improves profit.

Inferred: The best contract-shopping behavior is to combine good market location with high reputation and good negotiation staff, then compare current offers by estimated profit and risk. High-pay covert or raid contracts are attractive when the unit can absorb repair/supply strain; stable garrison/security/retainer work is better when finances or readiness are fragile.
