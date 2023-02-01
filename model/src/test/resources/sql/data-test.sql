INSERT INTO tag (name, operation)
VALUES ('tag1', 'INSERT'),
       ('tag2', 'INSERT'),
       ('tag3', 'INSERT'),
       ('tag4', 'INSERT'),
       ('tag5', 'INSERT');

INSERT INTO gift_certificate
    (name, description, price, duration, operation, create_date, last_update_date)
VALUES ('GiftCertificate1', 'Description', 500.00, 60, 'INSERT','2022-12-15T11:43:33', '2022-12-15T11:43:33'),
       ('GiftCertificate2', 'Description', 200.00, 1,'INSERT','2023-01-25T13:56:30', '2023-01-25T13:56:30'),
       ('GiftCertificate3', 'Description', 300.00, 90,'INSERT', '2023-01-30T09:08:56', '2023-01-30T09:08:56');

INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id)
VALUES (1, 2);

INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id)
VALUES (2, 2);

INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id)
VALUES (3, 4);

INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id)
VALUES (3, 2);

INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id)
VALUES (2, 4);

INSERT INTO "user"(name, operation)
values ('User1', 'INSERT'),
       ('User2', 'INSERT'),
       ('User3', 'INSERT');

INSERT INTO "order"(cost, gift_certificate_id, user_id, purchase_date, operation)
values (200, 1, 1, '2023-01-29 10:42:20.57','INSERT'),
       (500, 2, 1, '2023-01-29 10:42:20.57','INSERT'),
       (300, 3, 1, '2023-01-29 10:42:20.57','INSERT'),
       (200, 1, 2, '2023-01-29 10:42:20.57','INSERT'),
       (500, 2, 2, '2023-01-29 10:42:20.57','INSERT'),
       (300, 3, 2, '2023-01-29 10:42:20.57','INSERT'),
       (200, 1, 3, '2023-01-29 10:42:20.57','INSERT'),
       (500, 2, 3, '2023-01-29 10:42:20.57','INSERT'),
       (300, 3, 3, '2023-01-29 10:42:20.57','INSERT');
