package com.nashtech.rootkies.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nashtech.rootkies.controllers.AuthController;
import com.nashtech.rootkies.dto.auth.LoginRequest;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void signInSuccessTest() throws Exception {
        LoginRequest loginRequest = new LoginRequest("thuyht", "123123");

        mockMvc.perform(post("/signin")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk());
    }
   
}
