package com.me.io.exceptions;

public class WriteException extends RuntimeException {

	private static final long serialVersionUID = -7044786889910662148L;

	public WriteException() {
		super();
	}

	public WriteException(final String message) {
		super(message);
	}

	public WriteException(final Throwable cause) {
		super(cause);
	}

	public WriteException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
