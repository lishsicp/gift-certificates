package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long>,
    PagingAndSortingRepository<GiftCertificate, Long>, CustomGiftCertificateRepository {

    /**
     * Finds a {@link GiftCertificate} entity by name.
     *
     * @param name the name of the {@link GiftCertificate} to find
     * @return an {@link Optional} object containing the requested {@link GiftCertificate} object
     */
    Optional<GiftCertificate> findGiftCertificateByName(String name);
}

