package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.exception.IllegalUserFieldsException;
import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    void addUser(User user) throws IllegalUserFieldsException;
    
    List<User> getUserList();
    
    Optional<User> getUserById(long id);

    void updateUser(User user) throws IllegalUserFieldsException;

    void deleteUser(long id);

    void cleanUser();
    
    Optional<User> getUserByName(String user3);
}
