# Freestyle Promotion

Harness permits `freestyle` work, but not by default for every raw request.

The machine-readable source of truth for promotion criteria is:

- `.harness/freestyle-promotion.yml`

## Why This Exists

Without explicit promotion criteria, agents tend to do one of two bad things:

- over-ticket small repo questions and narrow edits
- under-track changes that clearly need durable workflow state

This file defines when a request must leave `freestyle` and become tracked
work.

## Promotion Rules

Promote immediately when any strong trigger appears:

- explicit tracked or workflow override
- approval history needed
- durable handoff needed
- clearly multi-step execution
- high-risk surface

Promote when multiple moderate signals appear together:

- validation-heavy change
- broad change scope
- unclear impact on existing behavior
- cross-surface change

## Stay In `freestyle` Only When

All of these are true:

- repo context was inspected
- the request is clear enough
- no strong tracked trigger is present
- the work fits one bounded session
- the scope is narrow or question-shaped

## Clarification Comes First

If the request is still too unclear to classify safely, stop and ask questions
before deciding `direct-work` or tracked workflow.

If `direct-work` remains legal after this check, use `docs/FREESTYLE_MODE.md`
as the operating contract for the actual lightweight execution.
