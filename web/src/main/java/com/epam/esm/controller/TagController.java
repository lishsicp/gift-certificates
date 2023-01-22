package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DaoException;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;
import java.util.List;

/**
 * This class is an endpoint of the API which allows to perform CRD operations
 * with {@link Tag} entities accessed through <i>api/tags</i>.
 * @author Lobur Yaroslav
 * @version 1.0
 */
@RestController
@RequestMapping("api/tags")
@Validated
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Gets all {@link Tag} entities from database.
     * @return a {@link List} of {@link Tag} entities. Response code 200.
     */
    @GetMapping()
    public List<Tag> allTags(@RequestParam(required = false, defaultValue = "0") int pageNo,
                             @RequestParam(required = false, defaultValue = "5 ") int size) {
        return tagService.findAll(pageNo, size);
    }

    /**
     * Gets a {@link Tag} by its <code>id</code> from database.
     * @param id for {@link Tag}
     * @return {@link Tag} entity. Response code 200.
     * @throws DaoException if {@link Tag} is not found.
     */
    @GetMapping("/{id}")
    public Tag tagById(@PathVariable @Valid @Min(value = 1, message = "40001") Long id) throws DaoException {
        return tagService.findById(id);
    }

    /**
     * Creates a new {@link Tag} entity in database.
     *
     * @param tag must be valid according to {@link Tag} entity.
     * @return ResponseEntity with saved {@link Tag}. Response code 201.
     * @throws DaoException if {@link Tag} an error occurred during saving.
     */
    @PostMapping
    public ResponseEntity<Object> saveTag(@RequestBody @Valid Tag tag) throws DaoException {
        Tag savedTag = tagService.save(tag);
        URI locationUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedTag.getId())
                .toUri();
        return ResponseEntity.created(locationUri).body(savedTag);
    }

    /**
     * Deletes {@link Tag} entity from database.
     *
     * @param id for {@link Tag} to delete.
     * @return ResponseEntity with empty body. Response code 204.
     * @throws DaoException if {@link Tag} entity do not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTag(@PathVariable @Valid @Min(value = 1, message = "40001") Long id) throws DaoException {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
