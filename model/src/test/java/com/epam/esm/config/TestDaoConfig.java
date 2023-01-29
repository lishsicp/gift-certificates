package com.epam.esm.config;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

@EnableAutoConfiguration
@ComponentScan("com.epam.esm")
@EntityScan("com.epam.esm")
@Profile("test")
public class TestDaoConfig {
}
