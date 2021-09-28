package com.nashtech.rootkies.dto.asset.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestDTO {
    private Long requestId;

    private UserDTO requestedBy;

    private UserDTO acceptedBy;

    private LocalDateTime returnedDate;

    private Short state;

    private Boolean isDeleted;

}
