package com.nashtech.rootkies.exception;

import com.nashtech.rootkies.dto.common.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ConvertEntityDTOException.class)
	public ResponseEntity convertEntityDTOException(ConvertEntityDTOException ex, WebRequest request) {
		ResponseDTO errorResponse = new ResponseDTO(ex.getMessage(), null, null);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConverterUserException.class)
	public ResponseEntity converterUserException(ConverterUserException ex, WebRequest request) {
		ResponseDTO errorResponse = new ResponseDTO(ex.getMessage(), null, null);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(CreateDataFailException.class)
	public ResponseEntity createDataFailException(CreateDataFailException ex, WebRequest request) {
		ResponseDTO errorResponse = new ResponseDTO(ex.getMessage(), null, null);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity dataNotFoundException(DataNotFoundException ex, WebRequest request) {
		ResponseDTO errorResponse = new ResponseDTO(ex.getMessage(), null, null);
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DeleteDataFailException.class)
	public ResponseEntity deleteDataFailException(DeleteDataFailException ex, WebRequest request) {
		ResponseDTO errorResponse = new ResponseDTO(ex.getMessage(), null, null);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DuplicateDataException.class)
	public ResponseEntity duplicateDataException(DuplicateDataException ex, WebRequest request) {
		ResponseDTO errorResponse = new ResponseDTO(ex.getMessage(), null, null);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidRequestDataException.class)
	public ResponseEntity invalidRequestDataException(InvalidRequestDataException ex, WebRequest request) {
		ResponseDTO errorResponse = new ResponseDTO(ex.getMessage(), null, null);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		ResponseDTO errorResponse = new ResponseDTO(ex.getMessage(), null, null);
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UpdateDataFailException.class)
	public ResponseEntity updateDataFailException(UpdateDataFailException ex, WebRequest request) {
		ResponseDTO errorResponse = new ResponseDTO(ex.getMessage(), null, null);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserAuthenticationException.class)
	public ResponseEntity userAuthenticationException(UserAuthenticationException ex, WebRequest request) {
		ResponseDTO errorResponse = new ResponseDTO(ex.getMessage(), null, null);
		return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(UserExistedException.class)
	public ResponseEntity userExistedException(UserExistedException ex, WebRequest request) {
		ResponseDTO errorResponse = new ResponseDTO(ex.getMessage(), null, null);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity userNotFoundException(UserNotFoundException ex, WebRequest request) {
		ResponseDTO errorResponse = new ResponseDTO(ex.getMessage(), null, null);
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserSignupException.class)
	public ResponseEntity userSignupException(UserSignupException ex, WebRequest request) {
		ResponseDTO errorResponse = new ResponseDTO(ex.getMessage(), null, null);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/** Global Exception **/
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> globleExcpetionHandler(Exception ex, WebRequest request) {
		ResponseDTO errorResponse = new ResponseDTO(ex.getMessage(), null, null);
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
