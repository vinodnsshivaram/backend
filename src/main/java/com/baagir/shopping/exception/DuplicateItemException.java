package com.baagir.shopping.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateItemException extends RuntimeException {
    public DuplicateItemException(String msg) {
        super(msg);
    }
}
