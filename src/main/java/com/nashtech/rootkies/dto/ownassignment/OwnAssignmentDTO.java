package com.nashtech.rootkies.dto.ownassignment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OwnAssignmentDTO {
    private Long assignmentId;
    private String assetCode;
    private String assetName;
    private String categoryName;
    private String assignedDate;
    private String state;
    private Boolean isReturnRequest;
}
