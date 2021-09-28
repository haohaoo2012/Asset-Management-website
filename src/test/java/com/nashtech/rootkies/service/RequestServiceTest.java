package com.nashtech.rootkies.service;

import com.nashtech.rootkies.constants.ErrorCode;
import com.nashtech.rootkies.constants.State;
import com.nashtech.rootkies.constants.SuccessCode;
import com.nashtech.rootkies.converter.RequestConverter;
import com.nashtech.rootkies.dto.common.ResponseDTO;
import com.nashtech.rootkies.dto.request.request.CreateRequestDTO;
import com.nashtech.rootkies.exception.CreateDataFailException;
import com.nashtech.rootkies.exception.InvalidRequestDataException;
import com.nashtech.rootkies.model.Assignment;
import com.nashtech.rootkies.model.Request;
import com.nashtech.rootkies.model.User;
import com.nashtech.rootkies.repository.RequestRepository;
import com.nashtech.rootkies.repository.UserRepository;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.constraints.Null;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {
    @MockBean
    private RequestRepository requestRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private RequestService requestService;

    @MockBean
    private RequestConverter requestConverter;

    @Test
    public void createRequestTest() throws CreateDataFailException {
        User user = User.builder().staffCode("SD0002").build();

        Request request = Request.builder()
                .assignment(Assignment.builder()
                        .assignmentId(1L)
                        .state(State.ACCEPTED)
                        .assignedTo(user).build())
                .requestedBy(user)
                .state(State.WAITING_FOR_RETURNING)
                .isDeleted(false)
                .build();

        CreateRequestDTO createRequestDTO = CreateRequestDTO.builder()
                .assignment(1L)
                .requestedBy("SD0002").build();

        when(requestRepository.save(request)).thenReturn(request);
        when(requestConverter.convertCreateRequestDtoToEntity(createRequestDTO)).thenReturn(request);

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setSuccessCode(SuccessCode.REQUEST_CREATE_SUCCESS);
        assertEquals(responseDTO.getSuccessCode(), requestService.createRequest(createRequestDTO).getSuccessCode());
    }

    @Test
    public void completeRequestFailByState() {
        User user = new User();
        user.setStaffCode("SD00001");

        Request request = new Request();
        request.setRequestId(1L);
        request.setRequestedBy(user);
        request.setState(State.COMPLETED);

        when(requestRepository.findByRequestId(anyLong(), anyLong())).thenReturn(Optional.of(request));
        when(userRepository.findByUsername(anyLong(), anyString())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(InvalidRequestDataException.class, () -> {
                requestService.completeRequest(1L, 1L, "abc");
        });

        assertEquals(ErrorCode.ERR_REQUEST_ALREADY_COMPLETE, exception.getMessage());
    }

    @Test
    public void completeRequestFailByIsDelete() {
        User user = new User();
        user.setStaffCode("SD00001");

        Request request = new Request();
        request.setRequestId(1L);
        request.setRequestedBy(user);
        request.setState(State.WAITING_FOR_RETURNING);
        request.setIsDeleted(true);
        
        when(requestRepository.findByRequestId(anyLong(), anyLong())).thenReturn(Optional.of(request));
        when(userRepository.findByUsername(anyLong(), anyString())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(InvalidRequestDataException.class, () -> {
                requestService.completeRequest(1L, 1L, "abc");
        });

        assertEquals(ErrorCode.ERR_REQUEST_IS_DELETED, exception.getMessage());
    }
    @Test
    public void CancelRequestFail() {
        User user = new User();
        user.setStaffCode("SD0001");

        Request request = new Request();
        request.setRequestId(1L);
        request.setRequestedBy(user);
        request.setState(State.COMPLETED);

        when(requestRepository.findByRequestId(anyLong(), anyLong())).thenReturn(Optional.of(request));
        when(userRepository.findByUsername(anyLong(), anyString())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(InvalidRequestDataException.class, () -> {
            requestService.cancelRequest(1L, 1L, "abc");
        });

        assertEquals(ErrorCode.ERR_REQUEST_ALREADY_COMPLETE, exception.getMessage());
    }

}
