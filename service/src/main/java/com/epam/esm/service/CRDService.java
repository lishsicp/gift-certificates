package com.epam.esm.service;

import java.util.List;

/**
 * Base interface of a CRD service for business layer
 * @param <T> Entity type of the data which is a [Entity]
 */
public interface CRDService<T> {
    /**
     * Finds all entities
     * @return A {@link List} of {@link T} objects.
     */
    List<T> getAll();

    /**
     * Finds an entity with given id
     *
     * @param id Id of the entity to find
     * @return Found entity
     */
    T getById(long id);

    /**
     * Saves given entity, used when creating a new entity
     *
     * @param entityToSave Entity to save
     * @return Saved entity
     */
    T save(T entityToSave);

    /**
     * Deletes an entity by given id.
     *
     * @param id Id of the entity to remove
     */
    void delete(long id);
}
