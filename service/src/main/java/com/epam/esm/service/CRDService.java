package com.epam.esm.service;

import com.epam.esm.service.exception.PersistentException;

import java.util.List;

public interface CRDService<T> {
    List<T> getAll(int page, int size);
    T getById(Long id) throws PersistentException;
    T save(T t) throws PersistentException;
    void delete(Long id) throws PersistentException;
}
