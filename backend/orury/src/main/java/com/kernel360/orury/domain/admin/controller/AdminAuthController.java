package com.kernel360.orury.domain.admin.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kernel360.orury.config.jwt.TokenProvider;
import com.kernel360.orury.domain.user.model.TokenDto;

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
		accessTokenCookie.setMaxAge(Integer.parseInt(accessCookieMaxAge));
		accessTokenCookie.setPath("/");
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setMaxAge(Integer.parseInt(refreshCookieMaxAge));
		refreshTokenCookie.setPath("/");

		response.addCookie(accessTokenCookie);
		response.addCookie(refreshTokenCookie);

		return "redirect:/admin/board";
	}

	@PostMapping("/refreshToken")
	public ResponseEntity<TokenDto> refreshAccessToken(
		@RequestHeader("Refresh-Token") String refreshTokenHeader
	) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		//서버 측에서 리프레시 토큰 검증
		String refreshToken = refreshTokenHeader.replace("Bearer ", "");
		if (tokenProvider.validateRefreshToken(refreshToken)) {
			String newAccessToken = tokenProvider.createAccessToken(authentication);
			var tokenDto = TokenDto.builder()
				.accessToken(newAccessToken)
				.refreshToken(refreshToken)
				.build();
			return ResponseEntity.ok(tokenDto);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}
}

