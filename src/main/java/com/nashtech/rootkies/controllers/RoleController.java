package com.nashtech.rootkies.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import com.nashtech.rootkies.constants.ErrorCode;
import com.nashtech.rootkies.constants.SuccessCode;
import com.nashtech.rootkies.dto.common.ResponseDTO;
import com.nashtech.rootkies.service.RoleService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/role")
@AllArgsConstructor
@Tag(name = "ROLE", description = "ROLE API")
public class RoleController {

    @Autowired
    private final RoleService service;

    @Operation(summary = "Get all role", description = "", tags = { "ROLE" }, security = {
            @SecurityRequirement(name = "bearer-key-admin"), @SecurityRequirement(name = "bearer-key-user") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> getAllRoles() {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            responseDTO.setData(service.getAllRole());
            responseDTO.setSuccessCode(SuccessCode.GET_ALL_ROLE_SUCCESS);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception ex) {
            responseDTO.setErrorCode(ErrorCode.ERR_GET_ALL_ROLE);
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
