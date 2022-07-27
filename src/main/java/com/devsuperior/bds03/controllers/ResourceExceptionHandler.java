package com.devsuperior.bds03.controllers;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e,HttpServletRequest request) {
		var err = new ValidationError();
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Resource not Found");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		
		for(FieldError field : e.getBindingResult().getFieldErrors()) {
			err.add(field.getField(), field.getDefaultMessage());
		}
		return ResponseEntity.status(status).body(err);
		
	}
	
	
}
