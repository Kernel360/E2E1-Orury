package com.kernel360.orury.global.error;

import com.kernel360.orury.global.common.ApiResponse;
import com.kernel360.orury.global.error.code.ServerErrorCode;
import com.kernel360.orury.global.error.dto.ErrorResponse;
import com.kernel360.orury.global.error.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(ErrorResponse.of(e.getErrorCode()));
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse> handleException(Exception e){
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, 500, ServerErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
//    }

}
