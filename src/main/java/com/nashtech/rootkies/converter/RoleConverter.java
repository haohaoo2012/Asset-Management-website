package com.nashtech.rootkies.converter;

import com.nashtech.rootkies.dto.role.RoleDTO;
import com.nashtech.rootkies.model.Role;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleConverter {

    public RoleDTO entityToDto(Role role){
        return RoleDTO.builder()
                .id(role.getId())
                .name(role.getRoleName().toString())
                .build();
    }

    public List<RoleDTO> entitiesToDto(List<Role> roles){
        return roles.stream()
                .map((role) -> entityToDto(role))
                .collect(Collectors.toList());
    }
}
