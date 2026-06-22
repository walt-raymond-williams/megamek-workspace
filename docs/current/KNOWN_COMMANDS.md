# Known Commands

These commands are safe, repeatable starting points for this workspace. Run them from PowerShell unless noted otherwise.

## Workspace

```powershell
cd C:\Users\waltr\Documents\megamek-workspace
git status --short
```

## Verify Local Runtime

```powershell
java -version
javac -version
Test-Path 'C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\MekHQ.exe'
Test-Path 'C:\Users\waltr\Documents\megamek-workspace\external\src\megamek'
Test-Path 'C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq'
Test-Path 'C:\Users\waltr\Documents\megamek-workspace\external\src\megameklab'
Test-Path 'C:\Users\waltr\Documents\megamek-workspace\external\src\mm-data'
```

Expected Java major version for running the installed suite: `21`.

Current local note:

- `Confirmed locally`: `java -version` and `javac -version` currently resolve to Java 21 from the shell.
- `Confirmed locally`: portable JDK 17 exists at `C:\Users\waltr\.jdks\temurin-17`.
- `Confirmed locally`: JDK 21 exists at `C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot`.

User-level Gradle toolchain discovery is configured in `C:\Users\waltr\.gradle\gradle.properties` with both JDK paths:

```properties
org.gradle.java.installations.auto-detect=true
org.gradle.java.installations.auto-download=false
org.gradle.java.installations.paths=C:/Users/waltr/.jdks/temurin-17,C:/Program Files/Eclipse Adoptium/jdk-21.0.11.10-hotspot
```

Use this in a PowerShell session when a command needs JDK 17 first on `PATH`:

```powershell
$env:JAVA_HOME='C:\Users\waltr\.jdks\temurin-17'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
java -version
javac -version
```

Use this in a PowerShell session when a command needs JDK 21 first on `PATH`:

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
java -version
javac -version
```

## Launch Installed Suite

```powershell
cd C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00
.\MekHQ.exe
.\MegaMek.exe
.\MegaMekLab.exe
```

## Inspect A Campaign Save Safely

Do not overwrite the original save. Stream-read or copy to `analysis/tmp/` first.

List the first lines of a gzip-compressed campaign XML file:

```powershell
$save = 'C:\path\to\campaign.cpnx.gz'
$stream = [System.IO.File]::OpenRead($save)
$gzip = [System.IO.Compression.GZipStream]::new($stream, [System.IO.Compression.CompressionMode]::Decompress)
$reader = [System.IO.StreamReader]::new($gzip)
1..40 | ForEach-Object { $reader.ReadLine() }
$reader.Dispose()
$gzip.Dispose()
$stream.Dispose()
```

Copy a save for experimental extraction:

```powershell
New-Item -ItemType Directory -Force analysis\tmp
Copy-Item 'C:\path\to\campaign.cpnx.gz' analysis\tmp\
```

## Export A Read-Only MekHQ Checkpoint Prototype

This prototype compiles a workspace Java helper against the installed MekHQ `0.51.00` jars, loads an explicit save path read-only, and writes checkpoint JSON to stdout. It writes compiled classes and stderr logs under ignored `analysis/tmp/mekhq-checkpoint-exporter/`.

```powershell
$save = 'C:\path\to\campaign.cpnx.gz'
$json = powershell -ExecutionPolicy Bypass -File tools\mekhq-checkpoint-exporter\run-mekhq-checkpoint-exporter.ps1 $save
$parsed = $json | ConvertFrom-Json
```

Smoke check the hardened prototype output:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File tools\mekhq-checkpoint-exporter\test-mekhq-checkpoint-exporter.ps1
```

Verified on `2026-06-21` against copied save `analysis/tmp/issue-22/Autosave-1-The Learning Ropes-30250720.cpnx.gz`; hardened smoke check passed on `2026-06-22`.

## Validate Battle-Record MUL Writer/Parser Round Trip

