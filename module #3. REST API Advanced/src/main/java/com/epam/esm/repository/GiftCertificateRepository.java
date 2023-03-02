package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This interface extends Spring's PagingAndSortingRepository interface for the GiftCertificate entity, adding a custom
 * <p>
 * method for finding a GiftCertificate entity by name.
 */
@Repository
public interface GiftCertificateRepository extends PagingAndSortingRepository<GiftCertificate, Long>, CustomGiftCertificateRepository {

    /**
     * Finds a GiftCertificate entity by name.
     *
     * @param name the name of the GiftCertificate to find
     * @return an Optional object containing the requested GiftCertificate object, or an empty Optional if no match is found
     */
    Optional<GiftCertificate> findGiftCertificateByName(String name);
}

