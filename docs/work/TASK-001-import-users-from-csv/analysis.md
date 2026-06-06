# Analysis

## Ticket

- ID: TASK-001
- Source: `docs/tickets/<ticketId>.md`

## Current State

- `UserController` currently exposes create, update, password change, paginated
  search, role lookup, avatar download, and delete endpoints under
  `api/v1/users`. There is no export endpoint today.
- Existing read endpoints use `Permission.USER_VIEW` for authorization. The new
  export endpoint belongs in the same controller surface and should follow the
  same permission boundary unless product intent says otherwise.
- User role data is indirect. `User` belongs to one `Group`, and `Group` owns a
  many-to-many set of `Role`. Existing `findUserWithRolesById` already maps
  roles from `user.group.roles`, so the export should derive the CSV `roles`
  column from the same relationship.
- `UserResponseDto` does not contain `firstName`, `lastName`, or roles, so the
  export should not reuse the current paginated DTO path as-is.
- `UserRepository` supports paginated search excluding `DELETED` users, but it
  does not provide a bulk read optimized for exporting users with groups and
  roles. An export implementation will likely need a dedicated fetch query to
  avoid lazy-loading or N+1 access when iterating users and roles.
- Product docs in `docs/product/api-contracts.md` list user CRUD/search, role
  lookup, avatar download, and delete, but no CSV export contract.

## Requirements Readback

- Add `GET /api/v1/users/export`.
- Return a downloadable CSV attachment with content type suitable for CSV.
- Each exported row should include: `id`, `username`, `email`, `firstName`,
  `lastName`, and `roles`.
- The `roles` column should serialize the user's role names as a comma-separated
  string.
- The endpoint is a full export, not a paginated API response.

## Gaps

- No controller endpoint, service method, repository query, or CSV-writing
  utility exists for this behavior.
- No product contract doc currently defines the export route, response headers,
  or file naming convention.
- The ticket phrase "all user data" is ambiguous against the repo's status
  model. Users can be `ACTIVE`, `DELETED`, `TEMPORARY_BLOCKED`, or
  `PERMANENT_BLOCKED`, and current read flows do not use one consistent rule
  for "all users":
  - `searchUsers(...)` excludes only `DELETED`.
  - `findUserById(...)` only returns `ACTIVE`.
  - delete is soft delete by status mutation.
- Because export semantics depend on which statuses are included, this needs
  confirmation before proposal or implementation.

## Risk Lane

- `normal`
- Why: this is one bounded API slice under the existing `/api/v1/users`
  surface. It changes a public API contract and needs validation around CSV
  formatting and role resolution, but it does not inherently require schema
  changes, auth model changes, or multi-domain decomposition.

## Validation Shape

- Unit: CSV row mapping for users with no group, empty roles, and multiple
  roles; header ordering; role-name join behavior.
- Integration: controller/service test for `GET /api/v1/users/export`
  validating permission guard, `Content-Type`, `Content-Disposition`, and CSV
  body shape.
- E2E: optional manual check through Swagger or curl once implementation
  exists.
- UAT: confirm exported rows and role strings match the database-backed user
  list for the chosen status scope.
- Platform: run `mvn test` and `scripts/harness verify ticket --id TASK-001`
  after implementation.

## Decomposition Check

- Yes. This is one reviewable endpoint/service/repository slice and should fit
  safely in a single ticket.

## Blocking Questions

- Which user statuses should the export include?
  - Recommended: all non-deleted users (`ACTIVE`, `TEMPORARY_BLOCKED`,
    `PERMANENT_BLOCKED`) to match the existing list-style behavior more closely
    than the single-user lookup path.
  - Option: only `ACTIVE` users.
  - Option: every row including `DELETED` users.
