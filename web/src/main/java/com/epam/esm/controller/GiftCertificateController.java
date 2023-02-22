package com.epam.esm.controller;

import com.epam.esm.assembler.GiftCertificateAssembler;
import com.epam.esm.controller.constraint.FilterConstraint;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.group.OnPersist;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.PersistentException;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;

/**
 * This class is an endpoint of the API which allows to perform CRUD operations
 * with {@link com.epam.esm.entity.GiftCertificate} entities accessed through <i>api/certificates</i>.
 * @author Lobur Yaroslav
 * @version 1.0
 */
@RestController
@RequestMapping("api/certificates")
@Validated
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    private final GiftCertificateAssembler giftCertificateAssembler;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService, GiftCertificateAssembler giftCertificateAssembler) {
        this.giftCertificateService = giftCertificateService;
        this.giftCertificateAssembler = giftCertificateAssembler;
    }

    /**
     * Allows to get certificates with tags (all params are optional and can be used in conjunction):
     * <ul>
     *  <li>search for gift certificates by several tags</li>
     *  <li>search by part of name/description</li>
     *  <li>sort by date or by name ASC/DESC</li>
     * </ul>
     *
     * @param page         page number.
     * @param size         number of showed entities on page.
     * @param filterParams is a {@link MultiValueMap} collection that contains {@link String} as
     *                     key and {@link String} as value.
     *                     <pre><ul>
     *                                                <li>name as {@link GiftCertificate} name</li>
     *                                                             <li>description as {@link GiftCertificate} description</li>
     *                                                             <li>tags as {@link Tag} name (multiple times)</li>
     *                                                             <li>name_sort as {@link  String} for sorting certificates by name (asc/desc)</li>
     *                                                             <li>date_sort as {@link  String} for sorting certificates by create date (asc/desc)</li>
     *                                         </ul></pre>
     * @return a {@link List} of found {@link GiftCertificate} entities with specified parameters. Response code 200.
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
     * Gets a {@link GiftCertificate} by its <code>id</code> from database.
     *
     * @param id for {@link GiftCertificate}
     * @return requested {@link GiftCertificate} entity. Response code 200.
     * @throws PersistentException if {@link GiftCertificate} is not found.
     */
    @GetMapping("/{id}")
    public GiftCertificateDto giftCertificateById(@PathVariable @Valid @Min(value = 1, message = "40001") Long id) throws PersistentException {
        GiftCertificateDto giftCertificateDto = giftCertificateService.getById(id);
        return giftCertificateAssembler.toModel(giftCertificateDto);
    }

    /**
     * Creates a new {@link GiftCertificate} entity with a
     * {@link List} of {@link Tag} entities.
     * If new {@link Tag} entities are passed during creation – they will be created in the database.
     *
     * @param giftCertificateDto must be valid according to {@link GiftCertificateDto} entity.
     * @return Saved {@link GiftCertificate}. Response code 201.
     * @throws PersistentException if {@link GiftCertificate} an error occurred during saving.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto saveGiftCertificate(@RequestBody @Validated(OnPersist.class) GiftCertificateDto giftCertificateDto) throws PersistentException {
        GiftCertificateDto savedCert = giftCertificateService.save(giftCertificateDto);
        return giftCertificateAssembler.toModel(savedCert);
    }

    /**
     * Updates a {@link GiftCertificate} by specified <code>id</code>.
     *
     * @param id                 a {@link GiftCertificate} id.
     * @param giftCertificateDto a {@link GiftCertificateDto} that contains information for updating.
     *                           Updates only fields, that are passed in request body.
     * @return Updated {@link GiftCertificate}. Response code 201.
     * @throws PersistentException if the {@link GiftCertificate} entity do not exist.
     */
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto updateGiftCertificate(@PathVariable @Min(value = 1, message="40001") Long id, @RequestBody @Valid GiftCertificateDto giftCertificateDto) throws PersistentException {
        GiftCertificateDto updated = giftCertificateService.update(id, giftCertificateDto);
        return giftCertificateAssembler.toModel(updated);
    }

    /**
     * Deletes {@link com.epam.esm.entity.GiftCertificate} entity from database.
     *
     * @param id for {@link com.epam.esm.entity.GiftCertificate} to delete.
     * @return ResponseEntity with empty body. Response code 204.
     * @throws PersistentException if {@link com.epam.esm.entity.GiftCertificate} entity do not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteGiftCertificate(@PathVariable @Valid @Min(value = 1, message = "40001") Long id) throws PersistentException {
        giftCertificateService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
