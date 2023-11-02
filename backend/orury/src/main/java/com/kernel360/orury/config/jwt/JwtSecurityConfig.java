package com.kernel360.orury.config.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
	private final TokenProvider tokenProvider;

	public JwtSecurityConfig(TokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}

	/**
	 author : aqrms
	 date : 2023/11/2
	 description : JwtFilter를 시큐리티 로직(SecurityConfig)에 등록하는 역할
	 */
	@Override
	public void configure(HttpSecurity http) {
		http.addFilterBefore(
			new JwtFilter(tokenProvider),
			UsernamePasswordAuthenticationFilter.class
		);
	}
}