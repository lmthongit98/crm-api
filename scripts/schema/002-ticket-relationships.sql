ALTER TABLE ticket_workflow RENAME TO ticket_workflow_old;

CREATE TABLE ticket_workflow (
    ticket_id                 TEXT PRIMARY KEY,
    ticket_path               TEXT NOT NULL,
    ticket_role               TEXT NOT NULL DEFAULT 'standalone'
                              CHECK(ticket_role IN (
                                'standalone',
                                'parent',
                                'child'
                              )),
    parent_ticket_id          TEXT,
    status                    TEXT NOT NULL DEFAULT 'ticket_loaded'
                              CHECK(status IN (
                                'ticket_loaded',
                                'analysis_complete',
                                'proposal_pending_approval',
                                'proposal_approved',
                                'decomposed',
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

INSERT INTO ticket_workflow (
    ticket_id,
    ticket_path,
    ticket_role,
    parent_ticket_id,
    status,
    proposal_approved_at,
    plan_approved_at,
    code_review_approved_at,
    created_at,
    updated_at,
    notes
)
SELECT
    ticket_id,
    ticket_path,
    'standalone',
    NULL,
    status,
    proposal_approved_at,
    plan_approved_at,
    code_review_approved_at,
    created_at,
    updated_at,
    notes
FROM ticket_workflow_old;

DROP TABLE ticket_workflow_old;

CREATE INDEX IF NOT EXISTS idx_ticket_workflow_status
    ON ticket_workflow(status);

CREATE INDEX IF NOT EXISTS idx_ticket_workflow_parent
    ON ticket_workflow(parent_ticket_id);
