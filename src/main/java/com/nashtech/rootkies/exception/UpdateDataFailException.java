package com.nashtech.rootkies.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UpdateDataFailException extends Exception {

    private static final long serialVersionUID = 2L;

    public UpdateDataFailException(String message){
        super(message);
    }
}
