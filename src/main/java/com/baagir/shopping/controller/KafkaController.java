package com.baagir.shopping.controller;

import com.baagir.shopping.domain.resources.User;
import com.baagir.shopping.service.KafkaProducerService;
import com.baagir.shopping.web.RequestThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.LinkedHashMap;

@SuppressWarnings({ "unchecked", "rawtypes" })
@RestController
@EnableAutoConfiguration
@RequestMapping("/messages")
public class KafkaController extends AbstractController {
    @Autowired
    KafkaProducerService producerService;


    @PostMapping(path = "/publish")
    public HttpEntity<User> publish(@RequestBody Object payload, HttpServletResponse response) throws ParseException {
        LinkedHashMap jsonPayload = (LinkedHashMap) payload;

        log.debug("ShoppingApplication : User POST : Response Headers : [ HAL : "+response.getHeader("Location")
                +", Correlation-Id : "+ RequestThreadLocal.getCorrelationId()
                +" ] Response Body : "+payload.toString());

        producerService.publish(jsonPayload.get("topic").toString(), jsonPayload.get("message").toString());

        return new ResponseEntity(HttpStatus.OK);
    }
}
