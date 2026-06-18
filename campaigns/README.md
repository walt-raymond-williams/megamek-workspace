# Campaigns

Use this directory for campaign-specific working notes.

Suggested layout:

```text
campaigns/
  raw/              # ignored local copies of actual save files
  <campaign-name>/
    notes.md
    reports/
    decisions.md
```

Raw `.cpnx` and `.cpnx.gz` files are ignored by default. Add them to git only when the user explicitly asks for a campaign snapshot to be versioned.

## Demo Save

`campaigns/demo/ai-ready-demo.cpnx.gz` is the intentional versioned demo campaign save for AI-ready workflow examples. It was copied from the installed MekHQ `0.51.00` sample campaign `The Learning Ropes.cpnx.gz` and can be used for save-file investigation, campaign reporting, parser examples, and requirements-discovery demonstrations.

Keep other raw campaign saves ignored unless they are deliberately promoted to demo fixtures.
