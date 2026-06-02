---
name: ticket-decomposition
description: Use when a Harness ticket such as TASK-123 is too broad for one safe review and must be split into child tickets. This skill decides whether to decompose, drafts child tickets with narrow reviewable scope, updates parent-child routing expectations, and stops the parent ticket in decomposed state instead of continuing planning or implementation.
---

# Ticket Decomposition

Use this Codex adapter when analysis or proposal work shows that one ticket is
too broad for one safe review in the current project's Harness workflow.

## Required Inputs

- `ticket_id`
- `goal=decomposition`

## Required Commands

- `scripts/harness ticket load --id <ticketId>`
- `scripts/harness ticket next --id <ticketId>`
- `scripts/harness ticket check-phase --id <ticketId>`
- `scripts/harness ticket status --id <ticketId>`
- `scripts/harness query tickets`

## Required Behavior

- Read `docs/tickets/<ticketId>.md` and any existing `analysis.md` or
  `proposal.md` before deciding.
- Decompose only when the ticket would exceed one safe review.
- Prefer each child ticket to cover one reviewable slice:
  - one REST API, or
  - one database table or one domain entity, or
  - one tightly bounded workflow slice
- Keep each child ticket to an estimate no greater than 5 working days.
- If a proposed child still spans multiple independent APIs, entities, or
  workflows, split it again before finalizing the decomposition.
- Update the parent ticket metadata to `Relationship: parent`.
- Create child tickets with `Relationship: child` and `Parent ticket:
  <ticketId>`.
- Update the parent `proposal.md` decomposition routing section with child ids
  and sequencing notes.
- Move the parent ticket to `decomposed` with:
  `scripts/harness ticket update --id <ticketId> --status decomposed`.
- Do not write `implementation-plan.md`, product code, review output, or UAT
  artifacts for the parent ticket.
- Do not run `scripts/harness ticket approve`.

## Output Contract

Report:

- `artifacts produced`
- `gates checked`
- `commands run`
- `blocking issues`
- `next allowed phase`

Also include:

- `decomposition decision`
- `child tickets drafted`
- `scope rule used`
- `dependency order`
