package com.nashtech.rootkies.service.impl;

import com.nashtech.rootkies.constants.ErrorCode;
import com.nashtech.rootkies.constants.SuccessCode;
import com.nashtech.rootkies.converter.CategoryConverter;
import com.nashtech.rootkies.dto.category.response.BasicCategoryDTO;
import com.nashtech.rootkies.dto.common.ResponseDTO;
import com.nashtech.rootkies.exception.CreateDataFailException;
import com.nashtech.rootkies.exception.DataNotFoundException;
import com.nashtech.rootkies.model.Category;
import com.nashtech.rootkies.repository.CategoryRepository;
import com.nashtech.rootkies.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryConverter categoryConverter;

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryConverter categoryConverter, CategoryRepository categoryRepository) {
        this.categoryConverter = categoryConverter;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ResponseDTO retrieveCategories() throws DataNotFoundException {
        try {
            ResponseDTO responseDto = new ResponseDTO();
            List<Category> categories;
            try {
                categories = categoryRepository.findAll(Sort.by("categoryName").ascending());
            } catch (Exception e) {
                throw new DataNotFoundException(ErrorCode.ERR_RETRIEVE_CATEGORY_FAIL);
            }
            List<BasicCategoryDTO> viewAssetsDTO = categoryConverter.convertToListBasicDTO(categories);

            responseDto.setData(viewAssetsDTO);
            responseDto.setSuccessCode(SuccessCode.CATEGORIES_LOADED_SUCCESS);
            return responseDto;
        } catch (Exception e) {
            throw new DataNotFoundException(ErrorCode.ERR_RETRIEVE_CATEGORY_FAIL);
        }
    }

    @Override
    public ResponseDTO saveCategory(Category category) throws CreateDataFailException {
        try {
            ResponseDTO responseDto = new ResponseDTO();

            Category categorySave;
            try {
                categorySave = categoryRepository.save(category);
            } catch (Exception e) {
                e.printStackTrace();
                throw new CreateDataFailException(ErrorCode.ERR_CREATE_CATEGORY_FAIL);
            }

            responseDto.setSuccessCode(SuccessCode.CATEGORY_CREATED_SUCCESS);
            responseDto.setData(categoryConverter.convertEntityToBasicDTO(categorySave));
            return responseDto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CreateDataFailException(ErrorCode.ERR_CREATE_CATEGORY_FAIL);
        }
    }

}
