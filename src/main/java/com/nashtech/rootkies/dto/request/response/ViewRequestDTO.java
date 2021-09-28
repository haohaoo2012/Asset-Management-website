package com.nashtech.rootkies.dto.request.response;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ViewRequestDTO {
    private Long requestId;

    private UserDTO requestedBy;

    private UserDTO acceptedBy;

    private LocalDateTime returnedDate;

    private Short state;

    private AssignmentDTO assignment;

}
