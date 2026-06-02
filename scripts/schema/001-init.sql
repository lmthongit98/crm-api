-- Harness durable schema
-- Canonical installed schema for operational harness data.

PRAGMA journal_mode = WAL;
PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS schema_version (
    version     INTEGER PRIMARY KEY,
    applied_at  TEXT    NOT NULL DEFAULT (datetime('now'))
);

INSERT OR IGNORE INTO schema_version (version) VALUES (1);

CREATE TABLE IF NOT EXISTS ticket_workflow (
    ticket_id                 TEXT PRIMARY KEY,
    ticket_path               TEXT NOT NULL,
    status                    TEXT NOT NULL DEFAULT 'ticket_loaded'
                              CHECK(status IN (
                                'ticket_loaded',
                                'analysis_complete',
                                'proposal_pending_approval',
                                'proposal_approved',
                                'plan_pending_approval',
                                'plan_approved',
                                'implementation_complete',
                                'code_review_approved',
                                'uat_generated',
                                'blocked'
                              )),
    proposal_approved_at      TEXT,
    plan_approved_at          TEXT,
    code_review_approved_at   TEXT,
    created_at                TEXT NOT NULL DEFAULT (datetime('now')),
    updated_at                TEXT NOT NULL DEFAULT (datetime('now')),
    notes                     TEXT
);

CREATE INDEX IF NOT EXISTS idx_ticket_workflow_status
    ON ticket_workflow(status);

CREATE TABLE IF NOT EXISTS decision (
    id                    TEXT PRIMARY KEY,
    title                 TEXT NOT NULL,
    created_at            TEXT NOT NULL DEFAULT (datetime('now')),
    status                TEXT NOT NULL DEFAULT 'proposed'
                          CHECK(status IN (
                            'proposed','accepted','superseded','rejected'
                          )),
    doc_path              TEXT,
    verify_command        TEXT,
    last_verified_at      TEXT,
    last_verified_result  TEXT
                          CHECK(last_verified_result IN ('pass','fail') OR
                                last_verified_result IS NULL),
    predicted_impact      TEXT,
    actual_outcome        TEXT,
    notes                 TEXT
);

CREATE TABLE IF NOT EXISTS trace (
    id               INTEGER PRIMARY KEY AUTOINCREMENT,
    created_at       TEXT    NOT NULL DEFAULT (datetime('now')),
    task_summary     TEXT    NOT NULL,
    ticket_id        TEXT    REFERENCES ticket_workflow(ticket_id),
    agent            TEXT,
    actions_taken    TEXT,
    files_read       TEXT,
    files_changed    TEXT,
    decisions_made   TEXT,
    errors           TEXT,
    outcome          TEXT
                     CHECK(outcome IN (
                       'completed','blocked','partial','failed'
                     )),
    duration_seconds INTEGER,
    token_estimate   INTEGER,
    harness_friction TEXT,
    notes            TEXT
);
