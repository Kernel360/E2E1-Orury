package com.kernel360.orury.domain.admin.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kernel360.orury.config.jwt.TokenProvider;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {
	private final TokenProvider tokenProvider;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	@Value("${jwt.cookie-name}")
	private String cookieName;
	@Value("${jwt.refresh-cookie-name}")
	private String cookieRefreshName;
	@Value("${jwt.access-validity}")
	private String accessCookieMaxAge;
	@Value("${jwt.refresh-validity}")
	private String refreshCookieMaxAge;

	public AdminAuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
		this.tokenProvider = tokenProvider;
		this.authenticationManagerBuilder = authenticationManagerBuilder;
	}

	@GetMapping("/login")
	public String login() {
		return "pages-login";
	}

	@PostMapping("/login")
	public String authenticate(@Valid @RequestParam String emailAddr, String password,
		HttpServletResponse response) {

		UsernamePasswordAuthenticationToken authenticationToken =
			new UsernamePasswordAuthenticationToken(emailAddr, password);

		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String accessToken = tokenProvider.createAccessToken(authentication);
		String refreshToken = tokenProvider.createRefreshToken(authentication);
		tokenProvider.storeToken(refreshToken);

		Cookie accessTokenCookie = new Cookie(cookieName, accessToken);
		Cookie refreshTokenCookie = new Cookie(cookieRefreshName, refreshToken);

		accessTokenCookie.setHttpOnly(true);
		accessTokenCookie.setMaxAge(Integer.parseInt(accessCookieMaxAge) * 1000);
		accessTokenCookie.setPath("/");
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setMaxAge(Integer.parseInt(refreshCookieMaxAge));
		refreshTokenCookie.setPath("/");

		response.addCookie(accessTokenCookie);
		response.addCookie(refreshTokenCookie);

		return "redirect:/admin/board";
	}

	@PostMapping("/logout")
	public String logout(HttpServletResponse response) {
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

		return "redirect:/admin/login";
	}
}

