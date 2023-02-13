package com.epam.esm.service;

import java.util.List;

public interface CRDService<T> {
    List<T> findAll();
    T findById(Long id);
    T save(T t);
    void delete(Long id);
}
