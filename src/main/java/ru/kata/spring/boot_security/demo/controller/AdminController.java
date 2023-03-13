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
import java.util.Set;

@Controller
public class AdminController {
    
    private final UserService service;
    
    private final RoleService roleService;
    
    
    public AdminController(UserService service, RoleService roleService) {
        this.service = service;
        this.roleService = roleService;
    }

    @GetMapping(value = "/getUsers")
    @ResponseBody
    public List<User> userList() {
        return service.getUserList();
    }

    @GetMapping(value = "/getUserByID/{id}")
    @ResponseBody
    public User userByID(@PathVariable Long id) {
        System.out.println(id);
        return service.getUserById(id).orElseThrow();
    }

    @GetMapping(value = "/getRoles")
    @ResponseBody
    public Set<Role> getRoles() {
        return roleService.getAllRoles();
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

    @PostMapping(value = "/admin")
    public void saveUser(@RequestBody User user) {
        try {
            service.addUser(user);
        } catch (IllegalUserFieldsException ignored) {}
    }

    @DeleteMapping(value = "/admin/{id}")
    @ResponseBody
    public void deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
    }

    @PatchMapping(value = "/admin")
    public void updateUser(@RequestBody User user) {
        try {
            service.updateUser(user);
        } catch (IllegalUserFieldsException ignored) {}
    }
}

