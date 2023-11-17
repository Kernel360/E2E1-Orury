package com.kernel360.orury.config.jwt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kernel360.orury.global.exception.TokenExpiredException;
import com.kernel360.orury.global.message.errors.ErrorMessages;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

	private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
	public static final String AUTHORIZATION_HEADER = "Authorization";
	private TokenProvider tokenProvider;

	public JwtFilter(TokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}

	//토큰의 인증정보를 SecurityContext에 저장
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws
		IOException,
		ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
		String jwt = resolveToken(httpServletRequest);
		String requestURI = httpServletRequest.getRequestURI();

		try{
			if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
				Authentication authentication = tokenProvider.getAuthentication(jwt);
				SecurityContextHolder.getContext().setAuthentication(authentication);
				logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
			}
		} catch (ExpiredJwtException e) {

			HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
			String errorMessage = ErrorMessages.EXPIRED_JWT.getMessage();
			// Create an ObjectMapper
			ObjectMapper objectMapper = new ObjectMapper();
			// Create a JSON object
			JsonNode errorJson = objectMapper.createObjectNode()
					.put("error", errorMessage)
					.put("errorCode", HttpStatus.UNAUTHORIZED.value());
			// Convert JSON object to string
			String jsonString = objectMapper.writeValueAsString(errorJson);
			// Set status code and headers
			httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
			httpServletResponse.setContentType("application/json; charset=UTF-8");
			httpServletResponse.setCharacterEncoding("UTF-8");
			// Write JSON string to the response body
			httpServletResponse.getWriter().write(jsonString);
			httpServletResponse.getWriter().flush();
			return;
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}

	//리퀘스트 헤더에서 토큰 정보를 꺼내온다
	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}

		return null;
	}
}
