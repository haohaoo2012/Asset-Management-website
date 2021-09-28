package com.nashtech.rootkies.converter;

import java.util.Optional;

import com.nashtech.rootkies.constants.ErrorCode;
import com.nashtech.rootkies.exception.DataNotFoundException;
import com.nashtech.rootkies.model.User;
import com.nashtech.rootkies.repository.UserRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocationConverter {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserRepository userRepository;

    public Long getLocationIdFromUsername(String username) throws DataNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.ERR_USER_NOT_FOUND));

        return user.getLocation().getLocationId();
    }

    public String getStaffCodeFromUsername(String username) throws DataNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.ERR_USER_NOT_FOUND));

        return user.getStaffCode();
    }
}
