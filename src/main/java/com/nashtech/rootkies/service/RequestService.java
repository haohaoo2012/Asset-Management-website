package com.nashtech.rootkies.service;

import com.nashtech.rootkies.dto.common.ResponseDTO;
import com.nashtech.rootkies.dto.request.request.CreateRequestDTO;
import com.nashtech.rootkies.dto.request.request.SearchFilterSortRequestDTO;
import com.nashtech.rootkies.exception.CreateDataFailException;
import com.nashtech.rootkies.exception.DataNotFoundException;
import com.nashtech.rootkies.exception.InvalidRequestDataException;
import com.nashtech.rootkies.exception.UpdateDataFailException;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

public interface RequestService {
        public ResponseDTO cancelRequest(Long locationId, Long requestId, String username)
                        throws DataNotFoundException, InvalidRequestDataException, UpdateDataFailException;

        public ResponseDTO completeRequest(Long locationId, Long requestId, String username)
                        throws DataNotFoundException, InvalidRequestDataException, UpdateDataFailException;

        public ResponseDTO retrieveRequests(PageRequest page, Long locationId) throws DataNotFoundException;

        public ResponseDTO countRequest(Long locationId) throws DataNotFoundException;

        public ResponseDTO retrieveRequestById(Long locationId, Long requestId) throws DataNotFoundException;

        public ResponseDTO retrieveRequestHavingFilterSearchSort(Integer pageNum, Integer numOfItems,
                        SearchFilterSortRequestDTO searchFilterSortRequestDTO, Long locationId)
                        throws DataNotFoundException;

        public ResponseDTO countRequestHavingFilterSearchSort(SearchFilterSortRequestDTO searchFilterSortRequestDTO,
                        Long locationId) throws DataNotFoundException;

        public ResponseDTO createRequest(CreateRequestDTO createRequestDTO) throws CreateDataFailException;
}
