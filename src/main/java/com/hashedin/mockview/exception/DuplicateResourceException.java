package com.hashedin.mockview.exception;

public class DuplicateResourceException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public DuplicateResourceException(String errorMessage) {
		super(errorMessage);
	}
}
