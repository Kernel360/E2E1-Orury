package com.kernel360.orury.config;

import com.kernel360.orury.config.jwt.*;
import com.kernel360.orury.config.jwt.admin.AdminAuthenticationFilter;
import com.kernel360.orury.config.jwt.admin.AdminAuthorizationFilter;
import com.kernel360.orury.domain.user.db.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CorsFilter;

import java.util.List;
import java.util.Map;

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
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	@Value("${token.cookie.name}")
	private String cookieName;
	@Value("${token.cookie.refresh-name}")
	private String cookieRefreshName;
	@Value("${jwt.access-validity}")
	private String accessCookieMaxAge;
	@Value("${jwt.refresh-validity}")
	private String refreshCookieMaxAge;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Order(1)
	public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
		AuthenticationManager authenticationManager = (AuthenticationManager)http.getSharedObjects();
		http
			.requestMatchers(requestMatchers -> requestMatchers
				.antMatchers("/admin/**")
			)
			.csrf().disable()
			.httpBasic().disable()

			.addFilterBefore(new AdminAuthenticationFilter(
					authenticationManager,
					tokenProvider,
					cookieName,
					cookieRefreshName,
					accessCookieMaxAge,
					refreshCookieMaxAge
				),
				UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(
				new AdminAuthorizationFilter(
					userRepository,
					tokenProvider,
					cookieName,
					cookieRefreshName,
					accessCookieMaxAge),
				BasicAuthenticationFilter.class
			)
			.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
				.antMatchers("/assets/**").permitAll()
				.anyRequest().authenticated()
			)
			.formLogin()
			.loginPage("/admin/login")
			.usernameParameter("emailAddr")
			.passwordParameter("password")
			.failureUrl("/admin/login")
			.permitAll();
		http
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/admin")
			.invalidateHttpSession(true);
		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
		http
			.requestMatchers(requestMatchers -> requestMatchers
				.antMatchers("/api/**")
			)
			.csrf(csrf -> csrf.disable())

			.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling(exceptionHandling -> exceptionHandling
				.accessDeniedHandler(jwtAccessDeniedHandler)
				.authenticationEntryPoint(jwtAuthenticationEntryPoint)
			)

			.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
				.mvcMatchers(SWAGGER.toArray(new String[0])).permitAll()
				.antMatchers("/api/hello", "/api/auth/login", "/api/user/signup").permitAll()
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