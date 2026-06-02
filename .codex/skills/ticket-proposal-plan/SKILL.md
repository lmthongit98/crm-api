---
name: ticket-proposal-plan
description: Use when working in the Harness ticket workflow and the requested phase is proposal or implementation-plan creation for a ticket such as TASK-123. This skill owns proposal.md and implementation-plan.md, enforces proposal and plan gates, and must refuse implementation, review, validation, or UAT work.
---

# Ticket Proposal Plan

Use this Codex adapter for either proposal creation or implementation-plan
creation in the current project's ticket workflow.

## Required Inputs

- `ticket_id`
- `goal=proposal` or `goal=implementation-plan`

## Required Commands

- `scripts/harness ticket next --id <ticketId>`
- `scripts/harness ticket status --id <ticketId>`
- `scripts/harness ticket start-phase --id <ticketId> --phase proposal` in proposal mode
- `scripts/harness ticket start-phase --id <ticketId> --phase implementation-plan` in plan mode
- `scripts/harness ticket gate --id <ticketId> --gate proposal` in plan mode
- `scripts/harness ticket update --id <ticketId> --status proposal_pending_approval`
- `scripts/harness ticket update --id <ticketId> --status plan_pending_approval`

Do not run `scripts/harness ticket approve`; only a human-approved action should
mutate approval gates.

If proposal work shows the ticket should become a parent ticket because one
approved slice would still cover multiple independent APIs, entities, or
workflows or would likely exceed 5 working days, stop and hand off to
`ticket-decomposition` instead of writing an implementation plan for the
parent.
