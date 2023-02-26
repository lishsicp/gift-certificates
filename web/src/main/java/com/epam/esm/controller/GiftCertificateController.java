package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.filter.SearchFilter;
import com.epam.esm.entity.group.OnPersist;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import javax.validation.constraints.Pattern;
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

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    /**
     * Allows to get certificates with tags (all params are optional and can be used in conjunction):
     * <ul>
     *  <li>by tag name</li>
     *  <li>search by part of name/description</li>
     *  <li>sort by date or by name ASC/DESC</li>
     * </ul>
     *
     * @param tagName     valid {@link Tag} name.
     * @param name        valid {@link GiftCertificate} name
     * @param description valid {@link GiftCertificate} description
     * @param sortBy      sort by {@link GiftCertificate} name or {@link GiftCertificate} last update date
     * @param sortByType  sort order is either ASC or DESC
     * @return a {@link List} of found {@link GiftCertificate} entities with specified parameters.
     */
    @GetMapping()
    public List<GiftCertificate> findAllCertificatesFiltered(
            @Pattern(regexp = "[\\w\\s]{2,128}+", message = "40002") @RequestParam(required = false) String tagName,
            @Pattern(regexp = "[\\w\\s]{2,128}+", message = "40003") @RequestParam(required = false) String name,
            @Pattern(regexp = "[\\w\\s]{2,512}+", message = "40004") @RequestParam(required = false) String description,
            @Pattern(regexp = "NAME|LAST_UPDATE_DATE", message = "40007") @RequestParam(required = false) String sortBy,
            @Pattern(regexp = "ASC|DESC", message = "40008") @RequestParam(required = false) String sortByType
    ) {
        SearchFilter searchFilter = SearchFilter.builder()
                .tagName(tagName)
                .name(name)
                .description(description)
                .sortBy(sortBy)
                .sortByType(sortByType)
                .build();
        return giftCertificateService.getAllCertificatesWithFilter(searchFilter);
    }

    /**
     * Gets a {@link com.epam.esm.entity.GiftCertificate} by its <code>id</code> from database.
     * @param id for {@link com.epam.esm.entity.GiftCertificate}
     * @return requested {@link com.epam.esm.entity.GiftCertificate} entity. Response code 200.
     */
    @GetMapping("/{id}")
    public GiftCertificate giftCertificateById(@PathVariable @Valid @Min(value = 1, message = "40001") Long id) {
        return giftCertificateService.getById(id);
    }

    /**
     * Creates a new {@link GiftCertificate} entity with a
     * {@link List} of {@link Tag} entities.
     * If new {@link Tag} entities are passed during creation – they will be created in the database.
     * @param giftCertificate must be valid according to {@link com.epam.esm.entity.GiftCertificate} entity.
     * @return ResponseEntity with saved {@link com.epam.esm.entity.Tag}. Response code 201.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificate saveGiftCertificate(@RequestBody @Validated(OnPersist.class) GiftCertificate giftCertificate) {
        return giftCertificateService.save(giftCertificate);
    }

    /**
     * Updates a {@link GiftCertificate} by specified <code>id</code>.
     * @param id a {@link GiftCertificate} id.
     * @param giftCertificate a {@link GiftCertificate} that contains information for updating.
     * Updates only fields, that are passed in request body.
     * @return ResponseEntity with message. Response code 203.
     */
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> updateGiftCertificate(@PathVariable @Min(value = 1, message="40001") Long id, @RequestBody @Valid GiftCertificate giftCertificate) {
        giftCertificateService.update(id, giftCertificate);
        return ResponseEntity.status(HttpStatus.CREATED).body("Success");
    }

    /**
     * Deletes {@link com.epam.esm.entity.GiftCertificate} entity from database.
     *
     * @param id for {@link com.epam.esm.entity.GiftCertificate} to delete.
     * @return ResponseEntity with empty body. Response code 204.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteGiftCertificate(@PathVariable @Valid @Min(value = 1, message = "40001") Long id) {
        giftCertificateService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
