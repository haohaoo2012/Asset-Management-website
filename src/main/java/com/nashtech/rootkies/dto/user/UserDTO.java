package com.nashtech.rootkies.dto.user;

import com.nashtech.rootkies.dto.user.request.RoleDTO;
import com.nashtech.rootkies.enums.ERole;
import com.nashtech.rootkies.enums.Gender;
import com.nashtech.rootkies.model.Role;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String staffCode;
    private String status;
    private String firstName;
    private String lastName;
    private String username;
    private String dateOfBirth;
    private String joinedDate;
    private String gender;
    private String role;
    private Long location;
    private Boolean isDeleted;
}
