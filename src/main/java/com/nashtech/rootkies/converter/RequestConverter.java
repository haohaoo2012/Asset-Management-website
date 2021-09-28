package com.nashtech.rootkies.converter;

import com.nashtech.rootkies.constants.ErrorCode;
import com.nashtech.rootkies.constants.State;
import com.nashtech.rootkies.dto.request.request.CreateRequestDTO;
import com.nashtech.rootkies.dto.request.response.ViewRequestDTO;
import com.nashtech.rootkies.dto.user.UserDTO;
import com.nashtech.rootkies.enums.ERole;
import com.nashtech.rootkies.enums.Gender;
import com.nashtech.rootkies.exception.ConvertEntityDTOException;
import com.nashtech.rootkies.model.Assignment;
import com.nashtech.rootkies.model.Request;
import com.nashtech.rootkies.model.User;
import com.nashtech.rootkies.repository.AssignmentRepository;
import com.nashtech.rootkies.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequestConverter {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ViewRequestDTO> convertToListDTO(Page<Request> requests) throws ConvertEntityDTOException {
        try {
            return requests.stream().map(request -> modelMapper.map(request, ViewRequestDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConvertEntityDTOException(ErrorCode.ERR_CONVERT_DTO_ENTITY_FAIL);
        }
    }

    public List<ViewRequestDTO> convertToListDTO(List<Request> requests) throws ConvertEntityDTOException {
        try {
            return requests.stream().map(request -> modelMapper.map(request, ViewRequestDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConvertEntityDTOException(ErrorCode.ERR_CONVERT_DTO_ENTITY_FAIL);
        }
    }

    public ViewRequestDTO convertToViewDTO(Request request) throws ConvertEntityDTOException {
        try {
            return modelMapper.map(request, ViewRequestDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConvertEntityDTOException(ErrorCode.ERR_CONVERT_DTO_ENTITY_FAIL);
        }
    }

    public Request convertCreateRequestDtoToEntity(CreateRequestDTO createRequestDTO) {
        Assignment assignment = assignmentRepository.findById(createRequestDTO.getAssignment()).get();
        User user = userRepository.findByStaffCode(createRequestDTO.getRequestedBy()).get();

        Request request = new Request();
        request.setRequestedBy(user);
        request.setAssignment(assignment);
        request.setState(State.WAITING_FOR_RETURNING);
        request.setIsDeleted(false);

        return request;
    }
}
