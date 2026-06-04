# Bug-Fix Workflow

Use this workflow when a tracked request is primarily about correcting existing
behavior rather than adding new product capability.

The machine-readable source of truth is:

- `.harness/workflow-definition.yml` for the shared tracked runtime
- `.harness/bug-fix-workflow.yml`

## When To Use It

Choose tracked `bug-fix` work when the goal is to:

- restore expected behavior
- correct a regression
- fix a production defect or test-proven failure
- repair a contract mismatch caused by a defect

Do not use this workflow for net-new capability that should be modeled as a
feature instead.

## Shared Runtime Skeleton

Tracked bug-fix work uses the shared tracked runtime defined in
`.harness/workflow-definition.yml`.

That shared definition remains authoritative for:

- durable phase order
- approval gates
- lane shortcuts
- shared artifact timing

The workflow difference is in the bug-oriented content and review focus, not
in a separate status machine.

## Required Artifact Focus

### `analysis.md`

Include:

- reproduction context
- expected behavior
- actual behavior
- impact scope
- suspected root cause or the key remaining root-cause question
- regression scope

### `proposal.md`

Include:

- the corrective outcome
- the chosen fix approach
- behavior guardrails and non-goals
- rollback or containment path
- validation targets

### `implementation-plan.md`

Include:

- before-and-after proof strategy
- code and data changes
- tests and regression checks
- observability and rollback notes

### `validation.md`

Include:

- reproduction steps
- results
- regression checks
- remaining risk

## Approval Focus

The shared gate names and gate-to-status mapping come from
`.harness/workflow-definition.yml`.

At those bug-fix approvals, humans should confirm:

- expected vs actual behavior is described correctly
- the chosen correction is narrow enough
- regression proof covers nearby behavior, not only the happy path

## Lane Guidance

Tracked bug-fix work inherits the shared lane model from
`.harness/workflow-definition.yml`.

The bug-fix-specific default remains `normal-fast`.

Escalate to `high-risk` if the defect touches:

- auth or authorization
- data loss, schema, or migration behavior
- public APIs or client-visible contracts
- external providers
- audit, privacy, or security
- weak proof in the affected area

## Runtime Behavior

`scripts/harness ticket start-phase` is now workflow-aware for tracked
`bugfix` ticket metadata when scaffolding:

- `analysis`
- `proposal`
- `implementation-plan`
- `validation`

Those phases use the preferred bug-fix templates here:

```text
docs/templates/bug-fix-work/
```

The runtime still uses the shared tracked ticket status machine from
`.harness/workflow-definition.yml`; this workflow does not introduce a separate
status engine.
