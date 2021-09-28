package com.nashtech.rootkies.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.nashtech.rootkies.constants.ErrorCode;
import com.nashtech.rootkies.constants.State;
import com.nashtech.rootkies.constants.SuccessCode;
import com.nashtech.rootkies.converter.RequestConverter;
import com.nashtech.rootkies.dto.common.ResponseDTO;
import com.nashtech.rootkies.dto.request.request.CreateRequestDTO;
import com.nashtech.rootkies.dto.request.request.SearchFilterSortRequestDTO;
import com.nashtech.rootkies.enums.ERole;
import com.nashtech.rootkies.exception.CreateDataFailException;
import com.nashtech.rootkies.exception.DataNotFoundException;
import com.nashtech.rootkies.exception.InvalidRequestDataException;
import com.nashtech.rootkies.exception.UpdateDataFailException;
import com.nashtech.rootkies.model.Request;
import com.nashtech.rootkies.model.User;
import com.nashtech.rootkies.repository.AssignmentRepository;
import com.nashtech.rootkies.repository.RequestRepository;
import com.nashtech.rootkies.repository.UserRepository;
import com.nashtech.rootkies.repository.specs.RequestSpecification;
import com.nashtech.rootkies.service.RequestService;
import com.nashtech.rootkies.dto.request.response.NumberOfRequestDTO;
import com.nashtech.rootkies.dto.request.response.ViewRequestDTO;
import com.nashtech.rootkies.repository.specs.SearchCriteria;
import com.nashtech.rootkies.repository.specs.SearchOperation;
import com.nashtech.rootkies.enums.SortType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    private final UserRepository userRepository;

    private final AssignmentRepository assignmentRepository;

    private final RequestConverter requestConverter;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, UserRepository userRepository,
                              AssignmentRepository assignmentRepository, RequestConverter requestConverter) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.assignmentRepository = assignmentRepository;
        this.requestConverter = requestConverter;
    }

    @Override
    public ResponseDTO cancelRequest(Long locationId, Long requestId, String username)
            throws DataNotFoundException, InvalidRequestDataException, UpdateDataFailException {
        Request request = requestRepository.findByRequestId(locationId, requestId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.ERR_REQUEST_NOT_FOUND));
        if (request.getState() != State.WAITING_FOR_RETURNING) {
            throw new InvalidRequestDataException(ErrorCode.ERR_REQUEST_ALREADY_COMPLETE);
        }
        User admin = userRepository.findByUsername(locationId, username)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.ERR_USER_NOT_FOUND));

        try {
            ResponseDTO responseDto = new ResponseDTO();
            request.setIsDeleted(true);
            request.setAcceptedBy(admin);
            LocalDateTime currentTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            request.setReturnedDate(currentTime);
            requestRepository.save(request);

            responseDto.setSuccessCode(SuccessCode.REQUEST_CANCEL_SUCCESS);
            return responseDto;
        } catch (Exception e) {
            throw new UpdateDataFailException(ErrorCode.ERR_REQUEST_CANCEL_FAIL);
        }
    }

    @Override
    public ResponseDTO completeRequest(Long locationId, Long requestId, String username)
            throws DataNotFoundException, InvalidRequestDataException, UpdateDataFailException {
        Request request = requestRepository.findByRequestId(locationId, requestId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.ERR_REQUEST_NOT_FOUND));
        if (request.getState() != State.WAITING_FOR_RETURNING) {
            throw new InvalidRequestDataException(ErrorCode.ERR_REQUEST_ALREADY_COMPLETE);
        }
        if(request.getIsDeleted() == true) {
            throw new InvalidRequestDataException(ErrorCode.ERR_REQUEST_IS_DELETED);
        }
        User admin = userRepository.findByUsername(locationId, username)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.ERR_USER_NOT_FOUND));

        try {
            ResponseDTO responseDto = new ResponseDTO();
            request.setState(State.COMPLETED);
            request.setAcceptedBy(admin);
            request.getAssignment().getAsset().setState(State.AVAILABLE);
            request.getAssignment().setState(State.ASSIGNMENT_HAD_COMPLETED_ASSET_HAD_RETURNED);
            LocalDateTime currentTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            request.setReturnedDate(currentTime);
            requestRepository.save(request);

            responseDto.setSuccessCode(SuccessCode.REQUEST_COMPLETE_SUCCESS);
            return responseDto;
        } catch (Exception e) {
            throw new UpdateDataFailException(ErrorCode.ERR_REQUEST_COMPLETE_FAIL);
        }
    }

    @Override
    public ResponseDTO retrieveRequests(PageRequest page, Long locationId) throws DataNotFoundException {
        try {
            ResponseDTO responseDto = new ResponseDTO();
            Page<Request> requests;
            try {
                requests = requestRepository.getAllByLocationAndDefaultState(page, locationId);
            } catch (Exception e) {
                throw new DataNotFoundException(ErrorCode.ERR_RETRIEVE_ASSIGNMENT_FAIL);
            }
            List<ViewRequestDTO> ViewRequestDTO = requestConverter.convertToListDTO(requests);

            responseDto.setData(ViewRequestDTO);
            responseDto.setSuccessCode(SuccessCode.REQUEST_LOADED_SUCCESS);
            return responseDto;
        } catch (Exception e) {
            throw new DataNotFoundException(ErrorCode.ERR_RETRIEVE_REQUEST_FAIL);
        }
    }

    @Override
    public ResponseDTO countRequest(Long locationId) throws DataNotFoundException {
        try {
            ResponseDTO responseDto = new ResponseDTO();
            NumberOfRequestDTO NumberOfrequestDTO = new NumberOfRequestDTO();
            try {

                NumberOfrequestDTO.setNumberOfEntity(requestRepository.CountAllByLocationAndDefaultState(locationId));
            } catch (Exception e) {
                throw new DataNotFoundException(ErrorCode.ERR_COUNT_REQUEST_FAIL);
            }
            responseDto.setData(NumberOfrequestDTO);
            responseDto.setSuccessCode(SuccessCode.REQUEST_COUNT_SUCCESS);
            return responseDto;
        } catch (Exception e) {
            throw new DataNotFoundException(ErrorCode.ERR_COUNT_REQUEST_FAIL);
        }
    }

    @Override
    public ResponseDTO retrieveRequestById(Long locationId, Long requestId) throws DataNotFoundException {
        try {
            ResponseDTO responseDto = new ResponseDTO();
            Request request = requestRepository.findByRequestId(locationId, requestId)
                    .orElseThrow(() -> new DataNotFoundException(ErrorCode.ERR_REQUEST_NOT_FOUND));

            ViewRequestDTO viewRequestDTO = requestConverter.convertToViewDTO(request);

            responseDto.setData(viewRequestDTO);
            responseDto.setSuccessCode(SuccessCode.REQUEST_LOADED_SUCCESS);
            return responseDto;
        } catch (Exception e) {
            throw new DataNotFoundException(ErrorCode.ERR_REQUEST_NOT_FOUND);
        }
    }

    private Pageable getPaging(Integer pageNum, Integer numOfItems,
            SearchFilterSortRequestDTO searchFilterSortRequestDTO) {
        if (!(searchFilterSortRequestDTO.getSortField().isBlank()
                || searchFilterSortRequestDTO.getSortType().isBlank())) {

            if (searchFilterSortRequestDTO.getSortField().equalsIgnoreCase("no")) {
                if (searchFilterSortRequestDTO.getSortType().equalsIgnoreCase(SortType.DSC.toString())) {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("requestId").descending());
                } else {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("requestId").ascending());
                }
            } else if (searchFilterSortRequestDTO.getSortField().equalsIgnoreCase("assetCode")) {
                if (searchFilterSortRequestDTO.getSortType().equalsIgnoreCase(SortType.DSC.toString())) {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("assignment.asset.assetCode").descending());
                } else {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("assignment.asset.assetCode").ascending());
                }
            } else if (searchFilterSortRequestDTO.getSortField().equalsIgnoreCase("assetName")) {
                if (searchFilterSortRequestDTO.getSortType().equalsIgnoreCase(SortType.DSC.toString())) {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("assignment.asset.assetName").descending());
                } else {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("assignment.asset.assetName").ascending());
                }
            } else if (searchFilterSortRequestDTO.getSortField().equalsIgnoreCase("requestedBy")) {
                if (searchFilterSortRequestDTO.getSortType().equalsIgnoreCase(SortType.DSC.toString())) {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("requestedBy.username").descending());
                } else {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("requestedBy.username").ascending());
                }
            } else if (searchFilterSortRequestDTO.getSortField().equalsIgnoreCase("assignedDate")) {
                if (searchFilterSortRequestDTO.getSortType().equalsIgnoreCase(SortType.DSC.toString())) {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("assignment.assignedDate").descending());
                } else {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("assignment.assignedDate").ascending());
                }
            } else if (searchFilterSortRequestDTO.getSortField().equalsIgnoreCase("acceptedBy")) {
                if (searchFilterSortRequestDTO.getSortType().equalsIgnoreCase(SortType.DSC.toString())) {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("acceptedBy.username").descending());
                } else {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("acceptedBy.username").ascending());
                }
            } else if (searchFilterSortRequestDTO.getSortField().equalsIgnoreCase("returnedDate")) {
                if (searchFilterSortRequestDTO.getSortType().equalsIgnoreCase(SortType.DSC.toString())) {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("returnedDate").descending());
                } else {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("returnedDate").ascending());
                }
            } else if (searchFilterSortRequestDTO.getSortField().equalsIgnoreCase("state")) {
                if (searchFilterSortRequestDTO.getSortType().equalsIgnoreCase(SortType.DSC.toString())) {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("state").descending());
                } else {
                    return PageRequest.of(pageNum, numOfItems, Sort.by("state").ascending());
                }
            }
        }
        return PageRequest.of(pageNum, numOfItems, Sort.by("assignment.assignedDate").descending());

    }

    @Override
    public ResponseDTO retrieveRequestHavingFilterSearchSort(Integer pageNum, Integer numOfItems,
            SearchFilterSortRequestDTO searchFilterSortRequestDTO, Long locationId) throws DataNotFoundException {
        try {
            ResponseDTO responseDto = new ResponseDTO();
            Pageable page = this.getPaging(pageNum, numOfItems, searchFilterSortRequestDTO);

            // search assignment (by asset code or asset name or requester’s username) and
            // filter assignment (by state and returned date)

            RequestSpecification assetCode = null;
            RequestSpecification assetName = null;
            RequestSpecification requestedBy = null;
            RequestSpecification state = null;
            RequestSpecification returnedDate = null;
            RequestSpecification requestLocation = new RequestSpecification();
            requestLocation.add(new SearchCriteria("requestedBy", locationId, SearchOperation.EQUAL));
            RequestSpecification requestIsDeleted = new RequestSpecification();
            requestIsDeleted.add(new SearchCriteria("isDeleted", false, SearchOperation.EQUAL));

            if (!searchFilterSortRequestDTO.getStates().isEmpty()) {
                state = new RequestSpecification();
                state.add(new SearchCriteria("state", searchFilterSortRequestDTO.getStates(), SearchOperation.IN));
            }
            if (searchFilterSortRequestDTO.getLocalDateTime() != null) {
                returnedDate = new RequestSpecification();
                returnedDate.add(new SearchCriteria("returnedDate", searchFilterSortRequestDTO.getLocalDateTime(),
                        SearchOperation.EQUAL));
            }
            if (!searchFilterSortRequestDTO.getSearchKeyWord().isBlank()) {
                assetCode = new RequestSpecification();
                assetCode.add(new SearchCriteria("assetCode", searchFilterSortRequestDTO.getSearchKeyWord(),
                        SearchOperation.MATCH));

                assetName = new RequestSpecification();
                assetName.add(new SearchCriteria("assetName", searchFilterSortRequestDTO.getSearchKeyWord(),
                        SearchOperation.MATCH));

                requestedBy = new RequestSpecification();
                requestedBy.add(new SearchCriteria("requestedBy", searchFilterSortRequestDTO.getSearchKeyWord(),
                        SearchOperation.MATCH));
            }

            Specification spec = Specification.where(requestLocation).and(requestIsDeleted);

            if (state != null) {
                spec = spec.and(state);
            }
            if (returnedDate != null) {
                spec = spec.and(returnedDate);
            }
            if (assetCode != null) {
                spec = spec.and(Specification.where(assetCode).or(assetName).or(requestedBy));
            }

            Page<Request> Requests;

            try {
                Requests = requestRepository.findAll(spec, page);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DataNotFoundException(ErrorCode.ERR_RETRIEVE_REQUEST_FAIL);
            }
            List<ViewRequestDTO> viewRequestDTO = requestConverter.convertToListDTO(Requests);

            responseDto.setData(viewRequestDTO);
            responseDto.setSuccessCode(SuccessCode.REQUEST_LOADED_SUCCESS);
            return responseDto;

        } catch (Exception e) {
            e.printStackTrace();
            throw new DataNotFoundException(ErrorCode.ERR_RETRIEVE_REQUEST_FAIL);
        }
    }

    @Override
    public ResponseDTO countRequestHavingFilterSearchSort(SearchFilterSortRequestDTO searchFilterSortRequestDTO,
            Long locationId) throws DataNotFoundException {
        try {
            ResponseDTO responseDto = new ResponseDTO();

            // search assignment (by asset code or asset name or requester’s username) and
            // filter assignment (by state and returned date)

            RequestSpecification assetCode = null;
            RequestSpecification assetName = null;
            RequestSpecification requestedBy = null;
            RequestSpecification state = null;
            RequestSpecification returnedDate = null;
            RequestSpecification requestLocation = new RequestSpecification();
            requestLocation.add(new SearchCriteria("requestedBy", locationId, SearchOperation.EQUAL));
            RequestSpecification requestIsDeleted = new RequestSpecification();
            requestIsDeleted.add(new SearchCriteria("isDeleted", false, SearchOperation.EQUAL));

            if (!searchFilterSortRequestDTO.getStates().isEmpty()) {
                state = new RequestSpecification();
                state.add(new SearchCriteria("state", searchFilterSortRequestDTO.getStates(), SearchOperation.IN));
            }
            if (searchFilterSortRequestDTO.getLocalDateTime() != null) {
                returnedDate = new RequestSpecification();
                returnedDate.add(new SearchCriteria("returnedDate", searchFilterSortRequestDTO.getLocalDateTime(),
                        SearchOperation.EQUAL));
            }
            if (!searchFilterSortRequestDTO.getSearchKeyWord().isBlank()) {
                assetCode = new RequestSpecification();
                assetCode.add(new SearchCriteria("assetCode", searchFilterSortRequestDTO.getSearchKeyWord(),
                        SearchOperation.MATCH));

                assetName = new RequestSpecification();
                assetName.add(new SearchCriteria("assetName", searchFilterSortRequestDTO.getSearchKeyWord(),
                        SearchOperation.MATCH));

                requestedBy = new RequestSpecification();
                requestedBy.add(new SearchCriteria("requestedBy", searchFilterSortRequestDTO.getSearchKeyWord(),
                        SearchOperation.MATCH));
            }

            Specification spec = Specification.where(requestLocation).and(requestIsDeleted);

            if (state != null) {
                spec = spec.and(state);
            }
            if (returnedDate != null) {
                spec = spec.and(returnedDate);
            }
            if (assetCode != null) {
                spec = spec.and(Specification.where(assetCode).or(assetName).or(requestedBy));
            }

            List<Request> Requests;

            try {
                Requests = requestRepository.findAll(spec);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DataNotFoundException(ErrorCode.ERR_COUNT_REQUEST_FAIL);
            }
            List<ViewRequestDTO> viewRequestDTO = requestConverter.convertToListDTO(Requests);
            NumberOfRequestDTO NumberOfrequestDTO = new NumberOfRequestDTO();

            NumberOfrequestDTO.setNumberOfEntity((long) viewRequestDTO.size());

            responseDto.setData(NumberOfrequestDTO);
            responseDto.setSuccessCode(SuccessCode.REQUEST_COUNT_SUCCESS);
            return responseDto;

        } catch (Exception e) {
            e.printStackTrace();
            throw new DataNotFoundException(ErrorCode.ERR_COUNT_REQUEST_FAIL);
        }
    }

    @Override
    public ResponseDTO createRequest(CreateRequestDTO createRequestDTO) throws CreateDataFailException {
        ResponseDTO responseDTO = new ResponseDTO();

        Request request = requestConverter.convertCreateRequestDtoToEntity(createRequestDTO);

        if (!request.getAssignment().getState().equals(State.ACCEPTED))
            throw new CreateDataFailException(ErrorCode.ERR_REQUEST_ASSIGNMENT_NOT_ACCEPT);

        User user = request.getRequestedBy();
        if (user.getRole().getRoleName().equals(ERole.ROLE_USER)) {
            if (request.getAssignment().getAssignedTo() != user)
                throw new CreateDataFailException(ErrorCode.ERR_CREATE_REQUEST_NOT_ALLOW);
        }

        try {
            Request saveRequest = requestRepository.save(request);
            responseDTO.setSuccessCode(SuccessCode.REQUEST_CREATE_SUCCESS);
            responseDTO.setData(requestConverter.convertToViewDTO(saveRequest));
            return responseDTO;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CreateDataFailException(ErrorCode.ERR_CREATE_REQUEST_FAIL);
        }
    }
}
