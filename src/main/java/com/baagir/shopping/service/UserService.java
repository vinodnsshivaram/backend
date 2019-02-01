package com.baagir.shopping.service;

import com.baagir.shopping.domain.resources.User;
import com.baagir.shopping.exception.DuplicateItemException;
import com.baagir.shopping.exception.ItemNotFoundException;
import com.baagir.shopping.repository.UserRepository;
import com.strategicgains.syntaxe.ValidationEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User signUp(User user) {
        ValidationEngine.validate(user);

        User fromDB = findByUserName(user.getUserName());
        if (fromDB != null)
            throw new DuplicateItemException("userName : " + user.getUserName() + " is already taken, please choose a different userName");

        user.setId(UUID.randomUUID().toString());
        User savedUser = repository.save(user);
        return savedUser;
    }

    private User findByUserNameAndEmailAddress(String userName, String emailAddress) {
        return repository.findByUserNameAndEmailAddress(userName, emailAddress);
    }

    public void updatePassword(User user) {
        User fromDB = findByUserName(user.getUserName());
        if (fromDB == null)
            throw new ItemNotFoundException("No user with " + user.getUserName() + " user name was found");
        fromDB.setPassword(user.getPassword());
        repository.save(fromDB);
    }

    public User findByEmailAddress(String emailAddress) {
        return repository.findByEmailAddress(emailAddress);
    }

    public User findByUserName(String userName) {
        return repository.findByUserName(userName);
    }
}
