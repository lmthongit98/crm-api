# Workflow Triggers

Harness supports explicit workflow triggers for raw prompts.

The machine-readable source of truth is:

- `.harness/workflow-triggers.yml`

## Trigger Forms

Supported explicit triggers are:

- `$freestyle`
- `$feature`
- `$bugfix`
- `$review`
- `$workflow`

## What They Mean

### `$freestyle`

Bias the request toward `direct-work`.

This is still not permission to ignore promotion rules.

### `$feature`

Bias the request toward `tracked-feature`.

### `$bugfix`

Bias the request toward `tracked-bug-fix`.

### `$review`

Bias the request toward `tracked-review`.

### `$workflow`

Force tracked execution while leaving workflow-type selection to the adapter
after repo inspection.

## Safety Rule

These triggers are strong routing hints, but they do not suppress:

- `needs-clarification`
- high-risk escalation
- approval gates
- forced tracked promotion
