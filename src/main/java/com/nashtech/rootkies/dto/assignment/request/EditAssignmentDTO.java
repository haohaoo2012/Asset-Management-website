package com.nashtech.rootkies.dto.assignment.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class EditAssignmentDTO {
    @NotNull
    private Long assignmentId;

    @NotBlank
    private String assignedToUserId;

    @NotNull
    private LocalDateTime assignedDate;


    private String note;

    @NotBlank
    private String assetCode;

}
