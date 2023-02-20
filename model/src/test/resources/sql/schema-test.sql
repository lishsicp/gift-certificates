create sequence if not exists tag_id_seq;
create sequence if not exists gift_certificate_id_seq;
create sequence if not exists order_id_seq;
create sequence if not exists user_id_seq;

drop table if exists gift_certificate CASCADE;
create table gift_certificate
(
    id               bigint    default nextval('gift_certificate_id_seq') primary key not null,
    name             varchar(128)                                                     not null,
    description      varchar(512),
    price            numeric(16, 2) check (price > 0)                                 not null,
    duration         integer                                                          not null,
    create_date      timestamp default (now() at time zone 'UTC+2')                   not null,
    last_update_date timestamp default (now() at time zone 'UTC+2')                   not null,
    UNIQUE (name)
);

drop table if exists tag CASCADE;
create table tag
(
    id   bigint default nextval('tag_id_seq') primary key not null,
    name varchar(128) not null,
    UNIQUE (name)
);

drop table if exists gift_certificate_tag CASCADE;
create table gift_certificate_tag
(
    gift_certificate_id int REFERENCES gift_certificate (id) ON UPDATE CASCADE ON DELETE CASCADE,
    tag_id              int REFERENCES tag (id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT gift_certificate_tag_pk PRIMARY KEY (gift_certificate_id, tag_id)
);

drop table if exists user_ CASCADE;
create table user_
(
    id   bigint
        default nextval('user_id_seq') primary key not null,
    name varchar(64) not null
);

drop table if exists order_ CASCADE;
create table order_
(
    id                  bigint default nextval('order_id_seq') primary key not null,
    cost                numeric(8, 2) check (cost > 0)                 not null,
    purchase_date       timestamp default (now() at time zone 'UTC+2') not null,
    gift_certificate_id serial REFERENCES gift_certificate (id) ON UPDATE CASCADE ON DELETE CASCADE,
    user_id             serial REFERENCES user_ (id) ON UPDATE CASCADE ON DELETE CASCADE
);
