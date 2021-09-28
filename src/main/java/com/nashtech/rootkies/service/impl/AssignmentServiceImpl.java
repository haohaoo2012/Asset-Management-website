package com.nashtech.rootkies.service.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.nashtech.rootkies.constants.ErrorCode;
import com.nashtech.rootkies.constants.State;
import com.nashtech.rootkies.constants.SuccessCode;
import com.nashtech.rootkies.converter.AssignmentConverter;
import com.nashtech.rootkies.converter.ownassignment.OwnAssignmentResponseConverter;
import com.nashtech.rootkies.converter.ownassignment.OwnAssignmentDetailConverter;
import com.nashtech.rootkies.dto.assignment.request.CreateAssignmentDTO;
import com.nashtech.rootkies.dto.assignment.request.SearchFilterSortAssignmentDTO;
import com.nashtech.rootkies.dto.assignment.response.NumberOfAssignmentDTO;
import com.nashtech.rootkies.dto.assignment.response.ViewAssignmentDTO;
import com.nashtech.rootkies.dto.common.ResponseDTO;
import com.nashtech.rootkies.dto.ownassignment.request.OwnAssignmentRequest;
import com.nashtech.rootkies.dto.ownassignment.response.OwnAssignmentDetail;
import com.nashtech.rootkies.dto.ownassignment.response.OwnAssignmentResponse;
import com.nashtech.rootkies.enums.SortType;
import com.nashtech.rootkies.exception.DataNotFoundException;
import com.nashtech.rootkies.exception.DeleteDataFailException;
import com.nashtech.rootkies.exception.InvalidRequestDataException;
import com.nashtech.rootkies.exception.UpdateDataFailException;
import com.nashtech.rootkies.exception.custom.ApiRequestException;
import com.nashtech.rootkies.model.Asset;
import com.nashtech.rootkies.model.Assignment;
import com.nashtech.rootkies.model.User;
import com.nashtech.rootkies.repository.AssetRepository;
import com.nashtech.rootkies.repository.AssignmentRepository;
import com.nashtech.rootkies.repository.UserRepository;
import com.nashtech.rootkies.repository.specs.AssignmentSpecification;
import com.nashtech.rootkies.repository.specs.SearchCriteria;
import com.nashtech.rootkies.repository.specs.SearchOperation;
import com.nashtech.rootkies.service.AssignmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;

    private final AssignmentConverter assignmentConverter;

    private final AssetRepository assetRepository;

    private final UserRepository userRepository;

    private final OwnAssignmentResponseConverter responseConverter;

    private final OwnAssignmentDetailConverter detailConverter;

    @Autowired
    public AssignmentServiceImpl(AssignmentRepository assignmentRepository, AssignmentConverter assignmentConverter,
            AssetRepository assetRepository, UserRepository userRepository,
            OwnAssignmentResponseConverter responseConverter, OwnAssignmentDetailConverter detailConverter) {
        this.assignmentRepository = assignmentRepository;
        this.assignmentConverter = assignmentConverter;
        this.assetRepository = assetRepository;
        this.userRepository = userRepository;
        this.responseConverter = responseConverter;
        this.detailConverter = detailConverter;
    }

    @Override
    public ResponseDTO countAssignment(Long locationId) throws DataNotFoundException {
        try {
            ResponseDTO responseDto = new ResponseDTO();
            NumberOfAssignmentDTO numberOfAssignmentDTO = new NumberOfAssignmentDTO();
            try {

                numberOfAssignmentDTO
                        .setNumberOfEntity(assignmentRepository.CountAllByLocationAndDefaultState(locationId));
            } catch (Exception e) {
                throw new DataNotFoundException(ErrorCode.ERR_COUNT_ASSIGNMENT_FAIL);
            }

            responseDto.setData(numberOfAssignmentDTO);
            responseDto.setSuccessCode(SuccessCode.ASSIGNMENT_COUNT_SUCCESS);
            return responseDto;
        } catch (Exception e) {
            throw new DataNotFoundException(ErrorCode.ERR_COUNT_ASSIGNMENT_FAIL);
        }
    }

    @Override
    public ResponseDTO retrieveAssignments(Pageable page, Long locationId) throws DataNotFoundException {
        try {
            ResponseDTO responseDto = new ResponseDTO();
            Page<Assignment> assignments;
            try {
                assignments = assignmentRepository.getAllByLocationAndDefaultState(page, locationId);
            } catch (Exception e) {
                throw new DataNotFoundException(ErrorCode.ERR_RETRIEVE_ASSIGNMENT_FAIL);
            }
            List<ViewAssignmentDTO> viewAssignmentDTO = assignmentConverter.convertToListDTO(assignments);

            responseDto.setData(viewAssignmentDTO);
            responseDto.setSuccessCode(SuccessCode.ASSIGNMENT_LOADED_SUCCESS);
            return responseDto;
        } catch (Exception e) {
            throw new DataNotFoundException(ErrorCode.ERR_RETRIEVE_ASSIGNMENT_FAIL);
        }
    }

    @Override
    public ResponseDTO retrieveAssignmentByAssignmentId(Long locationId, Long assignmentId)
            throws DataNotFoundException {
        try {
            ResponseDTO responseDto = new ResponseDTO();
            Optional<Assignment> assignment;

            assignment = assignmentRepository.findByAssignmentId(locationId, assignmentId);
            if (!assignment.isPresent()) {
                throw new DataNotFoundException(ErrorCode.ERR_ASSIGNMENT_ID_NOT_FOUND);
            }

            ViewAssignmentDTO viewAssignmentDTO = assignmentConverter.convertToViewDTO(assignment.get());

            responseDto.setData(viewAssignmentDTO);
            responseDto.setSuccessCode(SuccessCode.ASSIGNMENT_LOADED_SUCCESS);
            return responseDto;
        } catch (Exception e) {
            throw new DataNotFoundException(ErrorCode.ERR_ASSIGNMENT_ID_NOT_FOUND);
        }
    }

    private Pageable getPaging(Integer pageNum, Integer numOfItems,
            SearchFilterSortAssignmentDTO searchFilterSortAssignmentDTO) {
        if (!(searchFilterSortAssignmentDTO.getSortField().isBlank()
                || searchFilterSortAssignmentDTO.getSortType().isBlank())) {

            if (searchFilterSortAssignmentDTO.getSortField().equalsIgnoreCase("no")) {
                if (searchFilterSortAssignmentDTO.getSortType().equalsIgnoreCase(SortType.DSC.toString())) {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("assignmentId").descending());
                } else {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("assignmentId").ascending());
                }
            } else if (searchFilterSortAssignmentDTO.getSortField().equalsIgnoreCase("assetCode")) {
                if (searchFilterSortAssignmentDTO.getSortType().equalsIgnoreCase(SortType.DSC.toString())) {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("asset.assetCode").descending());
                } else {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("asset.assetCode").ascending());
                }
            } else if (searchFilterSortAssignmentDTO.getSortField().equalsIgnoreCase("assetName")) {
                if (searchFilterSortAssignmentDTO.getSortType().equalsIgnoreCase(SortType.DSC.toString())) {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("asset.assetName").descending());
                } else {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("asset.assetName").ascending());
                }
            } else if (searchFilterSortAssignmentDTO.getSortField().equalsIgnoreCase("assignedTo")) {
                if (searchFilterSortAssignmentDTO.getSortType().equalsIgnoreCase(SortType.DSC.toString())) {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("assignedTo.username").descending());
                } else {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("assignedTo.username").ascending());
                }
            } else if (searchFilterSortAssignmentDTO.getSortField().equalsIgnoreCase("assignedBy")) {
                if (searchFilterSortAssignmentDTO.getSortType().equalsIgnoreCase(SortType.DSC.toString())) {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("assignedBy.username").descending());
                } else {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("assignedBy.username").ascending());
                }
            } else if (searchFilterSortAssignmentDTO.getSortField().equalsIgnoreCase("category")) {
                if (searchFilterSortAssignmentDTO.getSortType().equalsIgnoreCase(SortType.DSC.toString())) {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("asset.category.categoryName").descending());
                } else {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("asset.category.categoryName").ascending());
                }
            } else if (searchFilterSortAssignmentDTO.getSortField().equalsIgnoreCase("assignedDate")) {
                if (searchFilterSortAssignmentDTO.getSortType().equalsIgnoreCase(SortType.DSC.toString())) {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("assignedDate").descending());
                } else {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("assignedDate").ascending());
                }
            } else if (searchFilterSortAssignmentDTO.getSortField().equalsIgnoreCase("state")) {
                if (searchFilterSortAssignmentDTO.getSortType().equalsIgnoreCase(SortType.DSC.toString())) {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("state").descending());
                } else {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("state").ascending());
                }
            } else {
                if (searchFilterSortAssignmentDTO.getSortType().equalsIgnoreCase(SortType.DSC.toString())) {
                    return PageRequest.of(pageNum, numOfItems,
                            Sort.by(searchFilterSortAssignmentDTO.getSortField()).descending());
                } else {
                    return PageRequest.of(pageNum, numOfItems,
                            Sort.by(searchFilterSortAssignmentDTO.getSortField()).ascending());
                }
            }
        }
        return PageRequest.of(pageNum, numOfItems, Sort.by("assignedDate").descending());
    }

    @Override
    public ResponseDTO retrieveAssignmentHavingFilterSearchSort(Integer pageNum, Integer numOfItems,
            SearchFilterSortAssignmentDTO searchFilterSortAssignmentDTO, Long locationId) throws DataNotFoundException {
        try {
            ResponseDTO responseDto = new ResponseDTO();
            Pageable page = this.getPaging(pageNum, numOfItems, searchFilterSortAssignmentDTO);

            // search assignment (by asset code or asset name or assignee’s username) and
            // filter assignment (by state, assigned date)

            AssignmentSpecification assetCode = null;
            AssignmentSpecification assetName = null;
            AssignmentSpecification assignedTo = null;
            AssignmentSpecification state = null;
            AssignmentSpecification date = null;
            AssignmentSpecification assignmentLocation = new AssignmentSpecification();
            assignmentLocation.add(new SearchCriteria("assignedBy", locationId, SearchOperation.EQUAL));
            AssignmentSpecification assignmentIsDeleted = new AssignmentSpecification();
            assignmentIsDeleted.add(new SearchCriteria("isDeleted", false, SearchOperation.EQUAL));
            AssignmentSpecification assignmentHadCompletedAssetHadReturned = new AssignmentSpecification();
            assignmentHadCompletedAssetHadReturned.add(new SearchCriteria("state",
                    State.ASSIGNMENT_HAD_COMPLETED_ASSET_HAD_RETURNED, SearchOperation.NOT_EQUAL));

            if (!searchFilterSortAssignmentDTO.getStates().isEmpty()) {
                state = new AssignmentSpecification();
                state.add(new SearchCriteria("state", searchFilterSortAssignmentDTO.getStates(), SearchOperation.IN));
            }
            if (searchFilterSortAssignmentDTO.getLocalDateTime() != null) {
                date = new AssignmentSpecification();
                date.add(new SearchCriteria("assignedDate", searchFilterSortAssignmentDTO.getLocalDateTime(),
                        SearchOperation.EQUAL));
            }
            if (!searchFilterSortAssignmentDTO.getSearchKeyWord().isBlank()) {
                assetCode = new AssignmentSpecification();
                assetCode.add(new SearchCriteria("assetCode", searchFilterSortAssignmentDTO.getSearchKeyWord(),
                        SearchOperation.MATCH));

                assetName = new AssignmentSpecification();
                assetName.add(new SearchCriteria("assetName", searchFilterSortAssignmentDTO.getSearchKeyWord(),
                        SearchOperation.MATCH));

                assignedTo = new AssignmentSpecification();
                assignedTo.add(new SearchCriteria("assignedTo", searchFilterSortAssignmentDTO.getSearchKeyWord(),
                        SearchOperation.MATCH));
            }
            
            Specification spec = Specification.where(assignmentLocation).and(assignmentIsDeleted).and(assignmentHadCompletedAssetHadReturned);

            if (state != null) {
                spec = spec.and(state);
            }
            if (date != null) {
                spec = spec.and(date);
            }
            if (assetCode != null) {
                spec = spec.and(Specification.where(assetCode).or(assetName).or(assignedTo));
            }

            Page<Assignment> assignments;

            try {
                assignments = assignmentRepository.findAll(spec, page);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DataNotFoundException(ErrorCode.ERR_RETRIEVE_ASSIGNMENT_FAIL);
            }
            List<ViewAssignmentDTO> viewAssignmentDTO = assignmentConverter.convertToListDTO(assignments);

            responseDto.setData(viewAssignmentDTO);
            responseDto.setSuccessCode(SuccessCode.ASSIGNMENT_LOADED_SUCCESS);
            return responseDto;

        } catch (Exception e) {
            e.printStackTrace();
            throw new DataNotFoundException(ErrorCode.ERR_RETRIEVE_ASSIGNMENT_FAIL);
        }
    }

    @Override
    public ResponseDTO countAssignmentHavingFilterSearchSort(
            SearchFilterSortAssignmentDTO searchFilterSortAssignmentDTO, Long locationId) throws DataNotFoundException {
        try {
            ResponseDTO responseDto = new ResponseDTO();
            // search assignment (by asset code or asset name or assignee’s username) and
            // filter assignment (by state, assigned date)

            AssignmentSpecification assetCode = null;
            AssignmentSpecification assetName = null;
            AssignmentSpecification assignedTo = null;
            AssignmentSpecification state = null;
            AssignmentSpecification date = null;
            AssignmentSpecification assignmentLocation = new AssignmentSpecification();
            assignmentLocation.add(new SearchCriteria("assignedBy", locationId, SearchOperation.EQUAL));
            AssignmentSpecification assignmentIsDeleted = new AssignmentSpecification();
            assignmentIsDeleted.add(new SearchCriteria("isDeleted", false, SearchOperation.EQUAL));
            AssignmentSpecification assignmentHadCompletedAssetHadReturned = new AssignmentSpecification();
            assignmentHadCompletedAssetHadReturned.add(new SearchCriteria("state",
                    State.ASSIGNMENT_HAD_COMPLETED_ASSET_HAD_RETURNED, SearchOperation.NOT_EQUAL));

            if (!searchFilterSortAssignmentDTO.getStates().isEmpty()) {
                state = new AssignmentSpecification();
                state.add(new SearchCriteria("state", searchFilterSortAssignmentDTO.getStates(), SearchOperation.IN));
            }
            if (searchFilterSortAssignmentDTO.getLocalDateTime() != null) {
                date = new AssignmentSpecification();
                date.add(new SearchCriteria("assignedDate", searchFilterSortAssignmentDTO.getLocalDateTime(),
                        SearchOperation.EQUAL));
            }
            if (!searchFilterSortAssignmentDTO.getSearchKeyWord().isBlank()) {
                assetCode = new AssignmentSpecification();
                assetCode.add(new SearchCriteria("assetCode", searchFilterSortAssignmentDTO.getSearchKeyWord(),
                        SearchOperation.MATCH));

                assetName = new AssignmentSpecification();
                assetName.add(new SearchCriteria("assetName", searchFilterSortAssignmentDTO.getSearchKeyWord(),
                        SearchOperation.MATCH));

                assignedTo = new AssignmentSpecification();
                assignedTo.add(new SearchCriteria("assignedTo", searchFilterSortAssignmentDTO.getSearchKeyWord(),
                        SearchOperation.MATCH));
            }

            Specification spec = Specification.where(assignmentLocation).and(assignmentIsDeleted).and(assignmentHadCompletedAssetHadReturned);

            if (state != null) {
                spec = spec.and(state);
            }
            if (date != null) {
                spec = spec.and(date);
            }
            if (assetCode != null) {
                spec = spec.and(Specification.where(assetCode).or(assetName).or(assignedTo));
            }

            List<Assignment> assignments;

            try {
                assignments = assignmentRepository.findAll(spec);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DataNotFoundException(ErrorCode.ERR_COUNT_ASSIGNMENT_FAIL);
            }
            List<ViewAssignmentDTO> viewAssignmentDTO = assignmentConverter.convertToListDTO(assignments);
            NumberOfAssignmentDTO numberOfAssignmentDTO = new NumberOfAssignmentDTO();

            numberOfAssignmentDTO.setNumberOfEntity((long) viewAssignmentDTO.size());

            responseDto.setData(numberOfAssignmentDTO);
            responseDto.setSuccessCode(SuccessCode.ASSIGNMENT_COUNT_SUCCESS);
            return responseDto;

        } catch (Exception e) {
            e.printStackTrace();
            throw new DataNotFoundException(ErrorCode.ERR_COUNT_ASSIGNMENT_FAIL);
        }
    }

    @Override
    public Assignment createAssignment(CreateAssignmentDTO createAssignmentDTO) throws DataNotFoundException {
        Optional<Asset> asset = assetRepository.findById(createAssignmentDTO.getAssetCode());
        if (!asset.isPresent()) {
            throw new DataNotFoundException(ErrorCode.ASSET_NOT_FOUND);
        } else if (asset.get().getState() != 1) {
            throw new DataNotFoundException(ErrorCode.ASSET_IS_NOT_AVAILABLE);
        }

        Assignment assignment = assignmentConverter.createDTOToEntity(createAssignmentDTO);
        // change state of asset
        assetRepository.updateStateWhenIsAssigned(createAssignmentDTO.getAssetCode());

        return assignmentRepository.save(assignment);
    }

    public ResponseDTO deleteAssetByAssignmentId(Long locationId, Long assignmentId)
            throws DataNotFoundException, DeleteDataFailException {

        ResponseDTO responseDto = new ResponseDTO();
        Optional<Assignment> assignment;

        assignment = assignmentRepository.findByAssignmentId(locationId, assignmentId);
        if (!assignment.isPresent()) {
            throw new DataNotFoundException(ErrorCode.ERR_ASSIGNMENT_ID_NOT_FOUND);
        }
        Assignment assignmentSave = assignment.get();
        if (assignmentSave.getState() != State.WAITING_FOR_ACCEPTANCE && assignmentSave.getState() != State.DECLINED) {
            throw new DeleteDataFailException(ErrorCode.ERR_ASSIGNMENT_DELETE_FAIL_DUE_TO_STATE);
        }
        try {
            assignmentSave.setIsDeleted(true);
            assignmentSave.getAsset().setState(State.AVAILABLE);
            assignmentRepository.save(assignmentSave);

            responseDto.setSuccessCode(SuccessCode.ASSIGNMENT_DELETE_SUCCESS);
            return responseDto;
        } catch (Exception e) {
            throw new DeleteDataFailException(ErrorCode.ERR_ASSIGNMENT_DELETE_FAIL);
        }
    }

    @Override
    public ResponseDTO editAssignment(Assignment assignment, String assetCode)
            throws UpdateDataFailException, DataNotFoundException {
        ResponseDTO responseDto = new ResponseDTO();

        Asset assetUp = assetRepository.findById(assetCode)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.ASSET_NOT_FOUND));
        try {
            if (!assignment.getAsset().getAssetCode().equalsIgnoreCase(assetCode)) {
                Asset assetCur = assignment.getAsset();
                assetCur.setState(State.AVAILABLE);
                assetRepository.save(assetCur);

                assetUp.setState(State.ASSIGNED);
                // assetRepository.save(assetUp);
                assignment.setAsset(assetUp);
            }

            ViewAssignmentDTO viewAssignmentDTO = assignmentConverter
                    .convertToViewDTO(assignmentRepository.save(assignment));

            responseDto.setData(viewAssignmentDTO);
            responseDto.setSuccessCode(SuccessCode.ASSIGNMENT_UPDATE_SUCCESS);
            return responseDto;
        } catch (Exception e) {
            throw new UpdateDataFailException(ErrorCode.ERR_ASSIGNMENT_UPDATE_FAIL);
        }
    }

    @Override
    public ResponseDTO acceptAssignment(Long locationId, Long assignmentId, String username)
            throws DataNotFoundException, InvalidRequestDataException, UpdateDataFailException {
        Assignment assignment = assignmentRepository.findByAssignmentId(locationId, assignmentId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.ERR_ASSIGNMENT_ID_NOT_FOUND));

        if (assignment.getState() != State.WAITING_FOR_ACCEPTANCE) {
            throw new InvalidRequestDataException(ErrorCode.ERR_ASSIGNMENT_ALREADY_ACCEPTED_OR_DECLINED);
        }

        if (!assignment.getAssignedTo().getUsername().equalsIgnoreCase(username)) {
            throw new InvalidRequestDataException(ErrorCode.ERR_ASSIGNMENT_NOT_YOUR);
        }

        try {
            ResponseDTO responseDto = new ResponseDTO();

            assignment.setState(State.ACCEPTED);
            assignmentRepository.save(assignment);

            responseDto.setSuccessCode(SuccessCode.ASSIGNMENT_ACCEPTED_SUCCESS);
            return responseDto;
        } catch (Exception e) {
            throw new UpdateDataFailException(ErrorCode.ERR_ASSIGNMENT_ACCEPTED_FAIL);
        }
    }

    @Override
    public ResponseDTO declineAssignment(Long locationId, Long assignmentId, String username)
            throws DataNotFoundException, InvalidRequestDataException, UpdateDataFailException {
        Assignment assignment = assignmentRepository.findByAssignmentId(locationId, assignmentId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.ERR_ASSIGNMENT_ID_NOT_FOUND));

        if (assignment.getState() != State.WAITING_FOR_ACCEPTANCE) {
            throw new InvalidRequestDataException(ErrorCode.ERR_ASSIGNMENT_ALREADY_ACCEPTED_OR_DECLINED);
        }

        if (!assignment.getAssignedTo().getUsername().equalsIgnoreCase(username)) {
            throw new InvalidRequestDataException(ErrorCode.ERR_ASSIGNMENT_NOT_YOUR);
        }

        try {
            ResponseDTO responseDto = new ResponseDTO();

            assignment.setState(State.DECLINED);
            Asset asset = assignment.getAsset();
            asset.setState(State.AVAILABLE);
            assignmentRepository.save(assignment);
            assetRepository.save(asset);

            responseDto.setSuccessCode(SuccessCode.ASSIGNMENT_DECLINED_SUCCESS);
            return responseDto;
        } catch (Exception e) {
            throw new UpdateDataFailException(ErrorCode.ERR_ASSIGNMENT_DECLINED_FAIL);
        }
    }

    public LocalDateTime getTimeForComparing() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(date + " 23:59:59", formatter);
        return dateTime;
    }

    @Override
    public OwnAssignmentResponse viewOwnAssignment(OwnAssignmentRequest request) {
        int pageNum = request.getPageNum();
        int pageSize = request.getPageSize();
        String staffCode = request.getStaffCode();
        String orderBy = request.getOrderBy();
        String typeOrder = request.getTypeOrder();

        User assignedTo = userRepository.findById(staffCode)
                .orElseThrow(() -> new ApiRequestException(ErrorCode.USER_NOT_FOUND));
        if (assignedTo.getIsDeleted()) {
            throw new ApiRequestException(ErrorCode.USER_IS_DISABLED);
        }

        try {
            Pageable pageable;
            if (typeOrder.equalsIgnoreCase("ASC")) {
                switch (orderBy) {
                    case "assetCode":
                        pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("asset.assetCode").ascending());
                        break;
                    case "assetName":
                        pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("asset.assetName").ascending());
                        break;
                    case "category":
                        pageable = PageRequest.of(pageNum - 1, pageSize,
                                Sort.by("asset.category.categoryName").ascending());
                        break;
                    case "state":
                        pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("state").ascending());
                        break;
                    default:
                        pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("assignedDate").ascending());
                }
            } else {
                switch (orderBy) {
                    case "assetCode":
                        pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("asset.assetCode").descending());
                        break;
                    case "assetName":
                        pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("asset.assetName").descending());
                        break;
                    case "category":
                        pageable = PageRequest.of(pageNum - 1, pageSize,
                                Sort.by("asset.category.categoryName").descending());
                        break;
                    case "state":
                        pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("state").descending());
                        break;
                    default:
                        pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("assignedDate").descending());
                }
            }

            LocalDateTime current = getTimeForComparing();

            Page<Assignment> page = assignmentRepository.findByAssignedToAndStateNotAndStateNotAndAssignedDateLessThanAndIsDeleted(
                    assignedTo, State.DECLINED, State.ASSIGNMENT_HAD_COMPLETED_ASSET_HAD_RETURNED, current, false, pageable);

            return responseConverter.convert(pageNum, page);
        } catch (Exception e) {
            throw new ApiRequestException(ErrorCode.ERR_LOAD_OWN_ASSIGNMENT);
        }
    }

    @Override
    public OwnAssignmentDetail getOwnAssignmentDetail(Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ApiRequestException(ErrorCode.ASSIGNMENT_NOT_FOUND));
        if (assignment.getIsDeleted()) {
            throw new ApiRequestException(ErrorCode.ASSIGNMENT_IS_DELETED);
        }
        if (assignment.getState() == State.DECLINED) {
            throw new ApiRequestException(ErrorCode.ASSIGNMENT_IS_DECLINED);
        }
        if(assignment.getState() == State.ASSIGNMENT_HAD_COMPLETED_ASSET_HAD_RETURNED){
            throw new ApiRequestException(ErrorCode.ASSIGNMENT_ALREADY_COMPLETE_RETURN);
        }

        try {
            OwnAssignmentDetail detail = detailConverter.convert(assignment);
            return detail;
        } catch (Exception e) {
            throw new ApiRequestException(ErrorCode.ERR_OWN_ASSIGNMENT_DETAIL);
        }
    }

}
