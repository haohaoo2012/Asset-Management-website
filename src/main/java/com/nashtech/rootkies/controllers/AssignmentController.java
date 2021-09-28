package com.nashtech.rootkies.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.nashtech.rootkies.constants.ErrorCode;
import com.nashtech.rootkies.constants.SuccessCode;
import com.nashtech.rootkies.converter.AssignmentConverter;
import com.nashtech.rootkies.converter.LocationConverter;
import com.nashtech.rootkies.dto.assignment.request.CreateAssignmentDTO;
import com.nashtech.rootkies.dto.assignment.request.EditAssignmentDTO;
import com.nashtech.rootkies.dto.assignment.request.SearchFilterSortAssignmentDTO;
import com.nashtech.rootkies.dto.common.ResponseDTO;
import com.nashtech.rootkies.dto.ownassignment.request.OwnAssignmentRequest;
import com.nashtech.rootkies.dto.ownassignment.response.OwnAssignmentDetail;
import com.nashtech.rootkies.dto.ownassignment.response.OwnAssignmentResponse;
import com.nashtech.rootkies.exception.DataNotFoundException;
import com.nashtech.rootkies.exception.DeleteDataFailException;
import com.nashtech.rootkies.exception.InvalidRequestDataException;
import com.nashtech.rootkies.exception.UpdateDataFailException;
import com.nashtech.rootkies.exception.*;
import com.nashtech.rootkies.model.Assignment;
import com.nashtech.rootkies.security.jwt.JwtUtils;
import com.nashtech.rootkies.service.AssignmentService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/assignment")
@Tag(name = "ASSIGNMENT", description = "ASSIGNMENT API")
public class AssignmentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssignmentController.class);

    private final AssignmentService assignmentService;

    private final LocationConverter locationConverter;

    private final JwtUtils jwtUtils;

    private final AssignmentConverter assignmentConverter;

    @Autowired
    public AssignmentController(AssignmentService assignmentService, LocationConverter locationConverter,
            JwtUtils jwtUtils, AssignmentConverter assignmentConverter) {
        this.assignmentService = assignmentService;
        this.locationConverter = locationConverter;
        this.jwtUtils = jwtUtils;
        this.assignmentConverter = assignmentConverter;
    }

    @Operation(summary = "Get all assignment", description = "", tags = { "ASSIGNMENT" }, security = {
            @SecurityRequirement(name = "bearer-key-admin") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> retrieveAssignments(HttpServletRequest req,
            @RequestParam(name = "page", required = true) Integer pageNum,
            @RequestParam(name = "size", required = true) Integer numOfItems) throws DataNotFoundException {
        String jwt = req.getHeader("Authorization").substring(7, req.getHeader("Authorization").length());
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        Long locationId = locationConverter.getLocationIdFromUsername(username);
        return ResponseEntity.ok(assignmentService.retrieveAssignments(
                PageRequest.of(pageNum, numOfItems, Sort.by("assignedDate").descending()), locationId));
    }

    @Operation(summary = "Count all customer", description = "", tags = { "ASSIGNMENT" }, security = {
            @SecurityRequirement(name = "bearer-key-admin") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> countAssignment(HttpServletRequest req) throws DataNotFoundException {
        String jwt = req.getHeader("Authorization").substring(7, req.getHeader("Authorization").length());
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        Long locationId = locationConverter.getLocationIdFromUsername(username);
        return ResponseEntity.ok(assignmentService.countAssignment(locationId));
    }

    @Operation(summary = "Get assignment by id", description = "", tags = { "ASSIGNMENT" }, security = {
            @SecurityRequirement(name = "bearer-key-admin"), @SecurityRequirement(name = "bearer-key-user") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @GetMapping("/{assignmentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ResponseDTO> retrieveAssignmentById(HttpServletRequest req,
            @PathVariable("assignmentId") Long assignmentId) throws DataNotFoundException {
        String jwt = req.getHeader("Authorization").substring(7, req.getHeader("Authorization").length());
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        Long locationId = locationConverter.getLocationIdFromUsername(username);
        return ResponseEntity.ok(assignmentService.retrieveAssignmentByAssignmentId(locationId, assignmentId));

    }

    @Operation(summary = "Get all assignment with filter, search, sort", description = "", tags = { "ASSIGNMENT" }, security = {
            @SecurityRequirement(name = "bearer-key-admin"), @SecurityRequirement(name = "bearer-key-user") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @PostMapping("/filter-search-sort")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ResponseDTO> retrieveAssignmentHavingFilterSearchSort(HttpServletRequest req,
            @RequestParam(name = "page", required = true) Integer pageNum,
            @RequestParam(name = "size", required = true) Integer numOfItems,
            @RequestBody SearchFilterSortAssignmentDTO searchFilterSortAssignmentDTO) throws DataNotFoundException {
        String jwt = req.getHeader("Authorization").substring(7, req.getHeader("Authorization").length());
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        Long locationId = locationConverter.getLocationIdFromUsername(username);
        return ResponseEntity.ok(assignmentService.retrieveAssignmentHavingFilterSearchSort(pageNum, numOfItems,
                searchFilterSortAssignmentDTO, locationId));
    }

    @Operation(summary = "Count all assignment with filter, search, sort", description = "", tags = { "ASSIGNMENT" }, security = {
            @SecurityRequirement(name = "bearer-key-admin"), @SecurityRequirement(name = "bearer-key-user") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @PostMapping("/count/filter-search-sort")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ResponseDTO> countAssignmentHavingFilterSearchSort(HttpServletRequest req,
            @RequestBody SearchFilterSortAssignmentDTO searchFilterSortAssignmentDTO) throws DataNotFoundException {
        String jwt = req.getHeader("Authorization").substring(7, req.getHeader("Authorization").length());
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        Long locationId = locationConverter.getLocationIdFromUsername(username);
        return ResponseEntity
                .ok(assignmentService.countAssignmentHavingFilterSearchSort(searchFilterSortAssignmentDTO, locationId));
    }

    @Operation(summary = "Create assignment", description = "", tags = { "ASSIGNMENT" }, security = {
            @SecurityRequirement(name = "bearer-key-admin") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> createAssignment(@Valid @RequestBody CreateAssignmentDTO dto,
            HttpServletRequest req) throws AssignmentConvertException, DataNotFoundException {

        ResponseDTO response = new ResponseDTO();

        String jwt = req.getHeader("Authorization").substring(7, req.getHeader("Authorization").length());
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        String assignedBy = locationConverter.getStaffCodeFromUsername(username);
        dto.setAssignedBy(assignedBy);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        if (!(LocalDateTime.parse(dto.getAssignedDate(), formatter).toLocalDate().isEqual(LocalDate.now())
                || LocalDateTime.parse(dto.getAssignedDate(), formatter).toLocalDate().isAfter(LocalDate.now()))) {
            response.setErrorCode(ErrorCode.ERR_ASSIGNED_DATE_IN_PAST);
            return ResponseEntity.badRequest().body(response);
        }

        Assignment assignment = assignmentService.createAssignment(dto);

        response.setData(assignmentConverter.entityToDTO(assignment));
        response.setSuccessCode(SuccessCode.CREATE_ASSIGNMENT_SUCCESS);
        return ResponseEntity.ok().body(response);

    }

    @Operation(summary = "Delete assignment by id", description = "", tags = { "ASSIGNMENT" }, security = {
            @SecurityRequirement(name = "bearer-key-admin") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @DeleteMapping("/{assignmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> deleteAssignmentById(HttpServletRequest req,
            @PathVariable("assignmentId") Long assignmentId) throws DataNotFoundException, DeleteDataFailException {
        String jwt = req.getHeader("Authorization").substring(7, req.getHeader("Authorization").length());
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        Long locationId = locationConverter.getLocationIdFromUsername(username);
        return ResponseEntity.ok(assignmentService.deleteAssetByAssignmentId(locationId, assignmentId));
    }

    @Operation(summary = "Edit assignment", description = "", tags = { "ASSIGNMENT" }, security = {
            @SecurityRequirement(name = "bearer-key-admin") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> editAssignment(HttpServletRequest req,
            @Valid @RequestBody EditAssignmentDTO EditAssignmentDTO)
            throws DataNotFoundException, InvalidRequestDataException, UpdateDataFailException {
        String jwt = req.getHeader("Authorization").substring(7, req.getHeader("Authorization").length());
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        Long locationId = locationConverter.getLocationIdFromUsername(username);
        Assignment assignment = assignmentConverter.convertEditAssignmentDTOToEntity(locationId, EditAssignmentDTO,
                username);
        return ResponseEntity.ok(assignmentService.editAssignment(assignment, EditAssignmentDTO.getAssetCode()));
    }

    @Operation(summary = "Accept assignemnt", description = "", tags = { "ASSIGNMENT" }, security = {
            @SecurityRequirement(name = "bearer-key-admin"), @SecurityRequirement(name = "bearer-key-user") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @PutMapping("/accept/{assignmentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ResponseDTO> acceptAssignment(HttpServletRequest req,
            @PathVariable("assignmentId") Long assignmentId)
            throws DataNotFoundException, InvalidRequestDataException, UpdateDataFailException {
        String jwt = req.getHeader("Authorization").substring(7, req.getHeader("Authorization").length());
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        Long locationId = locationConverter.getLocationIdFromUsername(username);
        return ResponseEntity.ok(assignmentService.acceptAssignment(locationId, assignmentId, username));
    }

    @Operation(summary = "Decline assignment", description = "", tags = { "ASSIGNMENT" }, security = {
            @SecurityRequirement(name = "bearer-key-admin"), @SecurityRequirement(name = "bearer-key-user") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @PutMapping("/decline/{assignmentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ResponseDTO> declineAssignment(HttpServletRequest req,
            @PathVariable("assignmentId") Long assignmentId)
            throws DataNotFoundException, InvalidRequestDataException, UpdateDataFailException {
        String jwt = req.getHeader("Authorization").substring(7, req.getHeader("Authorization").length());
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        Long locationId = locationConverter.getLocationIdFromUsername(username);
        return ResponseEntity.ok(assignmentService.declineAssignment(locationId, assignmentId, username));
    }

    // view own assignment
    @Operation(summary = "Get own assignment", description = "", tags = { "ASSIGNMENT" }, security = {
            @SecurityRequirement(name = "bearer-key-admin"), @SecurityRequirement(name = "bearer-key-user") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @PostMapping("/own")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ResponseDTO> viewOwnAssignment(@RequestBody OwnAssignmentRequest request) {
        OwnAssignmentResponse result = assignmentService.viewOwnAssignment(request);
        ResponseDTO response = new ResponseDTO();
        response.setData(result);
        response.setSuccessCode(SuccessCode.LOAD_OWN_ASSIGNMENT_SUCCESS);
        return ResponseEntity.ok(response);
    }

    // own assignment detail
    @Operation(summary = "Get own assignment detail", description = "", tags = { "ASSIGNMENT" }, security = {
            @SecurityRequirement(name = "bearer-key-admin"), @SecurityRequirement(name = "bearer-key-user") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @GetMapping("/own/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ResponseDTO> getOwnAssignmentDetail(@PathVariable("id") Long id) {
        OwnAssignmentDetail detail = assignmentService.getOwnAssignmentDetail(id);
        ResponseDTO response = new ResponseDTO();
        response.setData(detail);
        response.setSuccessCode(SuccessCode.LOAD_OWN_ASSIGNMENT_DETAIL_SUCCESS);
        return ResponseEntity.ok(response);
    }
}
