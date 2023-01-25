package com.epam.esm.dao;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CRDDao<T> {
    List<T> getAll(Pageable page);
    Optional<T> getById(Long id);
    void delete(Long id);
    T create(T t);
}
