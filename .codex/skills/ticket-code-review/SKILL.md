---
name: ticket-code-review
description: Use when working in the Harness ticket workflow and the requested phase is code review for a ticket such as TASK-123. This skill performs a review-only pass, validates behavior, regressions, missing tests, and gate compliance, produces findings first, and must not implement fixes by default.
---

# Ticket Code Review

Use this Codex adapter for the review phase of a Harness ticket.

## Required Behavior

- Run `scripts/harness ticket next --id <ticketId>` before review.
- Review-only mindset.
- Findings first, ordered by severity.
- Focus on bugs, regressions, missing proof, and gate violations.
- Do not implement fixes unless explicitly asked in a separate task.
