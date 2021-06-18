package com.hashedin.mockview.exception;

public class BadRequestException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public BadRequestException(String errorMessage) {
		super(errorMessage);
	}
}
