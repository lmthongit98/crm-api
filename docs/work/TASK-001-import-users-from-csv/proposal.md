# Proposal

## Ticket

- ID: TASK-001
- Status: `proposal_approved`
- Proposal type: `implementation`

## Outcome

- Add `GET /api/v1/users/export` under the existing `api/v1/users` controller.
- Return a downloadable CSV attachment containing one row per non-deleted user.
- Export the columns `id`, `username`, `email`, `firstName`, `lastName`, and
  `roles` in a fixed header order.
- Derive `roles` from the user's group roles and serialize them as a
  comma-separated string.
- Reuse the existing `Permission.USER_VIEW` authorization boundary for the new
  read endpoint.

## Proposed Approach

1. Add a new controller endpoint on `UserController` for `GET /export` that
   returns a CSV response with `text/csv` content type and an attachment header
   such as `filename=\"users-export.csv\"`.
2. Extend `UserService` and `UserServiceImpl` with an export method that loads
   all users whose status is not `DELETED`, maps entity fields into CSV rows,
   and emits an empty `roles` value when a user has no group or assigned roles.
3. Add a dedicated repository query that bulk-loads users with group and role
   associations in one read path so the export does not rely on lazy-loading or
   repeated role fetches while iterating rows.
4. Update the product API contract docs to describe the new route, response
   headers, exported columns, and non-deleted user scope.
5. Validate with focused tests around CSV formatting, role joining, response
   headers, and the non-deleted status filter.

## Decomposition Routing

- Parent or standalone: `standalone`
- Child tickets: `none`
- Next active ticket after approval or decomposition: `TASK-001`

## Affected Surfaces

- Product docs: `docs/product/api-contracts.md`
- Code modules:
  - `src/main/java/com/crm/controller/UserController.java`
  - `src/main/java/com/crm/service/UserService.java`
  - `src/main/java/com/crm/service/impl/UserServiceImpl.java`
  - `src/main/java/com/crm/repository/UserRepository.java`
- Data or schema: no schema change; read-only use of existing `User`,
  `Group`, and `Role` relationships
- External providers: none
- Validation:
  - CSV header and row formatting
  - role-name serialization
  - response headers and content type
  - exclusion of `DELETED` users

## Risks And Mitigations

- Risk: full export could trigger N+1 loading on user-role relationships.
  Mitigation: use a dedicated repository fetch path that preloads group and
  roles for the exported user set.
- Risk: CSV rows can break when fields contain commas or blanks.
  Mitigation: centralize CSV row generation and test empty values plus
  multi-role serialization.
- Risk: export semantics could drift from the repository's soft-delete model.
  Mitigation: document and test the chosen scope explicitly as all non-deleted
  users (`ACTIVE`, `TEMPORARY_BLOCKED`, `PERMANENT_BLOCKED`).

## Approval

Proposal approval must be recorded by a human before writing the implementation
plan. Agents should check the gate with:

```bash
scripts/harness ticket gate --id <ticketId> --gate proposal
```

If this proposal decomposes the ticket into child tickets, update the parent
ticket metadata, list the child tickets above, and sync the runtime with:

```bash
scripts/harness ticket update --id <ticketId> --status decomposed
```

Only run this approval command after explicit human approval on an
implementation proposal:

```bash
scripts/harness ticket approve --id <ticketId> --gate proposal
```
