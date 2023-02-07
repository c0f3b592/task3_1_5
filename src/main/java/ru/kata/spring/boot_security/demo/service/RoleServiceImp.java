package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleServiceImp implements RoleService {
    
    private final RoleRepository repository;
    
    @Autowired
    public RoleServiceImp(RoleRepository repository) {
        this.repository = repository;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Role> getRoleById(Long id) {
        return repository.findById(id);
    }
    
    @Override
    @Transactional
    public void saveRole(Role role) {
        repository.save(role);
    }
    
    @Override
    @Transactional
    public void cleanRoles() {
        repository.deleteAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Set<Role> getAllRoles() {
        return repository.findAll();
    }
    
    @Override
    public Role getUserRole() {
        Optional<Role> role = repository.findByRole("USER");
        return role.orElse(null);
    }
    
    @Override
    public Role getAdminRole() {
        Optional<Role> role = repository.findByRole("ADMIN");
        return role.orElse(null);
    }
}
