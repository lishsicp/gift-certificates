package com.epam.esm.service;

import com.epam.esm.service.exception.PersistentException;

public interface CRUDService<T> extends CRDService<T> {
    T update(Long id, T t) throws PersistentException;
}
