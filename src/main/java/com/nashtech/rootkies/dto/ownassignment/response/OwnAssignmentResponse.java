package com.nashtech.rootkies.dto.ownassignment.response;

import java.util.List;

import com.nashtech.rootkies.dto.ownassignment.OwnAssignmentDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OwnAssignmentResponse {
    private int currentPage;
    private int totalPages;
    private Long totalItems;
    private List<OwnAssignmentDTO> ownAssignments;
}
