package com.epam.esm.dao;


import java.util.List;

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
    List<T> getAll();

    /**
     * Finds an entity with given id
     *
     * @param id Id of the entity to find
     * @return Found entity
     */
    T getById(long id);

    /**
     * Removes an entity by given id.
     *
     * @param id Id of the entity to remove
     */
    void remove(long id);

    /**
     * Saves given entity, used when creating a new entity
     *
     * @param t Entity to save
     * @return Saved entity
     */
    T create(T t);
}
