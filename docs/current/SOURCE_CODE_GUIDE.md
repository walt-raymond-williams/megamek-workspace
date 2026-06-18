# Source Code Guide

This workspace relies heavily on source inspection. The source checkouts are not decoration; they are how agents should answer "how does this actually work?"

## Local Source Locations

```text
C:\Users\waltr\Documents\megamek-workspace\external\src\megamek
C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq
C:\Users\waltr\Documents\megamek-workspace\external\src\megameklab
C:\Users\waltr\Documents\megamek-workspace\external\src\mm-data
```

Installed suite for runtime comparison:

```text
C:\Users\waltr\Documents\megamek-workspace\external\installs\MekHQ-0.51.00
```

## How To Use Source

Use source when answering:

- what a campaign XML field means
- where saves are loaded or written
- how a campaign action is triggered from the UI
- how MekHQ launches MegaMek scenarios
- how contracts, finances, repairs, personnel, markets, and StratCon systems calculate outcomes
- whether command-line, import/export, or automation hooks exist
- what source change would be needed to expose better campaign control

## Search Patterns

Start broad, then narrow:

```powershell
rg "cpnx" C:\Users\waltr\Documents\megamek-workspace\external\src
rg "Campaign" C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq
rg "save" C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq
rg "Scenario" C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq
rg "launch" C:\Users\waltr\Documents\megamek-workspace\external\src\mekhq
rg "MULParser|MtfFile|BLKFile|ScenarioLoader" C:\Users\waltr\Documents\megamek-workspace\external\src\megamek
```

When a code path is important, record the class/file and the conclusion in the relevant `docs/current/` note.

## Modification Posture

If the user asks to modify MegaMek/MekHQ:

1. Work in the appropriate source repo under `C:\Users\waltr\Documents\megamek-workspace\external\src`.
2. Check `git status --short --branch` before edits.
3. Keep this workspace for notes, plans, campaign reports, and durable discoveries.
4. Verify with the source repo's own build/test workflow.
5. If the change affects campaign control, update this workspace's current docs.
