package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.exception.IllegalUserFieldsException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class RestControllers {

    private UserService service;

    private RoleService roleService;

    public RestControllers(UserService service, RoleService roleService) {
        this.service = service;
        this.roleService = roleService;
    }

    @GetMapping(value = "/admin/getUsers")
    public List<User> userList() {
        return service.getUserList();
    }

    @GetMapping(value = "/admin/getUserByID/{id}")
    public User userByID(@PathVariable Long id) {
        System.out.println(id);
        return service.getUserById(id).orElseThrow();
    }

    @GetMapping(value = "/admin/getRoles")
    public Set<Role> getRoles() {
        return roleService.getAllRoles();
    }

    @PostMapping(value = "/admin")
    public void saveUser(@RequestBody User user) {
        try {
            service.addUser(user);
        } catch (IllegalUserFieldsException ignored) {}
    }

    @DeleteMapping(value = "/admin/{id}")
    public void deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
    }

    @PatchMapping(value = "/admin")
    public void updateUser(@RequestBody User user) {
        try {
            service.updateUser(user);
        } catch (IllegalUserFieldsException ignored) {}
    }

    @GetMapping(value = "user/getAuthUser")
    public User authUser(Authentication auth) {
        Optional<User> optionalUser = service.getUserByName(auth.getName());
        return optionalUser.orElseThrow();
    }
}
