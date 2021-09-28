package com.nashtech.rootkies.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.nashtech.rootkies.dto.auth.JwtResponse;
import com.nashtech.rootkies.dto.auth.LoginRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthServiceTest {
    
    @Autowired
    AuthService authService;

    /*@Test
    public void signInTest() {
        JwtResponse response = authService.signIn(
            new LoginRequest("thuyht", "123123"));
        assertNotNull(response.getToken());
    }*/
}
