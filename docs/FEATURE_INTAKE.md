# Feature Intake

Every implementation prompt enters the intake gate before code changes. A new
project spec also enters through this gate before it becomes product docs, work
packets, or implementation work.

The human does not need to classify risk. The harness does.

The human does need to clarify product intent that cannot be derived from the
repo. When a raw requirement includes unclear concepts, missing business rules,
or terms that do not appear in the docs or codebase, the agent should ask
follow-up questions instead of guessing. Each follow-up question should include
suggested answers the human can confirm, reject, or refine.

## Intake Flow

```text
user prompt
  -> classify input type
  -> choose engagement mode
  -> choose workflow type
  -> restate as ticket slice or work packet
  -> find affected product docs and work packets
  -> ask follow-up questions for unclear concepts or missing repo truth
  -> run risk checklist
  -> choose lane: tiny, normal-fast, normal, or high-risk
  -> record type and lane in ticket metadata
```

Use `.harness/workflow-selection.yml` and `docs/WORKFLOW_SELECTION.md` to keep
engagement mode selection separate from workflow type selection and lane
selection.

For requests that do not already start from a tracked ticket, use
`.harness/raw-prompt-router.yml` and `docs/RAW_PROMPT_ROUTER.md` first so the
adapter can choose `needs-clarification`, `direct-work`, or a tracked route
before workflow selection.

Use `.harness/freestyle-promotion.yml` and `docs/FREESTYLE_PROMOTION.md` to
decide when `direct-work` is no longer legal and the request must be promoted
into tracked workflow.

If `direct-work` remains legal, use `.harness/freestyle-mode.yml` and
`docs/FREESTYLE_MODE.md` to govern the lightweight execution path.

If the raw prompt contains explicit trigger conventions such as `$freestyle`,
`$feature`, `$bugfix`, `$review`, or `$workflow`, apply
`.harness/workflow-triggers.yml` and `docs/WORKFLOW_TRIGGERS.md` before
falling back to ordinary prose inference.

If tracked workflow type is `bug-fix`, use `.harness/bug-fix-workflow.yml` and
`docs/BUG_FIX_WORKFLOW.md` for the bug-oriented artifact and gate
expectations.

## Input Types

| Type | Use when | Typical artifact |
| --- | --- | --- |
| Ticket id | Starting from `docs/tickets/<ticketId>.md` and the ticket workflow gates | Ticket workflow row plus work packet |
| New spec | Turning a user-provided project spec into harness-ready docs | Product docs, candidate epics, decisions |
| Spec slice | Implementing selected behavior from an accepted spec | Work packet |
| Change request | Changing, fixing, or refining accepted behavior | Work packet or direct patch |
| New initiative | Adding a larger product area that needs multiple tickets or work packets | Initiative notes plus work packets |
| Maintenance request | Changing technical, operational, or dependency behavior | Work packet, validation report, or decision |
| Harness improvement | Improving how humans and agents collaborate | Direct docs update or durable decision and validation evidence |

## Ticket Metadata

Every tracked ticket should record:

- `Type`: what kind of change this is, such as `feature`, `bugfix`,
  `maintenance`, or `harness-improvement`
- `Lane`: how much workflow ceremony the ticket needs

Lane selection is not the same thing as workflow type selection. In the current
MVP, tracked `feature` and tracked `bug-fix` both have active portable
contracts. `investigation` and `review` may still be selected and documented
as planned routing targets without implying that their dedicated runtime
behavior already exists.

The harness should choose lane from risk, scope, and clarity together. Do not
use "not high-risk" by itself as permission to skip workflow phases.

If clarity is still weak after repo inspection, the intake phase is not done.
Ask the user follow-up questions until the remaining ambiguity is small enough
for a safe ticket lane choice. Each question should come with suggested answers
to speed up clarification and keep the intent concrete.

## Lanes

### Tiny

Use for low-risk docs, copy, names, or narrow edits.

Requirements:

- Ticket metadata should still declare `Lane: tiny`.
- Agents may skip `analysis` and start from `proposal` or direct implementation
  when the requirement is already clear and bounded.
- Keep affected docs current.
- Run available quick checks.
- Update the harness only if friction was found.

### Normal-Fast

Use for bounded product or maintenance work that is not high-risk and is
already clear enough to avoid a separate analysis pass.

Requirements:

- Record `Lane: normal-fast` in the ticket metadata.
- Agents may skip `analysis` and start from `proposal`.
- Keep proposal, plan, validation, and approval gates intact unless the work is
  later reclassified.
- Reclassify to `normal` or `high-risk` if ambiguity or blast radius grows.
- If material ambiguity appears during intake, stop the fast path and ask the
  user clarifying questions before proceeding.

### Normal

Use for work item-sized behavior with bounded blast radius.

Requirements:

- Record `Lane: normal` in the ticket metadata.
- Create or update one work packet from `docs/templates/ticket-work/analysis.md`.
- Link relevant product docs.
- Add or update validation expectations.
- Implement the smallest vertical slice when implementation exists.
- Keep validation notes current for the affected slice.
- If analysis finds concepts or requirements that are still unclear after repo
  inspection, ask the user follow-up questions before moving to proposal.
  Include suggested answers with each question so the user can resolve
  ambiguity quickly.

### High-Risk

Use when the work can affect security, data, scope, contracts, or multiple
roles or platforms.

Requirements:

- Record `Lane: high-risk` in the ticket metadata.
- Create a high-risk work folder using `docs/templates/high-risk-work/`.
- Fill in `execplan.md`, `overview.md`, `design.md`, and `validation.md`.
- Ask for human confirmation before implementation if direction is ambiguous.
- Record a decision when behavior or architecture changes meaningfully.
- Decompose into child tickets when one approved plan would still cover too
  many independent APIs or workflows for safe review.
- Prefer each child ticket to fit one REST API, one database table or domain
  entity, or one tightly bounded workflow slice.
- Keep the estimated effort for each child ticket within 5 working days.

## Risk Checklist

| Risk flag | Applies when the work touches |
| --- | --- |
| Auth | login, logout, sessions, JWT, password, refresh token |
| Authorization | roles, permissions, tenant or company scope |
| Data model | schema, migrations, uniqueness, deletion, retention |
| Audit/security | audit logs, privacy, sensitive data, access logs |
| External systems | email, payments, cloud services, provider SDKs, queues, webhooks |
| Public contracts | API shape, response envelope, client-visible behavior |
| Existing behavior | already implemented or test-covered behavior changes |
| Weak proof | unclear or missing tests around the affected area |
| Multi-domain | more than one product domain changes at once |

Hard gates:

- Auth
- Authorization
- Data loss or migration
- Audit/security
- External provider behavior
- Removing or weakening validation requirements
