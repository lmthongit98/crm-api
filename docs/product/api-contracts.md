# API Contracts

This document summarizes the implemented HTTP contract exposed by the Spring
controllers in this repository. It is based on controller mappings, DTOs,
security configuration, and the actual error handling code.

## Contract Shape

- Base path: `/api/v1`
- Style: JSON REST endpoints, with one multipart upload flow for user avatars
  and one raw token response for login.
- Security: JWT bearer auth is required for most endpoints.
- Public endpoints:
  - `POST /api/v1/auth/login`
  - `GET /api/v1/users/file/{id}/{fileName}`
  - Swagger/OpenAPI endpoints under `/swagger-ui/**` and `/v3/api-docs/**`

## Authentication

`POST /api/v1/auth/login`

- Request body: `LoginDto` with `username` and `password`.
- Successful response: plain JWT string in the response body with `200 OK`.
- Invalid input: validation errors are returned as a list of messages with
  `400 BAD_REQUEST`.
- Authentication failure: the controller catches the error and returns the
  literal string `Username or password is not correct!` with `400 BAD_REQUEST`.

## Response And Error Conventions

Observed response patterns:

- Success responses are usually wrapped in `ResponseEntity`.
- Create operations generally return `201 CREATED`.
- Read operations generally return `200 OK`.
- Delete operations return no body.

Error responses:

- Validation failures in controller methods that explicitly check
  `BindingResult` return a JSON array of error messages.
- Domain errors such as not found, bad request, unauthorized, conflict, and
  method-not-allowed are mapped by `GlobalExceptionController` to:
  - `404 NOT_FOUND`
  - `400 BAD_REQUEST`
  - `401 UNAUTHORIZED`
  - `409 CONFLICT`
  - `405 METHOD_NOT_ALLOWED`
  - `500 INTERNAL_SERVER_ERROR`
- The standardized error body is `ErrorDetails` with:
  - `timestamp`
  - `message`
  - `details`

## Pagination And Search

The list endpoints for users and projects use the same query parameters:

- `searchKey`
- `pageNo` default `0`
- `pageSize` default `10`
- `sortBy` default `id`
- `sortDir` default `asc`

This pattern is implemented on:

- `GET /api/v1/users`
- `GET /api/v1/projects`

The response wrapper is `AbstractResponseDto<T>`, which includes page metadata
such as `pageNo`, `pageSize`, `totalElements`, `totalPages`, `last`, and a
`content` list.

## Domain Contracts

### Users

`/api/v1/users`

- `POST /with-avatar`: multipart-style create flow using request parameters
  `username`, `password`, `displayName`, `email`, and `file`.
- `POST /`: create a user from `UserRequestDto`.
- `PUT /{id}`: update a user from `UserToUpdateDto`.
- `PUT /change-password/{user-id}`: change password from `PasswordDto`.
- `GET /`: search users with pagination and sorting.
- `GET /{user-id}/roles`: fetch the user plus role list.
- `GET /file/{id}/{fileName}`: fetch avatar file content.
- `DELETE /?ids=1&ids=2...`: soft-delete users by id list.

Request and response notes:

- `UserRequestDto` enforces unique username and email via custom validators.
- `UserResponseDto` includes `id`, `username`, `displayName`, `email`,
  `status`, and `avatar`.
- Avatar URLs are returned in the response when an avatar is present.

### Groups

`/api/v1/groups`

- `GET /`: list groups.
- `GET /{group-id}`: fetch a group with roles.
- `POST /`: create a group from `GroupDto`.
- `PUT /{group-id}`: update a group from `GroupDto`.
- `POST /add-role/{group-id}/{role-id}`: attach a role.
- `POST /remove-role/{group-id}/{role-id}`: detach a role.
- `POST /add-user/{group-id}/{user-id}`: attach a user.
- `POST /remove-user/{group-id}/{user-id}`: detach a user.
- `DELETE /{id}`: delete a group.

Request and response notes:

- `GroupDto` requires `name` and `description`.
- `GroupWithRolesDto` expands a group with a set of `RoleDto`.

### Roles

`/api/v1/roles`

- `GET /`: list roles.
- `GET /{role-id}`: fetch one role.
- `POST /`: create a role from `RoleDto`.
- `PUT /{role-id}`: update a role from `RoleDto`.
- `DELETE /{id}`: delete a role.

Request and response notes:

- `RoleDto` carries a `Permission` enum as the role name plus a description.

### Projects

`/api/v1/projects`

- `GET /`: search projects with pagination and sorting.
- `GET /{id}`: fetch a project with member and task details.
- `POST /`: create a project from `ProjectRequestDto`.
- `PUT /{id}`: update a project from `ProjectRequestDto`.
- `POST /add-members/{project-id}`: add a list of user ids.
- `POST /remove-members/{project-id}`: remove a list of user ids.
- `DELETE /{id}`: delete a project.

Request and response notes:

- `ProjectRequestDto` currently has plain fields without validation annotations.
- `ProjectDetailResponseDto` expands the project with members and tasks.

### Tasks

`/api/v1/tasks`

- `GET /`: filter tasks by `assigneeIds`; when absent or empty, all tasks are
  returned.
- `GET /{id}`: fetch one task.
- `POST /`: create a task from `TaskRequestDto`.
- `PUT /{id}`: update a task from `TaskRequestDto`.
- `DELETE /{id}`: delete a task.

Request and response notes:

- `TaskRequestDto` requires `projectId` and `name`.
- `TaskResponseDto` expands the task with assignee and comments.

### Comments

`/api/v1/comments`

- `POST /`: add a comment from `CommentRequestDto`.
- `PUT /{id}`: edit comment body.
- `DELETE /{id}`: delete a comment.

Request and response notes:

- `CommentRequestDto` requires `userId`, `taskId`, and `body`.
- The edit endpoint accepts a raw string body rather than a DTO.

## Security Requirements By Route Group

- Auth: public login only.
- Users, groups, roles, projects, tasks, comments: authenticated plus a
  permission check through `@HasAnyPermissions`.
- The permission names come from `Permission` enum values and are enforced by
  the authorization aspect rather than method-specific Spring annotations.

## Inconsistencies And Unclear Areas

The contract is not fully consistent across controllers:

- Some create and update endpoints return `201 CREATED`, while others return
  `200 OK`.
- `PUT /api/v1/tasks/{id}` and `PUT /api/v1/comments/{id}` return `201 CREATED`
  even though they are update operations.
- `POST /api/v1/users` declares a `MultipartFile file` parameter, but the
  method does not use it; the separate `/with-avatar` flow is the only clear
  upload contract.
- `GET /api/v1/users/file/{id}/{fileName}` always advertises `image/jpeg`,
  even though the stored file type is whatever was uploaded.
- Some endpoints use `BindingResult` for manual validation handling and others
  rely on global exception handling or validation annotations only.
- `ProjectRequestDto` has no validation annotations, so its create/update
  contract is looser than the user, group, task, role, and comment DTOs.

