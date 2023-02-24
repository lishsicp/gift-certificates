package com.epam.esm.service;

public interface DataGeneratorService {
    void populateCertificates();
    void populateTags();
    void populateOrders();
    void populateUsers();

    default void populateAll() {
        populateTags();
        populateCertificates();
        populateUsers();
        populateOrders();
    }
}
