package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.exception.IllegalUserFieldsException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.HashSet;
import java.util.Set;

@Controller
public class AnonController {
    
    private final UserService service;
    
    private final RoleService roleService;
    
    
    public AnonController(UserService service, RoleService roleService) {
        this.service = service;
        this.roleService = roleService;
    }
    
    @GetMapping(value = "/")
    public String printWelcome(ModelMap model, Authentication auth) {
        if (auth != null) {
            model.addAttribute("username", auth.getName());
        }
        return "index";
    }
    
    @GetMapping(value = "/reg")
    public String regPage(ModelMap model) {
        User user = new User();
        user.setRoles(new HashSet<>());
        model.addAttribute("user", user);
        return "reg";
    }
    
    @PostMapping(value = "/reg")
    public String createUser(@ModelAttribute User user) {
        try {
            Set<Role> roles = new HashSet<>();
            roles.add(roleService.getUserRole());
            user.setRoles(roles);
            user.setEnabled(true);
            service.addUser(user);
            return "redirect:/index";
        } catch (IllegalUserFieldsException ignored) {}
        return "redirect:/reg";
    }
}
