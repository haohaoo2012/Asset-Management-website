package com.nashtech.rootkies.security.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import com.nashtech.rootkies.model.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private String staffCode;
    private String username;
    private String password;
    private String firstName;
    private  String lastName;
    private LocalDateTime dateOfBirth;
    private LocalDateTime joinedDate;
    private String gender;
    private Long idLocation;
    private String role;
    private Boolean firstLogin;
    private Boolean isDeleted;

    public static UserDetailsImpl build(User user) {
		return new UserDetailsImpl(user.getStaffCode(), user.getUsername(),
            user.getPassword(), user.getFirstName(), user.getLastName(),
            user.getDateOfBirth(), user.getJoinedDate(), user.getGender().name(), 
            user.getLocation().getLocationId(), user.getRole().getRoleName().name(),
            user.getFirstLogin(), user.getIsDeleted()
        );
	}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
		return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !this.isDeleted;
    }
    
}
