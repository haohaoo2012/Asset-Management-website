package com.nashtech.rootkies.converter.ownassignment;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.nashtech.rootkies.constants.State;
import com.nashtech.rootkies.dto.ownassignment.OwnAssignmentDTO;
import com.nashtech.rootkies.model.Assignment;

import com.nashtech.rootkies.model.Request;
import org.springframework.stereotype.Component;

@Component
public class OwnAssignmentDTOConverter {

    public Boolean checkIsReturnRequest(Assignment assignment) {
        Collection<Request> requests = assignment.getRequests();
        for (Request request : requests)
            if (!request.getIsDeleted())
                return true;
        return false;
    }
    
    public OwnAssignmentDTO toDTO(Assignment assignment) {
        OwnAssignmentDTO dto = new OwnAssignmentDTO();
        dto.setAssignmentId(assignment.getAssignmentId());
        dto.setAssetCode(assignment.getAsset().getAssetCode());
        dto.setAssetName(assignment.getAsset().getAssetName());
        dto.setCategoryName(assignment.getAsset().getCategory().getCategoryName());
        dto.setAssignedDate(assignment.getAssignedDate().toString().split("T")[0]);
        if(assignment.getState() == State.ACCEPTED){
            dto.setState("Accepted");
        }
        else if(assignment.getState() == State.WAITING_FOR_ACCEPTANCE){
            dto.setState("Waiting for acceptance");
        }

        dto.setIsReturnRequest(checkIsReturnRequest(assignment));
        return dto;
    }

    public List<OwnAssignmentDTO> toDTOList(List<Assignment> assignments){
        List<OwnAssignmentDTO> dtoList = assignments.stream()
                                        .map(assignment -> toDTO(assignment))
                                        .collect(Collectors.toList());
        return dtoList;
    }
}
