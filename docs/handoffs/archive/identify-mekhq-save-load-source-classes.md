# Agent Handoff: Identify MekHQ Save/Load Source Classes

## Issue

- GitHub issue: `#3`
- Roadmap entry: `Identify MekHQ save/load source classes`
- Priority: `Medium`

## Goal

Confirm how `.cpnx.gz` campaign files are loaded and saved in the local MekHQ source checkout.

## Result

Completed on `2026-06-22`.

Source inspection confirmed:

- `mekhq.campaign.CampaignFactory#createCampaign(InputStream)` is the load entry point. It wraps non-markable streams, reads the first two bytes, uses `GZIPInputStream` only for gzip magic bytes `0x1f 0x8b`, and otherwise treats the stream as XML.
- `mekhq.campaign.io.CampaignXmlParser#parse()` owns DOM parsing, root version reading, milestone upgrade-path handling, null-entity checks, and top-level campaign node processing.
- `mekhq.gui.CampaignGUI.saveCampaign(...)` is the manual save implementation. It appends `.cpnx` if no campaign extension is present and writes gzip only for paths ending in `.gz`.
- `mekhq.gui.FileDialogs` and `mekhq.io.FileType` own dialog extension behavior; `FileType.CPNX` accepts `cpnx`, `cpnx.gz`, and `xml`.
- `mekhq.service.AutosaveService` always writes autosaves through `GZIPOutputStream` as `Autosave-<n>-<campaign>-<date>.cpnx.gz`.
- `mekhq.campaign.Campaign#writeToXML(PrintWriter, boolean)` writes the root `campaign` XML and major campaign sections.

Documentation updated:

- `docs/current/SAVE_FORMAT_NOTES.md`
- `docs/current/SOURCE_CODE_GUIDE.md`
- `docs/current/KNOWN_COMMANDS.md`
- `docs/current/ROADMAP.md`
- `docs/current/TASKS.md`

## Verification

- Source repo `external/src/mekhq` was clean before inspection.
- No source checkout files were modified.
- Workspace documentation passed `git diff --check`.

## Remaining Notes

Raw XML parsing is a valid read-only fallback because the loader treats non-gzip campaign streams as XML, but derived campaign facts should still prefer source-backed methods where available.
