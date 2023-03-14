package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import java.util.Set;

@Controller
public class AdminController {

    private final RoleService roleService;

    public AdminController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping(value = "/admin")
    public String printUsers(ModelMap model) {
        User newUser = new User();
        Set<Role> roles = roleService.getAllRoles();
        newUser.setRoles(roles);
        model.addAttribute("newuser", newUser);
        model.addAttribute("selectableRoles", roles);
        return "admin";
    }
}

