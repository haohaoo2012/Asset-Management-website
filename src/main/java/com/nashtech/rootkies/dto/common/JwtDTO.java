package com.nashtech.rootkies.dto.common;

import lombok.Data;

@Data
public class JwtDTO {

    private String token;
    private String type;
    private Long id;
    private String email;
    private String role;

}
