package com.nashtech.rootkies.service.impl;

import com.nashtech.rootkies.constants.ErrorCode;
import com.nashtech.rootkies.dto.auth.JwtResponse;
import com.nashtech.rootkies.dto.auth.LoginRequest;
import com.nashtech.rootkies.dto.auth.SignUpRequest;
import com.nashtech.rootkies.enums.Gender;
import com.nashtech.rootkies.exception.custom.ApiRequestException;
import com.nashtech.rootkies.model.Location;
import com.nashtech.rootkies.model.Role;
import com.nashtech.rootkies.model.User;
import com.nashtech.rootkies.repository.LocationRepository;
import com.nashtech.rootkies.repository.RoleRepository;
import com.nashtech.rootkies.repository.UserRepository;
import com.nashtech.rootkies.security.jwt.JwtUtils;
import com.nashtech.rootkies.security.service.UserDetailsImpl;
import com.nashtech.rootkies.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    LocationRepository locationRepository;

    @Override
    public JwtResponse signIn(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if(userDetails.getRole().equalsIgnoreCase("ROLE_ADMIN_LOCKED") || 
            userDetails.getRole().equalsIgnoreCase("ROLE_USER_LOCKED"))
        {
            throw new ApiRequestException(ErrorCode.USER_BLOCKED);
        }
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());
        
        return new JwtResponse(jwt, userDetails.getStaffCode(), 
            userDetails.getUsername(), roles.get(0), userDetails.getFirstLogin(),
            userDetails.getIdLocation());
    }

    @Override
    public String fakeSignUp(SignUpRequest signUpRequest) {
        String staffCode = signUpRequest.getStaffcode();
        String username = signUpRequest.getUsername();
        String password = signUpRequest.getPassword();
        String firstName = signUpRequest.getFirstname();
        String lastName = signUpRequest.getLastname();
        Long idRole = signUpRequest.getIdRole();
        Long idLocation = signUpRequest.getIdLocation();

        Role role = roleRepository.findById(idRole).get();
        Location  location = locationRepository.findById(idLocation).get();

        // Optional<Role> role = roleRepository.findById(1L);
        // Optional<Location>  location = locationRepository.findById(1L);
        LocalDateTime current = LocalDateTime.now();
        User user = new User();
        user.setStaffCode(staffCode);
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setGender(Gender.Male);
        user.setDateOfBirth(current);
        user.setJoinedDate(current);
        user.setRole(role);
        user.setLocation(location);
        user.setFirstLogin(false);
        user.setIsDeleted(false);
        
        user = userRepository.save(user);
        if(user != null) {
            return "OK";
        }
        else{
            return "ERROR";
        }
    }

}
