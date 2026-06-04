# Agent Instructions

Add project-specific agent instructions here.

## Harness

This repo uses Harness. Before work, read:

- `docs/README.md`
- `docs/AGENT_PROTOCOL.md`
- `docs/PROJECT_PROFILE.md`
- `docs/HARNESS_SKILLS.md`
- `docs/FEATURE_INTAKE.md`
- `docs/RAW_PROMPT_ROUTER.md`
- `docs/FREESTYLE_PROMOTION.md`
- `docs/FREESTYLE_MODE.md`
- `docs/WORKFLOW_SELECTION.md`
- `docs/ARCHITECTURE.md`
- `docs/CONTEXT_RULES.md`
- if ticket or decision queries fail because `harness.db` is missing, run:
    - `scripts/harness init`
    - `scripts/harness import tracked`
- if the durable cache is still unavailable, use tracked markdown docs as the
  source of truth and continue

Use the stable repo-local entrypoint `scripts/harness` as the main operational
tool. Tracked markdown is the portable source of truth. `harness.db` is a local
cache that can be rebuilt from tracked docs.
