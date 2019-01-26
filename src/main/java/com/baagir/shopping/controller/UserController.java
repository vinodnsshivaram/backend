package com.baagir.shopping.controller;

import com.baagir.shopping.model.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @PostMapping("/users/signup")
    @ResponseBody
    public User signup(String name) {
        return new User();
    }
}
