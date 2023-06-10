package com.epam.esm.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    @GetMapping("/")
    public String login() {
        return "logout";
    }
}