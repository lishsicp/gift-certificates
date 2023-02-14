package com.epam.esm.service;


/**
 * Base implementation of a CRUD service for business layer
 * @param <T> Entity type of the data which is a [Entity]
 */
public interface CRUDService<T> extends CRDService<T> {
    /**
     * Updates given entity
     * @param t Entity to update
     */
    void update(T t);
}
