package com.epam.esm.service;

import com.epam.esm.exception.DaoException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CRDService<T> {
    List<T> findAll();
    T findById(Long id) throws DaoException;
    T save(T t) throws DaoException;
    void delete(Long id) throws DaoException;
}
