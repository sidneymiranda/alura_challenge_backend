INSERT INTO USERS (email, "password", username) VALUES ('sidneysmiranda@gmail.com', '$2a$12$x9MCvcYcJR4eFRCWu/2Nf.G64DbOB8rxcwbLb19BUr1hW5FRf/k2S', 'admin');

INSERT INTO "role" (id, name) values (1, 'ROLE_SUPER_ADMIN');
INSERT INTO "role" (id, name) values (2, 'ROLE_ADMIN');

INSERT INTO user_roles values (1, 1);
INSERT INTO user_roles values (1, 2);
