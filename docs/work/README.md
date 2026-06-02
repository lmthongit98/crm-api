# Work Packets

`docs/work/` is the standard home for ticket execution artifacts.

Requirements belong in `docs/tickets/<ticketId>.md`. A work packet contains the
artifacts produced while executing that requirement:

```text
docs/work/<ticketId>-short-title/
  analysis.md
  proposal.md
  implementation-plan.md
  validation.md
  uat.md
```

Work packets are not requirements. They are the analysis, proposal, plan,
validation evidence, and UAT mapping derived from the ticket.

`scripts/harness ticket load --id <ticketId>` registers the durable workflow
row only. Create work-packet files intentionally from
`docs/templates/ticket-work/` when the ticket reaches each stage.
