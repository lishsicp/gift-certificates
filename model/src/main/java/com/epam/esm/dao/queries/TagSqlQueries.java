package com.epam.esm.dao.queries;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TagSqlQueries {
    public final String GET_ALL = "SELECT * FROM tag";
    public final String GET_BY_ID = "SELECT * FROM tag WHERE id = ?";
    public final String GET_BY_NAME = "SELECT * FROM tag WHERE name = ?";
    public final String DELETE = "DELETE FROM tag WHERE id = ?";
    public final String INSERT = "INSERT INTO tag(name) VALUES(?)";
    public final String GET_TAGS_FOR_CERTIFICATE = "SELECT id, name FROM tag\n" +
            "         JOIN gift_certificate_tag ON tag.id = gift_certificate_tag.tag_id\n" +
            "WHERE gift_certificate_tag.gift_certificate_id = ?";
    public final String ADD_TAG_TO_CERTIFICATE = "INSERT INTO gift_certificate_tag VALUES (?, ?)";
    public final String DELETE_TAGS_FROM_CERTIFICATE = "DELETE FROM gift_certificate_tag WHERE gift_certificate_tag.gift_certificate_id = ?";
}
