# MEK-RPG MekHQ Checkpoint Export Schema

Status: draft MegaMek-side schema aligned with MEK-RPG issue `#67`, created `2026-06-21`.

Purpose: give a future MekHQ-owned read-only checkpoint exporter a concrete JSON target that MEK-RPG can consume without direct save/XML edits or write automation.

## Alignment

- `Confirmed from MEK-RPG docs`: MEK-RPG issue `#67` defines the desired consumer contract in `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_READ_ONLY_CHECKPOINT_EXPORT_CONTRACT.md`.
- `Confirmed from MEK-RPG docs`: MEK-RPG wants the top-level shape to stay close to its current Python save-summary helper: `bridge_metadata`, `campaign`, `finances`, `personnel`, `units`, `contracts`, `scenarios`, `repairs_and_logistics`, `markets`, `reports`, and `unsupported`.
- `Confirmed from MEK-RPG docs`: every complex object should preserve stable MekHQ ids when available and expose provenance through evidence/source fields, object warnings, and `method_backed`.
- `Confirmed from source`: source-backed field owners and derived-value warnings are mapped in `MEK_RPG_MEKHQ_CHECKPOINT_EXPORT.md`.
- `Out of scope`: this schema does not define write commands, pending-action application, save mutation, headless day advancement, or automation selectors as trusted executable commands.

## Evidence Values

Use these values exactly where possible:

- `Confirmed from MekHQ export`: value came from a MekHQ-owned exporter using MekHQ code.
- `Confirmed from MekHQ import`: value came from the current MEK-RPG read-only XML helper.
- `Method-backed`: value was produced by a MekHQ method rather than raw XML field inspection.
- `Serialized fact`: value is stored directly in a saved MekHQ campaign.
- `Inferred`: value was calculated or interpreted outside MekHQ business logic.
- `Unknown`: expected field is absent or not exposed.
- `Unsupported`: field is outside the current exporter/helper.
- `Needs MekHQ inspection`: field requires source or sample-save review before trust.

For method-backed exporter output, prefer:

```json
{
  "value": "example",
  "evidence": "Confirmed from MekHQ export",
  "method_backed": true,
  "source_owner": "Campaign#getLocalDate()",
  "warnings": []
}
```

For serialized-only values, prefer:

```json
{
  "value": "ACTIVE",
  "evidence": "Serialized fact",
  "method_backed": false,
  "source_owner": "Mission#writeToXML(...)",
  "warnings": []
}
```

## Draft Shape

```json
{
  "bridge_metadata": {
    "schema_name": "mekhq-read-only-checkpoint",
    "schema_version": "0.1",
    "producer": "mekhq-exporter",
    "producer_version": "Unknown",
    "mekhq_version": "0.51.00",
    "exported_at": "YYYY-MM-DDTHH:MM:SSZ",
    "input_path": "C:/path/to/campaign.cpnx.gz",
    "save_timestamp": "YYYY-MM-DDTHH:MM:SSZ",
    "save_size_bytes": 0,
    "gzip": true,
    "read_only": true,
    "checkpoint_id": "Unknown",
    "evidence": "Confirmed from MekHQ export",
    "warnings": []
  },
  "campaign": {
    "id": { "value": "Unknown", "evidence": "Serialized fact", "method_backed": false, "source_owner": "Campaign#writeToXML(...)", "warnings": [] },
    "name": { "value": "Unknown", "evidence": "Confirmed from MekHQ export", "method_backed": true, "source_owner": "Campaign#getName()", "warnings": [] },
    "date": { "value": "YYYY-MM-DD", "evidence": "Confirmed from MekHQ export", "method_backed": true, "source_owner": "Campaign#getLocalDate()", "warnings": [] },
    "start_date": { "value": "Unknown", "evidence": "Serialized fact", "method_backed": false, "source_owner": "Campaign#writeToXML(...)", "warnings": [] },
    "faction": { "value": "Unknown", "evidence": "Confirmed from MekHQ export", "method_backed": true, "source_owner": "Campaign#getFaction()", "warnings": [] },
    "gm_mode": { "value": "Unknown", "evidence": "Serialized fact", "method_backed": false, "source_owner": "Campaign#writeToXML(...)", "warnings": [] },
    "location": {
      "current_system_id": { "value": "Unknown", "evidence": "Confirmed from MekHQ export", "method_backed": true, "source_owner": "Campaign#getCurrentSystem()", "warnings": [] },
      "current_system_name": { "value": "Unknown", "evidence": "Confirmed from MekHQ export", "method_backed": true, "source_owner": "Campaign#getCurrentSystem()", "warnings": [] },
      "current_location": { "value": "Unknown", "evidence": "Confirmed from MekHQ export", "method_backed": true, "source_owner": "Campaign#getCurrentLocation()", "warnings": [] },
      "travel_state": { "value": "Unknown", "evidence": "Needs MekHQ inspection", "method_backed": "Unknown", "source_owner": "Location/LocationNode", "warnings": ["Route/base semantics need disposable-save validation."] }
    }
  },
  "finances": {
    "balance": { "value": "Unknown", "currency": "C-bills", "evidence": "Confirmed from MekHQ export", "method_backed": true, "source_owner": "Finances#getBalance()", "warnings": [] },
    "loan_balance": { "value": "Unknown", "currency": "C-bills", "evidence": "Confirmed from MekHQ export", "method_backed": true, "source_owner": "Finances#getLoanBalance()", "warnings": [] },
    "has_active_loans": { "value": "Unknown", "evidence": "Confirmed from MekHQ export", "method_backed": true, "source_owner": "Finances#hasActiveLoans()", "warnings": [] },
    "recent_transactions": []
  },
  "personnel": [],
  "units": [],
  "contracts": [],
  "scenarios": [],
  "repairs_and_logistics": {
    "shopping_list": [],
    "repair_work": [],
    "parts_pressure": [],
    "cargo": {},
    "transport_bays": {},
    "warnings": []
  },
  "markets": {
    "unit_offers": [],
    "personnel_applicants": [],
    "contract_offers": [],
    "warnings": []
  },
  "reports": {
    "current": [],
    "skill": [],
    "technical": [],
    "finances": [],
    "acquisitions": [],
    "medical": [],
    "personnel": [],
    "battle": [],
    "politics": [],
    "aggregate": [],
    "warnings": []
  },
  "unsupported": []
}
```

