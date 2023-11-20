package com.kernel360.orury.global.constants;

import lombok.Getter;

@Getter
public enum Constant {
    ADMIN("admin"),
    SYSTEM("system"),
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN"),


    // userInfo 등 하드 코딩된 것 상수로
    USERID("userId"),

    // header 종류
    AUTHORIZATION("Authorization"),
    REFRESH_HEADER("Refresh-token")
    ;

    private final String message;

    Constant(String message) {
        this.message = message;
    }

}
