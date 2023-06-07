package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {
}
