# Battle-Record MUL Round-Trip Validation

This note records the issue `#10` prototype status for validating tabletop battle-record MUL imports against MekHQ `0.51.00`.

Evidence labels follow `DOCUMENTATION_WORKFLOW.md`.

## Summary

- `Confirmed locally`: a generated battle-record `<record>` MUL can be written with MegaMek's native `EntityListFile.writeEntityList(...)` and parsed back with `MULParser`.
- `Confirmed locally`: the parser preserved all first-pass result buckets: one `survivors` entity, one `salvage` entity, one `retreated` entity, one `devastated` entity, and two `kills` entries.
- `Confirmed locally`: the parser preserved the survivor entity `externalId`, survivor crew `externalId`, and one pilot hit.
- `Confirmed from source`: MekHQ's Resolve Manually dialog calls `ResolveScenarioTracker.processMulFiles()`, which parses the selected battle-record MUL through `MULParser`.
- `Blocked`: live MekHQ UI import and post-import campaign observation could not be completed in this session because the Windows Computer Use helper still reports `Computer Use native pipe path is unavailable`.

## Local Prototype

Scratch files are intentionally ignored under `analysis/tmp/`:

- `analysis/tmp/Issue10BattleRecordRoundTrip.java`
- `analysis/tmp/issue-10-battle-record-round-trip.mul`

The prototype creates four simple Mek entities from the installed unit cache:

| Result bucket | Unit | Prototype state |
| --- | --- | --- |
| `survivors` | Wolverine WVR-6R | Player survivor with fixed unit UUID, fixed crew UUID, right-arm armor damage, and one pilot hit. |
| `salvage` | Locust LCT-1V | Enemy salvage candidate with right leg destroyed. |
| `retreated` | Commando COM-2D | Enemy unit that escaped and should not become normal salvage. |
| `devastated` | Stinger STG-3R | Enemy unit with center torso destroyed, treated as devastated rather than normal salvage. |

The prototype also writes two kill-credit rows assigning the Locust and Stinger kills to the Wolverine's entity `externalId`.

## Verification Command

Run from the installed MekHQ directory:

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
javac -cp 'MegaMek.jar;MekHQ.jar;lib/*' 'C:\Users\waltr\Documents\megamek-workspace\analysis\tmp\Issue10BattleRecordRoundTrip.java'
java -cp 'MegaMek.jar;MekHQ.jar;lib/*;C:\Users\waltr\Documents\megamek-workspace\analysis\tmp' Issue10BattleRecordRoundTrip 'C:\Users\waltr\Documents\megamek-workspace\analysis\tmp\issue-10-battle-record-round-trip.mul'
```

Observed output on `2026-06-18`:

```text
wrote=C:\Users\waltr\Documents\megamek-workspace\analysis\tmp\issue-10-battle-record-round-trip.mul
survivors=1
salvage=1
retreated=1
devastated=1
kills=2
survivorExternalId=11111111-1111-1111-1111-111111111111
survivorCrewExternalId=aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
survivorCrewHits=1
```

The Java compiler emitted an annotation-processing warning from the installed classpath, and MegaMek logging emitted normal Log4j startup/shutdown messages. Neither affected the result.

## Expected MekHQ Effects

`Confirmed from source`: once selected in Resolve Manually, MekHQ uses `ResolveScenarioTracker.loadUnitsAndPilots(...)` to sort parsed entities by section.

Expected effects for this prototype shape:

- `survivors`: a matching player campaign unit should be found by entity `externalId`, keep its post-battle entity state, and apply the crew hit through the matching crew `externalId`.
- `salvage`: an enemy unit should become potential salvage only when the operator claims battlefield control in the MekHQ prompt.
- `retreated`: an enemy unit should be recorded as retreated, not offered as normal salvage.
- `devastated`: an enemy unit should be tracked as devastated enemy loss, not ordinary kept salvage.
- `kills`: kill credit should map killed display names to the killer campaign unit by killer `externalId`.

## Remaining UI Validation

The unverified part of issue `#10` is the live MekHQ import pass:

`Updated 2026-06-19`: user task `#23` should happen before this pass. The preferred target is no longer an arbitrary sample campaign; it is a safe MekHQ campaign set up to reflect the user's real-life tabletop unit, including mechs, personnel, support staff, equipment, and DropShip/transport assumptions.

1. Open a disposable campaign save, never the active original.
2. Pick or create a scenario with deployed player units whose campaign `Unit` UUIDs match the generated survivor/killer entity ids.
3. Use MekHQ's Resolve Manually flow.
4. When prompted for a MUL file, select a battle-record `<record>` MUL.
5. Choose battlefield control according to the tabletop result.
6. Finish the resolve wizard.
7. Inspect the disposable campaign for unit damage, pilot wound, salvage candidates, retreated enemy handling, devastated enemy handling, and kill credit.

`Blocked`: Codex could not complete these clicks because Computer Use is unavailable in this environment. This is the same class of blocker as issue `#17`, but for battle-record import validation rather than roster replacement.

## Implications

`Confirmed locally`: custom generation does not need to hand-write full entity XML if it can construct MegaMek `Entity` objects and let `EntityListFile.writeEntityList(...)` serialize each bucket.

`Confirmed by workflow decision`: issue `#11` completed in `TABLETOP_RESULT_MUL_GENERATION_STRATEGY.md`. It selected MekHQ's built-in Resolve Manually workflow as the baseline, with a narrowed workspace installed-jar helper as the first custom path if generation is needed. The helper strategy is based on these two practical approaches:

- a small Java helper that loads setup MUL entities, applies tabletop result changes, and writes a battle-record `<record>` file with native MegaMek serialization
- a manual workflow that edits or reviews an exported MegaMek battle-record file, if the user can obtain one from a test game

Avoid a plain text/XML-only generator unless there is a strong reason; the native writer already handles broad entity details such as ammo, critical slots, crew hits, and external ids.
