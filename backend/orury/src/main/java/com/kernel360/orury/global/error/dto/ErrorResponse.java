package com.kernel360.orury.global.error.dto;

import com.kernel360.orury.global.error.code.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponse {
//    private int status;
    private int code;
    private String message;

    @Builder
    public ErrorResponse(String message, int code, int status) {
//        this.status = status;
        this.code = code;
        this.message = message;
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
//            .status(errorCode.getStatus())
            .code(errorCode.getCode())
            .message(errorCode.getMessage())
            .build();
    }
}