This ignored scratch proof writes a controlled battle-record `<record>` MUL with native MegaMek serialization and parses it back with `MULParser`.

```powershell
cd C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
javac -cp 'MegaMek.jar;MekHQ.jar;lib/*' 'C:\Users\waltr\Documents\megamek-workspace\analysis\tmp\Issue10BattleRecordRoundTrip.java'
java -cp 'MegaMek.jar;MekHQ.jar;lib/*;C:\Users\waltr\Documents\megamek-workspace\analysis\tmp' Issue10BattleRecordRoundTrip 'C:\Users\waltr\Documents\megamek-workspace\analysis\tmp\issue-10-battle-record-round-trip.mul'
```

Verified on `2026-06-22`: expected survivor, salvage, retreated, devastated, kill, external id, crew external id, and crew hit checks passed.

## Inspect The Demo Campaign Fixture

Use a copied save and the read-only checkpoint exporter for method-backed summary facts:

```powershell
New-Item -ItemType Directory -Force analysis\tmp\issue-2
Copy-Item campaigns\demo\ai-ready-demo.cpnx.gz analysis\tmp\issue-2\ai-ready-demo.cpnx.gz
$save = 'C:\Users\waltr\Documents\megamek-workspace\analysis\tmp\issue-2\ai-ready-demo.cpnx.gz'
$json = powershell -NoProfile -ExecutionPolicy Bypass -File tools\mekhq-checkpoint-exporter\run-mekhq-checkpoint-exporter.ps1 $save
$parsed = $json | ConvertFrom-Json
```

Verified on `2026-06-22` for the first demo status report.

## Search Source

```powershell
rg "cpnx" C:\Users\waltr\Documents\megamek-workspace\external\src
rg "class Campaign" C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq
rg "save" C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq
rg "Scenario" C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq
rg "MULParser|MtfFile|BLKFile|ScenarioLoader" C:\Users\waltr\Documents\megamek-workspace\external\src\megamek
```

When a source search answers a durable question, update the relevant `docs/current/` file with the file or class reference.

Trace MekHQ campaign save/load behavior:

```powershell
rg "cpnx|CampaignXml|load.*campaign|save.*campaign" C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq\MekHQ\src -g "*.java"
rg "GZIPInputStream|GZIPOutputStream|writeToXML|CampaignXmlParser" C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq\MekHQ\src -g "*.java"
```

Verified on `2026-06-22` for source-backed campaign save/load notes.

## Search Local Help And Docs

Search MekHQ glossary, source docs, and installed docs:

```powershell
rg -n "fatigue|maintenance|contract|StratCon|resupply|force generation" external/src/mekhq/MekHQ/resources external/src/mekhq/MekHQ/docs external/installs/MekHQ-0.51.00/docs
rg -n "scenario|RAT|force generator|movement|Princess|AutoResolve" external/src/megamek/megamek/docs external/installs/MekHQ-0.51.00/docs
rg -n "GlossaryDocs|DocumentationEntry|GlossaryEntry" external/src/mekhq/MekHQ/src external/src/mekhq/MekHQ/resources
```

Extract a local help PDF to ignored scratch text:

```powershell
New-Item -ItemType Directory -Force analysis\tmp\docs
pdftotext "external/src/mekhq/MekHQ/docs/GlossaryDocs/Unit Markets.pdf" analysis\tmp\docs\unit-markets.txt
rg -n "contract|availability|market" analysis\tmp\docs\unit-markets.txt
```

Verified on `2026-06-22`: `pdftotext` is available from Poppler and Python `pypdf` is importable.

## Source Repo Status

Check all source repo worktrees before source investigation or edits:

```powershell
git -C C:\Users\waltr\Documents\megamek-workspace\external\src\megamek status --short --branch
git -C C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq status --short --branch
git -C C:\Users\waltr\Documents\megamek-workspace\external\src\megameklab status --short --branch
git -C C:\Users\waltr\Documents\megamek-workspace\external\src\mm-data status --short --branch
```

Last observed state on `2026-06-18`:

