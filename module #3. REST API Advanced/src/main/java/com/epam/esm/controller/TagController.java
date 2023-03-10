package com.epam.esm.controller;

import com.epam.esm.assembler.TagAssembler;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.group.OnPersist;
import com.epam.esm.service.TagService;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * This class is used to implement controller logic for tags.
 */
@RestController
@RequestMapping("api/tags")
@Validated
public class TagController {

    private final TagService tagService;
    private final TagAssembler tagAssembler;

    public TagController(TagService tagService, TagAssembler tagAssembler) {
        this.tagService = tagService;
        this.tagAssembler = tagAssembler;
    }

    /**
     * Method used to get all tags.
     *
     * @param page The page number
     * @param size The page size
     * @return All the tags
     */
    @GetMapping()
    public PagedModel<TagDto> allTags(
            @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "40013") int page,
            @RequestParam(required = false, defaultValue = "5") @Min(value = 1, message = "40014") int size) {
        Page<TagDto> tagDtos = tagService.getAll(page, size);
        Link selfRel = linkTo(methodOn(this.getClass()).allTags(page, size)).withSelfRel();
        return tagAssembler.toCollectionModel(tagDtos, selfRel);
    }

    /**
     * Method used to get a tag by its id.
     *
     * @param id The id of the tag
     * @return The tag with the given id
     */
    @GetMapping("/{id}")
    public TagDto tagById(@PathVariable @Valid @Min(value = 1, message = "40001") Long id) {
        TagDto tagDto = tagService.getById(id);
        return tagAssembler.toModel(tagDto);
    }

    /**
     * Method used to get the most widely used tag with the highest cost of all orders.
     *
     * @return The most widely used tag with the highest cost of all orders.
     */
    @GetMapping("/popular")
    public TagDto popularTag() {
        TagDto tagDto = tagService.getMostWidelyUsedTagWithHighestCostOfAllOrders();
        return tagAssembler.toModel(tagDto);
    }

    /**
     * Method used to save a new tag.
     *
     * @param tagDto The details of the tag to save
     * @return The newly saved tag
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto saveTag(@RequestBody @Validated(OnPersist.class) TagDto tagDto)  {
        TagDto savedTagDto = tagService.save(tagDto);
        return tagAssembler.toModel(savedTagDto);
    }

    /**
     * Method used to delete an existing tag by its id.
     *
     * @param id The id of the tag to delete
     * @return A response indicating the completion of the delete operation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTag(@PathVariable @Valid @Min(value = 1, message = "40001") Long id) {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }
}