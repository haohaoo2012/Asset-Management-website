package com.nashtech.rootkies.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import javax.validation.Valid;

import com.nashtech.rootkies.converter.CategoryConverter;
import com.nashtech.rootkies.dto.category.request.CreateCategoryDTO;
import com.nashtech.rootkies.dto.common.ResponseDTO;
import com.nashtech.rootkies.exception.ConvertEntityDTOException;
import com.nashtech.rootkies.exception.CreateDataFailException;
import com.nashtech.rootkies.exception.DataNotFoundException;
import com.nashtech.rootkies.exception.DuplicateDataException;
import com.nashtech.rootkies.model.Category;
import com.nashtech.rootkies.service.CategoryService;
import com.nashtech.rootkies.repository.CategoryRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/category")
@Tag(name = "CATEGORY", description = "CATEGORY API")
public class CategoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    private final CategoryConverter categoryConverter;

    @Autowired
    public CategoryController(CategoryService categoryService, CategoryConverter categoryConverter,
            CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryConverter = categoryConverter;
    }

    @Operation(summary = "Get all category", description = "", tags = { "CATEGORY" }, security = {
            @SecurityRequirement(name = "bearer-key-admin") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> retrieveCategories() throws DataNotFoundException {
        return ResponseEntity.ok(categoryService.retrieveCategories());
    }

    @Operation(summary = "Create category", description = "", tags = { "CATEGORY" }, security = {
            @SecurityRequirement(name = "bearer-key-admin") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> saveCategory(@Valid @RequestBody CreateCategoryDTO createCategoryDTO)
            throws ConvertEntityDTOException, CreateDataFailException, DuplicateDataException {

        Category category = categoryConverter.convertCreateCategoryDTOToEntity(createCategoryDTO);
        return ResponseEntity.ok(categoryService.saveCategory(category));
    }
}
