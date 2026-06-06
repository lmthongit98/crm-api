# Review

## Summary of changes

- Implemented `GET /api/v1/users/export` returning a downloadable CSV (`users-export.csv`) for all non-deleted users.
- Added repository method `UserRepository.findAllNonDeletedWithGroupRoles()` to bulk-fetch users with group and roles to avoid N+1 queries.
- Added `UserService.exportAllNonDeletedUsers()` and its implementation mapping to `UserExportRow` DTO.
- Added `CsvUtils` utility to render CSV rows with basic escaping.
- Updated `implementation-plan.md` with concrete steps and test plan.

## Files changed

- `src/main/java/com/crm/controller/UserController.java` — added `/export` endpoint
- `src/main/java/com/crm/repository/UserRepository.java` — added bulk fetch query
- `src/main/java/com/crm/service/UserService.java` — added export method signature
- `src/main/java/com/crm/service/impl/UserServiceImpl.java` — implemented export logic
- `src/main/java/com/crm/dto/UserExportRow.java` — new DTO for export rows
- `src/main/java/com/crm/common/util/CsvUtils.java` — new CSV helper
- `docs/work/TASK-001-import-users-from-csv/implementation-plan.md` — populated plan

## Validation performed

- Build (packaging) succeeded: `./mvnw -DskipTests package` ✅
- Unit/integration tests executed: `./mvnw test` — all existing tests passed ✅

## Review notes and focus areas

- Performance: bulk fetch uses `LEFT JOIN FETCH` and `DISTINCT` to avoid duplicates; verify for large datasets and consider batching streaming if needed.
- CSV formatting: `CsvUtils` implements basic RFC4180 quoting; validate with consumer apps for edge cases.
- Security: endpoint is guarded by `Permission.USER_VIEW`. Confirm that exporting these columns is acceptable under privacy policy.

## How to review

1. Check the PR: https://github.com/lmthongit98/crm-api/pull/new/feature/TASK-001-users-export
2. Run locally:

```bash
git fetch origin && git checkout feature/TASK-001-users-export
./mvnw test
# or run the app and curl the endpoint as an authenticated user with USER_VIEW permission
```

## Suggested next steps

- Merge PR after review and run a manual export against a staging dataset to confirm format and performance.
- After merging, run `scripts/harness ticket gate --id TASK-001 --gate code_review` and follow the review workflow.

