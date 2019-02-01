package com.baagir.shopping.service;

import com.baagir.shopping.domain.resources.User;
import com.baagir.shopping.exception.DuplicateItemException;
import com.baagir.shopping.exception.ItemNotFoundException;
import com.baagir.shopping.repository.UserRepository;
import com.strategicgains.syntaxe.ValidationEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User signup(User user) {
        ValidationEngine.validate(user);
        user.setId(UUID.randomUUID().toString());
        User savedUser = null;
        try {
            savedUser = repository.save(user);
        } catch (DuplicateKeyException e) {
            throw new DuplicateItemException("userName : " + user.getUserName() + " or emailAddress : " + user.getEmailAddress() + " or both are used.");
        }
        return savedUser;
    }

    public void updatePassword(User user) {
        User fromDB = findByEmailAddress(user.getEmailAddress());
        fromDB.setPassword(user.getPassword());
        repository.save(fromDB);
    }

    public User findByEmailAddress(String emailAddress) {
        User fromDB = repository.findByEmailAddress(emailAddress);
        if (fromDB == null)
            throw new ItemNotFoundException("User with email address : " + emailAddress + " was not found");
        return fromDB;
    }
}
