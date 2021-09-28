package com.nashtech.rootkies.dto.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInAssignmentDTO {
    String staffCode;
    String fullName;
    String role;
}
