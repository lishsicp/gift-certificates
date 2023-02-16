alter table gift_certificate_tag
    drop constraint gift_certificate_has_tags_certificate_fk,
    drop constraint gift_certificate_has_tags_tag_fk;

drop table gift_certificate_tag;

drop table gift_certificate;

drop table tag;
