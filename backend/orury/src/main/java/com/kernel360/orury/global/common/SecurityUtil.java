package com.kernel360.orury.global.common;

import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class SecurityUtil {

	public static Optional<String> getCurrentUsername() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null) {
			log.debug("Security Context에 인증 정보가 없습니다.");
			return Optional.empty();
		}

		String username = null;
		if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
			username = springSecurityUser.getUsername();
		} else if (authentication.getPrincipal() instanceof String) {
			username = (String)authentication.getPrincipal();
		}

		return Optional.ofNullable(username);
	}
}
