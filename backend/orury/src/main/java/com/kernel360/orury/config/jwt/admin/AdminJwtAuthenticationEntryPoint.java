package com.kernel360.orury.config.jwt.admin;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AdminJwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException authException) throws IOException {
		// 유효한 자격증명을 제공하지 않고 접근하려 할때 redirect
		response.sendRedirect("/admin/login");
	}
}
