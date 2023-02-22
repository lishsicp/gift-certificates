package com.epam.esm.controller;

import com.epam.esm.assembler.UserAssembler;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.PersistentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;


/**
 * This class is an endpoint of the API which allows to perform READ operations
 * with {@link User} entities accessed through <i>api/users</i>.
 * @author Lobur Yaroslav
 * @version 1.0
 */
@RestController
@RequestMapping("api/users")
@Validated
public class UserController {

    private final UserService userService;
    private final UserAssembler userAssembler;

    @Autowired
    public UserController(UserService userService, UserAssembler userAssembler) {
        this.userService = userService;
        this.userAssembler = userAssembler;
    }

    /**
     * Gets all {@link User} entities from database.
     *
     * @param page         page number.
     * @param size         number of showed entities on page.
     * @return a {@link List} of {@link User} entities. Response code 200.
     */
    @GetMapping()
    public PagedModel<UserDto> allUsers(
            @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "40013") int page,
            @RequestParam(required = false, defaultValue = "5") @Min(value = 1, message = "40014") int size) {
        Page<UserDto> users = userService.getAll(page, size);
        return userAssembler.toCollectionModel(users, page, size);
    }

    /**
     * Gets a {@link User} by its <code>id</code> from database.
     * @param id for {@link User}
     * @return {@link User} entity. Response code 200.
     * @throws PersistentException if {@link User} is not found.
     */
    @GetMapping("/{id}")
    public UserDto userById(@PathVariable @Min(value = 1, message = "40001") Long id) throws PersistentException {
        UserDto userDto = userService.getById(id);
        return userAssembler.toModel(userDto);
    }

}
