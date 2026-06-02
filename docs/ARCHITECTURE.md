# Architecture

This document records the current implementation truth for the repository. It is
not an ideal target architecture and should stay aligned to the code that is
actually present.

## Current Stack

- Language and runtime: Java 17 on Spring Boot 3.0.0.
- Build tool: Maven.
- Application shape: single-module Spring Boot backend service.
- Primary persistence: Spring Data JPA with MySQL.
- API documentation: springdoc-openapi with Swagger UI.
- Security: Spring Security with JWT-based authentication and custom
  permission checks.

## Package Structure

The code is organized under `com.crm` with conventional Spring layers:

- `controller/`: REST controllers for auth, users, groups, roles, projects,
  tasks, comments, and global exception handling.
- `service/` and `service/impl/`: service interfaces and concrete service
  implementations.
- `repository/`: Spring Data JPA repositories.
- `model/`: JPA entities and the shared `BaseEntity`.
- `dto/`: request, response, and view DTOs.
- `config/`: JPA auditing setup and auditor resolution.
- `security/`: JWT filter/provider, security configuration, authorization
  aspect, permissions, and the `@HasAnyPermissions` annotation.
- `validation/`: custom validation annotations and validators.
- `common/`: constants, enums, and utility helpers.
- `exception/`: application-specific exceptions.

The repo does not show separate Gradle/Maven multi-module boundaries or a
hexagonal/DDD package split.

## Runtime Shape

The runtime is a request-driven REST API:

```text
HTTP request
  -> Spring Security filter chain
  -> JWT token extraction and validation
  -> controller endpoint
  -> request DTO / validation
  -> service implementation
  -> JPA repository
  -> MySQL
  -> DTO mapping / response
```

For secured endpoints, the request is first evaluated by `JwtAuthenticationFilter`.
`AuthServiceImpl` creates tokens during login, and `AuthorizationAspect`
enforces method-level permission checks through `@HasAnyPermissions`.

## Domain Areas

The main business areas visible in code are:

- Users and authentication.
- Groups and roles.
- Projects and members.
- Tasks and assignees.
- Comments on tasks.

These areas are exposed through REST controllers and backed by JPA entities and
repositories.

## Data And Persistence

The persistence boundary is MySQL through JPA/Hibernate.

Observed entity model:

- `User`, `Group`, `Role`, `Project`, `Task`, and `Comment`.
- Shared audit fields live in `BaseEntity`.
- `JpaConfig` enables auditing, and `AuditorAwareImpl` resolves the current
  user from the Spring Security context.
- Repository methods use derived queries and a few explicit JPQL queries for
  search and fetch joins.

Observed persistence behavior:

- Users are soft-deleted by status rather than physically removed.
- Projects, groups, and tasks are deleted through repository delete operations
  where the service chooses hard deletion.
- Unique constraints exist at the entity level for identifiers such as username,
  email, group name, project name, and role name.

## External Boundaries

The repository currently shows these external boundaries:

- Database: MySQL via `spring.datasource` and `mysql-connector-j`.
- File system: user avatar storage under `app.file-upload.root-path`, with
  local and Docker-specific profile values.
- Runtime configuration: `application.yml`, `application-dev.yml`, and
  `application-docker.yml`.
- API documentation surface: Swagger UI and OpenAPI docs.

The repository does not currently show messaging, email delivery, background
job schedulers, caches, or third-party provider integrations beyond JWT and the
OpenAPI dependency.

## Web And API Surface

The public API lives under `api/v1`.

Observed controller groups:

- `/api/v1/auth`
- `/api/v1/users`
- `/api/v1/groups`
- `/api/v1/roles`
- `/api/v1/projects`
- `/api/v1/tasks`
- `/api/v1/comments`

The controllers use request DTOs, validation annotations, and response DTOs
instead of exposing entities directly for most operations.

## Security And Authorization

Security is stateful only at the token level and stateless at the HTTP session
level.

Observed behavior:

- `/api/v1/auth/login` is publicly accessible.
- Swagger UI and OpenAPI JSON endpoints are publicly accessible.
- User avatar file downloads are publicly accessible.
- All other endpoints require authentication.
- Method-level permissions are enforced through `@HasAnyPermissions` and
  `AuthorizationAspect`.
- JWT tokens are extracted from the `Authorization: Bearer ...` header.
- Passwords are encoded with BCrypt.

This is a real implementation detail, not a claim that the authorization model
is complete or ideal.

## File Storage

User avatars are handled outside the database.

Observed flow:

```text
multipart upload
  -> user controller
  -> user service save/update
  -> filesystem path under app.file-upload.root-path
  -> file copy via FileUploadUtil
  -> avatar URL returned in user response
```

This is the only explicit file-storage boundary currently visible in the repo.

## Validation Ladder

Use the smallest proof that covers the touched behavior:

- Unit: helpers, validators, DTO mapping, and isolated business rules.
- Integration: controllers, repositories, security behavior, persistence, and
  filesystem interactions.
- Platform or release: runtime configuration, database connectivity, and file
  storage behavior.

## Boundary Rules

- Keep HTTP parsing, request validation, and status mapping in controllers.
- Keep business rules and cross-entity logic in services.
- Keep persistence access in repositories.
- Treat MySQL, the filesystem, JWT handling, and runtime config as external
  boundaries.
- Do not change public API shape, persistence behavior, or authorization rules
  without tracking the change through the Harness workflow and proof updates.

## Unclear Areas

The following areas need human review if future work depends on them:

- Whether the `AuthorizationAspect` permission model is the final intended
  access-control design or a transitional implementation.
- Whether filesystem avatar storage is intended to remain local-only or to be
  replaced by a provider-backed storage integration later.
- Whether `Project` and `Group` deletions are intentionally hard deletes while
  `User` uses soft delete by status.

