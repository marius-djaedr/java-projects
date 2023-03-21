package com.me.io.exceptions;

public class FunctionException extends RuntimeException {

	private static final long serialVersionUID = -7044786889910662148L;

	public FunctionException() {
		super();
	}

	public FunctionException(final String message) {
		super(message);
	}

	public FunctionException(final Throwable cause) {
		super(cause);
	}

	public FunctionException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
