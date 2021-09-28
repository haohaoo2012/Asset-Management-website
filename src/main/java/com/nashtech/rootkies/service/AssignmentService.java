package com.nashtech.rootkies.service;

import com.nashtech.rootkies.dto.assignment.request.CreateAssignmentDTO;
import com.nashtech.rootkies.dto.assignment.request.SearchFilterSortAssignmentDTO;
import com.nashtech.rootkies.dto.common.ResponseDTO;
import com.nashtech.rootkies.dto.ownassignment.request.OwnAssignmentRequest;
import com.nashtech.rootkies.dto.ownassignment.response.OwnAssignmentDetail;
import com.nashtech.rootkies.dto.ownassignment.response.OwnAssignmentResponse;
import com.nashtech.rootkies.exception.DataNotFoundException;
import com.nashtech.rootkies.exception.DeleteDataFailException;
import com.nashtech.rootkies.exception.InvalidRequestDataException;
import com.nashtech.rootkies.exception.UpdateDataFailException;
import com.nashtech.rootkies.model.Assignment;

import com.nashtech.rootkies.model.Assignment;
import org.springframework.data.domain.Pageable;

public interface AssignmentService {
        // search assignment (by asset code or asset name or assignee’s username) and
        // filter assignment (by state, assigned date)

        // sort by asset code, asset name, category, assigne date, state, assign to,
        // assign by, id
        // -------------------------------------------------------------------------------------
        // view assignment list, state = 1,2,3 and isdelete =false and location of the
        // user
        public ResponseDTO countAssignment(Long locationId) throws DataNotFoundException;

        public ResponseDTO retrieveAssignments(Pageable page, Long locationId) throws DataNotFoundException;
        // -------------------------------------------------------------------------------------

        public ResponseDTO retrieveAssignmentByAssignmentId(Long locationId, Long assignmentId)
                        throws DataNotFoundException;

        public ResponseDTO retrieveAssignmentHavingFilterSearchSort(Integer pageNum, Integer numOfItems,
                        SearchFilterSortAssignmentDTO searchFilterSortAssignmentDTO, Long locationId)
                        throws DataNotFoundException;

        public ResponseDTO countAssignmentHavingFilterSearchSort(
                        SearchFilterSortAssignmentDTO searchFilterSortAssignmentDTO, Long locationId)
                        throws DataNotFoundException;

        Assignment createAssignment(CreateAssignmentDTO createAssignmentDTO) throws DataNotFoundException;

        public ResponseDTO deleteAssetByAssignmentId(Long locationId, Long assignmentId)
                        throws DataNotFoundException, DeleteDataFailException;

        public ResponseDTO editAssignment(Assignment assignment, String AssetCode) throws UpdateDataFailException, DataNotFoundException;

        public ResponseDTO acceptAssignment(Long locationId, Long assignmentId, String username)
                        throws DataNotFoundException, InvalidRequestDataException, UpdateDataFailException;

        public ResponseDTO declineAssignment(Long locationId, Long assignmentId, String username)
                        throws DataNotFoundException, InvalidRequestDataException, UpdateDataFailException;
        
        //view own assignments
        public OwnAssignmentResponse viewOwnAssignment(OwnAssignmentRequest request);

        //own assignment detail
        public OwnAssignmentDetail getOwnAssignmentDetail(Long assignmentId);
}
