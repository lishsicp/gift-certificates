package com.epam.esm.service;

/**
 * This interface provides a base implementation of a CRUD service for the business layer.
 * It extends the CRDService interface and adds a method for updating an entity.
 *
 * @param <T> the type of entity managed by this service
 */
public interface CRUDService<T> extends CRDService<T> {

    /**
     * Updates the entity with the given ID.
     *
     * @param id the ID of the entity to update
     * @param entityToUpdate an entity to update
     * @return the updated entity
     */
    T update(long id, T entityToUpdate);
}
