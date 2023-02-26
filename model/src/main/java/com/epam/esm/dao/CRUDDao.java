package com.epam.esm.dao;


/**
 * Base interface of a CRUD repository for data layer.
 * @param <T> Entity type of the data which is a [Entity]
 */
public interface CRUDDao<T> extends CRDDao<T> {
    /**
     * Updates given entity
     * @param entityToUpdate Entity to update
     */
    void update(T entityToUpdate);
}
