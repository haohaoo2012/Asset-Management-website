package com.nashtech.rootkies.dto.assignment.response;

import com.nashtech.rootkies.enums.Gender;

import java.time.LocalDateTime;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {

	private String staffCode;

	private String username;

	private String firstName;

	private String lastName;
}
