package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;
import java.util.Optional;
import java.util.Set;

public interface RoleService {
    
    Optional<Role> getRoleById(Long id);
    
    void saveRole(Role role);
    
    void cleanRoles();
    
    Set<Role> getAllRoles();
    
    Role getUserRole();
    
    Role getAdminRole();
}
