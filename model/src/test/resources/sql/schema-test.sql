drop table if exists gift_certificate CASCADE;
create table if not exists gift_certificate
(
    id               serial                                         not null
        constraint gift_certificate_pk primary key,
    name             varchar(128)                                   not null,
    description      varchar(512),
    price            numeric(16, 2) check (price > 0)               not null,
    duration         integer                                        not null,
    create_date      timestamp default (now() at time zone 'UTC+2') not null,
    last_update_date timestamp default (now() at time zone 'UTC+2') not null,
    UNIQUE (name),
    operation        varchar(255)                                   not null,
    timestamp        bigint                                         not null default 11111111
);

drop table if exists tag CASCADE;
create table if not exists tag
(
    id        serial
        constraint tag_pk
            primary key,
    name      varchar(128) not null,
    UNIQUE (name),
    operation varchar(255) not null,
    timestamp bigint       not null default 11111111
);

drop table if exists gift_certificate_tag CASCADE;
create table if not exists gift_certificate_tag
(
    gift_certificate_id int REFERENCES gift_certificate (id) ON UPDATE CASCADE ON DELETE CASCADE,
    tag_id              int REFERENCES tag (id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT gift_certificate_tag_pk PRIMARY KEY (gift_certificate_id, tag_id)
);

drop table if exists "user" CASCADE;
create table if not exists "user"
(
    id        serial
        constraint user_pk
            primary key,
    name      varchar(64)  not null,
    operation varchar(255) not null,
    timestamp bigint       not null default 11111111
);

drop table if exists "order" CASCADE;
create table if not exists "order"
(
    id                  serial                         not null
        constraint order_pk primary key,
    cost                numeric(8, 2) check (cost > 0) not null,
    purchase_date       timestamp                               default (now() at time zone 'UTC+2') not null,
    gift_certificate_id serial REFERENCES gift_certificate (id) ON UPDATE CASCADE ON DELETE CASCADE,
    user_id             serial REFERENCES "user" (id) ON UPDATE CASCADE ON DELETE CASCADE,
    operation           varchar(255)                   not null,
    timestamp           bigint                         not null default 11111111
);
