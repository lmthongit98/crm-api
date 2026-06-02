# Context Engineering Rules

Context rules help agents decide what to read, when to read it, and when to
stop reading. They are additive to the stable `AGENTS.md` reading list.

The goal is not to maximize context. The goal is to put the right information
in the model for the current task phase and risk lane.

## Context Phases

### Intake Phase

Read to classify the request, find the affected surface, and choose a lane.

| Document Or Source | Tiny | Normal | High-Risk |
| --- | --- | --- | --- |
| `AGENTS.md` | Must | Must | Must |
| `docs/AGENT_PROTOCOL.md` | Must | Must | Must |
| `docs/PROJECT_PROFILE.md` | Must | Must | Must |
| `docs/FEATURE_INTAKE.md` | Must | Must | Must |
| `docs/tickets/<ticketId>.md` | Must when ticket-driven | Must when ticket-driven | Must when ticket-driven |
| `scripts/harness query matrix` | Must | Must | Must |
| `scripts/harness query tickets` | Should when ticket-driven | Must when ticket-driven | Must when ticket-driven |
| `README.md` | Should | Must | Must |
| `docs/HARNESS_SKILLS.md` | Skip | Should for skill-driven ticket work | Must for workflow or skill changes |
| `docs/ARCHITECTURE.md` | Skip | Should | Must |
| Relevant `docs/product/*` | Skip if unrelated | Must if product behavior changes | Must |
| Relevant `docs/work/*` | Skip if unrelated | Must if a work packet exists | Must |
| `docs/decisions/*` | Skip | Should if architecture or durable rules are touched | Must |
| `docs/HARNESS_COMPONENTS.md` | Skip | Should for Harness improvements | Must for observability or benchmark work |

### Planning Phase

Read to decide the smallest safe approach and expected proof.

| Document Or Source | Tiny | Normal | High-Risk |
| --- | --- | --- | --- |
| Current files to edit | Must | Must | Must |
| Ticket analysis or proposal files | Skip if no ticket | Must when ticket-driven | Must when ticket-driven |
| `docs/templates/ticket-work/analysis.md` | Skip | Should when creating or refreshing analysis | Should |
| `docs/templates/ticket-work/*` | Skip unless ticket-driven | Must when ticket-driven | Must when ticket-driven |
| `docs/HARNESS_SKILLS.md` | Skip unless skill behavior is relevant | Should for skill-driven ticket work | Must for workflow or specialist-agent changes |
| `docs/templates/high-risk-work/*` | Skip | Skip unless risk escalates | Must |
| `docs/ARCHITECTURE.md` | Skip | Should for code or boundary changes | Must |
| `docs/TEST_MATRIX.md` or `scripts/harness query matrix` | Should | Must | Must |
| Relevant decisions | Skip | Should | Must |
| `docs/HARNESS_MATURITY.md` | Skip | Should for Harness improvements | Must for maturity or process changes |
| Trace history and current Harness docs | Skip | Should if friction repeats | Must if changing Harness behavior |

### Implementation Phase

Read while making the change. Keep this phase scoped to files that directly
affect the selected work item.

| Document Or Source | Tiny | Normal | High-Risk |
| --- | --- | --- | --- |
| Files being changed | Must | Must | Must |
| Adjacent files with same pattern | Should | Must | Must |
| Relevant product docs | Skip if copy-only | Must if behavior changes | Must |
| Relevant work packet | Skip if no packet needed | Must | Must |
| `docs/HARNESS_SKILLS.md` | Skip unless the task changes workflow behavior | Should for skill-owned ticket phases | Must for workflow or specialist-agent changes |
| Ticket workflow status | Skip if no ticket | Must before gated phases | Must before gated phases |
| Relevant templates | Skip | Should when adding docs | Must |
| `docs/ARCHITECTURE.md` | Skip | Should for structural changes | Must |
| Provider, API, or security docs | Skip | Should if touched | Must |
| Unrelated docs and historical traces | Skip | Skip | Should only if they affect decisions |

### Validation Phase

Read to prove the change and avoid claiming unsupported completion.

