package com.CapBackEnd.backend.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/api/status")
    public String healthCheck() {
        return "Server is running! (Spring Boot + MSA)";
    }
}
