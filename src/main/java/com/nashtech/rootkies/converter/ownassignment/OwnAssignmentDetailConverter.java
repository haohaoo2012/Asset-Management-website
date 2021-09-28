package com.nashtech.rootkies.converter.ownassignment;

import com.nashtech.rootkies.constants.State;
import com.nashtech.rootkies.dto.ownassignment.response.OwnAssignmentDetail;
import com.nashtech.rootkies.model.Assignment;

import org.springframework.stereotype.Component;

@Component
public class OwnAssignmentDetailConverter {
    
    public OwnAssignmentDetail convert(Assignment assignment){
        OwnAssignmentDetail detail = new OwnAssignmentDetail();
        detail.setAssetCode(assignment.getAsset().getAssetCode());
        detail.setAssetName(assignment.getAsset().getAssetName());
        detail.setSpecification(assignment.getAsset().getSpecification());
        detail.setAssignedTo(assignment.getAssignedTo().getUsername());
        detail.setAssignedBy(assignment.getAssignedBy().getUsername());
        detail.setAssignedDate(assignment.getAssignedDate().toString().split("T")[0]);
        detail.setNote(assignment.getNote());
        if(assignment.getState() == State.ACCEPTED){
            detail.setState("Accepted");
        }
        else if(assignment.getState() == State.WAITING_FOR_ACCEPTANCE){
            detail.setState("Waiting for acceptance");
        }
        return detail;
    }
}
