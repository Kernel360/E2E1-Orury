package com.kernel360.orury.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kernel360.orury.global.common.ApiResponse;
import com.kernel360.orury.global.message.errors.ErrorMessages;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler{
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        // 여기에 원하는 JSON 응답을 작성합니다.
        // 예시로는 ApiResponse를 사용했습니다.
        ApiResponse apiResponse = ApiResponse.error(HttpStatus.UNAUTHORIZED, 401, ErrorMessages.EXPIRED_JWT.getMessage());
        new ObjectMapper().writeValue(response.getWriter(), apiResponse);
    }
}