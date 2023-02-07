package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.exception.IllegalUserFieldsException;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {
    
    private final UserRepository repository;
    
    private final PasswordEncoder encoder;
    
    @Autowired
    public UserServiceImp(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public void addUser(User user) throws IllegalUserFieldsException {
        if (    user.getUsername().isBlank() ||
                user.getPassword().isBlank()) {
            throw new IllegalUserFieldsException("invalid user fields");
        } else if (getUserByName(user.getUsername()).isPresent()) {
            throw new IllegalUserFieldsException("username is occupied");
        } else {
            user.setPassword(encoder.encode(user.getPassword()));
            repository.save(user);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getUserList() {
        List<User> list = new ArrayList<>();
        repository.findAll().forEach(list::add);
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public void updateUser(User user) throws IllegalUserFieldsException {
        if (    user.getUsername().isBlank() ||
                user.getPassword().isBlank()) {
            throw new IllegalUserFieldsException("invalid user fields");
        } else if ((user.getPassword())
                .equals(repository.
                        findById(user.getId())
                            .orElseThrow()
                            .getPassword())) {
            repository.save(user);
        } else {
            user.setPassword(encoder.encode(user.getPassword()));
            repository.save(user);
        }
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void cleanUser() {
        repository.deleteAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByName(String name) {
        return repository.findUserByUsername(name);
    }
    
}
