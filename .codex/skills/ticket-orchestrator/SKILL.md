---
name: ticket-orchestrator
description: Use when working in the Harness ticket workflow and the user has not specified the phase for an existing ticket such as TASK-123. This skill asks the runtime for the next legal phase, identifies blockers, and routes execution to the correct phase skill without re-implementing workflow policy.
---

# Ticket Orchestrator

Use this Codex adapter to decide the next legal Harness phase for an existing
tracked ticket before any phase-specific skill runs. For raw requirements, use
`raw-requirement-intake` first.

## Required Commands

- `scripts/harness ticket load --id <ticketId>`
- `scripts/harness ticket next --id <ticketId>`
- `scripts/harness ticket check-phase --id <ticketId>`
- `scripts/harness ticket status --id <ticketId>`

## Required Behavior

- Trust runtime guidance instead of inferring the next phase from docs alone.
- If blocked, report the exact gate, missing artifact, or repair action.
- Route to one of: `ticket-analysis`, `ticket-decomposition`,
  `ticket-proposal-plan`, `ticket-implementation`, `ticket-code-review`, or
  `ticket-robot-uat`.
- Do not run `scripts/harness ticket approve`.
- Do not write artifacts directly unless the routed phase owns them.

If the runtime reports `status=decomposed`, route the user to child-ticket work
instead of parent planning or implementation.
