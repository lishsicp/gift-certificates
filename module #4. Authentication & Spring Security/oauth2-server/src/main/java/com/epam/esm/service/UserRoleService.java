package com.epam.esm.service;

import com.epam.esm.entity.AuthUserRole;

import java.util.List;

public interface UserRoleService {

    AuthUserRole getByName(String roleName);

    List<AuthUserRole> getAll();

    AuthUserRole create(AuthUserRole authUserRole);

    void deleteByName(String roleName);
}
