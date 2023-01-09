package com.epam.esm.controller;

import com.epam.esm.entity.*;
import com.epam.esm.entity.filter.SearchFilter;
import com.epam.esm.exception.*;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.IncorrectUpdateValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/certificates")
@Validated
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

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
        return giftCertificateService.findAllCertificatesWithFilter(searchFilter);
    }

    @GetMapping("/{id}")
    public GiftCertificate giftCertificateById(@PathVariable @Valid @Min(1) Long id) throws DaoException {
        return giftCertificateService.findById(id);
    }

    @PostMapping("/new")
    public ResponseEntity<Object> saveGiftCertificate(@RequestBody @Valid GiftCertificate giftCertificate) throws DaoException {
        GiftCertificate savedCert = giftCertificateService.save(giftCertificate);
        URI locationUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCert.getId())
                .toUri();
        return ResponseEntity.created(locationUri).body(savedCert);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateGiftCertificate(@RequestBody GiftCertificate giftCertificate) throws DaoException, IncorrectUpdateValueException {
        giftCertificateService.update(giftCertificate);
        return ResponseEntity.status(HttpStatus.CREATED).body("Success");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GiftCertificate> deleteGiftCertificate(@PathVariable @Valid @Min(1) Long id) throws DaoException {
        giftCertificateService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
