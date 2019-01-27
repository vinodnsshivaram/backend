package com.baagir.shopping.controller;

import com.baagir.shopping.configuration.MongoConfiguration;
import com.baagir.shopping.model.User;
import com.baagir.shopping.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {
    @Autowired
    private MongoConfiguration configuration;
    @Autowired
    private UserService service;

    @PostMapping("/users/signup")
    @ResponseBody
    public ResponseEntity signup(@RequestBody @Valid User user, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getAllErrors().get(0));
        }
        user = service.save(user);
        return ResponseEntity.ok(user);
    }

    @PutMapping(path = "/users/{emailAddress}")
    @ResponseBody
    public ResponseEntity updatePassword(@PathVariable String emailAddress, @RequestBody @Valid User user, Errors errors) {
        if (!emailAddress.equalsIgnoreCase(user.getEmailAddress()))
            return ResponseEntity.badRequest().build();
        service.updatePassword(user);
        return ResponseEntity.noContent().build();
    }
}
