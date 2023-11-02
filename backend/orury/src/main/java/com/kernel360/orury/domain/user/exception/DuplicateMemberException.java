package com.kernel360.orury.domain.user.exception;

import net.bytebuddy.implementation.bytecode.Throw;

public class DuplicateMemberException extends RuntimeException {

	public DuplicateMemberException() {
		super();
	}

	public DuplicateMemberException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicateMemberException(String message) {
		super(message);
	}

	public DuplicateMemberException(Throwable cause) {
		super(cause);
	}

}
