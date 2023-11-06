package com.kernel360.orury.global.constants;

import lombok.Getter;

@Getter
public enum Constant {
    ADMIN("admin"),
    SYSTEM("system"),
    ROLE_USER("ROLE_USER"),
    ;

    private final String message;

    Constant(String message) {
        this.message = message;
    }

}
