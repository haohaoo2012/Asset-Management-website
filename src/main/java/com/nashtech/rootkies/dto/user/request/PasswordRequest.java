package com.nashtech.rootkies.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PasswordRequest {
    private String staffCode;
    private String newPassword;
}
