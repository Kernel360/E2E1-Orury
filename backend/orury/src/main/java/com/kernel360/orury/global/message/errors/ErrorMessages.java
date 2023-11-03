package com.kernel360.orury.global.message.errors;

import lombok.Getter;

@Getter
public enum ErrorMessages {
    THERE_IS_NO_BOARD("게시판이 존재하지 않습니다."),
    THERE_IS_NO_POST("게시글이 존재하지 않습니다."),
    THERE_IS_NO_COMMENT("댓글이 존재하지 않습니다."),
    ;

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

}
