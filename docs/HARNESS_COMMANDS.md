# Harness Runtime Command Reference

This document describes the current installed runtime surface for
`scripts/harness` inside a target repo.

Tracked markdown under `docs/` remains the source of truth. `harness.db` is a
local operational cache used by the runtime commands below.

## Usage

```bash
scripts/harness <command> [args]
```

Top-level commands:

- `init`
- `migrate`
- `import brownfield`
- `import tracked`
- `ticket load|status|gate|next|check-phase|start-phase|update|repair|approve`
- `verify ticket`
- `doctor`
- `export`
- `decision add|verify`
- `trace`
- `query matrix|tickets|decisions|traces|friction|stats|sql`

## Runtime State

- Default database path: `HARNESS_DB` if set, otherwise `./harness.db`
- Installed schema directory: `scripts/schema/`
- Local database tables:
  - `schema_version`
  - `ticket_workflow`
  - `decision`
  - `trace`

## Lifecycle Commands

### `scripts/harness init`

Creates `harness.db` if it does not already exist and applies the base schema.

Behavior:

- creates the DB file
- applies `scripts/schema/001-init.sql`
- leaves an existing DB unchanged

### `scripts/harness migrate`

Applies all installed SQL migrations in `scripts/schema/` to `harness.db`.

Behavior:

- runs `init` first if needed
- applies only missing schema versions
- records applied versions in `schema_version`
- is safe to run more than once

### `scripts/harness import brownfield`

Imports tracked decision docs into the local DB.

Behavior:

- runs `migrate` first
- imports `docs/decisions/*.md`
- does not import ticket workflow docs

### `scripts/harness import tracked`

Imports tracked decision docs and tracked ticket workflow docs into the local
DB.

Behavior:

- runs `migrate` first
- imports `docs/decisions/*.md`
- imports `docs/tickets/*.md`
- infers ticket status from files under `docs/work/<ticket-id>-*/`

## Ticket Commands

Ticket commands require the installed ticket schema in `harness.db`.

### `scripts/harness ticket load --id <ticket-id>`

Loads one tracked ticket doc into `ticket_workflow`.

Requirements:

- `docs/tickets/<ticket-id>.md` exists

Behavior:

- reads ticket metadata such as `Type`, `Lane`, `Relationship`, and
  `Parent ticket`
- infers current status from work packet files if they already exist
- inserts or updates the `ticket_workflow` row

### `scripts/harness ticket status --id <ticket-id>`

Prints the current ticket runtime state as `key=value` lines.

Output fields include:

- `ticket_id`
- `type`
- `lane`
- `role`
- `parent_ticket`
- `child_tickets`
- `status`
- `next_phase`
- `work_dir`
- approval timestamps
- `updated_at`

### `scripts/harness ticket gate --id <ticket-id> --gate <proposal|plan|code_review>`

Read-only gate check for the current ticket status.

Behavior:

- succeeds only when the ticket has already passed the named gate
- does not advance ticket state
- rejects decomposed parent tickets

### `scripts/harness ticket next --id <ticket-id>`

Prints the next recommended runtime action for a ticket as `key=value` lines.

Output includes:

- ticket metadata and current status
- `next_phase`
- missing expected artifacts
- blockers
- a recommended next command

Lane behavior:

- `tiny` and `normal-fast` start from `proposal`
- other lanes start from `analysis`

### `scripts/harness ticket check-phase --id <ticket-id>`

Human-readable phase summary for the current ticket.

This is similar to `ticket next` but optimized for a readable terminal summary
instead of machine-friendly `key=value` output.

### `scripts/harness ticket start-phase --id <ticket-id> --phase <phase>`

Scaffolds the next legal ticket-work template into `docs/work/`.

Supported scaffoldable phases:

- `analysis`
- `proposal`
- `implementation-plan`
- `validation`
- `uat`

Behavior:

- enforces the next legal phase from current runtime status
- creates `docs/work/<ticket-id>-*/` if missing
- copies the matching template from `docs/templates/ticket-work/`
- writes the ticket ID into the scaffolded file

Notes:

- this command does not scaffold product code
- `implementation`, `review-and-validation`, and `approval:*` are not valid
  scaffold phases

### `scripts/harness ticket update --id <ticket-id> --status <status>`

Advances a ticket to a limited set of runtime statuses after required artifacts
exist.

Supported target statuses:

- `proposal_pending_approval`
- `decomposed`
- `plan_pending_approval`
- `implementation_complete`
- `uat_generated`
- `blocked`

Behavior:

