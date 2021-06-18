package com.hashedin.mockview.exception;

public class ResourceNotFoundException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public ResourceNotFoundException(String errorMessage) {
        super(errorMessage);
    }

}
