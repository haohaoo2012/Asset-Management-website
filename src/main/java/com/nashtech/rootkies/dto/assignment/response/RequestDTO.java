package com.nashtech.rootkies.dto.assignment.response;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class RequestDTO {
    private Long requestId;

    private UserDTO requestedBy;

    private UserDTO acceptedBy;

    private LocalDateTime returnedDate;

    private Short state;

    private Boolean isDeleted;
}
