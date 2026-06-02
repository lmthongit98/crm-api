# Data Model

This repository uses a small Spring Data JPA domain model backed by MySQL. The
model is business-relevant, but it is not especially large or schema-heavy.
The important parts are the entity relationships, ownership rules, uniqueness
constraints, and delete behavior.

## Main Entities

- `User`: application account and profile record.
- `Group`: organizational grouping used to bundle users and roles.
- `Role`: permission-bearing role record.
- `Project`: project container with members and tasks.
- `Sprint`: project-owned sprint record for time-boxed delivery planning.
- `Task`: task record tied to a project and optional assignee.
- `Comment`: task comment authored by a user.

All of these inherit audit fields and optimistic locking from `BaseEntity`
(`id`, `version`, created/modified timestamps, created/modified by).

## Relationship Model

Observed ownership and cardinality:

- A `User` belongs to zero or one `Group`.
- A `Group` has many `User` records.
- A `Group` has many `Role` records through the `group_role` join table.
- A `Project` has many `Task` records and owns that relationship.
- A `Project` has many `Sprint` records and owns that relationship.
- A `Project` has many `User` members through the `project_user` join table.
- A `Sprint` belongs to one `Project`.
- A `Sprint` has many `Task` records through an optional task-side sprint link.
- A `Task` belongs to one `Project`.
- A `Task` may belong to zero or one `Sprint`.
- A `Task` may have one assigned `User`.
- A `Task` has many `Comment` records.
- A `Comment` belongs to one `Task` and one `User`.
- A `Role` is represented as a permission enum value rather than an arbitrary
  free-text code.

The code uses helper methods on `Group` and `Project` to keep many-to-many
relationships consistent in memory when adding or removing members/roles.

## Uniqueness And Required Data

Visible uniqueness constraints are defined in entity annotations and custom
validators:

- `User.username` is unique.
- `User.email` is unique.
- `Group.name` is unique.
- `Project.name` is unique.
- `Sprint.name` is unique within one project.
- `Role.name` is unique.
- `UserRequestDto` also validates username and email uniqueness before save.
- `GroupDto` validates group-name uniqueness before save.

Required fields are enforced mostly at the entity and DTO level:

- Users require username, password, display name, email, and status at create
  time.
- Groups require name and description.
- Projects require name, description, and type in the entity model, but the
  request DTO is looser and does not add validation annotations.
- Tasks require name, type, priority, status, and project association in the
  entity model; the request DTO enforces only project id and name directly.
- Sprints require name, status, and project association; `goal`, `startDate`,
  and `endDate` are optional in the persistence model.
- Comments require body, user, and task.

## Deletion Behavior

Deletion is mixed and should be treated carefully:

- `User` is soft-deleted by setting `status = DELETED`.
- `Project`, `Group`, `Role`, `Task`, and `Comment` are deleted through JPA
  repository delete calls.
- `Sprint` is modeled as a project-owned record and will be removed with its
  owning project through JPA cascade from `Project`.
- `Project.tasks` is configured with cascade delete and orphan removal, so task
  rows are removed when the owning project is deleted.
- `Project.sprints` is configured with cascade delete and orphan removal, so
  sprint rows are removed when the owning project is deleted.
- `Task.sprint` is nullable so backlog work can exist without sprint
  assignment.
- `Comment` rows are removed when the comment itself is deleted, but no cascade
  delete path from `Task` to `Comment` is explicitly configured.
- `Group` and `Role` deletion logic manually clears relationships before
  deleting the row.

## Retention-Sensitive Data

The main sensitive data and retention areas are:

- User passwords, which are stored as BCrypt hashes.
- User avatars, which are stored on the local filesystem rather than in the
  database.
- Audit metadata such as who created or last modified a row.
- JWT secret and runtime connection settings, which live in application config
  rather than the entity model.

## Migration-Sensitive Areas

The repository does not show a rich migration history in the application tree;
the only schema file present is the Harness operational schema under
`scripts/schema/`, not app DDL migrations.

Areas that would be migration-sensitive if changed:

- Unique constraints on `User`, `Group`, `Project`, and `Role`.
- Enum-backed columns for `UserStatus`, `ProjectType`, `TaskType`, `Priority`,
  `TaskStatus`, `SprintStatus`, and `Permission`.
- Relationship tables `group_role` and `project_user`.
- Nullable versus non-nullable foreign keys for sprint, task, comment, and group
  relationships.
- Soft-delete semantics on `User.status`.
- Audit columns inherited from `BaseEntity`.

## Service-Level Rules That Matter To Data

- `UserServiceImpl` enforces username and email uniqueness before updates.
- `UserServiceImpl` uses status filtering so only active users are returned by
  most lookup paths.
- `TaskServiceImpl` attaches a task to a project and optionally to an assignee.
- `ProjectServiceImpl` manages project membership through the project-user join
  table.
- `GroupServiceImpl` manages group membership and role assignment through the
  group-user and group-role relationships.
- `CommentServiceImpl` requires both a task and a user when creating a comment.

## Practical Note

This is a straightforward CRUD-style domain model. There is no sign of event
outbox tables, read models, history tables, or separate reporting schemas in
the repo.
