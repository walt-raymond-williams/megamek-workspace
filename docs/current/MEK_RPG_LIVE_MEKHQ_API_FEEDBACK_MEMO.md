# MEK-RPG Live MekHQ API Feedback Memo

Date: 2026-06-22

Audience: MEK-RPG team

Purpose: ask MEK-RPG to review whether the existing read-only checkpoint export needs should move from "user saves MekHQ, MEK-RPG reads the save/checkpoint JSON" toward a live localhost MekHQ API that exposes the currently loaded campaign state directly.

## Summary

The MegaMek workspace has now proven a small in-process MekHQ control API can run inside the open MekHQ GUI app. The first prototype is command-side only: `POST /advance-day` calls the loaded campaign's real `Campaign#newDay()` path, guarded by expected campaign identity and expected date, and can suppress daily nag dialogs for that API command.

That prototype changes the integration question. Instead of requiring the user to save a campaign before MEK-RPG can read state, MekHQ could expose a read-only live campaign-state API from the same loaded app:

- MEK-RPG asks MekHQ for current campaign state over `127.0.0.1`.
- MekHQ returns method-backed DTO JSON from the live `Campaign` object.
- The user still controls MekHQ as the hard ledger.
- Save-file parsing remains a fallback, fixture source, and offline import path.

This memo asks MEK-RPG which fields, shape, and workflow would make that useful.

## What We Think MEK-RPG Needs

Based on completed MEK-RPG checkpoint feedback issues `#84` through `#89`, the current consumer-side field priorities are:

1. Campaign basics: campaign id, name, faction, current date, current system/location, and read-only/import metadata.
2. Finances: current balance, loans/debt flags, and recent finance report or transaction context.
3. Personnel: MekHQ person ids, display names, ranks, roles, assignment, status, fatigue, hits/injuries, and salary/availability where method-backed.
4. Units: MekHQ unit ids, display names, chassis/model/type/weight, status, crew/person links, assigned techs, unit condition, repair summary, and transport/cargo warnings.
5. Contracts and scenarios: active contract ids/names/status, employer/enemy, system, dates, terms, scenario ids/status/date/report, and tactical handoff context.
6. Repairs and logistics: shopping list, repair pressure, parts pressure, tech bottlenecks, cargo/transport advisories, and daily technical/acquisition reports.
7. Markets: unit/personnel/contract offers as display-only opportunities, not automation selectors until stable ids and prompt policies exist.
8. Reports and warnings: sanitized current/technical/finance/personnel/battle/politics report buckets plus structured warnings and unsupported entries.

MEK-RPG has also said the trust-boundary fields matter:

- `evidence`
- `source_owner`
- `method_backed`
- `warnings`
- `unsupported`

Those fields should remain in any live API response whenever a value could become campaign-facing context, a hard checkpoint fact, or action-adjacent data.

## Proposed Live API Shape

Keep the same top-level grouping as the checkpoint export unless MEK-RPG wants a change:

```text
bridge_metadata, campaign, finances, personnel, units, contracts, scenarios,
repairs_and_logistics, markets, reports, unsupported
```

Recommended first endpoint:

```http
GET /campaign/state?sections=summary,finances,personnel,units,contracts,scenarios,repairs,reports
```

Alternative smaller endpoints:

```http
GET /campaign/summary
GET /campaign/personnel
GET /campaign/units
GET /campaign/contracts
GET /campaign/scenarios
GET /campaign/repairs
GET /campaign/reports
GET /campaign/markets
```

Every response should include identity guards:

```json
{
  "status": "ready",
  "campaignId": "full-mekhq-campaign-id-or-Unknown",
  "campaignName": "Example Campaign",
  "campaignDate": "3025-04-09",
  "mekhqVersion": "0.51.01",
  "apiMode": "local-read-only",
  "warnings": []
}
```

For field payloads, prefer the existing checkpoint envelope where the value is not trivially self-evident:

```json
{
  "value": "3025-04-09",
  "evidence": "Confirmed from MekHQ API",
  "method_backed": true,
  "source_owner": "Campaign#getLocalDate()",
  "warnings": []
}
```

## Why Live API May Be Better Than Save-Then-Import

- It avoids stale reads when MekHQ has unsaved UI state.
- It removes the requirement that the user save before MEK-RPG can build context.
- It lets MekHQ own derived values through real methods instead of forcing MEK-RPG to infer from XML.
- It can keep the same read-only trust model while improving freshness.
- It can eventually pair cleanly with guarded commands like `POST /advance-day`.

The save/checkpoint path should still remain because it is useful for offline analysis, regression fixtures, audit trails, and cases where MekHQ is not running.

## Questions For MEK-RPG

1. Does MEK-RPG want the live API to preserve the checkpoint export's top-level groups exactly, or would a smaller endpoint-oriented shape be easier to consume?
2. Which first endpoint would be most useful: `GET /campaign/summary`, full `GET /campaign/state`, or sectioned endpoints?
3. Should live API responses keep the full trust envelope on most fields, or should only caution/derived/action-adjacent fields use envelopes?
4. Which fields are required for a GM context packet before a session starts?
5. Which fields are required during play, without asking the user to save MekHQ?
6. Should MEK-RPG treat live API data as a checkpoint fact immediately, or as "live context" that becomes durable only after a save/import event?
7. Does MEK-RPG need a `checkpoint_id` or `state_revision` from the live API, even when no save file is involved?
8. How should MEK-RPG display "MekHQ is open but campaign is dirty/unsaved" if MekHQ can expose that signal?
9. Are markets useful in the live API now, or should we omit them until stable offer selectors exist?
10. For reports, does MEK-RPG want raw sanitized report lines, compact classified summaries, or both?
11. Which warnings should block a GM context refresh versus merely appear in bridge diagnostics?
12. Should live API state include local filesystem paths at all, or should it avoid them unless explicitly requested for debugging?

## Producer Recommendation

The MegaMek-side recommendation is to build this in phases:

1. Add `GET /campaign/summary` to the existing local MekHQ control API.
2. Add `GET /campaign/state?sections=...` using the existing checkpoint export top-level groups.
3. Keep the API disabled by default behind `-Dmekhq.controlApi.enabled=true` and bound to `127.0.0.1`.
4. Keep responses read-only and method-backed where practical.
5. Keep save-file checkpoint import as the offline fallback and regression fixture path.
6. Do not add market purchases, hiring, contract acceptance, repair execution, or tactical-result application as part of this read-only state API.

## Proposed MEK-RPG Feedback Output

Please reply with:

- endpoint preference
- fields required for first useful integration
- fields that should stay out of the first version
- trust-envelope preference
- whether live data should become durable campaign state immediately or only after save/import confirmation
- any adapter or test fixture changes MEK-RPG would want before consuming live API JSON

## Relevant MegaMek-Side References

- `docs/current/MEKHQ_ADVANCE_DAY_CONTROL_API_PROTOTYPE.md`
- `docs/current/MEK_RPG_MEKHQ_CHECKPOINT_EXPORT_SCHEMA.md`
- `docs/current/MEK_RPG_MEKHQ_BRIDGE_PRIMITIVES.md`
- `docs/current/MEK_RPG_CHECKPOINT_EXPORT_REVIEW_MEMO.md`

Relevant MEK-RPG-side references:

- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_CHECKPOINT_CONSUMED_FIELD_MAPPING.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_CHECKPOINT_WARNING_SURFACING.md`
- `C:\Users\waltr\Documents\mek-rpg\docs\current\MEKHQ_BRIDGE_DATA_MODEL.md`
