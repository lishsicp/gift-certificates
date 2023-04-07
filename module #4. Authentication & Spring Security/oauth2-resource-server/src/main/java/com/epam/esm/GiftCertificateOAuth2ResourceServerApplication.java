package com.epam.esm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GiftCertificateOAuth2ResourceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GiftCertificateOAuth2ResourceServerApplication.class, args);
    }
}
