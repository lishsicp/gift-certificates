package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
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
        return tagService.getAll();
    }

    /**
     * Gets a {@link com.epam.esm.entity.Tag} by its <code>id</code> from database.
     * @param id for {@link com.epam.esm.entity.Tag}
     * @return {@link com.epam.esm.entity.Tag} entity. Response code 200.
     */
    @GetMapping("/{id}")
    public Tag tagById(@PathVariable @Valid @Min(value = 1, message = "40001") Long id) {
        return tagService.getById(id);
    }

    /**
     * Creates a new {@link com.epam.esm.entity.Tag} entity in database.
     *
     * @param tag must be valid according to {@link com.epam.esm.entity.Tag} entity.
     * @return ResponseEntity with saved {@link com.epam.esm.entity.Tag}. Response code 201.
     */
    @PostMapping
    public Tag saveTag(@RequestBody @Valid Tag tag) {
        return tagService.save(tag);
    }

    /**
     * Deletes {@link com.epam.esm.entity.Tag} entity from database.
     *
     * @param id for {@link com.epam.esm.entity.Tag} to delete.
     * @return ResponseEntity with empty body. Response code 204.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTag(@PathVariable @Valid @Min(value = 1, message = "40001") Long id) {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
