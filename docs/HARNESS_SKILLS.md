# Harness Skills

This document maps the agent-neutral Harness workflow to Codex phase skills and
delegated specialist agents. `docs/AGENT_PROTOCOL.md` is the workflow source of
truth; `.codex/skills/*` are Codex adapters for this repository.

## Install Shape

Phase skills live in:

```text
.codex/skills/<skill-name>/
  SKILL.md
  agents/openai.yaml
```

Delegated specialist agents live in:

```text
.codex/agents/<agent-name>/
  SKILL.md
  agents/openai.yaml
```

## Shared Contract

All ticket workflow skills and delegated specialist agents use this input
contract:

- `ticket_id`
- `goal` or requested phase
- optional `changed_files`
- optional `validation_scope`

All should report this outcome contract before finishing:

- `artifacts produced`
- `gates checked`
- `commands run`
- `blocking issues`
- `next allowed phase`

Phase skills should refuse work outside their owned phase instead of silently
continuing to the next gate.

## Execution Model

- Skills are instructions for the current agent to execute one workflow phase.
- Specialist agents are prompts for spawned sub-agents that perform an
  independent second pass.
- Skills must use `scripts/harness ticket next`,
  `scripts/harness ticket check-phase`, `scripts/harness ticket status`, and
  `scripts/harness ticket gate` for runtime guidance and gate checks.
- Skills must not run `scripts/harness ticket approve` unless the user
  explicitly asks to approve a gate.
- `review-agent` and `uat-agent` must only be invoked through `spawn_agent`.

## Workflow Map

| Phase | Primary execution | Gate requirement before use | Main outputs | Stop conditions |
| --- | --- | --- | --- | --- |
| Raw requirement intake | `raw-requirement-intake` | Human has provided a raw requirement instead of a tracked ticket id | intake classification, router outcome, engagement-mode recommendation, workflow-type recommendation, rationale codes, ticket recommendation or direct-work recommendation, optional ticket draft | Missing requirement clarity, unclear scope, unresolved concepts not found in repo truth, or need to turn the request into a tracked ticket before phase work |
| Analyze | `ticket-analysis` | Ticket file exists at `docs/tickets/<ticketId>.md` | `analysis.md` under `docs/work/` | Missing ticket, ambiguous requirements, unresolved concepts not found in repo truth, or request to move into proposal, plan, code, tests, or UAT |
| Decompose | `ticket-decomposition` | Ticket exists and analysis or proposal shows it is too broad for one safe review | child ticket drafts, parent metadata updates, parent `proposal.md` routing updates | Ticket is already narrow enough for one safe review, missing scope clarity, or parent-child metadata cannot be established cleanly |
| Proposal | `ticket-proposal-plan` with `goal=proposal` | Analysis complete for `normal` and `high-risk`; `tiny` and `normal-fast` may start here directly | `proposal.md` | Missing required analysis for lanes that need it, unresolved ambiguity, or attempt to create plan or code before proposal approval |
| Plan | `ticket-proposal-plan` with `goal=implementation-plan` | Proposal approved | `implementation-plan.md` | Proposal not approved, parent ticket is `decomposed`, or attempt to implement directly |
| Implement | `ticket-implementation` | Plan approved | Code changes, work item updates, validation notes | Plan not approved or request to generate Robot UAT |
| Review | `ticket-code-review` | Implementation exists | Findings-first review output, residual risks | Attempt to implement fixes by default or missing changed-scope context |
| Optional Robot UAT | `ticket-robot-uat` | Code review approved | optional `tests/robot/<ticketId>/`, `uat.md` | Code review gate not approved |

Use `raw-requirement-intake` when the user has given a raw requirement instead
of a tracked ticket id.

Its primary job is to return one route label:

- `needs-clarification`
- `direct-work`
- `tracked-feature`
- `tracked-bug-fix`
- `tracked-investigation`
- `tracked-review`

Use `ticket-orchestrator` when the user has not specified the phase for an
existing tracked ticket and the runtime must determine the next legal action.

Use `ticket-decomposition` when a ticket spans multiple independently
reviewable APIs, domain/data slices, or workflows, or would likely exceed 5
working days.

## Required Commands By Phase

- Raw requirement intake:
  - inspect `docs/FEATURE_INTAKE.md`, `docs/RAW_PROMPT_ROUTER.md`, `docs/FREESTYLE_PROMOTION.md`, `docs/FREESTYLE_MODE.md`, `docs/WORKFLOW_SELECTION.md`, `docs/AGENT_PROTOCOL.md`, and `docs/PROJECT_PROFILE.md`
  - inspect relevant repo code and docs before classifying
  - when asking follow-up questions, include suggested answers the user can confirm or refine
  - if non-trivial, recommend creating `docs/tickets/<ticketId>.md` before phase work
- Analyze:
  - `scripts/harness ticket load --id <ticketId>`
  - `scripts/harness ticket next --id <ticketId>`
  - `scripts/harness ticket check-phase --id <ticketId>`
  - `scripts/harness ticket status --id <ticketId>`
  - `scripts/harness query tickets`
  - when asking follow-up questions, include suggested answers the user can confirm or refine
- Decompose:
  - `scripts/harness ticket load --id <ticketId>`
  - `scripts/harness ticket next --id <ticketId>`
  - `scripts/harness ticket check-phase --id <ticketId>`
  - `scripts/harness ticket status --id <ticketId>`
  - `scripts/harness query tickets`
  - `scripts/harness ticket update --id <ticketId> --status decomposed`
- Proposal:
  - `scripts/harness ticket next --id <ticketId>`
  - `scripts/harness ticket start-phase --id <ticketId> --phase proposal`
  - `scripts/harness ticket status --id <ticketId>`
  - `scripts/harness ticket update --id <ticketId> --status proposal_pending_approval`
- Plan:
  - `scripts/harness ticket next --id <ticketId>`
  - `scripts/harness ticket start-phase --id <ticketId> --phase implementation-plan`
  - `scripts/harness ticket status --id <ticketId>`
  - `scripts/harness ticket gate --id <ticketId> --gate proposal`
  - `scripts/harness ticket update --id <ticketId> --status plan_pending_approval`
- Implement:
  - `scripts/harness ticket next --id <ticketId>`
  - `scripts/harness ticket status --id <ticketId>`
  - project validation commands from `README.md`
- Review:
  - `scripts/harness ticket next --id <ticketId>`
  - `scripts/harness ticket status --id <ticketId>`
  - validation commands relevant to the changed slice
- Optional Robot UAT:
  - `scripts/harness ticket next --id <ticketId>`
  - `scripts/harness ticket start-phase --id <ticketId> --phase uat`
  - `scripts/harness ticket status --id <ticketId>`
  - `scripts/harness ticket gate --id <ticketId> --gate code_review`
  - `scripts/harness verify ticket --id <ticketId>`

## Companion Skills

- `grill-me`
  - Use when the user explicitly invokes `$grill-me` or says "grill me", or
    automatically when analysis, proposal, or implementation-plan work hits
    material ambiguity that cannot be resolved from local context.
  - Behavior: ask one question at a time, explore the repo first for
    discoverable answers, and provide a recommended answer with each question.

## Specialist Agents

- `review-agent`
  - Use for an independent pass on code review findings.
  - Constraints: invoke only via `spawn_agent`; never use as the primary phase
    skill; no code edits unless explicitly requested.
- `uat-agent`
  - Use for an independent optional Robot UAT pass after code review.
  - Constraints: invoke only via `spawn_agent`; never use as the primary phase
    skill; only after `code_review_approved`.
