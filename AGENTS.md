# Agent Instructions

Add project-specific agent instructions here.

## Harness

This repo uses Harness. Before work, read:

- `docs/AGENT_PROTOCOL.md`
- `docs/PROJECT_PROFILE.md`
- `docs/FEATURE_INTAKE.md`
- `docs/RAW_PROMPT_ROUTER.md`
- `docs/WORKFLOW_TRIGGERS.md`
- `docs/WORKFLOW_SELECTION.md`
- if ticket or decision queries fail because `harness.db` is missing, run:
    - `scripts/harness init`
    - `scripts/harness migrate`
    - `scripts/harness import tracked`
    - `scripts/harness doctor`
- if the durable cache is still unavailable, use tracked markdown docs as the
  source of truth and continue

Then read additional context only when the task needs it:

- `docs/FREESTYLE_PROMOTION.md` and `docs/FREESTYLE_MODE.md` when evaluating
  or executing `direct-work`
- `docs/WORKFLOW_DEFINITION.md` and `.harness/workflow-definition.yml` for
  tracked ticket phase rules, gates, artifacts, lane shortcuts, or legal
  status transitions
- `docs/BUG_FIX_WORKFLOW.md` and `.harness/bug-fix-workflow.yml` for tracked
  bug-fix work
- `docs/REVIEW_WORKFLOW.md` and `.harness/review-workflow.yml` for review
  artifact or code-review gate behavior
- `docs/ARCHITECTURE.md` for normal or high-risk code changes, structural
  changes, or boundary questions
- `docs/CONTEXT_RULES.md` when deciding what else to read for the current
  phase, lane, or validation depth
- `docs/HARNESS_SKILLS.md` only when the current agent uses the Codex adapter
  or when the task changes skill or specialist-agent behavior

Use the stable repo-local entrypoint `scripts/harness` as the main operational
tool. Tracked markdown is the portable source of truth. `harness.db` is a local
cache that can be rebuilt from tracked docs.
