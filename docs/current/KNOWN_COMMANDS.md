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

- `Confirmed locally`: `java -version` and `javac -version` currently resolve to Java 8 from the shell.
- `Confirmed locally`: JDK 21 exists at `C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot`.

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

## Search Source

```powershell
rg "cpnx" C:\Users\waltr\Documents\megamek-workspace\external\src
rg "class Campaign" C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq
rg "save" C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq
rg "Scenario" C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq
rg "MULParser|MtfFile|BLKFile|ScenarioLoader" C:\Users\waltr\Documents\megamek-workspace\external\src\megamek
```

When a source search answers a durable question, update the relevant `docs/current/` file with the file or class reference.

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

Current blocker:

- `Confirmed from source`: `megamek`, `mekhq`, `megameklab`, and `mm-data` build files configure Java toolchain 21.
- `Confirmed from source`: each repo contains `gradle\gradle-daemon-jvm.properties` with `toolchainVersion=17`.
- `Confirmed locally`: running `.\gradlew.bat tasks --all` currently fails because Gradle cannot resolve/download daemon toolchain 17 on this Windows machine.

Commands below are intended commands based on local Gradle build files. Treat them as `Blocked` until the Java 17 daemon issue is resolved and the command is rerun successfully.

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

After a command succeeds locally, update this section from `Blocked` to `Verified` for that command and record the date.

## Check Logs

```powershell
Get-ChildItem C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\logs -Force
```

Logs are runtime evidence, not durable documentation by themselves. Summarize useful findings in `docs/current/`.
