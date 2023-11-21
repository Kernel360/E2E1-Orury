package com.kernel360.orury.config.jwt.admin;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kernel360.orury.config.jwt.TokenProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AdminAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final TokenProvider tokenProvider;
	private final String cookieName;
	private final String cookieRefreshName;
	private final String accessCookieMaxAge;
	private final String refreshCookieMaxAge;

	@Override
	public Authentication attemptAuthentication(
		HttpServletRequest request,
		HttpServletResponse response
	) throws AuthenticationException {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
			request.getParameter("userEmail"),
			request.getParameter("password"),
			new ArrayList<>()
		);
		return authenticationManager.authenticate(authenticationToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) throws IOException, ServletException {
		String accessToken = tokenProvider.createAccessToken(authResult);
		String refreshToken = tokenProvider.createRefreshToken(authResult);
		tokenProvider.storeToken(refreshToken);

		addAuthenticationCookie(request, response, accessToken, true);
		addAuthenticationCookie(request, response, refreshToken, false);

		String preLoginUrl = request.getParameter("preLoginUrl");
		if (preLoginUrl != null && !preLoginUrl.isBlank()) {
			response.sendRedirect(preLoginUrl);
		} else {
			response.sendRedirect("/admin");
		}
	}

	private void addAuthenticationCookie(HttpServletRequest request, HttpServletResponse response, String token,
		boolean isAccessToken) {
		Cookie authCookie;
		int maxAge;

		if (isAccessToken) {
			authCookie = new Cookie(cookieName, token);
			maxAge = Integer.parseInt(accessCookieMaxAge) * 1000;
		} else {
			authCookie = new Cookie(cookieRefreshName, token);
			maxAge = Integer.parseInt(refreshCookieMaxAge) * 1000;
		}
		authCookie.setMaxAge(maxAge);
		authCookie.setPath("/");
		authCookie.setHttpOnly(true);
		authCookie.setSecure(request.isSecure());
		response.addCookie(authCookie);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException failed) throws IOException, ServletException {
		response.sendRedirect("/admin/login");
	}
}
