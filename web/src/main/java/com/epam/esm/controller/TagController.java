package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.service.CRDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/tags")
public class TagController {

    private final CRDService<Tag> tagCRDService;

    @Autowired
    public TagController(CRDService<Tag> tagCRDService) {
        this.tagCRDService = tagCRDService;
    }

    @GetMapping()
    public List<Tag> allTags() {
        return tagCRDService.findAll();
    }

    @GetMapping("/{id}")
    public Tag tagById(@PathVariable Long id) {
        return tagCRDService.findById(id);
    }

    @PostMapping("/new")
    public ResponseEntity<Tag> saveTag(@RequestBody Tag tag) {
        Tag savedTag = tagCRDService.save(tag);
        URI locationUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedTag.getId())
                .toUri();
        return ResponseEntity.created(locationUri).body(savedTag);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Tag> deleteTag(@PathVariable Long id) {
        tagCRDService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
