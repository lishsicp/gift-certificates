package com.epam.esm.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;


public abstract class GenericDao<T> implements CRDDao<T> {

    @PersistenceContext
    protected EntityManager entityManager;

    protected final Class<T> classType;

    protected GenericDao(Class<T> aClass) {
        this.classType = aClass;
    }

    @Override
    public List<T> getAll(Pageable pageable) {
        return entityManager.createQuery("select c from " + classType.getSimpleName() + " c", classType)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public Optional<T> getById(Long id) {
        return Optional.ofNullable(entityManager.find(classType, id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        T entity = entityManager.find(classType, id);
        entityManager.remove(entity);
    }

    @Override
    @Transactional
    public T create(T t) {
        entityManager.persist(t);
        entityManager.flush();
        return t;
    }
}
