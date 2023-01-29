INSERT INTO tag (name, operation)
VALUES ('tag1', 'INSERT'),
       ('tag2', 'INSERT'),
       ('tag3', 'INSERT'),
       ('tag4', 'INSERT'),
       ('tag5', 'INSERT');

INSERT INTO gift_certificate
    (name, description, price, duration, operation)
VALUES ('GiftCertificate1', 'Description', 500.00, 60, 'INSERT'),
       ('GiftCertificate2', 'Description', 200.00, 1, 'INSERT'),
       ('GiftCertificate3', 'Description', 300.00, 90, 'INSERT');

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
       ('User3', 'INSERT'),
       ('User4', 'INSERT');

INSERT INTO "order"(cost, gift_certificate_id, user_id, operation)
values (200, 1, 1, 'INSERT'),
       (500, 2, 1, 'INSERT'),
       (300, 3, 1, 'INSERT'),
       (200, 1, 2, 'INSERT'),
       (500, 2, 2, 'INSERT'),
       (300, 3, 2, 'INSERT'),
       (200, 1, 3, 'INSERT'),
       (500, 2, 3, 'INSERT'),
       (300, 3, 3, 'INSERT');
