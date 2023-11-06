package com.kernel360.orury.global.message.errors;

import lombok.Getter;

@Getter
public enum ErrorMessages {
    // 게시판
    THERE_IS_NO_BOARD("게시판이 존재하지 않습니다."),
    THERE_IS_NO_POST("게시글이 존재하지 않습니다."),
    THERE_IS_NO_COMMENT("댓글이 존재하지 않습니다."),

    // 회원
    NOT_EXIST_USER_EMAIL("존재하지 않는 회원입니다. (email 계정 없음)"),
    NOT_ACTIVATED("휴면 처리된 회원입니다."),

    // 인증
    MALFORMED_JWT("잘못된 JWT 서명입니다."),
    EXPIRED_JWT("만료된 JWT 서명입니다."),
    UNSUPPORTED_JWT("지원되지 않는 JWT 토큰입니다."),
    ILLEGAL_ARGUMENT_JWT("JWT 토큰이 잘못되었습니다."),
    ;

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

}