## Object Shapes

### Personnel

```json
{
  "id": "full-mekhq-person-uuid",
  "display_name": "Example Person",
  "full_title": "Example Person",
  "rank": { "raw_code": "Unknown", "label": "Unknown", "evidence": "Confirmed from MekHQ export", "method_backed": true, "source_owner": "Person#getRankName()" },
  "primary_role": { "raw_code": "Unknown", "label": "Unknown", "evidence": "Confirmed from MekHQ export", "method_backed": true, "source_owner": "Person#getPrimaryRoleDesc()" },
  "secondary_role": { "raw_code": "Unknown", "label": "Unknown", "evidence": "Confirmed from MekHQ export", "method_backed": true, "source_owner": "Person#getSecondaryRoleDesc()" },
  "status": { "raw_code": "Unknown", "label": "Unknown", "evidence": "Confirmed from MekHQ export", "method_backed": true, "source_owner": "Person#getStatus()" },
  "unit_id": "Unknown",
  "fatigue": { "value": "Unknown", "evidence": "Confirmed from MekHQ export", "method_backed": true, "source_owner": "Person#getFatigueDirect()" },
  "hits": { "value": "Unknown", "evidence": "Confirmed from MekHQ export", "method_backed": true, "source_owner": "Person#getHits()" },
  "salary": { "value": "Unknown", "currency": "C-bills", "evidence": "Confirmed from MekHQ export", "method_backed": true, "source_owner": "Person#getSalary(Campaign)" },
  "injuries": [],
  "skills": [],
  "warnings": []
}
```

### Units

```json
{
  "id": "full-mekhq-unit-uuid",
  "display_name": "Example Unit",
  "entity": {
    "chassis": "Unknown",
    "model": "Unknown",
    "type": "Unknown",
    "weight_tons": "Unknown",
    "source_owner": "Unit#getEntity()"
  },
  "status": { "raw_code": "Unknown", "label": "Unknown", "evidence": "Confirmed from MekHQ export", "method_backed": true, "source_owner": "Unit#getStatus()" },
  "scenario_id": "Unknown",
  "crew": [],
  "tech_id": "Unknown",
  "engineer_id": "Unknown",
  "damage_state": { "value": "Unknown", "evidence": "Confirmed from MekHQ export", "method_backed": true, "source_owner": "Unit#getDamageState()" },
  "repair_summary": {
    "parts_needed_count": "Unknown",
    "parts_needing_service_count": "Unknown",
    "under_repair": "Unknown",
    "last_maintenance_report": "Unknown",
    "source_owner": "Unit#getPartsNeeded(); Unit#getPartsNeedingService(...); Unit#isUnderRepair(); Unit#getLastMaintenanceReport()"
  },
  "transport": {
    "ship_transport": [],
    "tactical_transport": [],
    "tow_transport": [],
    "warnings": ["Validate exact transport assignment semantics against disposable saves."]
  },
  "warnings": []
}
```

### Contracts

