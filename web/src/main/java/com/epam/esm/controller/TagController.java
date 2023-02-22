package com.epam.esm.controller;

import com.epam.esm.assembler.TagModelAssembler;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.group.OnPersist;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.PersistentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
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
    private final TagModelAssembler tagModelAssembler;

    @Autowired
    public TagController(TagService tagService, TagModelAssembler tagModelAssembler) {
        this.tagService = tagService;
        this.tagModelAssembler = tagModelAssembler;
    }

    /**
     * Gets all {@link Tag} entities from database.
     * @return a {@link List} of {@link Tag} entities. Response code 200.
     */
    @GetMapping()
    public PagedModel<TagDto> allTags(
            @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "40013") int page,
            @RequestParam(required = false, defaultValue = "5") @Min(value = 1, message = "40014") int size) {
        Page<TagDto> tagDtos = tagService.getAll(page, size);
        return tagModelAssembler.toCollectionModel(tagDtos, page, size);
    }

    /**
     * Gets a {@link Tag} by its <code>id</code> from database.
     * @param id for {@link Tag}
     * @return {@link Tag} entity. Response code 200.
     * @throws PersistentException if {@link Tag} is not found.
     */
    @GetMapping("/{id}")
    public TagDto tagById(@PathVariable @Valid @Min(value = 1, message = "40001") Long id) throws PersistentException {
        TagDto tagDto = tagService.getById(id);
        return tagModelAssembler.toModel(tagDto);
    }

    /**
     * Get the most widely used {@link Tag} of a {@link User} with the highest cost of all {@link Order} entities.
     * @return ResponseEntity with most popular {@link Tag}. Response code 200.
     * @throws PersistentException if {@link Tag} is not found.
     */
    @GetMapping("/popular")
    public TagDto popularTag() throws PersistentException {
        TagDto tagDto = tagService.getMostWidelyUsedTagWithHighestCostOfAllOrders();
        return tagModelAssembler.toModel(tagDto);
    }

    /**
     * Creates a new {@link Tag} entity in database.
     *
     * @param tagDto must be valid according to {@link Tag} entity.
     * @return Saved {@link Tag}. Response code 201.
     * @throws PersistentException if {@link Tag} an error occurred during saving.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto saveTag(@RequestBody @Validated(OnPersist.class) TagDto tagDto) throws PersistentException {
        TagDto savedTagDto = tagService.save(tagDto);
        return tagModelAssembler.toModel(savedTagDto);
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
