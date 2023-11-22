package com.kernel360.orury.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.validator.internal.metadata.aggregated.rule.OverridingMethodMustNotAlterParameterConstraints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kernel360.orury.global.constants.Constant;
import com.kernel360.orury.global.message.errors.ErrorMessages;

import io.jsonwebtoken.ExpiredJwtException;

public class AdminJwtFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(AdminJwtFilter.class);
	private final TokenProvider tokenProvider;
	@Value("${jwt.cookie-name}")
	private String cookieName;
	@Value("${jwt.refresh-cookie-name}")
	private String cookieRefreshName;
	@Value("${jwt.access-validity}")
	private String accessCookieMaxAge;
	@Value("${jwt.refresh-validity}")
	private String refreshCookieMaxAge;

	public AdminJwtFilter(TokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}

	//토큰의 인증정보를 SecurityContext에 저장
	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws
		IOException,
		ServletException {
		try{
			String accessToken = resolveToken(request, cookieName);
			tokenProvider.validateToken(accessToken);
			Authentication authentication = tokenProvider.getAuthentication(accessToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch	(ExpiredJwtException e) {
			String refreshToken = resolveToken(request, cookieRefreshName);

			}
		}



		}




		// String refreshToken = "";
		// if (StringUtils.hasText(refreshHeader)) {
		// 	refreshToken = resolveToken(refreshHeader);
		// }
		//
		// String accessToken = resolveToken(authorizationHeader);
		//
		// if (StringUtils.hasText(refreshToken)) {
		// 	try {
		// 		tokenProvider.validateToken(refreshToken);
		// 	} catch (ExpiredJwtException e) {
		// 		// Create an ObjectMapper
		// 		ObjectMapper objectMapper = new ObjectMapper();
		// 		// Create a JSON object
		// 		JsonNode errorJson = objectMapper.createObjectNode()
		// 			.put("error", ErrorMessages.EXPIRED_REFRESH_JWT.getMessage())
		// 			.put("errorCode", 402);
		// 		// Convert JSON object to string
		// 		tempResponse(objectMapper, errorJson, servletResponse);
		// 		return;
		// 	}
		// } else {
		// 	try {
		// 		tokenProvider.validateToken(accessToken);
		// 	} catch (ExpiredJwtException e) {
		// 		// Create an ObjectMapper
		// 		ObjectMapper objectMapper = new ObjectMapper();
		// 		// Create a JSON object
		// 		JsonNode errorJson = objectMapper.createObjectNode()
		// 			.put("error", ErrorMessages.EXPIRED_JWT.getMessage())
		// 			.put("errorCode", 401);
		// 		// Convert JSON object to string
		// 		tempResponse(objectMapper, errorJson, servletResponse);
		// 		return;
		// 	}
		// }
		//
		// assert accessToken != null;
		// Authentication authentication = tokenProvider.getAuthentication(accessToken);
		// SecurityContextHolder.getContext().setAuthentication(authentication);
		// logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(),
		// 	servletRequest.getRequestURI());
		//
		// filterChain.doFilter(servletRequest, servletResponse);
	}

	private static void tempResponse(ObjectMapper objectMapper, JsonNode errorJson,
		HttpServletResponse httpServletResponse) throws IOException {
		String jsonString = objectMapper.writeValueAsString(errorJson);
		// Set status code and headers
		httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
		httpServletResponse.setContentType("application/json; charset=UTF-8");
		httpServletResponse.setCharacterEncoding("UTF-8");
		// Write JSON string to the response body
		httpServletResponse.getWriter().write(jsonString);
		httpServletResponse.getWriter().flush();
	}

	//리퀘스트 헤더에서 토큰 정보를 꺼내온다
	private String resolveToken(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookieName.equals(cookie.getName())){  // 쿠키 이름에 따라서 변경
				return cookie.getValue();
			}
		}
		return null;

}
