package ru.kata.spring.boot_security.demo.controller;

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
    public String printUsers(ModelMap model) {
        List<User> users = service.getUserList();
        model.addAttribute("users", users);
        return "admin";
    }
    
    @GetMapping(value = "/userinfo", params = {"newuser"})
    public String addUser(ModelMap model) {
        User user = new User();
        Set<Role> roles = roleService.getAllRoles();
        user.setRoles(roles);
        model.addAttribute("user", user);
        model.addAttribute("selectableRoles", roles);
        model.addAttribute("action", "new");
        return "userinfo";
    }
    
    @PostMapping(value = "/userinfo")
    public String saveUser(@ModelAttribute("user") User user) {
        try {
            service.addUser(user);
        } catch (IllegalUserFieldsException ignored) {}
        return "redirect:/admin";
    }
    
    @GetMapping(value = "/admin/{id}")
    public String updateUser(ModelMap model, @PathVariable Long id) {
        Optional<User> userOptional = service.getUserById(id);
        if (userOptional.isPresent()) {
            Set<Role> roles = roleService.getAllRoles();
            model.addAttribute("user", userOptional.get());
            model.addAttribute("selectableRoles", roles);
            model.addAttribute("action", "update");
            return "userinfo";
        }
        return "redirect:/admin";
    }
    
    @PatchMapping(value = "/userinfo", params = {"update"})
    public String updateUser(@ModelAttribute User user) {
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
