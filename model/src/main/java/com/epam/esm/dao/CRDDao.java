package com.epam.esm.dao;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CRDDao<T> {
    List<T> getAll(Pageable page);
    T getById(Long id);
    void delete(T t);
    T create(T t);
}
