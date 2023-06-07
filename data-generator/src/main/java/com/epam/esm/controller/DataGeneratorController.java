package com.epam.esm.controller;

import com.epam.esm.service.DataGeneratorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@RestController
@RequestMapping("api")
public class DataGeneratorController {

    private final File file;
    private final DataGeneratorService dataGeneratorService;

    public DataGeneratorController(DataGeneratorService dataGeneratorService) {
        this.dataGeneratorService = dataGeneratorService;
        this.file = createFile();
    }

    private File createFile() {
        String userHome = System.getProperty("user.dir");
        String filePath = userHome + File.separator + "populated.txt";
        return new File(filePath);
    }

    @GetMapping("tags/populate")
    @ResponseStatus(HttpStatus.OK)
    public void populateTags() {
        dataGeneratorService.populateTags();
    }

    @GetMapping("certificates/populate")
    @ResponseStatus(HttpStatus.OK)
    public void populateCertificates() {
        dataGeneratorService.populateCertificates();
    }

    @GetMapping("users/populate")
    @ResponseStatus(HttpStatus.OK)
    public void populateUsers() {
        dataGeneratorService.populateUsers();
    }

    @GetMapping("orders/populate")
    @ResponseStatus(HttpStatus.OK)
    public void populateOrders() {
        dataGeneratorService.populateOrders();
    }

    @GetMapping("populateAll")
    @ResponseStatus(HttpStatus.OK)
    public String populateAll() {
        if (!checkPopulated()) {
            dataGeneratorService.populateAll();
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("true");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Has been populated";
        }
        return checkPopulated() ? "Has been populated" : "Has not been populated";
    }

    private boolean checkPopulated() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String flag = reader.readLine();
            if (flag != null && flag.equals("true")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
