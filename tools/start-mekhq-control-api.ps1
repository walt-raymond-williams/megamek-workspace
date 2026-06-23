param(
    [string] $SourceRoot = "C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq",
    [string] $JavaHome = "C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot",
    [int] $Port = 32180,
    [switch] $NoBuild,
    [int] $PollTimeoutSeconds = 180
)

$ErrorActionPreference = "Stop"

function Write-Step {
    param([string] $Message)
    Write-Host "[mekhq-api] $Message"
}

function Get-ApiStatus {
    param([int] $StatusPort)

    try {
        Invoke-RestMethod -Method Get -Uri "http://127.0.0.1:$StatusPort/status" -TimeoutSec 3
    } catch {
        $null
    }
}

$existingStatus = Get-ApiStatus -StatusPort $Port
if ($null -ne $existingStatus) {
    Write-Step "API is already responding on port $Port."
    $existingStatus | ConvertTo-Json -Depth 10
    exit 0
}

if (-not (Test-Path $SourceRoot)) {
    throw "MekHQ source root not found: $SourceRoot"
}

if (-not (Test-Path (Join-Path $JavaHome "bin\java.exe"))) {
    throw "Java runtime not found under JavaHome: $JavaHome"
}

if (-not $NoBuild) {
    Write-Step "Building MekHQ source install with Gradle."
    Push-Location $SourceRoot
    try {
        & .\gradlew.bat :MekHQ:assemble
    } finally {
        Pop-Location
    }
}

$appDir = Join-Path $SourceRoot "MekHQ\build\install\MekHQ"
$mekHqExe = Join-Path $appDir "MekHQ.exe"

if (-not (Test-Path $mekHqExe)) {
    throw "Built MekHQ executable not found: $mekHqExe"
}

$env:JAVA_HOME = $JavaHome
$env:Path = "$JavaHome\bin;$env:Path"
$env:JAVA_TOOL_OPTIONS = "-Dmekhq.controlApi.enabled=true -Dmekhq.controlApi.port=$Port"

Write-Step "Starting MekHQ from $appDir with local control API on 127.0.0.1:$Port."
$process = Start-Process -FilePath $mekHqExe -WorkingDirectory $appDir -PassThru -WindowStyle Normal
Write-Step "Started MekHQ process $($process.Id)."

$deadline = (Get-Date).AddSeconds($PollTimeoutSeconds)
while ((Get-Date) -lt $deadline) {
    $status = Get-ApiStatus -StatusPort $Port
    if ($null -ne $status) {
        Write-Step "API is ready."
        $status | ConvertTo-Json -Depth 10
        exit 0
    }

    Start-Sleep -Seconds 5
}

Write-Step "API did not respond within $PollTimeoutSeconds seconds."
$process | Select-Object Id, ProcessName, Path, StartTime, Responding | Format-List
exit 1
