package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.Optional;

@Controller
public class UserController {
    
    private final UserService service;
    
    public UserController(UserService service) {
        this.service = service;
    }
    
    @GetMapping(value = "/user")
    public String printUser(ModelMap model, Authentication auth) {
        Optional<User> optionalUser = service.getUserByName(auth.getName());
        model.addAttribute("user", optionalUser.orElseThrow());
        return "user";
    }

}
