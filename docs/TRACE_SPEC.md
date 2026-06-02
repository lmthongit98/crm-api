# Trace Specification

The `trace` table records what happened during a Harness task. This document
defines the expected depth and format for each field so traces are useful for
review, failure attribution, and future harness evolution.

## Field Reference

| Field | Type | Required | Format |
| --- | --- | --- | --- |
| `task_summary` | TEXT | Yes | One sentence naming the outcome or attempted outcome. |
| `ticket_id` | TEXT | Standard+ for ticket-driven work | Ticket id from the related `ticket_workflow` row. |
| `agent` | TEXT | Optional for minimal; Standard+ expected | Short agent or tool name. |
| `actions_taken` | TEXT | Standard+ | JSON array text. With the current CLI, pass comma-separated items and the CLI stores JSON text. |
| `files_read` | TEXT | Standard+ | JSON array text of paths or command names. |
| `files_changed` | TEXT | Standard+ | JSON array text of changed file paths. |
| `decisions_made` | TEXT | Detailed | JSON array text of decision strings. |
| `errors` | TEXT | Standard+ if errors occurred; Detailed always | JSON array text. Use `none` for detailed traces when no errors occurred. |
| `outcome` | TEXT | Yes before final response | One of `completed`, `blocked`, `partial`, or `failed`. |
| `duration_seconds` | INTEGER | Detailed when available | Positive integer estimate or measured duration. |
| `token_estimate` | INTEGER | Detailed when available | Positive integer estimate. |
| `harness_friction` | TEXT | Standard+ when friction exists; Detailed always | One of `missing_context`, `weak_proof`, `unclear_template`, `command_ux`, `policy_runtime_mismatch`, or `none`. |
| `notes` | TEXT | Optional | Extra review context that does not fit other fields. |

## Quality Tiers

### Minimal

Use for tiny-lane tasks with no meaningful risk.

Required:

- `task_summary`
- `outcome`

### Standard

Use for normal-lane work.

Required:

- All Minimal fields.
- `ticket_id` for ticket-driven work when a ticket exists.
- `agent`
- `actions_taken`
- `files_read`
- `files_changed`
- At least one of `errors` or `harness_friction`

### Detailed

Use for high-risk work.

Required:

- All Standard fields.
- `decisions_made`
- `errors`
- `harness_friction`
- `duration_seconds` or a note explaining why unavailable
- `token_estimate` or a note explaining why unavailable

## Lane Mapping

| Lane | Expected Tier |
| --- | --- |
| Tiny | Minimal |
| Normal | Standard |
| High-risk | Detailed |

## Friction Capture Protocol

Populate `harness_friction` when:

- the agent had to infer a missing rule or source of truth
- validation was unclear, unavailable, or too expensive to run
- a document or durable record was stale or contradictory
- the task revealed a repeated manual step that should become a template,
  command, or checklist

Use these categories consistently:

- `missing_context`: missing source-of-truth docs, unclear ownership, or
  required context not present in tracked repo state
- `weak_proof`: missing, stale, or too-expensive validation for the changed
  slice
- `unclear_template`: work packet or decision templates are missing required
  guidance or contain misleading placeholders
- `command_ux`: runtime commands are too manual, unclear, or require repeated
  recovery steps
- `policy_runtime_mismatch`: documented workflow rules and runtime behavior do
  not match
- `none`: explicit no-friction marker for detailed traces

For review, validation, blocked, or high-risk work, record a trace before the
final hand-off so friction can be queried and triaged later.

If the friction should become future work, capture it in trace notes or a local
decision or work artifact rather than assuming a separate target-repo intake
tracker.
