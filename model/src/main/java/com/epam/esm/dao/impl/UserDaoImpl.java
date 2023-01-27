package com.epam.esm.dao.impl;

import com.epam.esm.dao.GenericDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl extends GenericDao<User> implements UserDao {

    @Autowired
    protected UserDaoImpl() {
        super(User.class);
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User save(User user) {
        throw new UnsupportedOperationException();
    }
}
