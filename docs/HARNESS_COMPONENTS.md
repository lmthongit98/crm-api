# Harness Components

This file maps the major Harness surfaces to their responsibilities.

| Component | Main files | Responsibility |
| --- | --- | --- |
| Workflow policy | `docs/AGENT_PROTOCOL.md`, `docs/FEATURE_INTAKE.md` | Defines the ticket workflow, risk lanes, gate policy, and source-of-truth hierarchy. |
| Project context | `docs/PROJECT_PROFILE.md`, `docs/ARCHITECTURE.md`, `docs/product/*` | Explains repo-specific stack, boundaries, and current product contract. |
| Work records | `docs/tickets/*`, `docs/work/*`, `docs/TEST_MATRIX.md`, `docs/decisions/*` | Stores requirements, work packets, proof expectations, and durable decisions. |
| Durable layer | `scripts/harness`, `scripts/schema/*`, `harness.db` | Stores and queries operational state as a local cache. |
| Adapters | `docs/HARNESS_SKILLS.md`, `.codex/skills/*`, `.codex/agents/*`, `docs/agent-adapters/README.md` | Maps the portable workflow onto specific agent tooling. |
| Observability | `docs/TRACE_SPEC.md`, trace records, decisions, and work packets | Preserves execution evidence and friction for later improvement. |
