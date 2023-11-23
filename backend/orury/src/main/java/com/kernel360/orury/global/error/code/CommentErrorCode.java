package com.kernel360.orury.global.error.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CommentErrorCode implements ErrorCode{
    THERE_IS_NO_COMMENT(400, 430, "해당 댓글이 존재하지 않습니다.");

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