```json
{
  "id": 0,
  "display_name": "Example Contract",
  "type": "Unknown",
  "status": { "raw_code": "Unknown", "label": "Unknown", "evidence": "Confirmed from MekHQ export", "method_backed": true, "source_owner": "Mission#getStatus()" },
  "system_id": "Unknown",
  "system_name": "Unknown",
  "employer": "Unknown",
  "enemy": "Unknown",
  "start_date": "Unknown",
  "end_date": "Unknown",
  "terms": {
    "advance": "Unknown",
    "transport_comp": "Unknown",
    "command_rights": "Unknown",
    "salvage_pct": "Unknown",
    "support": "Unknown",
    "source_owner": "Contract getters"
  },
  "scenario_ids": [],
  "warnings": []
}
```

### Scenarios

```json
{
  "id": 0,
  "mission_id": 0,
  "display_name": "Example Scenario",
  "status": { "raw_code": "Unknown", "label": "Unknown", "evidence": "Confirmed from MekHQ export", "method_backed": true, "source_owner": "Scenario#getStatus()" },
  "date": "Unknown",
  "report": { "text": "Unknown", "sanitized": true, "source_owner": "Scenario#getReport()" },
  "player_force": { "formation_id": "Unknown", "source_owner": "Scenario#getForces(Campaign)" },
  "bot_forces": [],
  "warnings": []
}
```

### Unit Market Offers

```json
{
  "id": null,
  "stable_selector_available": false,
  "display_name": "Example Unit Offer",
  "market_type": "Unknown",
  "unit_type": "Unknown",
  "unit_name": "Unknown",
  "percent": "Unknown",
  "transit_duration_days": "Unknown",
  "final_price": { "value": "Unknown", "currency": "C-bills", "evidence": "Confirmed from MekHQ export", "method_backed": true, "source_owner": "UnitMarketOffer#getPrice()" },
  "entity": { "type": "Unknown", "source_owner": "UnitMarketOffer#getEntity()" },
  "guard_fields": ["market_type", "unit_type", "unit_name", "percent", "transit_duration_days"],
  "warnings": ["UnitMarketOffer#writeToXML(...) does not expose a source-confirmed unique offer UUID."]
}
```

### Personnel Market Applicants

Use the personnel object shape plus:

```json
{
  "market_membership": {
    "is_market_applicant": true,
    "source_owner": "PersonnelMarket#getPersonnel()"
  },
  "hire_preconditions": {
    "value": "Unsupported",
    "evidence": "Unsupported",
    "warnings": ["Hiring remains manual/UI-driven until a later source-backed write command exists."]
  }
}
```

### Contract Market Offers

Use the contract object shape plus:

```json
{
  "market_membership": {
    "is_contract_offer": true,
    "source_owner": "AbstractContractMarket#getContracts()"
  },
  "prompt_policy": {
    "value": "Unsupported",
    "evidence": "Unsupported",
    "warnings": ["AtB/StratCon accept/decline prompts are not part of this read-only schema."]
  },
  "guard_fields": ["id", "display_name", "employer", "start_date", "end_date", "status"]
}
```

### Reports

Report entries should be sanitized, categorized, and short enough for MEK-RPG to display as alerts or context:

```json
{
  "category": "technical",
  "text": "Sanitized report line.",
  "date": "YYYY-MM-DD",
  "evidence": "Confirmed from MekHQ export",
  "method_backed": true,
  "source_owner": "Campaign#getTechnicalReport()",
  "contains_html": false,
  "warnings": []
}
```

## Unsupported Entry Shape

```json
{
  "area": "markets.unit_offers",
  "field": "stable_offer_id",
  "reason": "UnitMarketOffer#writeToXML(...) does not expose a unique offer UUID.",
  "evidence": "Unsupported",
  "recommended_owner": "MekHQ exporter or future source change",
  "blocks_automation": true
}
```

## Sanitized Fixture Guidance

A future fixture should:

- use fake campaign, person, unit, contract, and scenario names
- use fake UUIDs but preserve UUID format where MekHQ uses UUIDs
- include at least one method-backed finance value, unit damage value, personnel salary, unit market final price, and sanitized report line
- include at least one unsupported field for unit-market stable selector and one warning for cargo/transport validation
- avoid raw MekHQ XML, raw save payloads, copyrighted rulebook text, and live user campaign secrets

Recommended path if created later:

```text
docs/templates/mekhq-read-only-checkpoint.fixture.json
```

## Next Validation Target

The next MegaMek-side validation task should compare this draft schema against:

1. a disposable MekHQ save opened in the MekHQ UI
2. the current MEK-RPG `scripts/summarize-mekhq-save.py` output
3. method-backed values produced by a MekHQ-source or jar-backed prototype

Do not start write automation during that validation task.
