package com.epam.esm.service;

import com.epam.esm.service.exception.IncorrectUpdateValueException;

public interface CRUDService<T> extends CRDService<T> {
    void update(T t) throws IncorrectUpdateValueException;
}
