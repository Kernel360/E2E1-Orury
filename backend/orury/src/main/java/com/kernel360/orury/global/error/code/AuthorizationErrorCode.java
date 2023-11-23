package com.kernel360.orury.global.error.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AuthorizationErrorCode implements ErrorCode {
    THERE_IS_NO_AUTHORITY(403, 403, "권한이 없습니다");

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
