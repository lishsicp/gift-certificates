package com.epam.esm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication(scanBasePackages = "com.epam.esm")
public class ApplicationRunner {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ApplicationRunner.class, args);
        DispatcherServlet dispatcherServlet = context.getBean("dispatcherServlet", DispatcherServlet.class);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
    }
}
