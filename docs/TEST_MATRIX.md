# Test Matrix

This file maps product behavior to proof expectations. It is also a brownfield
seed for `scripts/harness import brownfield`.

## Status Values

| Status | Meaning |
| --- | --- |
| planned | Accepted as intended behavior, not fully proven |
| in_progress | Actively being built |
| implemented | Implemented and proof exists |
| changed | Contract changed after earlier implementation |
| retired | No longer part of the product contract |

## Matrix

| Work Item | Contract | Unit | Integration | E2E | UAT | Platform | Status | Evidence |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| SAMPLE-001 | Replace this row with current product behavior or the next accepted work item. | no | no | no | no | no | planned | Add current evidence or proof gap notes here. |

## Evidence Rules

- Unit proof covers pure rules, helpers, mappers, validation, and isolated
  service decisions.
- Integration proof covers controller or service contracts, persistence,
  security enforcement, provider adapters, and runtime behavior that crosses a
  real boundary.
- E2E proof is only required when a user-visible or client workflow exists.
- UAT tracks whether acceptance-level tests were generated for a ticket after
  code review approval.
- Platform proof covers runtime configuration, credentials, connectivity,
  deployment, jobs, or environment-specific behavior.
