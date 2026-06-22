param(
    [string] $SavePath = "C:\Users\waltr\Documents\megamek-workspace\analysis\tmp\issue-22\Autosave-1-The Learning Ropes-30250720.cpnx.gz"
)

$ErrorActionPreference = "Stop"

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$exporter = Join-Path $scriptDir "run-mekhq-checkpoint-exporter.ps1"

if (-not (Test-Path $SavePath)) {
    throw "Checkpoint smoke save not found: $SavePath"
}

function Assert-True {
    param(
        [bool] $Condition,
        [string] $Message
    )

    if (-not $Condition) {
        throw $Message
    }
}

$json = powershell -NoProfile -ExecutionPolicy Bypass -File $exporter $SavePath
$parsed = $json | ConvertFrom-Json

Assert-True ($parsed.bridge_metadata.schema_name -eq "mekhq-read-only-checkpoint") "Unexpected schema name."
Assert-True ($parsed.bridge_metadata.read_only -eq $true) "Exporter must remain read-only."
Assert-True (-not [string]::IsNullOrWhiteSpace($parsed.campaign.location.current_location.display_name)) "Location display_name is missing."
Assert-True (-not [string]::IsNullOrWhiteSpace($parsed.campaign.location.current_location.system_id)) "Location system_id is missing."
Assert-True ($parsed.campaign.location.current_location.PSObject.Properties.Name -contains "is_in_transit") "Location transit flag is missing."
Assert-True ($parsed.campaign.location.current_location.source_owner -like "*Campaign#getCurrentLocation*") "Location source owner is missing."
Assert-True ($parsed.contracts.Count -ge 1) "Expected at least one contract/mission."
Assert-True ($parsed.contracts[0].terms.source_owner -eq "Contract getters") "Contract terms should be method-backed through Contract getters."
Assert-True ($parsed.contracts[0].terms.PSObject.Properties.Name -contains "salvage_pct") "Contract salvage_pct is missing."
Assert-True ($parsed.contracts[0].terms.PSObject.Properties.Name -contains "transport_comp_pct") "Contract transport_comp_pct is missing."
Assert-True ($parsed.contracts[0].terms.PSObject.Properties.Name -contains "monthly_payout") "Contract monthly_payout is missing."
Assert-True ($parsed.markets.unit_offers.Count -ge 1) "Expected unit-market offers for selector warning check."
Assert-True ($parsed.markets.unit_offers[0].stable_selector_available -eq $false) "Unit-market selector should remain unavailable."
$stableOfferBlocks = @($parsed.unsupported | Where-Object { $_.area -eq "markets.unit_offers" -and $_.field -eq "stable_offer_id" -and $_.blocks_automation -eq $true })
$writeCommandBlocks = @($parsed.unsupported | Where-Object { $_.area -eq "write_commands" -and $_.blocks_automation -eq $true })
Assert-True ($stableOfferBlocks.Count -ge 1) "Stable-offer unsupported entry is missing."
Assert-True ($writeCommandBlocks.Count -ge 1) "Write-command unsupported entry is missing."

Write-Output "MekHQ checkpoint exporter smoke test passed."
