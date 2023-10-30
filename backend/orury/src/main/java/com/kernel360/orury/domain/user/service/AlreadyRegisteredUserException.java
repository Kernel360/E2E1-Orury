package com.kernel360.orury.domain.user.service;

public class AlreadyRegisteredUserException extends RuntimeException {
	public AlreadyRegisteredUserException(String message) {
		super(message);
	}

	public AlreadyRegisteredUserException() {
		super("이미 등록된 유저입니다.");
	}
}
