# Agent Adapters

Harness has one protocol and many possible agent adapters.

The protocol lives in `docs/AGENT_PROTOCOL.md`. Adapter files translate that
protocol into a specific tool's trigger format, prompt format, or skill system.
Adapters must not redefine gate policy, source-of-truth hierarchy, or done
criteria.

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

## Other Agents

For another agent, create adapter instructions that map the same phases:

- Analysis -> `analysis.md`
- Proposal -> `proposal.md`
- Plan -> `implementation-plan.md`
- Implementation -> approved product slice only
- Review -> findings-first review
- Validation -> proof and `validation.md`
- Optional UAT -> optional UAT mapping after code review approval
