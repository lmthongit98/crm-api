# Implementation Plan

## Ticket

- ID: TASK-001
- Status: `plan_approved`

Update this field as the ticket advances. It starts at
`plan_pending_approval`, then should be kept in sync with the current ticket
workflow state instead of remaining frozen once implementation is complete.

## Approved Proposal

- Link or summarize the approved proposal.

## Implementation Steps

1. Update product contract or docs.
2. Change application code.
3. Add or update tests.
4. Run validation.
5. Update Harness evidence.

## Interfaces And Data Flow

- API or request and response changes:
- Service flow:
- Persistence changes:
- Migration or backfill:
- Error handling:
- Compatibility:
- Rollout or rollback:
- Observability or logging:

## Test Plan

- Unit:
- Integration:
- Manual:
- Optional UAT candidates:
- Performance or SLA:
- Security or privacy:

## Approval

Plan approval must be recorded by a human before changing application code.
Agents should check the gate with:

```bash
scripts/harness ticket gate --id <ticketId> --gate plan
```

Only run this approval command after explicit human approval:

```bash
scripts/harness ticket approve --id <ticketId> --gate plan
```

## Outcome

- Record what happened after the plan was written.
