package com.epam.esm.dao;

import java.util.List;
import java.util.Optional;

public interface CRDDao<T> {
    List<T> getAll();
    Optional<T> getById(Long id);
    void remove(Long id);
    T create(T t);
}
