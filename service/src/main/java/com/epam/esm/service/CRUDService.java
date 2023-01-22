package com.epam.esm.service;

import com.epam.esm.exception.DaoException;
import com.epam.esm.service.exception.IncorrectUpdateValueException;

public interface CRUDService<T> extends CRDService<T> {
    T update(T t) throws DaoException, IncorrectUpdateValueException;
}
