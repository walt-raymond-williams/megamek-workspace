# Campaign Analysis Workflow

Use this when the user asks what a campaign save, scenario, roster, or decision means.

## 1. Establish Inputs

Record:

- campaign file path
- MekHQ version
- campaign date and era
- unit/faction/employer
- active contract or scenario
- optional systems that appear enabled

Check `ACTIVE_CAMPAIGN.md` first. Update it when the active campaign file, campaign identity, active contract, scenario, or enabled systems are confirmed.

Do not modify the original campaign file.

## 2. Extract Facts

Start with a factual inventory:

- command identity: unit name, commander, faction, current planet
- finances: cash, debt, monthly burn, outstanding obligations
- personnel: pilots, techs, admins, medics, fatigue, injuries, skills
- forces: lances/stars/forces, unit types, tonnage, BV if available
- units: armor/internal damage, destroyed sections, missing equipment, ammo, heat concerns, quirks
- logistics: parts, transport, dropships, spare units, salvage, prisoners
- contracts: employer, enemy, mission type, payment, salvage rights, transport terms, remaining battles
- scenarios: objectives, map/environment, deployment, allies/enemies, victory conditions

## 3. Interpret Mechanics

Translate raw data into gameplay meaning:

- Can the force deploy enough healthy units?
- Are repair queues and tech capacity becoming a bottleneck?
- Are pilots overworked, wounded, or badly matched to machines?
- Is the contract profitable after expected repair and ammo costs?
- Are salvage rights worth pursuing aggressively?
- Are there obvious Battle Value or tonnage mismatches?
- Is the scenario asking for speed, armor, range, jump capability, infantry, or aerospace support?

## 4. Recommend Actions

Sort recommendations by time horizon:

- immediate next click or next battle choice
- pre-battle prep
- campaign-week management
- long-term unit development

Label confidence:

- `High`: directly supported by campaign data or local docs
- `Medium`: strong BattleTech/MekHQ inference
- `Low`: plausible, needs user confirmation or rules lookup

## 5. Preserve Learning

If the work reveals a reusable file-field meaning, workflow, or rule interaction, update the relevant `docs/current/` file using `DOCUMENTATION_WORKFLOW.md`.
