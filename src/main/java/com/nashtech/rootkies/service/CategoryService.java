package com.nashtech.rootkies.service;

import com.nashtech.rootkies.dto.common.ResponseDTO;
import com.nashtech.rootkies.exception.CreateDataFailException;
import com.nashtech.rootkies.exception.DataNotFoundException;
import com.nashtech.rootkies.model.Category;

public interface CategoryService {

    public ResponseDTO retrieveCategories() throws DataNotFoundException;

    public ResponseDTO saveCategory(Category category) throws CreateDataFailException;
}
