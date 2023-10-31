package com.kernel360.orury.domain.user.controller;

import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kernel360.orury.domain.user.db.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {

	private final UserRepository userRepository;

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@ResponseBody
	@GetMapping("/main/1")
	public String getMain1() {

		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();

		String password = (authentication.getCredentials() == null) ?
			"보안을 위한 eraseCredentialsAfterAuthentication 정책에 의해 성공적으로 null 처리 되었습니다." :
			authentication.getCredentials().toString() + "입니다.";

		return "안녕하세요, " + authentication.getName() + "님!<br>" +
			"귀하의 비밀번호는 " + password;
	}
}
