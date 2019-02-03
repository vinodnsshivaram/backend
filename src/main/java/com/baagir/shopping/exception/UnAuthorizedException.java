package com.baagir.shopping.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnAuthorizedException extends AuthorizationServiceException {
    public UnAuthorizedException(String msg) {
        super(msg);
    }
}
