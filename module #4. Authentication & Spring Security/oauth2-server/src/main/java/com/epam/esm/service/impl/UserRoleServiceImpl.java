package com.epam.esm.service.impl;

import com.epam.esm.entity.AuthUserRole;
import com.epam.esm.exception.DuplicateKeyException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.repository.AuthUserRoleRepository;
import com.epam.esm.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    public static final String DO_NOT_EXIST = "User role do not exist";
    private final AuthUserRoleRepository authUserRoleRepository;

    @Override
    public AuthUserRole getByName(String roleName) {
        Optional<AuthUserRole> userRole = authUserRoleRepository.findByName(roleName);
        if (userRole.isEmpty()) {
            throw new EntityNotFoundException(DO_NOT_EXIST);
        }
        return userRole.get();
    }

    @Override
    public List<AuthUserRole> getAll() {
        return authUserRoleRepository.findAll();
    }

    @Override
    public AuthUserRole create(AuthUserRole authUserRole) {
        Optional<AuthUserRole> userRole = authUserRoleRepository.findByName(authUserRole.getName());
        if (userRole.isPresent()) {
            throw new DuplicateKeyException("User role already exist");
        }
        return authUserRoleRepository.save(authUserRole);
    }

    @Override
    public void deleteByName(String roleName) {
        Optional<AuthUserRole> userRole = authUserRoleRepository.findByName(roleName);
        if (userRole.isEmpty()) {
            throw new EntityNotFoundException(DO_NOT_EXIST);
        }
        authUserRoleRepository.deleteByName(roleName);
    }
}
