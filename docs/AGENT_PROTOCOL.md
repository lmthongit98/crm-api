# Agent Protocol

This document is the agent-neutral Harness workflow. It applies to Codex,
Copilot, Cursor, Claude Code, CI bots, and human operators. Tool-specific files
such as `.codex/skills/*` are adapters for this protocol, not the source of
truth.

Harness is an operating model for agent-assisted software delivery.

The product is what users touch. The harness is what agents touch.

## Source Of Truth

Tracked markdown is the portable source of truth for team workflows:

- `docs/tickets/<ticketId>.md` stores the local requirement.
- `docs/work/<ticketId>-*/` stores the work packet: analysis, proposal,
  implementation plan, validation evidence, and optional UAT mapping.
- `docs/product/*` stores accepted product behavior.
- `docs/decisions/*` stores durable architectural or workflow decisions.

`harness.db` is a local operational cache. Rebuild it from tracked docs with:

```bash
scripts/harness import tracked
```

Do not treat a local `harness.db` file as the only team state.

## Mental Model

```text
human intent
  -> intake
  -> work packet
  -> agent work loop
  -> product delta
  -> validation proof
  -> harness delta
  -> next intent
```

Every task has two possible outputs:

1. Product delta: app code, tests, API shape, data model, or product docs.
2. Harness delta: docs, templates, validation expectations, decision records,
   or workflow guidance that make the next task easier.

## Workflow Selection And Ticket Workflow

Requests should first be routed by:

1. raw prompt router outcome when no tracked ticket exists
2. `engagement_mode`
3. `workflow_type`
4. `lane`

The machine-readable raw prompt router contract lives in
`.harness/raw-prompt-router.yml`. The prose routing guide lives in
`docs/RAW_PROMPT_ROUTER.md`.

The machine-readable promotion criteria that decide when work must leave
`freestyle` live in `.harness/freestyle-promotion.yml`. The prose guide lives
in `docs/FREESTYLE_PROMOTION.md`.

The operating contract for work that remains in `freestyle` lives in
`.harness/freestyle-mode.yml`. The prose guide lives in
`docs/FREESTYLE_MODE.md`.

The explicit workflow trigger conventions for raw prompts live in
`.harness/workflow-triggers.yml`. The prose guide lives in
`docs/WORKFLOW_TRIGGERS.md`.

The machine-readable selection contract lives in
`.harness/workflow-selection.yml`. The prose selection guide lives in
`docs/WORKFLOW_SELECTION.md`.

The machine-readable shared tracked execution rules now live in:

- `.harness/workflow-definition.yml`
- `docs/WORKFLOW_DEFINITION.md`

The current active tracked workflows are feature and bug-fix. Both bind onto
the shared tracked runtime defined above.

Tracked bug-fix flow uses the same durable phase order but applies the
bug-oriented contract in `.harness/bug-fix-workflow.yml` and
`docs/BUG_FIX_WORKFLOW.md` for reproduction context, expected vs actual
behavior, root-cause notes, regression scope, and fix validation.

Durable review inside tracked workflows uses `.harness/review-workflow.yml`
and `docs/REVIEW_WORKFLOW.md` for the required `review.md` artifact before
`code_review_approved`.

Intake classification, raw prompt routing, freestyle promotion, freestyle mode,
workflow selection, risk lanes, and hard escalation rules live in
`docs/FEATURE_INTAKE.md`, `docs/RAW_PROMPT_ROUTER.md`,
`docs/FREESTYLE_PROMOTION.md`, `docs/FREESTYLE_MODE.md`,
`docs/WORKFLOW_TRIGGERS.md`, `docs/WORKFLOW_SELECTION.md`,
`docs/WORKFLOW_DEFINITION.md`, `docs/BUG_FIX_WORKFLOW.md`, and
`docs/REVIEW_WORKFLOW.md`.

At raw-requirement intake and during analysis, agents must ask follow-up
questions when important concepts, business rules, or scope statements remain
unclear after inspecting the codebase and tracked docs. Each follow-up question
should include suggested answers the user can confirm or refine. Do not guess
missing product intent.

Agents must stop at approval gates. A read-only gate check uses:

```bash
scripts/harness ticket gate --id <ticketId> --gate proposal
scripts/harness ticket gate --id <ticketId> --gate plan
scripts/harness ticket gate --id <ticketId> --gate code_review
```

Before writing or advancing a phase, inspect the runtime guidance:

```bash
scripts/harness ticket next --id <ticketId>
scripts/harness ticket check-phase --id <ticketId>
```

Only run `scripts/harness ticket approve ...` when a human explicitly asks to
approve that gate.

Use `scripts/harness ticket start-phase ...` to scaffold the next legal ticket
artifact instead of copying templates by hand. Use `ticket repair` only for
explicit recovery work when tracked docs and runtime state have drifted.

Do not reverse-engineer shared phase order, artifact requirements, lane
shortcuts, or gate-to-status mapping from prose when `.harness/workflow-definition.yml`
already answers the question directly.

## Phase Ownership

Each phase owns a narrow output:

- Analysis writes or updates only `analysis.md`.
- Analysis must stop and ask the user when key terms, rules, or expected
  outcomes cannot be confirmed from tracked docs or current code.
- Analysis follow-up questions should include suggested answers so the user can
  resolve ambiguity without drafting the answer shape from scratch.
- Proposal writes or updates only `proposal.md`.
- Planning writes or updates only `implementation-plan.md`.
- Implementation changes product code, product docs, and immediate validation
  notes for the approved slice only.
- Review writes or updates only `review.md`, produces findings first, and does
  not fix code by default.
- Validation updates proof and `validation.md`.
- UAT may write `uat.md` and test assets after code review approval when the
  repo or team wants an explicit acceptance pass.

If one ticket is too broad for one safe review, keep it as a parent ticket and
create child tickets with independently reviewable outcomes. Prefer each child
ticket to cover one REST API, one database table or domain entity, or one
tightly bounded workflow slice, and keep the estimated effort within 5 working
days. Update the parent ticket metadata to `Relationship: parent`, set each
child ticket's `Parent ticket`, and move the parent runtime status to
`decomposed` so routing targets the child tickets instead of the parent.

## Consistency Checks

Before final response on normal or high-risk ticket work, run:

```bash
scripts/harness verify ticket --id <ticketId>
```

For repository-wide Harness maintenance or CI:

```bash
scripts/harness doctor
```

These commands are read-only. They report drift between ticket status, work
packet files, approval gates, validation evidence, and parent or child links.
