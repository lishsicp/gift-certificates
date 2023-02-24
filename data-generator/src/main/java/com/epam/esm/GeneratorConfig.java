package com.epam.esm;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.epam.esm")
public class GeneratorConfig {
    @Bean
    public Faker getFaker() {
        return new Faker();
    }
}

