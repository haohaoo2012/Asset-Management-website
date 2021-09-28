package com.nashtech.rootkies.exception.custom;

public class ApiRequestException extends RuntimeException{
    
    public ApiRequestException(String message) {
        super(message);
    }

    public ApiRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
