package com.epam.esm.dao;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CRDDao<T> {
    List<T> findAll(Pageable page);
    Optional<T> findById(Long id);
    void delete(Long id);
    T save(T t);
}
