package com.epam.esm.dao;


import java.util.List;

public interface CRDDao<T> {
    List<T> getAll();
    T getById(long id);
    void remove(long id);
    T create(T t);
}
