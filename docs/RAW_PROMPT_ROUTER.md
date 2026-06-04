# Raw Prompt Router

Harness uses a lightweight router for requests that do not already start from a
tracked ticket.

The machine-readable source of truth for that decision is:

- `.harness/raw-prompt-router.yml`

The machine-readable promotion criteria used by that router are:

- `.harness/freestyle-promotion.yml`

The machine-readable workflow trigger conventions used by that router are:

- `.harness/workflow-triggers.yml`

The operating contract for work that stays in `freestyle` is:

- `.harness/freestyle-mode.yml`

## Why This Exists

Workflow selection alone is not enough for raw requests. A raw prompt still
needs one earlier decision:

- should the adapter stop for clarification
- should the work stay lightweight
- should the request be promoted into a tracked workflow

This router provides that decision without pretending the repo already has a
full workflow engine for every request type.

The router should apply the explicit promotion criteria before returning
`direct-work` or a tracked route.

When the prompt contains an explicit workflow trigger such as `$freestyle`,
`$feature`, `$bugfix`, `$review`, or `$workflow`, the router should treat that
as a strong routing signal before falling back to ordinary prose inference.

If it returns `direct-work`, the next step should follow
`docs/FREESTYLE_MODE.md`.

## Route Outcomes

The router returns one of:

- `needs-clarification`
- `direct-work`
- `tracked-feature`
- `tracked-bug-fix`
- `tracked-investigation`
- `tracked-review`

## Output Expectations

Adapters should report:

- chosen route
- rationale codes
- short repo-context summary
- open questions when clarification is still required
- recommended next step in Harness terms
- optional ticket recommendation or ticket draft for tracked routes

## Current MVP Boundary

This router standardizes output shape and route labels. Promotion thresholds
come from `docs/FREESTYLE_PROMOTION.md` and
`.harness/freestyle-promotion.yml`.

Explicit trigger conventions come from `docs/WORKFLOW_TRIGGERS.md` and
`.harness/workflow-triggers.yml`.
