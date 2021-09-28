package com.nashtech.rootkies.dto.assignment.request;

import com.nashtech.rootkies.constants.ErrorCode;
import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateAssignmentDTO {


    private String assignedBy;

    @NotBlank(message = ErrorCode.STAFF_CODE_IS_BLANK)
    private String assignedTo;

    @NotBlank(message = ErrorCode.ASSETCODE_IS_BLANK)
    private String assetCode;

    @NotBlank(message = ErrorCode.ASSIGNED_DATE_IS_BLANK)
    private String assignedDate;

    private String note;

}
