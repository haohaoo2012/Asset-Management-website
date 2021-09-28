package com.nashtech.rootkies.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nashtech.rootkies.constants.ErrorCode;
import com.nashtech.rootkies.constants.SuccessCode;
import com.nashtech.rootkies.converter.UserConverter;
import com.nashtech.rootkies.dto.auth.JwtResponse;
import com.nashtech.rootkies.dto.common.ResponseDTO;
import com.nashtech.rootkies.dto.user.request.ChangePasswordRequest;
import com.nashtech.rootkies.dto.user.request.CreateUserDTO;
import com.nashtech.rootkies.dto.user.request.EditUserDTO;
import com.nashtech.rootkies.dto.user.request.PasswordRequest;
import com.nashtech.rootkies.exception.ConvertEntityDTOException;
import com.nashtech.rootkies.exception.CreateDataFailException;
import com.nashtech.rootkies.exception.DataNotFoundException;
import com.nashtech.rootkies.exception.UpdateDataFailException;
import com.nashtech.rootkies.model.User;
import com.nashtech.rootkies.repository.specs.UserSpecificationBuilder;
import com.nashtech.rootkies.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user")
@Tag(name = "USER", description = "USER API")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    UserConverter userConverter;

    @Operation(summary = "Test call api", description = "", tags = { "USER" }, security = {
         @SecurityRequirement(name = "bearer-key-user") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @GetMapping("/home")
    @PreAuthorize("hasRole('USER')")
    public String getHome() {
        return "<h1>USER Home Page</h1>";
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Operation(summary = "Get all user", description = "", tags = { "USER" }, security = {
            @SecurityRequirement(name = "bearer-key-admin") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> getAllUser(@RequestParam Integer page, @RequestParam Integer size,
            @RequestParam String sort, @RequestParam String search) throws DataNotFoundException {
        ResponseDTO response = new ResponseDTO();

        Pageable pageable = null;
        if (sort.contains("ASC")) {
            pageable = PageRequest.of(page, size, Sort.by(sort.replace("ASC", "")).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sort.replace("DES", "")).descending());
        }

        UserSpecificationBuilder builder = new UserSpecificationBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        Specification<User> spec = builder.build();

        response.setData(userService.findAllUser(pageable, spec));

        response.setSuccessCode(SuccessCode.GET_USER_SUCCESS);
        return ResponseEntity.ok().body(response);

    }

    @Operation(summary = "User change password first login", description = "", tags = { "USER" }, security = {
            @SecurityRequirement(name = "bearer-key-user") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @PutMapping("/password/first")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseDTO> changePasswordFirstLogin(@RequestBody PasswordRequest passwordRequest) {
        JwtResponse response = userService.changePasswordFirstLogin(passwordRequest);
        ResponseDTO dto = new ResponseDTO();
        dto.setData(response);
        dto.setSuccessCode(SuccessCode.CHANGE_PASSWORD_SUCCESS);
        return ResponseEntity.ok(dto);
    }

    // changepssword
    @Operation(summary = "User change password", description = "", tags = { "USER" }, security = {
            @SecurityRequirement(name = "bearer-key-user") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @PutMapping("/password/{username}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseDTO> changePassword(@PathVariable("username") String username,
            @RequestBody ChangePasswordRequest request) {
        String message = userService.changePassword(username, request);
        ResponseDTO response = new ResponseDTO();
        response.setData(message);
        response.setSuccessCode(SuccessCode.CHANGE_PASSWORD_SUCCESS);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Check any valid assigment", description = "", tags = { "USER" }, security = {
            @SecurityRequirement(name = "bearer-key-admin") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @GetMapping("/checkHaveAssignment/{staffCode}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> checkAnyValidAssignment(@PathVariable String staffCode) {
        ResponseDTO response = new ResponseDTO();

        try {

            response.setData(userService.checkAnyValidAssignment(staffCode));
            response.setSuccessCode(SuccessCode.CHECK_HAVE_ASSIGNMENT_SUCCESS);
            return ResponseEntity.ok().body(response);
        } catch (Exception exception) {

            response.setErrorCode(ErrorCode.ERR_CHECK_VALID_ASSIGNMENT);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Disable user", description = "", tags = { "USER" }, security = {
            @SecurityRequirement(name = "bearer-key-admin") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @DeleteMapping("/{staffCode}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> disableUser(@PathVariable String staffCode) {
        ResponseDTO response = new ResponseDTO();

        try {

            userService.disableUser(staffCode);
            response.setSuccessCode(SuccessCode.DISABLE_USER_SUCCESS);
            return ResponseEntity.ok().body(response);

        } catch (Exception ex) {
            response.setErrorCode(ErrorCode.ERR_DISABLE_USER);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Find user", description = "", tags = { "USER" }, security = {
            @SecurityRequirement(name = "bearer-key-admin") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @GetMapping("/{staffcode}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> findUser(@PathVariable("staffcode") String staffCode)
            throws DataNotFoundException {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            Optional<User> user = userService.getUser(staffCode);

            responseDTO.setData(userConverter.convertToDto(user.get()));
            responseDTO.setSuccessCode(SuccessCode.FIND_USER_SUCCESS);
        } catch (Exception e) {
            throw new DataNotFoundException(ErrorCode.ERR_USER_NOT_FOUND);
        }
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "Create user", description = "", tags = { "USER" }, security = {
            @SecurityRequirement(name = "bearer-key-admin") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @PostMapping(value = "/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> createNewUser(@Valid @RequestBody CreateUserDTO createUserDTO)
            throws ConvertEntityDTOException, CreateDataFailException {
        ResponseDTO responseDTO = new ResponseDTO();
        User user = userConverter.convertCreateUserDTOtoEntity(createUserDTO);
        User saveUser = userService.createUser(user);
        responseDTO.setData(userConverter.entityToDetailDTO(saveUser));
        responseDTO.setSuccessCode(SuccessCode.USER_CREATED_SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @Operation(summary = "Update user", description = "", tags = { "USER" }, security = {
            @SecurityRequirement(name = "bearer-key-admin") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @PutMapping("/update/{staffcode}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> updateUser(@PathVariable(value = "staffcode") String staffcode,
            @Valid @RequestBody EditUserDTO editUserDTO) throws UpdateDataFailException {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            User user = userConverter.convertEditUserDTOtoEntity(editUserDTO);
            User updateUser = userService.updateUser(staffcode, user);
            responseDTO.setData(userConverter.entityToDetailDTO(updateUser));
            responseDTO.setSuccessCode(SuccessCode.USER_UPDATED_SUCCESS);
        } catch (Exception e) {
            throw new UpdateDataFailException(ErrorCode.ERR_UPDATE_USER_FAIL);
        }
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "Get all user assignemnt", description = "", tags = { "USER" }, security = {
            @SecurityRequirement(name = "bearer-key-admin") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @GetMapping("/assignment")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> getAllUserAssignment(@RequestParam Integer page, @RequestParam Integer size,
            @RequestParam String sort, @RequestParam String search) throws DataNotFoundException {
        ResponseDTO response = new ResponseDTO();

        Pageable pageable = null;
        if (sort.contains("ASC")) {
            pageable = PageRequest.of(page, size, Sort.by(sort.replace("ASC", "")).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sort.replace("DES", "")).descending());
        }

        UserSpecificationBuilder builder = new UserSpecificationBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        Specification<User> spec = builder.build();

        response.setData(userService.getAllUserInAssignment(pageable, spec));
        response.setSuccessCode(SuccessCode.GET_USER_SUCCESS);
        return ResponseEntity.ok().body(response);

    }
}
