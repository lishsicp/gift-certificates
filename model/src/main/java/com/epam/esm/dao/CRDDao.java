package com.epam.esm.dao;

import java.util.List;

public interface CRDDao<T> {
    List<T> getAll();
    T getById(Long id);
    void delete(T t);
    T create(T t);
}
