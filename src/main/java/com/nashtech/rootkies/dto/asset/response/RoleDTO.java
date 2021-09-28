package com.nashtech.rootkies.dto.asset.response;

import com.nashtech.rootkies.enums.ERole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoleDTO {
    private Long id;

    private ERole roleName;
}
