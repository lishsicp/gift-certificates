package com.epam.esm.service;

import com.epam.esm.exception.DaoException;

public interface CRUDService<T> extends CRDService<T> {
    void update(T t) throws DaoException;
}
