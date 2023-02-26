INSERT INTO tag (name) VALUES ('tag1'), ('tag2'), ('tag3');

INSERT INTO gift_certificate
(name, description, price, duration, create_date, last_update_date)
VALUES
    ('gift1', 'description1', 10, 60, '2023-02-17T10:48:40.303950', '2023-02-17T10:48:40.303950'),
    ('gift2', 'description2', 10, 60, '2023-02-17T10:48:40.303950', '2023-02-17T10:48:40.303950');

INSERT INTO gift_certificate_tag VALUES (1,1), (1,2), (1,3), (2, 3), (2,2);

