package com.kernel360.orury.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kernel360.orury.domain.user.model.UserRegisterDto;
import com.kernel360.orury.domain.user.model.UserResponseDto;
import com.kernel360.orury.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("")
	public ResponseEntity<UserResponseDto> signup(
		@RequestBody UserRegisterDto userRegisterDto
	) {
		var dto = userService.signup(userRegisterDto);
		return ResponseEntity.ok(dto);
	}

}
