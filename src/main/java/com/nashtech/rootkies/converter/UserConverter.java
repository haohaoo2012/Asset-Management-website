package com.nashtech.rootkies.converter;

import com.nashtech.rootkies.constants.ErrorCode;
import com.nashtech.rootkies.dto.PageDTO;
import com.nashtech.rootkies.dto.auth.SignupDTO;
import com.nashtech.rootkies.dto.user.UserDTO;
import com.nashtech.rootkies.dto.user.UserDetailDTO;
import com.nashtech.rootkies.dto.user.UserInAssignmentDTO;
import com.nashtech.rootkies.dto.user.request.CreateUserDTO;
import com.nashtech.rootkies.dto.user.request.EditUserDTO;
import com.nashtech.rootkies.enums.ERole;
import com.nashtech.rootkies.enums.Gender;
import com.nashtech.rootkies.exception.ConvertEntityDTOException;
import com.nashtech.rootkies.exception.ConverterUserException;
import com.nashtech.rootkies.model.User;
import com.nashtech.rootkies.repository.LocationRepository;
import com.nashtech.rootkies.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConverter {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    LocationRepository locationRepository;

    public UserDetailDTO entityToDetailDTO(User user) {
        try {
            return UserDetailDTO.builder()
                    .staffCode(user.getStaffCode())
                    .fullName(user.getFirstName() + " " + user.getLastName())
                    .username(user.getUsername())
                    .joinedDate(user.getJoinedDate().toLocalDate())
                    .dateOfBirth(user.getDateOfBirth().toLocalDate())
                    .gender(user.getGender().toString())
                    .type(user.getRole().getRoleName().toString())
                    .location(user.getLocation().getAddress())
                    .build();
        } catch (ConverterUserException ex) {
            throw new ConverterUserException(ErrorCode.ERR_CONVERT_ENTITY_DTO_FAIL);
        }

    }

    public PageDTO pageToPageDto(Page<User> page) {

        List<UserDetailDTO> users = page.stream()
                .map(user -> entityToDetailDTO(user))
                .collect(Collectors.toList());

        return PageDTO.builder()
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .data(users)
                .build();

    }




    public User convertToEntity(SignupDTO signupDto) throws ConvertEntityDTOException {
        User user;
        try {
            user = modelMapper.map(signupDto, User.class);
            /*user.setPassword(encoder.encode(signupDto.getPassword()));
            user.setCreatedDate(LocalDateTime.now());
            user.setDeleted(false);
            user.setStatus(UserStatus.INACTIVE.name());*/
        } catch (Exception ex) {
            throw new ConvertEntityDTOException(ErrorCode.ERR_CONVERT_DTO_ENTITY_FAIL);
        }

        return user;
    }

    public UserDTO convertToDto(User user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        //gender
        userDTO.setGender(user.getGender().name());
        //date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        userDTO.setDateOfBirth(user.getDateOfBirth().format(formatter));
        userDTO.setJoinedDate(user.getJoinedDate().format(formatter));
        //role
        userDTO.setRole(user.getRole().getRoleName().name());
        return userDTO;
    }

    public User convertToEti(UserDTO userDTO) {
        User user = modelMapper.map(userDTO , User.class);
        //gender
        user.setGender(Gender.valueOf(userDTO.getGender()));
        //role
        ERole eRole = ERole.valueOf(userDTO.getRole());
        user.setRole(roleRepository.findByRoleName(eRole).get());
        //convert string to datetime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        user.setDateOfBirth(LocalDateTime.parse(userDTO.getDateOfBirth(), formatter));
        user.setJoinedDate(LocalDateTime.parse(userDTO.getJoinedDate(), formatter));
        //location
        user.setLocation(locationRepository.findById(userDTO.getLocation()).get());
        return user;
    }


    public List<UserDTO> toListDto(List<User> listEntity) {
        List<UserDTO> listDto = new ArrayList<>();

        listEntity.forEach(e->{
            listDto.add(this.convertToDto(e));
        });
        return listDto;
    }

    public User convertEditUserDTOtoEntity(EditUserDTO editUserDTO) throws ConvertEntityDTOException {
        try {
            User user = modelMapper.map(editUserDTO, User.class);
            //gender
            user.setGender(Gender.valueOf(editUserDTO.getGender()));
            //role
            ERole eRole = ERole.valueOf(editUserDTO.getRole());
            user.setRole(roleRepository.findByRoleName(eRole).get());
            //convert string to datetime
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            user.setDateOfBirth(LocalDateTime.parse(editUserDTO.getDateOfBirth(), formatter));
            user.setJoinedDate(LocalDateTime.parse(editUserDTO.getJoinedDate(), formatter));
            return user;
        } catch (Exception ex) {
            throw new ConvertEntityDTOException(ErrorCode.ERR_CONVERT_DTO_ENTITY_FAIL);
        }
    }

    public User convertToEntity(CreateUserDTO dto) throws ConvertEntityDTOException {
        try {
            User user = modelMapper.map(dto, User.class);
            /*dto.getRoles().stream().map(role -> roleRepository.findByName(role.getName()).get()).collect(Collectors.toSet());
            user.setPassword(encoder.encode(dto.getPassword()));
            user.setCreatedDate(LocalDateTime.now());
            user.setDeleted(false);
            user.setStatus(UserStatus.ACTIVE.name());*/
            return user;
        } catch (Exception ex) {
            throw new ConvertEntityDTOException(ErrorCode.ERR_CONVERT_DTO_ENTITY_FAIL);
        }
    }

    public UserDTO convertToDTO(User user) throws ConvertEntityDTOException {
        try {
            UserDTO dto = modelMapper.map(user, UserDTO.class);
            /*dto.setRoles(user.getRoles().stream().map(role -> modelMapper.map(role , RoleDTO.class))
                    .collect(Collectors.toSet()));*/
            return dto;
        } catch (Exception ex) {
            throw new ConvertEntityDTOException(ErrorCode.ERR_CONVERT_DTO_ENTITY_FAIL);
        }
    }

    public User convertCreateUserDTOtoEntity(CreateUserDTO createUserDTO) throws ConvertEntityDTOException {
        try {
            User user = modelMapper.map(createUserDTO, User.class);
            //gender
            user.setGender(Gender.valueOf(createUserDTO.getGender()));
            //role
            ERole eRole = ERole.valueOf(createUserDTO.getRole());
            user.setRole(roleRepository.findByRoleName(eRole).get());
            //convert string to datetime
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            user.setDateOfBirth(LocalDateTime.parse(createUserDTO.getDateOfBirth(), formatter));
            user.setJoinedDate(LocalDateTime.parse(createUserDTO.getJoinedDate(), formatter));
            //location
            user.setLocation(locationRepository.findById(createUserDTO.getLocation()).get());
            return user;
        } catch (Exception ex) {
            throw new ConvertEntityDTOException(ErrorCode.ERR_CONVERT_DTO_ENTITY_FAIL);
        }
    }

    public UserInAssignmentDTO entityToUserAssignmentDTO(User user){
        return UserInAssignmentDTO.builder()
                .staffCode(user.getStaffCode())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .role(user.getRole().getRoleName().toString().replace("ROLE_" , ""))
                .build();
    }

    public PageDTO entityToUserAssignmentDTO(Page<User> page){


            List<UserInAssignmentDTO> users = page.stream()
                    .map(user -> entityToUserAssignmentDTO(user))
                    .collect(Collectors.toList());

            return PageDTO.builder()
                    .totalPages(page.getTotalPages())
                    .totalElements(page.getTotalElements())
                    .data(users)
                    .build();


    }
}
