param(
    [Parameter(Mandatory = $true, Position = 0)]
    [string] $SavePath,

    [string] $InstallPath = "C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00",

    [string] $JavaHome = "C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot"
)

$ErrorActionPreference = "Stop"

$workspace = Resolve-Path (Join-Path $PSScriptRoot "..\..")
$source = Join-Path $PSScriptRoot "MekHqCheckpointExporter.java"
$classes = Join-Path $workspace "analysis\tmp\mekhq-checkpoint-exporter\classes"
$stderrLog = Join-Path $workspace "analysis\tmp\mekhq-checkpoint-exporter\stderr.log"
$resolvedSave = Resolve-Path $SavePath
$resolvedInstall = Resolve-Path $InstallPath

if (-not (Test-Path $JavaHome)) {
    throw "JDK path not found: $JavaHome"
}

$env:JAVA_HOME = $JavaHome
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

if (Test-Path $classes) {
    Remove-Item -Recurse -Force $classes
}
New-Item -ItemType Directory -Force $classes | Out-Null
New-Item -ItemType Directory -Force (Split-Path $stderrLog -Parent) | Out-Null

$compileClassPath = @(
    (Join-Path $resolvedInstall "MekHQ.jar"),
    (Join-Path $resolvedInstall "MegaMek.jar"),
    (Join-Path $resolvedInstall "MegaMekLab.jar"),
    (Join-Path $resolvedInstall "lib\*")
) -join ";"

javac -proc:none -cp $compileClassPath -d $classes $source
if ($LASTEXITCODE -ne 0) {
    throw "javac failed with exit code $LASTEXITCODE"
}

Push-Location $resolvedInstall
try {
    $runClassPath = @(
        $classes,
        "MekHQ.jar",
        "MegaMek.jar",
        "MegaMekLab.jar",
        "lib\*"
    ) -join ";"

    java "-Djava.awt.headless=true" -cp $runClassPath MekHqCheckpointExporter $resolvedSave 2> $stderrLog
    if ($LASTEXITCODE -ne 0) {
        throw "java exporter failed with exit code $LASTEXITCODE; stderr: $stderrLog"
    }
} finally {
    Pop-Location
}
