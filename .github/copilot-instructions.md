# Harness GitHub Copilot Instructions

This repository uses Harness.

Harness workflow truth does not live in this file. Treat this file as a thin
Copilot adapter layer that points back to the shared repo-local Harness
contract.

## Read First

Before starting work, inspect:

- `docs/AGENT_PROTOCOL.md`
- `docs/PROJECT_PROFILE.md`
- `docs/FEATURE_INTAKE.md`
- `docs/RAW_PROMPT_ROUTER.md`
- `docs/WORKFLOW_TRIGGERS.md`
- `docs/WORKFLOW_SELECTION.md`

Read additional docs only when the task needs them:

- `docs/FREESTYLE_PROMOTION.md` and `docs/FREESTYLE_MODE.md` for
  `direct-work`
- `docs/WORKFLOW_DEFINITION.md` and `.harness/workflow-definition.yml` for
  tracked phase rules, gates, artifacts, and legal status transitions
- `docs/BUG_FIX_WORKFLOW.md` and `.harness/bug-fix-workflow.yml` for tracked
  bug-fix work
- `docs/REVIEW_WORKFLOW.md` and `.harness/review-workflow.yml` for review
  artifact or code-review gate behavior
- `docs/ARCHITECTURE.md` for code changes, boundary questions, or risk review
- `docs/CONTEXT_RULES.md` when deciding what else to inspect for the current
  phase or lane
- `docs/agent-adapters/copilot.md` when the task changes the Copilot adapter
  itself

If ticket or decision queries fail because `harness.db` is missing, rebuild the
local cache with:

```bash
scripts/harness init
scripts/harness migrate
scripts/harness import tracked
scripts/harness doctor
```

If the cache is still unavailable, continue from tracked markdown because it is
the portable source of truth.

## Raw Requests

When the user gives a raw requirement instead of a tracked ticket id:

- inspect the repo and shared Harness docs first
- use `.harness/raw-prompt-router.yml` and `docs/RAW_PROMPT_ROUTER.md` for
  route selection
- use `.harness/freestyle-promotion.yml` and `docs/FREESTYLE_PROMOTION.md`
  when deciding whether work must leave `freestyle`
- use `.harness/workflow-triggers.yml` and `docs/WORKFLOW_TRIGGERS.md` when the
  prompt includes explicit triggers such as `$freestyle`, `$feature`,
  `$bugfix`, `$review`, or `$workflow`
- use `.harness/workflow-selection.yml` and `docs/WORKFLOW_SELECTION.md` when
  tracked workflow selection is needed

Ask follow-up questions when important concepts, rules, or scope remain
unclear after repo inspection. Include suggested answers the user can confirm
or refine. Do not invent missing product intent.

### Routing Order

For raw requests, keep the shared Harness routing order:

```text
raw request
  -> `.harness/raw-prompt-router.yml`
  -> `engagement_mode`
  -> `workflow_type`
  -> `lane`
```

Do not skip directly to phase work when the shared router still needs to decide
between clarification, `direct-work`, and tracked execution.

### Shared Route Outcomes

Treat these as the valid shared raw-router outcomes:

- `needs-clarification`
- `direct-work`
- `tracked-feature`
- `tracked-bug-fix`
- `tracked-investigation`
- `tracked-review`

Do not invent Copilot-only route labels or alternate routing branches.

## Tracked Ticket Work

Use the repo-local runtime entrypoint `scripts/harness` as the main operational
surface.

Before advancing tracked work, inspect the runtime guidance:

```bash
scripts/harness ticket next --id <ticketId>
scripts/harness ticket check-phase --id <ticketId>
scripts/harness ticket status --id <ticketId>
```

Use these commands for common tracked actions:

```bash
scripts/harness ticket load --id <ticketId>
scripts/harness ticket start-phase --id <ticketId> --phase <phase>
scripts/harness ticket gate --id <ticketId> --gate proposal
scripts/harness ticket gate --id <ticketId> --gate plan
scripts/harness ticket gate --id <ticketId> --gate code_review
scripts/harness verify ticket --id <ticketId>
```

Do not copy templates by hand when `ticket start-phase` can scaffold the next
legal artifact.

Treat shared Harness artifacts as the required outputs:

- `analysis.md`
- `proposal.md`
- `implementation-plan.md`
- `review.md`
- `validation.md`
- optional `uat.md`

Do not invent Copilot-only artifact names or alternate workflow phases.

### Phase-To-Artifact Map

Use the shared Harness phase ownership directly:

- Analysis
  - writes or updates only `analysis.md`
  - stops for follow-up questions when repo truth is not clear enough
- Proposal
  - writes or updates only `proposal.md`
- Planning
  - writes or updates only `implementation-plan.md`
- Implementation
  - changes approved product code, tests, product docs, and immediate
    validation notes for the approved slice
  - does not create a Copilot-only implementation artifact
- Review
  - writes or updates only `review.md`
  - produces findings first and does not fix code by default
- Validation
  - updates proof and `validation.md`
- Optional UAT
  - may write `uat.md` and any accepted test assets after code review approval

For tracked bug-fix work, keep the same artifact names and shared phase order,
but satisfy the bug-oriented expectations from `docs/BUG_FIX_WORKFLOW.md` and
`.harness/bug-fix-workflow.yml`.

For high-risk tracked work, keep the same runtime and gate sequence. Use the
repo's required high-risk artifact set when lane guidance calls for files such
as `overview.md`, `design.md`, `execplan.md`, and `validation.md`.

## Approvals

Approval gates remain human-controlled.

- Use `scripts/harness ticket gate ...` for read-only gate checks.
- Run `scripts/harness ticket approve ...` only after explicit human approval.
- Do not treat Copilot suggestions, chat confirmation, or tool UI state as a
  substitute for the shared approval rule.

## Live Data And Database Checks

If Copilot has access to live environment tools, follow the shared policy in
`docs/AGENT_PROTOCOL.md`:

- prefer tracked docs, local code, and repo-owned schemas first
- keep checks read-only by default
- use live database inspection only when the current phase needs facts that
  repo-local truth cannot answer reliably
- keep routing-only or orchestration steps out of direct live database access

## Boundaries

This file may explain how Copilot should consume Harness, but it must not:

- replace `docs/AGENT_PROTOCOL.md` or `.harness/*.yml`
- replace `.harness/raw-prompt-router.yml`,
  `.harness/freestyle-promotion.yml`, `.harness/workflow-triggers.yml`, or
  `.harness/workflow-selection.yml` as routing truth
- redefine route labels, lanes, phases, gates, or done criteria
- bypass explicit approval requirements
- claim Codex-only capabilities such as repo-local skills or delegated agents
