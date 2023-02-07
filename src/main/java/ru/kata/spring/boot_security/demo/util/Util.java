package ru.kata.spring.boot_security.demo.util;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.exception.IllegalUserFieldsException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.HashSet;
import java.util.Set;

@Component
public class Util {

    private final UserService service;
    
    private final RoleService roleService;
    
    @Autowired
    public Util(UserService service, RoleService roleService) {
        this.service = service;
        this.roleService = roleService;
    }

    @PostConstruct
    public void fillTable() {
        roleService.saveRole(new Role("ADMIN"));
        roleService.saveRole(new Role("USER"));

        Set<Role> set1 = roleService.getAllRoles();
        Set<Role> set2 = new HashSet<>();
        set2.add(roleService.getUserRole());

        String password = "user";
        
        try {
            service.addUser(new User("user1", password, true, set1));
            service.addUser(new User("user2", password, true, set2));
            service.addUser(new User("user3", password, true, set2));
            service.addUser(new User("user4", password, true, set1));
            service.addUser(new User("user5", password, true, set2));
        } catch (IllegalUserFieldsException ignored) {}
        
        
    }

    @PreDestroy
    public void cleanTable() {
        service.cleanUser();
        roleService.cleanRoles();
    }

}
