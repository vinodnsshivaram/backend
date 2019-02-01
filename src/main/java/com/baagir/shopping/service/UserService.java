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
            throw new DuplicateItemException("userName : " + user.getUserName() + " or emailAddress : " + user.getEmailAddress() + " or both are used.");

        user.setId(UUID.randomUUID().toString());
        User savedUser = repository.save(user);
        return savedUser;
    }

    public void updatePassword(User user) {
        User fromDB = findByUserName(user.getUserName());
        fromDB.setPassword(user.getPassword());
        repository.save(fromDB);
    }

    public User findByEmailAddress(String emailAddress) {
        User fromDB = repository.findByEmailAddress(emailAddress);
        if (fromDB == null)
            throw new ItemNotFoundException("User with email address : " + emailAddress + " was not found");
        return fromDB;
    }

    public User findByUserName(String userName) {
        User fromDB = repository.findByUserName(userName);
        if (fromDB == null)
            throw new ItemNotFoundException("User with userName : " + userName + " was not found");
        return fromDB;
    }
}
