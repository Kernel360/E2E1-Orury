package com.kernel360.orury.config.jwt.admin;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kernel360.orury.config.jwt.TokenProvider;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AdminJwtFilter extends OncePerRequestFilter {

	private final TokenProvider tokenProvider;
	private final String cookieName;
	private final String cookieRefreshName;
	private final String accessCookieMaxAge;
	private final String refreshCookieMaxAge;

	//토큰의 인증정보를 SecurityContext에 저장
	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws
		IOException,
		ServletException {
		try {
			String accessToken = resolveToken(request, cookieName);
			tokenProvider.validateToken(accessToken);
			Authentication authentication = tokenProvider.getAuthentication(accessToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (ExpiredJwtException e) {
			try {
				String accessToken = resolveToken(request, cookieName);
				String refreshToken = resolveToken(request, cookieRefreshName);
				tokenProvider.validateToken(refreshToken);
				Authentication authentication = tokenProvider.getAuthentication(accessToken);
				String newAccessToken = tokenProvider.createAccessToken(authentication);
				Cookie accessTokenCookie = new Cookie(cookieName, newAccessToken);

				accessTokenCookie.setHttpOnly(true);
				accessTokenCookie.setMaxAge(60 * 60 * 24);
				accessTokenCookie.setPath("/");

				response.addCookie(accessTokenCookie);

				Authentication newAuthentication = tokenProvider.getAuthentication(newAccessToken);
				SecurityContextHolder.getContext().setAuthentication(newAuthentication);
			} catch (ExpiredJwtException ex) {
				clearAuthenticationAndCookie(response);
			}
		} catch (IllegalArgumentException ignored) {
		}
		filterChain.doFilter(request, response);
	}

	//리퀘스트 헤더에서 토큰 정보를 꺼내온다
	private String resolveToken(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookieName.equals(cookie.getName())) {  // 쿠키 이름에 따라서 변경
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	private void clearAuthenticationAndCookie(HttpServletResponse response) {
		SecurityContextHolder.clearContext();
		Cookie accessCookie = new Cookie(cookieName, null);
		accessCookie.setPath("/");
		accessCookie.setHttpOnly(true);
		accessCookie.setMaxAge(0);
		response.addCookie(accessCookie);
		Cookie refreshCookie = new Cookie(cookieRefreshName, null);
		refreshCookie.setPath("/");
		refreshCookie.setHttpOnly(true);
		refreshCookie.setMaxAge(0);
		response.addCookie(refreshCookie);
	}
}

