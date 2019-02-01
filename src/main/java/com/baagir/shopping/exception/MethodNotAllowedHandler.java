package com.baagir.shopping.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class MethodNotAllowedHandler {
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    protected Object methodNotSupportedErrorHandler(HttpServletRequest request, Exception exception) throws Exception {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(request.getPathInfo());
    }
}
