package com.epam.esm.extension;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class TestContainerExtension implements Extension, BeforeAllCallback, AfterAllCallback {

    @SuppressWarnings("rawtypes")
    private static final PostgreSQLContainer<?> container = (PostgreSQLContainer<?>) new PostgreSQLContainer(DockerImageName.parse("postgres").withTag("15.2-alpine")
    ).withDatabaseName("gifts")
            .withUsername("lobur")
            .withPassword("pass");

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        container.start();
        System.setProperty("spring.datasource.url", container.getJdbcUrl());
        System.setProperty("spring.datasource.username", container.getUsername());
        System.setProperty("spring.datasource.password", container.getPassword());
        System.setProperty("spring.liquibase.contexts", "!prod");
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {}
}
