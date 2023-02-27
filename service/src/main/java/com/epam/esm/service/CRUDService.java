package com.epam.esm.service;


/**
 * Base implementation of a CRUD service for business layer
 * @param <T> Entity type of the data which is a [Entity]
 */
public interface CRUDService<T> extends CRDService<T> {
    /**
     * Updates given entity
     *
     * @param id id of entity to update
     * @param entityToUpdate  Entity to update
     */
    void update(long id, T entityToUpdate);
}
