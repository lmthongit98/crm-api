---
name: ticket-implementation
description: Use when working in the Harness ticket workflow and the requested phase is implementation for a ticket such as TASK-123. This skill requires plan approval, implements only the approved slice, updates work evidence and validation notes, and must refuse optional Robot UAT generation.
---

# Ticket Implementation

Use this Codex adapter only for implementation after plan approval.

## Required Commands

- `scripts/harness ticket next --id <ticketId>`
- `scripts/harness ticket status --id <ticketId>`
- `scripts/harness ticket gate --id <ticketId> --gate plan`
- project validation commands from `README.md`
- `scripts/harness query matrix`

## Stop Rules

Stop and report instead of continuing when:

- plan approval is missing
- implementation would exceed the approved slice
- the user asks for optional Robot UAT generation
