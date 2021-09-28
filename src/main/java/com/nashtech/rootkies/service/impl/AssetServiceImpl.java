
package com.nashtech.rootkies.service.impl;

import com.nashtech.rootkies.constants.ErrorCode;
import com.nashtech.rootkies.constants.State;
import com.nashtech.rootkies.constants.SuccessCode;
import com.nashtech.rootkies.converter.AssetConverter;
import com.nashtech.rootkies.dto.PageDTO;
import com.nashtech.rootkies.dto.asset.request.EditAssetRequest;
import com.nashtech.rootkies.dto.asset.request.SearchFilterSortAssetDTO;
import com.nashtech.rootkies.dto.asset.response.*;
import com.nashtech.rootkies.dto.common.ResponseDTO;
import com.nashtech.rootkies.enums.SortType;
import com.nashtech.rootkies.exception.AssetConvertException;
import com.nashtech.rootkies.exception.CreateDataFailException;
import com.nashtech.rootkies.exception.DataNotFoundException;
import com.nashtech.rootkies.exception.DeleteDataFailException;
import com.nashtech.rootkies.exception.custom.ApiRequestException;
import com.nashtech.rootkies.model.Asset;
import com.nashtech.rootkies.model.Assignment;
import com.nashtech.rootkies.repository.AssetRepository;
import com.nashtech.rootkies.repository.AssignmentRepository;
import com.nashtech.rootkies.repository.CategoryRepository;
import com.nashtech.rootkies.repository.specs.AssetSpecification;
import com.nashtech.rootkies.repository.specs.SearchCriteria;
import com.nashtech.rootkies.repository.specs.SearchOperation;
import com.nashtech.rootkies.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;

    private final AssetConverter assetConverter;

    private final AssignmentRepository assignmentRepository;

    private final CategoryRepository categoryRepository;

    @Autowired
    public AssetServiceImpl(AssetRepository assetRepository, AssetConverter assetConverter,
            AssignmentRepository assignmentRepository, CategoryRepository categoryRepository) {
        this.assetRepository = assetRepository;
        this.assetConverter = assetConverter;
        this.assignmentRepository = assignmentRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ResponseDTO countAsset(Long locationId) throws DataNotFoundException {
        try {
            ResponseDTO responseDto = new ResponseDTO();
            NumberOfAssetDTO numberOfAssetDto = new NumberOfAssetDTO();
            try {
                numberOfAssetDto.setNumberOfEntity(assetRepository.CountAllByLocationAndDefaultState(locationId));
            } catch (Exception e) {
                throw new DataNotFoundException(ErrorCode.ERR_COUNT_ASSET_FAIL);
            }

            responseDto.setData(numberOfAssetDto);
            responseDto.setSuccessCode(SuccessCode.ASSET_COUNT_SUCCESS);
            return responseDto;
        } catch (Exception e) {
            throw new DataNotFoundException(ErrorCode.ERR_COUNT_ASSET_FAIL);
        }
    }

    @Override
    public ResponseDTO retrieveAsset(Pageable page, Long locationId) throws DataNotFoundException {
        try {
            ResponseDTO responseDto = new ResponseDTO();
            Page<Asset> assets;
            try {
                assets = assetRepository.getAllByLocationAndDefaultState(page, locationId);
            } catch (Exception e) {
                throw new DataNotFoundException(ErrorCode.ERR_RETRIEVE_ASSET_FAIL);
            }
            List<ViewAssetDTO> viewAssetsDTO = assetConverter.convertToListDTO(assets);

            responseDto.setData(viewAssetsDTO);
            responseDto.setSuccessCode(SuccessCode.ASSET_LOADED_SUCCESS);
            return responseDto;
        } catch (Exception e) {
            throw new DataNotFoundException(ErrorCode.ERR_RETRIEVE_ASSET_FAIL);
        }
    }

    @Override
    public ResponseDTO retrieveAssetByAssetCode(Long locationId, String assetCode) throws DataNotFoundException {
        try {
            ResponseDTO responseDto = new ResponseDTO();
            Optional<Asset> asset;

            asset = assetRepository.findByAssetCode(locationId, assetCode);
            if (!asset.isPresent()) {
                throw new DataNotFoundException(ErrorCode.ERR_ASSETCODE_NOT_FOUND);
            }

            DetailAssetDTO detailAssetDTO = assetConverter.convertToDetailDTO(asset.get());

            responseDto.setData(detailAssetDTO);
            responseDto.setSuccessCode(SuccessCode.ASSET_LOADED_SUCCESS);
            return responseDto;
        } catch (Exception e) {
            throw new DataNotFoundException(ErrorCode.ERR_ASSETCODE_NOT_FOUND);
        }
    }

    @Override
    public ResponseDTO retrieveAssetHavingFilterSearchSort(Integer pageNum, Integer numOfItems,
            SearchFilterSortAssetDTO searchFilterSortAssetDTO, Long locationId) throws DataNotFoundException {
        try {
            ResponseDTO responseDto = new ResponseDTO();
            Pageable page;
            if (!(searchFilterSortAssetDTO.getSortField().isBlank()
                    || searchFilterSortAssetDTO.getSortType().isBlank())) {
                if (searchFilterSortAssetDTO.getSortField().equalsIgnoreCase("category")) {
                    if (searchFilterSortAssetDTO.getSortType().equalsIgnoreCase(SortType.DSC.toString())) {
                        page = PageRequest.of(pageNum, numOfItems, Sort.by("category.categoryName").descending());
                    } else {
                        page = PageRequest.of(pageNum, numOfItems, Sort.by("category.categoryName").ascending());
                    }
                } else {
                    if (searchFilterSortAssetDTO.getSortType().equalsIgnoreCase(SortType.DSC.toString())) {
                        page = PageRequest.of(pageNum, numOfItems,
                                Sort.by(searchFilterSortAssetDTO.getSortField()).descending());
                    } else {
                        page = PageRequest.of(pageNum, numOfItems,
                                Sort.by(searchFilterSortAssetDTO.getSortField()).ascending());
                    }
                }

            } else {
                page = PageRequest.of(pageNum, numOfItems, Sort.by("assetName").ascending());
            }

            AssetSpecification assetState = null;
            AssetSpecification assetCategoryCode = null;
            AssetSpecification assetCode = null;
            AssetSpecification assetName = null;
            AssetSpecification assetLocation = new AssetSpecification();
            assetLocation.add(new SearchCriteria("location", locationId, SearchOperation.EQUAL));
            AssetSpecification assetIsDeleted = new AssetSpecification();
            assetIsDeleted.add(new SearchCriteria("isDeleted", false, SearchOperation.EQUAL));

            if (!searchFilterSortAssetDTO.getStates().isEmpty()) {
                assetState = new AssetSpecification();
                assetState.add(new SearchCriteria("state", searchFilterSortAssetDTO.getStates(), SearchOperation.IN));
            }
            if (!searchFilterSortAssetDTO.getCategoriesCode().isEmpty()) {
                assetCategoryCode = new AssetSpecification();
                assetCategoryCode.add(new SearchCriteria("category", searchFilterSortAssetDTO.getCategoriesCode(),
                        SearchOperation.IN));
            }
            if (!searchFilterSortAssetDTO.getSearchKeyWord().isBlank()) {
                assetCode = new AssetSpecification();
                assetCode.add(new SearchCriteria("assetCode", searchFilterSortAssetDTO.getSearchKeyWord(),
                        SearchOperation.MATCH));

                assetName = new AssetSpecification();
                assetName.add(new SearchCriteria("assetName", searchFilterSortAssetDTO.getSearchKeyWord(),
                        SearchOperation.MATCH));
            }

            Specification spec = Specification.where(assetLocation).and(assetIsDeleted);

            if (assetState != null) {
                spec = spec.and(assetState);
            }
            if (assetCategoryCode != null) {
                spec = spec.and(assetCategoryCode);
            }
            if (assetCode != null) {
                spec = spec.and(Specification.where(assetCode).or(assetName));
            }

            Page<Asset> assets;
            try {
                assets = assetRepository.findAll(spec, page);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DataNotFoundException(ErrorCode.ERR_RETRIEVE_ASSET_FAIL);
            }
            List<ViewAssetDTO> viewAssetsDTO = assetConverter.convertToListDTO(assets);

            responseDto.setData(viewAssetsDTO);
            responseDto.setSuccessCode(SuccessCode.ASSET_LOADED_SUCCESS);
            return responseDto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataNotFoundException(ErrorCode.ERR_RETRIEVE_ASSET_FAIL);
        }
    }

    @Override
    public ResponseDTO countAssetHavingFilterSearchSort(SearchFilterSortAssetDTO searchFilterSortAssetDTO,
            Long locationId) throws DataNotFoundException {
        try {
            ResponseDTO responseDto = new ResponseDTO();

            AssetSpecification assetState = null;
            AssetSpecification assetCategoryCode = null;
            AssetSpecification assetCode = null;
            AssetSpecification assetName = null;
            AssetSpecification assetLocation = new AssetSpecification();
            assetLocation.add(new SearchCriteria("location", locationId, SearchOperation.EQUAL));
            AssetSpecification assetIsDeleted = new AssetSpecification();
            assetIsDeleted.add(new SearchCriteria("isDeleted", false, SearchOperation.EQUAL));

            if (!searchFilterSortAssetDTO.getStates().isEmpty()) {
                assetState = new AssetSpecification();
                assetState.add(new SearchCriteria("state", searchFilterSortAssetDTO.getStates(), SearchOperation.IN));
            }
            if (!searchFilterSortAssetDTO.getCategoriesCode().isEmpty()) {
                assetCategoryCode = new AssetSpecification();
                assetCategoryCode.add(new SearchCriteria("category", searchFilterSortAssetDTO.getCategoriesCode(),
                        SearchOperation.IN));
            }
            if (!searchFilterSortAssetDTO.getSearchKeyWord().isBlank()) {
                assetCode = new AssetSpecification();
                assetCode.add(new SearchCriteria("assetCode", searchFilterSortAssetDTO.getSearchKeyWord(),
                        SearchOperation.MATCH));

                assetName = new AssetSpecification();
                assetName.add(new SearchCriteria("assetName", searchFilterSortAssetDTO.getSearchKeyWord(),
                        SearchOperation.MATCH));
            }

            Specification spec = Specification.where(assetLocation).and(assetIsDeleted);

            if (assetState != null) {
                spec = spec.and(assetState);
            }
            if (assetCategoryCode != null) {
                spec = spec.and(assetCategoryCode);
            }
            if (assetCode != null) {
                spec = spec.and(Specification.where(assetCode).or(assetName));
            }

            List<Asset> assets;
            try {
                assets = assetRepository.findAll(spec);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DataNotFoundException(ErrorCode.ERR_RETRIEVE_ASSET_FAIL);
            }
            List<ViewAssetDTO> viewAssetsDTO = assetConverter.convertToListDTO(assets);

            NumberOfAssetDTO numberOfAssetDto = new NumberOfAssetDTO();

            numberOfAssetDto.setNumberOfEntity((long) viewAssetsDTO.size());

            responseDto.setData(numberOfAssetDto);
            responseDto.setSuccessCode(SuccessCode.ASSET_COUNT_SUCCESS);
            return responseDto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataNotFoundException(ErrorCode.ERR_COUNT_ASSET_FAIL);
        }
    }
    // Edit Asset

    public Boolean checkFormatDate(String date) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public EditAssetDTO editAsset(String assetCode, EditAssetRequest editAssetRequest) {
        Asset asset = assetRepository.findById(assetCode)
                .orElseThrow(() -> new ApiRequestException(ErrorCode.ASSET_NOT_FOUND));

        if (asset.getIsDeleted()) {
            throw new ApiRequestException(ErrorCode.ASSET_IS_DELETED);
        }

        // List<Assignment> assignments = assignmentRepository.findByAsset(asset);
        if (asset.getState() == State.ASSIGNED) {
            throw new ApiRequestException(ErrorCode.ASSET_ALREADY_ASSIGNED);
        }

        String name = editAssetRequest.getName();
        String specification = editAssetRequest.getSpecification();
        String installDate = editAssetRequest.getInstallDate();
        String state = editAssetRequest.getState();

        if (name == null || name.trim().length() == 0) {
            throw new ApiRequestException(ErrorCode.NAME_IS_EMPTY);
        }

        if (specification == null || specification.trim().length() == 0) {
            throw new ApiRequestException(ErrorCode.SPEC_IS_EMPTY);
        }

        if (checkFormatDate(installDate) == false) {
            throw new ApiRequestException(ErrorCode.DATE_INCORRECT_FORMAT);
        }

        if (!state.trim().equalsIgnoreCase("available") && !state.trim().equalsIgnoreCase("not available")
                && !state.trim().equalsIgnoreCase("waiting for recycling")
                && !state.trim().equalsIgnoreCase("recycled")) {
            throw new ApiRequestException(ErrorCode.STATE_INCORRECT_FORMAT);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(installDate + " 00:00:00", formatter);

        asset.setAssetName(name);
        asset.setSpecification(specification);
        asset.setInstallDate(dateTime);

        if (state.trim().equalsIgnoreCase("available")) {
            asset.setState(State.AVAILABLE);
        } else if (state.trim().equalsIgnoreCase("not available")) {
            asset.setState(State.NOT_AVAILABLE);
        } else if (state.trim().equalsIgnoreCase("waiting for recycling")) {
            asset.setState(State.WAITING_FOR_RECYCLING);
        } else {
            asset.setState(State.RECYLED);
        }

        try {
            asset = assetRepository.save(asset);
            EditAssetDTO dto = assetConverter.toDTO(asset);
            return dto;
        } catch (Exception e) {
            throw new ApiRequestException(ErrorCode.ERR_EDIT_ASSET);
        }
    }

    @Override
    public ResponseDTO saveAsset(Asset asset) throws CreateDataFailException {
        try {
            ResponseDTO responseDto = new ResponseDTO();

            Asset assetSave;

            assetSave = assetRepository.save(asset);

            responseDto.setData(assetConverter.convertToCreateResponseDTO(assetSave));
            responseDto.setSuccessCode(SuccessCode.ASSET_CREATED_SUCCESS);
            return responseDto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CreateDataFailException(ErrorCode.ERR_CREATE_ASSET_FAIL);
        }
    }

    @Override
    public ResponseDTO deleteAssetByAssetCode(Long locationId, String assetCode)
            throws DataNotFoundException, DeleteDataFailException {

        ResponseDTO responseDto = new ResponseDTO();
        Optional<Asset> asset;

        asset = assetRepository.findByAssetCode(locationId, assetCode);
        if (!asset.isPresent()) {
            throw new DataNotFoundException(ErrorCode.ERR_ASSETCODE_NOT_FOUND);
        }
        Asset assetSave = asset.get();

        List<Assignment> assignemntList = assetSave.getAssignments().stream().collect(Collectors.toList());
        if (assignemntList.size() > 0) {
            for (int i = 0; i < assignemntList.size(); i++) {
                if (assignemntList.get(i).getState() == State.ACCEPTED
                        || assignemntList.get(i).getState() == State.DECLINED) {
                    throw new DeleteDataFailException(ErrorCode.ERR_ASSET_ALREADY_HAVE_ASSIGNMENT);
                }
                if (assignemntList.get(i).getState() == State.WAITING_FOR_ACCEPTANCE
                        && assignemntList.get(i).getIsDeleted() == false) {
                    throw new DeleteDataFailException(ErrorCode.ERR_ASSET_ALREADY_HAVE_ASSIGNMENT);
                }
            }
        }

        try {
            assetSave.setIsDeleted(true);
            assetRepository.save(assetSave);
            responseDto.setSuccessCode(SuccessCode.ASSET_DELETE_SUCCESS);
            return responseDto;
        } catch (Exception e) {
            throw new DeleteDataFailException(ErrorCode.ERR_ASSET_DELETE_FAIL);
        }

    }

    @Override
    public ResponseDTO checkDeleteAssetByAssetCode(Long locationId, String assetCode)
            throws DataNotFoundException, DeleteDataFailException {
        ResponseDTO responseDto = new ResponseDTO();
        Optional<Asset> asset;

        asset = assetRepository.findByAssetCode(locationId, assetCode);
        if (!asset.isPresent()) {
            throw new DataNotFoundException(ErrorCode.ERR_ASSETCODE_NOT_FOUND);
        }
        Asset assetCheck = asset.get();

        List<Assignment> assignemntList = assetCheck.getAssignments().stream().collect(Collectors.toList());
        if (assignemntList.size() > 0) {
            for (int i = 0; i < assignemntList.size(); i++) {
                if (assignemntList.get(i).getState() == State.ACCEPTED
                        || assignemntList.get(i).getState() == State.DECLINED) {
                    throw new DeleteDataFailException(ErrorCode.ERR_ASSET_ALREADY_HAVE_ASSIGNMENT);
                }
                if (assignemntList.get(i).getState() == State.WAITING_FOR_ACCEPTANCE
                        && assignemntList.get(i).getIsDeleted() == false) {
                    throw new DeleteDataFailException(ErrorCode.ERR_ASSET_ALREADY_HAVE_ASSIGNMENT);
                }
            }
        }

        responseDto.setSuccessCode(SuccessCode.ASSET_ABLE_TO_DELETE);
        return responseDto;
    }

    @Override
    public PageDTO getAllAssetAvailable(Pageable pageable, Specification specification) throws DataNotFoundException {

        try {
            return assetConverter.pageAssetToPageDTO(assetRepository.findAll(specification, pageable));

        } catch (Exception exception) {

            throw new DataNotFoundException(ErrorCode.ERR_GET_ALL_ASSET);
        }
    }

    public List<ReportDTO> getAssetReport(Long locationId) throws AssetConvertException {
        var reportList = new ArrayList<ReportDTO>();
        var categoryList = categoryRepository.findAll();

        for (var category : categoryList) {
            var report = assetConverter
                    .convertToReportDTO(assetRepository.getAssetReportByCategoryCode(
                            category.getCategoryCode(), locationId));

            // if category have no asset ,will get no report from database
            if (report != null) {
                reportList.add(report);
            }

        }

        // because if category have no asset database will return no report
        // so detect category have no asset and add it to reportList with data 0 0 0 0 0
        if (categoryList.size() != reportList.size()) {
            for (var category : categoryList) {
                boolean isHaveAsset = false;
                for (var report : reportList) {
                    if (category.getCategoryName().equals(report.getCategory())) {
                        isHaveAsset = true;
                        break;
                    }
                }
                if (isHaveAsset == false) {
                    reportList.add(new ReportDTO(category.getCategoryName(), 0, 0, 0, 0, 0, 0));
                }
            }
        }

        return reportList;
    }

}
