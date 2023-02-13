package com.epam.esm.service;

import java.util.List;

public interface CRDService<T> {
    List<T> findAll();
    T findById(long id);
    T save(T t);
    void delete(long id);
}
