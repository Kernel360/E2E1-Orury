package com.kernel360.orury.global.exception;

public class RefreshExpiredJwtException extends RuntimeException{
    public RefreshExpiredJwtException(String message){
        super(message);
    }
}
