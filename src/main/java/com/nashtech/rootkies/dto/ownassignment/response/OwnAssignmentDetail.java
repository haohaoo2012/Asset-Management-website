package com.nashtech.rootkies.dto.ownassignment.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OwnAssignmentDetail {
    private String assetCode;
    private String assetName;
    private String specification;
    private String assignedTo;
    private String assignedBy;
    private String assignedDate;
    private String state;
    private String note;
}
