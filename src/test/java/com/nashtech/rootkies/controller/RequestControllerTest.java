package com.nashtech.rootkies.controller;

import com.nashtech.rootkies.constants.ErrorCode;
import com.nashtech.rootkies.converter.LocationConverter;
import com.nashtech.rootkies.dto.request.request.CreateRequestDTO;
import com.nashtech.rootkies.exception.DataNotFoundException;
import com.nashtech.rootkies.repository.AssignmentRepository;
import com.nashtech.rootkies.repository.RequestRepository;
import com.nashtech.rootkies.security.jwt.JwtUtils;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class RequestControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestRepository requestRepository;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private LocationConverter locationConverter;

    @Test
    @WithMockUser(username = "test", roles = { "ADMIN" })
    public void createRequestControllerTest() throws Exception {
        when(jwtUtils.getUserNameFromJwtToken(Mockito.anyString())).thenReturn("test");
        when(locationConverter.getLocationIdFromUsername(Mockito.anyString())).thenReturn((long) 1);

        String createRequestDTO = "{\"assignment\": 1,\"requestedBy\": \"SD0002\"}";
        String error = this.mockMvc.perform(post("/request/save").contentType(APPLICATION_JSON_UTF8)
                .content(createRequestDTO)
                .header("Authorization",
                        "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJuaGltaCIsImlhdCI6MTYyODgyNjc0MCwiZXhwIjoxNjI4OTEzMTQwfQ.tbUR1aq0W1uiXL-HeLMqZ0PYH6Sc-o9-qmBMWTYe1iOvk5G1j5jTrfKWQ0xrvpI-jsVpIhlohp3-7HxUJn5Iig"))
                .andExpect(status().is(400)).andReturn().getResolvedException().getMessage();

        assertTrue(error.contains(ErrorCode.ERR_CREATE_REQUEST_NOT_ALLOW));
    }
}
