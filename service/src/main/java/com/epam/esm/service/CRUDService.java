package com.epam.esm.service;


public interface CRUDService<T> extends CRDService<T> {
    void update(T t);
}
