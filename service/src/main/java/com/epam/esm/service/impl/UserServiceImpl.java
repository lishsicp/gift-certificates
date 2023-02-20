package com.epam.esm.service.impl;

import com.epam.esm.repository.UserRepository;
import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.ExceptionErrorCode;
import com.epam.esm.service.exception.PersistentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userDao;

    @Autowired
    public UserServiceImpl(UserRepository userDao) {
        this.userDao = userDao;
    }


    @Override
    public Page<User> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return userDao.findAll(pageable);
    }

    @Override
    public User getById(Long id) throws PersistentException {
        return userDao.findById(id).orElseThrow(() ->
                new PersistentException(ExceptionErrorCode.RESOURCE_NOT_FOUND, id)
        );
    }

    @Override
    public User save(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Long id) throws PersistentException  {
        throw new UnsupportedOperationException();
    }
}
