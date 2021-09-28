package com.nashtech.rootkies.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.nashtech.rootkies.constants.ErrorCode;
import com.nashtech.rootkies.constants.SuccessCode;
import com.nashtech.rootkies.converter.CategoryConverter;
import com.nashtech.rootkies.dto.common.ResponseDTO;
import com.nashtech.rootkies.exception.ConvertEntityDTOException;
import com.nashtech.rootkies.exception.CreateDataFailException;
import com.nashtech.rootkies.model.Category;
import com.nashtech.rootkies.repository.CategoryRepository;
import com.nashtech.rootkies.service.CategoryService;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryConverter categoryConverter;

    @Test
    public void saveCategorySuccess() throws CreateDataFailException, ConvertEntityDTOException {
        assertNotNull(categoryRepository);
        // Given
        Category category = new Category();
        category.setCategoryCode("TE");
        category.setCategoryName("test");
        ResponseDTO responseDTOexpect = new ResponseDTO();
        responseDTOexpect.setSuccessCode(SuccessCode.CATEGORY_CREATED_SUCCESS);
        responseDTOexpect.setData(categoryConverter.convertEntityToBasicDTO(category));

        when(categoryRepository.save(category)).thenReturn(category);

        // When
        ResponseDTO responseDTO = categoryService.saveCategory(category);

        // Then
        assertEquals(responseDTOexpect, responseDTO);
    }
}
