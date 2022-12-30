package com.epam.esm.dao;

public interface CRUDDao<T> extends CRDDao<T> {
    void update(T e);
}
