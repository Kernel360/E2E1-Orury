package com.kernel360.orury.global.constants;

import lombok.Getter;

@Getter
public enum Constant {
    ADMIN("admin")
    ;

    private final String message;

    Constant(String message) {
        this.message = message;
    }

}
