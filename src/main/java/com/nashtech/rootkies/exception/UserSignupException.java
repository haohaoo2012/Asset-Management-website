package com.nashtech.rootkies.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserSignupException extends Exception {
    public UserSignupException(String message) {
    }
}
