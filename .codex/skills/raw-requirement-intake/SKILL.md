---
name: raw-requirement-intake
description: Use when the user provides a raw requirement instead of a tracked ticket id. This skill classifies the request using Harness intake rules, recommends direct work or ticket-driven flow, and should usually propose a tracked ticket for non-trivial work before any phase-specific execution starts.
---

# Raw Requirement Intake

Use this Codex adapter before ticket-phase work when the input is a raw
high-level requirement rather than an existing ticket such as `TASK-123`.

## Required Behavior

- Read `docs/FEATURE_INTAKE.md`, `docs/AGENT_PROTOCOL.md`, and
  `docs/PROJECT_PROFILE.md` first.
- Inspect relevant repo code and docs before classifying the requirement.
- Ask follow-up questions when the requirement contains concepts, statements, or
  business rules that are unclear and cannot be confirmed from the docs or
  codebase.
- For each follow-up question, include suggested answers the user can pick from
  or refine so clarification is faster and less error-prone.
- Keep asking until the remaining ambiguity is small enough to choose a safe
  `Type`, `Lane`, and recommendation.
- Determine input type, likely ticket `Type`, and likely `Lane`.
- Recommend `ticket-driven` flow when the work is non-trivial, multi-step,
  risky, unclear, or needs approval history.
- Recommend direct work only for clearly bounded `tiny` changes.
- If ticket-driven flow is recommended, produce:
  - a proposed ticket id
  - a concise ticket draft shape
  - the recommended next phase after the ticket is created
- Do not jump into implementation or phase-specific work from a raw
  requirement unless the request is clearly tiny and low-risk.
