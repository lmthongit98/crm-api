# GitHub Copilot Adapter

This file explains the planned GitHub Copilot adapter behavior inside a
Harness-installed repository.

It is adapter guidance only. Shared workflow truth stays in
`docs/AGENT_PROTOCOL.md`, `.harness/*.yml`, and `scripts/harness`.

## What This Overlay Adds

The Copilot overlay adds:

- `.github/copilot-instructions.md` for repo-local Copilot operating guidance
- this adapter reference for repo operators and maintainers

It does not add separate workflow policy, parallel ticket templates, or
Copilot-specific routing rules.

## Operating Model

Copilot should:

- inspect the shared Harness docs before starting work
- route raw requests through the shared intake and workflow-selection contract
- use `scripts/harness` for tracked-ticket runtime guidance, gate checks, and
  verification
- write the same shared Harness artifacts as any other adapter
- stop at the same explicit approval gates as any other adapter

## Raw Intake And Routing Truth

Copilot must reuse the same routing sources as every other Harness adapter.

For raw requests without a tracked ticket id, the routing truth is:

- `.harness/raw-prompt-router.yml` and `docs/RAW_PROMPT_ROUTER.md`
- `.harness/freestyle-promotion.yml` and `docs/FREESTYLE_PROMOTION.md`
- `.harness/workflow-triggers.yml` and `docs/WORKFLOW_TRIGGERS.md`
- `.harness/workflow-selection.yml` and `docs/WORKFLOW_SELECTION.md`

Routing order remains:

```text
raw request
  -> raw prompt router
  -> engagement_mode
  -> workflow_type
  -> lane
```

Copilot must inherit the shared route outcomes:

- `needs-clarification`
- `direct-work`
- `tracked-feature`
- `tracked-bug-fix`
- `tracked-investigation`
- `tracked-review`

Copilot must not add adapter-local route labels, alternate promotion rules, or
tool-specific workflow-selection branches.

## Required Shared Commands

For tracked work, the adapter should rely on:

```bash
scripts/harness ticket next --id <ticketId>
scripts/harness ticket check-phase --id <ticketId>
scripts/harness ticket status --id <ticketId>
scripts/harness ticket start-phase --id <ticketId> --phase <phase>
scripts/harness ticket gate --id <ticketId> --gate <gate>
scripts/harness verify ticket --id <ticketId>
```

If the local operational cache is missing or stale:

```bash
scripts/harness init
scripts/harness migrate
scripts/harness import tracked
scripts/harness doctor
```

## Phase Execution Map

Copilot phase execution should map onto the existing Harness artifacts exactly:

| Shared phase | Required output |
| --- | --- |
| Analysis | `analysis.md` |
| Proposal | `proposal.md` |
| Planning | `implementation-plan.md` |
| Implementation | approved product code, tests, product docs, and immediate validation notes |
| Review | `review.md` |
| Validation | proof and `validation.md` |
| Optional UAT | optional `uat.md` and any accepted test assets |

Rules:

- Copilot must not invent alternate artifact names for the same phase.
- Copilot must not merge multiple tracked phases into one adapter-specific
  output.
- `scripts/harness ticket start-phase` should remain the preferred way to
  scaffold the next legal artifact.
- Review remains a durable artifact phase before `code_review_approved`.

For tracked bug-fix work, keep this same artifact map while satisfying the
additional expectations in `docs/BUG_FIX_WORKFLOW.md`.

For high-risk lanes, keep the same shared runtime and approval gates. Use the
repo-required high-risk artifact set when the lane guidance calls for
`overview.md`, `design.md`, `execplan.md`, and `validation.md`.

## What Still Stays Shared

The following must remain agent-neutral:

- route labels such as `needs-clarification`, `direct-work`, and tracked routes
- workflow selection and lane rules
- tracked phase order and gate policy
- artifact names and ownership
- live-data and database-check policy
- human approval requirements

If Copilot needs new workflow behavior, add it to the shared Harness contract
first instead of introducing a Copilot-only rule here.

The same rule applies to intake and routing. If Copilot needs different route
semantics, change the shared `.harness/*.yml` contracts first instead of
teaching the adapter a parallel routing model.

## MVP Limits

The first Copilot adapter is intentionally thin.

It may require the operator or agent to:

- choose the right Copilot entry surface manually
- issue `scripts/harness` commands directly
- copy the next phase context into the active Copilot conversation

That limitation is acceptable as long as the overlay keeps Copilot aligned with
the same shared Harness workflow used by other adapters.
