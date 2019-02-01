package com.baagir.shopping.controller;

import com.baagir.shopping.domain.AbstractLinkableEntity;
import com.baagir.shopping.domain.resources.User;
import com.baagir.shopping.service.UserService;
import com.baagir.shopping.web.RequestThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.ParseException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@SuppressWarnings({ "unchecked", "rawtypes" })
@RestController
@EnableAutoConfiguration
@RequestMapping("/users")
public class UserController extends AbstractController {
    @Autowired
    private UserService service;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping(path = "/users/signup}")
    public HttpEntity<User> signup(@RequestBody @Valid User user, HttpServletResponse response) throws ParseException {
        user.cleanup();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User savedUser = service.signUp(user);

        log.debug("ShoppingApplication : User POST : Response Headers : [ HAL : "+response.getHeader("Location")
                +", Correlation-Id : "+ RequestThreadLocal.getCorrelationId()
                +" ] Response Body : "+savedUser.toString());


        Resource<AbstractLinkableEntity> resource = new Resource<>(savedUser);
        resource.add(getUsersSelfLink(savedUser.getId()));
        return new ResponseEntity(resource, HttpStatus.CREATED);
    }

    @PostMapping(path = "/users/login}")
    public HttpEntity<User> login(@RequestBody User user, HttpServletResponse response) throws ParseException {
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{emailAddress}")
    public HttpEntity<User> readByEmail(@PathVariable String emailAddress, HttpServletResponse response) throws ParseException {
        User fromDB = service.findByEmailAddress(emailAddress);


        log.debug("ShoppingApplication : User readByEmail GET : Response Headers : [ HAL : " + response.getHeader("Location")
                + ", Correlation-Id : " + RequestThreadLocal.getCorrelationId()
                + " ] Response Body : " +fromDB.toString());


        Resource<AbstractLinkableEntity> resource = new Resource<>(fromDB);
        resource.add(getUsersSelfLink(fromDB.getId()));
        return new ResponseEntity(resource, HttpStatus.OK);
    }

    @PutMapping(path = "/users/{userName}")
    public HttpEntity<User> updatePassword(@PathVariable String userName, @RequestBody User user, HttpServletResponse response) {
        if (!userName.equalsIgnoreCase(user.getEmailAddress()))
            return ResponseEntity.badRequest().build();

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        service.updatePassword(user);

        log.debug("ShoppingApplication : User updatePassword PUT : Response Headers : [ HAL : " + response.getHeader("Location")
                + ", Correlation-Id : " + RequestThreadLocal.getCorrelationId()
                + " ] Response Body : No Response Body");

        return ResponseEntity.noContent().build();
    }

    private Link getUsersSelfLink(String path) {
        Link selfLink = new Link(linkTo(UserController.class).slash(path).toUriComponentsBuilder().scheme("https").build().toUriString(), Link.REL_SELF);
        return selfLink;
    }
}
