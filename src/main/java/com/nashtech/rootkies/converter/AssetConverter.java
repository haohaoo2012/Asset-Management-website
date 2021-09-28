package com.nashtech.rootkies.converter;

import com.nashtech.rootkies.constants.ErrorCode;
import com.nashtech.rootkies.constants.State;
import com.nashtech.rootkies.dto.PageDTO;
import com.nashtech.rootkies.dto.asset.request.CreateAssetRequestDTO;
import com.nashtech.rootkies.dto.asset.response.*;
import com.nashtech.rootkies.exception.AssetConvertException;
import com.nashtech.rootkies.exception.ConvertEntityDTOException;
import com.nashtech.rootkies.exception.InvalidRequestDataException;
import com.nashtech.rootkies.model.Asset;
import com.nashtech.rootkies.model.Category;
import com.nashtech.rootkies.model.Location;
import com.nashtech.rootkies.repository.CategoryRepository;
import com.nashtech.rootkies.repository.LocationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AssetConverter {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    LocationRepository locationRepository;

    public DetailAssetDTO convertToDetailDTO(Asset asset) throws ConvertEntityDTOException {
        try {
            DetailAssetDTO detailAssetDTO = modelMapper.map(asset, DetailAssetDTO.class);

            Collection<AssignmentDTO> filteredAssignmentDTOCollection = detailAssetDTO.getAssignments().stream()
                    .filter(e -> !e.getIsDeleted()).collect(Collectors.toList());
            detailAssetDTO.setAssignments(filteredAssignmentDTOCollection);

            detailAssetDTO.getAssignments().forEach(e -> {
                Collection<RequestDTO> filteredRequestDTOCollection = e.getRequests().stream()
                        .filter(e1 -> !e1.getIsDeleted()).collect(Collectors.toList());
                e.setRequests(filteredRequestDTOCollection);
            });

            return detailAssetDTO;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ConvertEntityDTOException(ErrorCode.ERR_CONVERT_DTO_ENTITY_FAIL);
        }
    }

    public List<ViewAssetDTO> convertToListDTO(Page<Asset> assets) throws ConvertEntityDTOException {
        try {
            return assets.stream().map(asset -> modelMapper.map(asset, ViewAssetDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConvertEntityDTOException(ErrorCode.ERR_CONVERT_DTO_ENTITY_FAIL);
        }
    }

    public List<ViewAssetDTO> convertToListDTO(List<Asset> assets) throws ConvertEntityDTOException {
        try {
            return assets.stream().map(asset -> modelMapper.map(asset, ViewAssetDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConvertEntityDTOException(ErrorCode.ERR_CONVERT_DTO_ENTITY_FAIL);
        }
    }

    public Asset convertCreateAssetDTOToEntity(CreateAssetRequestDTO createAssetRequestDTO, Long locationId)
            throws ConvertEntityDTOException, InvalidRequestDataException {

        if (createAssetRequestDTO.getState() != State.AVAILABLE
                && createAssetRequestDTO.getState() != State.NOT_AVAILABLE) {
            throw new InvalidRequestDataException(ErrorCode.ERR_ASSET_STATE_NOT_CORRECT);
        }

        Optional<Category> category = categoryRepository.findById(createAssetRequestDTO.getCategoryCode());
        if (!category.isPresent()) {
            throw new InvalidRequestDataException(ErrorCode.ERR_CATEGORY_NOT_FOUND);
        }

        Optional<Location> location = locationRepository.findById(locationId);
        if (!location.isPresent()) {
            throw new InvalidRequestDataException(ErrorCode.ERR_LOCATION_NOT_FAIL);
        }

        Asset asset;
        try {
            asset = modelMapper.map(createAssetRequestDTO, Asset.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConvertEntityDTOException(ErrorCode.ERR_CONVERT_DTO_ENTITY_FAIL);
        }
        asset.setCategory(category.get());

        asset.setLocation(location.get());

        asset.setIsDeleted(false);

        return asset;

    }

    public CreateAssetResponseDTO convertToCreateResponseDTO(Asset asset) throws ConvertEntityDTOException {
        try {
            return modelMapper.map(asset, CreateAssetResponseDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConvertEntityDTOException(ErrorCode.ERR_CONVERT_DTO_ENTITY_FAIL);
        }
    }

    public EditAssetDTO toDTO(Asset asset) {
        EditAssetDTO dto = new EditAssetDTO();
        dto.setAssetCode(asset.getAssetCode());
        dto.setAssetName(asset.getAssetName());
        dto.setState(asset.getState());
        dto.setInstallDate(asset.getInstallDate());
        dto.setLocationId(asset.getLocation().getLocationId());
        dto.setSpecification(asset.getSpecification());
        dto.setIsDeleted(asset.getIsDeleted());
        dto.setCategoryId(asset.getCategory().getCategoryCode());
        return dto;
    }

    public AssetInAssignmentDTO entityToAssetInAssignmentDTO(Asset asset){

        return AssetInAssignmentDTO.builder()
                .code(asset.getAssetCode())
                .name(asset.getAssetName())
                .category(asset.getCategory().getCategoryName())
                .build();

    }

    public PageDTO pageAssetToPageDTO(Page<Asset> page){

        List<AssetInAssignmentDTO> users = page.stream()
                .map(asset -> entityToAssetInAssignmentDTO(asset))
                .collect(Collectors.toList());

        return PageDTO.builder()
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .data(users)
                .build();
    }

    public ReportDTO convertToReportDTO(List<Object[]> data) throws AssetConvertException {
        try{
            if(data.size() != 0){
            Object[] report = data.get(0);

            ReportDTO reportDTO = ReportDTO.builder()
                    .category((String) report[0])
                    .available(((BigInteger) report[1]).intValue())
                    .notAvailable(((BigInteger) report[2]).intValue())
                    .assigned(((BigInteger) report[3]).intValue())
                    .waitingForRecycle(((BigInteger) report[4]).intValue())
                    .recycled(((BigInteger) report[5]).intValue())
                    .build();
            reportDTO.countTotal();

            return reportDTO;
            }else{
                return null;
            }
        }catch (Exception ex){
            throw new AssetConvertException(ErrorCode.ERR_CONVERT_REPORT);
        }

    }
}