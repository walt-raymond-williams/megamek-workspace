# MekHQ Advance Day Control API Prototype

## Status

`Validated locally`: the local MekHQ checkout has a disabled-by-default source prototype, Gradle compile verification passes, the source build can launch MekHQ with the control API enabled, and a user-assisted live `/advance-day` test advanced a loaded campaign exactly one day without saving.

## Source Changes

Target repo:

- `external/src/mekhq`

Source branch:

- `codex/mekhq-advance-day-control-api`

Source commit:

- `9046a8075e` (`Add local advance day control API prototype`)
- `17207baa90` (`Allow local API to suppress advance day nags`)

Files changed:

- `MekHQ/src/mekhq/service/LocalControlService.java`
- `MekHQ/src/mekhq/MekHQ.java`
- `MekHQ/src/mekhq/gui/dialog/nagDialogs/NagController.java`

`Confirmed from source`: the prototype adds a disabled-by-default local HTTP service owned by the running `MekHQ` application instance. The service starts only when the JVM property `mekhq.controlApi.enabled` is true.

`Confirmed from source`: the service binds to `127.0.0.1` and defaults to port `32180`. A different port can be selected with `mekhq.controlApi.port`.

## API Shape

Enable the endpoint when launching a locally modified MekHQ build:

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat :MekHQ:run --args='' -Dmekhq.controlApi.enabled=true
```

`Unknown`: the exact source launch command still needs live validation with the user present. The important JVM property is:

```text
-Dmekhq.controlApi.enabled=true
```

Optional port override:

```text
-Dmekhq.controlApi.port=32180
```

Status endpoint:

```powershell
Invoke-RestMethod -Method Get -Uri 'http://127.0.0.1:32180/status'
```

Advance exactly one day:

```powershell
$body = @{
  command = 'advanceDayOnce'
  expectedCampaignName = 'The Learning Ropes'
  expectedDate = '3025-07-20'
  dismissAdvanceDayNags = $true
  saveAfterSuccess = $false
} | ConvertTo-Json

Invoke-RestMethod `
  -Method Post `
  -Uri 'http://127.0.0.1:32180/advance-day' `
  -ContentType 'application/json' `
  -Body $body
```

Request fields:

- `command`: must be `advanceDayOnce`.
- `expectedCampaignName`: accepted campaign identity guard.
- `expectedCampaignId`: accepted campaign identity guard.
- `expectedDate`: required ISO date guard, for example `3025-07-20`.
- `saveAfterSuccess`: defaults to false when omitted.
- `savePath`: required only when `saveAfterSuccess` is true.
- `dismissAdvanceDayNags`: optional boolean; defaults to true. When true, the local API temporarily suppresses MekHQ's daily advance-time nag sequence for this one command, equivalent to choosing the continue/advance-anyway path for those nag warnings.

At least one of `expectedCampaignName` or `expectedCampaignId` is required.

Response statuses:

- `advanced`: `Campaign#newDay()` returned true and date advanced by exactly one day.
- `blocked`: the command was already running or MekHQ blocked/canceled day advancement.
- `failed`: the command threw an exception or failed on the Swing event dispatch thread.
- `refused`: preflight checks failed, such as no loaded campaign, wrong expected date, or wrong campaign identity.

## Guardrails Implemented

- `Confirmed from source`: disabled by default.
- `Confirmed from source`: loopback bind only, using `127.0.0.1`.
- `Confirmed from source`: command concurrency guard allows only one local command at a time.
- `Confirmed from source`: refuses to run when no campaign/controller/GUI is loaded.
- `Confirmed from source`: requires expected date.
- `Confirmed from source`: requires expected campaign name or id.
- `Confirmed from source`: invokes `Campaign#newDay()` on the Swing event dispatch thread.
- `Confirmed from source`: `dismissAdvanceDayNags` defaults to true and temporarily suppresses only `NagController.triggerDailyNags(...)` for the current local API command.
- `Confirmed from source`: checks that the date advanced by exactly one day.
- `Confirmed from source`: does not auto-answer dialogs.
- `Confirmed from source`: save-after-success is opt-in and requires an explicit path.
- `Confirmed from source`: save-after-success calls `CampaignGUI.saveCampaign(...)`, not raw XML writing.

## Verification

`Confirmed locally`: Gradle compile verification passed on `2026-06-22`:

```text
.\gradlew.bat :MekHQ:compileJava
```

