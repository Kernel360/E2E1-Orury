package com.kernel360.orury.domain.user.controller;

import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.kernel360.orury.config.jwt.JwtFilter;
import com.kernel360.orury.config.jwt.TokenProvider;
import com.kernel360.orury.domain.user.model.LoginDto;
import com.kernel360.orury.domain.user.model.TokenDto;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final TokenProvider tokenProvider;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;

	public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
		this.tokenProvider = tokenProvider;
		this.authenticationManagerBuilder = authenticationManagerBuilder;
	}

	@PostMapping("/login")
	public ResponseEntity<TokenDto> authenticate(@Valid @RequestBody LoginDto loginDto) {

		UsernamePasswordAuthenticationToken authenticationToken =
			new UsernamePasswordAuthenticationToken(loginDto.getEmailAddr(), loginDto.getPassword());

		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String accessToken = tokenProvider.createAccessToken(authentication);
		String refreshToken = tokenProvider.createRefreshToken(authentication);
		tokenProvider.storeToken(refreshToken);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + accessToken);
		httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + refreshToken);

		var tokenDto = TokenDto.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build();

		return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);
	}

	@PostMapping("/refreshToken")
	public ResponseEntity<TokenDto> refreshAccessToken(
			@RequestHeader("Authorization") String refreshTokenHeader
	){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		//서버 측에서 리프레시 토큰 검증
		String refreshToken = refreshTokenHeader.replace("Bearer ", "");
		if(tokenProvider.validateRefreshToken(refreshToken)){
			String newAccessToken = tokenProvider.createAccessToken(authentication);
			var tokenDto = TokenDto.builder()
					.accessToken(newAccessToken)
					.refreshToken(refreshToken)
					.build();
			return ResponseEntity.ok(tokenDto);
		}
		else{
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}
}

