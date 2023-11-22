package com.kernel360.orury.config.jwt.admin;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kernel360.orury.config.jwt.TokenProvider;
import com.kernel360.orury.domain.user.db.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AdminAuthorizationFilter extends OncePerRequestFilter {

	private final UserRepository userRepository;
	private final TokenProvider tokenProvider;
	private final String cookieName;
	private final String cookieRefreshName;
	private final String accessCookieMaxAge;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain chain
	) throws IOException, ServletException {

		Optional<String> accessToken = extractTokenFromCookie(request, cookieName);
		Optional<String> refreshToken = extractTokenFromCookie(request, cookieRefreshName);

		if (accessToken.isPresent()) {
			processAccessToken(accessToken.get(), response);
		} else {
			refreshToken.ifPresentOrElse(
				presentRefreshToken -> processRefreshToken(accessToken.get(), presentRefreshToken, response),
				() -> clearAuthenticationAndCookie(response)
			);
		}

		chain.doFilter(request, response);
	}

	private void processAccessToken(String accessToken, HttpServletResponse response) {
		if (tokenProvider.isTokenExpired(accessToken)) {
			clearAuthenticationAndCookie(response);
		} else {
			validateAndAuthenticateToken(response, accessToken);
		}
	}

	private void processRefreshToken(String accessToken, String refreshToken, HttpServletResponse response) {
		if (!tokenProvider.isTokenExpired(refreshToken)) {
			try {
				Authentication authentication = tokenProvider.getAuthentication(accessToken);
				String newAccessToken = tokenProvider.createAccessToken(authentication);
				addAuthenticationCookie(response, newAccessToken);
				validateAndAuthenticateToken(response, newAccessToken);
			} catch (Exception e) {
				clearAuthenticationAndCookie(response);
			}
		} else {
			clearAuthenticationAndCookie(response);
		}
	}

	private Optional<String> extractTokenFromCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookieName.equals(cookie.getName())) {
					return Optional.of(cookie.getValue());
				}
			}
		}
		return Optional.empty();
	}

	private void addAuthenticationCookie(HttpServletResponse response, String token) {
		Cookie accessTokenCookie = new Cookie(cookieName, token);
		accessTokenCookie.setHttpOnly(true);
		accessTokenCookie.setPath("/");
		accessTokenCookie.setMaxAge(Integer.parseInt(accessCookieMaxAge) * 1000);
		response.addCookie(accessTokenCookie);
	}

	private void validateAndAuthenticateToken(HttpServletResponse response, String token) {
		try {
			if (tokenProvider.validateToken(token)) {
				Authentication authentication = tokenProvider.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			// String userEmail = tokenProvider.getUserEmail(token);
			// userRepository.findActiveUserByUserEmail(userEmail).ifPresent(user -> {
			// 	Authentication auth = getAuthentication(user);
			// 	SecurityContextHolder.getContext().setAuthentication(auth);
			// });
		} catch (Exception e) {
			clearAuthenticationAndCookie(response);
		}
	}

	// private Authentication getAuthentication(User user) {
	// 	return new UsernamePasswordAuthenticationToken(
	// 		user.getUserName(),
	// 		null,
	// 		Collections.singletonList(
	// 			new SimpleGrantedAuthority("ROLE_" + user.getUserPermissionType().getValue().toUpperCase()))
	// 	);
	// }

	private void clearAuthenticationAndCookie(HttpServletResponse response) {
		SecurityContextHolder.clearContext();
		Cookie cookie = new Cookie(cookieName, null);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
}
