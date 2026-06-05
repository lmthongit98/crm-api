# Workflow Definition

This repository includes `.harness/workflow-definition.yml` as the
machine-readable definition of the shared tracked ticket runtime.

## Purpose

Use this contract to answer:

- which tracked statuses exist
- which phase comes next
- which phases scaffold durable files
- which artifacts are required by status
- which approval gates are valid
- which lanes may skip `analysis`
- which workflow types bind onto the shared runtime
- which `ticket update` transitions are legal for shared tracked statuses

## Current Scope

The current MVP still uses one shared tracked runtime status engine for:

- feature-oriented tracked work
- tracked bug-fix work

The bug-fix workflow keeps its own additional contract in
`.harness/bug-fix-workflow.yml`, but both workflow types currently share the
same durable runtime phase skeleton.

## What This File Is Not

This file does not mean the installed runtime is already fully data-driven.

Today:

- `scripts/harness` now consumes selected rules from this file for lane-based
  entry, next-phase mapping, gate checks, artifact timing, manual ticket-update
  transitions, and workflow-type scaffold template selection
- some workflow behavior still remains in shell logic
- adapters and future runtime work should prefer this file over reverse
  engineering prose docs

## Main Sections

- `statuses`
- `phases`
- `gates`
- `artifacts`
- `lane_rules`
- `workflow_type_bindings`

## Practical Use

Read `.harness/workflow-definition.yml` when you need a compact answer for:

- whether `analysis` is required for the current lane
- which file `ticket start-phase` scaffolds for a phase
- which workflow type overrides the default scaffold path for that phase
- which gate must pass before the next durable phase
- which workflow types use generic vs bug-fix templates
