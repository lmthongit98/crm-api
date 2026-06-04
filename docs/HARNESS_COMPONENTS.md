# Harness Components

This file maps the major Harness surfaces to their responsibilities.

| Component | Main files | Responsibility |
| --- | --- | --- |
| Workflow policy | `docs/AGENT_PROTOCOL.md`, `docs/FEATURE_INTAKE.md`, `docs/RAW_PROMPT_ROUTER.md`, `docs/FREESTYLE_PROMOTION.md`, `docs/FREESTYLE_MODE.md`, `docs/WORKFLOW_SELECTION.md`, `.harness/raw-prompt-router.yml`, `.harness/freestyle-promotion.yml`, `.harness/freestyle-mode.yml`, `.harness/workflow-selection.yml` | Defines raw prompt routing, freestyle promotion, freestyle operating rules, workflow selection, ticket workflow, risk lanes, gate policy, and source-of-truth hierarchy. |
| Project context | `docs/PROJECT_PROFILE.md`, `docs/ARCHITECTURE.md`, `docs/product/*` | Explains repo-specific stack, boundaries, and current product contract. |
| Work records | `docs/tickets/*`, `docs/work/*`, `docs/decisions/*` | Stores requirements, work packets, validation evidence, and durable decisions. |
| Durable layer | `scripts/harness`, `scripts/schema/*`, `harness.db` | Stores and queries operational state as a local cache. |
| Adapters | `docs/HARNESS_SKILLS.md`, `.codex/skills/*`, `.codex/agents/*`, `docs/agent-adapters/README.md` | Maps the portable workflow onto specific agent tooling. |
| Observability | decisions, work packets, validation evidence, and current Harness docs | Preserves execution evidence and process gaps for later improvement. |
