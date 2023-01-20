package com.epam.esm.dao;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public abstract class GenericDao<T> implements CRDDao<T> {

    @PersistenceContext
    protected EntityManager entityManager;

    protected final Class<T> classType;

    protected GenericDao(Class<T> aClass) {
        this.classType = aClass;
    }

    @Override
    public List<T> getAll() {
        return entityManager.createQuery("select c from " + classType.getSimpleName() + " c", classType)
                .getResultList();
    }

    @Override
    public T getById(Long id) {
        return entityManager.find(classType, id);
    }

    @Override
    @Transactional
    public void delete(T t) {
        entityManager.remove(t);
    }

    @Override
    @Transactional
    public T create(T t) {
        entityManager.persist(t);
        entityManager.flush();
        return t;
    }
}