Observed state on `2026-06-22`: after installing portable JDK 17 at `C:\Users\waltr\.jdks\temurin-17` and configuring user-level Gradle discovery, `.\gradlew.bat :MekHQ:compileJava` completed successfully from `external/src/mekhq` in about 199 seconds. The command emitted existing deprecation and unchecked-operation warnings, including one warning in modified `MekHQ.java` for pre-existing `AtBGameThread` usage, but no compile errors.

`Confirmed locally`: `.\gradlew.bat :MekHQ:run` launched MekHQ with `JAVA_TOOL_OPTIONS='-Dmekhq.controlApi.enabled=true -Dmekhq.controlApi.port=32180'` on `2026-06-22`. `GET /status` returned `ready` and reported no loaded campaign.

`Confirmed locally`: after adding `dismissAdvanceDayNags`, `.\gradlew.bat :MekHQ:compileJava` and `.\gradlew.bat :MekHQ:checkstyleMain` both passed on `2026-06-22`.

`Confirmed locally`: fallback `javac` syntax/type checks passed against the installed MekHQ `0.51.00` jars:

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
New-Item -ItemType Directory -Force analysis\tmp\issue-35-compile | Out-Null
javac -d analysis\tmp\issue-35-compile -cp 'external\installs\MekHQ-0.51.00\MekHQ.jar;external\installs\MekHQ-0.51.00\MegaMek.jar;external\installs\MekHQ-0.51.00\MegaMekLab.jar;external\installs\MekHQ-0.51.00\lib\*' external\src\mekhq\MekHQ\src\mekhq\service\LocalControlService.java
javac -d analysis\tmp\issue-35-compile -cp 'analysis\tmp\issue-35-compile;external\installs\MekHQ-0.51.00\MekHQ.jar;external\installs\MekHQ-0.51.00\MegaMek.jar;external\installs\MekHQ-0.51.00\MegaMekLab.jar;external\installs\MekHQ-0.51.00\lib\*' external\src\mekhq\MekHQ\src\mekhq\MekHQ.java
```

The `MekHQ.java` fallback check emitted one unrelated existing deprecation warning for `AtBGameThread`.

`Confirmed locally with user present`: live `/advance-day` endpoint test passed on `2026-06-22` against loaded campaign `The Learning Ropes` (`ea0d334a-1582-459a-9084-b349f0baca5a`). The request guarded on expected date `3025-04-08` with `saveAfterSuccess=false`; the response returned `advanced`, `newDayReturned=true`, `dateBefore=3025-04-08`, `dateAfter=3025-04-09`, `visibleDialogs=0`, and `saveAttempted=false`. A follow-up `/status` call reported the campaign at `3025-04-09`.

`Confirmed by user`: dialogs were visible during the live `/advance-day` call and were manually dismissed by the user. Therefore, the `visibleDialogs=0` response should be interpreted only as "no dialogs detected when the response was assembled," not proof that the advance completed without prompts.

`Confirmed locally with user present`: after restarting MekHQ on source commit `17207baa90`, a guarded `/advance-day` request with `dismissAdvanceDayNags=true` advanced `The Learning Ropes` from `3025-04-08` to `3025-04-09`. The response returned `advanced`, `newDayReturned=true`, `advanceDayNagsDismissed=true`, `visibleDialogs=0`, and `saveAttempted=false`. A follow-up `/status` call reported the campaign at `3025-04-09`.

## User-Assisted Live Test Plan

When the user is present:

1. Build or launch the locally modified MekHQ source with `mekhq.controlApi.enabled=true`.
2. Open a copied/disposable campaign save.
3. Call `/status` and confirm the expected campaign name/id/date.
4. Call `/advance-day` with `saveAfterSuccess=false`.
5. Confirm MekHQ advances exactly one day in the UI.
6. Confirm the JSON response reports `advanced`, `dateBefore`, `dateAfter`, and `newDayReturned=true`.
7. Try one refused call with an intentionally wrong expected date.
8. Only after the no-save call succeeds, test `saveAfterSuccess=true` to an explicit disposable output path.

Steps 1-6 were completed successfully on `2026-06-22` against `The Learning Ropes`, advancing from `3025-04-08` to `3025-04-09` without saving. The user manually dismissed visible dialogs during the call.

Do not test against the real campaign save until the disposable campaign test is understood and accepted.

## Remaining Work

- Try one refused call with an intentionally wrong expected date.
- Test `dismissAdvanceDayNags=false` to confirm nag prompts still appear when requested.
- Test `saveAfterSuccess=true` to an explicit disposable output path.
- Decide whether to add broader prompt/dialog reporting so non-nag dialogs that appeared and were user-dismissed during the command are not lost by the final `visibleDialogs` snapshot.
- Decide whether this prototype should stay local-only or move to a more polished feature branch/PR shape.
