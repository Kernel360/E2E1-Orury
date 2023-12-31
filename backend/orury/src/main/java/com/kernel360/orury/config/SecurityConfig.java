package com.kernel360.orury.config;

import com.kernel360.orury.config.jwt.*;
import com.kernel360.orury.config.jwt.admin.AdminJwtAuthenticationEntryPoint;
import com.kernel360.orury.config.jwt.admin.AdminJwtFilter;
import com.kernel360.orury.domain.user.db.UserRepository;
import com.kernel360.orury.domain.user.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 author : aqrms
 date : 2023/11/2
 description : @EnableMethodSecurity는 추후 컨트롤러에서 API메서드 단위로 권한을 적용(@PreAuthorize)하기 위함
 */
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private static final List<String> SWAGGER = List.of(
		"/swagger-ui.html",
		"/swagger-ui/**",
		"/v3/api-docs/**",
		"/api/notify/**" // sse 테스트를 위해 임시로
	);
	private final TokenProvider tokenProvider;
	private final UserRepository userRepository;
	private final CorsFilter corsFilter;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final AdminJwtAuthenticationEntryPoint adminJwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	private final CustomUserDetailsService customUserDetailsService;
	@Value("${jwt.cookie-name}")
	private String cookieName;
	@Value("${jwt.refresh-cookie-name}")
	private String cookieRefreshName;
	@Value("${jwt.access-validity}")
	private String accessCookieMaxAge;
	@Value("${jwt.refresh-validity}")
	private String refreshCookieMaxAge;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(
		HttpSecurity http,
		BCryptPasswordEncoder passwordEncoder
	) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
			.userDetailsService(customUserDetailsService)
			.passwordEncoder(passwordEncoder)
			.and()
			.build();
	}

	@Bean
	@Order(1)
	public SecurityFilterChain adminFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws
		Exception {
		http
			.requestMatchers(requestMatchers -> requestMatchers
				.antMatchers("/admin/**")  // /api로 시작하는 요청을 처리
			)
			.csrf(csrf -> csrf.disable())

			.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling(exceptionHandling -> exceptionHandling
				.accessDeniedHandler(jwtAccessDeniedHandler)
				.authenticationEntryPoint(adminJwtAuthenticationEntryPoint)
			)

			.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
				.mvcMatchers(SWAGGER.toArray(new String[0])).permitAll()
				.antMatchers("/admin/login").permitAll()
				.anyRequest().authenticated()
			)

			// 세션을 사용하지 않기 때문에 STATELESS로 설정
			.sessionManagement(sessionManagement ->
				sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			// JWT필터 적용
			.addFilterBefore(new AdminJwtFilter(tokenProvider, cookieName, cookieRefreshName, accessCookieMaxAge,
				refreshCookieMaxAge), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
		http
			.requestMatchers(requestMatchers -> requestMatchers
				.antMatchers("/api/**")  // /api로 시작하는 요청을 처리
			)
			.csrf(csrf -> csrf.disable())

			.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling(exceptionHandling -> exceptionHandling
				.accessDeniedHandler(jwtAccessDeniedHandler)
				.authenticationEntryPoint(jwtAuthenticationEntryPoint)
			)

			.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
				.mvcMatchers(SWAGGER.toArray(new String[0])).permitAll()
				.antMatchers("/assets/**").permitAll()
				.antMatchers("/admin/**").permitAll()
				.antMatchers("/api/hello", "/api/auth/login", "/api/user/signup", "/api/board/notice").permitAll()
				.anyRequest().authenticated()
			)

			// 세션을 사용하지 않기 때문에 STATELESS로 설정
			.sessionManagement(sessionManagement ->
				sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			// JWT필터 적용
			.apply(new JwtSecurityConfig(tokenProvider))
		;
		return http.build();
	}

}