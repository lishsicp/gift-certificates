package com.epam.esm.controller;

import com.epam.esm.service.DataGeneratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeneratorController {

    DataGeneratorService generator;

    public GeneratorController(DataGeneratorService generator) {
        this.generator = generator;
    }


    @GetMapping("api/populate")
    ResponseEntity<Object> populate() {
        generator.populateAll();
        return ResponseEntity.ok("Ok");
    }
}
