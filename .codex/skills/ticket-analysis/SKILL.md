---
name: ticket-analysis
description: Use when working in the Harness ticket workflow and the requested phase is analysis for a ticket such as TASK-123. This skill loads the ticket, required Harness docs, relevant product and work context, and current code shape, then writes or updates analysis.md only.
---

# Ticket Analysis

Use this Codex adapter only for the analysis phase of a ticket-driven Harness
task in the current repo.

## Required Inputs

- `ticket_id`
- `goal=analysis`

## Required Commands

- `scripts/harness ticket load --id <ticketId>`
- `scripts/harness ticket next --id <ticketId>`
- `scripts/harness ticket check-phase --id <ticketId>`
- `scripts/harness ticket status --id <ticketId>`
- `scripts/harness query tickets`

## Output Contract

Report:

- `artifacts produced`
- `gates checked`
- `commands run`
- `blocking issues`
- `next allowed phase`

If important concepts, constraints, or expected outcomes are not clear after
reading the ticket, code, and docs, stop and ask the user follow-up questions
instead of drafting a speculative analysis.

For each follow-up question, include suggested answers the user can pick from
or refine so clarification stays concrete and the analysis can resume quickly.

If the ticket spans more than one independently reviewable API, domain/data
slice, or workflow and would likely exceed 5 working days, stop before proposal
or plan work and route to `ticket-decomposition`.

Write or update only `docs/work/<ticketId>-*/analysis.md`.
