INSERT INTO users (username, password, enabled) VALUES ('user', '{noop}password', true);
INSERT INTO authorities (username, authority) VALUES ('user', 'ROLE_USER');
