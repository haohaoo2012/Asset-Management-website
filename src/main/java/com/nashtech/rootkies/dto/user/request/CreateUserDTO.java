package com.nashtech.rootkies.dto.user.request;

import com.nashtech.rootkies.enums.Gender;
import com.nashtech.rootkies.model.Location;
import com.nashtech.rootkies.model.Role;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateUserDTO {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String dateOfBirth;

    private String gender;

    private String joinedDate;

    @NotBlank
    private String role;

    @NotNull
    private Long location;
}
