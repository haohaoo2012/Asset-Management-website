package com.nashtech.rootkies.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private String staffcode;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private Long idLocation;
    private Long idRole;
}
