---
name: ticket-robot-uat
description: Use when working in the Harness ticket workflow and the requested phase is optional Robot or acceptance UAT generation for a ticket such as TASK-123. This skill requires code review approval, may generate tests under tests/robot/<ticketId>/ plus uat.md, and must refuse work before the code review gate is approved.
---

# Optional Ticket Robot UAT

Use this Codex adapter only for optional UAT generation after code review approval.

## Required Commands

- `scripts/harness ticket next --id <ticketId>`
- `scripts/harness ticket start-phase --id <ticketId> --phase uat`
- `scripts/harness ticket status --id <ticketId>`
- `scripts/harness ticket gate --id <ticketId> --gate code_review`
- `scripts/harness verify ticket --id <ticketId>`
