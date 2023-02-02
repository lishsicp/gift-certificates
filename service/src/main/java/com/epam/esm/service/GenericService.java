package com.epam.esm.service;

import com.epam.esm.dao.CRDDao;
import com.epam.esm.service.exception.ExceptionErrorCode;
import com.epam.esm.service.exception.PersistentException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public abstract class GenericService<T> implements CRDService<T> {

    private final CRDDao<T> crudDao;

    protected GenericService(CRDDao<T> crudDao) {
        this.crudDao = crudDao;
    }

    @Override
    public List<T> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return crudDao.findAll(pageable);
    }

    @Override
    public T getById(Long id) throws PersistentException {
        return crudDao.findById(id).orElseThrow(() -> new PersistentException(ExceptionErrorCode.RESOURCE_NOT_FOUND, id));
    }

    @Override
    public void delete(Long id) throws PersistentException {
        Optional<T> optionalT = crudDao.findById(id);
        if (optionalT.isEmpty())
            throw new PersistentException(ExceptionErrorCode.RESOURCE_NOT_FOUND, id);
        crudDao.delete(id);
    }
}
