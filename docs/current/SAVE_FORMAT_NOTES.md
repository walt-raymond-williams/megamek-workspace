# MekHQ Save Format Notes

This file tracks confirmed and suspected facts about MekHQ campaign save files. Use evidence labels from `DOCUMENTATION_WORKFLOW.md`.

## Confirmed Facts

- `Confirmed from save`: The bundled sample `C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\campaigns\The Learning Ropes.cpnx.gz` is gzip-compressed XML.
- `Confirmed from save`: The bundled sample root element is `campaign`.
- `Confirmed from save`: The bundled sample reports `version="0.51.00"`.
- `Confirmed from save`: The early XML includes campaign identity, campaign date, faction, reputation, and transportation capacity/requirements.

## Safe Inspection Workflow

1. Identify the exact save path and version.
2. Do not overwrite or edit the original save.
3. Stream-read the gzip or copy it to `analysis/tmp/`.
4. Inspect XML structure before interpreting field meanings.
5. Prefer structured XML parsing once the relevant sections are known.
6. Compare extracted summaries against MekHQ UI or user-confirmed facts when possible.

## Major XML Areas

- `Unknown`: Active campaign save sections have not yet been mapped.
- `Inferred`: Major areas likely include campaign identity, personnel, units, finances, contracts, scenarios, repairs, logistics, and campaign options. Confirm from source and real campaign data before relying on exact element names.

## Field Meanings

Record field meanings here as they are confirmed.

| Field / Area | Meaning | Evidence | Source / Save |
| --- | --- | --- | --- |
| `campaign` root | Top-level MekHQ campaign save element | Confirmed from save | Bundled sample `The Learning Ropes.cpnx.gz` |
| `version="0.51.00"` | Save version reported by bundled sample | Confirmed from save | Bundled sample `The Learning Ropes.cpnx.gz` |

## Source References

Known source areas to map:

- `C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq`: campaign loading, saving, contracts, repairs, personnel, markets, and campaign UI behavior.
- `C:\Users\waltr\Documents\megamek-workspace\external\src\megamek`: unit loaders, scenario loading, tactical rules, and MegaMek game behavior.

Add specific class and file references here when confirmed.

## Open Questions

- Which MekHQ classes load and save `.cpnx.gz` campaign files in version `0.51.00`?
- What are the major XML sections in the user's active campaign save?
- Where are active contracts, scenarios, unit damage, personnel fatigue, finances, and campaign options represented?
- Which fields are safe to parse as stable identifiers, and which are display or transient values?
