---
name: raw-requirement-intake
description: Use when the user provides a raw requirement instead of a tracked ticket id. This skill acts as a workflow selector using Harness intake, routing, promotion, and workflow-selection rules, and returns `needs-clarification`, `direct-work`, or a tracked workflow recommendation before any phase-specific execution starts.
---

# Raw Requirement Intake

Use this Codex adapter before ticket-phase work when the input is a raw
high-level requirement rather than an existing ticket such as `TASK-123`.

## Required Behavior

- Read `docs/FEATURE_INTAKE.md`, `docs/RAW_PROMPT_ROUTER.md`,
  `docs/FREESTYLE_PROMOTION.md`, `docs/FREESTYLE_MODE.md`,
  `docs/WORKFLOW_TRIGGERS.md`,
  `docs/WORKFLOW_SELECTION.md`,
  `docs/AGENT_PROTOCOL.md`, and `docs/PROJECT_PROFILE.md` first.
- Inspect relevant repo code and docs before classifying the requirement.
- Ask follow-up questions when the requirement contains concepts, statements, or
  business rules that are unclear and cannot be confirmed from the docs or
  codebase.
- For each follow-up question, include suggested answers the user can pick from
  or refine so clarification is faster and less error-prone.
- Keep asking until the remaining ambiguity is small enough to choose a safe
  router outcome.
- Apply `.harness/raw-prompt-router.yml`,
  `.harness/workflow-triggers.yml`,
  `.harness/freestyle-promotion.yml`, and
  `.harness/workflow-selection.yml` in that order.
- Treat explicit trigger conventions such as `$freestyle`, `$feature`,
  `$bugfix`, `$review`, and `$workflow` as strong routing signals, but do not
  let them suppress clarification or forced tracked promotion.
- If `route` is `direct-work`, use `.harness/freestyle-mode.yml` as the
  operating contract for the next step.
- Determine:
  - input type
  - router outcome
  - engagement mode
  - workflow type when tracked
  - likely ticket `Type` and likely `Lane` when tracked
- Return exactly one router outcome:
  - `needs-clarification`
  - `direct-work`
  - `tracked-feature`
  - `tracked-bug-fix`
  - `tracked-investigation`
  - `tracked-review`
- Recommend `direct-work` only when `freestyle` is still legal under the
  promotion contract.
- If a tracked route is recommended, produce:
  - a proposed ticket id
  - a concise ticket draft shape
  - the recommended next phase after the ticket is created
- If `needs-clarification` is returned, stop and ask only the questions needed
  to reach a safe router outcome before recommending tracked work or
  `direct-work`.
- Do not jump into implementation or phase-specific work from a raw
  requirement unless the request is clearly tiny and low-risk.

## Output Contract

Always report:

- `route`
- `rationale_codes`
- `repo_context_summary`
- `open_questions`
- `recommended_next_step`

When `route` is `direct-work`, also report:

- why `freestyle` is still legal
- the narrow bounded change or question to handle next

When `route` is a tracked route, also report:

- `engagement_mode: tracked`
- `workflow_type`
- likely ticket `Type`
- likely ticket `Lane`
- `ticket_recommendation`
- optional `ticket_draft`

If `route` is `tracked-bug-fix`, point the next tracked step at
`docs/BUG_FIX_WORKFLOW.md` and the preferred templates under
`docs/templates/bug-fix-work/`.
