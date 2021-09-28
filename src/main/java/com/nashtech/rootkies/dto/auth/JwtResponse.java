package com.nashtech.rootkies.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private String staffCode;
	private String username;
	private String role;
	private Boolean firstLogin;
	private Long idLocation;
	
	public JwtResponse(String token, 
					String staffCode, 
					String username, 
					String role, 
					Boolean firstLogin,
					Long idLocation) {
		this.token = token;
		this.staffCode = staffCode;
		this.username = username;
		this.role = role;
		this.firstLogin = firstLogin;
		this.idLocation = idLocation;
	}
}
