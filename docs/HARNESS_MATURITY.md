# Harness Maturity

This document provides a lightweight maturity rubric for evaluating whether a
repository has adopted the durable Harness flow.

| Level | Meaning | Minimum evidence |
| --- | --- | --- |
| H0 | No Harness | No stable docs, templates, or durable commands exist. |
| H1 | Portable policy installed | `AGENTS.md`, `docs/AGENT_PROTOCOL.md`, `docs/FEATURE_INTAKE.md`, `docs/ARCHITECTURE.md`, and `docs/templates/*` exist. |
| H2 | Durable layer installed | `scripts/harness`, `scripts/schema/*`, `docs/TRACE_SPEC.md`, and `docs/CONTEXT_RULES.md` exist. |
| H3 | Ticket workflow active | `docs/tickets/*`, `docs/work/*`, `docs/TEST_MATRIX.md`, and `scripts/harness ticket ...` are in active use. |
| H4 | Adapter workflow aligned | `.codex/skills/*` and optional `.codex/agents/*` match `docs/HARNESS_SKILLS.md`. |
| H5 | Operationally maintained | Ticket verification, doctor audits, trace records, and Harness doc improvements are used consistently. |

Use this rubric to reason about adoption gaps, not as a substitute for actual
validation.
