package com.nashtech.rootkies.controller;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.nio.charset.Charset;
import java.util.Optional;

import com.nashtech.rootkies.converter.LocationConverter;
import com.nashtech.rootkies.model.Category;
import com.nashtech.rootkies.repository.CategoryRepository;
import com.nashtech.rootkies.security.jwt.JwtUtils;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private LocationConverter locationConverter;

    @Test
    // @WithMockUser(username = "admin" , roles={"",""} )
    @WithMockUser(username = "test", roles = { "ADMIN" })
    public void createCategrory() throws Exception {

        when(jwtUtils.getUserNameFromJwtToken(Mockito.anyString())).thenReturn("test");
        when(locationConverter.getLocationIdFromUsername(Mockito.anyString())).thenReturn((long) 1);
        when(categoryRepository.findByCategoryName(Mockito.anyString())).thenReturn(Optional.of(new Category()));

        String createCategoryRequest = "{\"categoryCode\":\"BB\",\"categoryName\":\"bbb\"}";
        String error = this.mockMvc
                .perform(post("/category").contentType(APPLICATION_JSON_UTF8).content(createCategoryRequest))
                .andExpect(status().is(400)).andReturn().getResolvedException().getMessage();

        assertTrue(error.contains("ERR_CATEGORY_NAME_EXISTED"));
    }

}
