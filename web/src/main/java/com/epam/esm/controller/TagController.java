package com.epam.esm.controller;

import com.epam.esm.assembler.TagModelAssembler;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.converter.TagConverter;
import com.epam.esm.dto.group.OnPersist;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.PersistentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    private final TagConverter tagConverter;
    private final TagModelAssembler tagModelAssembler;

    @Autowired
    public TagController(TagService tagService, TagConverter tagDtoTagController, TagModelAssembler tagModelAssembler) {
        this.tagService = tagService;
        this.tagConverter = tagDtoTagController;
        this.tagModelAssembler = tagModelAssembler;
    }

    /**
     * Gets all {@link Tag} entities from database.
     * @return a {@link List} of {@link Tag} entities. Response code 200.
     */
    @GetMapping()
    public CollectionModel<TagDto> allTags(
            @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "40013") int page,
            @RequestParam(required = false, defaultValue = "5") @Min(value = 1, message = "40014") int size) {
        List<TagDto> tags = tagService
                .getAll(page, size)
                .stream()
                .map(tagConverter::toDto)
                .map(tagModelAssembler::toModel)
                .collect(Collectors.toList());
        Link selfRel = linkTo(methodOn(this.getClass()).allTags(page, size)).withSelfRel();
        return CollectionModel.of(tags, selfRel);
    }

    /**
     * Gets a {@link Tag} by its <code>id</code> from database.
     * @param id for {@link Tag}
     * @return {@link Tag} entity. Response code 200.
     * @throws PersistentException if {@link Tag} is not found.
     */
    @GetMapping("/{id}")
    public TagDto tagById(@PathVariable @Valid @Min(value = 1, message = "40001") Long id) throws PersistentException {
        return tagModelAssembler.toModel(tagConverter.toDto(tagService.getById(id)));
    }

    /**
     * Creates a new {@link Tag} entity in database.
     *
     * @param tagDto must be valid according to {@link Tag} entity.
     * @return ResponseEntity with saved {@link Tag}. Response code 201.
     * @throws PersistentException if {@link Tag} an error occurred during saving.
     */
    @PostMapping
    public ResponseEntity<Object> saveTag(@RequestBody @Validated(OnPersist.class) TagDto tagDto) throws PersistentException {
        Tag savedTag = tagService.save(tagConverter.toEntity(tagDto));
        URI locationUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedTag.getId())
                .toUri();
        return ResponseEntity.created(locationUri).body(tagModelAssembler.toModel(tagConverter.toDto(savedTag)));
    }

    /**
     * Deletes {@link Tag} entity from database.
     *
     * @param id for {@link Tag} to delete.
     * @return ResponseEntity with empty body. Response code 204.
     * @throws PersistentException if {@link Tag} entity do not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTag(@PathVariable @Valid @Min(value = 1, message = "40001") Long id) throws PersistentException {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