| Document Or Source | Tiny | Normal | High-Risk |
| --- | --- | --- | --- |
| Work item or ticket acceptance criteria | Should | Must | Must |
| `docs/TEST_MATRIX.md` or `scripts/harness query matrix` | Should | Must | Must |
| Validation section of work packet | Skip if no packet | Must | Must |
| `docs/HARNESS_SKILLS.md` | Skip unless validating skill behavior | Should for workflow-skill verification | Must for workflow or specialist-agent claims |
| `docs/templates/validation-report.md` | Skip | Should for notable proof | Must for high-risk proof |
| Relevant commands from README or package docs | Should | Must | Must |

### Trace Phase

Read to leave useful evidence for the next agent.

| Document Or Source | Tiny | Normal | High-Risk |
| --- | --- | --- | --- |
| `docs/TRACE_SPEC.md` | Should | Must | Must |
| `scripts/harness query tickets` | Should when ticket-driven | Must when ticket-driven | Must when ticket-driven |
| `scripts/harness query matrix` | Should | Must | Must |
| Trace history or recent friction records | Skip | Should if friction occurred | Must |
| Changed-file list from `git status --short` | Must | Must | Must |
| Validation command output | Should | Must | Must |
| Work packet or progress log | Skip if no packet | Must | Must |

## Retrieval Triggers

| Trigger Condition | Action |
| --- | --- |
| Task touches database schema, durable records, or migrations | Read `scripts/schema/`, relevant decisions, and `scripts/harness` before planning. |
| Task touches CLI command behavior or installer distribution | Read `scripts/README.md`, `scripts/harness`, and installer docs before editing. |
| Task touches auth, authorization, audit, data loss, or external providers | Treat as high-risk and read `docs/templates/high-risk-work/*` before implementation. |
| Task changes public API shape, product behavior, or user-visible workflow | Read relevant `docs/product/*`, work packets, and proof expectations before editing. |
| Prompt is a ticket id | Read `docs/tickets/<ticketId>.md`, run `scripts/harness ticket load --id <ticketId>`, use `docs/templates/ticket-work/*`, and enforce proposal, plan, code review, and UAT gates with read-only `scripts/harness ticket gate` checks. |
| Task introduces or changes workflow skills, specialist agents, or ticket-phase ownership | Read `docs/HARNESS_SKILLS.md`, `docs/AGENT_PROTOCOL.md`, and `docs/HARNESS_COMPONENTS.md` before editing. |
| Ticket analysis reveals multiple independently reviewable APIs or workflows | Stop before writing one large plan, create child-ticket requirements, keep each child to one API or one domain/data slice with estimated effort under 5 working days, and continue planning on the smaller tickets instead of the parent ticket. |
| Task changes Harness policy, source hierarchy, risk classification, or validation requirements | Read `docs/AGENT_PROTOCOL.md`, `docs/FEATURE_INTAKE.md`, `docs/ARCHITECTURE.md`, and relevant decisions; pause if direction is ambiguous. |
| Task discovers repeated confusion, stale docs, or missing proof | Record `harness_friction`, update local Harness docs when appropriate, and capture the gap in trace notes when the fix is out of scope. |
| Final response is being prepared | Re-read the validation evidence, run `scripts/harness verify ticket --id <ticketId>` for ticket work, check `git status --short`, and read `docs/TRACE_SPEC.md` before recording the final trace. |

## Token Budget Guidance

| Lane | Target Context Budget | Read Shape |
| --- | --- | --- |
| Tiny | About 2K tokens | Stable entrypoint docs, matrix query, and the exact file being changed. |
| Normal | About 5K tokens | Intake docs, relevant product or work docs, architecture when structural, validation expectations, and trace spec at the end. |
| High-risk | About 10K tokens | Full intake, architecture, relevant decisions, high-risk templates, product docs, validation docs, and trace spec. |

Budget rules:

- Prefer targeted `rg` searches over bulk reading.
- Read the smallest section that answers the current phase question.
- Escalate context when a retrieval trigger fires.
- Do not keep reading unrelated past context after the lane, affected files,
  and validation path are clear.
