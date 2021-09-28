package com.nashtech.rootkies.service;

import com.nashtech.rootkies.dto.auth.JwtResponse;
import com.nashtech.rootkies.dto.auth.LoginRequest;
import com.nashtech.rootkies.dto.auth.SignUpRequest;

public interface AuthService {

    public JwtResponse signIn(LoginRequest loginRequest);

    public String fakeSignUp(SignUpRequest signUpRequest);
}
