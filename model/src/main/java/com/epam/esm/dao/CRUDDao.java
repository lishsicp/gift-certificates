package com.epam.esm.dao;

import com.epam.esm.exception.DaoException;

public interface CRUDDao<T> extends CRDDao<T> {
    void update(T e) throws DaoException;
}
