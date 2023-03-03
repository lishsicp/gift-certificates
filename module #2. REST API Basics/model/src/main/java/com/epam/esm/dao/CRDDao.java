package com.epam.esm.dao;


import java.util.List;
import java.util.Optional;

/**
 * Base interface of a CRD repository for data layer.
 *
 * @param <T> Entity type of the data which is a [Entity]
 */
public interface CRDDao<T> {
    /**
     * Finds all entities
     * @return A {@link List} of {@link T} objects.
     */
    List<T> findAll();

    /**
     * Finds an entity with given id
     *
     * @param id Id of the entity to find
     * @return Found entity
     */
    Optional<T> findById(long id);

    /**
     * Removes an entity by given id.
     *
     * @param id Id of the entity to remove
     */
    void delete(long id);

    /**
     * Creates new entity.
     *
     * @param entityToCreate entity to create
     * @return created entity
     */
    T create(T entityToCreate);
}
