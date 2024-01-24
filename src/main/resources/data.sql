INSERT INTO role
VALUES (1, 'ADMIN')
ON CONFLICT DO NOTHING;
INSERT INTO role
VALUES (2, 'USER')
ON CONFLICT DO NOTHING;

INSERT INTO users
VALUES (1, 'vitekb650@gmail.com', '12345678', 1, true, true, true, true)
ON CONFLICT DO NOTHING;
INSERT INTO users
VALUES (2, 'vitekb651@gmail.com', '12345678', 2, true, true, true, true)
ON CONFLICT DO NOTHING;


