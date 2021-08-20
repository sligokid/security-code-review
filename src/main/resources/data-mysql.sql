INSERT INTO role(name) VALUES('ROLE_USER');
INSERT INTO role(name) VALUES('ROLE_ADMIN');

-- Serenity test user
INSERT INTO `user` (created_at, updated_at, email, name, password, username) VALUES('2018-08-20 16:03:16', '2018-08-20 16:03:16', 'serenity@overstock.com', 'Serenity Test User', '$2a$10$D5RDhS.38/uIKmRFRVmN6OAc3wS3i0g/knFp408YCfStrZT6554fy', '_serenity_');
INSERT INTO user_role(user_id, role_id) VALUES(1, 1);
-- demo seller
INSERT INTO `user` (created_at, updated_at, email, name, password, username) VALUES('2018-08-20 16:03:16', '2018-08-20 16:03:16', 'hbogsora@gmail.com', 'Hakizimiana Bogsora', '$2a$10$9KSu0wrBqNn9jY9pi1CePutFLvqzqoO297dQbm9jdcojVDN29ptZ.', 'hbogsora');
INSERT INTO user_role(user_id, role_id) VALUES(2, 1);

-- demo buyer
INSERT INTO `user` (created_at, updated_at, email, name, password, username) VALUES('2018-08-20 16:03:16', '2018-08-20 16:03:16', 'rtwagiramungu@gmail.com', 'Runihura Twagiramungu', '$2a$10$ADcBEYRAqLakJk0Numguwe/CknvzyqRywBISdRb/hJ.73mqCjfeYW', 'rtwagiramungu');
INSERT INTO user_role(user_id, role_id) VALUES(3, 1);

