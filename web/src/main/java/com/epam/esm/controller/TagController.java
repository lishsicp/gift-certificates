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
 * with {@link com.epam.esm.entity.Tag} entities accessed through <i>api/tags</i>.
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
     * Gets all {@link com.epam.esm.entity.Tag} entities from database.
     * @return a {@link List} of {@link com.epam.esm.entity.Tag} entities. Response code 200.
     */
    @GetMapping()
    public List<Tag> allTags() {
        return tagService.findAll();
    }

    /**
     * Gets a {@link com.epam.esm.entity.Tag} by its <code>id</code> from database.
     * @param id for {@link com.epam.esm.entity.Tag}
     * @return {@link com.epam.esm.entity.Tag} entity. Response code 200.
     * @throws DaoException if {@link com.epam.esm.entity.Tag} is not found.
     */
    @GetMapping("/{id}")
    public Tag tagById(@PathVariable @Valid @Min(value = 1, message = "40001") Long id) {
        return tagService.findById(id);
    }

    /**
     * Creates a new {@link com.epam.esm.entity.Tag} entity in database.
     *
     * @param tag must be valid according to {@link com.epam.esm.entity.Tag} entity.
     * @return ResponseEntity with saved {@link com.epam.esm.entity.Tag}. Response code 201.
     * @throws DaoException if {@link com.epam.esm.entity.Tag} an error occurred during saving.
     */
    @PostMapping
    public ResponseEntity<Object> saveTag(@RequestBody @Valid Tag tag) {
        Tag savedTag = tagService.save(tag);
        URI locationUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedTag.getId())
                .toUri();
        return ResponseEntity.created(locationUri).body(savedTag);
    }

    /**
     * Deletes {@link com.epam.esm.entity.Tag} entity from database.
     *
     * @param id for {@link com.epam.esm.entity.Tag} to delete.
     * @return ResponseEntity with empty body. Response code 204.
     * @throws DaoException if {@link com.epam.esm.entity.Tag} entity do not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTag(@PathVariable @Valid @Min(value = 1, message = "40001") Long id) {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
