# Scripts

This directory contains Harness automation tools.

## Harness CLI

The Harness CLI wrapper is the primary interface for the durable layer.
Installed projects keep `scripts/harness` as the stable entrypoint.

For the full installed runtime reference, see `docs/HARNESS_COMMANDS.md`.

```bash
scripts/harness init
scripts/harness migrate
scripts/harness import brownfield
scripts/harness import tracked
scripts/harness ticket ...
scripts/harness ticket next --id <ticketId>
scripts/harness ticket start-phase --id <ticketId> --phase <phase>
scripts/harness ticket check-phase --id <ticketId>
scripts/harness verify ticket --id <ticketId>
scripts/harness doctor
scripts/harness export
scripts/harness decision ...
scripts/harness query ...
```

Run `scripts/harness help` for full usage.

Tracked markdown is the portable source of truth. `harness.db` is a local cache
that can be rebuilt from tracked docs.
