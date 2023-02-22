INSERT INTO tag (name)
VALUES ('tag1'),
       ('tag2'),
       ('tag3'),
       ('tag4'),
       ('tag5');

INSERT INTO gift_certificate
    (name, description, price, duration, create_date, last_update_date)
VALUES ('GiftCertificate1', 'Description', 500.00, 60, '2022-12-15T11:43:33', '2022-12-15T11:43:33'),
       ('GiftCertificate2', 'Description', 200.00, 1, '2023-01-25T13:56:30', '2023-01-25T13:56:30'),
       ('GiftCertificate3', 'Description', 300.00, 90, '2023-01-30T09:08:56', '2023-01-30T09:08:56');

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

INSERT INTO user_(name)
values ('User1'),
       ('User2'),
       ('User3');

INSERT INTO order_(cost, gift_certificate_id, user_id, purchase_date)
values (200, 1, 1, '2023-01-29 10:42:20.57'),
       (500, 2, 1, '2023-01-29 10:42:20.57'),
       (300, 3, 1, '2023-01-29 10:42:20.57'),
       (200, 1, 2, '2023-01-29 10:42:20.57'),
       (500, 2, 2, '2023-01-29 10:42:20.57'),
       (300, 3, 2, '2023-01-29 10:42:20.57'),
       (200, 1, 3, '2023-01-29 10:42:20.57'),
       (500, 2, 3, '2023-01-29 10:42:20.57'),
       (300, 3, 3, '2023-01-29 10:42:20.57');
