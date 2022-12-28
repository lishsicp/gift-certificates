drop table if exists gift_certificate;
create table gift_certificate
(
    id               serial                                     not null
        constraint gift_certificate_pk primary key,
    name             varchar(128) unique                        not null,
    description      varchar(512),
    price            numeric(16, 2)                             not null,
    duration         integer                                    not null,
    create_date      timestamp default (now() at time zone 'UTC') not null,
    last_update_date timestamp default (now() at time zone 'UTC') not null
);

drop table if exists tag;
create table tag
(
    id   serial
        constraint tag_pk
            primary key,
    name varchar(128) not null
);

DROP TABLE if exists gift_certificate_tag;
CREATE TABLE gift_certificate_tag
(
    gift_certificate_id int REFERENCES gift_certificate (id) ON UPDATE CASCADE ON DELETE CASCADE,
    tag_id              int REFERENCES tag (id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT gift_certificate_tag_pk PRIMARY KEY (gift_certificate_id, tag_id)
);