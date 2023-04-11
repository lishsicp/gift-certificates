package com.epam.esm.web;

import com.epam.esm.entity.AuthUserRole;
import com.epam.esm.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("oauth2/roles")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthUserRole> create(@RequestBody String roleName) {
        return ResponseEntity.ok(userRoleService.create(AuthUserRole.builder()
            .name(roleName).build()));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuthUserRole>> getAll() {
        return ResponseEntity.ok(userRoleService.getAll());
    }

    @GetMapping("/{roleName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthUserRole> getByName(@PathVariable String roleName) {
        return ResponseEntity.ok(userRoleService.getByName(roleName));
    }

    @DeleteMapping("/{roleName}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<List<AuthUserRole>> deleteByName(@PathVariable String roleName) {
        userRoleService.deleteByName(roleName);
        return ResponseEntity.noContent().build();
    }
}
