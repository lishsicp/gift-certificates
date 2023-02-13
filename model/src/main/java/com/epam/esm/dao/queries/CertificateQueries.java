package com.epam.esm.dao.queries;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CertificateQueries {
    public static final String GET_ALL = "SELECT * FROM gift_certificate";
    public static final String GET_BY_ID = "SELECT * FROM gift_certificate WHERE id = ?";
    public static final String DELETE_BY_ID = "DELETE FROM gift_certificate WHERE id = ?";
    public static final String UPDATE = "UPDATE gift_certificate SET ";
    public static final String INSERT = "INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date) VALUES (?,?,?,?,?,?)";
    public static final String GET_ALL_FILTERED = "SELECT DISTINCT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date FROM gift_certificate gc\n" +
            "                LEFT JOIN gift_certificate_tag gct on gc.id = gct.gift_certificate_id\n" +
            "                LEFT JOIN tag on tag.id = gct.tag_id";
}
