package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.exception.IllegalUserFieldsException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class AdminController {
    
    private final UserService service;
    
    private final RoleService roleService;
    
    
    public AdminController(UserService service, RoleService roleService) {
        this.service = service;
        this.roleService = roleService;
    }
    
    @GetMapping(value = "/admin")
    public String printUsers(ModelMap model, Authentication auth) {
        Optional<User> optionalUser = service.getUserByName(auth.getName());
        List<User> users = service.getUserList();
        User newUser = new User();
        Set<Role> roles = roleService.getAllRoles();
        newUser.setRoles(roles);
        model.addAttribute("users", users);
        model.addAttribute("headuser", optionalUser.orElseThrow());
        model.addAttribute("newuser", newUser);
        model.addAttribute("selectableRoles", roles);
        return "admin";
    }

    @PostMapping(value = "/admin")
    public String saveUser(@ModelAttribute("user") User user) {
        try {
            service.addUser(user);
        } catch (IllegalUserFieldsException ignored) {}
        return "redirect:/admin";
    }

    @PatchMapping(value = "/admin{id}{username}")
    public String updateUser(@ModelAttribute("newuser") User user) {
        try {
            service.updateUser(user);
        } catch (IllegalUserFieldsException ignored) {}

        return "redirect:/admin";
    }
    
    @DeleteMapping(value = "/admin/{id}")
    public String deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return "redirect:/admin";
    }
}
