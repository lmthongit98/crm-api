# Product Overview

This repository is a backend API for a CRM-style internal management system.
It is primarily a technical Spring Boot service rather than a user-facing
product with a separate frontend in this repo.

The implemented API covers these main areas:

- Authentication via `POST /api/v1/auth/login`, which returns a JWT.
- User management under `/api/v1/users`, including creation, update, search,
  password change, soft deletion by status, role lookup, and avatar upload/
  retrieval.
- Group management under `/api/v1/groups`, including CRUD plus assigning and
  removing users and roles.
- Role management under `/api/v1/roles`.
- Project management under `/api/v1/projects`, including search, CRUD, and
  member assignment.
- Task management under `/api/v1/tasks`, including CRUD and filtering by
  assignee.
- Comment management under `/api/v1/comments` for task comments.

The product model is centered on users, groups, roles, projects, tasks, and
comments. Access is controlled with JWT authentication plus permission-based
checks on endpoints. The only clearly implemented storage boundary outside the
database is local filesystem storage for user avatar files.

There is no evidence in the repo of additional product surfaces such as a UI,
messaging, email delivery, scheduled jobs, or third-party business
integrations.
