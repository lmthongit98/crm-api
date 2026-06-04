# Freestyle Mode

Harness treats `freestyle` as a real operating mode, not just the absence of a
ticket.

The machine-readable source of truth is:

- `.harness/freestyle-mode.yml`

## What Fits

Use `freestyle` for:

- repo questions
- narrow edits
- exploratory debugging
- small docs updates

## Rules

While working in `freestyle`:

- inspect repo context first
- keep the work narrow and bounded
- do not create tracked ticket artifacts by default
- do not invent workflow state
- do not self-approve any gate
- say clearly when the work should escalate into tracked workflow

## Escalate Out Of `freestyle` When

- scope grows
- risk grows
- approval is needed
- durable handoff is needed
- validation cost grows
- the work becomes clearly multi-step

## Expected Output

At minimum, report:

- route
- work summary
- commands run
- files touched
- escalation status
- next step
