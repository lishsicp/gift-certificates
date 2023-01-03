package com.epam.esm.dao.queries;

public class TagQuery {
    public static final String GET_ALL = "SELECT * FROM tag";
    public static final String GET_BY_ID = "SELECT * FROM tag WHERE id = ?";
    public static final String GET_BY_NAME = "SELECT * FROM tag WHERE name = ?";
    public static final String DELETE = "DELETE FROM tag WHERE id = ?";
    public static final String INSERT = "INSERT INTO tag(name) VALUES(?)";
}
