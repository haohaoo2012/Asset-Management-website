package com.nashtech.rootkies.dto.user.request;

import com.nashtech.rootkies.enums.ERole;
import com.nashtech.rootkies.enums.Gender;
import lombok.*;

import java.time.LocalDateTime;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditUserDTO {
    private String dateOfBirth;
    private String joinedDate;
    private String gender;
    private String role;
}
