# Review Workflow

Tracked tickets now require a durable review artifact before
`code_review_approved`.

The machine-readable source of truth is:

- `.harness/workflow-definition.yml` for the shared tracked runtime
- `.harness/review-workflow.yml`

## Entry Point

The shared tracked runtime in `.harness/workflow-definition.yml` defines when
review becomes the next durable phase.

At that point, the next durable review artifact should be:

```text
docs/work/<ticketId>-*/review.md
```

## Review Artifact

Use `docs/templates/ticket-work/review.md`.

The review artifact should capture:

- scope summary
- findings
- approval recommendation
- residual risks

## Runtime Behavior

`scripts/harness ticket start-phase --id <ticketId> --phase review` now
scaffolds `review.md`.

`scripts/harness ticket approve --id <ticketId> --gate code_review` now
requires a durable `review.md` for the same ticket before approval succeeds.

## Current MVP Boundary

Review uses the shared tracked ticket status machine defined in
`.harness/workflow-definition.yml`.

It becomes durable through:

- a scaffoldable `review` phase
- a required `review.md` artifact before code review approval
