package com.epam.esm.controller;

import com.epam.esm.assembler.GiftCertificateAssembler;
import com.epam.esm.controller.constraint.FilterConstraint;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.group.OnPersist;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * Controller class to manage {@link GiftCertificateDto Gift Certificates}.
 */
@RestController
@RequestMapping("api/certificates")
@Validated
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    private final GiftCertificateAssembler giftCertificateAssembler;

    public GiftCertificateController(GiftCertificateService giftCertificateService, GiftCertificateAssembler giftCertificateAssembler) {
        this.giftCertificateService = giftCertificateService;
        this.giftCertificateAssembler = giftCertificateAssembler;
    }

    /**
     * Method to get all {@link GiftCertificateDto Gift Certificates} with Pagination and Filtering options.
     *
     * @param page number of the page
     * @param size number of items in a page
     * @param filterParams filter parameters to apply
     * @return a {@link PagedModel} which contains all the {@link GiftCertificateDto Gift Certificates} with given filter options
     */
    @GetMapping()
    public PagedModel<GiftCertificateDto> findAllCertificatesWithParameters(
            @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "40013") int page,
            @RequestParam(required = false, defaultValue = "5") @Min(value = 1, message = "40014") int size,
            @RequestParam @FilterConstraint MultiValueMap<String, String> filterParams
    ) {
        Page<GiftCertificateDto> giftCertificateDtos = giftCertificateService.getAllWithFilter(page, size, filterParams);
        return giftCertificateAssembler.toCollectionModel(giftCertificateDtos, page, size, filterParams);
    }

    /**
     * Method to get a {@link GiftCertificateDto Gift Certificate} by its Id.
     *
     * @param id the Id of the {@link GiftCertificateDto Gift Certificate}
     * @return the {@link GiftCertificateDto Gift Certificate}
     */
    @GetMapping("/{id}")
    public GiftCertificateDto giftCertificateById(@PathVariable @Valid @Min(value = 1, message = "40001") long id) {
        GiftCertificateDto giftCertificateDto = giftCertificateService.getById(id);
        return giftCertificateAssembler.toModel(giftCertificateDto);
    }

    /**
     * Method to save a {@link GiftCertificateDto Gift Certificate}.
     *
     * @param giftCertificateDto the {@link GiftCertificateDto Gift Certificate} to be saved
     * @return saved {@link GiftCertificateDto Gift Certificate}
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto saveGiftCertificate(@RequestBody @Validated(OnPersist.class) GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto savedCert = giftCertificateService.save(giftCertificateDto);
        return giftCertificateAssembler.toModel(savedCert);
    }

    /**
     * Method to update a {@link GiftCertificateDto Gift Certificate}.
     *
     * @param id Id of the {@link GiftCertificateDto Gift Certificate} to be updated
     * @param giftCertificateDto the new fields for the {@link GiftCertificateDto Gift Certificate}
     * @return the updated {@link GiftCertificateDto Gift Certificate}
     */
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto updateGiftCertificate(@PathVariable @Min(value = 1, message="40001") long id, @RequestBody @Valid GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto updated = giftCertificateService.update(id, giftCertificateDto);
        return giftCertificateAssembler.toModel(updated);
    }

    /**
     * Method to delete a {@link GiftCertificateDto Gift Certificate} by its Id.
     *
     * @param id the Id of the {@link GiftCertificateDto Gift Certificate} to be deleted
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteGiftCertificate(@PathVariable @Valid @Min(value = 1, message = "40001") Long id) {
        giftCertificateService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
