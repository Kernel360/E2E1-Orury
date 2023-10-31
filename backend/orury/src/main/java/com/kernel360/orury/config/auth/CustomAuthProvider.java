package com.kernel360.orury.config.auth;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CustomAuthProvider implements AuthenticationProvider {

	private final PrincipalDetailsService principalDetailsService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String username = authentication.getName();
		String password = authentication.getCredentials().toString();

		PrincipalDetails principalDetails = principalDetailsService.loadUserByUsername(username);

		return checkPassword(principalDetails, password, bCryptPasswordEncoder);

	}

	private Authentication checkPassword(PrincipalDetails principalDetails, String rawPassword,
		BCryptPasswordEncoder bCryptPasswordEncoder) {
		if (bCryptPasswordEncoder.matches(rawPassword, principalDetails.getPassword())) {
			return new UsernamePasswordAuthenticationToken(
				principalDetails.getUsername(),
				principalDetails.getPassword(),
				principalDetails.getAuthorities());
		}

		throw new BadCredentialsException("배드 크레덴셜");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class
			.isAssignableFrom(authentication);
	}
}
