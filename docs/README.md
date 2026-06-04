# Documentation Map

This directory holds the Harness operating docs and the product contract for the
current repository.

## Main Files

- `AGENT_PROTOCOL.md`: agent-neutral ticket workflow and gate policy.
- `PROJECT_PROFILE.md`: project-specific stack, paths, commands, and risk
  overrides.
- `HARNESS_SKILLS.md`: phase-to-skill and gate-to-output mapping for the Codex
  ticket workflow adapter.
- `FEATURE_INTAKE.md`: intake classification and risk-lane rules.
- `RAW_PROMPT_ROUTER.md`: route labels and output rules for raw requests.
- `FREESTYLE_PROMOTION.md`: explicit criteria for leaving `freestyle`.
- `FREESTYLE_MODE.md`: operating rules for work that stays in `freestyle`.
- `WORKFLOW_TRIGGERS.md`: explicit override conventions for raw prompts.
- `WORKFLOW_SELECTION.md`: engagement-mode and workflow-type routing rules.
- `BUG_FIX_WORKFLOW.md`: tracked bug-fix artifact and gate expectations.
- `REVIEW_WORKFLOW.md`: durable review artifact and code-review gate rules.
- `ARCHITECTURE.md`: current brownfield architecture and boundary rules.
- `CONTEXT_RULES.md`: what agents should read by task phase and risk lane.

## Work Records

- `docs/work/`: standard ticket work packets and generated artifacts.
- `docs/decisions/`: durable decisions and tradeoffs.
- `docs/templates/`: reusable work, decision, and validation formats.

The Harness durable layer stores local operational cache state in `harness.db`,
managed by `scripts/harness`. Tracked markdown remains the portable source of
truth.