- `Confirmed locally`: all four source repos were clean on `main...origin/main`.

## Source Repo Build And Test

The source repos are Gradle-wrapper projects.

Current environment:

- `Confirmed from source`: `megamek`, `mekhq`, `megameklab`, and `mm-data` build files configure Java toolchain 21.
- `Confirmed from source`: each repo contains `gradle\gradle-daemon-jvm.properties` with `toolchainVersion=17`.
- `Confirmed locally`: portable JDK 17 is installed at `C:\Users\waltr\.jdks\temurin-17`, and user-level Gradle discovery includes both JDK 17 and JDK 21.
- `Confirmed locally`: on `2026-06-22`, `.\gradlew.bat :MekHQ:compileJava` started with a JDK 17 Gradle daemon and JDK 21 worker, resolving the earlier missing-JDK-17 blocker. The command exceeded a 304-second session timeout and was stopped with `.\gradlew.bat --stop`; rerun before marking Gradle compilation verified.

Commands below are intended commands based on local Gradle build files. Treat them as `Not yet verified` until each command succeeds locally and the result is recorded.

MegaMek:

```powershell
cd C:\Users\waltr\Documents\megamek-workspace\external\src\megamek
.\gradlew.bat :megamek:test
.\gradlew.bat :megamek:easyTest
.\gradlew.bat :megamek:run
.\gradlew.bat :megamek:distZip
```

MekHQ:

```powershell
cd C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq
.\gradlew.bat :MekHQ:test
.\gradlew.bat :MekHQ:testAll
.\gradlew.bat :MekHQ:run
.\gradlew.bat :MekHQ:distZip
```

MegaMekLab:

```powershell
cd C:\Users\waltr\Documents\megamek-workspace\external\src\megameklab
.\gradlew.bat :megameklab:test
.\gradlew.bat :megameklab:testAll
.\gradlew.bat :megameklab:run
.\gradlew.bat :megameklab:distZip
```

mm-data:

```powershell
cd C:\Users\waltr\Documents\megamek-workspace\external\src\mm-data
.\gradlew.bat check
.\gradlew.bat stageFiles
.\gradlew.bat stageMMFiles
.\gradlew.bat stageMMLFiles
```

After a command succeeds locally, update this section from `Not yet verified` to `Verified` for that command and record the date.

## MekHQ Local Advance Day Control API Prototype

Issue `#35` adds a source prototype under `external/src/mekhq` that is disabled by default. When a locally modified MekHQ build can be launched, enable the local-only endpoint with this JVM property:

```powershell
-Dmekhq.controlApi.enabled=true
```

Optional port override:

```powershell
-Dmekhq.controlApi.port=32180
```

Call status:

```powershell
Invoke-RestMethod -Method Get -Uri 'http://127.0.0.1:32180/status'
```

Advance exactly one day against an already-open copied/disposable campaign:

```powershell
$body = @{
  command = 'advanceDayOnce'
  expectedCampaignName = 'The Learning Ropes'
  expectedDate = '3025-07-20'
  saveAfterSuccess = $false
} | ConvertTo-Json

Invoke-RestMethod `
  -Method Post `
  -Uri 'http://127.0.0.1:32180/advance-day' `
  -ContentType 'application/json' `
  -Body $body
```

Current verification state:

- `Attempted`: `.\gradlew.bat :MekHQ:compileJava` started after installing JDK 17 and configuring Gradle toolchain discovery, but exceeded a 304-second session timeout and was stopped with `.\gradlew.bat --stop`.
- `Confirmed locally`: fallback `javac` checks for `LocalControlService.java` and modified `MekHQ.java` passed against installed MekHQ `0.51.00` jars on `2026-06-22`.
- `Not run`: live endpoint test; wait for the user and use copied/disposable saves only.

## Check Logs

```powershell
Get-ChildItem C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\logs -Force
```

Logs are runtime evidence, not durable documentation by themselves. Summarize useful findings in `docs/current/`.
