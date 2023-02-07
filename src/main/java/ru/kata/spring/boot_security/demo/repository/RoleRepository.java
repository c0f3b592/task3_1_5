package ru.kata.spring.boot_security.demo.repository;

import org.springframework.data.repository.CrudRepository;
import ru.kata.spring.boot_security.demo.model.Role;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends CrudRepository<Role, Long> {
    
    Optional<Role> findByRole(String role);
    
    @Override
    Set<Role> findAll();
}
