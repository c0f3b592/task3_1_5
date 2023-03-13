package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
    public String printUser() {
        return "user";
    }

    @GetMapping(value = "/getAuthUser")
    @ResponseBody
    public User authUser(Authentication auth) {
        Optional<User> optionalUser = service.getUserByName(auth.getName());
        return optionalUser.orElseThrow();
    }

}
