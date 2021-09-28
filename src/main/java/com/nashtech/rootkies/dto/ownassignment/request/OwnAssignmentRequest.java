package com.nashtech.rootkies.dto.ownassignment.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OwnAssignmentRequest {
    private int pageNum;
    private int pageSize;
    private String staffCode;
    private String orderBy;
    private String typeOrder;
}
