INSERT INTO roles (id, name) VALUES (1, 'admin') ON CONFLICT (id) DO NOTHING;
INSERT INTO roles (id, name) VALUES (2, 'user') ON CONFLICT (id) DO NOTHING;
