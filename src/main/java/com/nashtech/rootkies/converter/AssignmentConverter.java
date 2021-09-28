package com.nashtech.rootkies.converter;

import com.nashtech.rootkies.constants.ErrorCode;
import com.nashtech.rootkies.constants.State;
import com.nashtech.rootkies.dto.assignment.request.CreateAssignmentDTO;
import com.nashtech.rootkies.dto.assignment.request.EditAssignmentDTO;
import com.nashtech.rootkies.dto.assignment.response.AssetDTO;
import com.nashtech.rootkies.dto.assignment.response.RequestDTO;
import com.nashtech.rootkies.dto.assignment.response.UserDTO;
import com.nashtech.rootkies.dto.assignment.response.ViewAssignmentDTO;
import com.nashtech.rootkies.exception.AssignmentConvertException;
import com.nashtech.rootkies.exception.ConvertEntityDTOException;
import com.nashtech.rootkies.exception.DataNotFoundException;
import com.nashtech.rootkies.exception.InvalidRequestDataException;
import com.nashtech.rootkies.model.Asset;
import com.nashtech.rootkies.model.Assignment;
import com.nashtech.rootkies.model.Request;
import com.nashtech.rootkies.model.User;
import com.nashtech.rootkies.repository.AssetRepository;
import com.nashtech.rootkies.repository.AssignmentRepository;
import com.nashtech.rootkies.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AssignmentConverter {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    AssetRepository assetRepository;

    @Autowired
    UserRepository userRepository;

    public List<ViewAssignmentDTO> convertToListDTO(Page<Assignment> assignments) throws ConvertEntityDTOException {
        try {
            List<ViewAssignmentDTO> listViewAssignmentDTO = assignments.stream()
                    .map(assignment -> modelMapper.map(assignment, ViewAssignmentDTO.class))
                    .collect(Collectors.toList());
            listViewAssignmentDTO.forEach(e -> {
                Collection<RequestDTO> filteredCollection = e.getRequests().stream().filter(e1 -> !e1.getIsDeleted())
                        .collect(Collectors.toList());
                e.setRequests(filteredCollection);
            });
            return listViewAssignmentDTO;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConvertEntityDTOException(ErrorCode.ERR_CONVERT_DTO_ENTITY_FAIL);
        }
    }

    public List<ViewAssignmentDTO> convertToListDTO(List<Assignment> assignments) throws ConvertEntityDTOException {
        try {
            List<ViewAssignmentDTO> listViewAssignmentDTO = assignments.stream()
                    .map(assignment -> modelMapper.map(assignment, ViewAssignmentDTO.class))
                    .collect(Collectors.toList());
            listViewAssignmentDTO.forEach(e -> {
                Collection<RequestDTO> filteredCollection = e.getRequests().stream().filter(e1 -> !e1.getIsDeleted())
                        .collect(Collectors.toList());
                e.setRequests(filteredCollection);
            });
            return listViewAssignmentDTO;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConvertEntityDTOException(ErrorCode.ERR_CONVERT_DTO_ENTITY_FAIL);
        }
    }

    public ViewAssignmentDTO convertToViewDTO(Assignment assignment) throws ConvertEntityDTOException {
        try {
            ViewAssignmentDTO viewAssignmentDTO = modelMapper.map(assignment, ViewAssignmentDTO.class);
            Collection<RequestDTO> filteredCollection = viewAssignmentDTO.getRequests().stream()
                    .filter(e -> !e.getIsDeleted()).collect(Collectors.toList());
            viewAssignmentDTO.setRequests(filteredCollection);
            return viewAssignmentDTO;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConvertEntityDTOException(ErrorCode.ERR_CONVERT_DTO_ENTITY_FAIL);
        }
    }

    public Assignment createDTOToEntity(CreateAssignmentDTO dto){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return Assignment.builder()
                .assignedBy(User.builder().staffCode(dto.getAssignedBy()).build())
                .assignedTo(User.builder().staffCode(dto.getAssignedTo()).build())
                .note(dto.getNote())
                .state((short)2)
                .asset(Asset.builder().assetCode(dto.getAssetCode()).build())
                .isDeleted(false)
                .assignedDate(LocalDateTime.parse(dto.getAssignedDate() , formatter))
                .build();
    }

    public Assignment convertEditAssignmentDTOToEntity(Long locationId, @Valid EditAssignmentDTO editAssignmentDTO,
            String username) throws DataNotFoundException, InvalidRequestDataException {
        Assignment assignment = assignmentRepository.findByAssignmentId(locationId, editAssignmentDTO.getAssignmentId())
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.ERR_ASSIGNMENT_ID_NOT_FOUND));

        if (assignment.getState() != State.WAITING_FOR_ACCEPTANCE) {
            throw new InvalidRequestDataException(ErrorCode.ERR_ASSIGNMENT_ALREADY_ACCEPTED_OR_DECLINED);
        }

        LocalDateTime ldtNow = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime ldtToSet = editAssignmentDTO.getAssignedDate().withHour(0).withMinute(0).withSecond(0)
                .withNano(0);
        if (ldtToSet.compareTo(ldtNow) < 0) {
            throw new InvalidRequestDataException(ErrorCode.ERR_ASSIGNED_DATE_UPDATE_IS_EARLIER_THAN_CURRENT);
        }
        assignment.setAssignedDate(ldtToSet);

        Asset asset = assetRepository.findByAssetCode(locationId, editAssignmentDTO.getAssetCode())
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.ASSET_NOT_FOUND));
        if (!asset.getAssetCode().equalsIgnoreCase(editAssignmentDTO.getAssetCode())) {
            if (asset.getState() != State.AVAILABLE) {
                throw new InvalidRequestDataException(ErrorCode.ERR_ASSET_NOT_AVAILABLE);
            }
        }

        assignment.setAssignedTo(userRepository.findByStaffCode(locationId, editAssignmentDTO.getAssignedToUserId())
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.ERR_USER_NOT_FOUND)));

        assignment.setAssignedBy(userRepository.findByUsername(locationId, username)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.ERR_USER_NOT_FOUND)));

        assignment.setNote(editAssignmentDTO.getNote());

        return assignment;
    }

    public ViewAssignmentDTO entityToDTO(Assignment assignment) throws AssignmentConvertException {
        try {
            var asset = assetRepository.findById(assignment.getAsset().getAssetCode()).get();
            var assignedBy = userRepository.findById(assignment.getAssignedBy().getStaffCode())
                    .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));
            var assignedTo = userRepository.findById(assignment.getAssignedTo().getStaffCode())
                    .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));
            return ViewAssignmentDTO.builder()
                    .assignmentId(assignment.getAssignmentId())
                    .assignedTo(new UserDTO(assignedTo.getStaffCode(),assignedBy.getUsername(),assignedBy.getFirstName(),assignedBy.getLastName()))
                    .assignedBy(new UserDTO(assignedBy.getStaffCode(),assignedTo.getUsername(),assignedTo.getFirstName(),assignedTo.getLastName()))
                    .assignedDate(assignment.getAssignedDate())
                    .state(assignment.getState())
                    .note(assignment.getNote())
                    .asset(new AssetDTO(asset.getAssetCode() , asset.getAssetName() , asset.getSpecification() , null ))
                    .requests(null)
                    .build();
        }catch (Exception ex){
            throw  new AssignmentConvertException(ErrorCode.ERR_CONVERT_ASSIGNMENT_TO_DTO);

        }

    }

}
