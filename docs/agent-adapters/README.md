# Agent Adapters

Harness has one protocol and many possible agent adapters.

The protocol lives in `docs/AGENT_PROTOCOL.md`. Adapter files translate that
protocol into a specific tool's trigger format, prompt format, or skill system.
Adapters must not redefine gate policy, source-of-truth hierarchy, or done
criteria.

The installed brownfield template currently ships two adapter overlays:

- `codex`: richer repo-local skills and delegated-agent support
- `copilot`: thinner repo-local instruction and adapter-reference support

Future adapters must stay additive and should not be treated as available in an
installed repo until their overlay and CLI support ship.

Copilot support is intentionally thinner than Codex. It should still follow the
same shared workflow contract and runtime commands instead of inventing a
separate phase model.

Workflow routing truth lives in `.harness/raw-prompt-router.yml`,
`.harness/freestyle-promotion.yml`, `.harness/workflow-selection.yml`,
`.harness/freestyle-mode.yml`, `docs/RAW_PROMPT_ROUTER.md`,
`docs/FREESTYLE_PROMOTION.md`, `docs/FREESTYLE_MODE.md`, and
`docs/WORKFLOW_SELECTION.md`.

## Codex

Codex uses repo-local skills under `.codex/skills/*` and optional delegated
agents under `.codex/agents/*`.

The Codex adapter should:

- Read `docs/AGENT_PROTOCOL.md` and `docs/PROJECT_PROFILE.md`.
- Use `raw-requirement-intake` when the user gives a raw requirement instead of
  a tracked ticket id.
- Use `scripts/harness ticket next`, `scripts/harness ticket check-phase`,
  `scripts/harness ticket status`, and `scripts/harness ticket gate` for
  runtime guidance and read-only checks.
- Run `scripts/harness ticket approve` only after explicit human approval.
- Keep each skill scoped to its phase output.
- Follow the shared live-data and database-check policy in
  `docs/AGENT_PROTOCOL.md` instead of redefining phase ownership inside Codex
  skills.

## Other Agents

For another agent, create adapter instructions that map the same phases:

- Analysis -> `analysis.md`
- Proposal -> `proposal.md`
- Plan -> `implementation-plan.md`
- Review -> `review.md`
- Implementation -> approved product slice only
- Validation -> proof and `validation.md`
- Optional UAT -> optional UAT mapping after code review approval

All adapters should also inherit the shared live-data policy from
`docs/AGENT_PROTOCOL.md`:

- keep database or other live-environment checks read-only by default
- use them only when repo-local truth is insufficient for the current phase
- treat implementation as the primary owner, with narrower use in analysis,
  review, validation, and optional UAT
- keep routing-only adapter steps out of direct live database inspection
