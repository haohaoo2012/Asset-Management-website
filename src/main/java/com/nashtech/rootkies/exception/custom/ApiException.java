package com.nashtech.rootkies.exception.custom;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiException {
   
    private final String errorCode;
    private final HttpStatus httpsStatus;
    private final LocalDateTime time;
}
