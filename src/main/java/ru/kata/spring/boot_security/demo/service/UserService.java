package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;

public interface UserService {

    void addUser(User user);

    List<User> getUserList();

    User getUserById(long id);

    void updateUser(User user);

    void deleteUser(long id);

    void cleanUser();
    
    User getUserByName(String user3);
}
