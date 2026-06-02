# Project Profile

This file customizes the portable Harness protocol for this repository. Replace
these values when installing Harness into another brownfield project.

## Project

- Name: CRM Project
- Type: Brownfield backend API/service
- Primary language: Java 17
- Runtime: Spring Boot 3.0.0 on the JVM
- Build tool: Maven
- Database: MySQL (`crm_db` in local and Docker profiles)
- Public API prefix: `/api/v1`

## Source Layout

- Application source: `src/main/java`
- Test source: `src/test/java`
- Runtime config: `src/main/resources/application.yml`, `src/main/resources/application-dev.yml`, `src/main/resources/application-docker.yml`
- Product docs: `docs/product/`
- Ticket requirements: `docs/tickets/`
- Work packets: `docs/work/`
- Harness templates: `docs/templates/`

## Validation Commands

- Quick/default: `mvn test`
- Targeted unit or integration: `mvn -Dtest=<TestClass> test`
- Harness state check: `scripts/harness verify ticket --id <ticketId>`
- Full Harness audit: `scripts/harness doctor`

## Risk Overrides

Treat these surfaces as high-risk unless a ticket explicitly narrows scope:

- Authentication, session, token, JWT, or credential handling.
- Authorization, role, permission, or tenant-scope behavior.
- Database schema, persistence mapping, deletion, retention, or uniqueness rules.
- File upload and storage behavior, including `app.file-upload.root-path`.
- External provider or infrastructure integration behavior.
- Public API request or response shape changes, especially under `api/v1`.
- Secret handling, audit logging, privacy, or security enforcement.

## Evidence Notes

- The application uses Spring Web, Spring Data JPA, Spring Security,
  validation, JJWT, and springdoc-openapi.
- Security is JWT-based and the repo contains custom authorization around
  permissions and role/group access.
- The repo has one Spring Boot context test at
  `src/test/java/com/crm/CrmProjectApplicationTests.java`; no broader test
  suite was found in the current tree.
- `src/main/resources/application.yml` defaults to the `dev` profile and the
  repo includes a Docker-specific profile for MySQL host `mysqldocker`.
- `HELP.md` references a generated package-name note that does not match the
  actual source tree; the source tree under `src/main/java/com/crm` is the
  repo truth used here.

## Agent Adapter

Codex uses `.codex/skills/*` and optional `.codex/agents/*` as the current
adapter. Other agents should follow `docs/AGENT_PROTOCOL.md` and map their own
phase prompts to the same artifacts and gate checks.
