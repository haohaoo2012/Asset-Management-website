package com.nashtech.rootkies.dto.user;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailDTO {
    private String staffCode;
    private String fullName;
    private String username;
    private LocalDate joinedDate;
    private LocalDate dateOfBirth;
    private String gender;
    private String type;
    private String location;
}
