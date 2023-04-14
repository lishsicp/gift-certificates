package com.epam.esm.service.impl;

import com.epam.esm.entity.AuthUserRole;
import com.epam.esm.exception.DuplicateKeyException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.repository.AuthUserRoleRepository;
import com.epam.esm.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private static final String DO_NOT_EXIST = "User role '%s' do not exist";
    private static final String ALREADY_EXIST = "User role '%s' already exist";
    private final AuthUserRoleRepository authUserRoleRepository;

    @Override
    public AuthUserRole getByName(String roleName) {
        Optional<AuthUserRole> userRole = authUserRoleRepository.findByName(roleName);
        if (userRole.isEmpty()) {
            throw new EntityNotFoundException(String.format(DO_NOT_EXIST, roleName));
        }
        return userRole.get();
    }

    @Override
    public List<AuthUserRole> getAll() {
        return authUserRoleRepository.findAll();
    }

    @Override
    @Transactional
    public AuthUserRole create(AuthUserRole authUserRole) {
        Optional<AuthUserRole> userRole = authUserRoleRepository.findByName(authUserRole.getName());
        if (userRole.isPresent()) {
            throw new DuplicateKeyException(String.format(ALREADY_EXIST, authUserRole.getName()));
        }
        return authUserRoleRepository.save(authUserRole);
    }

    @Override
    @Transactional
    public void deleteByName(String roleName) {
        Optional<AuthUserRole> userRole = authUserRoleRepository.findByName(roleName);
        if (userRole.isEmpty()) {
            throw new EntityNotFoundException(DO_NOT_EXIST);
        }
        authUserRoleRepository.deleteByName(roleName);
    }
}
