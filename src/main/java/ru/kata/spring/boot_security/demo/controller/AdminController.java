package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {
    @GetMapping(value = "/admin")
    public String printUsers() {
        return "admin";
    }
}

