# Workflow Selection

Harness separates workflow routing into three decisions:

1. `engagement_mode`
2. `workflow_type`
3. `lane`

The machine-readable source of truth for this selection is:

- `.harness/workflow-selection.yml`

For raw requests that do not already start from a tracked ticket, use
`.harness/raw-prompt-router.yml`, `.harness/workflow-triggers.yml`, and
`.harness/freestyle-promotion.yml` first, and enter workflow selection only
after the router returns a tracked route.

## Why This Exists

Without an explicit selection layer, agents tend to collapse every non-trivial
prompt into the same ticket flow and treat lane choice as if it also decided
workflow type. That is too rigid for repo questions, exploratory debugging, bug
fixes, investigations, and review-only requests.

This file explains the selection model. The YAML file above is the machine-
readable contract that adapters and future runtime checks should consume.

## Selection Order

```text
request
  -> raw prompt router when no tracked ticket exists
  -> engagement_mode
  -> workflow_type
  -> lane
```

### `engagement_mode`

- `freestyle`: no tracked ticket is required yet
- `tracked`: durable ticket and workflow state are required

If `freestyle` is selected, use `docs/FREESTYLE_MODE.md` for the operating
rules of that path.

### `workflow_type`

Current tracked workflow labels are:

- `feature`
- `bug-fix`
- `investigation`
- `review`

### `lane`

Lane still answers a different question: how much ceremony or risk handling the
selected workflow needs.

## Current MVP Status

`feature` and `bug-fix` currently have active portable workflow contracts.

`investigation` and `review` are still included as machine-readable selection
targets so adapters and future runtime work can route explicitly without
pretending those dedicated workflow contracts already exist.

Tracked `bug-fix` currently shares the durable ticket phase order used by
feature workflow, but it adds bug-oriented artifact and gate expectations
through `docs/BUG_FIX_WORKFLOW.md` and `.harness/bug-fix-workflow.yml`.
