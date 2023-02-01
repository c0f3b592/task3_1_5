package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;

public interface RoleService {
    
    Role getRoleById(Long id);
    
    void saveRole(Role role);
    
    void cleanRoles();
    
    Role getRoleByName(String role);
}
