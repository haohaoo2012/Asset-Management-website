package com.nashtech.rootkies.dto.asset.response;

import java.time.LocalDateTime;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AssignmentDTO {
    private Long assignmentId;

    private UserDTO assignedTo;

    private UserDTO assignedBy;

    private LocalDateTime assignedDate;

    private Short state;

    private Collection<RequestDTO> requests;

    private Boolean isDeleted;
}
