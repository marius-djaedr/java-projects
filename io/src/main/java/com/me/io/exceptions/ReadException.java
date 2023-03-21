package com.me.io.exceptions;

public class ReadException extends RuntimeException {

	private static final long serialVersionUID = -7044786889910662148L;

	public ReadException() {
		super();
	}

	public ReadException(final String message) {
		super(message);
	}

	public ReadException(final Throwable cause) {
		super(cause);
	}

	public ReadException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
