INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO users (username,email, password, enabled) VALUES ('patrick','tarekbensassi@gmail.com', '$2a$10$cTUErxQqYVyU2qmQGIktpup5chLEdhD2zpzNEyYqmxrHHJbSNDOG.', '1');
INSERT INTO users_roles (user_id, role_id) VALUES (1, 1); -- user patrick has role USER
