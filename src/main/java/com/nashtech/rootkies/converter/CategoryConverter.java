package com.nashtech.rootkies.converter;

import com.nashtech.rootkies.constants.ErrorCode;
import com.nashtech.rootkies.dto.category.request.CreateCategoryDTO;
import com.nashtech.rootkies.dto.category.response.BasicCategoryDTO;
import com.nashtech.rootkies.exception.ConvertEntityDTOException;
import com.nashtech.rootkies.exception.DuplicateDataException;
import com.nashtech.rootkies.model.Category;
import com.nashtech.rootkies.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CategoryConverter {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CategoryRepository categoryRepository;

    public BasicCategoryDTO convertEntityToBasicDTO(Category cate) throws ConvertEntityDTOException {
        try {
            BasicCategoryDTO dto = modelMapper.map(cate, BasicCategoryDTO.class);
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConvertEntityDTOException(ErrorCode.ERR_CONVERT_DTO_ENTITY_FAIL);
        }

    }

    public List<BasicCategoryDTO> convertToListBasicDTO(List<Category> cates) throws ConvertEntityDTOException {
        try {
            return cates.stream().map(c -> {
                try {
                    return convertEntityToBasicDTO(c);
                } catch (ConvertEntityDTOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return new BasicCategoryDTO();
            }).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConvertEntityDTOException(ErrorCode.ERR_CONVERT_DTO_ENTITY_FAIL);
        }

    }

    public Category convertCreateCategoryDTOToEntity(CreateCategoryDTO createCategoryDTO)
            throws ConvertEntityDTOException, DuplicateDataException {
        Optional<Category> categoryByCategoryName = categoryRepository
                .findByCategoryName(createCategoryDTO.getCategoryName());
        if (categoryByCategoryName.isPresent()) {
            throw new DuplicateDataException(ErrorCode.ERR_CATEGORY_NAME_EXISTED);
        }

        Optional<Category> categoryByCategoryCode = categoryRepository.findById(createCategoryDTO.getCategoryCode());
        if (categoryByCategoryCode.isPresent()) {
            throw new DuplicateDataException(ErrorCode.ERR_CATEGORY_CODE_EXISTED);
        }
        try {
            return modelMapper.map(createCategoryDTO, Category.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConvertEntityDTOException(ErrorCode.ERR_CONVERT_DTO_ENTITY_FAIL);
        }

    }

}
