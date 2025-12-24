INSERT INTO user_types (user_type) VALUES
    ('ADMIN'),
    ('TECHNICAL_GROUP');

INSERT INTO statuses (status) VALUES
    ('NEW'),
    ('ASSIGNED'),
    ('IN_PROGRESS'),
    ('PENDING');

INSERT INTO connection_types (connection_type) VALUES
    ('REMOTE'),
    ('WI_FI');

INSERT INTO issue_types (issue_level, parent_issue, name, insert_date) VALUES
    (0, NULL, 'Technical Issue', '2025-12-18 00:00:00'),
    (0, NULL, 'Software Bug', '2025-12-18 00:00:00');

INSERT INTO issue_types (issue_level, parent_issue, name, insert_date) VALUES
    (1, (SELECT id FROM issue_types WHERE name = 'Technical Issue' LIMIT 1), 'Connection Lost', '2025-12-18 00:00:00'),
    (1, (SELECT id FROM issue_types WHERE name = 'Technical Issue' LIMIT 1), 'Printer Error', '2025-12-18 00:00:00');

INSERT INTO users (login, password, name, email, id_user_type)
VALUES (
    'admin',
    '$2a$10$0TlCIGDE5mpEH9VgwAlrVuqQ2y7i436hJ4gtV4HNbAeOgSeu.I5Ge',
    'Admin Admin',
    'hello.world@iongroup.com',
    (SELECT id FROM user_types WHERE user_type = 'ADMIN')
);