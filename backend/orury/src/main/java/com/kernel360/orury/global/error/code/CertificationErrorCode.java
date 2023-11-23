package com.kernel360.orury.global.error.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CertificationErrorCode implements ErrorCode {
    MALFORMED_JWT(401, 401, "잘못된 JWT 서명입니다."),
    EXPIRED_ACCESS_JWT(401, 411, "만료된 Access Token 입니다."),
    UNSUPPORTED_JWT(401, 401, "지원되지 않는 JWT 토큰입니다."),
    EXPIRED_REFRESH_JWT(401, 412, "만료된 Refresh Token 입니다."),
    ILLEGAL_ARGUMENT_JWT(401, 401, "JWT 토큰이 잘못되었습니다."),
    ILLEGAL_REFRESH_JWT(401, 401, "유효하지 않은 Refresh Token 입니다.");

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
