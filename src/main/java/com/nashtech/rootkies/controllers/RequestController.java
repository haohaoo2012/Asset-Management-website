package com.nashtech.rootkies.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import com.nashtech.rootkies.converter.LocationConverter;
import com.nashtech.rootkies.dto.common.ResponseDTO;
import com.nashtech.rootkies.dto.request.request.CreateRequestDTO;
import com.nashtech.rootkies.dto.request.request.SearchFilterSortRequestDTO;
import com.nashtech.rootkies.exception.CreateDataFailException;
import com.nashtech.rootkies.exception.DataNotFoundException;
import com.nashtech.rootkies.exception.InvalidRequestDataException;
import com.nashtech.rootkies.exception.UpdateDataFailException;
import com.nashtech.rootkies.security.jwt.JwtUtils;
import com.nashtech.rootkies.service.RequestService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/request")
@Tag(name = "REQUEST", description = "REQUEST API")
public class RequestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestController.class);

    private final RequestService requestService;

    private final LocationConverter locationConverter;

    private final JwtUtils jwtUtils;

    @Autowired
    public RequestController(RequestService requestService, LocationConverter locationConverter, JwtUtils jwtUtils) {
        this.requestService = requestService;
        this.locationConverter = locationConverter;
        this.jwtUtils = jwtUtils;
    }

    @Operation(summary = "Complete request", description = "", tags = { "REQUEST" }, security = {
            @SecurityRequirement(name = "bearer-key-admin") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @PutMapping("/complete/{requestId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> completeRequest(HttpServletRequest req,
            @PathVariable("requestId") Long requestId)
            throws DataNotFoundException, InvalidRequestDataException, UpdateDataFailException {
        String jwt = req.getHeader("Authorization").substring(7, req.getHeader("Authorization").length());
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        Long locationId = locationConverter.getLocationIdFromUsername(username);
        return ResponseEntity.ok(requestService.completeRequest(locationId, requestId, username));
    }

    @Operation(summary = "Cancel request", description = "", tags = { "REQUEST" }, security = {
            @SecurityRequirement(name = "bearer-key-admin") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @PutMapping("/cancel/{requestId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> cancelRequest(HttpServletRequest req, @PathVariable("requestId") Long requestId)
            throws DataNotFoundException, InvalidRequestDataException, UpdateDataFailException {
        String jwt = req.getHeader("Authorization").substring(7, req.getHeader("Authorization").length());
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        Long locationId = locationConverter.getLocationIdFromUsername(username);
        return ResponseEntity.ok(requestService.cancelRequest(locationId, requestId, username));
    }

    @Operation(summary = "Get all request", description = "", tags = { "REQUEST" }, security = {
            @SecurityRequirement(name = "bearer-key-admin") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> retrieveRequests(HttpServletRequest req,
            @RequestParam(name = "page", required = true) Integer pageNum,
            @RequestParam(name = "size", required = true) Integer numOfItems) throws DataNotFoundException {
        String jwt = req.getHeader("Authorization").substring(7, req.getHeader("Authorization").length());
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        Long locationId = locationConverter.getLocationIdFromUsername(username);
        return ResponseEntity.ok(requestService.retrieveRequests(
                PageRequest.of(pageNum, numOfItems, Sort.by("assignment.assignedDate").descending()), locationId));
    }

    @Operation(summary = "Count all request", description = "", tags = { "REQUEST" }, security = {
            @SecurityRequirement(name = "bearer-key-admin") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> countRequest(HttpServletRequest req) throws DataNotFoundException {
        String jwt = req.getHeader("Authorization").substring(7, req.getHeader("Authorization").length());
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        Long locationId = locationConverter.getLocationIdFromUsername(username);
        return ResponseEntity.ok(requestService.countRequest(locationId));
    }

    @Operation(summary = "Get request by id", description = "", tags = { "REQUEST" }, security = {
            @SecurityRequirement(name = "bearer-key-admin") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @GetMapping("/{requestId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> retrieveRequestById(HttpServletRequest req,
            @PathVariable("requestId") Long requestId) throws DataNotFoundException {
        String jwt = req.getHeader("Authorization").substring(7, req.getHeader("Authorization").length());
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        Long locationId = locationConverter.getLocationIdFromUsername(username);
        return ResponseEntity.ok(requestService.retrieveRequestById(locationId, requestId));

    }

    @Operation(summary = "Get all request with filter, search, sort", description = "", tags = { "REQUEST" }, security = {
            @SecurityRequirement(name = "bearer-key-admin") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @PostMapping("/filter-search-sort")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> retrieveRequestHavingFilterSearchSort(HttpServletRequest req,
            @RequestParam(name = "page", required = true) Integer pageNum,
            @RequestParam(name = "size", required = true) Integer numOfItems,
            @RequestBody SearchFilterSortRequestDTO searchFilterSortRequestDTO) throws DataNotFoundException {
        String jwt = req.getHeader("Authorization").substring(7, req.getHeader("Authorization").length());
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        Long locationId = locationConverter.getLocationIdFromUsername(username);
        return ResponseEntity.ok(requestService.retrieveRequestHavingFilterSearchSort(pageNum, numOfItems,
                searchFilterSortRequestDTO, locationId));
    }

    @Operation(summary = "Count all request with filter, search, sort", description = "", tags = { "REQUEST" }, security = {
            @SecurityRequirement(name = "bearer-key-admin"), @SecurityRequirement(name = "bearer-key-user") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @PostMapping("/count/filter-search-sort")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ResponseDTO> countRequestHavingFilterSearchSort(HttpServletRequest req,
            @RequestBody SearchFilterSortRequestDTO searchFilterSortRequestDTO) throws DataNotFoundException {
        String jwt = req.getHeader("Authorization").substring(7, req.getHeader("Authorization").length());
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        Long locationId = locationConverter.getLocationIdFromUsername(username);
        return ResponseEntity
                .ok(requestService.countRequestHavingFilterSearchSort(searchFilterSortRequestDTO, locationId));
    }

    @Operation(summary = "Create request", description = "", tags = { "REQUEST" }, security = {
            @SecurityRequirement(name = "bearer-key-admin"), @SecurityRequirement(name = "bearer-key-user") })
    @ApiResponses(value = { @ApiResponse(responseCode = "2xx", description = "Successfull"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @PostMapping(value = "/save")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ResponseDTO> createRequest(@Valid @RequestBody CreateRequestDTO createRequestDTO)
            throws CreateDataFailException {
        return ResponseEntity.ok(requestService.createRequest(createRequestDTO));
    }
}
