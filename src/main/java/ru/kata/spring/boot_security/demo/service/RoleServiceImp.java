package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

@Service
public class RoleServiceImp implements RoleService {
    
    private final RoleRepository repository;
    
    @Autowired
    public RoleServiceImp(RoleRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public Role getRoleById(Long id) {
        return repository.findById(id).get();
    }
    
    @Override
    public void saveRole(Role role) {
        repository.save(role);
    }
    
    @Override
    public void cleanRoles() {
        repository.deleteAll();
    }
    
    @Override
    public Role getRoleByName(String role) {
        return repository.findByRole(role).get();
    }
}
