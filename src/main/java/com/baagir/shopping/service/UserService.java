package com.baagir.shopping.service;

import com.baagir.shopping.model.User;
import com.baagir.shopping.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public User save(User user) {
        user = repository.save(user);
        return user;
    }

    public void updatePassword(User user) {
        User fromDB = repository.findByEmailAddress(user.getEmailAddress());
        fromDB.setPassword(user.getPassword());
        repository.save(fromDB);
    }
}
