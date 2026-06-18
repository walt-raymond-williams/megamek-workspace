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

Expected Java major version: `21`.

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

## Check Logs

```powershell
Get-ChildItem C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00\logs -Force
```

Logs are runtime evidence, not durable documentation by themselves. Summarize useful findings in `docs/current/`.
