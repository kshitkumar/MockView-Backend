package com.hashedin.mockview.exception;

public class InvalidPasswordException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidPasswordException(String errorMessage) {
        super(errorMessage);
    }

}
