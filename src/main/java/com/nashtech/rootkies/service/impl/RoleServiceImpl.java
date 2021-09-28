package com.nashtech.rootkies.service.impl;

import com.nashtech.rootkies.converter.RoleConverter;
import com.nashtech.rootkies.dto.role.RoleDTO;
import com.nashtech.rootkies.repository.RoleRepository;
import com.nashtech.rootkies.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    @Autowired
    private final RoleRepository repository;

    @Autowired
    private final RoleConverter converter;

    @Override
    public List<RoleDTO> getAllRole() {
        return converter.entitiesToDto(repository.findAll(Sort.by("roleName"))) ;
    }
}
