package com.kernel360.orury.config.jwt;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private final HandlerExceptionResolver resolver;
	public JwtAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
		this.resolver = resolver;
	}

	@Override
	public void commence(HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException authException
	) {
		// JwtFilter에서 처리한 밣생한 예외를 받는 부분
		if(request.getAttribute("exception") != null) {
			resolver.resolveException(request, response, null, (Exception) request.getAttribute("exception"));
		}

		// jwt token에서 정의한 에러 외 filter에서 발생한 예외를 global handler로 보냄
		else {
			resolver.resolveException(request, response, null, authException);
		}
		// 유효한 자격증명을 제공하지 않고 접근하려 할때 401
//		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}
}
