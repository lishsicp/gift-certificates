INSERT INTO tag (name)
VALUES ('birthday'),
       ('happy'),
       ('celebration'),
       ('christmas'),
       ('food'),
       ('presents'),
       ('gym'),
       ('health'),
       ('swimming pool');

INSERT INTO gift_certificate
    (name, description, price, duration)
VALUES ('Christmas Gift Certificate', 'Present this gift to acquire some valuable goods for your loved ones', 500.00,
        60),
       ('Birthday Gift Certificate', 'Free meal and cake from our partners', 200.00, 1),
       ('Gym Gift Certificate', '90-day premium gym subscription', 299.99, 90);

INSERT INTO gift_certificate_tag
VALUES (1, 2),
       (1, 3),
       (1, 4),
       (1, 6),
       (2, 1),
       (2, 2),
       (2, 3),
       (2, 5),
       (3, 7),
       (3, 8),
       (3, 9);

