package com.epam.esm.service;

import org.springframework.data.domain.Page;

/**
 * This interface defines the basic CRUD (Create, Read, Delete) operations for a service in the business layer.
 *
 * @param <T> the type of entity managed by this service
 */
public interface CRDService<T> {

    /**
     * Retrieves a paginated list of all entities.
     *
     * @param page the page number of the result set to retrieve
     * @param size the number of items per page to retrieve
     * @return a Page object containing the requested list of entities
     */
    Page<T> getAll(int page, int size);

    /**
     * Retrieves an entity by its ID.
     *
     * @param id the ID of the entity to retrieve
     * @return the entity with the given ID, or null if not found
     */
    T getById(long id);

    /**
     * Saves a new entity.
     *
     * @param t the entity to save
     * @return the saved entity
     */
    T save(T t);

    /**
     * Deletes an entity by its ID.
     *
     * @param id the ID of the entity to delete
     */
    void delete(long id);
}