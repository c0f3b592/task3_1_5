package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImp implements UserService {
    
    private final UserRepository repository;
    
    @Autowired
    public UserServiceImp(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void addUser(User user) {
        repository.save(user);
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
    public User getUserById(long id) {
        return repository.findById(id).get();
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        repository.save(user);
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
    public User getUserByName(String name) {
        return repository.findUserByUsername(name).get();
    }
    
}
