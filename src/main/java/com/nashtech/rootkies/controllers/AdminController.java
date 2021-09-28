package com.nashtech.rootkies.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import com.nashtech.rootkies.constants.SuccessCode;
import com.nashtech.rootkies.converter.*;
import com.nashtech.rootkies.dto.auth.JwtResponse;
import com.nashtech.rootkies.dto.common.ResponseDTO;
import com.nashtech.rootkies.dto.user.request.ChangePasswordRequest;
import com.nashtech.rootkies.dto.user.request.PasswordRequest;
import com.nashtech.rootkies.service.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/admin")
@Tag(name = "ADMIN", description = "ADMIN API")
public class AdminController {

        @Autowired
        private UserService userService;

        @Autowired
        private UserConverter userConverter;

        @Operation(summary = "Test call api", description = "", tags = { "ADMIN" }, security = {
                        @SecurityRequirement(name = "bearer-key-admin") })
        @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
                        @ApiResponse(responseCode = "400", description = "Bad request"),
                        @ApiResponse(responseCode = "401", description = "Unauthorized"),
                        @ApiResponse(responseCode = "403", description = "Forbidden"),
                        @ApiResponse(responseCode = "404", description = "Not found"),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error") })
        @GetMapping("/home")
        @PreAuthorize("hasRole('ADMIN')")
        public String getHome() {
                return "<h1>ADMIN Home Page</h1>";
        }

        @Operation(summary = "Admin change password first login", description = "", tags = { "ADMIN" }, security = {
                        @SecurityRequirement(name = "bearer-key-admin") })
        @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
                        @ApiResponse(responseCode = "400", description = "Bad request"),
                        @ApiResponse(responseCode = "401", description = "Unauthorized"),
                        @ApiResponse(responseCode = "403", description = "Forbidden"),
                        @ApiResponse(responseCode = "404", description = "Not found"),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error") })
        @PutMapping("/password/first")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ResponseDTO> changePasswordFirstLogin(@RequestBody PasswordRequest passwordRequest) {
                JwtResponse response = userService.changePasswordFirstLogin(passwordRequest);
                ResponseDTO dto = new ResponseDTO();
                dto.setData(response);
                dto.setSuccessCode(SuccessCode.CHANGE_PASSWORD_SUCCESS);
                return ResponseEntity.ok(dto);
        }

        // changepssword
        @Operation(summary = "Admin change password", description = "", tags = { "ADMIN" }, security = {
                        @SecurityRequirement(name = "bearer-key-admin") })
        @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
                        @ApiResponse(responseCode = "400", description = "Bad request"),
                        @ApiResponse(responseCode = "401", description = "Unauthorized"),
                        @ApiResponse(responseCode = "403", description = "Forbidden"),
                        @ApiResponse(responseCode = "404", description = "Not found"),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error") })
        @PutMapping("/password/{username}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ResponseDTO> changePassword(@PathVariable("username") String username,
                        @RequestBody ChangePasswordRequest request) {
                String message = userService.changePassword(username, request);
                ResponseDTO response = new ResponseDTO();
                response.setData(message);
                response.setSuccessCode(SuccessCode.CHANGE_PASSWORD_SUCCESS);
                return ResponseEntity.ok(response);
        }
}