- validates legal transitions from the current status
- validates required artifacts such as `proposal.md`,
  `implementation-plan.md`, `validation.md`, or `uat.md`
- syncs status lines back into scaffolded work packet files

Use `ticket approve` for approval-gated transitions and `ticket repair` for
manual recovery.

### `scripts/harness ticket repair --id <ticket-id> --status <status>`

Force-updates the stored ticket status for explicit workflow recovery.

Behavior:

- skips the legal transition checks used by `ticket update`
- syncs the new status into scaffolded work packet files when applicable

### `scripts/harness ticket approve --id <ticket-id> --gate <proposal|plan|code_review>`

Applies an explicit human approval gate transition.

Supported transitions:

- `proposal_pending_approval -> proposal_approved`
- `plan_pending_approval -> plan_approved`
- `implementation_complete -> code_review_approved`

Behavior:

- writes the matching approval timestamp
- syncs the resulting status into work packet files
- rejects decomposed parent tickets

## Verification Commands

### `scripts/harness verify ticket --id <ticket-id>`

Checks one ticket for runtime consistency.

Validation includes:

- required tracked ticket file exists
- parent/child relationship metadata is coherent
- expected work packet files exist for the current status
- ticket IDs inside work packet files match the ticket
- status lines and approval timestamps are consistent
- validation and UAT files no longer contain placeholder content where the
  current status requires real proof

### `scripts/harness doctor`

Runs `verify ticket` across every row in `ticket_workflow`.

Behavior:

- succeeds when all tracked tickets are consistent
- reports a failure count otherwise

## Decision Commands

### `scripts/harness decision add ...`

Records or updates a decision row in the local DB.

Supported flags:

- `--id <decision-id>` required
- `--title <title>` required
- `--status <proposed|accepted|superseded|rejected>`
- `--doc <path>`
- `--verify <command>`
- `--impact <text>`
- `--notes <text>`

Example:

```bash
scripts/harness decision add \
  --id 0001 \
  --title "Keep SQLite runtime cache" \
  --status accepted \
  --doc docs/decisions/0001-runtime-cache.md
```

### `scripts/harness decision verify <decision-id>`

Runs the stored `verify_command` for one decision and stores the result as
`pass` or `fail`.

Requirements:

- the decision exists
- it has a `verify_command`

## Trace Command

### `scripts/harness trace ...`

Records an execution trace row in the local DB.

Required flags:

- `--summary <text>`
- `--outcome <value>`

Optional flags:

- `--ticket <ticket-id>`
- `--agent <name>`
- `--duration <seconds>`
- `--tokens <count>`
- `--actions <comma-separated values>`
- `--read <comma-separated values>`
- `--changed <comma-separated values>`
- `--decisions <comma-separated values>`
- `--errors <comma-separated values>`
- `--friction <none|missing-context|weak-proof|unclear-template|command-ux|policy-runtime-mismatch>`
- `--notes <text>`

Behavior:

- stores list-like fields as JSON arrays
- validates `--ticket` if provided
- normalizes the allowed `--friction` values

## Query Commands

### `scripts/harness query matrix`

Reads `docs/TEST_MATRIX.md` and prints a table view of the tracked matrix rows.

### `scripts/harness query tickets`

Prints the `ticket_workflow` table.

### `scripts/harness query decisions`

Prints the `decision` table.

### `scripts/harness query traces`

Prints the 20 most recent trace rows.

### `scripts/harness query friction`

Prints the 20 most recent trace rows with a non-empty friction category.

### `scripts/harness query stats`

Prints counts for:

- matrix rows
- tickets
- decisions
- traces

### `scripts/harness query sql <sql...>`

Runs raw SQL against `harness.db` and prints the result in column format.

Use this for local inspection only.

## Export Command

### `scripts/harness export`

Prints a combined runtime snapshot containing:

- the parsed test matrix
- tracked ticket rows from `ticket_workflow`
- tracked decision rows from `decision`

## Common Flows

Initialize or refresh the local runtime:

```bash
scripts/harness init
scripts/harness migrate
scripts/harness import tracked
scripts/harness doctor
```

Load a ticket and scaffold the next phase:

```bash
scripts/harness ticket load --id DEMO-001
scripts/harness ticket next --id DEMO-001
scripts/harness ticket start-phase --id DEMO-001 --phase analysis
```

Approve a proposal-gated ticket:

```bash
scripts/harness ticket gate --id DEMO-001 --gate proposal
scripts/harness ticket approve --id DEMO-001 --gate proposal
```
