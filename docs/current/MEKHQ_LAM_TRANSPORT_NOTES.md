# MekHQ LAM Transport Notes

This note records source-backed behavior for Land-Air Mek transport and starting mode questions in MekHQ/MegaMek.

## Findings

- `Confirmed from source`: MegaMek models a Land-Air Mek as a `LandAirMek` that extends `BipedMek`, not `Aero`. It implements aero behavior separately; `LandAirMek.isAero()` returns true only when the conversion mode is `CONV_MODE_FIGHTER`. See `external/src/megamek/megamek/src/megamek/common/units/LandAirMek.java` and `external/src/megamek/megamek/unittests/megamek/common/units/LandAirMekTest.java`.
- `Confirmed from source`: An `ASFBay` can load a normal fighter or a `LandAirMek` whose conversion mode is `CONV_MODE_FIGHTER`. A `SmallCraftBay` has the same special-case support for fighter-mode LAMs. See `external/src/megamek/megamek/src/megamek/common/bays/ASFBay.java` and `external/src/megamek/megamek/src/megamek/common/bays/SmallCraftBay.java`.
- `Confirmed from source`: A `MekBay` can load a `LandAirMek` only when its conversion mode is `CONV_MODE_MEK`. See `external/src/megamek/megamek/src/megamek/common/bays/MekBay.java`.
- `Confirmed from source`: MekHQ's ship-transport assignment menu checks `transport.getEntity().canLoad(unit.getEntity(), false)` before assignment, so current conversion mode matters when assigning a LAM to a carrier. See `external/src/mekhq/MekHQ/src/mekhq/gui/menus/AssignForceToShipTransportMenu.java`.
- `Confirmed from source`: MekHQ's campaign transport cost calculation treats LAMs flexibly: spare ASF capacity can absorb LAMs before remaining LAMs count against Mek bay demand. See `external/src/mekhq/MekHQ/src/mekhq/campaign/mission/TransportCostCalculations.java`.
- `Confirmed from source`: MegaMek's lobby custom unit dialog has a Starting Mode control for LAMs with Biped/AirMek/Fighter choices, and enables starting altitude/velocity fields for fighter mode. It disables conversion-mode changes for loaded units to avoid mismatched bay state. See `external/src/megamek/megamek/src/megamek/client/ui/dialogs/customMek/CustomMekDialog.java`.
- `Confirmed from source`: MekHQ AtB dynamic scenario generation forces LAMs into fighter mode for space or atmosphere board types, and into Mek mode otherwise. See `external/src/mekhq/MekHQ/src/mekhq/campaign/mission/AtBDynamicScenarioFactory.java`.
- `Confirmed from source/data`: The `Leopard CV (2581)` and `Leopard CV (3054)` data files each define three `asfbay:2.0` transporters, i.e. six ASF cubicles, and no Mek bays. See `external/src/mm-data/data/mekfiles/dropships/TRO3057R/IS/Leopard CV (2581).blk` and `external/src/mm-data/data/mekfiles/dropships/TRO3057R/IS/Leopard CV (3054).blk`.

## Practical Implications

- A LAM can fit in an aerospace fighter bay only when it is in fighter mode.
- A LAM can fit in a Mek bay only when it is in Mek mode.
- A Leopard CV can carry a fighter-mode LAM in its ASF bays, but it cannot carry a Mek-mode LAM because the CV has no Mek bays.
- In a MegaMek scenario lobby, set the LAM's starting mode before loading it into a carrier bay. Once loaded, the custom deployment UI disables starting-mode changes.
- In MekHQ campaign assignment UI, expect the current conversion mode to affect whether the Leopard CV is accepted as a ship transport. If the UI only presents or accepts Mek-bay assignment for a default Mek-mode LAM, use a manual scenario/lobby setup or a controlled GM workaround rather than assuming the stock campaign assignment UI will make the fighter-mode bay choice for you.
