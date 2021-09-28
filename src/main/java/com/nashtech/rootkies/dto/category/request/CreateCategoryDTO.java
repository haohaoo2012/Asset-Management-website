package com.nashtech.rootkies.dto.category.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.nashtech.rootkies.constants.ErrorCode;

import lombok.*;

@Data
public class CreateCategoryDTO {

    @NotBlank
    @Pattern(regexp = "(^[A-Z]{2,3}$)", message = ErrorCode.ERR_CATEGORY_IDS_NOT_CORRECT)
    private String categoryCode;

    @NotBlank
    private String categoryName;

}
