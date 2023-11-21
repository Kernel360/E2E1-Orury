package com.kernel360.orury.config.jwt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kernel360.orury.global.constants.Constant;
import com.kernel360.orury.global.exception.RefreshExpiredJwtException;
import com.kernel360.orury.global.message.errors.ErrorMessages;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
	private final TokenProvider tokenProvider;

	public JwtFilter(TokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}

	//토큰의 인증정보를 SecurityContext에 저장
	@Override
	public void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws
		IOException,
		ServletException {

		String authorizationHeader = servletRequest.getHeader(Constant.AUTHORIZATION.getMessage());
		String refreshHeader = servletRequest.getHeader(Constant.REFRESH_HEADER.getMessage());

		// 헤더가 비었으면 user-password 필터로 이동
		if (!StringUtils.hasText(authorizationHeader) && !StringUtils.hasText(refreshHeader)){
			filterChain.doFilter(servletRequest, servletResponse);
			return ;
		}

		String refreshToken ="";
		if(StringUtils.hasText(refreshHeader)) {
			refreshToken = resolveToken(refreshHeader);
		}

		String accessToken = resolveToken(authorizationHeader);

		if (StringUtils.hasText(refreshToken)){
			try {
				tokenProvider.validateToken(refreshToken);
				assert accessToken != null;
				Authentication authentication = tokenProvider.getAuthentication(accessToken);
				SecurityContextHolder.getContext().setAuthentication(authentication);
				logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), servletRequest.getRequestURI());
			}catch(ExpiredJwtException e){
				RefreshExpiredJwtException refreshExpiredJwtException = new RefreshExpiredJwtException("Refresh Expired Jwt Exception");
				servletRequest.setAttribute("exception", refreshExpiredJwtException);
			}catch(Exception e){
				servletRequest.setAttribute("exception", e);
			}
//			} catch (ExpiredJwtException e) {
//				// Create an ObjectMapper
//				ObjectMapper objectMapper = new ObjectMapper();
//				// Create a JSON object
//				JsonNode errorJson = objectMapper.createObjectNode()
//						.put("error", ErrorMessages.EXPIRED_REFRESH_JWT.getMessage())
//						.put("errorCode", 402);
//				// Convert JSON object to string
//				tempResponse(objectMapper, errorJson, servletResponse);
//				return;
//			}
		}else {
			try {
				tokenProvider.validateToken(accessToken);
				Authentication authentication = tokenProvider.getAuthentication(accessToken);
				SecurityContextHolder.getContext().setAuthentication(authentication);
				logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), servletRequest.getRequestURI());
			}catch(Exception e){
				servletRequest.setAttribute("exception", e);
			}
//			} catch (Exception e) {
//				// Create an ObjectMapper
//				ObjectMapper objectMapper = new ObjectMapper();
//				// Create a JSON object
//				JsonNode errorJson = objectMapper.createObjectNode()
//						.put("error", ErrorMessages.EXPIRED_JWT.getMessage())
//						.put("errorCode", 401);
//				// Convert JSON object to string
//				tempResponse(objectMapper, errorJson, servletResponse);
//				return;
//			}
		}

		filterChain.doFilter(servletRequest, servletResponse);
	}

	private static void tempResponse(ObjectMapper objectMapper, JsonNode errorJson, HttpServletResponse httpServletResponse) throws IOException {
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
	private String resolveToken(String authorizationHeader) {
		if(StringUtils.hasText(authorizationHeader)) {
			return authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
		}
		else return null;
    }

}
