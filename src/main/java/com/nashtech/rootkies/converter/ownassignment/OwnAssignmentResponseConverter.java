package com.nashtech.rootkies.converter.ownassignment;

import java.util.List;

import com.nashtech.rootkies.dto.ownassignment.OwnAssignmentDTO;
import com.nashtech.rootkies.dto.ownassignment.response.OwnAssignmentResponse;
import com.nashtech.rootkies.model.Assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class OwnAssignmentResponseConverter {
    
    @Autowired
    private OwnAssignmentDTOConverter converter;

    public OwnAssignmentResponse convert(int pageNo, Page<Assignment> page){
        OwnAssignmentResponse result = new OwnAssignmentResponse();
        result.setCurrentPage(pageNo);
        result.setTotalPages(page.getTotalPages());
        result.setTotalItems(page.getTotalElements());

        List<OwnAssignmentDTO> list = converter.toDTOList(page.getContent());
        result.setOwnAssignments(list);
        return result;
    }
}
