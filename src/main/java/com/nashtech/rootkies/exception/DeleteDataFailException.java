package com.nashtech.rootkies.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DeleteDataFailException extends Exception {

    private static final long serialVersionUID = 3L;

    public DeleteDataFailException(String message) {
        super(message);
    }

}