package com.kernel360.orury.global.error.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BoardErrorCode implements ErrorCode {
    THERE_IS_NO_BOARD(400, 410, "해당 게시판이 존재하지 않습니다.");

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

