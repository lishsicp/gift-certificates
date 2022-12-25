package com.epam.esm.dao;


public interface CRUDDao<T> extends CRDDao<T> {
    T update(T e);
}
