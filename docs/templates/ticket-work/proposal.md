# Proposal

## Ticket

- ID:
- Status: `proposal_pending_approval`
- Proposal type: `implementation` or `decomposition`

## Outcome

- Describe the intended behavior after the change.

## Proposed Approach

1. Step.
2. Step.
3. Step.

## Decomposition Routing

- Parent or standalone: `standalone`
- Child tickets: `none`
- Next active ticket after approval or decomposition:

## Affected Surfaces

- Product docs:
- Code modules:
- Data or schema:
- External providers:
- Validation:

## Risks And Mitigations

- Risk and mitigation.

## Approval

Proposal approval must be recorded by a human before writing the implementation
plan. Agents should check the gate with:

```bash
scripts/harness ticket gate --id <ticketId> --gate proposal
```

If this proposal decomposes the ticket into child tickets, update the parent
ticket metadata, list the child tickets above, and sync the runtime with:

```bash
scripts/harness ticket update --id <ticketId> --status decomposed
```

Only run this approval command after explicit human approval on an
implementation proposal:

```bash
scripts/harness ticket approve --id <ticketId> --gate proposal
```
