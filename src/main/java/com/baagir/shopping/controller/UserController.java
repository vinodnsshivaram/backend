package com.baagir.shopping.controller;

import com.baagir.shopping.domain.AbstractLinkableEntity;
import com.baagir.shopping.domain.resources.User;
import com.baagir.shopping.service.UserService;
import com.baagir.shopping.web.RequestThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.ParseException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@SuppressWarnings({ "unchecked", "rawtypes" })
@RestController
@EnableAutoConfiguration
@RequestMapping("/users")
public class UserController extends AbstractController {
    @Autowired
    private UserService service;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;



//    public UserController(UserService service) {
//        this.service = service;
//    }

    @RequestMapping(method= RequestMethod.POST)
    public HttpEntity<User> signup(@RequestBody @Valid User user, HttpServletResponse response) throws ParseException {
        user.cleanup();
        User savedUser = service.signup(user);

        log.debug("DIONYSUS_NOTIFICATIONS : BatchMessage POST : Response Headers : [ HAL : "+response.getHeader("Location")
                +", Correlation-Id : "+ RequestThreadLocal.getCorrelationId()
                +" ] Response Body : "+savedUser.toString());


        Resource<AbstractLinkableEntity> resource = new Resource<>(savedUser);
        resource.add(getUsersSelfLink(savedUser.getId()));
        return new ResponseEntity(resource, HttpStatus.CREATED);
    }

    @GetMapping(path = "/{emailAddress}")
    public HttpEntity<User> readByEmail(@PathVariable String emailAddress) throws ParseException {
        User fromDB = service.findByEmailAddress(emailAddress);
        Resource<AbstractLinkableEntity> resource = new Resource<>(fromDB);
        resource.add(getUsersSelfLink(fromDB.getId()));
        return new ResponseEntity(resource, HttpStatus.OK);
    }

    @PutMapping(path = "/users/{emailAddress}")
    @ResponseBody
    public HttpEntity<User> updatePassword(@PathVariable String emailAddress, @RequestBody @Valid User user, Errors errors) {
        if (!emailAddress.equalsIgnoreCase(user.getEmailAddress()))
            return ResponseEntity.badRequest().build();
        service.updatePassword(user);
        return ResponseEntity.noContent().build();
    }

    private Link getUsersSelfLink(String emailAddress) throws ParseException {
        Link selfLink = new Link(ControllerLinkBuilder.linkTo(methodOn(UserController.class).readByEmail(emailAddress)).toUriComponentsBuilder().scheme("https").build().toUriString(), Link.REL_SELF);
        return selfLink;
    }
}
