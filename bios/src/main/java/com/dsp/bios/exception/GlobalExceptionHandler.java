/**
 * 
 * @author 
 * @version 
 */
 
package com.dsp.bios.exception;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.dsp.bios.response.GenericResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@ControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseBody
	public ResponseEntity<GenericResponse<Void>> handleEntityNotFoundExceptionException(EntityNotFoundException ex) {
		GenericResponse<Void> response = new GenericResponse<>(HttpStatus.NOT_FOUND.value(), false, ex.getMessage(),
				null);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseBody
	public ResponseEntity<GenericResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException ex) {
		GenericResponse<Void> response = new GenericResponse<>(HttpStatus.NOT_FOUND.value(), false, ex.getMessage(),
				null);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ApplicationNotFoundException.class)
	@ResponseBody
	public ResponseEntity<GenericResponse<Void>> handleApplicationNotFoundException(ApplicationNotFoundException ex) {
		GenericResponse<Void> response = new GenericResponse<>(HttpStatus.NOT_FOUND.value(), false, ex.getMessage(),
				null);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ModuleNotFoundException.class)
	@ResponseBody
	public ResponseEntity<GenericResponse<Void>> handleModuleNotFoundException(ModuleNotFoundException ex) {
		GenericResponse<Void> response = new GenericResponse<>(HttpStatus.NOT_FOUND.value(), false, ex.getMessage(),
				null);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ResponseEntity<GenericResponse<Map<String, String>>> handleValidationExceptions(
			MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}

		GenericResponse<Map<String, String>> response = new GenericResponse<>(HttpStatus.BAD_REQUEST.value(), false,
				"Validation failed", errors);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}


	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BadRequest.class)
	@ResponseBody
	public ResponseEntity<GenericResponse<Void>> handleBadRequest(BadRequest ex) {
		GenericResponse<Void> response = new GenericResponse<>(HttpStatus.BAD_REQUEST.value(),  false, ex.getMessage(),
				null);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.contentType(MediaType.APPLICATION_JSON)
				.body(response);
	}
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HandlerMethodValidationException.class)
	@ResponseBody
	public ResponseEntity<GenericResponse<Void>> handleBadRequest(HandlerMethodValidationException ex) {


		GenericResponse<Void> response = new GenericResponse<>(HttpStatus.BAD_REQUEST.value(), false,
				"Validation failed", null);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
