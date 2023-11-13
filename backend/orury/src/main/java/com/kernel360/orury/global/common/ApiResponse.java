package com.kernel360.orury.global.common;

import org.springframework.http.HttpStatus;

public record ApiResponse(
        HttpStatus status,
        Integer code,
        String message,
        Object data
) {
    public static ApiResponse success(Object data) {
        return new ApiResponse(HttpStatus.OK, 200, "OK", data);
    }

    public static ApiResponse error(HttpStatus status, int code, String message) {
        return new ApiResponse(status, code, message, null);
    }
}