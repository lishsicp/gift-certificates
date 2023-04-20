package com.epam.esm.controller;

import com.epam.esm.assembler.GiftCertificateAssembler;
import com.epam.esm.controller.constraint.FilterConstraint;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.group.OnPersist;
import com.epam.esm.service.GiftCertificateService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controller class to manage {@link GiftCertificateDto Gift Certificates}.
 */
@RestController
@RequestMapping("api/certificates")
@Validated
@RequiredArgsConstructor
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateAssembler giftCertificateAssembler;

    /**
     * Method to get all {@link GiftCertificateDto Gift Certificates} with Pagination and Filtering options.
     *
     * @param page         number of the page
     * @param size         number of items in a page
     * @param filterParams filter parameters to apply
     * @return a {@link PagedModel} which contains all the {@link GiftCertificateDto Gift Certificates} with given
     * filter options
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PagedModel<GiftCertificateDto> getAllWithParameters(
        @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "40013") int page,
        @RequestParam(required = false, defaultValue = "5") @Min(value = 1, message = "40014") int size,
        @RequestParam @FilterConstraint MultiValueMap<String, String> filterParams
    ) {
        Page<GiftCertificateDto> giftCertificateDtos =
            giftCertificateService.getAllWithFilter(page, size, filterParams);
        Link selfRel =
            linkTo(methodOn(this.getClass()).getAllWithParameters(page, size, filterParams)).withSelfRel();
        return giftCertificateAssembler.toCollectionModel(giftCertificateDtos, selfRel);
    }

    /**
     * Method to get a {@link GiftCertificateDto Gift Certificate} by its Id.
     *
     * @param id the Id of the {@link GiftCertificateDto Gift Certificate}
     * @return the {@link GiftCertificateDto Gift Certificate}
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GiftCertificateDto getById(@PathVariable @Valid @Min(value = 1, message = "40001") long id) {
        GiftCertificateDto giftCertificateDto = giftCertificateService.getById(id);
        return giftCertificateAssembler.toModel(giftCertificateDto);
    }

    /**
     * Method to save a {@link GiftCertificateDto Gift Certificate}.
     *
     * @param giftCertificateDto the {@link GiftCertificateDto Gift Certificate} to be saved
     * @return saved {@link GiftCertificateDto Gift Certificate}
     */
    @PreAuthorize(value = "hasRole('ADMIN') or hasAuthority('SCOPE_certificate.write')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto save(
        @RequestBody @Validated(OnPersist.class) GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto savedCert = giftCertificateService.save(giftCertificateDto);
        return giftCertificateAssembler.toModel(savedCert);
    }

    /**
     * Method to update a {@link GiftCertificateDto Gift Certificate}.
     *
     * @param id                 Id of the {@link GiftCertificateDto Gift Certificate} to be updated
     * @param giftCertificateDto the new fields for the {@link GiftCertificateDto Gift Certificate}
     * @return the updated {@link GiftCertificateDto Gift Certificate}
     */
    @PreAuthorize(value = "hasRole('ADMIN') or hasAuthority('SCOPE_certificate.write')")
    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public GiftCertificateDto update(@PathVariable @Min(value = 1, message = "40001") long id,
        @RequestBody @Valid GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto updated = giftCertificateService.update(id, giftCertificateDto);
        return giftCertificateAssembler.toModel(updated);
    }

    /**
     * Method to delete a {@link GiftCertificateDto Gift Certificate} by its Id.
     *
     * @param id the Id of the {@link GiftCertificateDto Gift Certificate} to be deleted
     */
    @PreAuthorize(value = "hasRole('ADMIN') or hasAuthority('SCOPE_certificate.write')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(
        @PathVariable @Valid @Min(value = 1, message = "40001") long id) {
        giftCertificateService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
