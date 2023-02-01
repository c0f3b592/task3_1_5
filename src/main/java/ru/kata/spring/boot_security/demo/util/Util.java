package ru.kata.spring.boot_security.demo.util;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.ArrayList;
import java.util.List;

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
    @Transactional
    public void fillTable() {
        roleService.saveRole(new Role("ADMIN"));
        roleService.saveRole(new Role("USER"));
        
        List<Role> list1 = new ArrayList<>();
        list1.add(roleService.getRoleByName("ADMIN"));
        list1.add(roleService.getRoleByName("USER"));
        
        List<Role> list2 = new ArrayList<>();
        list2.add(roleService.getRoleByName("USER"));
        
        service.addUser(new User("user1", "{noop}user", true, list1));
        service.addUser(new User("user2", "{noop}user", true, list2));
        service.addUser(new User("user3", "{noop}user", true, list2));
        service.addUser(new User("user4", "{noop}user", true, list1));
        service.addUser(new User("user5", "{noop}user", true, list2));
    }

    @PreDestroy
    @Transactional
    public void cleanTable() {
        service.cleanUser();
        roleService.cleanRoles();
    }

}
