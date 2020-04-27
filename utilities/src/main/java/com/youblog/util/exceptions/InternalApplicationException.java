package com.youblog.util.exceptions;

public class InternalApplicationException extends RuntimeException {
	public InternalApplicationException() {
	}

	public InternalApplicationException(String message) {
		super(message);
	}

	public InternalApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public InternalApplicationException(Throwable cause) {
		super(cause);
	}
}
