package com.hashedin.mockview.controller;

import com.hashedin.mockview.exception.BadRequestException;
import com.hashedin.mockview.exception.DuplicateResourceException;
import com.hashedin.mockview.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Void> handleResourceNotFound(ResourceNotFoundException exception) {
		log.error(exception.getMessage());
		return ResponseEntity.notFound().build();
	}
	
	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<Void> handleDuplicateResource(DuplicateResourceException exception) {
		log.error(exception.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}
	
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Void> handleBadRequest(BadRequestException exception) {
		log.error(exception.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}

}
