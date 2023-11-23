package com.kernel360.orury.global.error.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {
    THERE_IS_NO_USER(400, 400,"해당 유저가 존재하지 않습니다."),
    ALREADY_EXISTING_USER(400, 401, "이미 존재하는 유저입니다."),
    NOT_ACTIVATED(400, 402, "휴면 처리된 회원입니다.");

    private final int status;
    private final int code;
    private final String message;

    @Override
    public int getStatus() { return status; }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
