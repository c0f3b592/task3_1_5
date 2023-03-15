package ru.kata.spring.boot_security.demo.controller;


import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.Optional;

@RestController
public class RestUserController {

    private UserService service;

    public RestUserController(UserService service) {
        this.service = service;
    }

    @GetMapping(value = "user/getAuthUser")
    public User authUser(Authentication auth) {
        Optional<User> optionalUser = service.getUserByName(auth.getName());
        return optionalUser.orElseThrow();
    }
}
