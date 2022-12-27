package com.epam.esm.service;

import com.epam.esm.entity.Tag;

import java.util.List;

public interface CRDService<T> {
    List<T> findAll();
    T findById(Long id);
    Tag save(T t);
    void delete(Long id);
}
