package com.epam.esm.repository;

import com.epam.esm.entity.AuthUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserRoleRepository extends JpaRepository<AuthUserRole, Long> {

    Optional<AuthUserRole> findByName(String name);

    void deleteByName(String roleName);
}
