package com.epam.esm.config;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SpringConfig {
    @Bean
    public Faker getFaker() {
        return new Faker();
    }
}
