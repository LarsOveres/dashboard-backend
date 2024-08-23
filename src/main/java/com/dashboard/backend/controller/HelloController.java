package com.dashboard.backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Value("${spring.application.name}")
    private String appName;

    @GetMapping("/hello")
    public String home() {
        return "Welcome to " + appName;
    }

    @GetMapping("/admin")
    public String showAdmin() {
        return "Dit is de admin pagina";
    }

    @GetMapping("/user")
    public String showUser() {
        return "Dit is de user pagina";
    }
}