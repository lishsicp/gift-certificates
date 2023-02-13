package com.epam.esm.dao;

import com.epam.esm.exception.DaoException;

import java.util.List;

public interface CRDDao<T> {
    List<T> getAll();
    T getById(long id) throws DaoException;
    void remove(long id) throws DaoException;
    T create(T t) throws DaoException;
}
